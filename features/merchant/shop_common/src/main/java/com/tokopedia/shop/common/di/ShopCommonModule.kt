package com.tokopedia.shop.common.di

import android.content.Context
import com.tokopedia.abstraction.common.data.model.response.TkpdV4ResponseError
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.NetworkRouter
import com.tokopedia.shop.common.constant.GQLQueryNamedConstant
import com.tokopedia.shop.common.constant.GQLQueryNamedConstant.DEFAULT_SHOP_INFO_QUERY_NAME
import com.tokopedia.shop.common.constant.GQLQueryNamedConstant.SHOP_INFO_CORE_AND_ASSETS_QUERY_NAME
import com.tokopedia.shop.common.constant.GQLQueryNamedConstant.SHOP_INFO_FAVORITE_QUERY_NAME
import com.tokopedia.shop.common.constant.GQLQueryNamedConstant.SHOP_INFO_HEADER_CONTENT_DATA_QUERY_NAME
import com.tokopedia.shop.common.constant.GQLQueryNamedConstant.SHOP_TOP_CONTENT_QUERY_NAME
import com.tokopedia.shop.common.constant.GqlQueryConstant
import com.tokopedia.shop.common.constant.GqlQueryConstant.GQL_GET_SHOP_OPERATIONAL_HOUR_STATUS_QUERY_STRING
import com.tokopedia.shop.common.constant.GqlQueryConstant.QUERY_SHOP_SCORE_STRING
import com.tokopedia.shop.common.constant.GqlQueryConstant.SHOP_REPUTATION_QUERY_STRING
import com.tokopedia.shop.common.constant.GqlQueryConstant.getShopInfoQuery
import com.tokopedia.shop.common.constant.ShopCommonParamApiConstant
import com.tokopedia.shop.common.constant.ShopCommonUrl
import com.tokopedia.shop.common.data.interceptor.ShopAuthInterceptor
import com.tokopedia.shop.common.data.repository.ShopCommonRepositoryImpl
import com.tokopedia.shop.common.data.source.ShopCommonDataSource
import com.tokopedia.shop.common.data.source.cloud.ShopCommonCloudDataSource
import com.tokopedia.shop.common.data.source.cloud.api.ShopCommonApi
import com.tokopedia.shop.common.domain.interactor.GQLGetShopFavoriteStatusUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.domain.interactor.GetShopInfoByDomainUseCase
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.shop.common.domain.repository.ShopCommonRepository
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.Main
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Named

@Module
class ShopCommonModule {
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher {
        return Main
    }

    @Provides
    fun provideToggleFavouriteShopUseCase(@ApplicationContext context: Context): ToggleFavouriteShopUseCase {
        return ToggleFavouriteShopUseCase(GraphqlUseCase(), context.resources)
    }

    @Provides
    @Named(GQLQueryNamedConstant.SHOP_INFO)
    fun provideGqlQueryShopInfo(@ApplicationContext context: Context?): String {
        return getShopInfoQuery(
                GqlQueryConstant.SHOP_INFO_REQUEST_QUERY_STRING,
                DEFAULT_SHOP_INFO_QUERY_NAME
        )
    }

    @Provides
    @Named(GQLQueryNamedConstant.SHOP_INFO_FOR_HEADER)
    fun provideGqlQueryShopInfoForHeader(@ApplicationContext context: Context?): String {
        return getShopInfoQuery(
                GqlQueryConstant.SHOP_INFO_FOR_HEADER_REQUEST_QUERY_STRING,
                SHOP_INFO_HEADER_CONTENT_DATA_QUERY_NAME
        )
    }

    @Provides
    @Named(GQLQueryNamedConstant.SHOP_INFO_FOR_TOP_CONTENT)
    fun provideGqlQueryShopInfoForTopContent(@ApplicationContext context: Context?): String {
        return getShopInfoQuery(
                GqlQueryConstant.SHOP_INFO_FOR_TOP_CONTENT_REQUEST_QUERY_STRING,
                SHOP_TOP_CONTENT_QUERY_NAME
        )
    }

    @Provides
    @Named(GQLQueryNamedConstant.SHOP_INFO_FOR_CORE_AND_ASSETS)
    fun provideGqlQueryShopInfoForCoreAndAssets(@ApplicationContext context: Context?): String {
        return getShopInfoQuery(
                GqlQueryConstant.SHOP_INFO_FOR_CORE_AND_ASSETS_REQUEST_QUERY_STRING,
                SHOP_INFO_CORE_AND_ASSETS_QUERY_NAME
        )
    }

    @Provides
    @Named(GQLQueryNamedConstant.FAVORITE_STATUS_GQL)
    fun provideGqlQueryFavoriteStatus(@ApplicationContext context: Context?): String {
        return getShopInfoQuery(
                GqlQueryConstant.FAVORITE_STATUS_GQL_STRING,
                SHOP_INFO_FAVORITE_QUERY_NAME
        )
    }

    @Provides
    @Named(GQLQueryNamedConstant.SHOP_REPUTATION)
    fun provideGqlQueryShopReputation(@ApplicationContext context: Context?): String {
        return SHOP_REPUTATION_QUERY_STRING
    }

