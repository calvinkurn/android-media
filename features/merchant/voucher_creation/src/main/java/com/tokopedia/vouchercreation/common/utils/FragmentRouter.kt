package com.tokopedia.vouchercreation.common.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import javax.inject.Inject

class FragmentRouter @Inject constructor() {

    fun replace(manager: FragmentManager, containerId: Int, fragment: Fragment) {
        manager
            .beginTransaction()
            .replace(containerId, fragment)
            .commit()
    }

    fun replaceAndAddToBackstack(manager: FragmentManager, containerId: Int, fragment: Fragment) {
        manager
            .beginTransaction()
            .replace(containerId, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun popFragment(manager: FragmentManager) {
        manager.popBackStack()
    }
}