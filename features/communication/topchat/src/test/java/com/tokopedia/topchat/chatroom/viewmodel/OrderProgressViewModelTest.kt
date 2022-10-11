package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.topchat.FileUtil
import com.tokopedia.topchat.chatroom.domain.pojo.orderprogress.OrderProgressResponse
import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import org.junit.Assert
import org.junit.Test

class OrderProgressViewModelTest: BaseTopChatViewModelTest() {

    @Test
    fun should_get_order_progress_response_when_success() {
        // Given
        val successGetOrderProgressResponse: OrderProgressResponse = FileUtil.parse(
            "/success_get_order_progress.json",
            OrderProgressResponse::class.java
        )

        coEvery {
            orderProgressUseCase(any())
        } returns successGetOrderProgressResponse

        // When
        viewModel.getOrderProgress(testMessageId)

        // Then
        Assert.assertEquals(successGetOrderProgressResponse,
            (viewModel.orderProgress.value as Success).data
        )
    }

    @Test
    fun should_get_throwable_when_fail_to_get_order_progress() {
        // Given
        coEvery {
            orderProgressUseCase(any())
        } throws expectedThrowable

        // When
        viewModel.getOrderProgress(testMessageId)

        // Then
        Assert.assertEquals(expectedThrowable.message,
            (viewModel.orderProgress.value as Fail).throwable.message
        )
    }
}