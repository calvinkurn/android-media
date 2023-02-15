package com.tokopedia.topchat.chatroom.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.chat_common.domain.pojo.ChatReplies
import com.tokopedia.chat_common.domain.pojo.Contact
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.chat_common.domain.pojo.roommetadata.RoomMetaData
import com.tokopedia.topchat.chatroom.domain.pojo.GetChatResult
import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.every
import io.mockk.verify
import org.junit.Assert
import org.junit.Test

class GetChatViewModelTest : BaseTopChatViewModelTest() {

    override fun before() {
        super.before()
        viewModel.setRoomMetaData(RoomMetaData())
    }

    @Test
    fun check_reset_chat() {
        // When
        viewModel.resetChatUseCase()

        // Then
        verify(exactly = 1) {
            getChatUseCase.reset()
        }
    }

    @Test
    fun check_setBeforeReplyTime() {
        // Given
        val exCreateTime = "1234532"

        // When
        viewModel.setBeforeReplyTime(exCreateTime)

        // Then
        verify(exactly = 1) {
            getChatUseCase.minReplyTime = exCreateTime
        }
    }

    @Test
    fun check_isInTheMiddleOfThePage() {
        // When
        every {
            getChatUseCase.isInTheMiddleOfThePage()
        } returns MutableLiveData(true)

        viewModel.isInTheMiddleOfThePage()

        // Then
        verify(exactly = 1) {
            getChatUseCase.isInTheMiddleOfThePage()
        }
    }

    @Test
    fun should_get_existing_chat_when_success_but_message_id_empty() {
        // When
        viewModel.getExistingChat("")

        // Then
        Assert.assertEquals(
            null,
            viewModel.existingChat.value
        )
    }

    @Test
    fun should_get_existing_chat_when_success_get_chat_init() {
        // Given
        val expectedResponse = GetExistingChatPojo()
        val chatroomViewModel = existingChatMapper.map(expectedResponse)
        val expectedResult = GetChatResult(chatroomViewModel, expectedResponse.chatReplies)
        coEvery {
            getChatUseCase.getFirstPageChat(any())
        } returns expectedResponse

        // When
        viewModel.getExistingChat(testMessageId, true)

        // Then
        Assert.assertEquals(
            expectedResult,
            (viewModel.existingChat.value?.first as Success).data
        )
    }

    @Test
    fun should_get_existing_chat_when_success_get_chat_not_init() {
        // Given
        val expectedResponse = GetExistingChatPojo()
        val chatroomViewModel = existingChatMapper.map(expectedResponse)
        val expectedResult = GetChatResult(chatroomViewModel, expectedResponse.chatReplies)
        coEvery {
            getChatUseCase.getFirstPageChat(any())
        } returns expectedResponse

        // When
        viewModel.getExistingChat(testMessageId)

        // Then
        Assert.assertEquals(
            expectedResult,
            (viewModel.existingChat.value?.first as Success).data
        )
    }

    @Test
    fun check_room_meta_data() {
        // Given
        val expectedResponse = GetExistingChatPojo(
            chatReplies = ChatReplies(
                contacts = arrayListOf(Contact(userId = testUserId))
            )
        )
        val expectedMetaData = existingChatMapper.generateRoomMetaData(testMessageId, expectedResponse)
        coEvery {
            getChatUseCase.getFirstPageChat(any())
        } returns expectedResponse

        // When
        viewModel.getExistingChat(testMessageId)
        val metaData = viewModel.roomMetaData.value

        // Then
        Assert.assertEquals(
            expectedMetaData,
            metaData
        )
    }

    @Test
    fun should_get_error_when_failed_to_get_chat() {
        // Given
        coEvery {
            getChatUseCase.getFirstPageChat(any())
        } throws expectedThrowable

        // When
        viewModel.getExistingChat(testMessageId)

        // Then
        Assert.assertEquals(
            expectedThrowable.message,
            (viewModel.existingChat.value?.first as Fail).throwable.message
        )
    }

    @Test
    fun should_get_chat_when_success_load_top_page_chat_but_message_id_empty() {
        // When
        viewModel.loadTopChat("")

        // Then
        Assert.assertEquals(
            null,
            viewModel.topChat.value
        )
    }

    @Test
    fun should_get_chat_when_success_load_top_page_chat() {
        // Given
        val expectedResponse = GetExistingChatPojo()
        val chatroomViewModel = existingChatMapper.map(expectedResponse)
        val expectedResult = GetChatResult(chatroomViewModel, expectedResponse.chatReplies)
        coEvery {
            getChatUseCase.getTopChat(any())
        } returns expectedResponse

        // When
        viewModel.loadTopChat(testMessageId)

        // Then
        Assert.assertEquals(
            expectedResult,
            (viewModel.topChat.value as Success).data
        )
    }

    @Test
    fun should_get_error_when_failed_to_get_top_chat() {
        // Given
        coEvery {
            getChatUseCase.getTopChat(any())
        } throws expectedThrowable

        // When
        viewModel.loadTopChat(testMessageId)

        // Then
        Assert.assertEquals(
            expectedThrowable.message,
            (viewModel.topChat.value as Fail).throwable.message
        )
    }

    @Test
    fun should_get_chat_when_success_load_bottom_page_chat_but_message_id_empty() {
        // When
        viewModel.loadBottomChat("")

        // Then
        Assert.assertEquals(
            null,
            viewModel.bottomChat.value
        )
    }

    @Test
    fun should_get_chat_when_success_load_bottom_page_chat() {
        // Given
        val expectedResponse = GetExistingChatPojo()
        val chatroomViewModel = existingChatMapper.map(expectedResponse)
        val expectedResult = GetChatResult(chatroomViewModel, expectedResponse.chatReplies)
        coEvery {
            getChatUseCase.getBottomChat(any())
        } returns expectedResponse

        // When
        viewModel.loadBottomChat(testMessageId)

        // Then
        Assert.assertEquals(
            expectedResult,
            (viewModel.bottomChat.value as Success).data
        )
    }

    @Test
    fun should_get_error_when_failed_to_get_bottom_chat() {
        // Given
        coEvery {
            getChatUseCase.getBottomChat(any())
        } throws expectedThrowable

        // When
        viewModel.loadBottomChat(testMessageId)

        // Then
        Assert.assertEquals(
            expectedThrowable.message,
            (viewModel.bottomChat.value as Fail).throwable.message
        )
    }
}
