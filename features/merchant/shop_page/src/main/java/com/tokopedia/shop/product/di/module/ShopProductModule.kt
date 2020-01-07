package com.tokopedia.shop.product.di.module

import android.content.Context
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor
import com.tokopedia.gm.common.constant.GMCommonUrl
import com.tokopedia.gm.common.data.interceptor.GMAuthInterceptor
import com.tokopedia.gm.common.data.repository.GMCommonRepositoryImpl
import com.tokopedia.gm.common.data.source.GMCommonDataSource
import com.tokopedia.gm.common.data.source.cloud.GMCommonCloudDataSource
import com.tokopedia.gm.common.data.source.cloud.api.GMCommonApi
import com.tokopedia.gm.common.domain.repository.GMCommonRepository
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.NetworkRouter
import com.tokopedia.shop.R
import com.tokopedia.shop.common.constant.ShopCommonParamApiConstant
import com.tokopedia.shop.common.constant.ShopCommonParamApiConstant.GQL_PRODUCT_LIST
import com.tokopedia.shop.common.constant.ShopUrl
import com.tokopedia.shop.common.domain.interactor.DeleteShopInfoCacheUseCase
import com.tokopedia.shop.common.graphql.data.stampprogress.MembershipStampProgress
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.ClaimBenefitMembershipUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetMembershipUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetMembershipUseCaseNew
import com.tokopedia.shop.product.data.GQLQueryConstant
import com.tokopedia.shop.product.data.model.ShopFeaturedProduct
import com.tokopedia.shop.product.data.repository.ShopProductRepositoryImpl
import com.tokopedia.shop.product.data.source.cloud.ShopProductCloudDataSource
import com.tokopedia.shop.product.data.source.cloud.api.ShopOfficialStoreApi
import com.tokopedia.shop.product.data.source.cloud.interceptor.ShopOfficialStoreAuthInterceptor
import com.tokopedia.shop.product.di.ShopProductGMFeaturedQualifier
import com.tokopedia.shop.product.di.ShopProductQualifier
import com.tokopedia.shop.product.di.ShopProductWishListFeaturedQualifier
import com.tokopedia.shop.product.di.scope.ShopProductScope
import com.tokopedia.shop.product.domain.interactor.*
import com.tokopedia.shop.product.domain.repository.ShopProductRepository
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.constant.WishListCommonUrl
import com.tokopedia.wishlist.common.data.interceptor.WishListAuthInterceptor
import com.tokopedia.wishlist.common.data.repository.WishListCommonRepositoryImpl
import com.tokopedia.wishlist.common.data.source.WishListCommonDataSource
import com.tokopedia.wishlist.common.data.source.cloud.WishListCommonCloudDataSource
import com.tokopedia.wishlist.common.data.source.cloud.api.WishListCommonApi
import com.tokopedia.wishlist.common.data.source.cloud.mapper.WishListProductListMapper
import com.tokopedia.wishlist.common.domain.interactor.GetWishListUseCase
import com.tokopedia.wishlist.common.domain.repository.WishListCommonRepository
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Named

@ShopProductScope
@Module(includes = [ShopProductViewModelModule::class])
class ShopProductModule {
    @ShopProductScope
    @Provides
    fun getNetworkRouter(@ApplicationContext context: Context?): NetworkRouter? {
        return context as NetworkRouter?
    }

