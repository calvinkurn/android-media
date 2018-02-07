package com.tokopedia.abstraction.common.data.model.storage;

/**
 * Created by nathan on 11/28/17.
 */

public interface GlobalCacheManager {

    String save(String key, String value, long duration);

    String delete(String key);

    String get(String key);

    boolean isExpired(String key);
}
