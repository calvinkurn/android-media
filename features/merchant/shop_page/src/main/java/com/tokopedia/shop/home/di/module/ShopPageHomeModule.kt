package com.tokopedia.shop.home.di.module

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.common.network.coroutines.RestRequestInteractor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.play_common.domain.model.PlayToggleChannelEntity
import com.tokopedia.play_common.domain.usecases.GetPlayWidgetUseCase
import com.tokopedia.play_common.domain.usecases.PlayToggleChannelReminderUseCase
import com.tokopedia.shop.R
import com.tokopedia.network.interceptor.CommonErrorResponseInterceptor
import com.tokopedia.shop.analytic.ShopPageHomeTracking
import com.tokopedia.shop.common.constant.GQLQueryNamedConstant.GQL_CHECK_WISHLIST
import com.tokopedia.shop.common.di.ShopPageContext
import com.tokopedia.shop.home.GqlQueryConstant.GQL_ATC_MUTATION
import com.tokopedia.shop.home.GqlQueryConstant.GQL_CHECK_CAMPAIGN_NOTIFY_ME
import com.tokopedia.shop.home.GqlQueryConstant.GQL_GET_CAMPAIGN_NOTIFY_ME
import com.tokopedia.shop.home.GqlQueryConstant.GQL_GET_SHOP_NPL_CAMPAIGN_TNC
import com.tokopedia.shop.home.GqlQueryConstant.GQL_GET_SHOP_PAGE_HOME_LAYOUT
import com.tokopedia.shop.home.di.scope.ShopPageHomeScope
import com.tokopedia.shop.home.util.CoroutineDispatcherProvider
import com.tokopedia.shop.home.util.CoroutineDispatcherProviderImpl
import com.tokopedia.shop.product.data.GQLQueryConstant
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import com.tokopedia.youtube_common.domain.usecase.GetYoutubeVideoDetailUseCase
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Named

@ShopPageHomeScope
@Module(includes = [ShopPageHomeViewModelModule::class])
class ShopPageHomeModule {

    @ShopPageHomeScope
    @Provides
    @Named(GQL_GET_SHOP_PAGE_HOME_LAYOUT)
    fun getShopPageHomeLayoutQuery(@ShopPageContext context: Context): String {
        return """
            query get_shop_page_home_layout(${'$'}shopId: String!,${'$'}status:String,${'$'}layoutId:String){
              shopPageGetLayout (shopID:${'$'}shopId,status:${'$'}status,layoutID:${'$'}layoutId){
                layoutID
                masterLayoutID
                merchantTierID
                status
                maxWidgets
                publishDate
                widgets {
                  widgetID
                  layoutOrder
                  name
                  type
                  header {
                    title
                    ctaText
                    ctaLink
                    cover
                    ratio
                    isATC
                  }
                  data {
                    ... on DisplayWidget {
                      imageUrl
                      videoUrl
                      appLink
                      webLink
                    }
                    ... on ProductWidget {
                      productID
                      name
                      imageUrl
                      productUrl
                      displayPrice
                      originalPrice
                      discountPercentage
                      isShowFreeOngkir
                      freeOngkirPromoIcon
                      isSoldOut
                      rating
                      totalReview
                      isPO
                      cashback
                    }
                    ... on PromoWidget {
                      voucherID
                      imageUrl
                      name
                      voucherType {
                          voucherType
                          identifier
                      }
                      voucherCode
                      amount {
                        amountType
                        amount
                        amountFormatted
                      }
                      minimumSpend
                      minimumSpendFormatted
                      owner {
                        ownerID
                        identifier
                      }
                      validThru
                      tnc
                      inUseExpiry
                      status {
                        status
                        identifier
                      }
                    }
                    ... on CampaignWidget {
                               campaignID
                               name
                               description
                               startDate
                               endDate
                               statusCampaign
                               timeDescription
                               timeCounter
                               totalNotify
                               totalNotifyWording
                               banners {
                                    imageID
                                    imageURL
                                    bannerType
                               }
                               products {
                                    id
                                    name
                                    url
                                    urlApps
                                    urlMobile
                                    imageURL
                                    price
                                    countSold
                                    stock
                                    status
                                    discountedPrice
                                    discountPercentage
                                    position
                                    stockWording {
                                        title
                                    }
                                    hideGimmick
                                    stockSoldPercentage
                               }
                    }
                  }
                }
              }
            }
        """.trimIndent()
    }

    @ShopPageHomeScope
    @Provides
    @Named(GQL_CHECK_CAMPAIGN_NOTIFY_ME)
    fun getCheckCampaignNotifyMeQuery(@ShopPageContext context: Context): String {
        return """
            mutation check_campaign_notify_me(${'$'}params : CheckCampaignNotifyMeRequest!){
              checkCampaignNotifyMe(params:${'$'}params ) {
                campaign_id
                success
                message
                error_message
              }
            }
        """.trimIndent()
    }

    @ShopPageHomeScope
    @Provides
    @Named(GQL_GET_SHOP_NPL_CAMPAIGN_TNC)
    fun getShopNplCampaignTncQuery(@ShopPageContext context: Context): String {
        return """
            query get_merchant_campaign_tnc(${'$'}param: GetMerchantCampaignTNCRequest!){
              getMerchantCampaignTNC (params:${'$'}param){
                            title,
    						messages,
    						error {
    						  error_code
    						  error_message
    						}
              }
            }
        """.trimIndent()
    }

