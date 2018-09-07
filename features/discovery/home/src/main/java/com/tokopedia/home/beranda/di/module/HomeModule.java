package com.tokopedia.home.beranda.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.home.beranda.data.mapper.HomeMapper;
import com.tokopedia.home.beranda.data.repository.HomeRepository;
import com.tokopedia.home.beranda.data.repository.HomeRepositoryImpl;
import com.tokopedia.home.beranda.data.source.HomeDataSource;
import com.tokopedia.home.common.HomeDataApi;
import com.tokopedia.home.beranda.di.HomeScope;
import com.tokopedia.home.beranda.domain.interactor.GetHomeDataUseCase;
import com.tokopedia.home.beranda.domain.interactor.GetLocalHomeDataUseCase;
import com.tokopedia.home.beranda.presentation.presenter.HomePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author by errysuprayogi on 11/28/17.
 */

@Module(includes = HomeFeedModule.class)
public class HomeModule {

    @HomeScope
    @Provides
    HomeMapper providehomeMapper(){
        return new HomeMapper();
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
    HomeRepository homeRepository(HomeDataSource homeDataSource) {
        return new HomeRepositoryImpl(homeDataSource);
    }

    @Provides
    HomeDataSource provideHomeDataSource(HomeDataApi homeDataApi,
                                         HomeMapper homeMapper,
                                         @ApplicationContext Context context,
                                         GlobalCacheManager cacheManager,
                                         Gson gson){
        return new HomeDataSource(homeDataApi, homeMapper, context, cacheManager, gson);
    }

    @Provides
    GetHomeDataUseCase provideGetHomeDataUseCase(HomeRepository homeRepository){
        return new GetHomeDataUseCase(homeRepository);
    }

    @HomeScope
    @Provides
    GetLocalHomeDataUseCase getLocalHomeDataUseCase(HomeRepository repository){
        return new GetLocalHomeDataUseCase(repository);
    }
}
