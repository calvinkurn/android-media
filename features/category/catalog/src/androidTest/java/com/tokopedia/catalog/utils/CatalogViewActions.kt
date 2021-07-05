package com.tokopedia.catalog.utils

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import org.hamcrest.Matcher
import org.hamcrest.Matchers


object CatalogViewActions {
    /** Overwrites the constraints of the specified [ViewAction].  */
    fun withCustomConstraints(
            action: ViewAction, constraints: Matcher<View>): ViewAction {
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

    fun clickChildViewWithId(id: Int): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View>? {
                return null
            }

            override fun getDescription(): String {
                return "Click on a child view with specified id."
            }

            override fun perform(uiController: UiController?, view: View) {
                val v = view.findViewById<View>(id)
                v.performClick()
            }
        }
    }

    class ScrollToBottomAction : ViewAction {
        override fun getDescription(): String {
            return "scroll RecyclerView to bottom"
        }

        override fun getConstraints(): Matcher<View> {
            return Matchers.allOf<View>(ViewMatchers.isAssignableFrom(RecyclerView::class.java), ViewMatchers.isDisplayed())
        }

        override fun perform(uiController: UiController?, view: View?) {
            val recyclerView = view as RecyclerView
            val itemCount = recyclerView.adapter?.itemCount
            val position = itemCount?.minus(1) ?: 0
            recyclerView.scrollToPosition(position)
            uiController?.loopMainThreadUntilIdle()
        }
    }

//    fun validate(gtmLogDbSource: GtmLogDBSource,
//                 targetContext: Context,
//                 fileName: String) {
//        ViewMatchers.assertThat(getAnalyticsWithQuery(gtmLogDbSource, targetContext, fileName),
//                hasAllSuccess())
//    }

    fun setVisibility(visibility: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isEnabled()
            }

            override fun getDescription(): String {
                return "Set view visibility"
            }

            override fun perform(uiController: UiController, view: View) {
                uiController.loopMainThreadUntilIdle()
                view.visibility = visibility
                uiController.loopMainThreadUntilIdle()
            }
        }
    }
}