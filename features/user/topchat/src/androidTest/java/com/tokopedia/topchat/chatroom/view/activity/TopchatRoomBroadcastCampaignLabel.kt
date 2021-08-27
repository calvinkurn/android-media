package com.tokopedia.topchat.chatroom.view.activity

import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import org.hamcrest.CoreMatchers.not
import org.junit.Test

class TopchatRoomBroadcastCampaignLabel : TopchatRoomTest() {

    @Test
    fun should_show_starting_state_when_campaign_status_is_started() {
        // Given
        getChatUseCase.response = getChatUseCase.broadcastCampaignStarted
        launchChatRoomActivity()

        // Then
        assertBroadcastCampaignLabelAt(1, isDisplayed())
        assertBroadcastCampaignLabelDescAt(1, isDisplayed())
        assertBroadcastCampaignLabelDescAt(
            1, withText(
                R.string.desc_topchat_broadcast_campaign_started
            )
        )
        assertBroadcastCampaignLabelCountdownAt(1, not(isDisplayed()))
        assertBroadcastCampaignLabelStartDateIconAt(1, isDisplayed())
        assertBroadcastCampaignLabelStartDateTextAt(1, isDisplayed())
    }

    // TODO: should show ended state when campaign status is started
    // TODO: should show ended state when ongoing campaign finished countdown
    // TODO: should show ended state when end date is invalid or has passed

}