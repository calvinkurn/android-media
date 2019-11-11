package com.tokopedia.shop.common.di.module

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor.Companion.getInstance
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shop.common.constant.GQLQueryNamedConstant
import com.tokopedia.shop.common.data.source.cloud.api.ShopApi
import com.tokopedia.shop.common.di.ShopCommonModule
import com.tokopedia.shop.common.di.ShopQualifier
import com.tokopedia.shop.common.di.scope.ShopScope
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopReputationUseCase
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named

/**
 * @author sebastianuskh on 4/13/17.
 */
@ShopScope
@Module(includes = [ShopCommonModule::class])
class ShopModule {
    @ShopScope
    @Provides
    fun provideShopApi(@ShopQualifier retrofit: Retrofit): ShopApi {
        return retrofit.create(ShopApi::class.java)
    }

    @Provides
    fun provideGqlRepository(): GraphqlRepository {
        return getInstance().graphqlRepository
    }

    @Provides
    fun provideMultiRequestGraphqlUseCase(): MultiRequestGraphqlUseCase {
        return getInstance().multiRequestGraphqlUseCase
    }

    @Provides
    fun provideGqlGetShopInfoUseCase(graphqlUseCase: MultiRequestGraphqlUseCase?,
                                     @Named(GQLQueryNamedConstant.SHOP_INFO) gqlQuery: String?): GQLGetShopInfoUseCase {
        return GQLGetShopInfoUseCase(gqlQuery!!, graphqlUseCase!!)
    }

    @Provides
    fun provideGetShopReputationUseCase(graphqlUseCase: MultiRequestGraphqlUseCase?,
                                        @Named(GQLQueryNamedConstant.SHOP_REPUTATION) gqlQuery: String?): GetShopReputationUseCase {
        return GetShopReputationUseCase(gqlQuery!!, graphqlUseCase!!)
    }
}