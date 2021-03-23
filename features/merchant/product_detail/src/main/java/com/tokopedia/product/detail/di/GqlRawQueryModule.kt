package com.tokopedia.product.detail.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.atc_common.AtcConstant
import com.tokopedia.product.detail.R
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.usecase.SubmitHelpTicketUseCase
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import javax.inject.Named

@Module
class GqlRawQueryModule {

    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_WISHLIST_STATUS)
    fun provideRawWishlistQuery(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, com.tokopedia.wishlist.common.R.raw.gql_get_is_wishlisted)


    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_GET_RATE_ESTIMATION)
    fun provideRawGetRateEstimation(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_rates_estimation_v3)

    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_GET_TOP_ADS_MANAGE_PRODUCT)
    fun provideRawGetTopAds(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_top_ads_product_manage)

    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_DISCUSSION_MOST_HELPFUL)
    fun provideRawDiscussionMostHelpful(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_talk_discussion_most_helpful)

    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_PRODUCT_AFFILIATE)
    fun provideGetProductAffiliate(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, com.tokopedia.affiliatecommon.R.raw.query_product_affiliate_data)

    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.MUTATION_FAVORITE_SHOP)
    fun providePostFavorite(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, com.tokopedia.shop.common.R.raw.gql_mutation_favorite_shop)

    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_RECOMMEN_PRODUCT)
    fun provideRecommendationProduct(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, com.tokopedia.recommendation_widget_common.R.raw.query_recommendation_widget)

    @ProductDetailScope
    @Provides
    @Named(SubmitHelpTicketUseCase.QUERY_NAME)
    fun provideSubmitHelpTicket(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, com.tokopedia.purchase_platform.common.R.raw.submit_help_ticket)

    @ProductDetailScope
    @Provides
    @Named(AtcConstant.MUTATION_UPDATE_CART_COUNTER)
    fun provideUpdateCartCounterMutation(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.atc_common.R.raw.gql_update_cart_counter)
    }

    @ProductDetailScope
    @Provides
    @Named("atcMutation")
    fun provideAddToCartMutation(@ApplicationContext context: Context):
            String = GraphqlHelper.loadRawString(context.resources, com.tokopedia.atc_common.R.raw.mutation_add_to_cart)

    @ProductDetailScope
    @Provides
    @Named("atcOcsMutation")
    fun provideAddToCartOcsMutation(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.atc_common.R.raw.mutation_add_to_cart_one_click_shipment)
    }

    @ProductDetailScope
    @Provides
    @Named(AtcConstant.MUTATION_ATC_OCC)
    fun provideAtcOccMutation(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.atc_common.R.raw.mutation_add_to_cart_one_click_checkout)
    }

    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.MUTATION_NOTIFY_ME)
    fun provideNotifyMeStatus(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, com.tokopedia.shop.common.R.raw.gql_check_campaign_notify_me)
}