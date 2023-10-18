package com.tokopedia.contactus.utils

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import com.tokopedia.contactus.utils.ToasterUtils.waitOnView
import com.tokopedia.test.application.espresso_component.CommonMatcher
import com.tokopedia.test.application.matcher.RecyclerViewMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher


object InstrumentedTestUtil {

    fun <T : Activity> ActivityTestRule<T>.scrollRecyclerViewToPosition(
        recyclerView: RecyclerView,
        position: Int
    ) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        this.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
    }

    fun isShowToaster(text: String) {
        waitOnView(withText(text)).check(matches(ViewMatchers.isDisplayed()))
    }

    fun viewChildPerformClick(idMainView: Int, childPosition: Int) {
        onView(nthChildOf(withId(idMainView), childPosition)).perform(ViewActions.click())
    }

    private fun nthChildOf(parentMatcher: Matcher<View>, childPosition: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("with $childPosition child view of type parentMatcher")
            }

            override fun matchesSafely(view: View): Boolean {
                if (view.parent !is ViewGroup) {
                    return parentMatcher.matches(view.parent)
                }
                val group = view.parent as ViewGroup
                return parentMatcher.matches(view.parent) && group.getChildAt(childPosition) == view
            }
        }
    }

}
