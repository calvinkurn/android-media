package com.tokopedia.topchat.matchers

import android.view.View
import androidx.test.espresso.matcher.BoundedMatcher
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.topchat.chatroom.view.custom.SrwLinearLayout
import org.hamcrest.Description

fun isExpanded(): BoundedMatcher<View, SrwLinearLayout> {
    return object : BoundedMatcher<View, SrwLinearLayout>(SrwLinearLayout::class.java) {
        override fun describeTo(description: Description?) {
            description?.appendText("expanded")
        }

        override fun matchesSafely(item: SrwLinearLayout?): Boolean {
            return item?.rvSrw?.isVisible == true && item.isExpanded
        }
    }
}