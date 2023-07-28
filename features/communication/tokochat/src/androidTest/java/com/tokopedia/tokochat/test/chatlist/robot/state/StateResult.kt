package com.tokopedia.tokochat.test.chatlist.robot.state

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withSubstring
import com.tokopedia.tokochat.stub.common.matcher.withRecyclerView
import com.tokopedia.tokochat_common.R

object StateResult {
    fun assertEmptyState(position: Int) {
        onView(
            withRecyclerView(R.id.tokochat_list_rv)
                .atPositionOnView(position, R.id.tokochat_list_iv_empty_chat)
        ).check(matches(isDisplayed()))
    }

    fun assertGlobalErrorState() {
        onView(withSubstring("Ada gangguan di rumah Toped"))
            .check(matches(isDisplayed()))
        onView(withSubstring("Ke Homepage"))
            .check(matches(isDisplayed()))
    }

    fun assertNetworkErrorState() {
        onView(withSubstring("Koneksi internetmu terganggu!"))
            .check(matches(isDisplayed()))
        onView(withSubstring("Coba Lagi"))
            .check(matches(isDisplayed()))
    }
}
