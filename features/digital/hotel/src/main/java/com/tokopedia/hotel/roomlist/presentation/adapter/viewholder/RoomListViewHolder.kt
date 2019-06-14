package com.tokopedia.hotel.roomlist.presentation.adapter.viewholder

import android.text.Html
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.hotel.R
import com.tokopedia.hotel.roomlist.data.model.HotelRoom
import com.tokopedia.hotel.roomlist.data.model.HotelRoomInfo
import com.tokopedia.hotel.roomlist.data.model.RoomListModel
import com.tokopedia.hotel.common.presentation.widget.FacilityTextView
import com.tokopedia.hotel.roomlist.widget.ImageViewPager
import com.tokopedia.imagepreviewslider.presentation.activity.ImagePreviewSliderActivity
import kotlinx.android.synthetic.main.item_hotel_room_full.view.*
import kotlinx.android.synthetic.main.item_hotel_room_list.view.*
import kotlin.math.min

/**
 * @author by jessica on 25/03/19
 */

class RoomListViewHolder(val view: View, val listener: OnClickBookListener): AbstractViewHolder<HotelRoom>(view) {

   override fun bind(hotelRoom: HotelRoom) {
        with(itemView) {
            val roomListModel = mapToRoomListModel(hotelRoom)

            if (roomListModel.available) {

                room_description_layout.visibility = View.VISIBLE
                room_full_layout.visibility = View.GONE

                setImageViewPager(roomListModel.images)
                room_name_text_view.text = roomListModel.roomName
                max_occupancy_text_view.text = roomListModel.occupancyText
                bed_info_text_view.text = context.getString(R.string.hotel_room_list_room_description,
                        roomListModel.roomSize,
                        roomListModel.bedInfo)
                room_price_text_view.text = roomListModel.price
                pay_hotel_layout.visibility = if (roomListModel.payInHotel) View.VISIBLE else View.GONE
                room_left_text_view.visibility = if (roomListModel.roomLeft <= 2) View.VISIBLE else View.GONE
                room_left_text_view.text = getString(R.string.hotel_room_room_left_text, roomListModel.roomLeft.toString())
                cc_not_required_text_view.visibility = if (roomListModel.isCcRequired) View.GONE else View.VISIBLE
                initRoomFacility(roomListModel.breakfastIncluded, roomListModel.isRefundable, roomListModel.roomFacility)

                choose_room_button.setOnClickListener { listener.onClickBookListener(hotelRoom) }
                choose_room_button.text = getString(R.string.hotel_room_list_choose_room_button, "")
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
            room_facility_recycler_view.removeAllViews()

            var breakfastTextView = FacilityTextView(context)

            if (breakfastIncluded) {
                breakfastTextView.setIconAndText(R.drawable.ic_hotel_free_breakfast, getString(R.string.hotel_room_list_free_breakfast))
            } else {
                breakfastTextView.setIconAndText(R.drawable.ic_hotel_no_breakfast, getString(R.string.hotel_room_list_breakfast_not_included))
            }
            room_facility_recycler_view.addView(breakfastTextView)


            var refundableTextView = FacilityTextView(context)
            if (refundable) {
                refundableTextView.setIconAndText(R.drawable.ic_hotel_refundable, getString(R.string.hotel_room_list_refundable_with_condition))
            } else {
                refundableTextView.setIconAndText(R.drawable.ic_hotel_not_refundable, getString(R.string.hotel_room_list_not_refundable))
            }
            room_facility_recycler_view.addView(refundableTextView)


            for (i in 0..min(roomFacility.size, 1)) {
                var textView = FacilityTextView(context)
                textView.setIconAndText(roomFacility[i].iconUrl, roomFacility[i].name)
                room_facility_recycler_view.addView(textView)
            }
        }
    }

    fun setImageViewPager(imageUrls: List<String>) {
        with(itemView) {
            room_image_view_pager.setImages(imageUrls)
            room_image_view_pager.imageViewPagerListener = object : ImageViewPager.ImageViewPagerListener{
                override fun onImageClicked(position: Int) {
                    context.startActivity(ImagePreviewSliderActivity.getCallingIntent(
                            context!!, "Image", imageUrls, imageUrls, position
                    ))
                }
            }
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
            roomListModel.roomSize = hotelRoom.roomInfo.size.toString()
            roomListModel.maxOccupancy = hotelRoom.occupancyInfo.maxOccupancy
            roomListModel.maxFreeChild = hotelRoom.occupancyInfo.maxFreeChild
            roomListModel.occupancyText = hotelRoom.occupancyInfo.occupancyText
            roomListModel.bedInfo = hotelRoom.bedInfo
            roomListModel.roomFacility = hotelRoom.roomInfo.facility
            roomListModel.payInHotel = hotelRoom.additionalPropertyInfo.isDirectPayment
            roomListModel.breakfastIncluded = hotelRoom.breakfastInfo.isBreakfastIncluded
            roomListModel.isRefundable = hotelRoom.refundInfo.isRefundable
            roomListModel.isCcRequired = hotelRoom.creditCardInfo.isCCRequired
            roomListModel.creditCardInfo = hotelRoom.creditCardInfo.creditCardInfo
            roomListModel.price = hotelRoom.roomPrice.roomPrice
            roomListModel.roomLeft = hotelRoom.numberRoomLeft
            roomListModel.available = hotelRoom.available

            val images: MutableList<String> = arrayListOf()
            for (item in hotelRoom.roomInfo.roomImages) {
                images.add(item.urlOriginal)
            }
            roomListModel.images = images
        }
        return roomListModel
    }

    interface OnClickBookListener {
        fun onClickBookListener(room: HotelRoom)
    }

}