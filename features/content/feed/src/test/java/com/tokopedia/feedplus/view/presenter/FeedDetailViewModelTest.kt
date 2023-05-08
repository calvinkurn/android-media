package com.tokopedia.feedplus.view.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCampaign
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXGQLResponse
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXGetActivityProductsResponse
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedplus.view.repository.FeedDetailRepository
import com.tokopedia.feedplus.view.subscriber.FeedDetailViewState
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
import org.assertj.core.api.Assertions
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
    fun `fetch voucher - success`() {
        val expected = TokopointsCatalogMVCSummaryResponse(
            data = TokopointsCatalogMVCSummary(
                counterTotal = 0,
                animatedInfoList = emptyList(),
                isShown = true,
                resultStatus = ResultStatus(code = HttpURLConnection.HTTP_OK.toString(), message = null, reason = "", status = "")
            )
        )
        coEvery { mockMvcUseCase.getResponse(any()) } returns expected

        viewModel.fetchMerchantVoucherSummary("11")

        viewModel.merchantVoucherSummary.getOrAwaitValue().assertType<Success<TokopointsCatalogMVCSummary>> {}
    }

    @Test
    fun `fetch voucher - failed from gql`() {
        val expected = TokopointsCatalogMVCSummaryResponse(
            data = TokopointsCatalogMVCSummary(
                counterTotal = 0,
                animatedInfoList = emptyList(),
                isShown = true,
                resultStatus = ResultStatus(code = HttpURLConnection.HTTP_BAD_GATEWAY.toString(), message = listOf("Error ya"), reason = "", status = "")
            )
        )
        coEvery { mockMvcUseCase.getResponse(any()) } returns expected

        viewModel.fetchMerchantVoucherSummary("11")

        viewModel.merchantVoucherSummary.getOrAwaitValue().assertType<Fail> {
            it.throwable.assertType<ResponseErrorException> { it.message.assertEqualTo(expected.data?.resultStatus?.message?.firstOrNull().orEmpty()) }
        }
    }

    @Test
    fun `fetch voucher - error`() {
        coEvery { mockMvcUseCase.getResponse(any()) } throws gqlFailed

        viewModel.fetchMerchantVoucherSummary("11")

        viewModel.merchantVoucherSummary.getOrAwaitValue().assertType<Fail> {
            it.throwable.assertEqualTo(gqlFailed)
        }
    }

    @Test
    fun `get detail content by id, if success, then it should return data`() {
        val expectedNextCursor = "nextCursor"
        val expectedResponse = FeedXGQLResponse(
            data = FeedXGetActivityProductsResponse(
                products = listOf(
                    FeedXProduct(id = "1"),
                    FeedXProduct(id = "2")
                ),
                campaign = FeedXCampaign("", "", ""),
                isFollowed = false,
                contentType = "",
                nextCursor = expectedNextCursor,
                hasVoucher = false
            )
        )
        coEvery { mockRepo.fetchFeedDetail(any(), any()) } returns expectedResponse

        viewModel.getFeedDetail("1", 2)

        viewModel.getFeedDetailLiveData().getOrAwaitValue().assertType<FeedDetailViewState.Success> {
            it.feedXGetActivityProductsResponse.assertEqualTo(expectedResponse.data)
        }

        Assertions
            .assertThat(viewModel.cursor)
            .isEqualTo(expectedNextCursor)
    }

    @Test
    fun `get detail content by id, if success and product is empty, then it should return success with no data`() {
        coEvery { mockRepo.fetchFeedDetail(any(), any()) } returns FeedXGQLResponse(
            data = FeedXGetActivityProductsResponse(
                products = emptyList(),
                campaign = FeedXCampaign("", "", ""),
                isFollowed = false,
                contentType = "",
                nextCursor = "",
                hasVoucher = false
            )
        )

        viewModel.getFeedDetail("1", 1)

        viewModel.getFeedDetailLiveData().getOrAwaitValue().assertType<FeedDetailViewState.SuccessWithNoData> {}
    }

    @Test
    fun `get detail content by id, if error, then it should return error`() {
        val expectedThrowable = Throwable()
        coEvery { mockRepo.fetchFeedDetail(any(), any()) } throws expectedThrowable

        viewModel.getFeedDetail("1", 1)

        viewModel.getFeedDetailLiveData().getOrAwaitValue().assertType<FeedDetailViewState.Error> {
            it.error.assertEqualTo(expectedThrowable)
        }
    }

    @Test
    fun `get detail content by id and not from the first page, if error, then it should return error`() {
        val expectedThrowable = Throwable()
        coEvery { mockRepo.fetchFeedDetail(any(), any()) } throws expectedThrowable

        viewModel.getFeedDetail("1", 2)

        viewModel.getFeedDetailLiveData().getOrAwaitValue().assertType<FeedDetailViewState.Error> {
            it.error.assertEqualTo(expectedThrowable)
        }
    }
}
