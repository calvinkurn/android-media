package com.tokopedia.shop.product.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
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
import com.tokopedia.shop.common.constant.ShopCommonParamApiConstant
import com.tokopedia.shop.common.constant.ShopCommonParamApiConstant.GQL_PRODUCT_LIST
import com.tokopedia.shop.common.constant.ShopUrl
import com.tokopedia.shop.common.data.interceptor.ShopAuthInterceptor
import com.tokopedia.shop.common.di.ShopPageContext
import com.tokopedia.shop.common.graphql.data.stampprogress.MembershipStampProgress
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.ClaimBenefitMembershipUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetMembershipUseCase
import com.tokopedia.shop.product.data.model.ShopFeaturedProduct
import com.tokopedia.shop.product.data.repository.ShopProductRepositoryImpl
import com.tokopedia.shop.product.data.source.cloud.ShopProductCloudDataSource
import com.tokopedia.shop.product.data.source.cloud.api.ShopOfficialStoreApi
import com.tokopedia.shop.product.data.source.cloud.interceptor.ShopOfficialStoreAuthInterceptor
import com.tokopedia.shop.product.di.ShopProductGMFeaturedQualifier
import com.tokopedia.shop.product.di.ShopProductQualifier
import com.tokopedia.shop.product.di.ShopProductSortQualifier
import com.tokopedia.shop.product.di.ShopProductWishListFeaturedQualifier
import com.tokopedia.shop.product.di.scope.ShopProductScope
import com.tokopedia.shop.product.domain.interactor.GetProductCampaignsUseCase
import com.tokopedia.shop.product.domain.repository.ShopProductRepository
import com.tokopedia.shop.sort.data.repository.ShopProductSortRepositoryImpl
import com.tokopedia.shop.sort.data.source.cloud.ShopProductSortCloudDataSource
import com.tokopedia.shop.sort.data.source.cloud.api.ShopAceApi
import com.tokopedia.shop.sort.domain.repository.ShopProductSortRepository
import com.tokopedia.shop.sort.view.mapper.ShopProductSortMapper
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
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Named

@Module(includes = [ShopProductViewModelModule::class])
class ShopProductModule {
    @ShopProductScope
    @Provides
    fun getNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @Provides
    @Named(ShopCommonParamApiConstant.QUERY_STAMP_PROGRESS)
    fun provideQueryStampProgress(@ShopPageContext context: Context): String {
        return """
            query membershipStampProgress(${'$'}shopId: Int!){
              membershipStampProgress(shopID:${'$'}shopId) {
                isUserRegistered
                isShown
                program {
                  id
                  cardID
                  sectionID
                  quests {
                    id
                    title
                    iconURL
                    questUserID
                    status
                    taskID
                    currentProgress
                    targetProgress
                    actionButton {
                      text
                      isShown
                    }
                  }
                }
                infoMessage {
                  title
                  cta {
                    text
                    url
                    appLink
                  }
                }
              }
              }
        """.trimIndent()
    }

    @Provides
    @Named(ShopCommonParamApiConstant.QUERY_CLAIM_MEMBERSHIP)
    fun provideQueryClaimBenefit(@ShopPageContext context: Context): String {
        return """
            mutation membershipClaimBenefit(${'$'}questUserId:Int!){
              membershipClaimBenefit(questUserID:${'$'}questUserId ){
                title
                subTitle
                resultStatus{
                  code
                  message
                  reason
                }
              }
            }
        """.trimIndent()
    }

