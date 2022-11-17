package com.tokopedia.tokochat.test

import com.tokopedia.tokochat.base.TokoChatViewModelTestFixture
import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
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
}
