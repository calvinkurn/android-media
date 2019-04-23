package com.tokopedia.hotel.roomlist.presentation.adapter.viewholder

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.hotel.R
import com.tokopedia.hotel.roomlist.data.model.HotelRoom
import com.tokopedia.hotel.roomlist.data.model.RoomListModel
import com.tokopedia.hotel.roomlist.presentation.adapter.RoomFacilityAdapter
import com.tokopedia.hotel.roomlist.widget.ImageViewPager
import kotlinx.android.synthetic.main.item_hotel_room_list.view.*

/**
 * @author by jessica on 25/03/19
 */

class RoomListViewHolder(val view: View): AbstractViewHolder<HotelRoom>(view) {

   override fun bind(hotelRoom: HotelRoom) {
        with(itemView) {
            val roomListModel = mapToRoomListModel(hotelRoom)

            room_image_view_pager.imageUrls = ArrayList(roomListModel.images)
            room_image_view_pager.buildView()
            room_image_view_pager.setImages(roomListModel.images)
            room_name_text_view.text = roomListModel.roomName
            max_occupancy_text_view.text = roomListModel.occupancyText
            bed_info_text_view.text = roomListModel.bedInfo
            room_price_text_view.text = roomListModel.price
            pay_hotel_text_view.visibility = if (roomListModel.payInHotel) View.VISIBLE else View.GONE
            cc_not_required_text_view.visibility = if (roomListModel.isCcRequired) View.GONE else View.VISIBLE

            var adapter = RoomFacilityAdapter()
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            room_facility_recycler_view.layoutManager = layoutManager
            room_facility_recycler_view.adapter = adapter
            if (roomListModel.breakfastIncluded) adapter.addFacility("Termasuk Sarapan") else adapter.addFacility("Tidak Termasuk Sarapan")
            if (roomListModel.isRefundable) adapter.addFacility("Dapat direfund") else adapter.addFacility("Tidak dapat direfund")
            adapter.addFacility(roomListModel.roomFacility[0].name)
            adapter.addFacility(roomListModel.roomFacility[1].name)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_hotel_room_list
    }

    fun mapToRoomListModel(hotelRoom: HotelRoom): RoomListModel {
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

}