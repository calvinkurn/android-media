package com.tokopedia.home.explore.di;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.cachemanager.CacheManager;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.home.common.HomeDataApi;
import com.tokopedia.home.explore.data.repository.ExploreRepositoryImpl;
import com.tokopedia.home.explore.data.source.ExploreDataSource;
import com.tokopedia.home.explore.domain.GetExploreDataUseCase;
import com.tokopedia.home.explore.domain.GetExploreLocalDataUseCase;
import com.tokopedia.home.explore.view.presentation.ExplorePresenter;
import com.tokopedia.shop.common.constant.GQLQueryNamedConstant;
import com.tokopedia.shop.common.constant.GqlQueryConstant;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCaseRx;
import com.tokopedia.user.session.UserSession;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

import static com.tokopedia.shop.common.constant.GQLQueryNamedConstant.DEFAULT_SHOP_INFO_QUERY_NAME;

/**
 * Created by errysuprayogi on 2/2/18.
 */

@Module
public class ExploreModule {

    @Provides
    ExplorePresenter explorePresenter(UserSession userSession, GetExploreLocalDataUseCase localDataUseCase, GetExploreDataUseCase dataUseCase) {
        return new ExplorePresenter(dataUseCase, localDataUseCase, userSession);
    }

    @ExploreScope
    @Provides
    UserSession getUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @ExploreScope
    @Provides
    GraphqlUseCase graphqlUseCase() {
        return new GraphqlUseCase();
    }

    @ExploreScope
    @Provides
    ExploreDataSource dataSource(@ApplicationContext Context context, HomeDataApi dataApi,
                                 CacheManager cacheManager, UserSession userSession, GetShopInfoUseCaseRx getShopInfoUseCase, Gson gson) {
        return new ExploreDataSource(context, dataApi, cacheManager, getShopInfoUseCase, userSession, gson);
    }

    @ExploreScope
    @Provides
    @Named(GQLQueryNamedConstant.SHOP_INFO)
    String provideQueryGetShopInfo(@ApplicationContext Context context){
        return GqlQueryConstant.INSTANCE.getShopInfoQuery(
                GqlQueryConstant.SHOP_INFO_REQUEST_QUERY_STRING,
                DEFAULT_SHOP_INFO_QUERY_NAME
        );
    }

    @ExploreScope
    @Provides
    GetShopInfoUseCaseRx getShopInfoUseCase(GraphqlUseCase graphqlUseCase, @Named(GQLQueryNamedConstant.SHOP_INFO) String query){
        return new GetShopInfoUseCaseRx(graphqlUseCase, query);
    }

    @ExploreScope
    @Provides
    ExploreRepositoryImpl exploreRepository(@ApplicationContext Context context, ExploreDataSource dataSource) {
        return new ExploreRepositoryImpl(context, dataSource);
    }

    @ExploreScope
    @Provides
    GetExploreDataUseCase getExploreDataUseCase(ExploreRepositoryImpl repository, @ApplicationContext Context context) {
        return new GetExploreDataUseCase(repository, context);
    }

    @ExploreScope
    @Provides
    GetExploreLocalDataUseCase getExploreLocalDataUseCase(ExploreRepositoryImpl repository) {
        return new GetExploreLocalDataUseCase(repository);
    }
}
