package com.tokopedia.notifications;

/**
 * Created by Ashwani Tyagi on 29/10/18.
 */
public interface CMRouter {
    String getUserId();

    boolean getBooleanRemoteConfig(String key, boolean defaultValue);

    long getLongRemoteConfig(String key, long defaultValue);
}
