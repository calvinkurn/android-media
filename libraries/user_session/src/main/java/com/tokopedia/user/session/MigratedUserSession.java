package com.tokopedia.user.session;

import static com.tokopedia.user.session.Constants.LOGIN_SESSION;
import static com.tokopedia.user.session.datastore.workmanager.DataStoreMigrationWorker.USER_SESSION_LOGGER_TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.Nullable;

import com.google.crypto.tink.Aead;
import com.google.crypto.tink.shaded.protobuf.InvalidProtocolBufferException;
import com.tokopedia.encryption.security.AeadEncryptorImpl;
import com.tokopedia.encryption.utils.EncryptionExt;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.user.session.datastore.DataStorePreference;
import com.tokopedia.user.session.datastore.UserSessionDataStore;
import com.tokopedia.user.session.datastore.UserSessionDataStoreClient;
import com.tokopedia.user.session.datastore.UserSessionKeyMapper;
import com.tokopedia.user.session.util.EncoderDecoder;

import java.security.GeneralSecurityException;
import java.security.KeyStoreException;
import java.util.HashMap;

import timber.log.Timber;

public class MigratedUserSession {
    public static final String suffix = "_v2";
    protected Context context;

    private Aead aead;

    final private DataStorePreference abTestPlatform;
    public MigratedUserSession(Context context) {
        this(context, new DataStorePreference(context), null);
    }

    public MigratedUserSession(Context context, DataStorePreference abTestPlatform, Aead encryptor) {
        this.context = context.getApplicationContext();
        this.abTestPlatform = abTestPlatform;
        this.aead = encryptor;
    }

    private Boolean isEnableDataStore() {
        return this.abTestPlatform.isDataStoreEnabled();
    }

    // can't use DI because it will change UserSession constructor

