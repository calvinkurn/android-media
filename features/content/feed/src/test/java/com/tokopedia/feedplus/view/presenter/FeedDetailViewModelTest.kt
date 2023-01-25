package com.tokopedia.feedplus.view.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.feedplus.view.repository.FeedDetailRepository
import com.tokopedia.mvcwidget.ResultStatus
import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummary
import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummaryResponse
import com.tokopedia.mvcwidget.usecases.MVCSummaryUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokopedia.feedplus.helper.assertEqualTo
import com.tokopedia.tokopedia.feedplus.helper.assertType
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.HttpURLConnection

/**
 * @author by astidhiyaa on 27/09/22
 */
@ExperimentalCoroutinesApi
class FeedDetailViewModelTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val testDispatcher = coroutineTestRule.dispatchers

    private lateinit var viewModel: FeedDetailViewModel

    private val mockRepo: FeedDetailRepository = mockk(relaxed = true)

    private val mockMvcUseCase: MVCSummaryUseCase = mockk(relaxed = true)

    private val gqlFailed = MessageErrorException("ooPs")


    @Before
    fun setUp() {
        viewModel =
            FeedDetailViewModel(feedDetailRepository = mockRepo, mvcSummaryUseCase = mockMvcUseCase, dispatcherProvider = testDispatcher)
    }

    /**
     * fetch voucher
     */

    @Test
    fun `fetch voucher - success` (){
        val expected = TokopointsCatalogMVCSummaryResponse(data = TokopointsCatalogMVCSummary(counterTotal = 0, animatedInfoList = emptyList(), isShown = true,
            resultStatus = ResultStatus(code = HttpURLConnection.HTTP_OK.toString(), message = null, reason = "", status = "")))
        coEvery { mockMvcUseCase.getResponse(any()) } returns expected

        viewModel.fetchMerchantVoucherSummary("11")

        viewModel.merchantVoucherSummary.getOrAwaitValue().assertType<Success<TokopointsCatalogMVCSummary>> {}
    }

    @Test
    fun `fetch voucher - failed from gql` (){
        val expected = TokopointsCatalogMVCSummaryResponse(data = TokopointsCatalogMVCSummary(counterTotal = 0, animatedInfoList = emptyList(), isShown = true,
            resultStatus = ResultStatus(code = HttpURLConnection.HTTP_BAD_GATEWAY.toString(), message = listOf("Error ya"), reason = "", status = "")))
        coEvery { mockMvcUseCase.getResponse(any()) } returns expected

        viewModel.fetchMerchantVoucherSummary("11")

        viewModel.merchantVoucherSummary.getOrAwaitValue().assertType<Fail> {
            it.throwable.assertType<ResponseErrorException> { it.message.assertEqualTo(expected.data?.resultStatus?.message?.firstOrNull().orEmpty())  }
        }
    }

    @Test
    fun `fetch voucher - error` (){
        coEvery { mockMvcUseCase.getResponse(any()) } throws gqlFailed

        viewModel.fetchMerchantVoucherSummary("11")

        viewModel.merchantVoucherSummary.getOrAwaitValue().assertType<Fail> {
            it.throwable.assertEqualTo(gqlFailed)
        }
    }
}
