package com.example.akamai_bot_lib;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.util.Pair;

import androidx.test.runner.AndroidJUnit4;


import com.tokopedia.akamai_bot_lib.UtilsKt;
import com.tokopedia.akamai_bot_lib.test.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().getContext();

        assertEquals("com.tokopedia.akamai_bot_lib.test", appContext.getPackageName());
    }

    @Test
    public void regex() {

        Context context = androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().getContext();
        Resources resources = context.getResources();

        String input = GraphqlHelper.loadRawString(resources, R.raw.mutation_af_register_username);
        assertTrue(UtilsKt.getMutation(input, "RegisterUsername"));
    }

    @Test
    public void regex2() {

        Context context = androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().getContext();
        Resources resources = context.getResources();

        String input = GraphqlHelper.loadRawString(resources, R.raw.mutation_af_register_username);
        List<String> any = UtilsKt.getQueryListFromQueryString(input);
        Log.d(this.getClass().getName(), any.toString());
        assertTrue(UtilsKt.getMutation(input, "RegisterUsername"));
        assertEquals(1,any.size());
        assertTrue(any.get(0).equalsIgnoreCase("bymeRegisterAffiliateName"));
    }

    @Test
    public void regex3() {

        Context context = androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().getContext();
        Resources resources = context.getResources();

        String input = GraphqlHelper.loadRawString(resources, R.raw.mutation_register);
        List<String> any = UtilsKt.getQueryListFromQueryString(input);
        Log.d(this.getClass().getName(), any.toString());
        assertEquals(1,any.size());
        assertTrue(any.get(0).equalsIgnoreCase("register"));
    }

    @Test
    public void getQueryTest() {

        String input = "[\n  {\r\n    \"operationName\": null,\r\n    \"query\": \"mutation login_email($grant_type: String!, $username: String!, $password: String!, $supported:String!) {\\\n  login_token(input: {grant_type: $grant_type, username: $username, password: $password, supported: $supported}) {\\\n    acc_sid\\\n    access_token\\\n    expires_in\\\n    refresh_token\\\n    sid\\\n    token_type\\\n    sq_check\\\n    errors {\\\n      name\\\n      message\\\n    }\\\n    event_code\\\n  }\\\n}\\\n\",\r\n    \"variables\": {\r\n      \"password\": \"am9rYW0zNTQ=d09e\",\r\n      \"grant_type\": \"cGFzc3dvcmQ=3594\",\r\n      \"username\": \"ZWxseWt1c0BnbWFpbC5jb20=15ab\",\r\n      \"supported\": \"true\"\r\n    }\r\n  }\r\n]";

        try {
            JSONArray jsonArray = new JSONArray(input);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String query = jsonObject.getString("query");
            List<String> any = UtilsKt.getQueryListFromQueryString(query);
            Log.d(this.getClass().getName(), any.toString());
            assertEquals(1, any.size());
            assertTrue(any.get(0).equalsIgnoreCase("login_token"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Pair<String[], String[]> listRaw(){
        Context context = androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().getContext();
        Resources resources = context.getResources();


        Field[] fields=R.raw.class.getFields();
        String[] names = new String[fields.length];
        String[] allResourcesIds = new String[fields.length];
        for(int count=0; count < fields.length; count++){
            try {
                int resourceID=fields[count].getInt(fields[count]);
                String input = GraphqlHelper.loadRawString(resources, resourceID);
                allResourcesIds[count] = input;
                names[count] = fields[count].getName();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return new Pair<>(allResourcesIds, names) ;
    }

    @Test
    public void getAllQueryTest() {
        String whatitget = null;
        Pair<String[], String[]> pair = listRaw();

        for (int i = 0; i < pair.first.length; i++) {
            String input = pair.first[i];
            String name = pair.second[i];
            try {
                List<String> any = UtilsKt.getQueryListFromQueryString(input);
                System.out.println(any.toString()+" ][ name ][ "+name+" [size] "+any.size());
                Log.d(this.getClass().getName(), any.toString()+" ][ name ][ "+name+" [size] "+any.size());
                whatitget = any.get(0);
            } catch (Exception e) {
                Log.e("EIT", e.toString()+ " ][ "+whatitget);
            }
        }
    }

    @Test
    public void performanceTest(){
        Context context = androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().getContext();
        Resources resources = context.getResources();
        String input = GraphqlHelper.loadRawString(resources, R.raw.tp_gql_user_info);
        for (int i = 0; i < 1_000_000; i++) {
            List<String> any = UtilsKt.getQueryListFromQueryString(input);
            assertEquals(1, any.size());
            assertTrue(any.get(0).equalsIgnoreCase("mf_get_user_info"));
        }
    }


}
