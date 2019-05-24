package com.tokopedia.hotel.roomlist.data.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.hotel.roomlist.presentation.adapter.RoomListTypeFactory

/**
 * @author by jessica on 15/04/19
 */

data class RoomListModel(
        var roomName: String = "",
        var roomSize: String = "",
        var maxOccupancy: Int = 0,
        var maxFreeChild: Int = 0,
        var occupancyText: String = "",
        var bedInfo: String = "",
        var breakfastIncluded: Boolean = false,
        var isRefundable: Boolean = false,
        var roomFacility: List<HotelRoomInfo.Facility> = listOf(),
        var payInHotel: Boolean = false,
        var isCcRequired: Boolean = false,
        var creditCardInfo: String = "",
        var price: String = "",
        var images: List<String> = listOf(),
        var roomLeft: Int = 0,
        var available: Boolean = true
)