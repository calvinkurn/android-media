package com.tokopedia.tokofood.category

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokofood.data.createMerchantListEmptyPageResponse
import com.tokopedia.tokofood.data.createMerchantListEmptyResponse
import com.tokopedia.tokofood.data.createMerchantListModel1
import com.tokopedia.tokofood.data.createMerchantListModel2
import com.tokopedia.tokofood.data.createMerchantListResponse
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodLayoutState
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodErrorStateUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodListUiModel
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test

@FlowPreview
@ExperimentalCoroutinesApi
class TokoFoodCategoryViewModelTest : TokoFoodCategoryViewModelTestFixture() {

    @Test
    fun `when getting error state should run and give the error result`() {
        runBlockingTest {
            val throwable = Throwable("Error Timeout")

            val result = async {
                viewModel.flowLayoutList.first()
            }

            viewModel.setErrorState(throwable)

            val categoryLayoutList = result.await() as Pair<Success<TokoFoodListUiModel>, Boolean>
            val actualResponse =
                categoryLayoutList.first.data.items.find { it is TokoFoodErrorStateUiModel }
            Assert.assertNotNull(actualResponse)
            Assert.assertFalse(categoryLayoutList.second)
            result.cancel()
        }
    }

    @Test
    fun `when getting category page should run and give the merchant list result`() {
        onGetMerchantList_thenReturn(createMerchantListEmptyPageResponse(), pageKey = "0")

        val expectedResult = TokoFoodListUiModel(
            items = listOf(
                createMerchantListModel1(),
                createMerchantListModel2()
            ), TokoFoodLayoutState.SHOW
        )
        var actualResponse: Pair<Result<TokoFoodListUiModel>, Boolean>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setCategoryLayout(LocalCacheModel())
            collectorJob.cancel()
        }
        Assert.assertEquals(expectedResult, (actualResponse?.first as Success).data)
        actualResponse?.second?.let {
            Assert.assertTrue(it)
        }
    }

    @Test
    fun `when getting empty category page should run and give the empty state result`() {
        onGetMerchantList_thenReturn(createMerchantListEmptyResponse())

        val expectedResult =
            TokoFoodListUiModel(items = viewModel.categoryLayoutItemList, TokoFoodLayoutState.SHOW)
        var actualResponse: Pair<Result<TokoFoodListUiModel>, Boolean>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setCategoryLayout(LocalCacheModel())
            collectorJob.cancel()
        }
        Assert.assertEquals(expectedResult, (actualResponse?.first as Success).data)
        actualResponse?.second?.let {
            Assert.assertTrue(it)
        }
    }

    @Test
    fun `when check is page showing error should return true`() {
        runBlockingTest {
            val throwable = Throwable("Error Timeout")

            val result = async {
                viewModel.flowLayoutList.first()
            }

            viewModel.setErrorState(throwable)

            val categoryLayoutList = result.await() as Pair<Success<TokoFoodListUiModel>, Boolean>
            val actualResponse =
                categoryLayoutList.first.data.items.find { it is TokoFoodErrorStateUiModel }
            Assert.assertNotNull(actualResponse)

            val actualResponseEmptyState = viewModel.isShownEmptyState()
            verifyCategoryIsShowingErrorState(actualResponseEmptyState)
            Assert.assertFalse(categoryLayoutList.second)
            result.cancel()
        }
    }

    @Test
    fun `when check is page not showing error and show loader should return false`() {
        onGetMerchantList_thenReturn(createMerchantListEmptyPageResponse(), pageKey = "0")

        val expectedResult = TokoFoodListUiModel(
            items = listOf(
                createMerchantListModel1(),
                createMerchantListModel2()
            ), TokoFoodLayoutState.SHOW
        )
        var actualResponse: Pair<Result<TokoFoodListUiModel>, Boolean>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setCategoryLayout(LocalCacheModel())
            collectorJob.cancel()
        }
        Assert.assertEquals(expectedResult, (actualResponse?.first as Success).data)

        val actualResponseEmptyState = viewModel.isShownEmptyState()
        actualResponse?.second?.let {
            Assert.assertTrue(it)
        }
        verifyCategoryIsNotShowingErrorState(actualResponseEmptyState)
    }

    @Test
    fun `when check is page not showing error should return false`() {

        val actualResponse = viewModel.isShownEmptyState()

        verifyCategoryIsNotShowingErrorState(actualResponse)
    }

    @Test
    fun `when scroll and loadmore should run and give the success result`() {
        val containLastItemIndex = 5
        val itemCount = 6

        onGetMerchantList_thenReturn(createMerchantListResponse(), pageKey = "0")
        onGetMerchantList_thenReturn(createMerchantListResponse(), pageKey = "1")

        val expectedResult = TokoFoodListUiModel(items = listOf(
            createMerchantListModel1(),
            createMerchantListModel2(),
            createMerchantListModel1(),
            createMerchantListModel2()
        ), TokoFoodLayoutState.LOAD_MORE)

        var actualResponse: Pair<Result<TokoFoodListUiModel>, Boolean>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setCategoryLayout(LocalCacheModel())
            viewModel.onScrollProductList(containLastItemIndex, itemCount, localCacheModel = LocalCacheModel())
            collectorJob.cancel()
        }
        actualResponse?.second?.let {
            Assert.assertFalse(it)
        }
        Assert.assertEquals(expectedResult, (actualResponse?.first as Success).data)
    }

    @Test
    fun `when scroll and loadmore, but containLastItemIndex and itemCount-1 not same, should run and  not load more`() {
        val containLastItemIndex = 5
        val itemCount = 5

        onGetMerchantList_thenReturn(createMerchantListResponse(), pageKey = "0")

        val expectedResult = TokoFoodListUiModel(items = listOf(
            createMerchantListModel1(),
            createMerchantListModel2(),
        ), TokoFoodLayoutState.SHOW)

        var actualResponse: Pair<Result<TokoFoodListUiModel>, Boolean>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setCategoryLayout(LocalCacheModel())
            viewModel.onScrollProductList(containLastItemIndex, itemCount, localCacheModel = LocalCacheModel())
            collectorJob.cancel()
        }
        actualResponse?.second?.let {
            Assert.assertTrue(it)
        }
        Assert.assertEquals(expectedResult, (actualResponse?.first as Success).data)
    }

    @Test
    fun `when scroll and execute loadmore, but containLastItemIndex is less than 0, should run and not load more`() {
        val containLastItemIndex = -1
        val itemCount = 0

        onGetMerchantList_thenReturn(createMerchantListResponse(), pageKey = "0")

        val expectedResult = TokoFoodListUiModel(items = listOf(
            createMerchantListModel1(),
            createMerchantListModel2(),
        ), TokoFoodLayoutState.SHOW)

        var actualResponse: Pair<Result<TokoFoodListUiModel>, Boolean>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setCategoryLayout(LocalCacheModel())
            viewModel.onScrollProductList(containLastItemIndex, itemCount, localCacheModel = LocalCacheModel())
            collectorJob.cancel()
        }
        actualResponse?.second?.let {
            Assert.assertTrue(it)
        }
        Assert.assertEquals(expectedResult, (actualResponse?.first as Success).data)
    }

    @Test
    fun `when scroll and execute loadmore, but pageKey is empty, should run and not load more`() {
        val containLastItemIndex = 5
        val itemCount = 6

        onGetMerchantList_thenReturn(createMerchantListEmptyPageResponse(), pageKey = "0")

        val expectedResult = TokoFoodListUiModel(items = listOf(
            createMerchantListModel1(),
            createMerchantListModel2(),
        ), TokoFoodLayoutState.SHOW)

        var actualResponse: Pair<Result<TokoFoodListUiModel>, Boolean>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setCategoryLayout(LocalCacheModel())
            viewModel.onScrollProductList(containLastItemIndex, itemCount, localCacheModel = LocalCacheModel())
            collectorJob.cancel()
        }
        actualResponse?.second?.let {
            Assert.assertTrue(it)
        }
        Assert.assertEquals(expectedResult, (actualResponse?.first as Success).data)
    }

    @Test
    fun `when getting categoryLayout should run and give the success result`() {
        onGetMerchantList_thenReturn(createMerchantListEmptyPageResponse(), pageKey = "0")
        val expectedResult = TokoFoodListUiModel(items = listOf(
            createMerchantListModel1(),
            createMerchantListModel2()
        ), TokoFoodLayoutState.SHOW)

        var actualResponse: Pair<Result<TokoFoodListUiModel>, Boolean>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setCategoryLayout(LocalCacheModel())
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedResult, (actualResponse?.first as Success).data)
        actualResponse?.second?.let {
            Assert.assertTrue(it)
        }
    }

    @Test
    fun `when getting categoryLayout with custome param should run and give the success result`() {
        onGetMerchantList_thenReturn(createMerchantListResponse(), option = 1, sortBy = 1, cuisine = "Coffe")

        val expectedResult = TokoFoodListUiModel(items = listOf(
            createMerchantListModel1(),
            createMerchantListModel2()
        ), TokoFoodLayoutState.SHOW)

        var actualResponse: Pair<Result<TokoFoodListUiModel>, Boolean>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setCategoryLayout(LocalCacheModel(), option = 1, sortBy = 1, cuisine = "Coffe")
            collectorJob.cancel()
        }
        Assert.assertEquals(expectedResult, (actualResponse?.first as Success).data)
        actualResponse?.second?.let {
            Assert.assertTrue(it)
        }
    }

    @Test
    fun `when scroll and loadmore and param is all different should run and give the success result`() {
        val containLastItemIndex = 5
        val itemCount = 6

        onGetMerchantList_thenReturn(createMerchantListResponse(), pageKey = "0", option = 1, sortBy = 1, cuisine = "Coffe", brandUId = "Lala")
        onGetMerchantList_thenReturn(createMerchantListResponse(), pageKey = "1", option = 1, sortBy = 1, cuisine = "Coffe", brandUId = "Lala")

        val expectedResult = TokoFoodListUiModel(items = listOf(
            createMerchantListModel1(),
            createMerchantListModel2(),
            createMerchantListModel1(),
            createMerchantListModel2()
        ), TokoFoodLayoutState.LOAD_MORE)

        var actualResponse: Pair<Result<TokoFoodListUiModel>, Boolean>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setCategoryLayout(LocalCacheModel(), option = 1, sortBy = 1, cuisine = "Coffe", brandUId = "Lala")
            viewModel.onScrollProductList(containLastItemIndex, itemCount, localCacheModel = LocalCacheModel(), option = 1, sortBy = 1, cuisine = "Coffe", brandUId = "Lala")
            collectorJob.cancel()
        }
        Assert.assertEquals(expectedResult, (actualResponse?.first as Success).data)
        actualResponse?.second?.let {
            Assert.assertFalse(it)
        }
    }

    @Test
    fun `when getting categoryLayout should run and give the fail result`() {
        onGetMerchantList_thenReturn(NullPointerException())

        var actualResponse: Pair<Result<TokoFoodListUiModel>, Boolean>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setCategoryLayout(LocalCacheModel())
            collectorJob.cancel()
        }
        Assert.assertTrue(actualResponse?.first is Fail)
        actualResponse?.second?.let {
            Assert.assertTrue(it)
        }
    }

    @Test
    fun `when scroll and loadmore should error and give the fail result`() {
        val containLastItemIndex = 5
        val itemCount = 6

        onGetMerchantList_thenReturn(createMerchantListResponse(), pageKey = "0")
        onGetMerchantList_thenReturn(NullPointerException(), pageKey = "1")

        var actualResponse: Pair<Result<TokoFoodListUiModel>, Boolean>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowLayoutList.collectLatest {
                    actualResponse = it
                }
            }
            viewModel.setCategoryLayout(LocalCacheModel())
            viewModel.onScrollProductList(containLastItemIndex, itemCount, localCacheModel = LocalCacheModel())

            collectorJob.cancel()
        }
        Assert.assertTrue(actualResponse?.first is Fail)
        actualResponse?.second?.let {
            Assert.assertFalse(it)
        }
    }

    @Test
    fun `when scroll and execute loadmore, but there is loadmore state, should run and not load more`() {
        val containLastItemIndex = 5
        val itemCount = 6
        mockProgressBar()

        val shouldLoadMore = viewModel.shouldLoadMore(containLastItemIndex, itemCount)
        Assert.assertFalse(shouldLoadMore)
    }

    @Test
    fun `when scroll and execute loadmore, but there is error state, should run and not load more`() {
        val containLastItemIndex = 5
        val itemCount = 6
        mockErrorState(Throwable())

        val shouldLoadMore = viewModel.shouldLoadMore(containLastItemIndex, itemCount)
        Assert.assertFalse(shouldLoadMore)
    }
}