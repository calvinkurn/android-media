package com.tokopedia.hotel.cancellation.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.cancellation.di.DaggerHotelCancellationComponent
import com.tokopedia.hotel.cancellation.di.HotelCancellationComponent
import com.tokopedia.hotel.cancellation.presentation.fragment.HotelCancellationFragment
import com.tokopedia.hotel.common.presentation.HotelBaseActivity

/**
 * @author by jessica on 27/04/20
 */

class HotelCancellationActivity: HotelBaseActivity(), HasComponent<HotelCancellationComponent> {
    override fun shouldShowOptionMenu(): Boolean = false

    override fun getNewFragment(): Fragment = HotelCancellationFragment()

    companion object {
        fun getCallingIntent(context: Context): Intent = Intent(context, HotelCancellationActivity::class.java)
    }

    override fun getComponent(): HotelCancellationComponent = DaggerHotelCancellationComponent.builder()
            .hotelComponent(HotelComponentInstance.getHotelComponent(application))
            .build()
}