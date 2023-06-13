package com.tokopedia.tokochat.test.robot.header_date

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.tokochat.stub.common.matcher.withRecyclerView
import com.tokopedia.tokochat_common.R

object HeaderDateResult {

    fun assertHeaderDateVisibility(position: Int, isVisible: Boolean) {
        val matcher = if (isVisible) {
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        } else {
            ViewAssertions.doesNotExist()
        }
        Espresso.onView(
            withRecyclerView(R.id.tokochat_chatroom_rv)
                .atPositionOnView(position, R.id.tokochat_tv_header_date)
        ).check(matcher)
    }
}
