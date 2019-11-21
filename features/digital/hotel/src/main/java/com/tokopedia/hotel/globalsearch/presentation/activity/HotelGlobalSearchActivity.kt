package com.tokopedia.hotel.globalsearch.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.common.presentation.HotelBaseActivity
import com.tokopedia.hotel.globalsearch.di.DaggerHotelGlobalSearchComponent
import com.tokopedia.hotel.globalsearch.di.HotelGlobalSearchComponent

class HotelGlobalSearchActivity : HotelBaseActivity(), HasComponent<HotelGlobalSearchComponent> {

    override fun getComponent(): HotelGlobalSearchComponent =
            DaggerHotelGlobalSearchComponent.builder()
                    .hotelComponent(HotelComponentInstance.getHotelComponent(application))
                    .build()

    override fun shouldShowOptionMenu(): Boolean = false

    override fun getNewFragment(): Fragment? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
