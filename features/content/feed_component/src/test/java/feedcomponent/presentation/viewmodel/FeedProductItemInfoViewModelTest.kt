package feedcomponent.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCampaign
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXGQLResponse
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXGetActivityProductsResponse
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.domain.usecase.FeedXGetActivityProductsUseCase
import com.tokopedia.feedcomponent.presentation.utils.FeedXProductResult
import com.tokopedia.feedcomponent.presentation.viewmodel.FeedProductItemInfoViewModel
import com.tokopedia.mvcwidget.usecases.MVCSummaryUseCase
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Muhammad Furqan on 08/08/23
 */
class FeedProductItemInfoViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineTestRule = UnconfinedTestRule()

    private val mvcSummaryUseCase: MVCSummaryUseCase = mockk()
    private val feedXGetActivityProductsUseCase: FeedXGetActivityProductsUseCase = mockk()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = coroutineTestRule.dispatchers

    private lateinit var viewModel: FeedProductItemInfoViewModel

    @Before
    fun setUp() {
        viewModel = FeedProductItemInfoViewModel(
            mvcSummaryUseCase,
            feedXGetActivityProductsUseCase,
            testDispatcher
        )
    }

    @Test
    fun whenGetPagingLiveData_shouldGetLastPagingLiveData() {
        assert(viewModel.getPagingLiveData().value == null)
    }

    @Test
    fun fetchFeedXProductsData_whenFailed_shouldReturnFail() {
        coEvery { feedXGetActivityProductsUseCase.getFeedDetailParam(any(), any()) } returns mapOf()
        coEvery { feedXGetActivityProductsUseCase(any()) } throws Throwable("Failed")

        viewModel.fetchFeedXProductsData()

        assert(viewModel.feedProductsResponse.value is FeedXProductResult.Error)
        assert((viewModel.feedProductsResponse.value as FeedXProductResult.Error).error.message == "Failed")
    }

    @Test
    fun fetchFeedXProductsData_whenSuccess_withoutProduct() {
        val dummyNextCursor = "dummyNextCursor"
        coEvery { feedXGetActivityProductsUseCase.getFeedDetailParam(any(), any()) } returns mapOf()
        coEvery { feedXGetActivityProductsUseCase(any()) } returns FeedXGQLResponse(
            data = FeedXGetActivityProductsResponse(
                products = emptyList(),
                isFollowed = true,
                contentType = "",
                campaign = FeedXCampaign(),
                nextCursor = dummyNextCursor,
                hasVoucher = false
            )
        )

        viewModel.fetchFeedXProductsData()

        assert(viewModel.feedProductsResponse.value is FeedXProductResult.SuccessWithNoData)
    }

    @Test
    fun fetchFeedXProductsDataNextPage_whenSuccess_withoutProduct() {
        val dummyNextCursor = "dummyNextCursor"
        val page = 2
        coEvery { feedXGetActivityProductsUseCase.getFeedDetailParam(any(), any()) } returns mapOf()
        coEvery { feedXGetActivityProductsUseCase(any()) } returns FeedXGQLResponse(
            data = FeedXGetActivityProductsResponse(
                products = emptyList(),
                isFollowed = true,
                contentType = "",
                campaign = FeedXCampaign(),
                nextCursor = dummyNextCursor,
                hasVoucher = false
            )
        )

        viewModel.fetchFeedXProductsData(page = page)

        assert(viewModel.feedProductsResponse.value is FeedXProductResult.LoadingState)
        assert(!(viewModel.feedProductsResponse.value as FeedXProductResult.LoadingState).isLoading)
        assert((viewModel.feedProductsResponse.value as FeedXProductResult.LoadingState).loadingMore)
    }

    @Test
    fun fetchFeedXProductsData_whenSuccess_shouldReturnSuccess() {
        val page = 2
        val dummyNextCursor = "dummyNextCursor"
        val dummyProductResponse = FeedXGetActivityProductsResponse(
            products = listOf(FeedXProduct()),
            nextCursor = dummyNextCursor
        )

        coEvery { feedXGetActivityProductsUseCase.getFeedDetailParam(any(), any()) } returns mapOf()
        coEvery { feedXGetActivityProductsUseCase(any()) } returns FeedXGQLResponse(
            data = dummyProductResponse
        )

        viewModel.fetchFeedXProductsData(page = page)

        assert(viewModel.getPagingLiveData().value == page + 1)
        assert(viewModel.feedProductsResponse.value is FeedXProductResult.Success)
        val data = viewModel.feedProductsResponse.value as FeedXProductResult.Success
        assert(data.feedXGetActivityProductsResponse == dummyProductResponse)
    }

    @Test
    fun showProductsFromPost() {
        val products = listOf(FeedXProduct(id = "1"), FeedXProduct(id = "1"))

        viewModel.showProductsFromPost(products)

        assert(viewModel.feedProductsResponse.value is FeedXProductResult.Success)
        val data = (viewModel.feedProductsResponse.value as FeedXProductResult.Success)
        assert(data.feedXGetActivityProductsResponse.products.size == products.size)

        products.forEachIndexed { index, item ->
            val product = data.feedXGetActivityProductsResponse.products[index]
            assert(product.id == item.id)
        }
    }
}
