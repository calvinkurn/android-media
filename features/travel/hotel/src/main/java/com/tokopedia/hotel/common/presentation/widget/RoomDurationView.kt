package com.tokopedia.hotel.common.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.util.HotelUtils
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_hotel_room_duration.view.*

/**
 * @author by resakemal on 20/05/19
 */

class RoomDurationView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
        BaseCustomView(context, attrs, defStyleAttr)  {

    init {
        View.inflate(context, R.layout.widget_hotel_room_duration, this)
    }

    fun setRoomDates(checkInDate: String, checkOutDate: String) {
        hotel_check_in_date.text = changeDateStringFormat(checkInDate,
                TravelDateUtil.YYYY_MM_DD, TravelDateUtil.DEFAULT_VIEW_FORMAT)
        hotel_check_out_date.text = changeDateStringFormat(checkOutDate,
                TravelDateUtil.YYYY_MM_DD, TravelDateUtil.DEFAULT_VIEW_FORMAT)

        hotel_room_night_count.text = context.getString(R.string.hotel_room_night_count,
                HotelUtils.countDayDifference(checkInDate, checkOutDate))
    }

    fun setRoomCheckTimes(checkInTime: String, checkOutTime: String) {
        hotel_check_in_time.show()
        hotel_check_out_time.show()

        hotel_check_in_time.text = checkInTime
        hotel_check_out_time.text = checkOutTime
    }

    fun setRoomDatesFormatted(checkInDate: String, checkOutDate: String, nightCount: String) {
        hotel_check_in_date.text = checkInDate
        hotel_check_out_date.text = checkOutDate
        hotel_room_night_count.text = nightCount
    }

    fun changeDateStringFormat(dateString: String, oldFormat: String, newFormat: String): String {
        return TravelDateUtil.dateToString(newFormat, TravelDateUtil.stringToDate(oldFormat, dateString))
    }
}