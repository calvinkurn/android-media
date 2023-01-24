package com.tokopedia.tokochat.test.robot.ticker

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.tokopedia.tokochat.stub.common.matcher.withRecyclerView

object TickerResult {

    fun assertTickerVisibility(position: Int, isVisible: Boolean) {
        val matcher = if (isVisible) {
            matches(isDisplayed())
        } else {
            doesNotExist()
        }
        onView(
            withRecyclerView(com.tokopedia.tokochat_common.R.id.tokochat_chatroom_rv)
                .atPositionOnView(position, com.tokopedia.tokochat_common.R.id.tokochat_tk_prompt)
        ).check(matcher)
    }
}
