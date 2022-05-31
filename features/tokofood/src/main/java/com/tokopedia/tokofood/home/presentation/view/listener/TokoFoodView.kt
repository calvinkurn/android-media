package com.tokopedia.tokofood.home.presentation.view.listener

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

interface TokoFoodView {
    fun getFragmentPage(): Fragment
    fun getFragmentManagerPage(): FragmentManager
    fun refreshLayoutPage()
}