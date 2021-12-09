package com.tokopedia.topchat.chatroom.view.activity

import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.imageattachment.ImageAttachmentRobot
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

    // TODO: Should not show bottomsheet on long click opposite image bubble
    // TODO: Should render deleted type bubble when status equal to 5 (deleted) from GQL
    // TODO: Should replace bubble to deleted type bubble when receive 104 event ws
    // TODO: Should prevent long click when msg bubble is deleted
    // TODO: Should show success toaster when success delete message
    // TODO: Should show failed toaster when error delete message

}