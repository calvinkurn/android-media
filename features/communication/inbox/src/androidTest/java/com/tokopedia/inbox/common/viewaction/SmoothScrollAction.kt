package com.tokopedia.inbox.common.viewaction

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher

fun smoothScrollTo(position: Int): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return ViewMatchers.isDisplayingAtLeast(90)
        }

        override fun getDescription(): String {
            return "smooth scroll order widget"
        }

        override fun perform(uiController: UiController?, view: View?) {
            (view as? RecyclerView)?.smoothScrollToPosition(position)
            uiController?.loopMainThreadForAtLeast(500)
        }

    }
}