    @ShopProductScope
    @Provides
    @Named(GQLQueryConstant.SHOP_FEATURED_PRODUCT)
    fun getShopFeaturedProductQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_shop_featured_product)
    }

    @ShopProductScope
    @Provides
    @Named(GQLQueryConstant.SHOP_PRODUCT)
    fun getShopProductQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_shop_product)
    }

    @Provides
    @Named(ShopCommonParamApiConstant.QUERY_STAMP_PROGRESS)
    fun provideQueryStampProgress(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.shop.common.R.raw.gql_get_stamp_progress)
    }

    @Provides
    @Named(ShopCommonParamApiConstant.QUERY_CLAIM_MEMBERSHIP)
    fun provideQueryClaimBenefit(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.shop.common.R.raw.gql_mutation_membership_claim)
    }

    @ShopProductScope
    @Provides
    fun provideGetMembershipUseCase(@Named(ShopCommonParamApiConstant.QUERY_STAMP_PROGRESS) gqlQuery: String?,
                                    gqlUseCase: MultiRequestGraphqlUseCase?): GetMembershipUseCase {
        return GetMembershipUseCase(gqlQuery!!, gqlUseCase!!)
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

    @ShopProductScope
    @Provides
    @Named(GQL_PRODUCT_LIST)
    fun provideProductListQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(
                context.resources,
                R.raw.gql_get_product_list
        )
    }

    @ShopProductScope
    @Provides
    fun provideDeleteShopInfoUseCase(@ApplicationContext context: Context ): DeleteShopInfoCacheUseCase {
        return DeleteShopInfoCacheUseCase(context)
    }

    @ShopProductScope
    @Provides
    fun provideClaimBenefitMembershipUseCase(@Named(ShopCommonParamApiConstant.QUERY_CLAIM_MEMBERSHIP) gqlQuery: String?,
                                             gqlUseCase: MultiRequestGraphqlUseCase?): ClaimBenefitMembershipUseCase {
        return ClaimBenefitMembershipUseCase(gqlQuery!!, gqlUseCase!!)
    }

    @ShopProductScope
    @Provides
    fun getShopFeaturedProductUseCase(@Named(GQLQueryConstant.SHOP_FEATURED_PRODUCT) gqlQuery: String?,
                                      gqlUseCase: MultiRequestGraphqlUseCase?): GetShopFeaturedProductUseCase {
        return GetShopFeaturedProductUseCase(gqlQuery!!, gqlUseCase!!)
    }

    @Provides
    fun getShopProductUseCase(@Named(GQLQueryConstant.SHOP_PRODUCT) gqlQuery: String?,
                              gqlUseCase: MultiRequestGraphqlUseCase?): GqlGetShopProductUseCase {
        return GqlGetShopProductUseCase(gqlQuery!!, gqlUseCase!!)
    }

    @Provides
    fun provideGMAuthInterceptor(@ApplicationContext context: Context?,
                                 abstractionRouter: AbstractionRouter?): GMAuthInterceptor {
        return GMAuthInterceptor(context, abstractionRouter)
    }

    @ShopProductGMFeaturedQualifier
    @Provides
    fun provideGMOkHttpClient(gmAuthInterceptor: GMAuthInterceptor?,
                              @ApplicationScope httpLoggingInterceptor: HttpLoggingInterceptor?,
                              errorResponseInterceptor: HeaderErrorResponseInterceptor?,
                              cacheApiInterceptor: CacheApiInterceptor?): OkHttpClient {
        return Builder()
            .addInterceptor(cacheApiInterceptor)
            .addInterceptor(gmAuthInterceptor)
            .addInterceptor(errorResponseInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @ShopProductGMFeaturedQualifier
    @ShopProductScope
    @Provides
    fun provideGMRetrofit(@ShopProductGMFeaturedQualifier okHttpClient: OkHttpClient?,
                          retrofitBuilder: Retrofit.Builder): Retrofit {
        return retrofitBuilder.baseUrl(GMCommonUrl.BASE_URL).client(okHttpClient).build()
    }

    @ShopProductScope
    @Provides
    fun provideGMCommonApi(@ShopProductGMFeaturedQualifier retrofit: Retrofit): GMCommonApi {
        return retrofit.create(GMCommonApi::class.java)
    }

    @ShopProductScope
    @Provides
    fun provideGMCommonCloudDataSource(gmCommonApi: GMCommonApi?): GMCommonCloudDataSource {
        return GMCommonCloudDataSource(gmCommonApi)
    }

    @ShopProductScope
    @Provides
    fun provideGMCommonDataSource(gmCommonCloudDataSource: GMCommonCloudDataSource?): GMCommonDataSource {
        return GMCommonDataSource(gmCommonCloudDataSource)
    }

    @ShopProductScope
    @Provides
    fun provideGMCommonRepository(gmCommonDataSource: GMCommonDataSource?): GMCommonRepository {
        return GMCommonRepositoryImpl(gmCommonDataSource)
    }

    // WishList
    @Provides
    fun provideWishListAuthInterceptor(@ApplicationContext context: Context?,
                                       networkRouter: NetworkRouter?,
                                       userSessionInterface: UserSessionInterface?): WishListAuthInterceptor {
        return WishListAuthInterceptor(context, networkRouter, userSessionInterface)
    }

    @ShopProductWishListFeaturedQualifier
    @Provides
    fun provideWishListOkHttpClient(wishListAuthInterceptor: WishListAuthInterceptor?,
                                    @ApplicationScope httpLoggingInterceptor: HttpLoggingInterceptor?,
                                    errorResponseInterceptor: HeaderErrorResponseInterceptor?): OkHttpClient {
        return Builder()
            .addInterceptor(wishListAuthInterceptor)
            .addInterceptor(errorResponseInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @ShopProductWishListFeaturedQualifier
    @ShopProductScope
    @Provides
    fun provideWishListRetrofit(@ShopProductWishListFeaturedQualifier okHttpClient: OkHttpClient?,
                                retrofitBuilder: Retrofit.Builder): Retrofit {
        return retrofitBuilder.baseUrl(WishListCommonUrl.BASE_URL).client(okHttpClient).build()
    }

    @ShopProductScope
    @Provides
    fun provideWishListCommonApi(@ShopProductWishListFeaturedQualifier retrofit: Retrofit): WishListCommonApi {
        return retrofit.create(WishListCommonApi::class.java)
    }

    @ShopProductScope
    @Provides
    fun provideWishListProductListMapper(): WishListProductListMapper {
        return WishListProductListMapper()
    }

    @ShopProductScope
    @Provides
    fun provideWishListCommonCloudDataSource(wishListCommonApi: WishListCommonApi?): WishListCommonCloudDataSource {
        return WishListCommonCloudDataSource(wishListCommonApi)
    }

    @ShopProductScope
    @Provides
    fun provideWishListCommonDataSource(wishListCommonCloudDataSource: WishListCommonCloudDataSource?): WishListCommonDataSource {
        return WishListCommonDataSource(wishListCommonCloudDataSource)
    }

    @ShopProductScope
    @Provides
    fun provideWishListCommonRepository(wishListCommonDataSource: WishListCommonDataSource?): WishListCommonRepository {
        return WishListCommonRepositoryImpl(wishListCommonDataSource)
    }

    @ShopProductScope
    @Provides
    fun provideGetWishListUseCase(wishListCommonRepository: WishListCommonRepository?): GetWishListUseCase {
        return GetWishListUseCase(wishListCommonRepository)
    }

    @ShopProductScope
    @Provides
    fun provideAddToWishListUseCase(@ApplicationContext context: Context?): AddWishListUseCase {
        return AddWishListUseCase(context)
    }

    @ShopProductScope
    @Provides
    fun provideRemoveFromWishListUseCase(@ApplicationContext context: Context?): RemoveWishListUseCase {
        return RemoveWishListUseCase(context)
    }

    // Product
    @Provides
    fun provideShopOfficialStoreAuthInterceptor(@ApplicationContext context: Context?,
                                                networkRouter: NetworkRouter?,
                                                userSessionInterface: UserSessionInterface?): ShopOfficialStoreAuthInterceptor {
        return ShopOfficialStoreAuthInterceptor(context, networkRouter, userSessionInterface)
    }

    @ShopProductQualifier
    @Provides
    fun provideOfficialStoreOkHttpClient(shopOfficialStoreAuthInterceptor: ShopOfficialStoreAuthInterceptor?,
                                         @ApplicationScope httpLoggingInterceptor: HttpLoggingInterceptor?,
                                         errorResponseInterceptor: HeaderErrorResponseInterceptor?,
                                         cacheApiInterceptor: CacheApiInterceptor?): OkHttpClient {
        return Builder()
            .addInterceptor(cacheApiInterceptor)
            .addInterceptor(shopOfficialStoreAuthInterceptor)
            .addInterceptor(errorResponseInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @ShopProductQualifier
    @ShopProductScope
    @Provides
    fun provideOfficialStoreRetrofit(@ShopProductQualifier okHttpClient: OkHttpClient?, retrofitBuilder: Retrofit.Builder): Retrofit {
        return retrofitBuilder.baseUrl(ShopUrl.BASE_OFFICIAL_STORE_URL).client(okHttpClient).build()
    }

    @ShopProductScope
    @Provides
    fun provideShopOfficialStoreApi(@ShopProductQualifier retrofit: Retrofit): ShopOfficialStoreApi {
        return retrofit.create(ShopOfficialStoreApi::class.java)
    }

    @ShopProductScope
    @Provides
    fun provideShopProductRepository(shopProductDataSource: ShopProductCloudDataSource?): ShopProductRepository {
        return ShopProductRepositoryImpl(shopProductDataSource)
    }

    @ShopProductScope
    @Provides
    fun provideDeleteShopProductTomeUseCase(@ApplicationContext context: Context?): DeleteShopProductTomeUseCase {
        return DeleteShopProductTomeUseCase(context)
    }

    @ShopProductScope
    @Provides
    fun provideDeleteShopProductAceUseCase(@ApplicationContext context: Context?): DeleteShopProductAceUseCase {
        return DeleteShopProductAceUseCase(context)
    }

    @ShopProductScope
    @Provides
    fun provideGetProductCampaignsUseCase(wishListCommonRepository: ShopProductRepository?): GetProductCampaignsUseCase {
        return GetProductCampaignsUseCase(wishListCommonRepository)
    }

    @ShopProductScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context?): UserSessionInterface {
        return UserSession(context)
    }
}