package com.tokopedia.tkpd.beranda.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.etc.apis.home.CategoryApi;
import com.tokopedia.tkpd.beranda.data.mapper.HomeBannerMapper;
import com.tokopedia.tkpd.beranda.data.repository.HomeRepository;
import com.tokopedia.tkpd.beranda.data.source.HomeBannerDataSource;
import com.tokopedia.tkpd.beranda.domain.interactor.GetHomeBannerUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * @author by errysuprayogi on 11/28/17.
 */

@Module
public class BannerModule {

    @Provides
    HomeBannerDataSource homeBannerDataSource(@ApplicationContext Context context,
                                              CategoryApi categoryApi, HomeBannerMapper homeBannerMapper,
                                              GlobalCacheManager cacheManager, Gson gson){
        return new HomeBannerDataSource(context, categoryApi, homeBannerMapper, cacheManager, gson);
    }

    @Provides
    GetHomeBannerUseCase getHomeBannerUseCase(ThreadExecutor threadExecutor,
                                              PostExecutionThread postExecutionThread,
                                              HomeRepository homeRepository){
        return new GetHomeBannerUseCase(threadExecutor, postExecutionThread, homeRepository);
    }

    @Provides
    HomeBannerMapper homeBannerMapper(Gson gson){
        return new HomeBannerMapper(gson);
    }
}
