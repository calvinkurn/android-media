package com.tokopedia.play.broadcaster.helper

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher

/**
 * Created by kenny.hadisaputra on 02/03/22
 */
internal fun waitFor(delay: Long): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> = ViewMatchers.isRoot()
        override fun getDescription(): String = "wait for $delay milliseconds"
        override fun perform(uiController: UiController, v: View?) {
            uiController.loopMainThreadForAtLeast(delay)
        }
    }
}