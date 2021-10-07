package com.tokopedia.topchat.chatroom.view.activity

import android.util.Log
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.ReplyBubbleResult
import com.tokopedia.topchat.chatroom.view.activity.robot.ReplyBubbleRobot
import org.junit.Test

class ReplyBubbleTest : TopchatRoomTest() {

    @Test
    fun should_show_bottomsheet_menu_on_long_click_normal_text_bubble() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultReplyBubbleResponse
        launchChatRoomActivity()

        // When
        ReplyBubbleRobot.longClickBubbleAt(1)

        // Then
        assertLongClickMenu(isDisplayed())
    }

    @Test
    fun should_show_reply_bubble_compose_when_user_long_click_and_reply() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultReplyBubbleResponse
        launchChatRoomActivity()

        // When
        ReplyBubbleRobot.longClickBubbleAt(1)
        ReplyBubbleRobot.clickReplyItemMenu()

        // Then
        ReplyBubbleResult.hasVisibleReplyCompose()
    }

    @Test
    fun should_hide_or_cancel_reply_compose_when_user_click_close_icon() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultReplyBubbleResponse
        launchChatRoomActivity()

        // When
        ReplyBubbleRobot.longClickBubbleAt(1)
        ReplyBubbleRobot.clickReplyItemMenu()
        ReplyBubbleRobot.clickReplyComposeCloseIcon()

        // Then
        ReplyBubbleResult.hasNoVisibleReplyCompose()
    }

    @Test
    fun should_not_sent_closed_reply_compose_to_websocket() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultReplyBubbleResponse
        launchChatRoomActivity()

        // When
        ReplyBubbleRobot.longClickBubbleAt(1)
        ReplyBubbleRobot.clickReplyItemMenu()
        ReplyBubbleRobot.clickReplyComposeCloseIcon()
        clickComposeArea()
        typeMessage("reply this")
        clickSendBtn()

        // Then
        ReplyBubbleResult.hasNoVisibleReplyBubbleAt(0)
    }

    @Test
    fun should_go_to_specific_bubble_when_reply_compose_is_clicked() {
        // Given
        val lastBubbleIndex = getChatUseCase.getLastIndexOf(
            getChatUseCase.longReplyBubbleResponse
        )
        getChatUseCase.response = getChatUseCase.longReplyBubbleResponse
        launchChatRoomActivity()

        // When
        scrollChatToPosition(lastBubbleIndex)
        ReplyBubbleRobot.longClickBubbleAt(lastBubbleIndex)
        ReplyBubbleRobot.clickReplyItemMenu()
        scrollChatToPosition(0)
        ReplyBubbleRobot.clickReplyCompose()

        // Then
        assertMsgBubbleAt(lastBubbleIndex, isDisplayed())
    }

    @Test
    fun should_go_to_specific_bubble_when_msg_bubble_local_id_is_exist() {
        // Given
        val lastBubbleIndex = getChatUseCase.getLastIndexOf(
            getChatUseCase.longReplyBubbleResponse
        )
        getChatUseCase.response = getChatUseCase.longReplyBubbleResponse
        launchChatRoomActivity()

        // When
        scrollChatToPosition(lastBubbleIndex)
        ReplyBubbleRobot.longClickBubbleAt(lastBubbleIndex)
        ReplyBubbleRobot.clickReplyItemMenu()
        scrollChatToPosition(0)
        clickComposeArea()
        typeMessage("reply this")
        clickSendBtn()
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)
        ReplyBubbleRobot.clickReplyBubbleAt(0)

        // Then
        assertMsgBubbleAt(lastBubbleIndex + 1, isDisplayed())
    }

    @Test
    fun should_sent_and_render_reply_bubble_when_user_sent_normal_text() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultReplyBubbleResponse
        launchChatRoomActivity()

        // When
        ReplyBubbleRobot.longClickBubbleAt(1)
        ReplyBubbleRobot.clickReplyItemMenu()
        clickComposeArea()
        typeMessage("reply this")
        clickSendBtn()
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)

        // Then
        ReplyBubbleResult.hasVisibleReplyBubbleAt(0)
    }

    @Test
    fun should_show_normal_text_reply_bubble_when_parent_reply_is_not_null_from_GQL() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultReplyBubbleResponse
        launchChatRoomActivity()

        // Then
        ReplyBubbleResult.hasVisibleReplyBubbleAt(0)
    }

    @Test
    fun should_sent_and_render_reply_bubble_when_user_sent_sticker() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultReplyBubbleResponse
        launchChatRoomActivity()

        // When
        ReplyBubbleRobot.longClickBubbleAt(1)
        ReplyBubbleRobot.clickReplyItemMenu()
        clickStickerIconMenu()
        clickStickerAtPosition(0)
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)

        // Then
        ReplyBubbleResult.hasVisibleReplyBubbleStickerAt(0)
    }

    @Test
    fun should_show_sticker_reply_bubble_when_parent_reply_is_not_null_from_GQL() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultReplyBubbleResponse
        launchChatRoomActivity()

        // Then
        ReplyBubbleResult.hasVisibleReplyBubbleStickerAt(2)
    }

    @Test
    fun should_match_the_senderId_name_with_contacts_from_chatReplies_GQL() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultReplyBubbleResponse
        val roomMetaData = getChatUseCase.getCurrentRoomMetaData(MSG_ID)
        launchChatRoomActivity()

        // When
        ReplyBubbleRobot.longClickBubbleAt(1)
        ReplyBubbleRobot.clickReplyItemMenu()
        clickComposeArea()
        typeMessage("reply this")
        clickSendBtn()
        websocket.simulateResponseFromRequestQueue(getChatUseCase.response)

        // Then
        ReplyBubbleResult.hasVisibleReplyBubbleAt(0)
        ReplyBubbleResult.hasReplyBubbleTitleAt(0, roomMetaData.sender.name)
    }

    // TODO: should reset chatroom page like chat search when click reply bubble from GQL (ioe, local id is not exist)
    // TODO: should able copy to clipboard msg bubble
    // TODO: should show expired toaster when user click expired reply bubble
    // TODO: should disable long click on fraud status msg true from ws
    // TODO: should show image reply bubble when parent reply is not null from GQL
    // TODO: should show image reply bubble when parent reply is not null from websocket

}