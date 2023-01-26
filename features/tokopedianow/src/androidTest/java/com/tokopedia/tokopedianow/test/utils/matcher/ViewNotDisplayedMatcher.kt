package com.tokopedia.tokopedianow.test.utils.matcher

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.Visibility
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

internal class ViewNotDisplayedMatcher: TypeSafeMatcher<View>() {
    override fun describeTo(description: Description) { /* nothing to do */ }

    override fun matchesSafely(item: View): Boolean = withEffectiveVisibility(Visibility.GONE).matches(item)
}
