package com.tokopedia.product.detail.di

import android.content.ComponentCallbacks
import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.product.detail.R
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@ProductDetailScope
@Module
class GqlRawQueryModule {

    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_PRODUCT_INFO)
    fun provideRawProductInfo(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_product_info)

    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_VARIANT)
    fun provideRawVariant(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_product_variant)

    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_WISHLIST_STATUS)
    fun provideRawWishlistQuery(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_is_wishlisted)

    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_PRODUCT_RATING)
    fun provideRawProductRatingQuery(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_product_rating)

    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_WISHLIST_COUNT)
    fun provideRawProductWishlistCountQuery(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_product_wishlist_count)

    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_GET_VOUCHER)
    fun provideRawGetVoucher(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_query_merchant_voucher)

    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_GET_RATE_ESTIMATION)
    fun provideRawGetRateEstimation(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_rates_estimation_v3)

    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_GET_IMAGE_REVIEW)
    fun provideRawGetImageReview(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_image_review)

    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_GET_MOST_HELPFUL_REVIEW)
    fun provideRawGetMostHelpfulReview(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_product_most_helpful_review)

    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_GET_LATEST_TALK)
    fun provideRawGetLatestTalk(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_product_latest_talk)

    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_DISPLAY_ADS)
    fun provideRawDisplayAds(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.get_topads_query)

    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_OTHER_PRODUCT)
    fun provideRawOtherProduct(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_other_product)

    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_SHOP)
    fun provideGetShop(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_shop_info)

    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_SHOP_BADGE)
    fun provideGetShopBadge(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_shop_badge)

    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_PRODUCT_AFFILIATE)
    fun provideGetProductAffiliate(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_product_affiliate_data)

    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_SHOP_COMMITMENT)
    fun provideGetShopCommitment(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_shop_commitment)

    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_INSTALLMENT)
    fun provideGetInstallment(@ApplicationContext context: Context): String =
        GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_installment)

    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_CHECKOUTTYPE)
    fun provideCheckoutType(@ApplicationContext context: Context): String =
        GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_checkout_type)

    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.MUTATION_FAVORITE_SHOP)
    fun providePostFavorite(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_mutation_favorite_shop)

    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_SHOP_COD_STATUS)
    fun provideShopCodStatus(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_shop_cod)

    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_USER_COD_STATUS)
    fun provideUserCodStatus(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_user_cod)

    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_PRODUCT_PP)
    fun provideProductPurchaseProtection(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_product_pp)
}