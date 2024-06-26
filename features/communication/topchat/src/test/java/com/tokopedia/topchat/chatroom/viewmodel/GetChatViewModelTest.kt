package com.tokopedia.topchat.chatroom.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.chat_common.domain.pojo.ChatReplies
import com.tokopedia.chat_common.domain.pojo.Contact
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.chat_common.domain.pojo.roommetadata.RoomMetaData
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.topchat.chatroom.domain.pojo.GetChatResult
import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.every
import io.mockk.verify
import org.junit.Assert.assertEquals
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
    fun should_give_correct_isInTheMiddleOfThePage_after_set() {
        // Given
        val dummyIsInTheMiddleOfThePage = true

        // When
        webSocketViewModel.isInTheMiddleOfThePage = dummyIsInTheMiddleOfThePage

        // Then
        assertEquals(dummyIsInTheMiddleOfThePage, webSocketViewModel.isInTheMiddleOfThePage)
    }

    @Test
    fun should_get_existing_chat_when_success_but_message_id_empty() {
        // When
        viewModel.getExistingChat("")

        // Then
        assertEquals(
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
        assertEquals(
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
        assertEquals(
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
        assertEquals(
            expectedMetaData,
            metaData
        )
    }

    @Test
    fun should_give_correct_room_meta_data_after_set() {
        // Given
        val dummyRoomMetaData = RoomMetaData(
            _msgId = testMessageId
        )

        // When
        webSocketViewModel.roomMetaData = dummyRoomMetaData

        // Then
        assertEquals(webSocketViewModel.roomMetaData, dummyRoomMetaData)
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
        assertEquals(
            expectedThrowable.message,
            (viewModel.existingChat.value?.first as Fail).throwable.message
        )
    }

    @Test
    fun should_get_chat_when_success_load_top_page_chat_but_message_id_empty() {
        // When
        viewModel.loadTopChat("")

        // Then
        assertEquals(
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
        assertEquals(
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
        assertEquals(
            expectedThrowable.message,
            (viewModel.topChat.value as Fail).throwable.message
        )
    }

    @Test
    fun should_get_chat_when_success_load_bottom_page_chat_but_message_id_empty() {
        // When
        viewModel.loadBottomChat("")

        // Then
        assertEquals(
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
        assertEquals(
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
        assertEquals(
            expectedThrowable.message,
            (viewModel.bottomChat.value as Fail).throwable.message
        )
    }

    @Test
    fun should_do_nothing_when_set_message_id_but_room_message_id_null() {
        // Given
        viewModel.setRoomMetaData(null)

        // When
        viewModel.updateMessageId(testMessageId)

        // Then
        assertEquals(null, viewModel.roomMetaData.value)
    }

    @Test
    fun should_give_correct_value_when_in_the_middle_of_page() {
        // Given
        every {
            getChatUseCase.isInTheMiddleOfThePage()
        } returns MutableLiveData(true)

        // When
        val result = viewModel.getMiddlePageLiveData().value

        // Then
        assertEquals(true, result)
    }

    @Test
    fun should_give_false_value_when_get_in_the_middle_of_page_but_null() {
        // Given
        every {
            getChatUseCase.isInTheMiddleOfThePage()
        } returns MutableLiveData(null)

        // When
        val result = viewModel.isInTheMiddleOfThePage()

        // Then
        assertEquals(false, result)
    }

    @Test
    fun should_give_correct_value_when_set_user_location() {
        // Given
        val localCacheModel = LocalCacheModel(address_id = "123")
        viewModel.initUserLocation(localCacheModel)

        // When
        val result = viewModel.userLocationInfo.value

        // Then
        assertEquals(localCacheModel.address_id, result?.address_id)
    }
}
