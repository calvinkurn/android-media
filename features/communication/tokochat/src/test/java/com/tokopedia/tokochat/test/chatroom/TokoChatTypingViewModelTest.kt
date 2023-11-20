package com.tokopedia.tokochat.test.chatroom

import androidx.lifecycle.MutableLiveData
import com.tokopedia.tokochat.base.TokoChatViewModelTestFixture
import com.tokopedia.tokochat.utils.observeAwaitValue
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class TokoChatTypingViewModelTest : TokoChatViewModelTestFixture() {

    @Test
    fun `when getTypingStatus should call getTypingStatusCallback (from SDK) once and return typing status`() {
        runBlocking {
            // Given
            val typingStatusListDummy = listOf("123", "456")
            coEvery {
                getTypingUseCase.getTypingStatus()
            } returns MutableLiveData(typingStatusListDummy)

            // When
            val result = viewModel.getTypingStatus()?.observeAwaitValue()

            // Then
            coVerify(exactly = 1) {
                getTypingUseCase.getTypingStatus()
            }
            Assert.assertEquals(typingStatusListDummy, result)
        }
    }

    @Test
    fun `when failed to getTypingStatus should call give throwable on error livedata`() {
        runBlocking {
            // Given
            coEvery {
                getTypingUseCase.getTypingStatus()
            } throws throwableDummy

            // When
            viewModel.getTypingStatus()

            // Then
            Assert.assertEquals(
                throwableDummy,
                viewModel.error.observeAwaitValue()?.first
            )
        }
    }

    @Test
    fun `when setTypingStatus should call setTypingStatus (from SDK) once`() {
        runBlocking {
            // Given
            val typingStatusDummy = true

            // When
            viewModel.setTypingStatus(typingStatusDummy)

            // Then
            coVerify(exactly = 1) {
                getTypingUseCase.setTypingStatus(typingStatusDummy)
            }
        }
    }

    @Test
    fun `when failed to setTypingStatus should call give throwable on error livedata`() {
        runBlocking {
            // Given
            val typingStatusDummy = true

            coEvery {
                getTypingUseCase.setTypingStatus(any())
            } throws throwableDummy

            // When
            viewModel.setTypingStatus(typingStatusDummy)

            // Then
            Assert.assertEquals(
                throwableDummy,
                viewModel.error.observeAwaitValue()?.first
            )
        }
    }

    @Test
    fun `when resetTypingStatus should call resetTypingStatusCallback (from SDK) once`() {
        runBlocking {
            // When
            viewModel.resetTypingStatus()

            // Then
            coVerify(exactly = 1) {
                getTypingUseCase.resetTypingStatus()
            }
        }
    }

    @Test
    fun `when failed to resetTypingStatus should call give throwable on error livedata`() {
        runBlocking {
            // Given
            coEvery {
                getTypingUseCase.resetTypingStatus()
            } throws throwableDummy

            // When
            viewModel.resetTypingStatus()

            // Then
            Assert.assertEquals(
                throwableDummy,
                viewModel.error.observeAwaitValue()?.first
            )
        }
    }
}
