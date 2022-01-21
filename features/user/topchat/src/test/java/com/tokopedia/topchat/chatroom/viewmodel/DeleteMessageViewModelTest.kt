package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.topchat.chatroom.domain.pojo.unsendreply.UnsendReply
import com.tokopedia.topchat.chatroom.domain.pojo.unsendreply.UnsendReplyResponse
import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf
import org.junit.Test

class DeleteMessageViewModelTest : BaseTopChatViewModelTest() {

    @Test
    fun should_update_live_data_value_if_success() {
        // Given
        val successResponse = UnsendReplyResponse(UnsendReply(isSuccess = true))
        val replyTime = "11"
        coEvery {
            unsendReplyUseCase.invoke(any())
        } returns successResponse

        // When
        viewModel.deleteMsg("0", replyTime)

        // Then
        val result: Result<String>? = viewModel.deleteBubble.value
        val data = (result as? Success)?.data
        assertThat(result, IsInstanceOf(Success::class.java))
        assertThat(data, `is`(replyTime))
    }

    @Test
    fun should_update_live_data_value_if_failed() {
        // Given
        val successResponse = UnsendReplyResponse(UnsendReply(isSuccess = false))
        val replyTime = "11"
        coEvery {
            unsendReplyUseCase.invoke(any())
        } returns successResponse

        // When
        viewModel.deleteMsg("0", replyTime)

        // Then
        val result: Result<String>? = viewModel.deleteBubble.value
        assertThat(result, IsInstanceOf(Fail::class.java))
    }

    @Test
    fun should_update_live_data_value_if_throw_exception() {
        // Given
        coEvery {
            unsendReplyUseCase.invoke(any())
        } throws IllegalStateException("error")

        // When
        viewModel.deleteMsg("0", "0")

        // Then
        val result: Result<String>? = viewModel.deleteBubble.value
        assertThat(result, IsInstanceOf(Fail::class.java))
    }

}