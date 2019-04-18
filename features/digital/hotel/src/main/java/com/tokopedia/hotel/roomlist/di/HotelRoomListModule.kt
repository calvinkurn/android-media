package com.tokopedia.hotel.roomlist.di

import com.tokopedia.hotel.roomlist.data.model.mapper.RoomListModelMapper
import dagger.Module
import dagger.Provides

/**
 * @author by furqan on 28/03/19
 */
@Module
class HotelRoomListModule {
    @HotelRoomListScope
    @Provides
    fun providesRoomListModelMapper(): RoomListModelMapper = RoomListModelMapper()
}