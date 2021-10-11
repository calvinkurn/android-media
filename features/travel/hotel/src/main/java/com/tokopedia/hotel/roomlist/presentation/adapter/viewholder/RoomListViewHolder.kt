package com.tokopedia.hotel.roomlist.presentation.adapter.viewholder

import android.graphics.Paint
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.presentation.widget.FacilityTextView
import com.tokopedia.hotel.databinding.ItemHotelRoomListBinding
import com.tokopedia.hotel.roomlist.data.model.HotelRoom
import com.tokopedia.hotel.roomlist.data.model.HotelRoomInfo
import com.tokopedia.hotel.roomlist.data.model.RoomListModel
import com.tokopedia.hotel.roomlist.widget.ImageViewPager
import com.tokopedia.imagepreviewslider.presentation.util.ImagePreviewSlider
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import kotlin.math.min
import kotlinx.android.synthetic.main.layout_hotel_image_slider.view.*

/**
 * @author by jessica on 25/03/19
 */

class RoomListViewHolder(val binding: ItemHotelRoomListBinding, val listener: OnClickBookListener) : AbstractViewHolder<HotelRoom>(binding.root) {

    override fun bind(hotelRoom: HotelRoom) {
        with(binding) {
            val roomListModel = mapToRoomListModel(hotelRoom)

            if (roomListModel.available) {

                roomDescriptionLayout.visibility = View.VISIBLE
                roomFullLayout.root.visibility = View.GONE

                setImageViewPager(roomListModel.images, hotelRoom)
                roomNameTextView.text = roomListModel.roomName
                maxOccupancyTextView.text = roomListModel.occupancyText
                bedInfoTextView.text = itemView.context.getString(R.string.hotel_room_list_room_description,
                        roomListModel.roomSize,
                        roomListModel.bedInfo)
                roomPriceTextView.text = roomListModel.price
                payHotelLayout.visibility = if (roomListModel.payInHotel) View.VISIBLE else View.GONE
                payHotelTextView.text = roomListModel.payInHotelString
                roomLeftTextView.visibility = if (roomListModel.roomLeft <= 2) View.VISIBLE else View.GONE
                roomLeftTextView.text = getString(R.string.hotel_room_room_left_text, roomListModel.roomLeft.toString())
                ccNotRequiredTextView.text = roomListModel.creditCardHeader
                initRoomFacility(roomListModel.breakfastInfo, roomListModel.refundInfo, roomListModel.roomFacility)

                chooseRoomButton.setOnClickListener { listener.onClickBookListener(hotelRoom) }
                chooseRoomButton.text = getString(R.string.hotel_room_list_choose_room_button, "")


                if (roomListModel.slashPrice.isNotEmpty()) {
                    roomListSlashPriceTv.show()
                    roomListSlashPriceTv.paintFlags = roomListSlashPriceTv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    roomListSlashPriceTv.text = roomListModel.slashPrice
                } else roomListSlashPriceTv.hide()

                if (roomListModel.tagging.isNotEmpty()) {
                    roomListTaggingTv.show()
                    roomListTaggingTv.text = roomListModel.tagging
                } else roomListTaggingTv.hide()

            } else {
                roomDescriptionLayout.visibility = View.GONE
                roomFullLayout.root.visibility = View.VISIBLE
                if (roomListModel.images.isNotEmpty()) roomFullLayout.roomListRoomFullImageView.loadImage(roomListModel.images.first())
                roomFullLayout.roomFullRoomNameTextView.text = roomListModel.roomName
            }
        }
    }

    private fun initRoomFacility(breakfastInfo: HotelRoom.RoomBreakfastInfo, refundInfo: HotelRoom.RefundInfo, roomFacility: List<HotelRoomInfo.Facility>) {
        with(binding) {
            roomFacilityRecyclerView.removeAllViews()

            if (breakfastInfo.breakFast.isNotEmpty()) {
                val breakfastTextView = FacilityTextView(itemView.context)
                breakfastTextView.setIconAndText(breakfastInfo.iconUrl, breakfastInfo.breakFast)
                roomFacilityRecyclerView.addView(breakfastTextView)
            }

            if (refundInfo.refundStatus.isNotEmpty()) {
                val refundableTextView = FacilityTextView(itemView.context)
                refundableTextView.setIconAndText(refundInfo.iconUrl, refundInfo.refundStatus)
                roomFacilityRecyclerView.addView(refundableTextView)
            }

            for (i in 0 until min(roomFacility.size, 2)) {
                var textView = FacilityTextView(itemView.context)
                textView.setIconAndText(roomFacility[i].iconUrl, roomFacility[i].name)
                roomFacilityRecyclerView.addView(textView)
            }
        }
    }

    private fun setImageViewPager(imageUrls: List<String>, room: HotelRoom) {
        with(binding) {
            if (imageUrls.size >= 5) roomImageViewPager.setImages(imageUrls.subList(0, 5))
            else roomImageViewPager.setImages(imageUrls)
            roomImageViewPager.imageViewPagerListener = object : ImageViewPager.ImageViewPagerListener {
                override fun onImageClicked(position: Int) {
                    listener.onPhotoClickListener(room)
                    ImagePreviewSlider.instance.start(itemView.context, room.roomInfo.name, imageUrls, imageUrls, position, itemView.image_banner)
                }
            }
            roomImageViewPager.buildView()
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