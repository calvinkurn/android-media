package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.topchat.chatlist.pojo.ChatDelete
import com.tokopedia.topchat.chatlist.pojo.ChatDeleteStatus
import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import com.tokopedia.topchat.common.Constant.INT_STATUS_TRUE
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import org.junit.Assert
import org.junit.Test

class MoveChatToTrashUseCaseViewModelTest: BaseTopChatViewModelTest() {
    @Test
    fun should_get_delete_chat_status_when_success_delete_chat() {
        //Given
        val expectedResult = ChatDeleteStatus().apply {
            this.chatMoveToTrash.list = arrayListOf(
                ChatDelete(
                    isSuccess = INT_STATUS_TRUE,
                    detailResponse = "Success",
                    messageId = testMessageId.toLongOrZero()
                )
            )
        }
        coEvery {
            mutationMoveChatToTrashUseCase(any())
        } returns expectedResult

        //When
        viewModel.deleteChat(testMessageId)

        //Then
        Assert.assertEquals(
            expectedResult,
            (viewModel.chatDeleteStatus.value as Success).data
        )
    }

    @Test
    fun should_get_delete_chat_status_when_success_delete_chat_but_empty() {
        //Given
        val expectedResult = ChatDeleteStatus().apply {
            this.chatMoveToTrash.list = arrayListOf()
        }
        coEvery {
            mutationMoveChatToTrashUseCase(any())
        } returns expectedResult

        //When
        viewModel.deleteChat(testMessageId)

        //Then
        Assert.assertEquals(
            null,
            viewModel.chatDeleteStatus.value
        )
    }

    @Test
    fun should_get_error_message_when_fail_to_delete_chat() {
        //Given
        val expectedResult = ChatDeleteStatus().apply {
            this.chatMoveToTrash.list = arrayListOf(
                ChatDelete(
                    isSuccess = 0,
                    detailResponse = "Oops!",
                    messageId = testMessageId.toLongOrZero()
                )
            )
        }
        coEvery {
            mutationMoveChatToTrashUseCase(any())
        } returns expectedResult

        //When
        viewModel.deleteChat(testMessageId)

        //Then
        Assert.assertEquals(
            expectedResult.chatMoveToTrash.list.first().detailResponse,
            (viewModel.chatDeleteStatus.value as Fail).throwable.message
        )
    }

    @Test
    fun should_get_exception_when_error_delete_chat() {
        //Given
        coEvery {
            mutationMoveChatToTrashUseCase(any())
        } throws expectedThrowable

        //When
        viewModel.deleteChat(testMessageId)

        //Then
        Assert.assertEquals(
            expectedThrowable.message,
            (viewModel.chatDeleteStatus.value as Fail).throwable.message
        )
    }
}