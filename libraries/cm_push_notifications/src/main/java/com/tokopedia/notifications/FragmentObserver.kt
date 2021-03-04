package com.tokopedia.notifications

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.lifecycle.FragmentLifecycleCallback
import com.tokopedia.notifications.inApp.CmFragmentLifecycleHandler
import timber.log.Timber

class FragmentObserver(private val cmFragmentLifecycleHandler: CmFragmentLifecycleHandler) : FragmentLifecycleCallback {

    override fun onFragmentStart(fragment: Fragment) {
        try {
            cmFragmentLifecycleHandler.onFragmentStart(fragment)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override fun onFragmentResume(fragment: Fragment) {
        try {
            cmFragmentLifecycleHandler.onFragmentResume(fragment)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override fun onFragmentStop(fragment: Fragment) {
        try {
            cmFragmentLifecycleHandler.onFragmentStop(fragment)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override fun onFragmentSelected(fragment: Fragment) {
        try {
            cmFragmentLifecycleHandler.onFragmentSelected(fragment)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override fun onFragmentUnSelected(fragment: Fragment) {
        try {
            cmFragmentLifecycleHandler.onFragmentUnSelected(fragment)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override fun onFragmentDestroyed(fragment: Fragment) {
        try {
            cmFragmentLifecycleHandler.onFragmentDestroyed(fragment)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}