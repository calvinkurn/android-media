package com.tokopedia.topchat.chatroom.view.activity

import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.copytoclipboard.CopyToClipboardRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.replybubble.ReplyBubbleRobot
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Test


@UiTest
class CopyMessageToClipBoardTest : TopchatRoomTest() {

    @Test
    fun should_able_copy_to_clipboard_msg_bubble() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultReplyBubbleResponse
        launchChatRoomActivity()

        // When
        val msg = getBubbleMsgAtPosition(1)
        ReplyBubbleRobot.longClickBubbleAt(1)
        CopyToClipboardRobot.clickCtcItemMenu()
        val copiedText = getClipboardMsg()

        // Then
        assertThat(copiedText.toString(), `is`(msg.toString()))
    }

}