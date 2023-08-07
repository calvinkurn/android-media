package com.tokopedia.tokochat.test.chatlist.robot.general

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withSubstring
import com.tokopedia.tokochat.stub.common.matcher.withItemCount
import com.tokopedia.tokochat.stub.common.matcher.withRecyclerView
import com.tokopedia.tokochat_common.R
import org.hamcrest.CoreMatchers.equalTo

object GeneralResult {

    fun assertPageTitle(title: String) {
        onView(withSubstring(title))
            .check(matches(isDisplayed()))
    }

    fun assertDriverName(position: Int, name: String) {
        onView(
            withRecyclerView(R.id.tokochat_list_rv)
                .atPositionOnView(position, R.id.tokochat_list_tv_driver_name)
        ).check(matches(withSubstring(name)))
    }

    fun assertDriverImageProfile(position: Int) {
        onView(
            withRecyclerView(R.id.tokochat_list_rv)
                .atPositionOnView(position, R.id.tokochat_list_iv_driver)
        ).check(matches(isDisplayed()))
    }

    fun assertDriverBadge(position: Int) {
        onView(
            withRecyclerView(R.id.tokochat_list_rv)
                .atPositionOnView(position, R.id.tokochat_list_iv_logo)
        ).check(matches(isDisplayed()))
    }

    fun assertDriverTypeOrder(position: Int, order: String) {
        onView(
            withRecyclerView(R.id.tokochat_list_rv)
                .atPositionOnView(position, R.id.tokochat_list_tv_order_type)
        ).check(matches(withSubstring(order)))
    }

    fun assertThumbnailMessage(position: Int, message: String) {
        onView(
            withRecyclerView(R.id.tokochat_list_rv)
                .atPositionOnView(position, R.id.tokochat_list_tv_message)
        ).check(matches(withSubstring(message)))
    }

    fun assertBusinessName(position: Int, business: String) {
        onView(
            withRecyclerView(R.id.tokochat_list_rv)
                .atPositionOnView(position, R.id.tokochat_list_tv_business_name)
        ).check(matches(withSubstring(business)))
    }

    fun assertTimeStamp(position: Int, timeStamp: String) {
        onView(
            withRecyclerView(R.id.tokochat_list_rv)
                .atPositionOnView(position, R.id.tokochat_list_tv_time)
        ).check(matches(withSubstring(timeStamp)))
    }

    fun assertNoTimeStamp(position: Int) {
        onView(
            withRecyclerView(R.id.tokochat_list_rv)
                .atPositionOnView(position, R.id.tokochat_list_tv_time)
        ).check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }

    fun assertCounter(position: Int, counter: String) {
        onView(
            withRecyclerView(R.id.tokochat_list_rv)
                .atPositionOnView(position, R.id.tokochat_list_counter)
        ).check(matches(withSubstring(counter)))
    }

    fun assertNoCounter(position: Int) {
        onView(
            withRecyclerView(R.id.tokochat_list_rv)
                .atPositionOnView(position, R.id.tokochat_list_counter)
        ).check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)))
    }

    fun assertChatListItemTotal(count: Int) {
        onView(
            withId(R.id.tokochat_list_rv)
        ).check(withItemCount(equalTo(count)))
    }
}
