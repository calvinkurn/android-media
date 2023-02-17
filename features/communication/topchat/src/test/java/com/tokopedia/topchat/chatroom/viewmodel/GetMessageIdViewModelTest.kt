package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.topchat.chatroom.domain.pojo.GetExistingMessageIdPojo
import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import org.junit.Assert
import org.junit.Test

class GetMessageIdViewModelTest : BaseTopChatViewModelTest() {
    @Test
    fun should_get_message_id_when_successfull() {
        // Given
        val expectedMessageId = "567"
        val expectedResult = GetExistingMessageIdPojo().apply {
            this.chatExistingChat.messageId = expectedMessageId
        }
        coEvery {
            getExistingMessageIdUseCase.invoke(any())
        } returns expectedResult

        // When
        viewModel.getMessageId(testShopId, testUserId, source)

        // Then
        Assert.assertEquals(
            viewModel.messageId.value,
            Success(expectedResult.chatExistingChat.messageId)
        )
    }

    @Test
    fun should_get_message_id_when_successfull_even_room_meta_data_is_null() {
        // Given
        val expectedMessageId = "567"
        val expectedResult = GetExistingMessageIdPojo().apply {
            this.chatExistingChat.messageId = expectedMessageId
        }
        coEvery {
            getExistingMessageIdUseCase.invoke(any())
        } returns expectedResult
        viewModel.setRoomMetaData(null)

        // When
        viewModel.getMessageId(testShopId, testUserId, source)

        // Then
        Assert.assertEquals(
            viewModel.messageId.value,
            Success(expectedResult.chatExistingChat.messageId)
        )
        Assert.assertEquals(
            null,
            viewModel.roomMetaData.value
        )
    }

    @Test
    fun should_not_error_when_the_userid_and_shopid_is_not_number() {
        // Given
        val testShopIdWrong = "test"
        val testUserIdWrong = "test"
        val expectedMessageId = "567"
        val expectedResult = GetExistingMessageIdPojo().apply {
            this.chatExistingChat.messageId = expectedMessageId
        }
        coEvery {
            getExistingMessageIdUseCase.invoke(any())
        } returns expectedResult

        // When
        viewModel.getMessageId(testShopIdWrong, testUserIdWrong, source)

        // Then
        Assert.assertEquals(
            viewModel.messageId.value,
            Success(expectedResult.chatExistingChat.messageId)
        )
    }

    @Test
    fun should_get_throwable_when_failed_get_message_id() {
        // Given
        coEvery {
            getExistingMessageIdUseCase.invoke(any())
        } throws expectedThrowable

        // When
        viewModel.getMessageId(testShopId, testUserId, source)

        // Then
        Assert.assertEquals(viewModel.messageId.value, Fail(expectedThrowable))
    }
}
