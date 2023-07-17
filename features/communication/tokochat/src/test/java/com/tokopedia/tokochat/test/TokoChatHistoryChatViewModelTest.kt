package com.tokopedia.tokochat.test

import androidx.lifecycle.MutableLiveData
import com.gojek.conversations.database.chats.ConversationsMessage
import com.tokopedia.tokochat.base.TokoChatViewModelTestFixture
import com.tokopedia.tokochat.utils.observeAwaitValue
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class TokoChatHistoryChatViewModelTest : TokoChatViewModelTestFixture() {

    @Test
    fun `when getChatHistory should return List of ConversationsMessage`() {
        runBlocking {
            // Given
            val dummyListMessage = listOf<ConversationsMessage>()

            coEvery {
                getChatHistoryUseCase(any())
            } returns MutableLiveData(dummyListMessage)

            // When
            val result = viewModel.getChatHistory(CHANNEL_ID_DUMMY)?.observeAwaitValue()

            // Then
            Assert.assertEquals(dummyListMessage, result)
        }
    }

    @Test
    fun `when failed to getChatHistory should give throwable in error livedata`() {
        runBlocking {
            // Given
            coEvery {
                getChatHistoryUseCase(any())
            } throws throwableDummy

            // When
            viewModel.getChatHistory(CHANNEL_ID_DUMMY)?.observeAwaitValue()

            // Then
            Assert.assertEquals(
                throwableDummy,
                viewModel.error.observeAwaitValue()?.first
            )
        }
    }

    @Test
    fun `when loadPreviousMessages should call loadPreviousMessage (from SDK) once`() {
        runBlocking {
            // When
            viewModel.loadPreviousMessages()

            // Then
            coVerify(exactly = 1) {
                getChatHistoryUseCase.loadPreviousMessage()
            }
        }
    }

    @Test
    fun `when failed to loadPreviousMessages should give throwable in error livedata`() {
        runBlocking {
            // Given
            coEvery {
                getChatHistoryUseCase.loadPreviousMessage()
            } throws throwableDummy

            // When
            viewModel.loadPreviousMessages()

            // Then
            Assert.assertEquals(
                throwableDummy,
                viewModel.error.observeAwaitValue()?.first
            )
        }
    }
}
