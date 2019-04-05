package com.tokopedia.hotel.search.presentation.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.search.di.DaggerHotelSearchPropertyComponent
import com.tokopedia.hotel.search.di.HotelSearchPropertyComponent
import com.tokopedia.hotel.search.presentation.fragment.HotelSearchResultFragment

class HotelSearchResultActivity: BaseSimpleActivity(), HasComponent<HotelSearchPropertyComponent> {

    override fun getNewFragment(): Fragment = HotelSearchResultFragment()

    override fun getComponent(): HotelSearchPropertyComponent =
            DaggerHotelSearchPropertyComponent.builder()
                    .hotelComponent(HotelComponentInstance.getHotelComponent(application))
                    .build()


    companion object {
        fun createIntent(context: Context) = Intent(context, HotelSearchResultActivity::class.java)
    }
}