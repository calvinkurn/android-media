package com.tokopedia.remoteconfig;

import java.util.Set;

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

    interface Listener {
        void onComplete(RemoteConfig remoteConfig);

        void onError(Exception e);
    }
}
