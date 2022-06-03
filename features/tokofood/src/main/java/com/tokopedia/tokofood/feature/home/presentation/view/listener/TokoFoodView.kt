package com.tokopedia.tokofood.feature.home.presentation.view.listener

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

interface TokoFoodView {
    fun getFragmentPage(): Fragment
    fun getFragmentManagerPage(): FragmentManager
    fun refreshLayoutPage()
}