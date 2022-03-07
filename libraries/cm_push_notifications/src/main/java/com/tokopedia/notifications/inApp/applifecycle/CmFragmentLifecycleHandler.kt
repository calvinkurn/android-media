package com.tokopedia.notifications.inApp.applifecycle

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.lifecycle.FragmentLifecycleCallback
import com.tokopedia.notifications.inApp.external.CmEventListener
import com.tokopedia.notifications.inApp.external.PushIntentHandler

class CmFragmentLifecycleHandler(private val callback: ShowInAppCallback,
                                 private val pushIntentHandler: PushIntentHandler)
    : FragmentLifecycleCallback {

    override fun onFragmentStart(fragment: Fragment) {
        runCatching {
            getFragmentCallbacks().forEach { it.onFragmentStart(fragment) }
            showInApp(fragment.javaClass.name, fragment.hashCode())
        }
    }

    override fun onFragmentResume(fragment: Fragment) {
        runCatching {
            getFragmentCallbacks().forEach { it.onFragmentResume(fragment) }
            showInApp(fragment.javaClass.name, fragment.hashCode())
        }
    }

    override fun onFragmentStop(fragment: Fragment) {
        runCatching {
            getFragmentCallbacks().forEach { it.onFragmentStop(fragment) }
        }
    }

    override fun onFragmentUnSelected(fragment: Fragment) {
        runCatching {
            getFragmentCallbacks().forEach { it.onFragmentUnSelected(fragment) }
        }
    }

    override fun onFragmentDestroyed(fragment: Fragment) {
        runCatching {
            getFragmentCallbacks().forEach { it.onFragmentDestroyed(fragment) }
        }
    }

    override fun onFragmentSelected(fragment: Fragment) {
        runCatching {
            getFragmentCallbacks().forEach { it.onFragmentSelected(fragment) }
            showInApp(fragment.javaClass.name, fragment.hashCode())
        }
    }

    private fun showInApp(name: String, entityHashCode: Int) {
        runCatching {
            if (!pushIntentHandler.isHandledByPush && callback.canShowDialog()) {
                callback.showInAppForScreen(name, entityHashCode, false)
            }
        }
    }

    private fun getFragmentCallbacks() = CmEventListener.fragmentLifecycleCallbackList
}

interface ShowInAppCallback {
    fun showInAppForScreen(name: String, entityHashCode: Int, isActivity: Boolean)
    fun canShowDialog(): Boolean
}