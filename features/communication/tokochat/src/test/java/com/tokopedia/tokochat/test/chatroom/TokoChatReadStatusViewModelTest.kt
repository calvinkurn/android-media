package com.tokopedia.tokochat.test.chatroom

import com.tokopedia.tokochat.base.TokoChatViewModelTestFixture
import com.tokopedia.tokochat.utils.observeAwaitValue
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class TokoChatReadStatusViewModelTest : TokoChatViewModelTestFixture() {

    @Test
    fun `when markChatAsRead should call markAllMessagesAsRead (from SDK) once`() {
        runBlocking {
            // When
            viewModel.markChatAsRead(CHANNEL_ID_DUMMY)

            // Then
            coVerify(exactly = 1) {
                markAsReadUseCase(CHANNEL_ID_DUMMY)
            }
        }
    }

    @Test
    fun `when failed to markChatAsRead should call give throwable on error livedata`() {
        runBlocking {
            // Given
            coEvery {
                markAsReadUseCase(any())
            } throws throwableDummy

            // When
            viewModel.markChatAsRead(CHANNEL_ID_DUMMY)

            // Then
            assertEquals(
                throwableDummy,
                viewModel.error.observeAwaitValue()?.first
            )
        }
    }
}
