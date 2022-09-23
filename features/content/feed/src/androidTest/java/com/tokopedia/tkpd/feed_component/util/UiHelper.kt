package com.tokopedia.tkpd.feed_component.util

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.tkpd.feed_component.robot.FeedPlusContainerRobot
import org.hamcrest.CoreMatchers.`is`

/**
 * Created By : Jonathan Darwin on September 23, 2022
 */

fun select(@IdRes id: Int): ViewInteraction {
    return onView(withId(id))
}

fun select(text: String): ViewInteraction {
    return onView(withText(text))
}

fun selectTag(tag: String): ViewInteraction {
    return onView(withTagValue(`is`(tag)))
}
