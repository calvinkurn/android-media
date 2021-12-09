package com.tokopedia.topchat.chatroom.view.activity

import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.imageattachment.ImageAttachmentRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.longclickbubblemenu.LongClickBubbleMenuResult
import com.tokopedia.topchat.chatroom.view.activity.robot.longclickbubblemenu.LongClickBubbleMenuRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.msgbubble.MsgBubbleResult
import org.junit.Test

@UiTest
class UnsendImageTest: TopchatRoomTest() {

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
    }

    @Test
    fun should_not_show_bottomsheet_on_long_click_opposite_image_bubble(){
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
    fun should_render_deleted_type_bubble_when_status_equal_to_5_deleted_from_GQL(){
        // Given
        getChatUseCase.response = getChatUseCase.deleteImageResponseWithStatus5
        launchChatRoomActivity()

        // Then
        MsgBubbleResult.assertMsgIsDeletedAt(0)
    }

    // TODO: Should replace bubble to deleted type bubble when receive 104 event ws
    // TODO: Should prevent long click when msg bubble is deleted
    // TODO: Should show success toaster when success delete message
    // TODO: Should show failed toaster when error delete message

}