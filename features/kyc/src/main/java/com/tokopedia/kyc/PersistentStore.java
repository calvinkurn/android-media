package com.tokopedia.kyc;

import android.content.Context;

import com.tokopedia.cachemanager.CacheManager;
import com.tokopedia.cachemanager.PersistentCacheManager;

public class PersistentStore {
    private CacheManager cacheManager;
    private static PersistentStore persistentStore;
    public static PersistentStore getInstance(Context context){
        if(persistentStore == null){
            persistentStore = new PersistentStore();
            persistentStore.cacheManager = new PersistentCacheManager(context,"");
        }
        return persistentStore;
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }
}
