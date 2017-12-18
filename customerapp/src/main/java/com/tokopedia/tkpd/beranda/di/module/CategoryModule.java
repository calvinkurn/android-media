package com.tokopedia.tkpd.beranda.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.beranda.data.mapper.HomeCategoryMapper;
import com.tokopedia.tkpd.beranda.data.repository.HomeRepository;
import com.tokopedia.tkpd.beranda.data.source.HomeCategoryDataSource;
import com.tokopedia.tkpd.beranda.domain.interactor.GetHomeCategoryUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * @author by errysuprayogi on 11/28/17.
 */

@Module
public class CategoryModule {

    @Provides
    GetHomeCategoryUseCase getHomeCategoryUseCase(ThreadExecutor threadExecutor,
                                                  PostExecutionThread postExecutionThread,
                                                  HomeRepository homeRepository){
        return new GetHomeCategoryUseCase(threadExecutor, postExecutionThread, homeRepository);
    }

    @Provides
    HomeCategoryDataSource homeCategoryDataSource(MojitoApi mojitoApi,
                                                  HomeCategoryMapper homeCategoryMapper,
                                                  SessionHandler sessionHandler,
                                                  GlobalCacheManager cacheManager,
                                                  Gson gson){
        return new HomeCategoryDataSource(mojitoApi, homeCategoryMapper, sessionHandler, cacheManager, gson);
    }

    @Provides
    HomeCategoryMapper homeCategoryMapper(Gson gson){
        return new HomeCategoryMapper(gson);
    }

}
