package com.tokopedia.tokofood.category

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokofood.data.createLoadingCategoryState
import com.tokopedia.tokofood.data.createMerchantListModel1
import com.tokopedia.tokofood.data.createMerchantListModel2
import com.tokopedia.tokofood.data.createMerchantListResponse
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodLayoutState
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodListUiModel
import org.junit.Test

class TokoFoodCategoryViewModelTest: TokoFoodCategoryViewModelTestFixture() {

    @Test
    fun `when getting loading state should run and give the success result`() {
        viewModel.getLoadingState()

        val expectedResponse = createLoadingCategoryState()

        verifyGetCategoryLayoutResponseSuccess(expectedResponse)
    }

    @Test
    fun `when getting error state should run and give the error result`() {
        val throwable = Throwable("Error Timeout")

        viewModel.getErrorState(throwable)

        verifyGetErrorLayoutShown()
    }

    @Test
    fun `when getting categoryLayout should run and give the success result`() {
        onGetMerchantList_thenReturn(createMerchantListResponse())

        viewModel.getCategoryLayout(LocalCacheModel(), page = "1")

        val expectedResult = TokoFoodListUiModel(items = listOf(
            createMerchantListModel1(),
            createMerchantListModel2()
        ), TokoFoodLayoutState.SHOW)

        verifyGetCategoryLayoutResponseSuccess(expectedResult)
    }

    @Test
    fun `when getting categoryLayout with custome param should run and give the success result`() {
        onGetMerchantList_thenReturn(createMerchantListResponse(), option = 1, sortBy = 1, cuisine = "Coffe", pageKey = "")

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

        viewModel.getCategoryLayout(LocalCacheModel(), page = "1")

        verifyGetCategoryLayoutResponseFail()
    }
}