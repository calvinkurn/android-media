package com.tokopedia.topchat.chatroom.view.activity.cassava

import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.composeAreaRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.header.HeaderRobot
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

@CassavaTest
class ChatRoomCassavaTest : TopchatRoomTest() {

    @get:Rule
    var cassavaTestRule = CassavaTestRule(true, true)

    @Test
    fun chat_follow_shop_tracker() {
        // Given
        val journeyId = "117"
        getChatUseCase.response = getChatUseCase.defaultChatWithBuyerResponse
        launchChatRoomActivity()

        // When
        HeaderRobot.clickThreeDotsMenu()
        HeaderRobot.clickFollowMenu()

        // Then
        assertThat(cassavaTestRule.validate(journeyId), hasAllSuccess())
    }

    @Test
    fun chat_report_user() {
        // Given
        val journeyId = "125"
        getChatUseCase.response = getChatUseCase.defaultChatWithBuyerResponse
        launchChatRoomActivity()
        stubIntents()

        // When
        HeaderRobot.clickThreeDotsMenu()
        HeaderRobot.clickReportUserMenu()

        // Then
        assertThat(cassavaTestRule.validate(journeyId), hasAllSuccess())
    }

    @Test
    fun chat_send_chat() {
        // Given
        val journeyId = "88"
        getChatUseCase.response = getChatUseCase.defaultChatWithBuyerResponse
        launchChatRoomActivity()

        // When
        composeAreaRobot {
            clickComposeArea()
            typeMessageComposeArea("Hello")
            clickSendBtn()
        }

        // Then
        assertThat(cassavaTestRule.validate(journeyId), hasAllSuccess())
    }

    @Test
    fun product_card_impression_tracker_test() {
        // Given
        val journeyId = "142"
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()
        stubIntents()

        // Then
        assertThat(cassavaTestRule.validate(journeyId), hasAllSuccess())
    }
}
