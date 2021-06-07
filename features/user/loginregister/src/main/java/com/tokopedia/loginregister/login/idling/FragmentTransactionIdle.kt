package com.tokopedia.loginregister.login.idling

import androidx.fragment.app.FragmentManager
import androidx.test.espresso.IdlingResource

class FragmentTransactionIdle(
        private val supportFragmentManager: FragmentManager,
        private val tag: String
) : IdlingResource {

    private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName(): String {
        return "Wait fragment transaction to complete commit"
    }

    override fun isIdleNow(): Boolean {
        val fragment = supportFragmentManager.findFragmentByTag(tag)
        val isIdle = fragment != null && fragment.isResumed
        if (isIdle) {
            resourceCallback?.onTransitionToIdle()
        }
        return isIdle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.resourceCallback = callback
    }
}