package com.tokopedia.core.common.cache.di.module;

import android.content.Context;
import android.content.pm.PackageManager;

import com.tokopedia.cacheapi.data.repository.CacheApiRepositoryImpl;
import com.tokopedia.cacheapi.data.source.CacheApiDataSource;
import com.tokopedia.cacheapi.data.source.db.CacheApiDatabaseSource;
import com.tokopedia.cacheapi.domain.CacheApiRepository;
import com.tokopedia.cacheapi.domain.interactor.CacheApiClearAllUseCase;
import com.tokopedia.cacheapi.domain.interactor.CacheApiDataDeleteUseCase;
import com.tokopedia.cacheapi.domain.interactor.CacheApiWhiteListUseCase;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.common.cache.di.qualifier.VersionNameQualifier;

import dagger.Module;
import dagger.Provides;

/**
 * Created by normansyahputa on 8/16/17.
 */

@Module
public class CacheModule {

    @VersionNameQualifier
    @Provides
    public String provideVersionName(@ApplicationContext Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    @Provides
    CacheApiDatabaseSource provideCacheApiDatabaseSource() {
        return new CacheApiDatabaseSource();
    }

    @Provides
    CacheApiRepository provideApiCacheRepository(CacheApiDataSource apiCacheDataSource) {
        return new CacheApiRepositoryImpl(apiCacheDataSource);
    }

    @Provides
    CacheApiDataSource provideApiCacheDataSource(CacheApiDatabaseSource cacheApiDatabaseSource) {
        return new CacheApiDataSource(cacheApiDatabaseSource);
    }

    @Provides
    CacheApiWhiteListUseCase provideCacheApiWhiteListUseCase(
            CacheApiRepository apiCacheRepository){
        return new CacheApiWhiteListUseCase(apiCacheRepository);
    }

    @Provides
    CacheApiClearAllUseCase provideCacheApiClearAllUseCase(
            CacheApiRepository apiCacheRepository){
        return new CacheApiClearAllUseCase(apiCacheRepository);
    }

    @Provides
    CacheApiDataDeleteUseCase provideCacheApiDataDeleteUseCase(
            CacheApiRepository apiCacheRepository){
        return new CacheApiDataDeleteUseCase(apiCacheRepository);
    }

}