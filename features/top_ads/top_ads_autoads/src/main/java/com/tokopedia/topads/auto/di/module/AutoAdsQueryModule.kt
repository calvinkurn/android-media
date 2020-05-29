package com.tokopedia.topads.auto.di.module

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.topads.auto.R
import com.tokopedia.topads.auto.di.ActivityContext
import com.tokopedia.topads.auto.di.AutoAdsScope
import com.tokopedia.topads.auto.internal.RawQueryKeyObject

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

/**
 * Author errysuprayogi on 20,May,2019
 */
@Module
@AutoAdsScope
class AutoAdsQueryModule(val context: Context) {

    @AutoAdsScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyObject.QUERY_ADS_SHOP_INFO)
    fun queryShopInfo(@ActivityContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_autoads_shop_info)

    @AutoAdsScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyObject.QUERY_ADS_BID_INFO)
    fun queryBidInfo(@ActivityContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_autoads_bid_info)

    @AutoAdsScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyObject.QUERY_GET_AUTO_ADS)
    fun queryGetAutoAds(@ActivityContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_get_autoads)

    @AutoAdsScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyObject.QUERY_POST_AUTO_ADS)
    fun queryPostAutoAds(@ActivityContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_post_autoads)

    @AutoAdsScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyObject.QUERY_POTENTIAL_REACH_ESTIMATION)
    fun queryPotentialReach(@ActivityContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_potential_reach_estimation)
  
    @Provides
    @ActivityContext
    fun providesContext() = context

}
