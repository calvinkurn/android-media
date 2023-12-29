package com.tokopedia.tokopedianow.test.util

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import org.hamcrest.Matcher

object ViewIdlingResourceHelper {

    fun waitForViewDisplayed(matcher: Matcher<View>, idleMatcher: Matcher<View?> = isDisplayed()) {
        val idlingResource: IdlingResource = ViewIdlingResource(matcher, idleMatcher)
        try {
            IdlingRegistry.getInstance().register(idlingResource)
            Espresso.onView(matcher).check(ViewAssertions.matches(isDisplayed()))
        } finally {
            IdlingRegistry.getInstance().unregister(idlingResource)
        }
    }
}
