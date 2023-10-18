package com.tokopedia.pdpCheckout.testing.oneclickcheckout.action

import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.*

fun scrollTo(): ViewAction = NestedScrollViewScrollTo()

fun swipeUpTop(): ViewAction = GeneralSwipeAction(
    Swipe.FAST,
    GeneralLocation.CENTER,
    CoordinatesProvider { floatArrayOf(0f, 0f) },
    Press.FINGER
)
