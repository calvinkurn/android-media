package com.tokopedia.remoteconfig;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.tokopedia.config.GlobalConfig;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by okasurya on 9/11/17.
 */

public class FirebaseRemoteConfigImpl implements RemoteConfig {
    private static final String CACHE_NAME = "RemoteConfigDebugCache";
    private static final long THIRTY_MINUTES = TimeUnit.MINUTES.toSeconds(30);
    private static final long CONFIG_CACHE_EXPIRATION = THIRTY_MINUTES;

    private FirebaseRemoteConfig firebaseRemoteConfig;
    private SharedPreferences sharedPrefs;

    public FirebaseRemoteConfigImpl(Context context) {
        try {
            this.firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        } catch (Exception ignored) { } // FirebaseApp is not intialized, ignoring the error and handle it with default value

        if (GlobalConfig.isAllowDebuggingTools() && context != null) {
            this.sharedPrefs = context.getSharedPreferences(CACHE_NAME, Context.MODE_PRIVATE);
        }
    }

    private boolean isDebug() {
        return GlobalConfig.isAllowDebuggingTools() && sharedPrefs != null;
    }

    @Override
    public Set<String> getKeysByPrefix(String prefix) {
        if (firebaseRemoteConfig != null) {
            Set<String> set = firebaseRemoteConfig.getKeysByPrefix(prefix);
            if (isDebug()) {
                Map<String, ?> map = sharedPrefs.getAll();
                String key = "";
                for (Map.Entry<String,?> entry : map.entrySet())
                    key = entry.getKey();
                    if (key.startsWith(prefix)){
                        set.add(key);
                    }
            }
            return set;
        }

        return null;
    }

    @Override
    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        if (isDebug()) {
            String cacheValue = sharedPrefs.getString(key, null);

            if (cacheValue != null) {
                return cacheValue.equalsIgnoreCase("true");
            }
        }

        if (firebaseRemoteConfig != null) {
            String value = firebaseRemoteConfig.getString(key);
            if (TextUtils.isEmpty(value)) {
                return defaultValue;
            } else {
                return "true".equalsIgnoreCase(value);
            }
        }
        return defaultValue;
    }

    @Override
    public double getDouble(String key) {
        return getDouble(key, 0.0D);
    }

    @Override
    public double getDouble(String key, double defaultValue) {
        if (isDebug()) {
            String cachedValue = sharedPrefs.getString(key, null);

            if (cachedValue != null) {
                return Double.parseDouble(cachedValue);
            }
        }

        if (firebaseRemoteConfig != null) {
            return firebaseRemoteConfig.getDouble(key);
        }

        return defaultValue;
    }

    @Override
    public long getLong(String key) {
        return getLong(key, 0L);
    }

    @Override
    public long getLong(String key, long defaultValue) {
        if (isDebug()) {
            String cacheValue = sharedPrefs.getString(key, null);

            if (cacheValue != null) {
                return Long.parseLong(cacheValue);
            }
        }

        if (firebaseRemoteConfig != null) {
            long value = firebaseRemoteConfig.getLong(key);
            if (value == 0L) {
                return defaultValue;
            } else {
                return value;
            }
        }

        return defaultValue;
    }

    @Override
    public String getString(String key) {
        return getString(key, "");
    }

    @Override
    public String getString(String key, String defaultValue) {
        if (isDebug()) {
            String cacheValue = sharedPrefs.getString(key, null);

            if (cacheValue != null) {
                return cacheValue;
            }
        }

        if (firebaseRemoteConfig != null) {
            String value = firebaseRemoteConfig.getString(key);
            if (TextUtils.isEmpty(value)) {
                return defaultValue;
            } else {
                return value;
            }
        }

        return defaultValue;
    }

    @Override
    public void setString(String key, String value) {
        if (isDebug()) {
            sharedPrefs.edit().putString(key, value).apply();
        }
    }

    @Override
    public void fetch(@Nullable final Listener listener) {
        try {
            if (firebaseRemoteConfig != null) {
                firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_default);
                FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                        .setMinimumFetchIntervalInSeconds(CONFIG_CACHE_EXPIRATION)
                        .build();
                firebaseRemoteConfig.setConfigSettingsAsync(configSettings);
                firebaseRemoteConfig.fetchAndActivate()
                        .addOnCompleteListener(new JobExecutor(), task -> {
                            if (listener != null) {
                                listener.onComplete(FirebaseRemoteConfigImpl.this);
                            }
                        })
                        .addOnFailureListener(exception -> {
                            if (listener != null) {
                                listener.onError(exception);
                            }
                        });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}