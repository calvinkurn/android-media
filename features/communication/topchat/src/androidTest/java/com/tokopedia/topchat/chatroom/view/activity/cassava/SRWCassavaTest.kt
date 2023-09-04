package com.tokopedia.topchat.chatroom.view.activity.cassava

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.topchat.chatroom.view.activity.base.BaseBuyerTopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.srw.SrwRobot.dismissSrwOnboarding
import com.tokopedia.topchat.chatroom.view.custom.SrwFrameLayout
import org.junit.Rule
import org.junit.Test

@CassavaTest
class SRWCassavaTest : BaseBuyerTopchatRoomTest() {

    @get:Rule
    var cassavaTestRule = CassavaTestRule(true, true)

    private val DEFAULT_PRODUCT_ID = "1111"

    @Test
    fun test_chat_srw() {
        val journeyId = "114"

        // Test
        track_chat_srw()
        track_show_onboarding_srw()
        track_close_onboarding_srw()

        // Then
        assertThat(cassavaTestRule.validate(journeyId), hasAllSuccess())
    }

    private fun track_chat_srw() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwUseCase.defaultResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(EX_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickSrwPreviewItemAt(0)

        // Close
        finishChatRoomActivity()
    }

    private fun track_show_onboarding_srw() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwProductBundlingResponse
        getChatPreAttachPayloadUseCase.response =
            getChatPreAttachPayloadUseCase.generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        CoachMarkPreference.setShown(context, SrwFrameLayout.TAG, false)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // Close
        finishChatRoomActivity()
    }

    private fun track_close_onboarding_srw() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwProductBundlingResponse
        getChatPreAttachPayloadUseCase.response =
            getChatPreAttachPayloadUseCase.generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        CoachMarkPreference.setShown(context, SrwFrameLayout.TAG, false)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        dismissSrwOnboarding()

        // Close
        finishChatRoomActivity()
    }
}
