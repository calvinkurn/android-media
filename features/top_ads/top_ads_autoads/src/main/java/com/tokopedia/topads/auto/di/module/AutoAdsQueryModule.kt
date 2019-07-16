package com.tokopedia.topads.auto.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.topads.auto.R
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
class AutoAdsQueryModule {

    @AutoAdsScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyObject.QUERY_ADS_SHOP_INFO)
    fun queryShopInfo(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_autoads_shop_info)

    @AutoAdsScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyObject.QUERY_ADS_BID_INFO)
    fun queryBidInfo(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_autoads_bid_info)

    @AutoAdsScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyObject.QUERY_GET_AUTO_ADS)
    fun queryGetAutoAds(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_get_autoads)

    @AutoAdsScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyObject.QUERY_POST_AUTO_ADS)
    fun queryPostAutoAds(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_post_autoads)

}
