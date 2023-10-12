package com.tokopedia.inbox.universalinbox.test.robot.general

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.inbox.R
import com.tokopedia.inbox.universalinbox.stub.common.withTotalItem

object GeneralResult {
    fun assertInboxRvTotalItem(total: Int) {
        Espresso.onView(ViewMatchers.withId(R.id.inbox_rv)).check(
            matches(withTotalItem(total))
        )
    }
}
