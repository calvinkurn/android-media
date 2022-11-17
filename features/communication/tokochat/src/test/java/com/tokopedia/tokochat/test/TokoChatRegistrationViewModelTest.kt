package com.tokopedia.tokochat.test

import com.tokopedia.tokochat.base.TokoChatViewModelTestFixture
import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
import org.junit.Test

class TokoChatRegistrationViewModelTest : TokoChatViewModelTestFixture() {

    @Test
    fun `when registerActiveChannel should call softRegisterChannel (from SDK) once`() {
        runBlocking {
            // When
            viewModel.registerActiveChannel(CHANNEL_ID_DUMMY)

            // Then
            coVerify(exactly = 1) {
                registrationChannelUseCase.registerActiveChannel(CHANNEL_ID_DUMMY)
            }
        }
    }

    @Test
    fun `when deRegisterActiveChannel should call softDeregisterChannel (from SDK) once`() {
        runBlocking {
            // When
            viewModel.deRegisterActiveChannel(CHANNEL_ID_DUMMY)

            // Then
            coVerify(exactly = 1) {
                registrationChannelUseCase.deRegisterActiveChannel(CHANNEL_ID_DUMMY)
            }
        }
    }
}
