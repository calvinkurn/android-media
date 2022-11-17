package com.tokopedia.tokochat.test

import com.tokopedia.tokochat.base.TokoChatViewModelTestFixture
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
import org.junit.Test

class TokoChatMessageViewModelTest : TokoChatViewModelTestFixture() {

    @Test
    fun `when send message should be called exactly once`() {
        runBlocking {
            // Given
            val dummyMessage = "message dummy 123"

            coEvery {
                sendMessageUseCase.sendTextMessage(any(), any(), any())
            } returns Unit

            // When
            viewModel.sendMessage(CHANNEL_ID_DUMMY, dummyMessage)

            // Then
            coVerify(exactly = 1) {
                sendMessageUseCase.sendTextMessage(CHANNEL_ID_DUMMY, dummyMessage, any())
            }
        }
    }
}
