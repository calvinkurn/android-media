package com.tokopedia.tkpd.beranda.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.tkpd.beranda.data.mapper.HomeDataMapper;
import com.tokopedia.tkpd.beranda.data.repository.HomeRepository;
import com.tokopedia.tkpd.beranda.data.repository.HomeRepositoryImpl;
import com.tokopedia.tkpd.beranda.data.source.BrandsOfficialStoreDataSource;
import com.tokopedia.tkpd.beranda.data.source.HomeBannerDataSource;
import com.tokopedia.tkpd.beranda.data.source.HomeCategoryDataSource;
import com.tokopedia.tkpd.beranda.data.source.TickerDataSource;
import com.tokopedia.tkpd.beranda.data.source.TopPicksDataSource;
import com.tokopedia.tkpd.beranda.di.HomeScope;
import com.tokopedia.tkpd.beranda.domain.interactor.GetLocalHomeDataUseCase;
import com.tokopedia.tkpd.beranda.presentation.presenter.HomePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author by errysuprayogi on 11/28/17.
 */

@Module(includes = {CategoryModule.class, BannerModule.class, BrandsModule.class, TopPicksModule.class, TickerModule.class, HomeFeedModule.class})
public class HomeModule {

    @HomeScope
    @Provides
    HomeDataMapper homeDataMapper(@ApplicationContext Context context){
        return new HomeDataMapper(context);
    }

    @HomeScope
    @Provides
    GlobalCacheManager globalCacheManager() {
        return new GlobalCacheManager();
    }

    @HomeScope
    @Provides
    HomePresenter homePresenter(@ApplicationContext Context context) {
        return new HomePresenter(context);
    }

    @HomeScope
    @Provides
    GetLocalHomeDataUseCase getLocalHomeDataUseCase(ThreadExecutor threadExecutor,
                                                    PostExecutionThread postExecutionThread,
                                                    HomeRepository repository,
                                                    HomeDataMapper dataMapper){
        return new GetLocalHomeDataUseCase(threadExecutor, postExecutionThread, repository, dataMapper);
    }

    @HomeScope
    @Provides
    HomeRepository homeRepository(HomeCategoryDataSource homeCategoryDataSource,
                                  HomeBannerDataSource homeBannerDataSource,
                                  BrandsOfficialStoreDataSource brandsOfficialStoreDataSource,
                                  TopPicksDataSource topPicksDataSource,
                                  TickerDataSource tickerDataSource) {
        return new HomeRepositoryImpl(homeCategoryDataSource, homeBannerDataSource,
                brandsOfficialStoreDataSource, topPicksDataSource, tickerDataSource);
    }

}
