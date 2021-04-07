package com.tokopedia.hotel.roomlist.presentation.adapter.viewholder

import android.graphics.Paint
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.presentation.widget.FacilityTextView
import com.tokopedia.hotel.roomlist.data.model.HotelRoom
import com.tokopedia.hotel.roomlist.data.model.HotelRoomInfo
import com.tokopedia.hotel.roomlist.data.model.RoomListModel
import com.tokopedia.hotel.roomlist.widget.ImageViewPager
import com.tokopedia.imagepreviewslider.presentation.util.ImagePreviewSlider
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.item_hotel_room_full.view.*
import kotlinx.android.synthetic.main.item_hotel_room_list.view.*
import kotlinx.android.synthetic.main.layout_hotel_image_slider.view.*
import kotlin.math.min

/**
 * @author by jessica on 25/03/19
 */

class RoomListViewHolder(val view: View, val listener: OnClickBookListener) : AbstractViewHolder<HotelRoom>(view) {

    override fun bind(hotelRoom: HotelRoom) {
        with(itemView) {
            val roomListModel = mapToRoomListModel(hotelRoom)

            if (roomListModel.available) {

                room_description_layout.visibility = View.VISIBLE
                room_full_layout.visibility = View.GONE

                setImageViewPager(roomListModel.images, hotelRoom)
                room_name_text_view.text = roomListModel.roomName
                max_occupancy_text_view.text = roomListModel.occupancyText
                bed_info_text_view.text = context.getString(R.string.hotel_room_list_room_description,
                        roomListModel.roomSize,
                        roomListModel.bedInfo)
                room_price_text_view.text = roomListModel.price
                pay_hotel_layout.visibility = if (roomListModel.payInHotel) View.VISIBLE else View.GONE
                pay_hotel_text_view.text = roomListModel.payInHotelString
                room_left_text_view.visibility = if (roomListModel.roomLeft <= 2) View.VISIBLE else View.GONE
                room_left_text_view.text = getString(R.string.hotel_room_room_left_text, roomListModel.roomLeft.toString())
                cc_not_required_text_view.text = roomListModel.creditCardHeader
                initRoomFacility(roomListModel.breakfastInfo, roomListModel.refundInfo, roomListModel.roomFacility)

                choose_room_button.setOnClickListener { listener.onClickBookListener(hotelRoom) }
                choose_room_button.text = getString(R.string.hotel_room_list_choose_room_button, "")

                if (roomListModel.slashPrice.isNotEmpty()) {
                    room_list_slash_price_tv.show()
                    room_list_slash_price_tv.paintFlags = room_list_slash_price_tv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    room_list_slash_price_tv.text = roomListModel.slashPrice
                } else room_list_slash_price_tv.hide()

                if (roomListModel.tagging.isNotEmpty()) {
                    room_list_tagging_tv.show()
                    room_list_tagging_tv.text = roomListModel.tagging
                } else room_list_tagging_tv.hide()

            } else {
                room_description_layout.visibility = View.GONE
                room_full_layout.visibility = View.VISIBLE
                if (roomListModel.images.isNotEmpty()) room_list_room_full_image_view.loadImage(roomListModel.images.first())
                room_full_room_name_text_view.text = roomListModel.roomName
            }
        }
    }

    private fun initRoomFacility(breakfastInfo: HotelRoom.RoomBreakfastInfo, refundInfo: HotelRoom.RefundInfo, roomFacility: List<HotelRoomInfo.Facility>) {
        with(itemView) {
            room_facility_recycler_view.removeAllViews()

            if (breakfastInfo.breakFast.isNotEmpty()) {
                val breakfastTextView = FacilityTextView(context)
                breakfastTextView.setIconAndText(breakfastInfo.iconUrl, breakfastInfo.breakFast)
                room_facility_recycler_view.addView(breakfastTextView)
            }

            if (refundInfo.refundStatus.isNotEmpty()) {
                val refundableTextView = FacilityTextView(context)
                refundableTextView.setIconAndText(refundInfo.iconUrl, refundInfo.refundStatus)
                room_facility_recycler_view.addView(refundableTextView)
            }

            for (i in 0 until min(roomFacility.size, 2)) {
                var textView = FacilityTextView(context)
                textView.setIconAndText(roomFacility[i].iconUrl, roomFacility[i].name)
                room_facility_recycler_view.addView(textView)
            }
        }
    }

    private fun setImageViewPager(imageUrls: List<String>, room: HotelRoom) {
        with(itemView) {
            if (imageUrls.size >= 5) room_image_view_pager.setImages(imageUrls.subList(0, 5))
            else room_image_view_pager.setImages(imageUrls)
            room_image_view_pager.imageViewPagerListener = object : ImageViewPager.ImageViewPagerListener {
                override fun onImageClicked(position: Int) {
                    listener.onPhotoClickListener(room)
                    ImagePreviewSlider.instance.start(context, room.roomInfo.name, imageUrls, imageUrls, position, itemView.image_banner)
                }
            }
            room_image_view_pager.buildView()
        }
    }

    companion object {
        val LAYOUT = R.layout.item_hotel_room_list
    }

    private fun mapToRoomListModel(hotelRoom: HotelRoom): RoomListModel {
        var roomListModel = RoomListModel()
        roomListModel.roomName = hotelRoom.roomInfo.name
        roomListModel.roomSize = hotelRoom.roomInfo.size.toString()
        roomListModel.maxOccupancy = hotelRoom.occupancyInfo.maxOccupancy
        roomListModel.maxFreeChild = hotelRoom.occupancyInfo.maxFreeChild
        roomListModel.occupancyText = hotelRoom.occupancyInfo.occupancyText
        roomListModel.bedInfo = hotelRoom.bedInfo
        roomListModel.roomFacility = hotelRoom.roomInfo.facility
        roomListModel.payInHotel = !hotelRoom.isDirectPayment
        roomListModel.breakfastInfo = hotelRoom.breakfastInfo
        roomListModel.refundInfo = hotelRoom.refundInfo
        roomListModel.refundStatus = hotelRoom.refundInfo.refundStatus
        roomListModel.creditCardHeader = hotelRoom.creditCardInfo.header
        roomListModel.creditCardInfo = hotelRoom.creditCardInfo.creditCardInfo
        roomListModel.price = hotelRoom.roomPrice.roomPrice
        roomListModel.roomLeft = hotelRoom.numberRoomLeft
        roomListModel.available = hotelRoom.available
        roomListModel.slashPrice = hotelRoom.roomPrice.deals.price
        roomListModel.tagging = hotelRoom.roomPrice.deals.tagging
        roomListModel.payInHotelString = hotelRoom.isDirectPaymentString

        val images: MutableList<String> = arrayListOf()
        for (item in hotelRoom.roomInfo.roomImages) {
            images.add(item.urlOriginal)
        }
        roomListModel.images = images
        return roomListModel
    }

    interface OnClickBookListener {
        fun onClickBookListener(room: HotelRoom)
        fun onPhotoClickListener(room: HotelRoom)
    }

}