package com.tokopedia.hotel.cancellation.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.cancellation.di.DaggerHotelCancellationComponent
import com.tokopedia.hotel.cancellation.di.HotelCancellationComponent
import com.tokopedia.hotel.cancellation.presentation.fragment.HotelCancellationConfirmationFragment
import com.tokopedia.hotel.common.presentation.HotelBaseActivity

/**
 * @author by jessica on 08/05/20
 */

class HotelCancellationConfirmationActivity: HotelBaseActivity(), HasComponent<HotelCancellationComponent> {
    override fun shouldShowOptionMenu(): Boolean = false

    override fun getNewFragment(): Fragment = HotelCancellationConfirmationFragment()

    override fun getComponent(): HotelCancellationComponent = DaggerHotelCancellationComponent.builder()
            .hotelComponent(HotelComponentInstance.getHotelComponent(application))
            .build()

    companion object {
        fun getCallingIntent(context: Context): Intent = Intent(context, HotelCancellationConfirmationActivity::class.java)
        const val CANCELLATION_REASON_FRAGMENT_TAG = "CANCELLATION_REASON"
    }
}