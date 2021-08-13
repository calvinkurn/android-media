package com.tokopedia.promotionstarget.cm.lifecycle

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.lifecycle.FragmentLifecycleCallback

import com.tokopedia.promotionstarget.domain.presenter.GratifCancellationExceptionType

class GratifFragmentLifeCycleCallback(val cmActivityLiefcycleContract: CmActivityLiefcycleContract) : FragmentLifecycleCallback {
    override fun onFragmentStart(fragment: Fragment) {
        cancelJob(fragment.hashCode(), GratifCancellationExceptionType.FRAGMENT_STARTED)
    }

    override fun onFragmentResume(fragment: Fragment) {
        cancelJob(fragment.hashCode(), GratifCancellationExceptionType.FRAGMENT_RESUME)
    }

    override fun onFragmentStop(fragment: Fragment) {
        cancelJob(fragment.hashCode(), GratifCancellationExceptionType.FRAGMENT_STOP)
    }

    override fun onFragmentSelected(fragment: Fragment) {
        cancelJob(fragment.hashCode(), GratifCancellationExceptionType.FRAGMENT_SELECTED)
    }

    override fun onFragmentUnSelected(fragment: Fragment) {
        cancelJob(fragment.hashCode(), GratifCancellationExceptionType.FRAGMENT_UNSELECTED)
    }

    override fun onFragmentDestroyed(fragment: Fragment) {
        cancelJob(fragment.hashCode(), GratifCancellationExceptionType.FRAGMENT_DESTROYED)
    }

    fun cancelJob(entityHashCode: Int, @GratifCancellationExceptionType reason: String?) {
        cmActivityLiefcycleContract.cancelJob(entityHashCode, reason)
    }
}
