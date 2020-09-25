package com.tokopedia.notifications

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.listener.FragmentLifecycleCallback
import com.tokopedia.notifications.inApp.CMInAppManager

class FragmentObserver(private val cmInAppManager: CMInAppManager) : FragmentLifecycleCallback {

    override fun onFragmentStart(fragment: Fragment) {
        try {
            cmInAppManager.onFragmentStart(fragment)
        } catch (e: Exception) {
        }
    }

    override fun onFragmentStop(fragment: Fragment) {
        try {
            cmInAppManager.onFragmentStop(fragment)
        } catch (e: Exception) {
        }
    }

    override fun onFragmentSelected(fragment: Fragment) {
        try {
            cmInAppManager.onFragmentSelected(fragment)
        } catch (e: Exception) {
        }
    }

    override fun onFragmentUnSelected(fragment: Fragment) {
        try {
            cmInAppManager.onFragmentUnSelected(fragment)
        } catch (e: Exception) {
        }
    }

    override fun onFragmentDestroyed(fragment: Fragment) {
        try {
            cmInAppManager.onFragmentDestroyed(fragment)
        } catch (e: Exception) {
        }
    }
}