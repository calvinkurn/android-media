package feedcomponent.product

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.feed.component.product.FeedTaggedProductUiModel
import com.tokopedia.feed.component.product.FeedTaggedProductViewModel
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCampaign
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXGQLResponse
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXGetActivityProductsResponse
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.domain.usecase.FeedXGetActivityProductsUseCase
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import feedcomponent.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Muhammad Furqan on 11/07/23
 */
internal class FeedTaggedProductViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineTestRule = UnconfinedTestRule()

    private val feedXGetActivityProductsUseCase: FeedXGetActivityProductsUseCase = mockk()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = coroutineTestRule.dispatchers

    private lateinit var viewModel: FeedTaggedProductViewModel

    private val activityId = "123456"

    @Before
    fun setUp() {
        viewModel = FeedTaggedProductViewModel(
            feedXGetActivityProductsUseCase = feedXGetActivityProductsUseCase,
            dispatchers = testDispatcher
        )
    }

    @Test
    fun onInit() {
        assert(viewModel.shopId.isEmpty())
    }

    @Test
    fun fetchFeedProduct_whenFailed_shouldBeFail() {
        // given
        coEvery {
            feedXGetActivityProductsUseCase.getFeedDetailParam(
                activityId,
                ""
            )
        } returns mapOf()
        coEvery { feedXGetActivityProductsUseCase(any()) } throws Throwable("Failed")

        // when
        viewModel.fetchFeedProduct(activityId, FeedTaggedProductUiModel.SourceType.Organic)

        // then
        assert(viewModel.feedTagProductList.value is Fail)
        val response = viewModel.feedTagProductList.value as Fail
        assert(response.throwable.message == "Failed")
    }

    @Test
    fun fetchFeedProduct_whenSuccess_shouldBeSuccess() {
        // given
        coEvery {
            feedXGetActivityProductsUseCase.getFeedDetailParam(
                activityId,
                ""
            )
        } returns mapOf()
        coEvery { feedXGetActivityProductsUseCase(any()) } returns getDummyData()

        // when
        viewModel.fetchFeedProduct(activityId, FeedTaggedProductUiModel.SourceType.Organic)

        // then
        assert(viewModel.feedTagProductList.value is Success)
        val response = viewModel.feedTagProductList.value as Success
        assert(response.data.size == getDummyData().data.products.size)
        assert(viewModel.shopId == "09876")
    }

    @Test
    fun non_organic_if_stock_available_return_stock_available() {
        val expected = List(4) { buildProduct(isStockAvailable = true) }
        coEvery {
            feedXGetActivityProductsUseCase.getFeedDetailParam(
                activityId,
                ""
            )
        } returns mapOf()
        coEvery { feedXGetActivityProductsUseCase(any()) } returns FeedXGQLResponse(
            data = FeedXGetActivityProductsResponse(
                products = expected
            )
        )

        viewModel.fetchFeedProduct(activityId, FeedTaggedProductUiModel.SourceType.NonOrganic)

        assert(viewModel.feedTagProductList.value is Success)
        val response = viewModel.feedTagProductList.value as Success
        assert(response.data.size == expected.size)

        (viewModel.feedTagProductList.getOrAwaitValue() as Success).data.forEach {
            assert(it.stock is FeedTaggedProductUiModel.Stock.Available)
        }
    }

    @Test
    fun non_organic_if_stock_not_available_return_stock_available() {
        val expected = List(4) { buildProduct(isStockAvailable = false) }
        coEvery {
            feedXGetActivityProductsUseCase.getFeedDetailParam(
                activityId,
                ""
            )
        } returns mapOf()
        coEvery { feedXGetActivityProductsUseCase(any()) } returns FeedXGQLResponse(
            data = FeedXGetActivityProductsResponse(
                products = expected
            )
        )

        viewModel.fetchFeedProduct(activityId, FeedTaggedProductUiModel.SourceType.NonOrganic)

        assert(viewModel.feedTagProductList.value is Success)
        val response = viewModel.feedTagProductList.value as Success
        assert(response.data.size == expected.size)

        (viewModel.feedTagProductList.getOrAwaitValue() as Success).data.forEach {
            assert(it.stock is FeedTaggedProductUiModel.Stock.Available)
        }
    }

    @Test
    fun organic_if_stock_available_return_stock_available() {
        val expected = List(4) { buildProduct(isStockAvailable = true) }
        coEvery {
            feedXGetActivityProductsUseCase.getFeedDetailParam(
                activityId,
                ""
            )
        } returns mapOf()
        coEvery { feedXGetActivityProductsUseCase(any()) } returns FeedXGQLResponse(
            data = FeedXGetActivityProductsResponse(
                products = expected
            )
        )

        viewModel.fetchFeedProduct(activityId, FeedTaggedProductUiModel.SourceType.Organic)

        assert(viewModel.feedTagProductList.value is Success)
        val response = viewModel.feedTagProductList.value as Success
        assert(response.data.size == expected.size)

        (viewModel.feedTagProductList.getOrAwaitValue() as Success).data.forEach {
            assert(it.stock is FeedTaggedProductUiModel.Stock.Available)
        }
    }

    @Test
    fun organic_if_stock_not_available_return_out_of_stock() {
        val expected = List(4) { buildProduct(isStockAvailable = false) }
        coEvery {
            feedXGetActivityProductsUseCase.getFeedDetailParam(
                activityId,
                ""
            )
        } returns mapOf()
        coEvery { feedXGetActivityProductsUseCase(any()) } returns FeedXGQLResponse(
            data = FeedXGetActivityProductsResponse(
                products = expected
            )
        )

        viewModel.fetchFeedProduct(activityId, FeedTaggedProductUiModel.SourceType.Organic)

        assert(viewModel.feedTagProductList.value is Success)
        val response = viewModel.feedTagProductList.value as Success
        assert(response.data.size == expected.size)

        (viewModel.feedTagProductList.getOrAwaitValue() as Success).data.forEach {
            assert(it.stock is FeedTaggedProductUiModel.Stock.OutOfStock)
        }
    }

    private fun getDummyData(): FeedXGQLResponse = FeedXGQLResponse(
        data = FeedXGetActivityProductsResponse(
            products = listOf(
                FeedXProduct(shopID = "09876"),
                FeedXProduct(),
                FeedXProduct(),
                FeedXProduct(),
                FeedXProduct(),
            ),
            isFollowed = true,
            contentType = "content type",
            campaign = FeedXCampaign(),
            nextCursor = "",
            hasVoucher = false
        )
    )

    private fun buildProduct(
        isStockAvailable: Boolean = true,
        name: String = "Kolak",
        price: String = "Rp 10.000"
    ) =
        FeedXProduct(
            appLink = "",
            isAvailable = isStockAvailable, name = name, priceOriginalFmt = price
        )
}
