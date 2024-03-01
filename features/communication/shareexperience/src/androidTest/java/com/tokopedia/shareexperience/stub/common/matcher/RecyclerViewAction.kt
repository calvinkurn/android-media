package com.tokopedia.shareexperience.stub.common.matcher

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher

fun smoothScrollTo(position: Int): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return allOf(isAssignableFrom(RecyclerView::class.java), isDisplayed())
        }

        override fun getDescription(): String {
            return "smooth scroll RecyclerView to position: $position"
        }

        override fun perform(uiController: UiController, view: View) {
            val recyclerView = view as RecyclerView
            recyclerView.smoothScrollToPosition(position)
            uiController.loopMainThreadUntilIdle()
            val targetView = recyclerView.layoutManager?.findViewByPosition(position)
            if (targetView != null) {
                uiController.loopMainThreadUntilIdle()
            }
        }
    }
}

fun waitForLayout(): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return isAssignableFrom(RecyclerView::class.java)
        }

        override fun getDescription(): String {
            return "wait for RecyclerView to complete layout"
        }

        override fun perform(uiController: UiController, view: View) {
            val recyclerView = view as RecyclerView
            if (recyclerView.isComputingLayout) {
                uiController.loopMainThreadUntilIdle()
            }
        }
    }
}
