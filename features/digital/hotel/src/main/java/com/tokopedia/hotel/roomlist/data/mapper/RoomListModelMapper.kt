package com.tokopedia.hotel.roomlist.data.mapper

import com.tokopedia.hotel.roomlist.data.model.HotelRoom
import com.tokopedia.hotel.roomlist.data.model.HotelRoomData
import com.tokopedia.hotel.roomlist.data.model.RoomListModel

/**
 * @author by jessica on 15/04/19
 */

class RoomListModelMapper{

    fun transform(hotelRoom: HotelRoom): RoomListModel {
        var roomListModel = RoomListModel()
        if (hotelRoom != null) {
            roomListModel.roomName = hotelRoom.roomInfo.name
            roomListModel.maxOccupancy = hotelRoom.occupancyInfo.maxOccupancy
            roomListModel.maxFreeChild = hotelRoom.occupancyInfo.maxFreeChild
            roomListModel.occupancyText = hotelRoom.occupancyInfo.occupancyText
            roomListModel.bedInfo = hotelRoom.bedInfo
            roomListModel.roomFacility = hotelRoom.roomInfo.facility
//            roomListModel.payInHotel = hotelRoom.roomInfo
            roomListModel.breakfastIncluded = hotelRoom.breakfastInfo.isBreakfastIncluded
            roomListModel.isRefundable = hotelRoom.refundInfo.isRefundable
            roomListModel.isCcRequired = hotelRoom.creditCardInfo.isCCRequired
            roomListModel.price = hotelRoom.roomPrice[0].roomPrice
            roomListModel.actualPrice = hotelRoom.roomPrice[0].totalPrice
            val images: MutableList<String> = arrayListOf()
            for (item in hotelRoom.roomInfo.roomImages) {
                images.add(item.urlOriginal)
            }
            roomListModel.images = images
        }
        return roomListModel
    }

    fun transform(hotelRoomData: HotelRoomData): MutableList<RoomListModel> {
        val list: MutableList<RoomListModel> = arrayListOf()
        for (room in hotelRoomData.rooms) {
            list.add(transform(room))
        }
        return list
    }
}