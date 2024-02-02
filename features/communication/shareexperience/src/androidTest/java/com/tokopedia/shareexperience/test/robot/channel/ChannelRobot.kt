package com.tokopedia.shareexperience.test.robot.channel

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.shareexperience.R
import com.tokopedia.shareexperience.stub.common.matcher.smoothScrollTo
import com.tokopedia.shareexperience.stub.common.matcher.waitForLayout
import com.tokopedia.shareexperience.stub.common.matcher.withRecyclerView

object ChannelRobot {
    fun clickSocialChannelOn(position: Int) {
        onView(
            withRecyclerView(R.id.shareex_rv_channel_social)
                .atPositionOnView(position, R.id.shareex_icon_channel)
        ).perform(click())
    }

    fun clickCommonChannelOn(position: Int) {
        onView(
            withRecyclerView(R.id.shareex_rv_channel_common)
                .atPositionOnView(position, R.id.shareex_icon_channel)
        ).perform(click())
    }

    fun scrollSocialToPosition(position: Int) {
        onView(withId(R.id.shareex_rv_channel_social))
            .perform(waitForLayout())
            .perform(smoothScrollTo(position))
        Thread.sleep(300)
    }
}
