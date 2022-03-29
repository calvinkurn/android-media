package com.tokopedia.user.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.Nullable;

import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.user.session.datastore.UserSessionAbTestPlatform;
import com.tokopedia.user.session.datastore.UserSessionDataStore;
import com.tokopedia.user.session.datastore.UserSessionDataStoreClient;
import com.tokopedia.user.session.datastore.UserSessionKeyMapper;
import com.tokopedia.user.session.util.EncoderDecoder;

import java.util.HashMap;

public class MigratedUserSession {
    public static final String suffix = "_v2";
    protected Context context;

    public MigratedUserSession(Context context) {
	this.context = context.getApplicationContext();
    }

    private Boolean isEnableDataStore() {
	if(context != null) {
	    return UserSessionAbTestPlatform.INSTANCE.isDataStoreEnable(context);
	}
	return false;
    }

    // can't use DI because it will change UserSession constructor
    @Nullable
    private UserSessionDataStore getDataStore() {
        if(isEnableDataStore()) {
	    return UserSessionDataStoreClient.INSTANCE.getInstance(context);
	}
        return null;
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
	if(isEnableDataStore()) {
	    try {
		UserSessionKeyMapper.INSTANCE.mapUserSessionKeyString(
			keyName,
			getDataStore(),
			String.valueOf(value)
		);
	    } catch (Exception e) {
		HashMap<String, String> data = new HashMap<>();
		data.put("type", "set_long_exception");
		data.put("error", Log.getStackTraceString(e));
		ServerLogger.log(Priority.P2, "USER_SESSION_DATA_STORE", data);
	    }
	}
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
	if(isEnableDataStore()) {
	    try {
		if (keyName != null && value != null) {
		    UserSessionKeyMapper.INSTANCE.mapUserSessionKeyString(
			    keyName,
			    getDataStore(),
			    value
		    );
		}
	    } catch (Exception e) {
		HashMap<String, String> data = new HashMap<>();
		data.put("type", "set_string_exception");
		data.put("error", Log.getStackTraceString(e));
		ServerLogger.log(Priority.P2, "USER_SESSION_DATA_STORE", data);
	    }
	}

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
	String debug = "";
	boolean isGettingFromMap = false;

	Pair<String, String> key = new Pair<>(newPrefName, newKeyName);
	if (UserSessionMap.map.containsKey(key)) {
	    try {
		Object value = UserSessionMap.map.get(key);
		if (value == null) {
		    isGettingFromMap = true;
		    return defValue;
		} else {
		    isGettingFromMap = true;
		    return (String) value;
		}
	    } catch (Exception ignored) {
	    }
	}

	try {
	    String oldValue = internalGetString(prefName, keyName, defValue);

	    if (oldValue != null && !oldValue.equals(defValue)) {
		internalCleanKey(prefName, keyName);
		internalSetString(newPrefName, newKeyName, EncoderDecoder.Encrypt(oldValue, UserSession.KEY_IV));
		UserSessionMap.map.put(key, oldValue);
		return oldValue;
	    }

	    SharedPreferences sharedPrefs = context.getSharedPreferences(newPrefName, Context.MODE_PRIVATE);
	    String value = sharedPrefs.getString(newKeyName, defValue);

	    if (value != null) {
		if (value.equals(defValue)) {// if value same with def value\
		    return value;
		} else {
		    value = EncoderDecoder.Decrypt(value, UserSession.KEY_IV);// decrypt here
		    if(value.isEmpty()) {
			return defValue;
		    } else {
			UserSessionMap.map.put(key, value);
			return value;
		    }
		}
	    } else {
		return defValue;
	    }
	} catch (Exception e) {
	    return defValue;
	}
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
	if(isEnableDataStore()) {
	    try {
		if (keyName != null) {
		    UserSessionKeyMapper.INSTANCE.mapUserSessionKeyBoolean(
			    keyName,
			    getDataStore(),
			    value
		    );
		}
	    } catch (Exception e) {
		HashMap<String, String> data = new HashMap<>();
		data.put("type", "set_boolean_exception");
		data.put("error", Log.getStackTraceString(e));
		ServerLogger.log(Priority.P2, "USER_SESSION_DATA_STORE", data);
	    }
	}

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
