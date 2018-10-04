package com.tokopedia.cacheapi.util;

import com.tokopedia.cacheapi.data.repository.CacheApiRepositoryImpl;
import com.tokopedia.cacheapi.data.source.CacheApiDataSource;
import com.tokopedia.cacheapi.data.source.db.CacheApiDatabaseSource;
import com.tokopedia.cacheapi.domain.CacheApiRepository;

/**
 * Created by nathan on 1/25/18.
 */

public class Injection {

    private static CacheApiDatabaseSource cacheApiDatabaseSource;
    private static CacheApiDataSource cacheApiDataSource;
    private static CacheApiRepository cacheApiRepository;

    private static CacheApiDatabaseSource provideCacheApiDatabaseSource() {
        if (cacheApiDatabaseSource == null) {
            cacheApiDatabaseSource = new CacheApiDatabaseSource();
        }
        return cacheApiDatabaseSource;
    }

    private static CacheApiDataSource provideCacheApiDataSource() {
        if (cacheApiDataSource == null) {
            cacheApiDataSource = new CacheApiDataSource(provideCacheApiDatabaseSource());
        }
        return cacheApiDataSource;
    }

    public static CacheApiRepository provideCacheApiRepository() {
        if (cacheApiRepository == null) {
            cacheApiRepository = new CacheApiRepositoryImpl(provideCacheApiDataSource());
        }
        return cacheApiRepository;
    }
}
