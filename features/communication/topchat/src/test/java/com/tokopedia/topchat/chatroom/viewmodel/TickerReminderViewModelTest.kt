package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.topchat.chatroom.domain.pojo.getreminderticker.GetReminderTickerResponse
import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Test

class TickerReminderViewModelTest : BaseTopChatViewModelTest() {

    @Test
    fun should_get_ticker_reminder_when_successfull() {
        // Given
        val expectedResponse = GetReminderTickerResponse()
        coEvery {
            reminderTickerUseCase.invoke(any())
        } returns expectedResponse

        // When
        viewModel.getTickerReminder(false)

        // Then
        assertThat(
            viewModel.tickerReminder.value,
            `is`(Success(expectedResponse.getReminderTicker))
        )
    }

    @Test
    fun should_get_ticker_reminder_when_successfull_even_when_msg_id_zero() {
        // Given
        val expectedResponse = GetReminderTickerResponse()
        coEvery {
            reminderTickerUseCase.invoke(any())
        } returns expectedResponse
        viewModel.setRoomMetaData(null)

        // When
        viewModel.getTickerReminder(false)

        // Then
        assertThat(
            viewModel.tickerReminder.value,
            `is`(Success(expectedResponse.getReminderTicker))
        )
        assertEquals(null, viewModel.roomMetaData.value)
    }

    @Test
    fun should_do_nothing_when_error_get_ticker_reminder() {
        // Given
        coEvery {
            reminderTickerUseCase.invoke(any())
        } throws IllegalStateException()

        // When
        viewModel.getTickerReminder(false)

        // Then
        assertEquals(
            viewModel.tickerReminder.value,
            null
        )
    }

    @Test
    fun should_close_ticker_reminder_when_closed() {
        // Given
        val response = GetReminderTickerResponse()
        coEvery {
            closeReminderTicker.invoke(any())
        } returns Unit

        // When
        viewModel.closeTickerReminder(response.getReminderTicker, false)

        // Then
        coVerify {
            closeReminderTicker(any())
        }
    }

    @Test
    fun should_close_ticker_reminder_when_closed_even_when_room_meta_data_is_null() {
        // Given
        val response = GetReminderTickerResponse()
        coEvery {
            closeReminderTicker.invoke(any())
        } returns Unit
        viewModel.setRoomMetaData(null)

        // When
        viewModel.closeTickerReminder(response.getReminderTicker, false)

        // Then
        coVerify {
            closeReminderTicker(any())
        }
    }

    @Test
    fun should_do_nothing_when_error_close_ticker_reminder() {
        // Given
        val response = GetReminderTickerResponse()
        coEvery {
            closeReminderTicker.invoke(any())
        } throws IllegalStateException()

        // When
        viewModel.closeTickerReminder(response.getReminderTicker, false)
    }
}
