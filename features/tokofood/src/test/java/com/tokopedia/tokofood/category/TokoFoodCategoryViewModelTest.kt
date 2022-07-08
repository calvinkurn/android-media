package com.tokopedia.tokofood.category

import android.accounts.NetworkErrorException
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokofood.data.createLoadMoreState
import com.tokopedia.tokofood.data.createLoadingCategoryState
import com.tokopedia.tokofood.data.createMerchantListEmptyPageResponse
import com.tokopedia.tokofood.data.createMerchantListEmptyResponse
import com.tokopedia.tokofood.data.createMerchantListModel1
import com.tokopedia.tokofood.data.createMerchantListModel2
import com.tokopedia.tokofood.data.createMerchantListResponse
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodLayoutState
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodListUiModel
import org.junit.Test

class TokoFoodCategoryViewModelTest: TokoFoodCategoryViewModelTestFixture() {

    @Test
    fun `when getting loading state should run and give the success result`() {
        val expectedPageKey = "0"
        viewModel.showLoadingState()

        val expectedResponse = createLoadingCategoryState()

        verifyGetCategoryLayoutResponseSuccess(expectedResponse)
        verifyPageKey(expectedPageKey)
    }

    @Test
    fun `when getting error state should run and give the error result`() {
        val throwable = Throwable("Error Timeout")

        viewModel.showErrorState(throwable)

        verifyGetErrorLayoutShown()
    }

    @Test
    fun `when getting categoryLayout should run and give the success result`() {
        onGetMerchantList_thenReturn(createMerchantListEmptyPageResponse(), pageKey = "0")

        viewModel.getCategoryLayout(LocalCacheModel())

        val expectedResult = TokoFoodListUiModel(items = listOf(
            createMerchantListModel1(),
            createMerchantListModel2()
        ), TokoFoodLayoutState.SHOW)

        verifyGetCategoryLayoutResponseSuccess(expectedResult)
    }

    @Test
    fun `when getting categoryLayout but the data is empty should show empty state and give the success result`() {
        onGetMerchantList_thenReturn(createMerchantListEmptyResponse())

        viewModel.getCategoryLayout(LocalCacheModel())

        val expectedResult = TokoFoodListUiModel(items = viewModel.categoryLayoutItemList, TokoFoodLayoutState.SHOW)

        verifyGetCategoryLayoutResponseSuccess(expectedResult)
    }

    @Test
    fun `when getting categoryLayout with custome param should run and give the success result`() {
        onGetMerchantList_thenReturn(createMerchantListResponse(), option = 1, sortBy = 1, cuisine = "Coffe")

        viewModel.getCategoryLayout(LocalCacheModel(), option = 1, sortBy = 1, cuisine = "Coffe")

        val expectedResult = TokoFoodListUiModel(items = listOf(
            createMerchantListModel1(),
            createMerchantListModel2()
        ), TokoFoodLayoutState.SHOW)

        verifyGetCategoryLayoutResponseSuccess(expectedResult)
    }

    @Test
    fun `when getting categoryLayout should run and give the fail result`() {
        onGetMerchantList_thenReturn(NullPointerException())

        viewModel.getCategoryLayout(LocalCacheModel())

        verifyGetCategoryLayoutResponseFail()
    }

    @Test
    fun `when scroll and loadmore should run and give the success result`() {
        val containLastItemIndex = 5
        val itemCount = 6

        onGetMerchantList_thenReturn(createMerchantListResponse(), pageKey = "0")
        onGetMerchantList_thenReturn(createMerchantListResponse(), pageKey = "1")

        viewModel.getCategoryLayout(LocalCacheModel())
        viewModel.onScrollProductList(containLastItemIndex, itemCount, localCacheModel = LocalCacheModel())

        val expectedResult = TokoFoodListUiModel(items = listOf(
            createMerchantListModel1(),
            createMerchantListModel2(),
            createMerchantListModel1(),
            createMerchantListModel2()
        ), TokoFoodLayoutState.LOAD_MORE)

        verifyLoadMoreLayoutResponseSuccess(expectedResult)
    }

