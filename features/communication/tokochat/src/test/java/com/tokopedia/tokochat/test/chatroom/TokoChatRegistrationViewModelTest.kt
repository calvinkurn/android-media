package com.tokopedia.tokochat.test.chatroom

import com.tokopedia.tokochat.base.TokoChatViewModelTestFixture
import com.tokopedia.tokochat.utils.observeAwaitValue
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class TokoChatRegistrationViewModelTest : TokoChatViewModelTestFixture() {

    @Test
    fun `when registerActiveChannel should call softRegisterChannel (from SDK) once`() {
        runTest {
            // When
            viewModel.registerActiveChannel(CHANNEL_ID_DUMMY)

            // Then
            coVerify(exactly = 1) {
                registrationChannelUseCase.registerActiveChannel(CHANNEL_ID_DUMMY)
            }
        }
    }

    @Test
    fun `when failed to registerActiveChannel should call give throwable on error livedata`() {
        runTest {
            // Given
            coEvery {
                registrationChannelUseCase.registerActiveChannel(any())
            } throws throwableDummy

            // When
            viewModel.registerActiveChannel(CHANNEL_ID_DUMMY)

            // Then
            Assert.assertEquals(
                throwableDummy,
                viewModel.error.observeAwaitValue()?.first
            )
        }
    }

    @Test
    fun `when deRegisterActiveChannel should call softDeregisterChannel (from SDK) once`() {
        runTest {
            // When
            viewModel.deRegisterActiveChannel(CHANNEL_ID_DUMMY)

            // Then
            coVerify(exactly = 1) {
                registrationChannelUseCase.deRegisterActiveChannel(CHANNEL_ID_DUMMY)
            }
        }
    }

    @Test
    fun `when failed to deRegisterActiveChannel should call give throwable on error livedata`() {
        runTest {
            // Given
            coEvery {
                registrationChannelUseCase.deRegisterActiveChannel(any())
            } throws throwableDummy

            // When
            viewModel.deRegisterActiveChannel(CHANNEL_ID_DUMMY)

            // Then
            Assert.assertEquals(
                throwableDummy,
                viewModel.error.observeAwaitValue()?.first
            )
        }
    }
}
