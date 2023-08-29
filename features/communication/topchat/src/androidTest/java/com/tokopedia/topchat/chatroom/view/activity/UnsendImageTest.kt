package com.tokopedia.topchat.chatroom.view.activity

import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.generalResult
import com.tokopedia.topchat.chatroom.view.activity.robot.imageattachment.ImageAttachmentResult
import com.tokopedia.topchat.chatroom.view.activity.robot.imageattachment.ImageAttachmentRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.longclickbubblemenu.LongClickBubbleMenuResult
import com.tokopedia.topchat.chatroom.view.activity.robot.longclickbubblemenu.LongClickBubbleMenuRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.msgbubble.MsgBubbleResult
import com.tokopedia.topchat.chatroom.view.activity.robot.replybubble.ReplyBubbleRobot
import org.junit.Test

@UiTest
class UnsendImageTest : TopchatRoomTest() {

    /**
     * included:
     * - Should show success toaster when success delete message
     * - Should show bottomsheet menu before deletion
     * - Should show dialog confirmation before deletion
     */
    @Test
    fun should_replace_image_bubble_to_deleted_type_bubble_after_success_delete_message() {
        // Given
        getChatUseCase.response = getChatUseCase.deleteImageResponse
        unsendReplyUseCase.response = unsendReplyUseCase.successDeleteResponse
        launchChatRoomActivity()

        // When
        ImageAttachmentRobot.longClickImageAt(0)
        LongClickBubbleMenuRobot.clickDeleteMsgMenu()
        LongClickBubbleMenuRobot.clickConfirmDeleteMsgDialog()

        // Then
        MsgBubbleResult.assertMsgIsDeletedAt(0)
        generalResult {
            assertToasterText(context.getString(R.string.topchat_success_delete_msg_bubble))
        }
    }

    @Test
    fun should_not_show_bottomsheet_on_long_click_opposite_image_bubble() {
        // Given
        getChatUseCase.response = getChatUseCase.deleteImageResponse
        unsendReplyUseCase.response = unsendReplyUseCase.successDeleteResponse
        launchChatRoomActivity()

        // When
        ImageAttachmentRobot.longClickImageAt(1)

        // Then
        LongClickBubbleMenuResult.assertNoBottomSheet()
    }

    @Test
    fun should_render_deleted_type_bubble_when_status_equal_to_5_deleted_from_GQL() {
        // Given
        getChatUseCase.response = getChatUseCase.deleteImageResponseWithStatus5
        launchChatRoomActivity()

        // Then
        MsgBubbleResult.assertMsgIsDeletedAt(0)
    }

    @Test
    fun should_replace_bubble_to_deleted_type_bubble_when_receive_104_event_ws() {
        // Given
        getChatUseCase.response = getChatUseCase.deleteImageResponse
        launchChatRoomActivity()

        // When
        websocket.simulateResponse(websocket.deleteImageResponse)

        // Then
        MsgBubbleResult.assertMsgIsDeletedAt(0)
    }

    @Test
    fun should_prevent_long_click_when_msg_bubble_is_deleted() {
        // Given
        getChatUseCase.response = getChatUseCase.deleteImageResponseWithStatus5
        launchChatRoomActivity()

        // When
        ReplyBubbleRobot.longClickBubbleAt(0)

        // Then
        LongClickBubbleMenuResult.assertNoBottomSheet()
    }

    @Test
    fun should_show_failed_toaster_when_error_delete_message() {
        // Given
        getChatUseCase.response = getChatUseCase.deleteImageResponse
        unsendReplyUseCase.isError = true
        launchChatRoomActivity()

        // When
        ImageAttachmentRobot.longClickImageAt(0)
        LongClickBubbleMenuRobot.clickDeleteMsgMenu()
        LongClickBubbleMenuRobot.clickConfirmDeleteMsgDialog()

        // Then
        ImageAttachmentResult.assertExistAt(0)
        generalResult {
            assertToasterText(context.getString(R.string.topchat_error_delete_msg_bubble))
        }
    }

    @Test
    fun should_show_failed_toaster_when_fail_delete_message() {
        // Given
        getChatUseCase.response = getChatUseCase.deleteImageResponse
        unsendReplyUseCase.response = unsendReplyUseCase.failDeleteResponse
        launchChatRoomActivity()

        // When
        ImageAttachmentRobot.longClickImageAt(0)
        LongClickBubbleMenuRobot.clickDeleteMsgMenu()
        LongClickBubbleMenuRobot.clickConfirmDeleteMsgDialog()

        // Then
        ImageAttachmentResult.assertExistAt(0)
        generalResult {
            assertToasterText(context.getString(R.string.topchat_error_delete_msg_bubble))
        }
    }
}
