package com.tokopedia.search.utils

import android.view.View
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

private class AnyViewMatcher: BaseMatcher<View>() {
    override fun describeTo(description: Description?) {
        description?.appendText("is anything")
    }

    override fun matches(item: Any?): Boolean {
        return true
    }
}

fun isAnyView(): Matcher<View> = AnyViewMatcher()
