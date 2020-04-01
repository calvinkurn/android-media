package com.tokopedia.cacheapi.util;

import android.content.Context;

import com.tokopedia.cacheapi.data.repository.CacheApiRepositoryImpl;
import com.tokopedia.cacheapi.data.source.CacheApiDataSource;
import com.tokopedia.cacheapi.data.source.db.CacheApiDatabase;
import com.tokopedia.cacheapi.data.source.db.CacheApiDatabaseSource;
import com.tokopedia.cacheapi.domain.CacheApiRepository;

/**
 * Created by nathan on 1/25/18.
 */

public class Injection {

    private static CacheApiDatabaseSource cacheApiDatabaseSource;
    private static CacheApiDataSource cacheApiDataSource;
    private static CacheApiRepository cacheApiRepository;

    private static CacheApiDatabaseSource provideCacheApiDatabaseSource(Context context) {
        if (cacheApiDatabaseSource == null) {
            cacheApiDatabaseSource = new CacheApiDatabaseSource(
                    CacheApiDatabase.getInstance(context).getCacheApiVersionDao(),
                    CacheApiDatabase.getInstance(context).getCacheApiWhitelistDao(),
                    CacheApiDatabase.getInstance(context).getCacheApiDataDao()
            );
        }
        return cacheApiDatabaseSource;
    }

    private static CacheApiDataSource provideCacheApiDataSource(Context context) {
        if (cacheApiDataSource == null) {
            cacheApiDataSource = new CacheApiDataSource(provideCacheApiDatabaseSource(context));
        }
        return cacheApiDataSource;
    }

    public static CacheApiRepository provideCacheApiRepository(Context context) {
        if (cacheApiRepository == null) {
            cacheApiRepository = new CacheApiRepositoryImpl(provideCacheApiDataSource(context));
        }
        return cacheApiRepository;
    }
}
