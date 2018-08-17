package com.tokopedia.challenges.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;

import javax.inject.Inject;

/**
 * @author vishal.gupta 06/08/2018
 */
public class IndiSession {
    private static final String INDI_SESSION = "INDI_SESSION";

    private static final String INDI_USER_ID = "INDI_USER_ID";
    private static final String INDI_ACCESS_TOKEN = "INDI_ACCESS_TOKEN";

    private Context context;

    @Inject
    public IndiSession(@ApplicationContext Context context) {
        this.context = context;
    }

    /**
     * GETTER METHOD
     */

    public String getAccessToken() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(INDI_SESSION,
                Context.MODE_PRIVATE);
        return sharedPrefs.getString(INDI_ACCESS_TOKEN, "");
    }

    public String getUserId() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(INDI_SESSION,
                Context.MODE_PRIVATE);
        return sharedPrefs.getString(INDI_USER_ID, "");
    }

    public void setAccessToken(String token) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(INDI_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(INDI_ACCESS_TOKEN, token);
        editor.apply();
    }

    public void setUserId(String userId) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(INDI_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(INDI_USER_ID, userId);
        editor.apply();
    }

    public void clearToken() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(INDI_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(INDI_ACCESS_TOKEN, "");
        editor.apply();
    }

    public void doLogout() {
        try {
            setUserId("");
        } catch (Exception ex) {
        }
    }
}
