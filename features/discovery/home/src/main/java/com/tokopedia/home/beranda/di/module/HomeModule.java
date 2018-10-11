package com.tokopedia.home.beranda.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.paging.PagingHandler;
import com.tokopedia.home.beranda.data.mapper.HomeMapper;
import com.tokopedia.home.beranda.data.repository.HomeRepository;
import com.tokopedia.home.beranda.data.repository.HomeRepositoryImpl;
import com.tokopedia.home.beranda.data.source.HomeDataSource;
import com.tokopedia.home.common.HomeDataApi;
import com.tokopedia.home.beranda.di.HomeScope;
import com.tokopedia.home.beranda.domain.interactor.GetHomeDataUseCase;
import com.tokopedia.home.beranda.domain.interactor.GetLocalHomeDataUseCase;
import com.tokopedia.home.beranda.presentation.presenter.HomePresenter;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoByDomainUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * @author by errysuprayogi on 11/28/17.
 */

@Module(includes = HomeFeedModule.class)
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
    protected com.tokopedia.user.session.UserSession provideUserSession(
            @ApplicationContext Context context){
        return new com.tokopedia.user.session.UserSession(context);
    }

    @HomeScope
    @Provides
    protected GetLocalHomeDataUseCase getLocalHomeDataUseCase(HomeRepository repository){
        return new GetLocalHomeDataUseCase(repository);
    }
}
