package com.tokopedia.otp.common.action

import android.view.View
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.util.TreeIterables
import org.hamcrest.Matcher

object RootViewAction {

    fun searchFor(matcher: Matcher<View>): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isRoot()
            }

            override fun getDescription(): String {
                return "Searching for view $matcher in the root view"
            }

            override fun perform(uiController: UiController, view: View) {
                var tries = 0
                val childViews: Iterable<View> = TreeIterables.breadthFirstViewTraversal(view)
                childViews.forEach {
                    tries++
                    if (matcher.matches(it)) {
                        return
                    }
                }
                throw NoMatchingViewException.Builder()
                        .withRootView(view)
                        .withViewMatcher(matcher)
                        .build()
            }
        }
    }
}