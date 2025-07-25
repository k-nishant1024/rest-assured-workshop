package exercises;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import dataentities.Photo;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WireMockTest(httpPort = 9876)
public class RestAssuredExercises7Test {

    private RequestSpecification requestSpec;

    @BeforeEach
    public void createRequestSpecification() {

        requestSpec = new RequestSpecBuilder().
                setBaseUri("http://localhost").
                setPort(9876).
                setContentType(ContentType.JSON).
                build();
    }

    @Test
    public void fromUserId_findPhotoTitle_expectPariaturSuntEveniet() {

        /*******************************************************
         * Perform a GET to /users and extract the user id
         * that corresponds to the user with username 'Karianne'
         *
         * Hint: use extract().path() and a 'find' filter to do this.
         *
         * Store the user id in a variable of type int
         ******************************************************/
            int userId =
            given().
                spec(requestSpec).
            when().
                    get("/users").
            then().extract().path("find{it.username=='Karianne'}.id");

        /*******************************************************
         * Use a JUnit assertEquals to verify that the userId
         * is equal to 4
         ******************************************************/

        assertEquals(4, userId);

        /*******************************************************
         * Perform a GET to /albums and extract all albums that
         * are associated with the previously retrieved user id.
         *
         * Hint: use extract().path() and a 'findAll' to do this.
         *
         * Store these in a variable of type List<Integer>.
         ******************************************************/
            List<Integer> albumList =
            given().
                spec(requestSpec).
            when().
                    get("/albums").
            then().extract().path(String.format("findAll{it.userId==%d}.id",userId));

        /*******************************************************
         * Use a JUnit assertEquals to verify that the list has
         * exactly 10 items (hint: use the size() method)
         ******************************************************/

        assertEquals(10, albumList.size());

        /*******************************************************
         * Perform a GET to /albums/XYZ/photos, where XYZ is the
         * id of the fifth album in the previously extracted list
         * of album IDs (hint: use get(index) on the list).
         *
         * Deserialize the list of photos returned into a variable
         * of type List<Photo>.
         *
         * Hint: see
         * https://stackoverflow.com/questions/21725093/rest-assured-deserialize-response-json-as-listpojo
         * (the accepted answer should help you solve this one).
         ******************************************************/

            int id= albumList.get(4);
             List<Photo> photoList = Arrays.asList(given().
                spec(requestSpec).
            when().get(String.format("/albums/%d/photos",id)).then().extract().as(Photo[].class));

        /*******************************************************
         * Use a JUnit assertEquals to verify that the title of
         * the 32nd photo in the list equals 'pariatur sunt eveniet'
         *
         * Hint: use the get() method to retrieve an object with a
         * specific index from a List
         ******************************************************/

        assertEquals(photoList.get(31).getTitle(), "pariatur sunt eveniet");
    }
}