package com.tokopedia.sellerreview.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerreview.domain.usecase.SendReviewUseCase
import com.tokopedia.sellerreview.view.model.SendReviewParam
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created By @ilhamsuaib on 29/01/21
 */

@ExperimentalCoroutinesApi
class ReviewViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @RelaxedMockK
    lateinit var sendReviewUseCase: SendReviewUseCase

    private lateinit var viewModel: SellerReviewViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = SellerReviewViewModel({ sendReviewUseCase }, coroutineTestRule.dispatchers)
    }

    @Test
    fun `should success when submit review then notify the UI to update success state`() = coroutineTestRule.runBlockingTest {
        val param = SendReviewParam("12345", 4)
        sendReviewUseCase.params = SendReviewUseCase.createParams(param)

        coEvery {
            sendReviewUseCase.executeOnBackground()
        } returns true

        viewModel.submitReview(param)

        coVerify {
            sendReviewUseCase.executeOnBackground()
        }

        assert(viewModel.reviewStatus.value == Success(true))
    }

    @Test
    fun `should failed when submit review then notify the UI to update error state`() = coroutineTestRule.runBlockingTest {

        val throwable = MessageErrorException("error")
        val param = SendReviewParam("12345", 4)
        sendReviewUseCase.params = SendReviewUseCase.createParams(param)

        coEvery {
            sendReviewUseCase.executeOnBackground()
        } throws throwable

        viewModel.submitReview(param)

        coVerify {
            sendReviewUseCase.executeOnBackground()
        }

        assert(viewModel.reviewStatus.value == Fail(throwable))
    }
}