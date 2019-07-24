package com.tokopedia.shop.common.di.module;

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor;
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase;
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository;
import com.tokopedia.shop.common.constant.GQLQueryNamedConstant;
import com.tokopedia.shop.common.data.source.cloud.api.ShopApi;
import com.tokopedia.shop.common.data.source.cloud.api.ShopWSApi;
import com.tokopedia.shop.common.di.ShopCommonModule;
import com.tokopedia.shop.common.di.ShopQualifier;
import com.tokopedia.shop.common.di.ShopWSQualifier;
import com.tokopedia.shop.common.di.scope.ShopScope;
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase;
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopReputationUseCase;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 4/13/17.
 */
@ShopScope
@Module(includes = ShopCommonModule.class)
public class ShopModule {

    @ShopScope
    @Provides
    public ShopApi provideShopApi(@ShopQualifier Retrofit retrofit) {
        return retrofit.create(ShopApi.class);
    }

    @ShopScope
    @Provides
    public ShopWSApi provideShopWsApi(@ShopWSQualifier Retrofit retrofit) {
        return retrofit.create(ShopWSApi.class);
    }

    @Provides
    public GraphqlRepository provideGqlRepository(){
        return GraphqlInteractor.getInstance().getGraphqlRepository();
    }

    @Provides
    public MultiRequestGraphqlUseCase provideMultiRequestGraphqlUseCase(){
        return GraphqlInteractor.getInstance().getMultiRequestGraphqlUseCase();
    }

    @Provides
    public GQLGetShopInfoUseCase provideGqlGetShopInfoUseCase(MultiRequestGraphqlUseCase graphqlUseCase,
                                                              @Named(GQLQueryNamedConstant.SHOP_INFO)
                                                              String gqlQuery){
        return new GQLGetShopInfoUseCase(gqlQuery, graphqlUseCase);
    }

    @Provides
    public GetShopReputationUseCase provideGetShopReputationUseCase(MultiRequestGraphqlUseCase graphqlUseCase,
                                                                    @Named(GQLQueryNamedConstant.SHOP_REPUTATION)
                                                                    String gqlQuery){
        return new GetShopReputationUseCase(gqlQuery, graphqlUseCase);
    }
}