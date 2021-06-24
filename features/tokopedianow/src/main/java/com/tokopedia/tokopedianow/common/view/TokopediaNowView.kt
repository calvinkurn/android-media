package com.tokopedia.tokopedianow.common.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

interface TokopediaNowView {
    fun getFragmentPage(): Fragment
    fun getFragmentManagerPage(): FragmentManager
    fun refreshLayoutPage()
}

