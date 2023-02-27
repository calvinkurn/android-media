package com.tokopedia.shop.product.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.atc_common.domain.mapper.AddToCartDataMapper
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.gm.common.constant.GMCommonUrl
import com.tokopedia.gm.common.data.interceptor.GMAuthInterceptor
import com.tokopedia.gm.common.data.repository.GMCommonRepositoryImpl
import com.tokopedia.gm.common.data.source.GMCommonDataSource
import com.tokopedia.gm.common.data.source.cloud.GMCommonCloudDataSource
import com.tokopedia.gm.common.data.source.cloud.api.GMCommonApi
import com.tokopedia.gm.common.domain.repository.GMCommonRepository
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.network.NetworkRouter
import com.tokopedia.shop.common.constant.ShopUrl
import com.tokopedia.shop.common.di.ShopPageContext
import com.tokopedia.shop.common.graphql.data.stampprogress.MembershipStampProgress
import com.tokopedia.shop.product.data.model.ShopFeaturedProduct
//import com.tokopedia.shop.product.data.repository.ShopProductRepositoryImpl
//import com.tokopedia.shop.product.data.source.cloud.ShopProductCloudDataSource
//import com.tokopedia.shop.product.data.source.cloud.api.ShopOfficialStoreApi
import com.tokopedia.shop.product.data.source.cloud.interceptor.ShopOfficialStoreAuthInterceptor
import com.tokopedia.shop.product.di.ShopProductGMFeaturedQualifier
import com.tokopedia.shop.product.di.ShopProductQualifier
import com.tokopedia.shop.product.di.scope.ShopProductScope
//import com.tokopedia.shop.product.domain.interactor.GetProductCampaignsUseCase
//import com.tokopedia.shop.product.domain.repository.ShopProductRepository
import com.tokopedia.shop.sort.view.mapper.ShopProductSortMapper
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module(includes = [ShopProductViewModelModule::class])
class ShopProductModule {
    @ShopProductScope
    @Provides
    fun getNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @ShopProductScope
    @Provides
    fun provideGraphqlGetMembershipUseCaseNew(gqlRepository: GraphqlRepository): GraphqlUseCase<MembershipStampProgress> {
        return GraphqlUseCase(gqlRepository)
    }

    @ShopProductScope
    @Provides
    fun provideGraphqlGetShopFeaturedProductUseCaseNew(gqlRepository: GraphqlRepository): GraphqlUseCase<ShopFeaturedProduct.Response> {
        return GraphqlUseCase(gqlRepository)
    }

    @Provides
    fun provideGMAuthInterceptor(
        @ShopPageContext context: Context,
        userSession: UserSessionInterface,
        abstractionRouter: NetworkRouter
    ): GMAuthInterceptor {
        return GMAuthInterceptor(context, userSession, abstractionRouter)
    }

