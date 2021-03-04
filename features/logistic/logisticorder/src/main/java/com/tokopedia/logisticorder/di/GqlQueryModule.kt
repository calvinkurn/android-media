package com.tokopedia.logisticorder.di

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.logisticorder.R
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey


@Module
class GqlQueryModule {

    @Provides
    @IntoMap
    @StringKey(RawQueryConstant.GET_RETRY_AVAILABILITY)
    fun provideRetryAvailQuery(@TrackingPageContext context: Context) : String =
            GraphqlHelper.loadRawString(context.resources, R.raw.retry_availability)

    @Provides
    @IntoMap
    @StringKey(RawQueryConstant.RETRY_PICKUP)
    fun provideRetryBooking(@TrackingPageContext context: Context) : String =
            GraphqlHelper.loadRawString(context.resources, R.raw.retry_booking)

}