package com.tokopedia.home.beranda.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.paging.PagingHandler;
import com.tokopedia.digital.common.data.apiservice.DigitalEndpointService;
import com.tokopedia.digital.common.data.source.CategoryListDataSource;
import com.tokopedia.digital.common.data.source.StatusDataSource;
import com.tokopedia.digital.widget.data.repository.DigitalWidgetRepository;
import com.tokopedia.digital.widget.view.model.mapper.CategoryMapper;
import com.tokopedia.digital.widget.view.model.mapper.StatusMapper;
import com.tokopedia.home.beranda.data.mapper.HomeFeedMapper;
import com.tokopedia.home.beranda.data.mapper.HomeMapper;
import com.tokopedia.home.beranda.data.repository.HomeRepository;
import com.tokopedia.home.beranda.data.repository.HomeRepositoryImpl;
import com.tokopedia.home.beranda.data.source.HomeDataSource;
import com.tokopedia.home.beranda.domain.interactor.GetHomeFeedUseCase;
import com.tokopedia.home.common.HomeDataApi;
import com.tokopedia.home.beranda.di.HomeScope;
import com.tokopedia.home.beranda.domain.interactor.GetHomeDataUseCase;
import com.tokopedia.home.beranda.domain.interactor.GetLocalHomeDataUseCase;
import com.tokopedia.home.beranda.presentation.presenter.HomePresenter;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoByDomainUseCase;
import com.tokopedia.graphql.domain.GraphqlUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * @author by errysuprayogi on 11/28/17.
 */

@Module
public class HomeModule {

    @HomeScope
    @Provides
    protected HomeMapper providehomeMapper(@ApplicationContext Context context){
        return new HomeMapper(context);
    }

    @HomeScope
    @Provides
    protected HomePresenter homePresenter(PagingHandler pagingHandler,
                                UserSession userSession,
                                GetShopInfoByDomainUseCase getShopInfoByDomainUseCase) {
        return realHomePresenter(pagingHandler, userSession, getShopInfoByDomainUseCase);
    }

    protected HomePresenter realHomePresenter(PagingHandler pagingHandler,
                                          UserSession userSession,
                                          GetShopInfoByDomainUseCase getShopInfoByDomainUseCase){
        return new HomePresenter(pagingHandler, userSession, getShopInfoByDomainUseCase);
    }

    @HomeScope
    @Provides
    protected PagingHandler pagingHandler(){
        return new PagingHandler();
    }

    @HomeScope
    @Provides
    protected HomeRepository homeRepository(HomeDataSource homeDataSource) {
        return new HomeRepositoryImpl(homeDataSource);
    }

    @Provides
    protected HomeDataSource provideHomeDataSource(HomeDataApi homeDataApi,
                                         HomeMapper homeMapper,
                                         @ApplicationContext Context context,
                                         CacheManager cacheManager,
                                         Gson gson){
        return new HomeDataSource(homeDataApi, homeMapper, context, cacheManager, gson);
    }

    @Provides
    protected GetHomeDataUseCase provideGetHomeDataUseCase(HomeRepository homeRepository){
        return new GetHomeDataUseCase(homeRepository);
    }

    @Provides
    protected GetHomeFeedUseCase provideGetHomeFeedUseCase(@ApplicationContext Context context,
                                                           GraphqlUseCase graphqlUseCase,
                                                           HomeFeedMapper homeFeedMapper){
        return new GetHomeFeedUseCase(context, graphqlUseCase, homeFeedMapper);
    }

    @Provides
    HomeFeedMapper homeFeedMapper() {
        return new HomeFeedMapper();
    }

    @Provides
    GraphqlUseCase graphqlUseCase() {
        return new GraphqlUseCase();
    }

    @Provides
    protected com.tokopedia.user.session.UserSession provideUserSession(
            @ApplicationContext Context context){
        return new com.tokopedia.user.session.UserSession(context);
    }

    @HomeScope
    @Provides
    protected GetLocalHomeDataUseCase getLocalHomeDataUseCase(HomeRepository repository){
        return new GetLocalHomeDataUseCase(repository);
    }

    @HomeScope
    @Provides
    protected DigitalEndpointService provideDigitalEndpointService(){
        return new DigitalEndpointService();
    }

    @HomeScope
    @Provides
    protected StatusDataSource provideStatusDataSource(DigitalEndpointService digitalEndpointService,
                                                       CacheManager cacheManager,
                                                       StatusMapper statusMapper){
        return new StatusDataSource(
                digitalEndpointService,
                cacheManager,
                statusMapper);
    }

    @HomeScope
    @Provides
    protected CategoryListDataSource provideCategoryListDataSource(DigitalEndpointService digitalEndpointService,
                                                       CacheManager cacheManager,
                                                                   CategoryMapper categoryMapper){
        return new CategoryListDataSource(
                digitalEndpointService,
                cacheManager,
                categoryMapper);
    }

    @HomeScope
    @Provides
    protected StatusMapper provideStatusMapper(){
        return new StatusMapper();
    }

    @HomeScope
    @Provides
    protected CategoryMapper provideCategoryMapper(){
        return new CategoryMapper();
    }

    @HomeScope
    @Provides
    protected DigitalWidgetRepository providetDigitalWidgetRepository(
            StatusDataSource statusDataSource,
            CategoryListDataSource categoryListDataSource){
        return new DigitalWidgetRepository(
                statusDataSource,
                categoryListDataSource
        );
    }
}
