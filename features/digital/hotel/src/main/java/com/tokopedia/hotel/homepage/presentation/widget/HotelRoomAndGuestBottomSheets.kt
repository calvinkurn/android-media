package com.tokopedia.hotel.homepage.presentation.widget

import android.support.annotation.StringRes
import android.view.View
import com.tokopedia.common.travel.widget.SelectPassengerView
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.hotel.R

/**
 * @author by furqan on 09/04/19
 */
class HotelRoomAndGuestBottomSheets : BottomSheets() {

    private lateinit var listener: HotelGuestListener

    private lateinit var spvRoomCounter: SelectPassengerView
    private lateinit var spvAdultCounter: SelectPassengerView
    private lateinit var spvChildCounter: SelectPassengerView
    private lateinit var btnSave: ButtonCompat

    override fun getLayoutResourceId(): Int = R.layout.bottom_sheets_hotel_room_and_guest

    override fun initView(view: View?) {
        spvRoomCounter = view?.findViewById(R.id.spv_hotel_room) as SelectPassengerView
        spvAdultCounter = view?.findViewById(R.id.spv_hotel_adult) as SelectPassengerView
        spvChildCounter = view?.findViewById(R.id.spv_hotel_child) as SelectPassengerView
        btnSave = view?.findViewById(R.id.btn_hotel_save_guest) as ButtonCompat

        spvRoomCounter.value = DEFAULT_ROOM
        spvAdultCounter.value = DEFAULT_ADULT
        spvChildCounter.value = DEFAULT_CHILD

        spvRoomCounter.setOnPassengerCountChangeListener {
            if (!validGuestInfo()) {
                spvRoomCounter.value = it - 1
            }
            true
        }
        spvAdultCounter.setOnPassengerCountChangeListener {
            if (!validGuestInfo()) {
                spvAdultCounter.value = it - 1
            }
            true
        }
        spvChildCounter.setOnPassengerCountChangeListener {
            if (!validGuestInfo()) {
                spvChildCounter.value = it - 1
            }
            true
        }
        btnSave.setOnClickListener {
            if (::listener.isInitialized) {
                listener.onSaveGuest(spvRoomCounter.value, spvAdultCounter.value, spvChildCounter.value)
            }
            dismiss()
        }
    }

    override fun title(): String = getString(R.string.hotel_bottom_sheets_room_guest_title)

    private fun validGuestInfo(): Boolean =
            if (spvRoomCounter.value > spvAdultCounter.value) {
                listener.showGuestErrorTicker(R.string.hotel_guest_info_room_more_than_adult_error_message)
                false
            } else {
                // valid
                true
            }

    interface HotelGuestListener {

        fun onSaveGuest(room: Int, adult: Int, child: Int)

        fun showGuestErrorTicker(@StringRes resId: Int)
    }

    companion object {
        val DEFAULT_ROOM = 1
        val DEFAULT_ADULT = 1
        val DEFAULT_CHILD = 0
    }
}