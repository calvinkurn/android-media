package com.tokopedia.tkpd;

import com.tokopedia.abstraction.common.data.model.storage.GlobalCacheManager;


/**
 * Created by nabillasabbaha on 1/22/18.
 */

public class GlobalCacheManagerImpl implements GlobalCacheManager {

    private com.tokopedia.core.database.manager.GlobalCacheManager cacheManager;

    public GlobalCacheManagerImpl() {
        this.cacheManager = new com.tokopedia.core.database.manager.GlobalCacheManager();
    }

    @Override
    public void save(String key, String value, int duration) {
        cacheManager.setKey(key);
        cacheManager.setValue(value);
        cacheManager.setCacheDuration(duration);
        cacheManager.store();
    }

    @Override
    public void delete(String key) {
        cacheManager.delete(key);
    }

    @Override
    public String get(String key) {
        return cacheManager.getValueString(key);
    }

    @Override
    public boolean isExpired(String key) {
        return cacheManager.isAvailable(key);
    }
}
