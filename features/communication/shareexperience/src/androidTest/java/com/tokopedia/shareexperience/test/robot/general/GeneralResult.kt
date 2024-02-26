package com.tokopedia.shareexperience.test.robot.general

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withSubstring
import com.tokopedia.shareexperience.R
import com.tokopedia.shareexperience.stub.common.matcher.withTotalItem

object GeneralResult {
    fun assertRvTotalItem(total: Int) {
        onView(withId(R.id.shareex_rv_bottom_sheet)).check(
            matches(withTotalItem(total))
        )
    }

    fun assertToaster(msg: String) {
        onView(withSubstring(msg)).check(
            matches(isDisplayed())
        )
    }
}
