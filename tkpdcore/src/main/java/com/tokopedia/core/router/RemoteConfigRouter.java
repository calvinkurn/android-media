package com.tokopedia.core.router;

/**
 * Created by okasurya on 11/6/17.
 */

public interface RemoteConfigRouter {
    boolean getBooleanConfig(String key);

    byte[] getByteArrayConfig(String key);

    double getDoubleConfig(String key);

    long getLongConfig(String key);

    String getStringConfig(String key);
}
