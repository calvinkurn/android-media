package com.tokopedia.search.result.presentation.view.listener

import androidx.fragment.app.Fragment

interface ChooseAddressListener {

    fun onLocalizingAddressSelected()

    fun getFragment(): Fragment
}