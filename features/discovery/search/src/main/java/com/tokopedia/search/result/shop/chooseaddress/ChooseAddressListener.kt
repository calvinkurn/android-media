package com.tokopedia.search.result.shop.chooseaddress

import androidx.fragment.app.Fragment

internal interface ChooseAddressListener {

    fun onLocalizingAddressSelected()

    fun getFragment(): Fragment

    fun onViewPagerChanged()
}