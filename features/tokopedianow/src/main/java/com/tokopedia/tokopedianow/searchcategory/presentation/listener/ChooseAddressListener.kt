package com.tokopedia.tokopedianow.searchcategory.presentation.listener

import androidx.fragment.app.Fragment

interface ChooseAddressListener {

    fun onLocalizingAddressSelected()

    fun getFragment(): Fragment
}