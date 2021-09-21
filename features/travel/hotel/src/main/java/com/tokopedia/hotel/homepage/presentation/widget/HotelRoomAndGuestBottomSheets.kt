package com.tokopedia.hotel.homepage.presentation.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.common.travel.widget.SelectPassengerView
import com.tokopedia.hotel.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton

/**
 * @author by furqan on 09/04/19
 */
class HotelRoomAndGuestBottomSheets : BottomSheetUnify() {

    lateinit var listener: HotelGuestListener

    private lateinit var spvRoomCounter: SelectPassengerView
    private lateinit var spvAdultCounter: SelectPassengerView
    private lateinit var btnSave: UnifyButton

    var roomCount = DEFAULT_ROOM
    var adultCount = DEFAULT_ADULT

    init {
        isFullpage = false
        isDragable = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        val view = View.inflate(context, R.layout.bottom_sheets_hotel_room_and_guest, null)
        setChild(view)
        initView(view)
    }

    fun initView(view: View) {
        spvRoomCounter = view.findViewById(R.id.spv_hotel_room) as SelectPassengerView
        spvAdultCounter = view.findViewById(R.id.spv_hotel_adult) as SelectPassengerView
        btnSave = view.findViewById(R.id.btn_hotel_save_guest) as UnifyButton

        spvRoomCounter.hideSubtitle()
        spvAdultCounter.hideSubtitle()

        spvRoomCounter.value = roomCount
        spvAdultCounter.value = adultCount

        spvRoomCounter.setOnPassengerCountChangeListener(object : SelectPassengerView.OnPassengerCountChangeListener {
            override fun onChange(number: Int): Boolean {
                if (spvRoomCounter.value > spvAdultCounter.value) {
                    spvAdultCounter.value = number
                }
                return true
            }
        })
        spvAdultCounter.setOnPassengerCountChangeListener(object : SelectPassengerView.OnPassengerCountChangeListener {
            override fun onChange(number: Int): Boolean {
                if (spvRoomCounter.value > spvAdultCounter.value) {
                    spvRoomCounter.value = number
                }
                return true
            }

        })
        btnSave.setOnClickListener {
            if (::listener.isInitialized) {
                listener.onSaveGuest(spvRoomCounter.value, spvAdultCounter.value)
            }
            dismiss()
        }

        setTitle(getString(R.string.hotel_bottom_sheets_room_guest_title))
    }

    interface HotelGuestListener {

        fun onSaveGuest(room: Int, adult: Int)
    }

    companion object {
        const val DEFAULT_ROOM = 1
        const val DEFAULT_ADULT = 1
    }
}