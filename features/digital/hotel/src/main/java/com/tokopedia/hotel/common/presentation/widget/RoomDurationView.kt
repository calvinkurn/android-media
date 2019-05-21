package com.tokopedia.hotel.common.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.util.HotelUtils
import kotlinx.android.synthetic.main.widget_hotel_room_duration.view.*

/**
 * @author by resakemal on 20/05/19
 */

class RoomDurationView : BaseCustomView {

    init {
        View.inflate(context, R.layout.widget_hotel_room_duration, this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr)

    fun setRoomDates(checkInDate: String, checkOutDate: String) {
        hotel_check_in_date.text = changeDateStringFormat(checkInDate,
                TravelDateUtil.YYYY_MM_DD, TravelDateUtil.DEFAULT_VIEW_FORMAT)
        hotel_check_out_date.text = changeDateStringFormat(checkOutDate,
                TravelDateUtil.YYYY_MM_DD, TravelDateUtil.DEFAULT_VIEW_FORMAT)

        hotel_room_night_count.text = context.getString(R.string.hotel_room_night_count,
                HotelUtils.countNightDifference(checkInDate, checkOutDate))
    }

    fun setRoomCheckTimes(checkInTime: String, checkOutTime: String) {
        hotel_check_in_time.visibility = View.VISIBLE
        hotel_check_out_time.visibility = View.VISIBLE

        hotel_check_in_time.text = checkInTime
        hotel_check_out_time.text = checkOutTime
    }

    fun changeDateStringFormat(dateString: String, oldFormat: String, newFormat: String): String {
        return TravelDateUtil.dateToString(newFormat, TravelDateUtil.stringToDate(oldFormat, dateString))
    }
}