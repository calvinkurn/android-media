package com.tokopedia.hotel.booking.presentation.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.booking.di.DaggerHotelBookingComponent
import com.tokopedia.hotel.booking.di.HotelBookingComponent
import com.tokopedia.hotel.booking.presentation.fragment.HotelBookingFragment
import com.tokopedia.hotel.common.presentation.HotelBaseActivity
import com.tokopedia.hotel.roomdetail.di.HotelRoomDetailComponent

class HotelBookingActivity : HotelBaseActivity(), HasComponent<HotelBookingComponent> {

    override fun getComponent(): HotelBookingComponent =
        DaggerHotelBookingComponent.builder()
                .hotelComponent(HotelComponentInstance.getHotelComponent(application))
                .build()

    override fun getNewFragment(): Fragment =
            HotelBookingFragment.getInstance(
                    intent.getStringExtra(HotelBookingFragment.ARG_CART_ID)
            )

    override fun shouldShowOptionMenu(): Boolean = false

    companion object {
        fun getCallingIntent(context: Context, cartId: String): Intent =
                Intent(context, HotelBookingActivity::class.java)
                        .putExtra(HotelBookingFragment.ARG_CART_ID, cartId)
    }
}