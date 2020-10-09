package com.tokopedia.notifications.inApp

import androidx.fragment.app.Fragment
import com.tokopedia.promotionstarget.domain.presenter.GratifCancellationExceptionType

class CmFragmentLifecycleHandler(private val callback: ShowInAppCallback) {

    fun onFragmentStart(fragment: Fragment) {
        cancelJob(fragment.hashCode(), GratifCancellationExceptionType.FRAGMENT_STARTED)
        showInApp(fragment.javaClass.name, fragment.hashCode())
    }

    fun onFragmentResume(fragment: Fragment) {
        cancelJob(fragment.hashCode(), GratifCancellationExceptionType.FRAGMENT_RESUME)
        showInApp(fragment.javaClass.name, fragment.hashCode())
    }

    fun onFragmentStop(fragment: Fragment) {
        cancelJob(fragment.hashCode(), GratifCancellationExceptionType.FRAGMENT_STOP)
    }

    fun onFragmentUnSelected(fragment: Fragment) {
        cancelJob(fragment.hashCode(), GratifCancellationExceptionType.FRAGMENT_UNSELECTED)
    }

    fun onFragmentDestroyed(fragment: Fragment) {
        cancelJob(fragment.hashCode(), GratifCancellationExceptionType.FRAGMENT_DESTROYED)
    }

    fun onFragmentSelected(fragment: Fragment) {
        cancelJob(fragment.hashCode(), GratifCancellationExceptionType.FRAGMENT_SELECTED)
        showInApp(fragment.javaClass.name, fragment.hashCode())
    }

    private fun cancelJob(entityHashCode: Int, @GratifCancellationExceptionType reason: String) {
        callback.cancelGratifJob(entityHashCode, reason)
    }

    private fun showInApp(name: String, entityHashCode: Int) {
        if (callback.canShowDialog()) {
            callback.showInAppForScreen(name, entityHashCode)
        }
    }
}

interface ShowInAppCallback {
    fun showInAppForScreen(name: String, entityHashCode: Int)
    fun cancelGratifJob(entityHashCode: Int, @GratifCancellationExceptionType reason: String?)
    fun canShowDialog():Boolean
}