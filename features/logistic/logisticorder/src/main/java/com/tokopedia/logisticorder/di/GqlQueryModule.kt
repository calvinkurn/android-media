package com.tokopedia.logisticorder.di

import android.app.Activity
import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.logisticorder.R
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey


@Module
class GqlQueryModule(val activity: Activity) {

    @Provides
    fun getContext(): Context = activity

    @Provides
    @IntoMap
    @StringKey(RawQueryConstant.GET_RETRY_AVAILABILITY)
    fun provideRetryAvailQuery(context: Context) : String =
            GraphqlHelper.loadRawString(context.resources, R.raw.retry_availability)

    @Provides
    @IntoMap
    @StringKey(RawQueryConstant.RETRY_PICKUP)
    fun provideRetryBooking(context: Context) : String =
            GraphqlHelper.loadRawString(context.resources, R.raw.retry_booking)

}