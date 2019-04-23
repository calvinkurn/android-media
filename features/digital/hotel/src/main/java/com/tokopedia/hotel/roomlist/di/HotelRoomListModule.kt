package com.tokopedia.hotel.roomlist.di

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.hotel.roomlist.usecase.GetHotelRoomListUseCase
import dagger.Module
import dagger.Provides

/**
 * @author by furqan on 28/03/19
 */
@Module
class HotelRoomListModule {

    @HotelRoomListScope
    @Provides
    fun provideMultiRequestGraphqlUseCase(graphqlRepository: GraphqlRepository): MultiRequestGraphqlUseCase =
            MultiRequestGraphqlUseCase(graphqlRepository)

    @HotelRoomListScope
    @Provides
    fun provideGetRoomListUseCase(useCase: MultiRequestGraphqlUseCase): GetHotelRoomListUseCase =
            GetHotelRoomListUseCase(useCase)
}