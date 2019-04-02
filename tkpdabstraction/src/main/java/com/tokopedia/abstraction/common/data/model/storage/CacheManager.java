package com.tokopedia.abstraction.common.data.model.storage;

/**
 * Created by nathan on 11/28/17.
 */
@Deprecated
public interface CacheManager {

    void save(String key, String value, long durationInSeconds);

    void delete(String key);

    String get(String key);

    boolean isExpired(String key);
}
