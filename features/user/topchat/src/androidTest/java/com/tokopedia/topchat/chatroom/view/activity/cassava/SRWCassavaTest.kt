package com.tokopedia.topchat.chatroom.view.activity.cassava

import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.topchat.chatroom.view.activity.TopchatRoomBuyerProductAttachmentTest.Companion.putProductAttachmentIntent
import com.tokopedia.topchat.chatroom.view.activity.base.BaseBuyerTopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.srw.SrwRobot.dismissSrwOnboarding
import com.tokopedia.topchat.chatroom.view.custom.SrwFrameLayout
import org.hamcrest.MatcherAssert
import org.junit.Rule
import org.junit.Test

@CassavaTest
class SRWCassavaTest : BaseBuyerTopchatRoomTest() {

    @get:Rule
    var cassavaTestRule = CassavaTestRule(false, false)

    private val DEFAULT_PRODUCT_ID = "1111"

    @Test
    fun track_show_onboarding_srw() {
        // Given
        val journeyId = ""
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwProductBundlingResponse
        getChatPreAttachPayloadUseCase.response =
            getChatPreAttachPayloadUseCase.generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        CoachMarkPreference.setShown(context, SrwFrameLayout.TAG, false)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // Then
        MatcherAssert.assertThat(cassavaTestRule.validate(journeyId), hasAllSuccess())
    }

    @Test
    fun track_close_onboarding_srw() {
        // Given
        val journeyId = ""
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwProductBundlingResponse
        getChatPreAttachPayloadUseCase.response =
            getChatPreAttachPayloadUseCase.generatePreAttachPayload(DEFAULT_PRODUCT_ID)
        CoachMarkPreference.setShown(context, SrwFrameLayout.TAG, false)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        //When
        dismissSrwOnboarding()

        // Then
        MatcherAssert.assertThat(cassavaTestRule.validate(journeyId), hasAllSuccess())
    }
}