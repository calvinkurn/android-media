package com.tokopedia.hotel.roomlist.data.model

/**
 * @author by jessica on 22/04/19
 */

data class HotelRoomListPageModel(
        var propertyId: Int = 0,
        var checkIn: String = "",
        var checkOut: String = "",
        var adult: Int = 0,
        var child: Int = 0,
        var room: Int = 0
)