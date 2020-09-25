package com.tokopedia.promotionstarget.data.di.modules

import android.content.Context
import com.tokopedia.promotionstarget.R
import com.tokopedia.promotionstarget.data.di.*
import com.tokopedia.promotionstarget.data.gql.GraphqlHelper
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@Module
class GratiffPresenterModule {

    @Provides
    @Named(GRATIFF_NOTIFICATION)
    fun provideGratiffNotification(context: Context): String = GraphqlHelper.loadRawString(context.resources, R.raw.t_promo_gratif_notification)

    @Provides
    @Named(HACHIKO_COUPON_DETAIL)
    fun provideHachikoCouponDetail(context: Context): String = GraphqlHelper.loadRawString(context.resources, R.raw.t_promo_hachiko_coupon)

}