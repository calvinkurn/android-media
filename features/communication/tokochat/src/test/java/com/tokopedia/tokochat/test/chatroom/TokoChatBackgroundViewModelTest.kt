package com.tokopedia.tokochat.test.chatroom

import com.tokopedia.tokochat.base.TokoChatViewModelTestFixture
import com.tokopedia.tokochat.common.util.TokoChatUrlUtil
import com.tokopedia.tokochat.utils.observeAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class TokoChatBackgroundViewModelTest : TokoChatViewModelTestFixture() {
    @Test
    fun `when getTokoChatBackground, this method should return livedata success`() {
        runTest {
            val expectedImageUrl = TokoChatUrlUtil.IC_TOKOFOOD_SOURCE

            // given
            coEvery {
                getTokoChatBackgroundUseCase(Unit)
            } returns flowOf(expectedImageUrl)

            // when
            viewModel.getTokoChatBackground()

            // then
            coVerify {
                getTokoChatBackgroundUseCase(Unit)
            }

            val actualResult = (viewModel.chatBackground.observeAwaitValue() as Success).data

            Assert.assertEquals(expectedImageUrl, actualResult)
        }
    }

    @Test
    fun `when getTokoChatBackground, this method should return livedata fail`() {
        runTest {
            val errorException = Throwable()

            // given
            coEvery {
                getTokoChatBackgroundUseCase(Unit)
            } throws errorException

            // when
            viewModel.getTokoChatBackground()

            // then
            coVerify {
                getTokoChatBackgroundUseCase(Unit)
            }

            val actualResult = (viewModel.chatBackground.observeAwaitValue() as Fail).throwable::class.java

            val expectedResult = errorException::class.java
            Assert.assertEquals(expectedResult, actualResult)
        }
    }
}
