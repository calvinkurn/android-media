package com.tokopedia.hotel.evoucher.di

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import dagger.Module
import dagger.Provides

/**
 * @author by furqan on 14/05/19
 */

@Module
class HotelEVoucherModule {

    @HotelEVoucherScope
    @Provides
    fun provideMultiRequestGraphqlUseCase(graphqlRepository: com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository): MultiRequestGraphqlUseCase =
            MultiRequestGraphqlUseCase(graphqlRepository)

}