    @Test
    fun `when scroll and loadmore should error and give the fail result`() {
        val containLastItemIndex = 5
        val itemCount = 6

        onGetMerchantList_thenReturn(createMerchantListResponse(), pageKey = "0")
        onGetMerchantList_thenReturn(NullPointerException(), pageKey = "1")

        viewModel.getCategoryLayout(LocalCacheModel())
        viewModel.onScrollProductList(containLastItemIndex, itemCount, localCacheModel = LocalCacheModel())

        verifyLoadMoreLayoutResponseFail()
    }

    @Test
    fun `when scroll and loadmore, but containLastItemIndex and itemCount-1 not same, should run and  not load more`() {
        val containLastItemIndex = 5
        val itemCount = 5

        onGetMerchantList_thenReturn(createMerchantListResponse(), pageKey = "0")

        viewModel.getCategoryLayout(LocalCacheModel())
        viewModel.onScrollProductList(containLastItemIndex, itemCount, localCacheModel = LocalCacheModel())

        val expectedResult = TokoFoodListUiModel(items = listOf(
            createMerchantListModel1(),
            createMerchantListModel2(),
        ), TokoFoodLayoutState.SHOW)

        verifyGetCategoryLayoutResponseSuccess(expectedResult)
    }

    @Test
    fun `when scroll and execute loadmore, but containLastItemIndex is less than 0, should run and not load more`() {
        val containLastItemIndex = -1
        val itemCount = 0

        onGetMerchantList_thenReturn(createMerchantListResponse(), pageKey = "0")

        viewModel.getCategoryLayout(LocalCacheModel())
        viewModel.onScrollProductList(containLastItemIndex, itemCount, localCacheModel = LocalCacheModel())

        val expectedResult = TokoFoodListUiModel(items = listOf(
            createMerchantListModel1(),
            createMerchantListModel2(),
        ), TokoFoodLayoutState.SHOW)

        verifyGetCategoryLayoutResponseSuccess(expectedResult)
    }

    @Test
    fun `when scroll and execute loadmore, but pageKey is empty, should run and not load more`() {
        val containLastItemIndex = 5
        val itemCount = 6

        onGetMerchantList_thenReturn(createMerchantListEmptyPageResponse(), pageKey = "0")

        viewModel.getCategoryLayout(LocalCacheModel())
        viewModel.onScrollProductList(containLastItemIndex, itemCount, localCacheModel = LocalCacheModel())

        val expectedResult = TokoFoodListUiModel(items = listOf(
            createMerchantListModel1(),
            createMerchantListModel2(),
        ), TokoFoodLayoutState.SHOW)

        verifyGetCategoryLayoutResponseSuccess(expectedResult)
    }

    @Test
    fun `when scroll and execute loadmore, but there is error state, should run and not load more`() {
        val throwable = Throwable("Error Timeout")
        val containLastItemIndex = 5
        val itemCount = 6

        viewModel.showErrorState(throwable)
        viewModel.onScrollProductList(containLastItemIndex, itemCount, localCacheModel = LocalCacheModel())

        verifyGetErrorLayoutShown()
    }

    @Test
    fun `when scroll and execute loadmore, but there is loadmore state, should run and not load more`() {
        viewModel.showProgressBar()
        val expectedResponse = createLoadMoreState()
        val containLastItemIndex = 5
        val itemCount = 6

        viewModel.onScrollProductList(containLastItemIndex, itemCount, LocalCacheModel())


        verifyGetCategoryLayoutResponseSuccess(expectedResponse)
    }

    @Test
    fun `when check is page showing error should return true`() {
        viewModel.showErrorState(NetworkErrorException())

        val actualResponse = viewModel.isShownEmptyState()

        verifyCategoryIsShowingErrorState(actualResponse)
    }

    @Test
    fun `when check is page not showing error and show loader should return false`() {

        viewModel.showProgressBar()

        val actualResponse = viewModel.isShownEmptyState()

        verifyCategoryIsNotShowingErrorState(actualResponse)
    }

    @Test
    fun `when check is page not showing error should return false`() {

        val actualResponse = viewModel.isShownEmptyState()

        verifyCategoryIsNotShowingErrorState(actualResponse)
    }
}