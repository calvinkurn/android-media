package com.tokopedia.user.session;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

public class UserSessionJavaTest {

    @Before
    public void clearAll() {
        UserSessionMap.map.clear();
        Context context = InstrumentationRegistry.getInstrumentation().getContext();

        SharedPreferences sharedPrefs = context.getApplicationContext().getSharedPreferences("LOGIN_SESSION_v2", Context.MODE_PRIVATE);
        sharedPrefs.edit().clear().commit();

        SharedPreferences sharedPrefs2 = context.getApplicationContext().getSharedPreferences("LOGIN_SESSION", Context.MODE_PRIVATE);
        sharedPrefs2.edit().clear().commit();
    }

    @Test
    public void whenGettingValue_oldPreferenceShouldBeRemoved() {
        // save using old key and value : for example login_session
        String keyName = "EMAIL";
        String v = "noiz354@gmail.com";

        Context context = InstrumentationRegistry.getInstrumentation().getContext().getApplicationContext();
        SharedPreferences sharedPrefs = context.getSharedPreferences("LOGIN_SESSION", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(keyName, "noiz354@gmail.com");
        editor.apply();

        UserSession userSession = new UserSession(context);
        String email = userSession.getEmail();
        assertEquals(v, email);

        // old value shold be removed by now
        String oldValue = sharedPrefs.getString("EMAIL", "none");
        assertEquals("none", oldValue);
    }

    @Test
    public void basicGetAndSet() {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();

        UserSession userSession = new UserSession(context);

        assertEquals(userSession.getEmail(), "");

        String email = "noiz354@gmail.com";
        userSession.setEmail(email);

        assertEquals(userSession.getEmail(), email);

        // clear cache
        UserSessionMap.map.clear();

        // still return same value
        String givenEmail = userSession.getEmail();
        assertEquals(givenEmail, email);

        // changes one more time
        email = "foo@gmail.com";
        userSession.setEmail(email);
        givenEmail = userSession.getEmail();
        assertEquals(givenEmail, email);

        // clear cache
        UserSessionMap.map.clear();

        // still return same value
        givenEmail = userSession.getEmail();
        assertEquals(givenEmail, email);
    }

}
