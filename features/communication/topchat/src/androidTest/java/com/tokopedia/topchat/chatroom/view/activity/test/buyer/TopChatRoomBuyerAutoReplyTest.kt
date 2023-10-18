package com.tokopedia.topchat.chatroom.view.activity.test.buyer

import com.tokopedia.topchat.chatroom.view.activity.base.BaseBuyerTopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.generalRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.msgBubbleResult
import com.tokopedia.topchat.chatroom.view.activity.robot.msgBubbleRobot
import org.junit.Test

class TopChatRoomBuyerAutoReplyTest : BaseBuyerTopchatRoomTest() {

    @Test
    fun open_chatroom_get_auto_reply() {
        // Given
        getChatUseCase.response = getChatUseCase.autoReplyResponse
        launchChatRoomActivity()

        // When
        generalRobot {
            smoothScrollChatToPosition(1)
        }

        // Then
        msgBubbleResult {
            // Old Auto Reply
            assertAutoReplyWelcomeMessage(
                position = 1,
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                checkTruncated = false
            )
            assertAutoReplyListRvGoneAt(1)
            assertAutoReplyBubbleLabelAt(1)
            assertAutoReplyReadMoreAt(1, "Baca Selengkapnya")
        }

        // When
        generalRobot {
            smoothScrollChatToPosition(0)
        }

        // New Auto Reply
        msgBubbleResult {
            assertAutoReplyWelcomeMessage(
                position = 0,
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                checkTruncated = true // ellipsis
            )
            assertAutoReplyListRvShownAt(0)
            assertAutoReplyListItemCountAt(position = 0, 3)
            assertAutoReplyBubbleLabelAt(0)
            assertAutoReplyReadMoreAt(0, "Baca Selengkapnya")
        }
    }

    @Test
    fun click_read_more_show_auto_reply_bottomsheet() {
        // Given
        getChatUseCase.response = getChatUseCase.autoReplyResponse
        launchChatRoomActivity()

        // When
        // click button selengkapnya
        msgBubbleRobot {
            clickReadMoreAutoReplyMsgBubbleAt(0)
        }

        // Then
        msgBubbleResult {
            assertAutoReplyBottomSheetTitle("Informasi Toko")
            assertAutoReplyListBottomSheet()
            assertAutoReplyListItemCount(3)
        }
    }
}
