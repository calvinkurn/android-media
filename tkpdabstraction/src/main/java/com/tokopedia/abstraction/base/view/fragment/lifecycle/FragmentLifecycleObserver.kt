package com.tokopedia.abstraction.base.view.fragment.lifecycle

import androidx.fragment.app.Fragment

object FragmentLifecycleObserver {

    private val callbacks = arrayListOf<FragmentLifecycleCallback>()

    fun onFragmentStart(fragment: Fragment) {
        for (item in callbacks) {
            item.onFragmentStart(fragment)
        }
    }

    fun onFragmentResume(fragment: Fragment) {
        for (item in callbacks) {
            item.onFragmentResume(fragment)
        }
    }

    fun onFragmentStop(fragment: Fragment) {
        for (item in callbacks) {
            item.onFragmentStop(fragment)
        }
    }

    fun onFragmentSelected(fragment: Fragment) {
        for (item in callbacks) {
            item.onFragmentSelected(fragment)
        }
    }

    fun onFragmentUnSelected(fragment: Fragment) {
        for (item in callbacks) {
            item.onFragmentUnSelected(fragment)
        }
    }

    fun registerCallback(fragmentLifecycleCallback: FragmentLifecycleCallback) {
        callbacks.add(fragmentLifecycleCallback)
    }

    fun unRegisterCallback(fragmentLifecycleCallback: FragmentLifecycleCallback) {
        callbacks.remove(fragmentLifecycleCallback)
    }

}