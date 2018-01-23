package com.tokopedia.core.common.cache.di.module;

import android.content.Context;
import android.content.pm.PackageManager;

import com.tokopedia.cacheapi.data.repository.ApiCacheRepositoryImpl;
import com.tokopedia.cacheapi.data.source.ApiCacheDataSource;
import com.tokopedia.cacheapi.data.source.cache.CacheApiVersionCache;
import com.tokopedia.cacheapi.data.source.db.CacheApiDataManager;
import com.tokopedia.cacheapi.domain.ApiCacheRepository;
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
    CacheApiVersionCache provideCacheApiVersionCache(@ApplicationContext Context context, @VersionNameQualifier String versionName) {
        return new CacheApiVersionCache(context, versionName);
    }

    @Provides
    CacheApiDataManager provideCacheApiDataManager() {
        return new CacheApiDataManager();
    }

    @Provides
    ApiCacheRepository provideApiCacheRepository(ApiCacheDataSource apiCacheDataSource) {
        return new ApiCacheRepositoryImpl(apiCacheDataSource);
    }

    @Provides
    ApiCacheDataSource provideApiCacheDataSource(CacheApiVersionCache cacheApiVersionCache, CacheApiDataManager cacheApiDataManager) {
        return new ApiCacheDataSource(cacheApiVersionCache, cacheApiDataManager);
    }

    @Provides
    CacheApiWhiteListUseCase provideCacheApiWhiteListUseCase(
            ApiCacheRepository apiCacheRepository){
        return new CacheApiWhiteListUseCase(apiCacheRepository);
    }

    @Provides
    CacheApiClearAllUseCase provideCacheApiClearAllUseCase(
            ApiCacheRepository apiCacheRepository){
        return new CacheApiClearAllUseCase(apiCacheRepository);
    }

    @Provides
    CacheApiDataDeleteUseCase provideCacheApiDataDeleteUseCase(
            ApiCacheRepository apiCacheRepository){
        return new CacheApiDataDeleteUseCase(apiCacheRepository);
    }

}