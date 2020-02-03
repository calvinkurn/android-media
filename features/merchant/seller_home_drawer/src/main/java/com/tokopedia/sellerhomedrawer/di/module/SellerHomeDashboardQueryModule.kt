package com.tokopedia.sellerhomedrawer.di.module

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.sellerhomedrawer.R
import com.tokopedia.sellerhomedrawer.data.constant.SellerHomeParamConstant
import com.tokopedia.sellerhomedrawer.di.SellerHomeDashboardScope
import dagger.Module
import dagger.Provides
import javax.inject.Named

@SellerHomeDashboardScope
@Module
class SellerHomeDashboardQueryModule {

    @Provides
    @Named(SellerHomeParamConstant.RAW_GM_STATUS)
    fun provideGetShopStatusQuery(context: Context) : String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gold_merchant_status)
    }

    @Provides
    @Named(SellerHomeParamConstant.SELLER_DRAWER_DATA)
    fun provideGetSellerDrawerDataQuery(context: Context) : String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.sah_seller_drawer_data_query)
    }

    @Provides
    @Named(SellerHomeParamConstant.GET_CHAT_NOTIFICATION_QUERY)
    fun provideGetChatNotificationQuery(@Named("application") context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.sah_query_get_chat_notification)
    }

    @Provides
    @Named(SellerHomeParamConstant.GET_INFO_PENJUAL_NOTIFICATION_QUERY)
    fun provideGetInfoPenjualNotificationQuery(@Named("application") context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.sah_query_notification_center_total_unread)
    }
}