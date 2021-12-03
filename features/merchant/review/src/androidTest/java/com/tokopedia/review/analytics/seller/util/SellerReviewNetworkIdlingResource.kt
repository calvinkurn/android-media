package com.tokopedia.review.analytics.seller.util

import androidx.test.espresso.IdlingResource

class SellerReviewNetworkIdlingResource(private val listener: SellerReviewIdlingInterface) :
    IdlingResource {

    override fun getName(): String = listener.getName()

    private var callback: IdlingResource.ResourceCallback? = null

    override fun isIdleNow(): Boolean {
        val isIdle = listener.idleState()
        if (isIdle) callback?.onTransitionToIdle()
        return isIdle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.callback = callback
    }
}

interface SellerReviewIdlingInterface {
    fun getName(): String
    fun idleState(): Boolean
}