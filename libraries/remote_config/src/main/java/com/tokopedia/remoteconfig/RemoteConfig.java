package com.tokopedia.remoteconfig;


import androidx.annotation.Nullable;

import com.google.firebase.remoteconfig.ConfigUpdate;

import java.util.Set;

import kotlinx.coroutines.flow.Flow;

/**
 * Created by okasurya on 11/6/17.
 */

public interface RemoteConfig {
    /**
     * Returns a Set of all Firebase Remote Config parameter keys with the given prefix.
     *
     * @param prefix The key prefix to look for. If the prefix is an empty string, all keys are returned.
     * @return Set of Remote Config parameter keys that start with the specified prefix.
     */
    Set<String> getKeysByPrefix(String prefix);

    /**
     * Gets the value corresponding to the specified key, as a boolean.
     * @param key The Remote Config parameter key to look up.
     * @return value as boolean
     */
    boolean getBoolean(String key);

    /**
     * Gets the value corresponding to the specified key, as a boolean.
     * @param key The Remote Config parameter key to look up.
     * @param defaultValue the defaultValue if any error happened
     * @return value as boolean
     */
    boolean getBoolean(String key, boolean defaultValue);

    /**
     * Gets the value corresponding to the specified key, as a double.
     * @param key The Remote Config parameter key to look up.
     * @return value as double
     */
    double getDouble(String key);

    /**
     * Gets the value corresponding to the specified key, as a double.
     * @param key The Remote Config parameter key to look up.
     * @param defaultValue the defaultValue if any error happened
     * @return value as double
     */
    double getDouble(String key, double defaultValue);

    /**
     * Gets the value corresponding to the specified key, as a long.
     * @param key The Remote Config parameter key to look up.
     * @return value as long
     */
    long getLong(String key);

    /**
     * Gets the value corresponding to the specified key, as a long.
     * @param key The Remote Config parameter key to look up.
     * @param defaultValue the defaultValue if any error happened
     * @return value as long
     */
    long getLong(String key, long defaultValue);

    /**
     * Gets the value corresponding to the specified key, as a string.
     * @param key The Remote Config parameter key to look up.
     * @return value as string
     */
    String getString(String key);

    /**
     * Gets the value corresponding to the specified key, as a string.
     * @param key The Remote Config parameter key to look up.
     * @param defaultValue the defaultValue if any error happened
     * @return value as string
     */
    String getString(String key, String defaultValue);

    /**
    * This is for debugging only to set remote config locally
    */
    void setString(String key, String value);

    /**
     * Fetch key value data
     * @param listener The listener that will be called when fetching data is complete
     *                 or if any error happened
     */
    void fetch(Listener listener);

    /**
     * Set realtime update when there is any key updates within remote config
     * @param realTimeUpdateListener The listener that will be called addOnConfigUpdateListener and override the onUpdate and onError methods
     *                               or if any error happened
     */
    void setRealtimeUpdate(@Nullable RealTimeUpdateListener realTimeUpdateListener);

    interface Listener {
        void onComplete(RemoteConfig remoteConfig);

        void onError(Exception e);
    }

    /**
     * @param RealTimeUpdateListener to listen when real-time config updates from the Remote Config data and automatically
     *                               or if any error happened
     */
    interface RealTimeUpdateListener {
        void onUpdate(Set<String> updatedKeys);
        void onError(Exception e);
    }
}
