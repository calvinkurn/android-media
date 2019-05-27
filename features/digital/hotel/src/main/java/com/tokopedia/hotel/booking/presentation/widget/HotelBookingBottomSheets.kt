package com.tokopedia.hotel.booking.presentation.widget

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.design.component.TextViewCompat
import com.tokopedia.hotel.R

/**
 * @author by resakemal on 15/05/19
 */
class HotelBookingBottomSheets : BottomSheets() {

    var contentViewList: ArrayList<View> = arrayListOf()
    var title = ""

    override fun getLayoutResourceId(): Int = R.layout.bottom_sheets_hotel_booking

    override fun initView(view: View) {
        val contentView: LinearLayout = view.findViewById(R.id.hotel_booking_container)
        for (viewItem in contentViewList) contentView.addView(viewItem)
    }

    override fun title() = title

    fun addContentView(view: View) {
        contentViewList.add(view)
    }
}