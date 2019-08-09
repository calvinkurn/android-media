package com.tokopedia.hotel.search.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
@HotelSearchPropertyScope
class HotelSearchPropertyModule{

    @Provides
    @HotelSearchPropertyScope
    @Named("search_query")
    fun provideSearchQuery(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, com.tokopedia.hotel.R.raw.gql_get_property_search)
}