package com.tokopedia.feed.component.product

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCampaign
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXGQLResponse
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXGetActivityProductsResponse
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.domain.mapper.ProductMapper
import com.tokopedia.feedcomponent.domain.usecase.FeedXGetActivityProductsUseCase
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
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
        val activityId = "123456"
        coEvery {
            feedXGetActivityProductsUseCase.getFeedDetailParam(
                activityId,
                ""
            )
        } returns mapOf()
        coEvery { feedXGetActivityProductsUseCase(any()) } throws Throwable("Failed")

        // when
        viewModel.fetchFeedProduct(activityId, emptyList(), ContentTaggedProductUiModel.SourceType.Organic)

        // then
        assert(viewModel.feedTagProductList.value is Fail)
        val response = viewModel.feedTagProductList.value as Fail
        assert(response.throwable.message == "Failed")
    }

    @Test
    fun fetchFeedProduct_whenSuccess_shouldBeSuccess() {
        // given
        val activityId = "123456"
        val dummyData = getDummyData()
        val productList = dummyData.data.products.map {
            ProductMapper.transform(
                it,
                dummyData.data.campaign,
                ContentTaggedProductUiModel.SourceType.Organic
            )
        }
        coEvery {
            feedXGetActivityProductsUseCase.getFeedDetailParam(
                activityId,
                ""
            )
        } returns mapOf()
        coEvery { feedXGetActivityProductsUseCase(any()) } returns getDummyData()

        // when
        viewModel.fetchFeedProduct(activityId, productList, ContentTaggedProductUiModel.SourceType.Organic)

        // then
        assert(viewModel.feedTagProductList.value is Success)
        val response = viewModel.feedTagProductList.value as Success
        assert(response.data.size == getDummyData().data.products.size)
    }

    @Test
    fun fetchFeedProduct_whenSuccessWithoutDefault_shouldBeSuccess() {
        // given
        val activityId = "123456"
        coEvery {
            feedXGetActivityProductsUseCase.getFeedDetailParam(
                activityId,
                ""
            )
        } returns mapOf()
        coEvery { feedXGetActivityProductsUseCase(any()) } returns getDummyData()

        // when
        viewModel.fetchFeedProduct(activityId, emptyList(), ContentTaggedProductUiModel.SourceType.Organic)

        // then
        assert(viewModel.feedTagProductList.value is Success)
        val response = viewModel.feedTagProductList.value as Success
        assert(response.data.size == getDummyData().data.products.size)
        assert(viewModel.shopId == "09876")
    }

    private fun getDummyData(): FeedXGQLResponse = FeedXGQLResponse(
        data = FeedXGetActivityProductsResponse(
            products = listOf(
                FeedXProduct(shopID = "09876"),
                FeedXProduct(),
                FeedXProduct(),
                FeedXProduct(),
                FeedXProduct()
            ),
            isFollowed = true,
            contentType = "content type",
            campaign = FeedXCampaign(),
            nextCursor = "",
            hasVoucher = false
        )
    )
}
