package com.tokopedia.shopdiscount.utils.navigation

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
}