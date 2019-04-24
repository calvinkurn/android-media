package com.tokopedia.hotel.roomlist.presentation.adapter.viewholder

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.hotel.R
import com.tokopedia.hotel.roomlist.data.model.HotelRoom
import com.tokopedia.hotel.roomlist.data.model.HotelRoomInfo
import com.tokopedia.hotel.roomlist.data.model.RoomListModel
import com.tokopedia.hotel.roomlist.presentation.adapter.RoomFacilityAdapter
import kotlinx.android.synthetic.main.item_hotel_room_full.view.*
import kotlinx.android.synthetic.main.item_hotel_room_list.view.*
import kotlin.math.min

/**
 * @author by jessica on 25/03/19
 */

class RoomListViewHolder(val view: View): AbstractViewHolder<HotelRoom>(view) {

   override fun bind(hotelRoom: HotelRoom) {
        with(itemView) {
            val roomListModel = mapToRoomListModel(hotelRoom)

            if (roomListModel.roomLeft > 0) {

                room_description_layout.visibility = View.VISIBLE
                room_full_layout.visibility = View.GONE

                setImageViewPager(roomListModel.images)
                room_name_text_view.text = roomListModel.roomName
                max_occupancy_text_view.text = roomListModel.occupancyText
                bed_info_text_view.text = roomListModel.bedInfo
                room_price_text_view.text = roomListModel.price
                pay_hotel_text_view.visibility = if (roomListModel.payInHotel) View.VISIBLE else View.GONE
                room_left_text_view.visibility = if (roomListModel.roomLeft <= 2) View.VISIBLE else View.GONE
                room_left_text_view.text = getString(R.string.hotel_room_room_left_text, roomListModel.roomLeft.toString())
                cc_not_required_text_view.visibility = if (roomListModel.isCcRequired) View.GONE else View.VISIBLE
                initRoomFacility(roomListModel.breakfastIncluded, roomListModel.isRefundable, roomListModel.roomFacility)

            } else {
                room_description_layout.visibility = View.GONE
                room_full_layout.visibility = View.VISIBLE
                setImageViewPager(listOf(roomListModel.images[0]))
                room_full_room_name_text_view.text = roomListModel.roomName
            }
        }
    }

    fun initRoomFacility(breakfastIncluded: Boolean, refundable: Boolean, roomFacility: List<HotelRoomInfo.Facility>) {
        with(itemView) {
            var adapter = RoomFacilityAdapter()
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            room_facility_recycler_view.layoutManager = layoutManager
            room_facility_recycler_view.adapter = adapter

            if (breakfastIncluded) adapter.addFacility(getString(R.string.hotel_room_list_filter_free_breakfast))
            else adapter.addFacility(getString(R.string.hotel_room_list_breakfast_not_included))

            if (refundable) adapter.addFacility(getString(R.string.hotel_room_list_refundable_with_condition))
            else adapter.addFacility(getString(R.string.hotel_room_list_not_refundable))

            for(i in 0..min(roomFacility.size, 1)) {
                adapter.addFacility(roomFacility[i].name)
            }
        }
    }

    fun setImageViewPager(imageUrls: List<String>) {
        with(itemView) {
            room_image_view_pager.setImages(imageUrls)
            room_image_view_pager.buildView()
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
            roomListModel.roomLeft = hotelRoom.numberRoomLeft

            val images: MutableList<String> = arrayListOf()
            for (item in hotelRoom.roomInfo.roomImages) {
                images.add(item.urlOriginal)
            }
            roomListModel.images = images
        }
        return roomListModel
    }

}