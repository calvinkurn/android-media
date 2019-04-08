package com.tokopedia.hotel.destination.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.hotel.common.di.scope.HotelScope
import dagger.Module
import dagger.Provides

/**
 * @author by jessica on 26/03/19
 */

@Module
@HotelDestinationScope
class HotelDestinationModule {

    @HotelDestinationScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

}