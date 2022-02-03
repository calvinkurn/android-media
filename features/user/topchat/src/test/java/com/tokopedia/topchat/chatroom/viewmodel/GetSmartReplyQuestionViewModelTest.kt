package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.topchat.chatroom.domain.pojo.srw.ChatSmartReplyQuestionResponse
import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import com.tokopedia.topchat.common.data.Resource
import com.tokopedia.topchat.common.data.Status
import io.mockk.coEvery
import kotlinx.coroutines.flow.flow
import org.junit.Assert
import org.junit.Test

class GetSmartReplyQuestionViewModelTest: BaseTopChatViewModelTest() {

    private val testProductId = "testProduct123"

    @Test
    fun should_get_response_when_success_get_srw_question() {
        //Given
        val expectedResponse = ChatSmartReplyQuestionResponse()
        coEvery {
            getSmartReplyQuestionUseCase(any())
        } returns flow {
            emit(Resource.success(expectedResponse))
        }

        //When
        viewModel.getSmartReplyWidget(testMessageId, testProductId)

        //Then
        Assert.assertEquals(
            expectedResponse,
            viewModel.srw.value?.data
        )
    }

    @Test
    fun should_get_error_when_fail_to_get_srw_question() {
        //Given
        coEvery {
            getSmartReplyQuestionUseCase(any())
        } throws expectedThrowable

        //When
        viewModel.getSmartReplyWidget(testMessageId, testProductId)

        //Then
        Assert.assertEquals(
            Status.ERROR,
            viewModel.srw.value?.status
        )
    }
}