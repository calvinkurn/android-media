package com.tokopedia.notifications.inApp

import androidx.fragment.app.Fragment

class CmFragmentLifecycleHandler(private val callback: ShowInAppCallback,
                                 private val pushIntentHandler: PushIntentHandler) {

    fun onFragmentStart(fragment: Fragment) {
        getFragmentCallbacks().forEach { it.onFragmentStart(fragment) }
        showInApp(fragment.javaClass.name, fragment.hashCode())
    }

    fun onFragmentResume(fragment: Fragment) {
        getFragmentCallbacks().forEach { it.onFragmentResume(fragment) }
        showInApp(fragment.javaClass.name, fragment.hashCode())
    }

    fun onFragmentStop(fragment: Fragment) {
        getFragmentCallbacks().forEach { it.onFragmentStop(fragment) }
    }

    fun onFragmentUnSelected(fragment: Fragment) {
        getFragmentCallbacks().forEach { it.onFragmentUnSelected(fragment) }
    }

    fun onFragmentDestroyed(fragment: Fragment) {
        getFragmentCallbacks().forEach { it.onFragmentDestroyed(fragment) }
    }

    fun onFragmentSelected(fragment: Fragment) {
        getFragmentCallbacks().forEach { it.onFragmentSelected(fragment) }
        showInApp(fragment.javaClass.name, fragment.hashCode())
    }

    private fun showInApp(name: String, entityHashCode: Int) {
        if (!pushIntentHandler.isHandledByPush && callback.canShowDialog()) {
            callback.showInAppForScreen(name, entityHashCode, false)
        }
    }

    private fun getFragmentCallbacks() = CmEventListener.fragmentLifecycleCallbackList
}

interface ShowInAppCallback {
    fun showInAppForScreen(name: String, entityHashCode: Int, isActivity:Boolean)
    fun canShowDialog(): Boolean
}