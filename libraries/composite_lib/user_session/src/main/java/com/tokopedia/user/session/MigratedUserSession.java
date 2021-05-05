package com.tokopedia.user.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;

import com.tokopedia.user.session.util.EncoderDecoder;

public class MigratedUserSession {
    public static final String suffix = "_v2";
    protected Context context;

    public MigratedUserSession(Context context) {
        this.context = context.getApplicationContext();
    }


    protected long getLong(String prefName, String keyName, long defValue) {
        String newPrefName = String.format("%s%s", prefName, suffix);
        String newKeyName = String.format("%s%s", keyName, suffix);

        // look up from cache
        Pair<String, String> key = new Pair<>(newPrefName, newKeyName);
        if (UserSessionMap.map.containsKey(key)) {
            try {
                Object value = UserSessionMap.map.get(key);
                if (value == null) {
                    return defValue;
                } else {
                    return (long) value;
                }
            } catch (Exception ignored) {
            }
        }

        long oldValue = internalGetLong(prefName, keyName, defValue);

        if (oldValue != defValue) {
            internalCleanKey(prefName, keyName);
            internalSetLong(newPrefName, newKeyName, oldValue);
            UserSessionMap.map.put(key, oldValue);
            return oldValue;
        }

        SharedPreferences sharedPrefs = context.getSharedPreferences(newPrefName, Context.MODE_PRIVATE);
        long value = sharedPrefs.getLong(newKeyName, defValue);
        UserSessionMap.map.put(key, value);
        return value;
    }

    private long internalGetLong(String prefName, String keyName, long defValue) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return sharedPrefs.getLong(keyName, defValue);
    }

    private void internalSetLong(String prefName, String keyName, long value) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putLong(keyName, value);
        editor.apply();
    }

    Pair<String, String> convertToNewKey(String prefName, String keyName){
        return new Pair<>(String.format("%s%s", prefName, suffix), String.format("%s%s", keyName, suffix));
    }

    protected void setLong(String prefName, String keyName, long value) {
        Pair<String, String> newKeys = convertToNewKey(prefName, keyName);
        prefName = newKeys.first;
        keyName = newKeys.second;
        UserSessionMap.map.put(new Pair<>(prefName, keyName), value);
        internalSetLong(prefName, keyName, value);
    }

    protected void cleanKey(String prefName, String keyName) {
        Pair<String, String> newKeys = convertToNewKey(prefName, keyName);
        prefName = newKeys.first;
        keyName = newKeys.second;
        UserSessionMap.map.remove(new Pair<>(prefName, keyName));
        internalCleanKey(prefName, keyName);
    }

    private void internalCleanKey(String prefName, String keyName) {
        Pair<String, String> newKeys = convertToNewKey(prefName, keyName);
        prefName = newKeys.first;
        keyName = newKeys.second;
        SharedPreferences sharedPrefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.remove(keyName).apply();
    }

    protected void nullString(String prefName, String keyName) {
        Pair<String, String> newKeys = convertToNewKey(prefName, keyName);
        prefName = newKeys.first;
        keyName = newKeys.second;
        UserSessionMap.map.put(new Pair<>(prefName, keyName), null);
        internalSetString(prefName, keyName, null);
    }

    private String internalGetString(String prefName, String keyName, String defValue) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return sharedPrefs.getString(keyName, defValue);
    }

    protected void setString(String prefName, String keyName, String value) {
        Pair<String, String> newKeys = convertToNewKey(prefName, keyName);
        prefName = newKeys.first;
        keyName = newKeys.second;
        UserSessionMap.map.put(new Pair<>(prefName, keyName), value);
        value = EncoderDecoder.Encrypt(value, UserSession.KEY_IV);// encrypt string here
        internalSetString(prefName, keyName, value);
    }

    private void internalSetString(String prefName, String keyName, String value) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(keyName, value);
        editor.apply();
    }

    protected String getAndTrimOldString(String prefName, String keyName, String defValue) {
        String newPrefName = String.format("%s%s", prefName, suffix);
        String newKeyName = String.format("%s%s", keyName, suffix);

        Pair<String, String> key = new Pair<>(newPrefName, newKeyName);
        if (UserSessionMap.map.containsKey(key)) {
            try {
                Object value = UserSessionMap.map.get(key);
                if (value == null) {
                    return defValue;
                } else {
                    return (String) value;
                }
            } catch (Exception ignored) {
            }
        }

        String oldValue = internalGetString(prefName, keyName, defValue);

        if (oldValue != null && !oldValue.equals(defValue)) {
            internalCleanKey(prefName, keyName);
            internalSetString(newPrefName, newKeyName, oldValue);
            UserSessionMap.map.put(key, oldValue);
            return oldValue;
        }

        SharedPreferences sharedPrefs = context.getSharedPreferences(newPrefName, Context.MODE_PRIVATE);
        String value = sharedPrefs.getString(newKeyName, defValue);
        value = EncoderDecoder.Decrypt(value, UserSession.KEY_IV);// decrypt here
        UserSessionMap.map.put(key, value);
        return value;
    }

    private boolean internalGetBoolean(String prefName, String keyName, boolean defValue) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean(keyName, defValue);
    }

    private void internalSetBoolean(String prefName, String keyName, boolean value) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(keyName, value);
        editor.apply();
    }

    protected void setBoolean(String prefName, String keyName, boolean value) {
        Pair<String, String> newKeys = convertToNewKey(prefName, keyName);
        prefName = newKeys.first;
        keyName = newKeys.second;
        UserSessionMap.map.put(new Pair<>(prefName, keyName), value);
        internalSetBoolean(prefName, keyName, value);
    }

    protected boolean getAndTrimOldBoolean(String prefName, String keyName, boolean defValue) {
        String newPrefName = String.format("%s%s", prefName, suffix);
        String newKeyName = String.format("%s%s", keyName, suffix);

        Pair<String, String> key = new Pair<>(newPrefName, newKeyName);
        if (UserSessionMap.map.containsKey(key)) {
            try {
                Object value = UserSessionMap.map.get(key);
                if (value == null) {
                    return defValue;
                } else {
                    return (boolean) value;
                }
            } catch (Exception ignored) {
            }
        }

        boolean oldValue = internalGetBoolean(prefName, keyName, defValue);

        if (oldValue != defValue) {
            internalCleanKey(prefName, keyName);
            internalSetBoolean(newPrefName, newKeyName, oldValue);
            UserSessionMap.map.put(key, oldValue);
            return oldValue;
        }

        SharedPreferences sharedPrefs = context.getSharedPreferences(newPrefName, Context.MODE_PRIVATE);
        boolean value = sharedPrefs.getBoolean(newKeyName, defValue);
        UserSessionMap.map.put(key, value);
        return value;
    }
}
