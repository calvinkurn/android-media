package com.tokopedia.hotel.common.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.hotel.R
import com.tokopedia.hotel.databinding.WidgetHotelRoomDurationBinding
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.utils.date.DateUtil

/**
 * @author by resakemal on 20/05/19
 */

class RoomDurationView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
        BaseCustomView(context, attrs, defStyleAttr)  {

    private val binding = WidgetHotelRoomDurationBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    fun setViewLabel(checkInLabel: String, checkOutLabel: String) {
        with(binding) {
            hotelCheckInLabel.text = checkInLabel
            hotelCheckOutLabel.text = checkOutLabel
        }
    }

    fun setRoomDates(checkInDate: String, checkOutDate: String) {
        with(binding) {
            hotelCheckInDate.text = DateUtil.formatDate(DateUtil.YYYY_MM_DD,
                DateUtil.DEFAULT_VIEW_FORMAT, checkInDate)
            hotelCheckOutDate.text = DateUtil.formatDate(DateUtil.YYYY_MM_DD,
                DateUtil.DEFAULT_VIEW_FORMAT, checkOutDate)

            hotelRoomNightCount.text = context.getString(R.string.hotel_room_night_count,
                DateUtil.getDayDiff(checkInDate, checkOutDate))
        }
    }

    fun setRoomCheckTimes(checkInTime: String, checkOutTime: String) {
        with(binding) {
            hotelCheckInTime.show()
            hotelCheckOutTime.show()

            hotelCheckInTime.text = checkInTime
            hotelCheckOutTime.text = checkOutTime
        }
    }

    fun setRoomDatesFormatted(checkInDate: String, checkOutDate: String, nightCount: String) {
        with(binding) {
            hotelCheckInDate.text = checkInDate
            hotelCheckOutDate.text = checkOutDate
            hotelRoomNightCount.text = nightCount
        }
    }

}
