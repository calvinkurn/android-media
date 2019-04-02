package com.tokopedia.hotel.search.presentation.activity

import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.common.di.component.HotelComponent

class HotelSearchResultActivity: BaseSimpleActivity(), HasComponent<HotelComponent> {

    override fun getNewFragment(): Fragment {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getComponent(): HotelComponent = HotelComponentInstance.getHotelComponent(application)
}