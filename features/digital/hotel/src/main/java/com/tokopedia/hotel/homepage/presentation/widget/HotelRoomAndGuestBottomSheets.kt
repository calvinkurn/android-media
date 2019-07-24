package com.tokopedia.hotel.homepage.presentation.widget

import android.view.View
import com.tokopedia.common.travel.widget.SelectPassengerView
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.hotel.R

/**
 * @author by furqan on 09/04/19
 */
class HotelRoomAndGuestBottomSheets : BottomSheets() {

    lateinit var listener: HotelGuestListener

    private lateinit var spvRoomCounter: SelectPassengerView
    private lateinit var spvAdultCounter: SelectPassengerView
    private lateinit var btnSave: ButtonCompat

    var roomCount = DEFAULT_ROOM
    var adultCount = DEFAULT_ADULT

    override fun getLayoutResourceId(): Int = R.layout.bottom_sheets_hotel_room_and_guest

    override fun initView(view: View) {
        spvRoomCounter = view.findViewById(R.id.spv_hotel_room) as SelectPassengerView
        spvAdultCounter = view.findViewById(R.id.spv_hotel_adult) as SelectPassengerView
        btnSave = view.findViewById(R.id.btn_hotel_save_guest) as ButtonCompat

        spvRoomCounter.hideSubtitle()
        spvAdultCounter.hideSubtitle()

        spvRoomCounter.value = roomCount
        spvAdultCounter.value = adultCount

        spvRoomCounter.setOnPassengerCountChangeListener {
            if (spvRoomCounter.value > spvAdultCounter.value) {
                spvAdultCounter.value = it
            }
            true
        }
        spvAdultCounter.setOnPassengerCountChangeListener {
            if (spvRoomCounter.value > spvAdultCounter.value) {
                spvRoomCounter.value = it
            }
            true
        }
        btnSave.setOnClickListener {
            if (::listener.isInitialized) {
                listener.onSaveGuest(spvRoomCounter.value, spvAdultCounter.value)
            }
            dismiss()
        }
    }

    override fun title(): String = getString(R.string.hotel_bottom_sheets_room_guest_title)

    interface HotelGuestListener {

        fun onSaveGuest(room: Int, adult: Int)
    }

    companion object {
        const val DEFAULT_ROOM = 1
        const val DEFAULT_ADULT = 1
    }
}