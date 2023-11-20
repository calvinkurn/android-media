package com.tokopedia.topchat.chatroom.view.activity.test

import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.FlakyTest
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.broadcastResult
import org.hamcrest.CoreMatchers.not
import org.junit.Test
import java.util.concurrent.TimeUnit

@UiTest
class TopchatRoomBroadcastCampaignLabel : TopchatRoomTest() {

    // TODO: verify label background color

    @Test
    fun should_show_starting_state_when_campaign_status_is_started() {
        // Given
        val bannerAttachmentId = getChatUseCase.getBannerAttachmentId(
            getChatUseCase.defaultBroadcastCampaignLabel
        )
        getChatUseCase.response = getChatUseCase.defaultBroadcastCampaignLabel
        chatAttachmentUseCase.response = chatAttachmentUseCase.createBroadcastCampaignStarted(
            bannerAttachmentId
        )
        val labelDesc = chatAttachmentUseCase.getCampaignLabel(
            chatAttachmentUseCase.response
        )
        launchChatRoomActivity()

        // Then
        broadcastResult {
            assertBroadcastCampaignLabelAt(1, isDisplayed())
            assertBroadcastCampaignLabelDescAt(1, isDisplayed())
            assertBroadcastCampaignLabelDescAt(
                1, withText(labelDesc)
            )
            assertBroadcastCampaignLabelCountdownAt(1, not(isDisplayed()))
            assertBroadcastCampaignLabelStartDateIconAt(1, isDisplayed())
            assertBroadcastCampaignLabelStartDateTextAt(1, isDisplayed())
        }
    }

    @Test
    fun should_show_ongoing_state_when_campaign_status_is_ongoing() {
        // Given
        val bannerAttachmentId = getChatUseCase.getBannerAttachmentId(
            getChatUseCase.defaultBroadcastCampaignLabel
        )
        getChatUseCase.response = getChatUseCase.defaultBroadcastCampaignLabel
        chatAttachmentUseCase.response = chatAttachmentUseCase.createBroadcastCampaignOnGoing(
            bannerAttachmentId
        )
        val labelDesc = chatAttachmentUseCase.getCampaignLabel(
            chatAttachmentUseCase.response
        )
        launchChatRoomActivity()

        // Then
        broadcastResult {
            assertBroadcastCampaignLabelAt(1, isDisplayed())
            assertBroadcastCampaignLabelDescAt(1, isDisplayed())
            assertBroadcastCampaignLabelDescAt(1, withText(labelDesc))
            assertBroadcastCampaignLabelCountdownAt(1, isDisplayed())
            assertBroadcastCampaignLabelStartDateIconAt(1, not(isDisplayed()))
            assertBroadcastCampaignLabelStartDateTextAt(1, not(isDisplayed()))
        }
    }

    @Test
    @FlakyTest
    fun should_show_ended_state_when_ongoing_campaign_finished_countdown() {
        // Given
        val bannerAttachmentId = getChatUseCase.getBannerAttachmentId(
            getChatUseCase.defaultBroadcastCampaignLabel
        )
        getChatUseCase.response = getChatUseCase.defaultBroadcastCampaignLabel
        chatAttachmentUseCase.response = chatAttachmentUseCase.createBroadcastCampaignAboutToEnd(
            bannerAttachmentId
        )
        val endWording = chatAttachmentUseCase.getEndWordingBannerFrom(
            chatAttachmentUseCase.response
        )
        launchChatRoomActivity()

        // When
        // Wait for countdown to finish
        val waitTime = TimeUnit.SECONDS.toMillis(4)
        Thread.sleep(waitTime)

        // Then
        broadcastResult {
            assertBroadcastCampaignLabelAt(1, isDisplayed())
            assertBroadcastCampaignLabelDescAt(1, isDisplayed())
            assertBroadcastCampaignLabelDescAt(1, withText(endWording))
            assertBroadcastCampaignLabelCountdownAt(1, not(isDisplayed()))
            assertBroadcastCampaignLabelStartDateIconAt(1, not(isDisplayed()))
            assertBroadcastCampaignLabelStartDateTextAt(1, not(isDisplayed()))
        }
    }

    @Test
    fun should_show_ended_state_when_campaign_status_is_ended() {
        // Given
        val bannerAttachmentId = getChatUseCase.getBannerAttachmentId(
            getChatUseCase.defaultBroadcastCampaignLabel
        )
        getChatUseCase.response = getChatUseCase.defaultBroadcastCampaignLabel
        chatAttachmentUseCase.response = chatAttachmentUseCase.createBroadcastCampaignEnded(
            bannerAttachmentId
        )
        val endWording = chatAttachmentUseCase.getEndWordingBannerFrom(
            chatAttachmentUseCase.response
        )
        launchChatRoomActivity()

        // Then
        broadcastResult {
            assertBroadcastCampaignLabelAt(1, isDisplayed())
            assertBroadcastCampaignLabelDescAt(1, isDisplayed())
            assertBroadcastCampaignLabelDescAt(1, withText(endWording))
            assertBroadcastCampaignLabelCountdownAt(1, not(isDisplayed()))
            assertBroadcastCampaignLabelStartDateIconAt(1, not(isDisplayed()))
            assertBroadcastCampaignLabelStartDateTextAt(1, not(isDisplayed()))
        }
    }

    @Test
    fun should_hide_campaign_label_when_broadcast_does_not_have_campaign() {
        // Given
        val bannerAttachmentId = getChatUseCase.getBannerAttachmentId(
            getChatUseCase.defaultBroadcastCampaignLabel
        )
        getChatUseCase.response = getChatUseCase.defaultBroadcastCampaignLabel
        chatAttachmentUseCase.response = chatAttachmentUseCase.createBroadcastNoCampaign(
            bannerAttachmentId
        )
        launchChatRoomActivity()

        // Then
        broadcastResult {
            assertBroadcastCampaignLabelAt(1, not(isDisplayed()))
        }
    }
}
