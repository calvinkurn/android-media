package com.tokopedia.abstraction.base.view.fragment.lifecycle

import androidx.fragment.app.Fragment

interface FragmentLifecycleCallback{
    fun onFragmentStart(fragment: Fragment)
    fun onFragmentResume(fragment: Fragment)
    fun onFragmentStop(fragment: Fragment)
    fun onFragmentSelected(fragment: Fragment)
    fun onFragmentUnSelected(fragment: Fragment)
    fun onFragmentDestroyed(fragment: Fragment)
}