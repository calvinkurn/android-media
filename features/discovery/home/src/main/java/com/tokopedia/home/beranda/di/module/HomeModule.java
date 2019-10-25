package com.tokopedia.home.beranda.di.module;

import android.content.Context;
import com.google.gson.Gson;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.paging.PagingHandler;
import com.tokopedia.digital.widget.view.model.mapper.CategoryMapper;
import com.tokopedia.digital.widget.view.model.mapper.StatusMapper;
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor;
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.home.beranda.data.mapper.FeedTabMapper;
import com.tokopedia.home.beranda.data.mapper.HomeFeedMapper;
import com.tokopedia.home.beranda.data.mapper.HomeMapper;
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactory;
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactoryImpl;
import com.tokopedia.home.beranda.data.repository.HomeRepository;
import com.tokopedia.home.beranda.data.repository.HomeRepositoryImpl;
import com.tokopedia.home.beranda.data.source.HomeDataSource;
import com.tokopedia.home.beranda.di.HomeScope;
import com.tokopedia.home.beranda.domain.interactor.*;
import com.tokopedia.home.beranda.presentation.presenter.HomeFeedPresenter;
import com.tokopedia.home.beranda.presentation.presenter.HomePresenter;
import com.tokopedia.home.beranda.presentation.view.viewmodel.ItemTabBusinessViewModel;
import com.tokopedia.home.common.HomeAceApi;
import com.tokopedia.home.common.HomeDataApi;
import com.tokopedia.permissionchecker.PermissionCheckerHelper;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoByDomainUseCase;
import com.tokopedia.stickylogin.domain.usecase.StickyLoginUseCase;
import com.tokopedia.topads.sdk.di.TopAdsWishlistModule;
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;
import dagger.Module;
import dagger.Provides;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.Dispatchers;

import javax.inject.Named;

/**
 * @author by errysuprayogi on 11/28/17.
 */

@Module(includes = TopAdsWishlistModule.class)
public class HomeModule {

    @HomeScope
    @Provides
    protected HomeMapper providehomeMapper(@ApplicationContext Context context,
                                           HomeVisitableFactory homeVisitableFactory){
        return new HomeMapper(context, homeVisitableFactory);
    }

    @HomeScope
    @Provides
    protected HomePresenter homePresenter(UserSessionInterface userSession,
                                          GetShopInfoByDomainUseCase getShopInfoByDomainUseCase) {
        return realHomePresenter(userSession, getShopInfoByDomainUseCase);
    }

    @Provides
    protected HomeFeedPresenter homeFeedPresenter(
            GetHomeFeedUseCase getHomeFeedUseCase,
            AddWishListUseCase addWishListUseCase,
            RemoveWishListUseCase removeWishListUseCase,
            TopAdsWishlishedUseCase topAdsWishlishedUseCase,
            UserSessionInterface userSessionInterface
    ) {
        return new HomeFeedPresenter(userSessionInterface, getHomeFeedUseCase, addWishListUseCase, removeWishListUseCase, topAdsWishlishedUseCase);
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
                                                   HomeAceApi homeAceApi,
                                                   HomeMapper homeMapper,
                                                   @ApplicationContext Context context){
        return new HomeDataSource(homeDataApi, homeAceApi, homeMapper, context);
    }

    @Provides
    protected GetHomeDataUseCase provideGetHomeDataUseCase(HomeRepository homeRepository){
        return new GetHomeDataUseCase(homeRepository);
    }

    @Provides
    protected SendGeolocationInfoUseCase provideSendGeolocationInfoUseCase(HomeRepository homeRepository){
        return new SendGeolocationInfoUseCase(homeRepository);
    }


    @Provides
    protected GetHomeFeedUseCase provideGetHomeFeedUseCase(@ApplicationContext Context context,
                                                           GraphqlUseCase graphqlUseCase,
                                                           HomeFeedMapper homeFeedMapper){
        return new GetHomeFeedUseCase(context, graphqlUseCase, homeFeedMapper);
    }

    @Provides
    protected GetFeedTabUseCase provideGetFeedTabUseCase(@ApplicationContext Context context,
                                                         GraphqlUseCase graphqlUseCase,
                                                         FeedTabMapper feedTabMapper){
        return new GetFeedTabUseCase(context, graphqlUseCase, feedTabMapper);
    }

    @Provides
    AddWishListUseCase provideAddWishlistUseCase(@ApplicationContext Context context){
        return new AddWishListUseCase(context);
    }


    @Provides
    RemoveWishListUseCase provideRemoveWishListUseCase(@ApplicationContext Context context){
        return new RemoveWishListUseCase(context);
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
    protected GetKeywordSearchUseCase getKeywordSearchUseCase(@ApplicationContext Context context){
        return new GetKeywordSearchUseCase(context);
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

    @Provides
    protected GraphqlRepository provideGraphqlRepository() {
        return GraphqlInteractor.getInstance().getGraphqlRepository();
    }

    @HomeScope
    @Provides
    @Named("Main")
    protected CoroutineDispatcher provideMainDispatcher() {
        return Dispatchers.getMain();
    }

    @Provides
    @HomeScope
    protected ItemTabBusinessViewModel provideItemTabBusinessViewModel(GraphqlUseCase graphqlUseCase) {
        return new ItemTabBusinessViewModel(graphqlUseCase);
    }

    @Provides
    @HomeScope
    protected PermissionCheckerHelper providePermissionCheckerHelper() {
        return new PermissionCheckerHelper();
    }

    @Provides
    @HomeScope
    protected HomeVisitableFactory provideHomeVisitableFactory() {
        return new HomeVisitableFactoryImpl();
    }
  
    @Provides
    @HomeScope
    protected StickyLoginUseCase provideStickyLoginUseCase(@ApplicationContext Context context, GraphqlRepository graphqlRepository) {
        return new StickyLoginUseCase(context.getResources(), graphqlRepository);
    }

    @Provides
    RemoteConfig provideRemoteConfig(@ApplicationContext Context context) {
        return new FirebaseRemoteConfigImpl(context);
    }
}
