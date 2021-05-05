package com.tokopedia.testproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;

import com.tokopedia.user.session.MigratedUserSession;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionMap;
//import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class UserSessionTest {

    @Before
    public void clearAll(){
        UserSessionMap.map.clear();
    }

    @Test
    public void testFresh(){

        // save using old key and value : for example login_session
        String keyName = "EMAIL";

        Context context = InstrumentationRegistry.getInstrumentation().getContext().getApplicationContext();
        SharedPreferences sharedPrefs = context.getSharedPreferences("LOGIN_SESSION", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(keyName, "noiz354@gmail.com");
        editor.apply();


        UserSession userSession = new UserSession(context);
        String email = userSession.getEmail();

        // old value shold be remove by now
        String oldValue = sharedPrefs.getString("EMAIL", "none");
        assertEquals(email,oldValue);

        email = "noiz354@gmail.com";
        userSession.setEmail(email);

        assertEquals(userSession.getEmail(),email);
    }

    @Test
    public void testFresh3(){
        Context context = InstrumentationRegistry.getInstrumentation().getContext();

        SharedPreferences sharedPrefs = context.getApplicationContext().getSharedPreferences("LOGIN_SESSION_v2", Context.MODE_PRIVATE);
        sharedPrefs.edit().clear().commit();

        UserSession userSession = new UserSession(context);
        assertEquals(userSession.getEmail(),"");

        String email = "noiz354@gmail.com";
        userSession.setEmail(email);

        assertEquals(userSession.getEmail(),email);


    }

    @Test
    public void testFresh4(){
        Context context = InstrumentationRegistry.getInstrumentation().getContext();

        // hapus shared pref lama
        SharedPreferences sharedPrefs = context.getApplicationContext().getSharedPreferences("LOGIN_SESSION_v2", Context.MODE_PRIVATE);
        sharedPrefs.edit().clear().commit();


        UserSession userSession = new UserSession(context);

        assertEquals(userSession.getEmail(),"");

        String email = "noiz354@gmail.com";
        userSession.setEmail(email);

        assertEquals(userSession.getEmail(),email);

        // clear cache
        UserSessionMap.map.clear();

        // still return same value
        String givenEmail = userSession.getEmail();
        assertEquals(givenEmail,email);

        // changes one more time
        email = "noiz354_@gmail.com";
        userSession.setEmail(email);
        givenEmail = userSession.getEmail();
        assertEquals(givenEmail,email);

        // clear cache
        UserSessionMap.map.clear();

        // still return same value
        givenEmail = userSession.getEmail();
        assertEquals(givenEmail,email);

        // ini harusnya dihapus
        sharedPrefs = context.getSharedPreferences("LOGIN_SESSION", Context.MODE_PRIVATE);
        String oldValue = sharedPrefs.getString("EMAIL", "none");
        assertEquals("none",oldValue);
    }


    @Test
    public void testFresh2(){
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.tokopedia.user.session", appContext.getPackageName());
//        assertThat(false);
    }
}
