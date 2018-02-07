package com.tokopedia.tkpd.beranda.di.module;

import com.google.gson.Gson;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.ace.apis.SearchApi;
import com.tokopedia.tkpd.beranda.data.mapper.TopPicksMapper;
import com.tokopedia.tkpd.beranda.data.repository.HomeRepository;
import com.tokopedia.tkpd.beranda.data.source.TopPicksDataSource;
import com.tokopedia.tkpd.beranda.domain.interactor.GetTopPicksUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * @author by errysuprayogi on 11/28/17.
 */

@Module
public class TopPicksModule {

    @Provides
    TopPicksDataSource topPicksDataSource(SearchApi searchApi, TopPicksMapper topPicksMapper, GlobalCacheManager cacheManager, Gson gson){
        return new TopPicksDataSource(searchApi, topPicksMapper, cacheManager, gson);
    }

    @Provides
    TopPicksMapper topPicksMapper(Gson gson){
        return new TopPicksMapper(gson);
    }

    @Provides
    GetTopPicksUseCase getTopPicksUseCase(ThreadExecutor threadExecutor,
                                          PostExecutionThread postExecutionThread,
                                          HomeRepository homeRepository){
        return new GetTopPicksUseCase(threadExecutor, postExecutionThread, homeRepository);
    }

}
