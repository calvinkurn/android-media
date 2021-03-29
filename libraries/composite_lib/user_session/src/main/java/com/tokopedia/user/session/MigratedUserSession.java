package com.tokopedia.user.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.tokopedia.user.session.util.EncoderDecoder;

import java.util.HashMap;

import kotlin.Pair;

public class MigratedUserSession {
    public static final boolean IS_ENABLE = false;

    protected Context context;

    public MigratedUserSession(Context context) {
        this.context = context.getApplicationContext();
    }

    private String internalGetString(String prefName, String keyName, String defValue) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return sharedPrefs.getString(keyName, defValue);
    }

    protected long getLong(String prefName, String keyName, long defValue) {
        Pair<String, String> key = new Pair<>(prefName, keyName);
        if (UserSessionMap.getMap().containsKey(key)) {
            try {
                return (Long) UserSessionMap.getMap().get(key);
            } catch (Exception ignored) {
            }
        }
        if (!IS_ENABLE) {
            prefName = EncoderDecoder.Decrypt(prefName, UserSession.KEY_IV);
            keyName = EncoderDecoder.Decrypt(keyName, UserSession.KEY_IV);
        }

        long value = internalGetLong(prefName, keyName, defValue);
        UserSessionMap.getMap().put(key, value);
        return value;
    }

    private long internalGetLong(String prefName, String keyName, long defValue) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return sharedPrefs.getLong(keyName, defValue);
    }

    protected void setLong(String prefName, String keyName, long value) {
        UserSessionMap.getMap().put(new Pair<>(prefName, keyName), value);

        if (!IS_ENABLE) {
            prefName = EncoderDecoder.Decrypt(prefName, UserSession.KEY_IV);
            keyName = EncoderDecoder.Decrypt(keyName, UserSession.KEY_IV);
        }

        internalSetLong(prefName, keyName, value);
    }

    private void internalSetLong(String prefName, String keyName, long value) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putLong(keyName, value);
        editor.apply();
    }

    protected void cleanKey(String prefName, String keyName) {
        UserSessionMap.getMap().remove(new Pair<>(prefName, keyName));
        if (!IS_ENABLE) {
            prefName = EncoderDecoder.Decrypt(prefName, UserSession.KEY_IV);
            keyName = EncoderDecoder.Decrypt(keyName, UserSession.KEY_IV);
        }

        internalCleanKey(prefName, keyName);
    }

    private void internalCleanKey(String prefName, String keyName) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.remove(keyName).apply();
    }

    protected void nullString(String prefName, String keyName) {
        UserSessionMap.getMap().put(new Pair<>(prefName, keyName), null);
        if (!IS_ENABLE) {
            prefName = EncoderDecoder.Decrypt(prefName, UserSession.KEY_IV);
            keyName = EncoderDecoder.Decrypt(keyName, UserSession.KEY_IV);
        }

        internalSetString(prefName, keyName, null);
    }

    protected void setString(String prefName, String keyName, String value) {
        UserSessionMap.getMap().put(new Pair<>(prefName, keyName), value);
        if (!IS_ENABLE) {
            prefName = EncoderDecoder.Decrypt(prefName, UserSession.KEY_IV);
            keyName = EncoderDecoder.Decrypt(keyName, UserSession.KEY_IV);
        }
        internalSetString(prefName, keyName, value);
    }

    private void internalSetString(String prefName, String keyName, String value) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(keyName, value);
        editor.apply();
    }

    protected String getAndTrimOldString(String prefName, String keyName, String defValue) {
        Pair<String, String> key = new Pair<>(prefName, keyName);
        if (UserSessionMap.getMap().containsKey(key)) {
            try {
                return (String) UserSessionMap.getMap().get(key);
            } catch (Exception ignored) {
            }
        }

        String oldprefName = EncoderDecoder.Decrypt(prefName, UserSession.KEY_IV);
        String oldKeyName = EncoderDecoder.Decrypt(keyName, UserSession.KEY_IV);

        String oldValue = internalGetString(oldprefName, oldKeyName, defValue);

        if (!IS_ENABLE) {
            UserSessionMap.getMap().put(key, oldValue);
            return oldValue;
        }

        if (oldValue != null && !oldValue.equals(defValue)) {
            internalCleanKey(oldprefName, oldKeyName);
            internalSetString(prefName, keyName, oldValue);
            UserSessionMap.getMap().put(key, oldValue);
            return oldValue;
        }

        SharedPreferences sharedPrefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        String value = sharedPrefs.getString(keyName, defValue);
        UserSessionMap.getMap().put(key, value);
        return value;
    }

    private boolean internalGetBoolean(String prefName, String keyName, boolean defValue) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean(keyName, defValue);
    }

    protected void setBoolean(String prefName, String keyName, boolean value) {
        UserSessionMap.getMap().put(new Pair<>(prefName, keyName), value);
        if (!IS_ENABLE) {
            prefName = EncoderDecoder.Decrypt(prefName, UserSession.KEY_IV);
            keyName = EncoderDecoder.Decrypt(keyName, UserSession.KEY_IV);
        }
        internalSetBoolean(prefName, keyName, value);
    }

    private void internalSetBoolean(String prefName, String keyName, boolean value) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(keyName, value);
        editor.apply();
    }

    protected boolean getAndTrimOldBoolean(String prefName, String keyName, boolean defValue) {
        Pair<String, String> key = new Pair<>(prefName, keyName);
        if (UserSessionMap.getMap().containsKey(key)) {
            try {
                return (boolean) UserSessionMap.getMap().get(key);
            } catch (Exception ignored) {
            }
        }
        String oldprefName = EncoderDecoder.Decrypt(prefName, UserSession.KEY_IV);
        String oldKeyName = EncoderDecoder.Decrypt(keyName, UserSession.KEY_IV);

        boolean oldValue = internalGetBoolean(oldprefName, oldKeyName, defValue);

        if (!IS_ENABLE) {
            UserSessionMap.getMap().put(key, oldValue);
            return oldValue;
        }

        if (oldValue != defValue) {
            internalCleanKey(oldprefName, oldKeyName);
            internalSetBoolean(prefName, keyName, oldValue);
            UserSessionMap.getMap().put(key, oldValue);
            return oldValue;
        }

        SharedPreferences sharedPrefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        boolean value = sharedPrefs.getBoolean(keyName, defValue);
        UserSessionMap.getMap().put(key, value);
        return value;
    }
}