    @ShopProductScope
    @Provides
    fun provideGetMembershipUseCase(@Named(ShopCommonParamApiConstant.QUERY_STAMP_PROGRESS) gqlQuery: String,
                                    gqlUseCase: MultiRequestGraphqlUseCase): GetMembershipUseCase {
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
    fun provideProductListQuery(@ShopPageContext context: Context): String {
        return """
            query GetProductList(${'$'}shopId:String!,${'$'}filter:ProductListFilter!){
              GetProductList(shopID:${'$'}shopId, filter:${'$'}filter){
                status
                errors
                totalData
                links{
                  self
                  next
                  prev
                }
                data{
                  product_id
                  condition
                  name
                  name_encoded
                  position
                  product_url
                  status
                  stock
                  minimum_order
                  cashback {
                    cashback
                    cashback_amount
                  }
                  price{
                    currency_id
                    currency_text
                    value
                    value_idr
                    text
                    text_idr
                    identifier
                  }
                  flag{
                    is_variant
                    is_featured
                    is_preorder
                    with_stock
                    is_freereturn
                  }
                  primary_image{
                    original
                    thumbnail
                    resize300
                  }
                }
              }
            }
        """.trimIndent()
    }

    @ShopProductScope
    @Provides
    fun provideClaimBenefitMembershipUseCase(@Named(ShopCommonParamApiConstant.QUERY_CLAIM_MEMBERSHIP) gqlQuery: String,
                                             gqlUseCase: MultiRequestGraphqlUseCase): ClaimBenefitMembershipUseCase {
        return ClaimBenefitMembershipUseCase(gqlQuery!!, gqlUseCase!!)
    }

    @Provides
    fun provideGMAuthInterceptor(@ShopPageContext context: Context,
                                 userSession: UserSessionInterface,
                                 abstractionRouter: NetworkRouter): GMAuthInterceptor {
        return GMAuthInterceptor(context, userSession, abstractionRouter)
    }

    @ShopProductGMFeaturedQualifier
    @Provides
    fun provideGMOkHttpClient(gmAuthInterceptor: GMAuthInterceptor?,
                              @ApplicationScope httpLoggingInterceptor: HttpLoggingInterceptor?,
                              errorResponseInterceptor: HeaderErrorResponseInterceptor?): OkHttpClient {
        return Builder()
            .addInterceptor(gmAuthInterceptor)
            .addInterceptor(errorResponseInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @ShopProductGMFeaturedQualifier
    @ShopProductScope
    @Provides
    fun provideGMRetrofit(@ShopProductGMFeaturedQualifier okHttpClient: OkHttpClient,
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

    // WishList
    @Provides
    fun provideWishListAuthInterceptor(@ShopPageContext context: Context,
                                       networkRouter: NetworkRouter,
                                       userSessionInterface: UserSessionInterface): WishListAuthInterceptor {
        return WishListAuthInterceptor(context, networkRouter, userSessionInterface)
    }

    @ShopProductWishListFeaturedQualifier
    @Provides
    fun provideWishListOkHttpClient(wishListAuthInterceptor: WishListAuthInterceptor,
                                    @ApplicationScope httpLoggingInterceptor: HttpLoggingInterceptor,
                                    errorResponseInterceptor: HeaderErrorResponseInterceptor): OkHttpClient {
        return Builder()
            .addInterceptor(wishListAuthInterceptor)
            .addInterceptor(errorResponseInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @ShopProductWishListFeaturedQualifier
    @ShopProductScope
    @Provides
    fun provideWishListRetrofit(@ShopProductWishListFeaturedQualifier okHttpClient: OkHttpClient,
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
    fun provideWishListCommonCloudDataSource(wishListCommonApi: WishListCommonApi): WishListCommonCloudDataSource {
        return WishListCommonCloudDataSource(wishListCommonApi)
    }

    @ShopProductScope
    @Provides
    fun provideWishListCommonDataSource(wishListCommonCloudDataSource: WishListCommonCloudDataSource): WishListCommonDataSource {
        return WishListCommonDataSource(wishListCommonCloudDataSource)
    }

    @ShopProductScope
    @Provides
    fun provideWishListCommonRepository(wishListCommonDataSource: WishListCommonDataSource): WishListCommonRepository {
        return WishListCommonRepositoryImpl(wishListCommonDataSource)
    }

    @ShopProductScope
    @Provides
    fun provideGetWishListUseCase(wishListCommonRepository: WishListCommonRepository): GetWishListUseCase {
        return GetWishListUseCase(wishListCommonRepository)
    }

    // Product
    @Provides
    fun provideShopOfficialStoreAuthInterceptor(@ShopPageContext context: Context,
                                                networkRouter: NetworkRouter,
                                                userSessionInterface: UserSessionInterface): ShopOfficialStoreAuthInterceptor {
        return ShopOfficialStoreAuthInterceptor(context, networkRouter, userSessionInterface)
    }

    @ShopProductQualifier
    @Provides
    fun provideOfficialStoreOkHttpClient(shopOfficialStoreAuthInterceptor: ShopOfficialStoreAuthInterceptor?,
                                         @ApplicationScope httpLoggingInterceptor: HttpLoggingInterceptor?,
                                         errorResponseInterceptor: HeaderErrorResponseInterceptor?): OkHttpClient {
        return Builder()
            .addInterceptor(shopOfficialStoreAuthInterceptor)
            .addInterceptor(errorResponseInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @ShopProductQualifier
    @ShopProductScope
    @Provides
    fun provideOfficialStoreRetrofit(@ShopProductQualifier okHttpClient: OkHttpClient, retrofitBuilder: Retrofit.Builder): Retrofit {
        return retrofitBuilder.baseUrl(ShopUrl.BASE_OFFICIAL_STORE_URL).client(okHttpClient).build()
    }

    @ShopProductScope
    @Provides
    fun provideShopOfficialStoreApi(@ShopProductQualifier retrofit: Retrofit): ShopOfficialStoreApi {
        return retrofit.create(ShopOfficialStoreApi::class.java)
    }

    @ShopProductScope
    @Provides
    fun provideShopProductRepository(shopProductDataSource: ShopProductCloudDataSource): ShopProductRepository {
        return ShopProductRepositoryImpl(shopProductDataSource)
    }

    @ShopProductScope
    @Provides
    fun provideGetProductCampaignsUseCase(wishListCommonRepository: ShopProductRepository): GetProductCampaignsUseCase {
        return GetProductCampaignsUseCase(wishListCommonRepository)
    }

    @ShopProductScope
    @Provides
    fun provideUserSessionInterface(@ShopPageContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @ShopProductSortQualifier
    @ShopProductScope
    @Provides
    fun provideOkHttpClient(shopAuthInterceptor: ShopAuthInterceptor?,
                            @ApplicationScope httpLoggingInterceptor: HttpLoggingInterceptor?,
                            errorResponseInterceptor: HeaderErrorResponseInterceptor?): OkHttpClient? {
        return Builder()
                .addInterceptor(shopAuthInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build()
    }

    @ShopProductSortQualifier
    @ShopProductScope
    @Provides
    fun provideShopAceRetrofit(@ShopProductSortQualifier okHttpClient: OkHttpClient,
                               retrofitBuilder: Retrofit.Builder): Retrofit {
        return retrofitBuilder.baseUrl(ShopUrl.BASE_ACE_URL).client(okHttpClient).build()
    }

    @ShopProductScope
    @Provides
    fun provideShopAceApi(@ShopProductSortQualifier retrofit: Retrofit): ShopAceApi {
        return retrofit.create(ShopAceApi::class.java)
    }

    @ShopProductScope
    @Provides
    fun provideShopProductSortRepository(shopProductDataSource: ShopProductSortCloudDataSource): ShopProductSortRepository {
        return ShopProductSortRepositoryImpl(shopProductDataSource)
    }

    @ShopProductScope
    @Provides
    fun provideShopProductSortMapper(): ShopProductSortMapper {
        return ShopProductSortMapper()
    }
}