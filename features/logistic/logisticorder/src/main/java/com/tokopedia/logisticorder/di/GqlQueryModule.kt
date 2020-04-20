package com.tokopedia.logisticorder.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import com.tokopedia.logisticorder.R


@Module
class GqlQueryModule {

    @Provides
    @IntoMap
    @StringKey(RawQueryConstant.GET_RETRY_AVAILABILITY)
    fun provideRetryAvailQuery(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.retry_availability)

    @Provides
    @IntoMap
    @StringKey(RawQueryConstant.RETRY_PICKUP)
    fun provideRetryBooking(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.retry_booking)

}