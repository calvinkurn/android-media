package com.tokopedia.hotel.orderdetail.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.hotel.R
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * @author by jessica on 09/05/19
 */

@Module
class HotelOrderDetailModule {

    @Provides
    @HotelOrderDetailScope
    @Named("dummy_order_detail")
    fun provideDummySearchResult(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.dummy_hotel_order_detail)
}