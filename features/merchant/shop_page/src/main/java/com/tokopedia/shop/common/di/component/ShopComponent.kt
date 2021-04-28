package com.tokopedia.shop.common.di.component

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.shop.common.constant.GQLQueryNamedConstant
import com.tokopedia.shop.common.data.source.cloud.api.ShopApi
import com.tokopedia.shop.common.di.GqlGetShopInfoForHeaderUseCaseQualifier
import com.tokopedia.shop.common.di.GqlGetShopInfoUseCaseCoreAndAssetsQualifier
import com.tokopedia.shop.common.di.GqlGetShopInfoUseCaseTopContentQualifier
import com.tokopedia.shop.common.di.ShopPageContext
import com.tokopedia.shop.common.di.module.ShopModule
import com.tokopedia.shop.common.di.scope.ShopScope
import com.tokopedia.shop.common.domain.interactor.GQLGetShopFavoriteStatusUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.domain.interactor.GetShopInfoByDomainUseCase
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.shop.common.domain.GetShopReputationUseCase
import dagger.Component
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Named

/**
 * @author sebastianuskh on 4/13/17.
 */
@ShopScope
@Component(modules = [ShopModule::class], dependencies = [BaseAppComponent::class])
interface ShopComponent {
    @get:ApplicationContext
    val context: Context
    val shopApi: ShopApi
    val abstractionRouter: AbstractionRouter
    fun retrofitBuilder(): Retrofit.Builder
    fun gson(): Gson
    fun tkpdAuthInterceptor(): TkpdAuthInterceptor
    fun headerErrorResponseInterceptor(): HeaderErrorResponseInterceptor
    fun httpLoggingInterceptor(): HttpLoggingInterceptor
    fun gqlGetShopInfoUseCase(): GQLGetShopInfoUseCase
    val shopInfoByDomainUseCase: GetShopInfoByDomainUseCase
    fun toggleFavouriteShopUseCase(): ToggleFavouriteShopUseCase
    val gQLGetShopFavoriteStatusUseCase: GQLGetShopFavoriteStatusUseCase
    val shopReputationUseCase: GetShopReputationUseCase
    val graphqlRepository: GraphqlRepository
    val multiRequestGraphqlUseCase: MultiRequestGraphqlUseCase
    fun provideCoroutineDispatchers(): CoroutineDispatchers

    @get:Named(GQLQueryNamedConstant.GQL_GET_SHOP_OPERATIONAL_HOUR_STATUS)
    val gqlQueryShopOperationalHourStatus: String

    @GqlGetShopInfoUseCaseTopContentQualifier
    fun gqlGetShopInfoTopContentUseCase(): GQLGetShopInfoUseCase

    @GqlGetShopInfoUseCaseCoreAndAssetsQualifier
    fun gqlGetShopInfoCoreAndAssetsUseCase(): GQLGetShopInfoUseCase

    @GqlGetShopInfoForHeaderUseCaseQualifier
    fun gqlGetShopInfoForHeaderUseCase(): GQLGetShopInfoUseCase

    @ShopPageContext
    fun provideActivityContext(): Context

    @get:Named(GQLQueryNamedConstant.SHOP_INFO_FOR_TOP_CONTENT)
    val gqlShopInfoForTopContent: String

    @get:Named(GQLQueryNamedConstant.SHOP_INFO_FOR_CORE_AND_ASSETS)
    val gqlShopInfoForCoreAndAssets: String
}