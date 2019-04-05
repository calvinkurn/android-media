package com.tokopedia.hotel.search.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.hotel.R
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
@HotelSearchPropertyScope
class HotelSearchPropertyModule{

    @Provides
    @HotelSearchPropertyScope
    @Named("dummy_search_result")
    fun provideDummySearchResult(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.dummy_search_result)
}