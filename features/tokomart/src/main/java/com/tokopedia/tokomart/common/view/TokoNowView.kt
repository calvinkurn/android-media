package com.tokopedia.tokomart.common.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

interface TokoNowView {
    fun getTokoNowFragment(): Fragment
    fun getTokoNowFragmentManager(): FragmentManager
}

