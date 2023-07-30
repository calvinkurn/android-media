package com.tokopedia.tokochat.test.chatroom

import com.tokopedia.tokochat.base.TokoChatViewModelTestFixture
import com.tokopedia.tokochat.utils.observeAwaitValue
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class TokoChatConnectionViewModelTest : TokoChatViewModelTestFixture() {

    @Test
    fun `when doCheckChatConnection should set the connection check job`() {
        runBlocking {
            // Given
            val connectionDummy = true
            coEvery {
                getChannelUseCase.isChatConnected()
            } returns connectionDummy

            // When
            viewModel.doCheckChatConnection()

            val result = viewModel.isChatConnected.observeAwaitValue(time = 6000)

            // Then
            coVerify(exactly = 1) {
                viewModel.cancelCheckConnection()
            }

            coVerify {
                getChannelUseCase.isChatConnected()
            }

            Assert.assertEquals(connectionDummy, result)
            Assert.assertNotEquals(null, viewModel.connectionCheckJob)
        }
    }

    @Test
    fun `when cancelCheckConnection should set connectionCheckJob as null`() {
        runBlocking {
            // Given
            val connectionDummy = true
            coEvery {
                getChannelUseCase.isChatConnected()
            } returns connectionDummy

            // When
            viewModel.doCheckChatConnection()
            viewModel.cancelCheckConnection()

            // Then
            Assert.assertEquals(null, viewModel.connectionCheckJob)
        }
    }

    @Test
    fun `when currentCoroutineContext on doCheckChatConnection is not active should not check chat connected`() {
        runBlocking {
            // When
            viewModel.doCheckChatConnection()
            viewModel.connectionCheckJob?.cancel()
            Thread.sleep(6000) // Wait until delay end

            // Then
            coVerify(exactly = 0) {
                getChannelUseCase.isChatConnected()
            }
        }
    }

    @Test
    fun `when currentCoroutineContext is inactive on doCheckChatConnection while looping should not check chat connected anymore`() {
        runBlocking {
            // When
            viewModel.doCheckChatConnection()
            Thread.sleep(6000) // Wait until delay end

            viewModel.connectionCheckJob?.cancel()
            Thread.sleep(6000) // Wait until delay end

            // Then
            coVerify(exactly = 1) {
                getChannelUseCase.isChatConnected()
            }
        }
    }

    @Test
    fun `set connection check job`() {
        runBlocking {
            // Given
            val dummyJob = mockk<Job>(relaxed = true)

            // When
            viewModel.connectionCheckJob = dummyJob

            // Then
            Assert.assertEquals(dummyJob, viewModel.connectionCheckJob)
        }
    }

    @Test
    fun `when failed doCheckChatConnection should give throwable on error livedata`() {
        runBlocking {
            // Given
            coEvery {
                getChannelUseCase.isChatConnected()
            } throws throwableDummy

            // When
            viewModel.doCheckChatConnection()
            Thread.sleep(6000)

            // Then
            Assert.assertEquals(
                false,
                viewModel.isChatConnected.observeAwaitValue()
            )
        }
    }
}
