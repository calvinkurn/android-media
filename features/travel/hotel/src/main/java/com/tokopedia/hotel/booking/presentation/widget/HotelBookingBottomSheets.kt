package com.tokopedia.hotel.booking.presentation.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.tokopedia.hotel.R
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * @author by resakemal on 15/05/19
 */
class HotelBookingBottomSheets : BottomSheetUnify() {

    private var contentViewList: ArrayList<View> = arrayListOf()

    init {
        isFullpage = false
        setTitle("")
        isDragable = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        val view = View.inflate(context, R.layout.bottom_sheets_hotel_booking, null)
        setChild(view)
        initView(view)
    }

    fun initView(view: View) {
        val contentView: LinearLayout = view.findViewById(R.id.hotel_booking_container)
        for (viewItem in contentViewList) contentView.addView(viewItem)
    }

    fun addContentView(view: View) {
        contentViewList.add(view)
    }
}