package com.tokopedia.hotel.booking.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.booking.di.DaggerHotelBookingComponent
import com.tokopedia.hotel.booking.di.HotelBookingComponent
import com.tokopedia.hotel.booking.presentation.fragment.HotelPayAtHotelPromoFragment
import com.tokopedia.hotel.common.presentation.HotelBaseActivity

class HotelPayAtHotelPromoActivity : HotelBaseActivity(), HasComponent<HotelBookingComponent> {

    override fun shouldShowOptionMenu(): Boolean = false

    override fun getNewFragment(): Fragment = HotelPayAtHotelPromoFragment.newInstance()

    override fun getComponent(): HotelBookingComponent = DaggerHotelBookingComponent.builder()
            .hotelComponent(HotelComponentInstance.getHotelComponent(application))
            .build()

    companion object{
        fun getCallingIntent(context: Context): Intent =
                Intent(context, HotelPayAtHotelPromoActivity::class.java)
    }
}