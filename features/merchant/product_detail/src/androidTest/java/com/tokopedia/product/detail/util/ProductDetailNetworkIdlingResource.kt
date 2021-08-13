package com.tokopedia.product.detail.util

import androidx.test.espresso.IdlingResource

/**
 * Created by Yehezkiel on 20/04/21
 */

class ProductDetailNetworkIdlingResource(private val listener: ProductIdlingInterface) : IdlingResource {

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

interface ProductIdlingInterface {
    fun getName(): String
    fun idleState(): Boolean
}