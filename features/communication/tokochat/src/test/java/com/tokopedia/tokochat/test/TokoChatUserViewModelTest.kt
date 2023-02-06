package com.tokopedia.tokochat.test

import androidx.lifecycle.MutableLiveData
import com.tokopedia.tokochat.base.TokoChatViewModelTestFixture
import com.tokopedia.tokochat.utils.observeAwaitValue
import io.mockk.coEvery
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class TokoChatUserViewModelTest : TokoChatViewModelTestFixture() {

    @Test
    fun `when getMemberLeft should give LiveData String of member left`() {
        runBlocking {
            // Given
            val memberLeftLiveDataDummy = MutableLiveData("123")
            coEvery {
                getChannelUseCase.getMemberLeftLiveData()
            } returns memberLeftLiveDataDummy

            // When
            val result = viewModel.getMemberLeft()

            // Then
            Assert.assertEquals(memberLeftLiveDataDummy, result)
        }
    }

    @Test
    fun `when failed to getMemberLeft should give LiveData String of member left`() {
        runBlocking {
            // Given
            coEvery {
                getChannelUseCase.getMemberLeftLiveData()
            } throws throwableDummy

            // When
            viewModel.getMemberLeft()

            // Then
            Assert.assertEquals(
                throwableDummy,
                viewModel.error.observeAwaitValue()?.first
            )
        }
    }

    @Test
    fun `when getUserId should give user id as non empty string`() {
        runBlocking {
            // Given
            coEvery {
                registrationChannelUseCase.getUserId()
            } returns USER_ID_DUMMY

            // When
            val result = viewModel.getUserId()

            // Then
            Assert.assertEquals(USER_ID_DUMMY, result)
        }
    }

    @Test
    fun `when reset member left livedata should return null`() {
        runBlocking {
            // Given
            coEvery {
                getChannelUseCase.getMemberLeftLiveData()
            } returns MutableLiveData(null)

            // When
            viewModel.resetMemberLeft()
            val result = viewModel.getMemberLeft().value

            // Then
            Assert.assertEquals(null, result)
        }
    }
}
