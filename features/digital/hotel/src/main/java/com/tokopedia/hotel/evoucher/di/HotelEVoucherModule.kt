package com.tokopedia.hotel.evoucher.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.hotel.R
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * @author by furqan on 14/05/19
 */

@Module
@HotelEVoucherScope
class HotelEVoucherModule {

    @Provides
    @HotelEVoucherScope
    @Named("dummy_order_detail")
    fun provideDummySearchResult(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.dummy_hotel_order_detail)


    @HotelEVoucherScope
    @Provides
    fun provideMultiRequestGraphqlUseCase(graphqlRepository: com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository): MultiRequestGraphqlUseCase =
            MultiRequestGraphqlUseCase(graphqlRepository)

}