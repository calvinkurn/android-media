package com.tokopedia.shop.common.di.component;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase;
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository;
import com.tokopedia.shop.common.constant.GQLQueryNamedConstant;
import com.tokopedia.shop.common.data.source.cloud.api.ShopApi;
import com.tokopedia.shop.common.di.GqlGetShopInfoForHeaderUseCaseQualifier;
import com.tokopedia.shop.common.di.GqlGetShopInfoForTabUseCaseQualifier;
import com.tokopedia.shop.common.di.ShopPageContext;
import com.tokopedia.shop.common.di.GqlGetShopInfoUseCaseCoreAndAssetsQualifier;
import com.tokopedia.shop.common.di.GqlGetShopInfoUseCaseGoldQualifier;
import com.tokopedia.shop.common.di.GqlGetShopInfoUseCaseHomeTypeQualifier;
import com.tokopedia.shop.common.di.GqlGetShopInfoUseCaseOsQualifier;
import com.tokopedia.shop.common.di.GqlGetShopInfoUseCaseTopContentQualifier;
import com.tokopedia.shop.common.di.module.ShopModule;
import com.tokopedia.shop.common.di.scope.ShopScope;
import com.tokopedia.shop.common.domain.interactor.GQLGetShopFavoriteStatusUseCase;
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoByDomainUseCase;
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase;
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopReputationUseCase;

import javax.inject.Named;

import dagger.Component;
import kotlinx.coroutines.CoroutineDispatcher;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 4/13/17.
 */
@ShopScope
@Component(modules = ShopModule.class, dependencies = BaseAppComponent.class)
public interface ShopComponent {

    @ApplicationContext
    Context getContext();

    CacheApiInterceptor cacheApiInterceptor();

    ShopApi getShopApi();

    AbstractionRouter getAbstractionRouter();

    Retrofit.Builder retrofitBuilder();

    Gson gson();

    TkpdAuthInterceptor tkpdAuthInterceptor();

    HeaderErrorResponseInterceptor headerErrorResponseInterceptor();

    HttpLoggingInterceptor httpLoggingInterceptor();

    GQLGetShopInfoUseCase gqlGetShopInfoUseCase();

    GetShopInfoByDomainUseCase getShopInfoByDomainUseCase();

    ToggleFavouriteShopUseCase toggleFavouriteShopUseCase();

    GQLGetShopFavoriteStatusUseCase getGQLGetShopFavoriteStatusUseCase();

    GetShopReputationUseCase getShopReputationUseCase();

    CoroutineDispatcher getCoroutineDispatcher();

    GraphqlRepository getGraphqlRepository();

    MultiRequestGraphqlUseCase getMultiRequestGraphqlUseCase();

    @Named(GQLQueryNamedConstant.GQL_GET_SHOP_OPERATIONAL_HOUR_STATUS)
    String getGqlQueryShopOperationalHourStatus();

    @GqlGetShopInfoForTabUseCaseQualifier
    GQLGetShopInfoUseCase gqlGetShopInfoForTabUseCase();

    @GqlGetShopInfoUseCaseOsQualifier
    GQLGetShopInfoUseCase gqlGetShopInfoUseCaseOs();

    @GqlGetShopInfoUseCaseGoldQualifier
    GQLGetShopInfoUseCase gqlGetShopInfoUseCaseGold();

    @GqlGetShopInfoUseCaseTopContentQualifier
    GQLGetShopInfoUseCase gqlGetShopInfoTopContentUseCase();

    @GqlGetShopInfoUseCaseHomeTypeQualifier
    GQLGetShopInfoUseCase gqlGetShopInfoHomeTypeUseCase();

    @GqlGetShopInfoUseCaseCoreAndAssetsQualifier
    GQLGetShopInfoUseCase gqlGetShopInfoCoreAndAssetsUseCase();

    @GqlGetShopInfoForHeaderUseCaseQualifier
    GQLGetShopInfoUseCase gqlGetShopInfoForHeaderUseCase();

    @ShopPageContext
    Context provideActivityContext();
}