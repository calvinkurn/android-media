package com.tokopedia.pdpCheckout.testing.cart.robot

import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.cart.view.CartActivity

open class CartPageIntentTestRule(initialTouchMode: Boolean, launchActivity: Boolean) :
        IntentsTestRule<CartActivity>(CartActivity::class.java, initialTouchMode, launchActivity)