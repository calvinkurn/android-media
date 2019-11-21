package com.tokopedia.hotel.globalsearch.presentation.fragment

import com.tokopedia.hotel.common.presentation.HotelBaseFragment
import com.tokopedia.hotel.globalsearch.di.HotelGlobalSearchComponent

/**
 * @author by furqan on 19/11/2019
 */
class HotelGlobalSearchFragment : HotelBaseFragment() {

    override fun onErrorRetryClicked() { }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(HotelGlobalSearchComponent::class.java).inject(this)
    }

}