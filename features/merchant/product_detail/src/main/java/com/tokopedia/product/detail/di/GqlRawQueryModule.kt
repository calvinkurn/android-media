package com.tokopedia.product.detail.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.atc_common.AtcConstant
import com.tokopedia.product.detail.R
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
    @StringKey(RawQueryKeyConstant.QUERY_DISCUSSION_MOST_HELPFUL)
    fun provideRawDiscussionMostHelpful(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_talk_discussion_most_helpful)

    @ProductDetailScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_RECOMMEN_PRODUCT)
    fun provideRecommendationProduct(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, com.tokopedia.recommendation_widget_common.R.raw.query_recommendation_widget)

    @ProductDetailScope
    @Provides
    @Named(AtcConstant.MUTATION_UPDATE_CART_COUNTER)
    fun provideUpdateCartCounterMutation(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.atc_common.R.raw.gql_update_cart_counter)
    }

    @ProductDetailScope
    @Provides
    @Named("atcOcsMutation")
    fun provideAddToCartOcsMutation(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.atc_common.R.raw.mutation_add_to_cart_one_click_shipment)
    }
}