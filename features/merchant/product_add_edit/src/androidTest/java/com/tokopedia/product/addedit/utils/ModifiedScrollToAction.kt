package com.tokopedia.product.addedit.utils

import android.view.View
import android.widget.HorizontalScrollView
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ScrollToAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher

/*
this class is modified version of ScrollToAction
it is enable matcher to interact with ScrollView, HorizontalScrollView, NestedScrollView
*/

class ModifiedScrollToAction: ViewAction by ScrollToAction() {
    override fun getConstraints(): Matcher<View> {
        return CoreMatchers.allOf(
            ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
            ViewMatchers.isDescendantOfA(
                CoreMatchers.anyOf(
                    ViewMatchers.isAssignableFrom(ScrollView::class.java),
                    ViewMatchers.isAssignableFrom(HorizontalScrollView::class.java),
                    ViewMatchers.isAssignableFrom(NestedScrollView::class.java)
                )
            )
        )
    }
}
