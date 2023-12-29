 
package com.tokopedia.tokochat.test.chatroom

import com.tokopedia.tokochat.base.TokoChatViewModelTestFixture
import com.tokopedia.tokochat.view.chatroom.TokoChatViewModel.Companion.DELAY_UPDATE_ORDER_STATE
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class TokoChatConnectionViewModelTest : TokoChatViewModelTestFixture() {

    @Test
    fun `when doCheckChatConnection should set the connection check job and set the value`() {
        runTest {
            // Given
            coEvery {
                tokoChatRoomUseCase.isChatConnected()
            } returns true

            // When
            viewModel.doCheckChatConnection()
            advanceTimeBy(DELAY_UPDATE_ORDER_STATE + 1000) // first delay

            // Given
            coEvery {
                tokoChatRoomUseCase.isChatConnected()
            } returns false
            advanceTimeBy(DELAY_UPDATE_ORDER_STATE + 1000) // second delay

            // Then
            coVerify(exactly = 1) {
                viewModel.cancelCheckConnection()
            }

            coVerify {
                tokoChatRoomUseCase.isChatConnected()
            }

            Assert.assertEquals(false, viewModel.isChatConnected.value)
        }
    }

    @Test
    fun `when cancelCheckConnection should set connectionCheckJob as null`() {
        runTest {
            // Given
            val connectionDummy = true
            coEvery {
                tokoChatRoomUseCase.isChatConnected()
            } returns connectionDummy

            // When
            viewModel.doCheckChatConnection()
            viewModel.cancelCheckConnection()

            // Then
            Assert.assertEquals(null, viewModel.connectionCheckJob)
        }
    }

    @Test
    fun `when currentCoroutineContext on doCheckChatConnection is not active should not continue check chat connected`() {
        runTest {
            // Given
            every {
                tokoChatRoomUseCase.isChatConnected()
            } returns true
            // When
            viewModel.doCheckChatConnection()
            viewModel.connectionCheckJob?.cancel()
            advanceTimeBy(DELAY_UPDATE_ORDER_STATE + 1000)

            // Then
            coVerify(exactly = 1) {
                tokoChatRoomUseCase.isChatConnected()
            }
        }
    }

    @Test
    fun `when currentCoroutineContext is inactive on doCheckChatConnection while looping should not check chat connected anymore`() {
        runTest {
            // When
            viewModel.doCheckChatConnection()
            advanceTimeBy(DELAY_UPDATE_ORDER_STATE + 1000) // Wait until delay end

            viewModel.connectionCheckJob?.cancel()
            advanceTimeBy(DELAY_UPDATE_ORDER_STATE + 1000) // Wait until delay end

            // Then
            coVerify(exactly = 1) {
                tokoChatRoomUseCase.isChatConnected()
            }
        }
    }

    @Test
    fun `set connection check job`() {
        runTest {
            // Given
            val dummyJob = mockk<Job>(relaxed = true)

            // When
            viewModel.connectionCheckJob = dummyJob

            // Then
            Assert.assertEquals(dummyJob, viewModel.connectionCheckJob)
        }
    }

    @Test
    fun `when failed doCheckChatConnection should not intervene with chatroom`() {
        runTest {
            // Given
            coEvery {
                tokoChatRoomUseCase.isChatConnected()
            } throws throwableDummy

            // When
            viewModel.doCheckChatConnection()
            advanceTimeBy(DELAY_UPDATE_ORDER_STATE + 1000)

            // Then
            Assert.assertEquals(
                true,
                viewModel.isChatConnected.value
            )
        }
    }
}
