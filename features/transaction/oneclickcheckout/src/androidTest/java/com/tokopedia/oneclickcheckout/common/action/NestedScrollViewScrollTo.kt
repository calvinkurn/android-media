package com.tokopedia.oneclickcheckout.common.action

import android.view.View
import android.widget.HorizontalScrollView
import android.widget.ListView
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher

// Duplicate Class For ScrollToViewAction with support for NestedScrollView descendants
class NestedScrollViewScrollTo(scrollToAction: ViewAction = ViewActions.scrollTo()) :
    ViewAction by scrollToAction {

    override fun getConstraints(): Matcher<View> {
        return CoreMatchers.allOf(
            ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
            ViewMatchers.isDescendantOfA(
                CoreMatchers.anyOf(
                    ViewMatchers.isAssignableFrom(NestedScrollView::class.java),
                    ViewMatchers.isAssignableFrom(ScrollView::class.java),
                    ViewMatchers.isAssignableFrom(HorizontalScrollView::class.java),
                    ViewMatchers.isAssignableFrom(ListView::class.java)
                )
            )
        )
    }
}
