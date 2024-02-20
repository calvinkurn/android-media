package com.tokopedia.inbox.universalinbox.stub.common

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher

fun withCustomConstraints(action: ViewAction, constraints: Matcher<View>): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return constraints
        }

        override fun getDescription(): String {
            return action.description
        }

        override fun perform(uiController: UiController?, view: View?) {
            action.perform(uiController, view)
        }
    }
}

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

fun smoothScrollTo(position: Int, offset: Int): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return allOf(isAssignableFrom(RecyclerView::class.java), isDisplayed())
        }

        override fun getDescription(): String {
            return "smooth scroll RecyclerView to position: $position"
        }

        override fun perform(uiController: UiController, view: View) {
            val recyclerView = view as RecyclerView
            val layoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager
            if (offset > 0) {
                layoutManager.scrollToPositionWithOffset(position, offset)
            } else {
                recyclerView.smoothScrollToPosition(position)
            }
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
