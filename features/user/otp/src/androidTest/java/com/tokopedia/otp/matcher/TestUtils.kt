package com.tokopedia.otp.matcher

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import org.hamcrest.Matcher
import org.hamcrest.Matchers


object TestUtils {
    fun <VH : RecyclerView.ViewHolder> actionOnItemViewAtPosition(position: Int,
                                                                   @IdRes viewId: Int,
                                                                   viewAction: ViewAction): ViewAction {
        return ActionOnItemViewAtPositionViewAction<RecyclerView.ViewHolder>(position, viewId, viewAction)
    }

    fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {
        return RecyclerViewMatcher(recyclerViewId)
    }

    private class ActionOnItemViewAtPositionViewAction<VH : RecyclerView.ViewHolder>(private val position: Int,
                                                                                      @param:IdRes private val viewId: Int,
                                                                                      private val viewAction: ViewAction) : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return Matchers.allOf(isAssignableFrom(RecyclerView::class.java), isDisplayed())
        }

        override fun getDescription(): String {
            return ("actionOnItemAtPosition performing ViewAction: "
                    + viewAction.description
                    + " on item at position: "
                    + position)
        }

        override fun perform(uiController: UiController, view: View) {
            val recyclerView: RecyclerView = view as RecyclerView
            ScrollToPositionViewAction(position).perform(uiController, view)
            uiController.loopMainThreadUntilIdle()
            val targetView: View = recyclerView.getChildAt(position).findViewById(viewId)
            viewAction.perform(uiController, targetView)
        }
    }

    private class ScrollToPositionViewAction(private val position: Int) : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return Matchers.allOf(isAssignableFrom(RecyclerView::class.java), isDisplayed())
        }

        override fun getDescription(): String {
            return "scroll RecyclerView to position: $position"
        }

        override fun perform(uiController: UiController, view: View) {
            val recyclerView: RecyclerView = view as RecyclerView
            recyclerView.scrollToPosition(position)
        }
    }
}