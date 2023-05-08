package com.tokopedia.oneclickcheckout.common.action

import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.CoordinatesProvider
import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.action.GeneralSwipeAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Swipe

fun scrollTo(): ViewAction = NestedScrollViewScrollTo()

fun swipeUpTop(): ViewAction = GeneralSwipeAction(
    Swipe.FAST,
    GeneralLocation.CENTER,
    CoordinatesProvider { floatArrayOf(0f, 0f) },
    Press.FINGER
)
