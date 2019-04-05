package com.tokopedia.hotel.search.presentation.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.common.di.component.HotelComponent
import com.tokopedia.hotel.search.presentation.fragment.HotelSearchResultFragment

class HotelSearchResultActivity: BaseSimpleActivity(), HasComponent<HotelComponent> {

    override fun getNewFragment(): Fragment = HotelSearchResultFragment()

    override fun getComponent(): HotelComponent = HotelComponentInstance.getHotelComponent(application)

    companion object {
        fun createIntent(context: Context) = Intent(context, HotelSearchResultActivity::class.java)
    }
}