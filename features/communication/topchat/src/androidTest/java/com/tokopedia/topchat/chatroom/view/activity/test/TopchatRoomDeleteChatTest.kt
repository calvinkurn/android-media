package com.tokopedia.topchat.chatroom.view.activity.test

import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.chatlist.domain.pojo.ChatDeleteStatus
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.generalResult
import com.tokopedia.topchat.chatroom.view.activity.robot.headerRobot
import org.junit.Assert.assertTrue
import org.junit.Test

class TopchatRoomDeleteChatTest: TopchatRoomTest() {

    @Test
    fun should_close_chatroom_when_success_delete_chat(){
        //Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        moveChatToTrashUseCase.response = getChatDeleteResponse()
        launchChatRoomActivity()
        stubIntents()

        //When
        headerRobot {
            clickThreeDotsMenu()
            clickDeleteChat()
            clickConfirmDeleteChat()
        }

        //Then
        assertTrue(activity.isFinishing)
    }

    @Test
    fun should_show_error_message_when_fail_to_delete_chat(){
        //Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        moveChatToTrashUseCase.response = getChatDeleteResponse(isSuccess = false)
        launchChatRoomActivity()
        stubIntents()

        //When
        headerRobot {
            clickThreeDotsMenu()
            clickDeleteChat()
            clickConfirmDeleteChat()
        }

        //Then
        generalResult {
            assertToasterWithSubText("Oops!")
        }
    }

    @Test
    fun should_show_error_message_when_error_to_delete_chat(){
        //Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        moveChatToTrashUseCase.errorMessage = "Oops!"
        launchChatRoomActivity()
        stubIntents()

        //When
        headerRobot {
            clickThreeDotsMenu()
            clickDeleteChat()
            clickConfirmDeleteChat()
        }

        //Then
        generalResult {
            assertToasterWithSubText("Oops!")
        }
    }

    private fun getChatDeleteResponse(isSuccess: Boolean = true): ChatDeleteStatus {
        val chatDeleteResponse: ChatDeleteStatus = AndroidFileUtil.parse(
            "success_delete_chat.json",
            ChatDeleteStatus::class.java
        )
        if (!isSuccess) {
            chatDeleteResponse.chatMoveToTrash.list.first().isSuccess = 0
            chatDeleteResponse.chatMoveToTrash.list.first().detailResponse = "Oops!"
        }
        return chatDeleteResponse
    }
}