    @ShopProductGMFeaturedQualifier
    @Provides
    fun provideGMOkHttpClient(
        gmAuthInterceptor: GMAuthInterceptor,
        @ApplicationScope httpLoggingInterceptor: HttpLoggingInterceptor,
        errorResponseInterceptor: HeaderErrorResponseInterceptor
    ): OkHttpClient {
        return Builder()
            .addInterceptor(gmAuthInterceptor)
            .addInterceptor(errorResponseInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @ShopProductGMFeaturedQualifier
    @ShopProductScope
    @Provides
    fun provideGMRetrofit(
        @ShopProductGMFeaturedQualifier okHttpClient: OkHttpClient,
        retrofitBuilder: Retrofit.Builder
    ): Retrofit {
        return retrofitBuilder.baseUrl(GMCommonUrl.BASE_URL).client(okHttpClient).build()
    }

    @ShopProductScope
    @Provides
    fun provideGMCommonApi(@ShopProductGMFeaturedQualifier retrofit: Retrofit): GMCommonApi {
        return retrofit.create(GMCommonApi::class.java)
    }

    @ShopProductScope
    @Provides
    fun provideGMCommonCloudDataSource(gmCommonApi: GMCommonApi): GMCommonCloudDataSource {
        return GMCommonCloudDataSource(gmCommonApi)
    }

    @ShopProductScope
    @Provides
    fun provideGMCommonDataSource(gmCommonCloudDataSource: GMCommonCloudDataSource): GMCommonDataSource {
        return GMCommonDataSource(gmCommonCloudDataSource)
    }

    @ShopProductScope
    @Provides
    fun provideGMCommonRepository(gmCommonDataSource: GMCommonDataSource): GMCommonRepository {
        return GMCommonRepositoryImpl(gmCommonDataSource)
    }

//    // Product
//    @Provides
//    fun provideShopOfficialStoreAuthInterceptor(
//        @ShopPageContext context: Context,
//        networkRouter: NetworkRouter,
//        userSessionInterface: UserSessionInterface
//    ): ShopOfficialStoreAuthInterceptor {
//        return ShopOfficialStoreAuthInterceptor(context, networkRouter, userSessionInterface)
//    }

//    @ShopProductQualifier
//    @Provides
//    fun provideOfficialStoreOkHttpClient(
//        shopOfficialStoreAuthInterceptor: ShopOfficialStoreAuthInterceptor,
//        @ApplicationScope httpLoggingInterceptor: HttpLoggingInterceptor,
//        errorResponseInterceptor: HeaderErrorResponseInterceptor
//    ): OkHttpClient {
//        return Builder()
//            .addInterceptor(shopOfficialStoreAuthInterceptor)
//            .addInterceptor(errorResponseInterceptor)
//            .addInterceptor(httpLoggingInterceptor)
//            .build()
//    }

//    @ShopProductQualifier
//    @ShopProductScope
//    @Provides
//    fun provideOfficialStoreRetrofit(@ShopProductQualifier okHttpClient: OkHttpClient, retrofitBuilder: Retrofit.Builder): Retrofit {
//        return retrofitBuilder.baseUrl(ShopUrl.BASE_OFFICIAL_STORE_URL).client(okHttpClient).build()
//    }

//    @ShopProductScope
//    @Provides
//    fun provideShopOfficialStoreApi(@ShopProductQualifier retrofit: Retrofit): ShopOfficialStoreApi {
//        return retrofit.create(ShopOfficialStoreApi::class.java)
//    }

//    @ShopProductScope
//    @Provides
//    fun provideShopProductRepository(shopProductDataSource: ShopProductCloudDataSource): ShopProductRepository {
//        return ShopProductRepositoryImpl(shopProductDataSource)
//    }

//    @ShopProductScope
//    @Provides
//    fun provideGetProductCampaignsUseCase(wishListCommonRepository: ShopProductRepository): GetProductCampaignsUseCase {
//        return GetProductCampaignsUseCase(wishListCommonRepository)
//    }

    @ShopProductScope
    @Provides
    fun provideUserSessionInterface(@ShopPageContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @ShopProductScope
    @Provides
    fun provideShopProductSortMapper(): ShopProductSortMapper {
        return ShopProductSortMapper()
    }

    @ShopProductScope
    @Provides
    fun provideAddToCart(
        graphqlRepository: GraphqlRepository,
        addToCartDataMapper: AddToCartDataMapper,
        chosenAddressRequestHelper: ChosenAddressRequestHelper
    ): AddToCartUseCase {
        return AddToCartUseCase(graphqlRepository, addToCartDataMapper, chosenAddressRequestHelper)
    }

    @ShopProductScope
    @Provides
    fun provideUpdateCart(
        graphqlRepository: GraphqlRepository,
        chosenAddressRequestHelper: ChosenAddressRequestHelper
    ): UpdateCartUseCase {
        return UpdateCartUseCase(graphqlRepository, chosenAddressRequestHelper)
    }

    @ShopProductScope
    @Provides
    fun provideDeleteCart(graphqlRepository: GraphqlRepository): DeleteCartUseCase {
        return DeleteCartUseCase(graphqlRepository)
    }
}
