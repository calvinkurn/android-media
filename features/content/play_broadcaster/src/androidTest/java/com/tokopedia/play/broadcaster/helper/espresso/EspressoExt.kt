package com.tokopedia.play.broadcaster.helper

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
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

internal fun delay(delayInMillis: Long = 500) {
    Espresso.onView(ViewMatchers.isRoot()).perform(waitFor(delayInMillis))
}

internal fun clickOnViewChild(viewId: Int) = object : ViewAction {
    override fun getConstraints() = null

    override fun getDescription() = "Click on a child view with specified id."

    override fun perform(uiController: UiController, view: View) {
        click().perform(uiController, view.findViewById(viewId))
    }
}