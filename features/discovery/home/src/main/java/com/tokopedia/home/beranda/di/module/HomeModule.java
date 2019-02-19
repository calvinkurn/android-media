package com.tokopedia.home.beranda.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.paging.PagingHandler;
import com.tokopedia.digital.common.data.apiservice.DigitalEndpointService;
import com.tokopedia.digital.common.data.apiservice.DigitalGqlApiService;
import com.tokopedia.digital.common.data.source.CategoryListDataSource;
import com.tokopedia.digital.common.data.source.StatusDataSource;
import com.tokopedia.digital.widget.data.repository.DigitalWidgetRepository;
import com.tokopedia.digital.widget.data.source.RecommendationListDataSource;
import com.tokopedia.digital.widget.view.model.mapper.CategoryMapper;
import com.tokopedia.digital.widget.view.model.mapper.StatusMapper;
import com.tokopedia.home.beranda.data.mapper.CheckNullMapper;
import com.tokopedia.home.beranda.data.mapper.FeedTabMapper;
import com.tokopedia.home.beranda.data.mapper.HomeFeedMapper;
import com.tokopedia.home.beranda.data.mapper.HomeMapper;
import com.tokopedia.home.beranda.data.repository.HomeRepository;
import com.tokopedia.home.beranda.data.repository.HomeRepositoryImpl;
import com.tokopedia.home.beranda.data.source.HomeDataSource;
import com.tokopedia.home.beranda.domain.interactor.GetFeedTabUseCase;
import com.tokopedia.home.beranda.domain.interactor.GetHomeFeedUseCase;
import com.tokopedia.home.beranda.presentation.presenter.HomeFeedPresenter;
import com.tokopedia.home.common.HomeDataApi;
import com.tokopedia.home.beranda.di.HomeScope;
import com.tokopedia.home.beranda.domain.interactor.GetHomeDataUseCase;
import com.tokopedia.home.beranda.domain.interactor.GetLocalHomeDataUseCase;
import com.tokopedia.home.beranda.presentation.presenter.HomePresenter;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoByDomainUseCase;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.user.session.UserSessionInterface;

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
    protected HomePresenter homePresenter(UserSessionInterface userSession,
                                          GetShopInfoByDomainUseCase getShopInfoByDomainUseCase) {
        return realHomePresenter(userSession, getShopInfoByDomainUseCase);
    }

    @Provides
    protected HomeFeedPresenter homeFeedPresenter() {
        return new HomeFeedPresenter();
    }

    protected HomePresenter realHomePresenter(UserSessionInterface userSession,
                                              GetShopInfoByDomainUseCase getShopInfoByDomainUseCase){
        return new HomePresenter(userSession, getShopInfoByDomainUseCase);
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
                                                           HomeFeedMapper homeFeedMapper,
                                                           CheckNullMapper checkNullMapper){
        return new GetHomeFeedUseCase(context, graphqlUseCase, homeFeedMapper, checkNullMapper);
    }

    @Provides
    protected GetFeedTabUseCase provideGetFeedTabUseCase(@ApplicationContext Context context,
                                                         GraphqlUseCase graphqlUseCase,
                                                         FeedTabMapper feedTabMapper,
                                                         CheckNullMapper checkNullMapper){
        return new GetFeedTabUseCase(context, graphqlUseCase, feedTabMapper, checkNullMapper);
    }

    @Provides
    CheckNullMapper checkNullMapper() {
        return new CheckNullMapper();
    }

    @Provides
    FeedTabMapper feedTabMapper() {
        return new FeedTabMapper();
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
    protected UserSessionInterface provideUserSession(
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
    protected DigitalGqlApiService provideDigitalGqlApiService() {
        return new DigitalGqlApiService();
    }

    @HomeScope
    @Provides
    protected RecommendationListDataSource provideRecommendationListDataSource(
            DigitalGqlApiService digitalGqlApiService, @ApplicationContext Context context) {
        return new RecommendationListDataSource(digitalGqlApiService, context);
    }

    @HomeScope
    @Provides
    protected DigitalWidgetRepository providetDigitalWidgetRepository(
            StatusDataSource statusDataSource,
            CategoryListDataSource categoryListDataSource,
            RecommendationListDataSource recommendationListDataSource){
        return new DigitalWidgetRepository(
                statusDataSource,
                categoryListDataSource,
                recommendationListDataSource
        );
    }
}
