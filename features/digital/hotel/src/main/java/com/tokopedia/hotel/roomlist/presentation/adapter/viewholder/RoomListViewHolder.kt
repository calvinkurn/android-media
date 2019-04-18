package com.tokopedia.hotel.roomlist.presentation.adapter.viewholder

import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.list.adapter.TouchImageAdapter
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.hotel.R
import com.tokopedia.hotel.destination.data.model.SearchDestination
import com.tokopedia.hotel.destination.view.adapter.SearchDestinationListener
import com.tokopedia.hotel.roomlist.data.model.RoomListModel
import com.tokopedia.hotel.roomlist.presentation.adapter.RoomFacilityAdapter
import com.tokopedia.hotel.roomlist.widget.ImageViewPager
import com.tokopedia.hotel.roomlist.widget.ImageViewPagerAdapter
import kotlinx.android.synthetic.main.item_hotel_room_list.view.*
import kotlinx.android.synthetic.main.item_search_destination_result.view.*

/**
 * @author by jessica on 25/03/19
 */

class RoomListViewHolder(val view: View, val imageViewPagerListener: ImageViewPager.ImageViewPagerListener): AbstractViewHolder<RoomListModel>(view) {

   override fun bind(roomListModel: RoomListModel) {
        with(itemView) {
            room_image_view_pager.imageViewPagerListener = imageViewPagerListener
            room_image_view_pager.imageUrls = ArrayList(roomListModel.images)
            room_image_view_pager.buildView()
            room_image_view_pager.setPagerAdapter(ImageViewPagerAdapter(roomListModel.images,
                    room_image_view_pager.imageViewPagerListener))
            room_name_text_view.text = roomListModel.roomName
            max_occupancy_text_view.text = roomListModel.roomOccupancyInfo.occupancyText
            bed_info_text_view.text = roomListModel.bedInfo
            room_price_text_view.text = roomListModel.price
            pay_hotel_text_view.visibility = if (roomListModel.payInHotel) View.VISIBLE else View.GONE
            cc_not_required_text_view.visibility = if (roomListModel.isCcRequired) View.GONE else View.VISIBLE

            var adapter = RoomFacilityAdapter()
            if (roomListModel.breakfastIncluded) adapter.addFacility("Termasuk Sarapan") else adapter.addFacility("Tidak Termasuk Sarapan")
            if (roomListModel.isRefundable) adapter.addFacility("Dapat direfund") else adapter.addFacility("Tidak dapat direfund")
            adapter.addFacility(roomListModel.roomFacility[0].name)
            adapter.addFacility(roomListModel.roomFacility[1].name)

            room_facility_recycler_view.adapter = adapter
        }
    }

    companion object {
        val LAYOUT = R.layout.item_hotel_room_list
    }

}