    @Nullable
    private UserSessionDataStore getDataStore() {
        if (isEnableDataStore()) {
            return UserSessionDataStoreClient.getInstance(context);
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

    Pair<String, String> convertToNewKey(String prefName, String keyName) {
        return new Pair<>(String.format("%s%s", prefName, suffix), String.format("%s%s", keyName, suffix));
    }

    protected void setLong(String prefName, String keyName, long value) {
        if (isEnableDataStore()) {
            try {
                UserSessionKeyMapper.INSTANCE.mapUserSessionKeyString(
                        keyName,
                        getDataStore(),
                        String.valueOf(value)
                );
            } catch (Exception e) {
                logUserSessionEvent("set_long_exception", e);
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

    private Aead getAead() {
        if (aead == null) {
            aead = new AeadEncryptorImpl(context).getAead();
        }
        return aead;
    }

    private String internalGetString(String prefName, String keyName, String defValue) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return sharedPrefs.getString(keyName, defValue);
    }

    protected void setString(String prefName, String keyName, String value) {
        if (isEnableDataStore()) {
            try {
                if (keyName != null && value != null) {
                    UserSessionKeyMapper.INSTANCE.mapUserSessionKeyString(
                            keyName,
                            getDataStore(),
                            value
                    );
                }
            } catch (Exception e) {
                logUserSessionEvent("set_string_exception", e);
            }
        }

        Pair<String, String> newKeys = convertToNewKey(prefName, keyName);
        prefName = newKeys.first;
        keyName = newKeys.second;
        UserSessionMap.map.put(new Pair<>(prefName, keyName), value);
        value = encryptString(value, keyName);
        internalSetString(prefName, keyName, value);
    }

    /*
        Latest encryption logic for string.
        Check PII data from SET, if keyName is PII data encrypt with aead (tink)
        else use existing encryption
    */
    private String encryptString(String message, String keyName) {
        try {
            if (Constants.PII_DATA_SET.contains(keyName)) {
                // Create backup for PII Data using old encryption
                String backupData = EncoderDecoder.Encrypt(message, UserSession.KEY_IV);
                setPiiDataBackup(backupData, keyName);

                // Encryption using google tink
                String result = EncryptionExt.simplyEncrypt(getAead(), message);
                if(!result.isEmpty()) {
                    setPiiMigrationStatus(true, keyName);
                }
                return result;
            } else {
                return EncoderDecoder.Encrypt(message, UserSession.KEY_IV);
            }
        } catch (Exception e) {
            logUserSessionEvent("encrypt_string_exception", e);
            return "";
        }
    }

    private String decryptString(String message, String keyName) {
        Timber.i("decrypting %s with key %s", message, keyName);
        try {
	    /*
            Check PII data from SET, if keyName is PII data decrypt with aead (tink)
            else use existing decryption
	    */
            if (Constants.PII_DATA_SET.contains(keyName)) {
                return EncryptionExt.simplyDecrypt(getAead(), message);
            } else {
                return EncoderDecoder.Decrypt(message, UserSession.KEY_IV);
            }
        } catch (Exception e) {
            Timber.e(e);
            if(e instanceof InvalidProtocolBufferException ||
                    e instanceof GeneralSecurityException ||
                    e instanceof KeyStoreException ||
                    e instanceof IllegalArgumentException) {
                setEncryptionState(true);
            }

            // Check for backup value
            String backupValue = EncoderDecoder.Decrypt(getBackupPiiData(keyName), UserSession.KEY_IV);
            if(!backupValue.isEmpty()) {
                logUserSessionEvent("decrypt_string_exception_with_backup", e);
                return backupValue;
            } else {
                return "";
            }
        }
    }

    private void internalSetString(String prefName, String keyName, String value) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(keyName, value);
        editor.apply();
    }

    private void setPiiMigrationStatus(Boolean isMigrated, String keyName) {
        String newPrefName = String.format("%s%s", LOGIN_SESSION, suffix);
        SharedPreferences sharedPrefs = context.getSharedPreferences(newPrefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(keyName + Constants.IS_PII_MIGRATED, isMigrated);
        editor.apply();
    }

    private void setEncryptionState(Boolean isError) {
        String prefName = "ENCRYPTION_STATE_PREF";
        String keyName = "KEY_ENCRYPTION_ERROR";
        SharedPreferences sharedPrefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(keyName, isError);
        editor.apply();
    }

    private void setPiiDataBackup(String data, String keyName) {
        String newPrefName = String.format("%s%s", LOGIN_SESSION, suffix);
        SharedPreferences sharedPrefs = context.getSharedPreferences(newPrefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(keyName + Constants.PII_BACKUP, data);
        editor.apply();
    }

    private String getBackupPiiData(String keyName) {
        String newPrefName = String.format("%s%s", LOGIN_SESSION, suffix);
        SharedPreferences sharedPrefs = context.getSharedPreferences(newPrefName, Context.MODE_PRIVATE);
        return sharedPrefs.getString(keyName + Constants.PII_BACKUP, "");
    }

    private Boolean isMigratedToGoogleTink(String keyName) {
        if (Constants.PII_DATA_SET.contains(keyName)) {
            String newPrefName = String.format("%s%s", LOGIN_SESSION, suffix);
            SharedPreferences sharedPrefs = context.getSharedPreferences(newPrefName, Context.MODE_PRIVATE);
            return sharedPrefs.getBoolean(keyName + Constants.IS_PII_MIGRATED, false);
        }
        return true;
    }

    private void logUserSessionEvent(String method, @Nullable Exception e) {
        HashMap<String, String> data = new HashMap<>();
        data.put("method", method);
        if(e != null) {
            data.put("error", Log.getStackTraceString(e));
        }
        ServerLogger.log(Priority.P2, USER_SESSION_LOGGER_TAG, data);
    }

    protected String getAndTrimOldString(String prefName, String keyName, String defValue) {
        String newPrefName = String.format("%s%s", prefName, suffix);
        String newKeyName = String.format("%s%s", keyName, suffix);

        Pair<String, String> key = new Pair<>(newPrefName, newKeyName);
        if (UserSessionMap.map.containsKey(key)) {
            try {
                Object value = UserSessionMap.map.get(key);
                if (value == null) return defValue;
                else return (String) value;
            } catch (Exception ignored) {}
        }

        try {
            String oldValue = internalGetString(prefName, keyName, defValue);

            if (oldValue != null && !oldValue.equals(defValue)) {
                Timber.d("cleaning %s", oldValue);
                internalCleanKey(prefName, keyName);
                internalSetString(newPrefName, newKeyName, encryptString(oldValue, newKeyName));
                UserSessionMap.map.put(key, oldValue);
                return oldValue;
            }

            SharedPreferences sharedPrefs = context.getSharedPreferences(newPrefName, Context.MODE_PRIVATE);
            String value = sharedPrefs.getString(newKeyName, defValue);

            if (value != null) {
                if (!isMigratedToGoogleTink(newKeyName)) {
                    String decryptedCurValue = EncoderDecoder.Decrypt(value, UserSession.KEY_IV);
                    String encryptedNewValue = encryptString(decryptedCurValue, newKeyName);
                    if (encryptedNewValue != null && !encryptedNewValue.isEmpty()) {
                        internalSetString(newPrefName, newKeyName, encryptedNewValue);
                        UserSessionMap.map.put(key, decryptedCurValue);
                        return decryptedCurValue;
                    }
                }

                if (value.equals(defValue)) {// if value same with def value\
                    return value;
                } else {
                    if(isMigratedToGoogleTink(keyName)) {
                        value = decryptString(value, newKeyName);
                    } else {
                        value = EncoderDecoder.Decrypt(value, UserSession.KEY_IV);
                    }
                    if (value.isEmpty()) {
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
        if (isEnableDataStore()) {
            try {
                if (keyName != null) {
                    UserSessionKeyMapper.INSTANCE.mapUserSessionKeyBoolean(
                            keyName,
                            getDataStore(),
                            value
                    );
                }
            } catch (Exception e) {
                logUserSessionEvent("set_boolean_exception", e);
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
