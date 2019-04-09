package com.tokopedia.hotel.homepage.presentation.widget

import android.view.View
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.hotel.R

/**
 * @author by furqan on 09/04/19
 */
class HotelRoomAndGuestBottomSheets: BottomSheets() {

    override fun getLayoutResourceId(): Int = R.layout.bottom_sheets_hotel_room_and_guest

    override fun initView(view: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun title(): String = getString(R.string.hotel_bottom_sheets_room_guest_title)

}