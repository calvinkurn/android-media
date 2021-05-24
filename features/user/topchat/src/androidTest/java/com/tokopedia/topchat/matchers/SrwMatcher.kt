package com.tokopedia.topchat.matchers

import android.view.View
import androidx.test.espresso.matcher.BoundedMatcher
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.topchat.chatroom.view.custom.SrwFrameLayout
import org.hamcrest.Description

fun isExpanded(): BoundedMatcher<View, SrwFrameLayout> {
    return object : BoundedMatcher<View, SrwFrameLayout>(SrwFrameLayout::class.java) {
        override fun describeTo(description: Description?) {
            description?.appendText("expanded")
        }

        override fun matchesSafely(item: SrwFrameLayout?): Boolean {
            return item?.rvSrw?.isVisible == true && item.isExpanded
        }
    }
}