package com.tokopedia.hotel.booking.presentation.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.R
import com.tokopedia.hotel.booking.di.DaggerHotelBookingComponent
import com.tokopedia.hotel.booking.di.HotelBookingComponent
import com.tokopedia.hotel.booking.presentation.fragment.HotelBookingFragment
import com.tokopedia.hotel.common.presentation.HotelBaseActivity

class HotelBookingActivity : HotelBaseActivity(), HasComponent<HotelBookingComponent> {
    override fun getParentViewResourceID(): Int = com.tokopedia.abstraction.R.id.parent_view

    override fun getLayoutRes() = com.tokopedia.abstraction.R.layout.activity_base_simple

    override fun getComponent(): HotelBookingComponent =
        DaggerHotelBookingComponent.builder()
                .hotelComponent(HotelComponentInstance.getHotelComponent(application))
                .build()

    override fun getNewFragment(): Fragment =
            HotelBookingFragment.getInstance(
                    intent.getStringExtra(HotelBookingFragment.ARG_CART_ID),
                    intent.getStringExtra(HotelBookingFragment.ARG_DESTINATION_TYPE),
                    intent.getStringExtra(HotelBookingFragment.ARG_DESTINATION_NAME),
                    intent.getIntExtra(HotelBookingFragment.ARG_ROOM_COUNT, 0),
                    intent.getIntExtra(HotelBookingFragment.ARG_GUEST_COUNT, 0)
            )

    override fun shouldShowOptionMenu(): Boolean = false

    override fun onBackPressed() {
        val dialog = DialogUnify(this as AppCompatActivity, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(getString(R.string.hotel_booking_back_dialog_title))
        dialog.setDescription(getString(R.string.hotel_booking_back_dialog_desc))
        dialog.setPrimaryCTAText(getString(R.string.hotel_booking_back_dialog_yes))
        dialog.setPrimaryCTAClickListener {
            dialog.dismiss()
        }
        dialog.setSecondaryCTAText(getString(R.string.hotel_booking_back_dialog_no))
        dialog.setSecondaryCTAClickListener {
            dialog.dismiss()
            super.onBackPressed()
        }
        dialog.show()
    }

    override fun getScreenName(): String = HOTEL_BOOKING_SCREEN_NAME

    companion object {

        const val HOTEL_BOOKING_SCREEN_NAME = "/hotel/bookingroom"
        
        fun getCallingIntent(context: Context, cartId: String, destinationType: String, destinationName: String, roomCount: Int, guestCount: Int): Intent =
                Intent(context, HotelBookingActivity::class.java)
                        .putExtra(HotelBookingFragment.ARG_CART_ID, cartId)
                        .putExtra(HotelBookingFragment.ARG_DESTINATION_TYPE, destinationType)
                        .putExtra(HotelBookingFragment.ARG_DESTINATION_NAME, destinationName)
                        .putExtra(HotelBookingFragment.ARG_ROOM_COUNT, roomCount)
                        .putExtra(HotelBookingFragment.ARG_GUEST_COUNT, guestCount)
    }
}