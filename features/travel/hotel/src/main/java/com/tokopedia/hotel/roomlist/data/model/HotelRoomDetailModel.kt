package com.tokopedia.hotel.roomlist.data.model

/**
 * @author by jessica on 08/05/19
 */

data class HotelRoomDetailModel(
        val hotelRoom: HotelRoom = HotelRoom(),
        val addToCartParam: HotelAddCartParam = HotelAddCartParam()
)