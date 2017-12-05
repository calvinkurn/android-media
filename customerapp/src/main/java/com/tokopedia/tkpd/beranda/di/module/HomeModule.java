package com.tokopedia.tkpd.beranda.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.core.network.di.qualifier.MojitoQualifier;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.apiservice.DigitalEndpointService;
import com.tokopedia.digital.widget.data.mapper.FavoriteNumberListDataMapper;
import com.tokopedia.digital.widget.data.mapper.IFavoriteNumberMapper;
import com.tokopedia.digital.widget.domain.DigitalWidgetRepository;
import com.tokopedia.digital.widget.model.mapper.CategoryMapper;
import com.tokopedia.digital.widget.model.mapper.StatusMapper;
import com.tokopedia.tkpd.beranda.data.mapper.HomeCategoryMapper;
import com.tokopedia.tkpd.beranda.data.repository.HomeRepository;
import com.tokopedia.tkpd.beranda.data.repository.HomeRepositoryImpl;
import com.tokopedia.tkpd.beranda.data.source.BrandsOfficialStoreDataSource;
import com.tokopedia.tkpd.beranda.data.source.HomeBannerDataSource;
import com.tokopedia.tkpd.beranda.data.source.HomeCategoryDataSource;
import com.tokopedia.tkpd.beranda.data.source.TickerDataSource;
import com.tokopedia.tkpd.beranda.data.source.TopPicksDataSource;
import com.tokopedia.tkpd.beranda.di.HomeScope;
import com.tokopedia.tkpd.beranda.domain.interactor.GetHomeCategoryUseCase;
import com.tokopedia.tkpd.beranda.domain.interactor.GetTopPicksUseCase;
import com.tokopedia.tkpd.beranda.presentation.presenter.HomePresenter;
import com.tokopedia.tkpd.home.recharge.interactor.RechargeNetworkInteractor;
import com.tokopedia.tkpd.home.recharge.interactor.RechargeNetworkInteractorImpl;

import dagger.Module;
import dagger.Provides;

/**
 * @author by errysuprayogi on 11/28/17.
 */

@Module(includes = {CategoryModule.class, BannerModule.class, BrandsModule.class, TopPicksModule.class, TickerModule.class})
public class HomeModule {

    @Provides
    HomePresenter homePresenter(@ApplicationContext Context context){
        return new HomePresenter(context);
    }

    @HomeScope
    @Provides
    HomeRepository homeRepository(HomeCategoryDataSource homeCategoryDataSource,
                                  HomeBannerDataSource homeBannerDataSource,
                                  BrandsOfficialStoreDataSource brandsOfficialStoreDataSource,
                                  TopPicksDataSource topPicksDataSource,
                                  TickerDataSource tickerDataSource){
        return new HomeRepositoryImpl(homeCategoryDataSource, homeBannerDataSource, brandsOfficialStoreDataSource, topPicksDataSource, tickerDataSource);
    }

}
