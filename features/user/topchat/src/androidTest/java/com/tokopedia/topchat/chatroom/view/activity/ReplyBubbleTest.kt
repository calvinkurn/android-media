package com.tokopedia.topchat.chatroom.view.activity

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

    // TODO: should not sent closed reply compose to websocket
    // TODO: should go to specific bubble when reply compose is clicked
    // TODO: should sent and render reply bubble when user sent normal text
    // TODO: should show normal text reply bubble when parent reply is not null from GQL
    // TODO: should show text reply bubble when parent reply is not null from websocket
    // TODO: should sent and render reply bubble when user sent sticker
    // TODO: should show sticker reply bubble when parent reply is not null from GQL
    // TODO: should show sticker reply bubble when parent reply is not null from websocket
    // TODO: should show image reply bubble when parent reply is not null from GQL
    // TODO: should show image reply bubble when parent reply is not null from websocket
    // TODO: should match the senderId name with contacts from chatReplies GQL
    // TODO: should go to specific bubble when msg bubble local id is exist
    // TODO: should reset chatroom page like chat search when click reply bubble from GQL (ioe, local id is not exist)
    // TODO: should able copy to clipboard msg bubble
    // TODO: should show expired toaster when user click expired reply bubble
    // TODO: should disable long click on fraud status msg true from ws

}