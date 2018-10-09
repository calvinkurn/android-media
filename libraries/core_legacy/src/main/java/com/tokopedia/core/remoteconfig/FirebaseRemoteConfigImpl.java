package com.tokopedia.core.remoteconfig;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.core.R;

import java.util.concurrent.TimeUnit;

/**
 * Created by okasurya on 9/11/17.
 */

public class FirebaseRemoteConfigImpl implements RemoteConfig {
    private static final String CACHE_NAME = "RemoteConfigDebugCache";
    private static final long THREE_HOURS = TimeUnit.HOURS.toSeconds(3);
    private static final long CONFIG_CACHE_EXPIRATION = THREE_HOURS;

    private FirebaseRemoteConfig firebaseRemoteConfig;

    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPrefs;

    public FirebaseRemoteConfigImpl(Context context) {
        this.firebaseRemoteConfig = getInstance(context);

        if(GlobalConfig.isAllowDebuggingTools() && context != null) {
            this.sharedPrefs = context.getSharedPreferences(CACHE_NAME, Context.MODE_PRIVATE);
            this.editor = sharedPrefs.edit();
        }
    }

    private FirebaseRemoteConfig getInstance(Context context) {
        try {
            if (FirebaseApp.getInstance() == null) {
                FirebaseApp.initializeApp(context);
            }

            return FirebaseRemoteConfig.getInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    @Override
    public boolean getBoolean(String key, Boolean defaultValue) {
        if (isDebug()) {
            String cacheValue = sharedPrefs.getString(key, String.valueOf(defaultValue));
            if (!cacheValue.equalsIgnoreCase(String.valueOf(defaultValue)) && !cacheValue.isEmpty()) {
                return cacheValue.equalsIgnoreCase("true");
            }
        }

        if (firebaseRemoteConfig != null) {
            return firebaseRemoteConfig.getBoolean(key);
        }
        return defaultValue;
    }

    @Override
    public byte[] getByteArray(String key) {
        return getByteArray(key, new byte[0]);
    }

    @Override
    public byte[] getByteArray(String key, byte[] defaultValue) {
        if (firebaseRemoteConfig != null) {
            return firebaseRemoteConfig.getByteArray(key);
        }
        return defaultValue;
    }

    @Override
    public double getDouble(String key) {
        return getDouble(key, 0.0D);
    }

    @Override
    public double getDouble(String key, double defaultValue) {
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
            String cacheValue = sharedPrefs.getString(key, String.valueOf(defaultValue));
            if (!cacheValue.equalsIgnoreCase(String.valueOf(defaultValue)) && !cacheValue.isEmpty()) {
                return Long.parseLong(cacheValue);
            }
        }
        if (firebaseRemoteConfig != null) {
            return firebaseRemoteConfig.getLong(key);
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
            String cacheValue = sharedPrefs.getString(key, defaultValue);
            if (!cacheValue.equalsIgnoreCase(defaultValue) && !cacheValue.isEmpty()) {
                return cacheValue;
            }
        }
        if (firebaseRemoteConfig != null) {
            return firebaseRemoteConfig.getString(key);
        }
        return defaultValue;
    }

    @Override
    public void setString(String key, String value) {
        if (!isDebug()) {
            return;
        }
        editor.putString(key, value).apply();
    }

    @Override
    public void fetch(@Nullable final Listener listener) {
        try {
            if (firebaseRemoteConfig != null) {
                firebaseRemoteConfig.setDefaults(R.xml.remote_config_default);
                firebaseRemoteConfig.fetch(CONFIG_CACHE_EXPIRATION)
                        .addOnCompleteListener(new JobExecutor(), new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    firebaseRemoteConfig.activateFetched();
                                }
                                if (listener != null) {
                                    listener.onComplete();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if (listener != null) {
                                    listener.onError(e);
                                }
                            }
                        });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private boolean isDebug() {
        return GlobalConfig.isAllowDebuggingTools()
                && sharedPrefs != null
                && editor != null;
    }
}