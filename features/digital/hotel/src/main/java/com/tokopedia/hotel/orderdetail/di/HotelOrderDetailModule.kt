package com.tokopedia.hotel.orderdetail.di

import com.tokopedia.common.travel.utils.TrackingCrossSellUtil
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import dagger.Module
import dagger.Provides

/**
 * @author by jessica on 09/05/19
 */

@Module
class HotelOrderDetailModule {

    @HotelOrderDetailScope
    @Provides
    fun provideMultiRequestGraphqlUseCase(graphqlRepository: com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository): MultiRequestGraphqlUseCase =
            MultiRequestGraphqlUseCase(graphqlRepository)

    @HotelOrderDetailScope
    @Provides
    fun provideTrackingCrossSellUtil(): TrackingCrossSellUtil = TrackingCrossSellUtil()
}