    @Provides
    @Named(GQLQueryNamedConstant.GQL_GET_SHOP_OPERATIONAL_HOUR_STATUS)
    fun provideGqlQueryShopOperationalHourStatus(@ApplicationContext context: Context?): String {
        return GQL_GET_SHOP_OPERATIONAL_HOUR_STATUS_QUERY_STRING
    }

    @Provides
    fun provideGetShopInfoByDomainUseCase(shopCommonRepository: ShopCommonRepository?): GetShopInfoByDomainUseCase {
        return GetShopInfoByDomainUseCase(shopCommonRepository!!)
    }

    @Provides
    fun provideShopCommonRepository(shopInfoDataSource: ShopCommonDataSource?): ShopCommonRepository {
        return ShopCommonRepositoryImpl(shopInfoDataSource!!)
    }

    @Provides
    fun provideShopCommonCloudDataSource(shopCommonApi: ShopCommonApi?): ShopCommonCloudDataSource {
        return ShopCommonCloudDataSource(shopCommonApi)
    }

    @Provides
    fun provideShopCommonDataSource(shopInfoCloudDataSource: ShopCommonCloudDataSource?): ShopCommonDataSource {
        return ShopCommonDataSource(shopInfoCloudDataSource)
    }

    @Provides
    fun provideShopCommonApi(@ShopQualifier retrofit: Retrofit): ShopCommonApi {
        return retrofit.create(ShopCommonApi::class.java)
    }

    @ShopQualifier
    @Provides
    fun provideOkHttpClient(shopAuthInterceptor: ShopAuthInterceptor?,
                            @ApplicationScope httpLoggingInterceptor: HttpLoggingInterceptor?,
                            errorResponseInterceptor: ErrorResponseInterceptor?): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(shopAuthInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build()
    }

    @ShopQualifier
    @Provides
    fun provideRetrofit(@ShopQualifier okHttpClient: OkHttpClient?,
                        retrofitBuilder: Retrofit.Builder): Retrofit {
        return retrofitBuilder.baseUrl(ShopCommonUrl.BASE_URL).client(okHttpClient).build()
    }

    @Provides
    fun provideErrorResponseInterceptor(): ErrorResponseInterceptor {
        return ErrorResponseInterceptor(TkpdV4ResponseError::class.java)
    }

    @ShopWSQualifier
    @Provides
    fun provideWSRetrofit(@ShopQualifier okHttpClient: OkHttpClient?,
                          retrofitBuilder: Retrofit.Builder): Retrofit {
        return retrofitBuilder.baseUrl(ShopCommonUrl.BASE_WS_URL).client(okHttpClient).build()
    }

    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @Provides
    @Named(ShopCommonParamApiConstant.QUERY_SHOP_SCORE)
    fun provideQueryShopScore(@ApplicationContext context: Context?): String {
        return QUERY_SHOP_SCORE_STRING
    }

    @Provides
    fun provideFavorite(graphqlUseCase: MultiRequestGraphqlUseCase?,
                        @Named(GQLQueryNamedConstant.FAVORITE_STATUS_GQL) gqlQuery: String?): GQLGetShopFavoriteStatusUseCase {
        return GQLGetShopFavoriteStatusUseCase(gqlQuery!!, graphqlUseCase!!)
    }

    @Provides
    fun provideGqlGetShopInfoUseCase(graphqlUseCase: MultiRequestGraphqlUseCase?,
                                     @Named(GQLQueryNamedConstant.SHOP_INFO) gqlQuery: String?): GQLGetShopInfoUseCase {
        return GQLGetShopInfoUseCase(gqlQuery!!, graphqlUseCase!!)
    }

    @GqlGetShopInfoForHeaderUseCaseQualifier
    @Provides
    fun provideGqlGetShopInfoForHeaderUseCase(graphqlUseCase: MultiRequestGraphqlUseCase?,
                                              @Named(GQLQueryNamedConstant.SHOP_INFO_FOR_HEADER) gqlQuery: String?): GQLGetShopInfoUseCase {
        return GQLGetShopInfoUseCase(gqlQuery!!, graphqlUseCase!!)
    }

    @GqlGetShopInfoUseCaseTopContentQualifier
    @Provides
    fun provideGqlGetShopInfoUseCaseTopContent(graphqlUseCase: MultiRequestGraphqlUseCase?,
                                               @Named(GQLQueryNamedConstant.SHOP_INFO_FOR_TOP_CONTENT) gqlQuery: String?): GQLGetShopInfoUseCase {
        return GQLGetShopInfoUseCase(gqlQuery!!, graphqlUseCase!!)
    }

    @GqlGetShopInfoUseCaseCoreAndAssetsQualifier
    @Provides
    fun provideGqlGetShopInfoUseCaseCoreAndAssets(graphqlUseCase: MultiRequestGraphqlUseCase?,
                                                  @Named(GQLQueryNamedConstant.SHOP_INFO_FOR_CORE_AND_ASSETS) gqlQuery: String?): GQLGetShopInfoUseCase {
        return GQLGetShopInfoUseCase(gqlQuery!!, graphqlUseCase!!)
    }
}