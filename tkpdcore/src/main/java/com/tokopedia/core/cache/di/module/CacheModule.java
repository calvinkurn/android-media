package com.tokopedia.core.cache.di.module;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.base.di.qualifier.ApiCacheQualifier;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.di.qualifier.VersionNameQualifier;
import com.tokopedia.core.base.di.scope.ApplicationScope;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.cache.data.repository.ApiCacheRepositoryImpl;
import com.tokopedia.core.cache.data.source.cache.CacheHelper;
import com.tokopedia.core.cache.domain.ApiCacheRepository;
import com.tokopedia.core.cache.domain.interactor.CacheApiWhiteListUseCase;
import com.tokopedia.core.var.TkpdCache;

import dagger.Module;
import dagger.Provides;

/**
 * Created by normansyahputa on 8/16/17.
 */

@Module
public class CacheModule {
    @ApiCacheQualifier
    @Provides
    public LocalCacheHandler provideLocalCacheHandler(@ApplicationContext Context context) {
        return new LocalCacheHandler(context, TkpdCache.CACHE_API);
    }


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
    ApiCacheRepository provideApiCacheRepository(@ApiCacheQualifier LocalCacheHandler localCacheHandler,
                                                 @VersionNameQualifier String versionName){
        return new ApiCacheRepositoryImpl(localCacheHandler, versionName);
    }

    @Provides
    CacheApiWhiteListUseCase provideCacheApiWhiteListUseCase(
            @ApplicationScope ThreadExecutor threadExecutor,
            @ApplicationScope PostExecutionThread postExecutionThread,
            ApiCacheRepository apiCacheRepository){
        return new CacheApiWhiteListUseCase(threadExecutor, postExecutionThread, apiCacheRepository);
    }

    @Provides
    CacheHelper provideCacheHelper(){
        return new CacheHelper();
    }
}
