package com.tokopedia.hotel.booking.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.common.travel.presentation.model.TravelContactData
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.booking.di.DaggerHotelBookingComponent
import com.tokopedia.hotel.booking.di.HotelBookingComponent
import com.tokopedia.hotel.booking.presentation.fragment.HotelContactDataFragment

class HotelContactDataActivity: BaseSimpleActivity(), HasComponent<HotelBookingComponent> {

    override fun getNewFragment(): Fragment =
            HotelContactDataFragment.getInstance(
                    intent.getParcelableExtra(EXTRA_INITIAL_CONTACT_DATA)
            )

    override fun getComponent(): HotelBookingComponent =
            DaggerHotelBookingComponent.builder()
                    .hotelComponent(HotelComponentInstance.getHotelComponent(application))
                    .build()


    companion object {

        const val EXTRA_INITIAL_CONTACT_DATA = "EXTRA_INITIAL_CONTACT_DATA"

        fun getCallingIntent(context: Context, contactData: TravelContactData): Intent =
                Intent(context, HotelContactDataActivity::class.java)
                        .putExtra(EXTRA_INITIAL_CONTACT_DATA, contactData)
    }
}