    @ShopPageHomeScope
    @Provides
    @Named(GQL_GET_CAMPAIGN_NOTIFY_ME)
    fun getCampaignNotifyMeQuery(@ShopPageContext context: Context): String {
        return """
            query get_campaign_notify_me(${'$'}params:GetCampaignNotifyMeRequest!){
              getCampaignNotifyMe (params: ${'$'}params ){
                campaign_id
                success
                message
                error_message
                is_available
              }
            }
        """.trimIndent()
    }

    @ShopPageHomeScope
    @Provides
    @Named(GQLQueryConstant.SHOP_PRODUCT)
    fun getShopProductQuery(@ShopPageContext context: Context): String {
        return """
            query getShopProduct(${'$'}shopId: String!,${'$'}filter: ProductListFilter!){
              GetShopProduct(shopID:${'$'}shopId, filter:${'$'}filter){
                status
                errors
                data {
                  product_id
                  name
                  product_url
                  stock
                  status
                  price{
                    text_idr
                  }
                  flags{
                    isFeatured
                    isPreorder
                    isFreereturn
                    isVariant
                    isWholesale
                    isWishlist
                    isSold
                    supportFreereturn
                    mustInsurance
                    withStock
                  }
                  stats{
                    reviewCount
                    rating
                  }
                  campaign{
                    original_price
                    original_price_fmt
                    discounted_price_fmt
                    discounted_percentage
                    discounted_price
                  }
                  primary_image{
                    original
                    thumbnail
                    resize300
                  }
                  cashback{
                    cashback
                    cashback_amount
                  }
                  freeOngkir {
                    isActive
                    imgURL
                  }
                  label_groups {
                    position
                    type
                    title
                  }
                }
                totalData
              }
            }
        """.trimIndent()
    }

    @ShopPageHomeScope
    @Provides
    @Named(GQL_ATC_MUTATION)
    fun provideAddToCartMutation(@ShopPageContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.atc_common.R.raw.mutation_add_to_cart);
    }

    @ShopPageHomeScope
    @Provides
    @Named(GQL_CHECK_WISHLIST)
    fun provideCheckWishlistQuery(@ShopPageContext context: Context): String {
        return """
            query CheckWishList(${'$'}productID:String!){
              checkWishlist(productID:${'$'}productID){
                product_id
                is_wishlist
              }
            }
        """.trimIndent()
    }

    @ShopPageHomeScope
    @Provides
    fun provideErrorInterceptors(): CommonErrorResponseInterceptor {
        return CommonErrorResponseInterceptor()
    }

    @ShopPageHomeScope
    @Provides
    fun provideInterceptors(loggingInterceptor: HttpLoggingInterceptor,
                            commonErrorResponseInterceptor: CommonErrorResponseInterceptor): MutableList<Interceptor> {
        return mutableListOf(loggingInterceptor, commonErrorResponseInterceptor)
    }

    @ShopPageHomeScope
    @Provides
    fun provideRestRepository(interceptors: MutableList<Interceptor>,
                              @ShopPageContext context: Context): RestRepository {
        return RestRequestInteractor.getInstance().restRepository.apply {
            updateInterceptors(interceptors, context)
        }
    }

    @ShopPageHomeScope
    @Provides
    fun getCoroutineDispatcherProvider(): CoroutineDispatcherProvider {
        return CoroutineDispatcherProviderImpl
    }

    @ShopPageHomeScope
    @Provides
    fun getShopProductUseCase(
            @Named(GQLQueryConstant.SHOP_PRODUCT) gqlQuery: String,
            gqlUseCase: MultiRequestGraphqlUseCase
    ): GqlGetShopProductUseCase {
        return GqlGetShopProductUseCase(gqlQuery, gqlUseCase)
    }

    @ShopPageHomeScope
    @Provides
    fun provideAddToWishListUseCase(@ShopPageContext context: Context?): AddWishListUseCase {
        return AddWishListUseCase(context)
    }

    @ShopPageHomeScope
    @Provides
    fun provideRemoveFromWishListUseCase(@ShopPageContext context: Context?): RemoveWishListUseCase {
        return RemoveWishListUseCase(context)
    }

    @ShopPageHomeScope
    @Provides
    fun provideGetPlayWidgetUseCase(graphqlRepository: GraphqlRepository): GetPlayWidgetUseCase{
        return GetPlayWidgetUseCase(graphqlRepository)
    }

    @ShopPageHomeScope
    @Provides
    fun providePlayToggleChannelReminderUseCase(graphqlRepository: GraphqlRepository): PlayToggleChannelReminderUseCase{
        val graphQlUseCase = GraphqlUseCase<PlayToggleChannelEntity>(graphqlRepository)
        return PlayToggleChannelReminderUseCase(graphQlUseCase)
    }

    @ShopPageHomeScope
    @Provides
    fun provideGetYoutubeVideoUseCase(restRepository: RestRepository): GetYoutubeVideoDetailUseCase {
        return GetYoutubeVideoDetailUseCase(restRepository)
    }

    @ShopPageHomeScope
    @Provides
    fun provideUserSessionInterface(@ShopPageContext context: Context?): UserSessionInterface {
        return UserSession(context)
    }

    @ShopPageHomeScope
    @Provides
    fun provideShopPageHomeTracking(@ShopPageContext context: Context): ShopPageHomeTracking {
        return ShopPageHomeTracking(TrackingQueue(context))
    }

}