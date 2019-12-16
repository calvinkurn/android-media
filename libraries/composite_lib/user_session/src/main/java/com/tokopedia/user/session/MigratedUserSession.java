package com.tokopedia.user.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.tokopedia.user.session.util.EncoderDecoder;

import static com.tokopedia.user.session.Constants.ACCESS_TOKEN;
import static com.tokopedia.user.session.Constants.LOGIN_SESSION;

public class MigratedUserSession {

    protected Context context;

    public MigratedUserSession(Context context) {
        this.context = context;
    }

    protected String getString(String prefName, String keyName, String defValue) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return sharedPrefs.getString(keyName, defValue);
    }

    protected void nullString(String prefName, String keyName) {
        setString(prefName, keyName, null);
    }

    protected void setString(String prefName, String keyName, String value) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(keyName, value);
        editor.apply();
    }

    protected String getAndTrimOldString(String prefName, String keyName, String defValue) {

        String oldprefName = EncoderDecoder.Decrypt(prefName, UserSession.KEY_IV);
        String oldKeyName = EncoderDecoder.Decrypt(keyName, UserSession.KEY_IV);

        String oldValue = getString(oldprefName, oldKeyName, defValue);

        if(!oldValue.equals(defValue)){
            nullString(oldprefName, oldKeyName);
            setString(prefName, keyName, oldValue);

            return oldValue;
        }

        SharedPreferences sharedPrefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return sharedPrefs.getString(keyName, defValue);
    }

    protected boolean getBoolean(String prefName, String keyName, boolean defValue) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean(keyName, defValue);
    }

    protected void setBoolean(String prefName, String keyName, boolean value) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(keyName, value);
        editor.apply();
    }

    protected boolean getAndTrimOldBoolean(String prefName, String keyName, boolean defValue) {

        String oldprefName = EncoderDecoder.Decrypt(prefName, UserSession.KEY_IV);
        String oldKeyName = EncoderDecoder.Decrypt(keyName, UserSession.KEY_IV);

        boolean oldValue = getBoolean(oldprefName, oldKeyName, defValue);

        if( oldValue != defValue){
            nullString(oldprefName, oldKeyName);
            setBoolean(prefName, keyName, oldValue);

            return oldValue;
        }

        SharedPreferences sharedPrefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean(keyName, defValue);
    }




}
