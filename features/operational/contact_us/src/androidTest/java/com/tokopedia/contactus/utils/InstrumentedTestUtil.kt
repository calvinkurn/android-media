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

    fun performClick(id: Int) {
        onView(
            CommonMatcher
                .firstView(withId(id))
        )
            .perform(ViewActions.click())
    }

    fun <T : Activity> ActivityTestRule<T>.scrollRecyclerViewToPosition(
        recyclerView: RecyclerView,
        position: Int
    ) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        this.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
    }

    fun RecyclerView.clickOnPosition(position: Int) {
        onView(withId(this.id))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    position, ViewActions.click()
                )
            )
    }

    fun isTextDisplayed(vararg texts: String) {
        texts.forEach {
            onView(withText(it))
                .check(matches(ViewMatchers.isDisplayed()))
        }
    }

    fun isShowToaster(text: String) {
        waitOnView(withText(text)).check(matches(ViewMatchers.isDisplayed()))
    }

    fun isViewChildIsShow(idMainView: Int, childPosition: Int) {
        onView(
            nthChildOf(
                withId(idMainView),
                childPosition
            )
        ).check(matches(ViewMatchers.isDisplayed()))
    }

    fun viewChildPerformClick(idMainView: Int, childPosition: Int) {
        onView(nthChildOf(withId(idMainView), childPosition)).perform(ViewActions.click())
    }

    fun isGoneView(resId: Int) {
        onView(withId(resId))
            .check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }

    fun isVisible(resId: Int) {
        onView(withId(resId)).check(matches(ViewMatchers.isDisplayed()))
    }

    fun RecyclerView.isViewVisibleInItemPosition(position: Int, viewToCheck: Int) {
        onView(
            RecyclerViewMatcher(this.id)
                .atPositionOnView(position, viewToCheck)
        ).check(matches(ViewMatchers.isDisplayed()))
    }

    fun RecyclerView.isTextShowedInItemPosition(position: Int, viewToCheck: Int, text: String) {
        onView(RecyclerViewMatcher(this.id).atPositionOnView(position, viewToCheck)).check(matches(
            withText(text)
        ))
    }

    fun RecyclerView.isViewGoneInItemPosition(position: Int, viewToCheck: Int) {
        onView(
            RecyclerViewMatcher(this.id)
                .atPositionOnView(position, viewToCheck)
        ).check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
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
