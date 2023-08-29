package com.tokopedia.topchat.chatroom.view.activity.test

import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.copyToClipboardResult
import com.tokopedia.topchat.chatroom.view.activity.robot.copyToClipboardRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.replyBubbleRobot
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
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
        replyBubbleRobot {
            longClickBubbleAt(1)
        }
        copyToClipboardRobot {
            clickCtcItemMenu()
        }

        // Then
        copyToClipboardResult {
            val copiedText = getClipboardMsg(context)
            assertThat(copiedText.toString(), `is`(msg.toString()))
        }
    }
}
