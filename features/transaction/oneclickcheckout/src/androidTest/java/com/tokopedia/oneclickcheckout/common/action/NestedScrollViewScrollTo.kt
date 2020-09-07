package com.tokopedia.oneclickcheckout.common.action

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ScrollToAction
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.util.HumanReadables
import org.hamcrest.Matcher
import org.hamcrest.Matchers

/*
 * Duplicate Class For ScrollToViewAction specific for NestedScrollView descendants
 */
class NestedScrollViewScrollTo : ViewAction {
    private val TAG = ScrollToAction::class.java.simpleName

    override fun getDescription(): String {
        return "scroll to for nested scroll view"
    }

    override fun getConstraints(): Matcher<View> {
        return Matchers.allOf(
                ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                ViewMatchers.isDescendantOfA(Matchers.allOf(ViewMatchers.isAssignableFrom(NestedScrollView::class.java)))
        )
    }

    override fun perform(uiController: UiController, view: View) {
        if (ViewMatchers.isDisplayingAtLeast(100).matches(view)) {
            Log.i(TAG, "View is already displayed. Returning.")
            return
        }
        val rect = Rect()
        view.getDrawingRect(rect)
        if (!view.requestRectangleOnScreen(rect, true)) {
            Log.w(TAG, "Scrolling to view was requested, but none of the parents scrolled.")
        }
        uiController.loopMainThreadUntilIdle()
        if (!ViewMatchers.isDisplayingAtLeast(100).matches(view)) {
            throw PerformException.Builder()
                    .withActionDescription(this.description)
                    .withViewDescription(HumanReadables.describe(view))
                    .withCause(RuntimeException("Scrolling to view was attempted, but the view is not displayed"))
                    .build()
        }
    }
}