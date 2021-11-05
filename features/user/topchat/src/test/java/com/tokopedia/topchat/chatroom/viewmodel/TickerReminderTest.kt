package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.topchat.chatroom.domain.pojo.getreminderticker.GetReminderTickerResponse
import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test

class TickerReminderTest : BaseTopChatViewModelTest() {

    @Test
    fun should_get_ticker_reminder_when_successfull() {
        //Given
        val expectedResponse = GetReminderTickerResponse()
        coEvery {
            reminderTickerUseCase.invoke(any())
        } returns expectedResponse

        //When
        viewModel.getTickerReminder()

        //Then
        assertThat(
            viewModel.srwTickerReminder.value,
            `is`(Success(expectedResponse.getReminderTicker))
        )
    }

}