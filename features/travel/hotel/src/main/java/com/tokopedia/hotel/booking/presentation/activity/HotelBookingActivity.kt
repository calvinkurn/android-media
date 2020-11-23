package com.tokopedia.hotel.booking.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
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

    var cartId: String = ""

    override fun getParentViewResourceID(): Int = com.tokopedia.abstraction.R.id.parent_view

    override fun getLayoutRes() = com.tokopedia.abstraction.R.layout.activity_base_simple

    override fun getComponent(): HotelBookingComponent =
        DaggerHotelBookingComponent.builder()
                .hotelComponent(HotelComponentInstance.getHotelComponent(application))
                .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        val uri = intent.data
        if (uri != null) {
            cartId = uri.lastPathSegment ?: ""
        } else if (intent.extras?.containsKey(HotelBookingFragment.ARG_CART_ID) == true) {
            cartId = intent.getStringExtra(HotelBookingFragment.ARG_CART_ID) ?: ""
        }

        super.onCreate(savedInstanceState)
    }

    override fun getNewFragment(): Fragment = HotelBookingFragment.getInstance(cartId)

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
        
        fun getCallingIntent(context: Context, cartId: String): Intent =
                Intent(context, HotelBookingActivity::class.java)
                        .putExtra(HotelBookingFragment.ARG_CART_ID, cartId)
    }
}