package com.tokopedia.shop.analyticvalidator.util

import android.view.View
import androidx.test.espresso.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.google.android.material.tabs.TabLayout
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf


object ViewActionUtil {
    fun waitUntilViewIsDisplayed(matcher: Matcher<View?>) {
        val idlingResource: IdlingResource = ViewIdlingResource(matcher, isDisplayed())
        try {
            IdlingRegistry.getInstance().register(idlingResource)
            // First call to onView is to trigger the idler.
            onView(withId(0)).check(doesNotExist())
        } finally {
            IdlingRegistry.getInstance().unregister(idlingResource)
        }
    }

    fun clickTabLayoutPosition(tabIndex: Int): ViewAction {
        return object : ViewAction {
            override fun getDescription(): String {
                return "with tab at index $tabIndex"
            }

            override fun getConstraints(): Matcher<View> {
                return AllOf.allOf(isDisplayed(), ViewMatchers.isAssignableFrom(TabLayout::class.java))
            }

            override fun perform(uiController: UiController, view: View) {
                val tabLayout = view as TabLayout
                val tabAtIndex: TabLayout.Tab = tabLayout.getTabAt(tabIndex)
                        ?: throw PerformException.Builder()
                                .withCause(Throwable("No tab at index $tabIndex"))
                                .build()
                tabAtIndex.view.performClick()
            }
        }
    }
}
