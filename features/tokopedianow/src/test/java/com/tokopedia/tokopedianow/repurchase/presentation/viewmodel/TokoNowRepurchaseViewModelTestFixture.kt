package com.tokopedia.tokopedianow.repurchase.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressQglResponse
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryListResponse
import com.tokopedia.tokopedianow.categorylist.domain.usecase.GetCategoryListUseCase
import com.tokopedia.tokopedianow.common.model.*
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutListUiModel
import com.tokopedia.tokopedianow.recentpurchase.domain.model.TokoNowRepurchasePageResponse
import com.tokopedia.tokopedianow.recentpurchase.domain.usecase.GetRepurchaseProductListUseCase
import com.tokopedia.tokopedianow.recentpurchase.presentation.fragment.TokoNowRecentPurchaseFragment
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseEmptyStateNoHistoryUiModel
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseLayoutUiModel
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseSortFilterUiModel
import com.tokopedia.tokopedianow.recentpurchase.presentation.viewmodel.TokoNowRecentPurchaseViewModel
import com.tokopedia.tokopedianow.sortfilter.presentation.uimodel.SortFilterUiModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.mockito.ArgumentMatchers
import java.lang.reflect.Field

abstract class TokoNowRepurchaseViewModelTestFixture {

    @RelaxedMockK
    lateinit var getRepurchaseProductListUseCase: GetRepurchaseProductListUseCase
    @RelaxedMockK
    lateinit var getMiniCartUseCase: GetMiniCartListSimplifiedUseCase
    @RelaxedMockK
    lateinit var getCategoryListUseCase: GetCategoryListUseCase
    @RelaxedMockK
    lateinit var addToCartUseCase: AddToCartUseCase
    @RelaxedMockK
    lateinit var updateCartUseCase: UpdateCartUseCase
    @RelaxedMockK
    lateinit var deleteCartUseCase: DeleteCartUseCase
    @RelaxedMockK
    lateinit var getRecommendationUseCase: GetRecommendationUseCase
    @RelaxedMockK
    lateinit var getChooseAddressWarehouseLocUseCase: GetChosenAddressWarehouseLocUseCase
    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel : TokoNowRecentPurchaseViewModel

    protected lateinit var privateLocalCacheModel: Field

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = TokoNowRecentPurchaseViewModel(
                getRepurchaseProductListUseCase,
                getMiniCartUseCase,
                getCategoryListUseCase,
                addToCartUseCase,
                updateCartUseCase,
                deleteCartUseCase,
                getRecommendationUseCase,
                getChooseAddressWarehouseLocUseCase,
                userSession,
                CoroutineTestDispatchersProvider
        )

        privateLocalCacheModel = viewModel::class.java.getDeclaredField("localCacheModel").apply {
            isAccessible = true
        }
    }

    protected fun verifyShowLayoutSuccess(expectedResponse: RepurchaseLayoutUiModel) {
        val actualResponse = viewModel.getLayout.value
        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    protected fun verifySortFilterLayoutSuccess(expectedResponse: RepurchaseLayoutUiModel) {
        val actualResponse = viewModel.getLayout.value
        val expectedDescription = (expectedResponse.layoutList.firstOrNull { it is RepurchaseSortFilterUiModel } as RepurchaseSortFilterUiModel).sortFilterList
        val actualDescription = ((actualResponse as Success).data.layoutList.firstOrNull { it is RepurchaseSortFilterUiModel } as RepurchaseSortFilterUiModel).sortFilterList
        Assert.assertEquals(expectedDescription, actualDescription)
    }

    protected fun verifyEmptyStateNoHistoryLayoutSuccess(expectedResponse: RepurchaseLayoutUiModel) {
        val actualResponse = viewModel.getLayout.value
        val expectedDescription = (expectedResponse.layoutList.firstOrNull { it is RepurchaseEmptyStateNoHistoryUiModel } as RepurchaseEmptyStateNoHistoryUiModel).description
        val actualDescription = ((actualResponse as Success).data.layoutList.firstOrNull { it is RepurchaseEmptyStateNoHistoryUiModel } as RepurchaseEmptyStateNoHistoryUiModel).description
        Assert.assertEquals(expectedDescription, actualDescription)
    }

    protected fun verifyEmptyStateOocLayoutSuccess(expectedResponse: RepurchaseLayoutUiModel) {
        val actualResponse = viewModel.getLayout.value
        val expectedObject = (expectedResponse.layoutList.firstOrNull { it is TokoNowEmptyStateOocUiModel } as TokoNowEmptyStateOocUiModel)
        val actualObject = ((actualResponse as Success).data.layoutList.firstOrNull { it is TokoNowEmptyStateOocUiModel } as TokoNowEmptyStateOocUiModel)
        Assert.assertEquals(expectedObject, actualObject)
    }

    protected fun verifyEmptyStateFailedLayoutSuccess(expectedResponse: RepurchaseLayoutUiModel) {
        val actualResponse = viewModel.getLayout.value
        val expectedObject = (expectedResponse.layoutList.firstOrNull { it is TokoNowServerErrorUiModel } as TokoNowServerErrorUiModel)
        val actualObject = ((actualResponse as Success).data.layoutList.firstOrNull { it is TokoNowServerErrorUiModel } as TokoNowServerErrorUiModel)
        Assert.assertEquals(expectedObject, actualObject)
    }

    protected fun verifyEmptyStateNoResultLayoutSuccess(expectedResponse: RepurchaseLayoutUiModel) {
        val actualResponse = viewModel.getLayout.value
        val expectedObject = (expectedResponse.layoutList.firstOrNull { it is TokoNowEmptyStateNoResultUiModel } as TokoNowEmptyStateNoResultUiModel)
        val actualObject = ((actualResponse as Success).data.layoutList.firstOrNull { it is TokoNowEmptyStateNoResultUiModel } as TokoNowEmptyStateNoResultUiModel)

        Assert.assertEquals(expectedObject.defaultTitleResId, actualObject.defaultTitleResId)
        Assert.assertEquals(expectedObject.defaultDescriptionResId, actualObject.defaultDescriptionResId)
        Assert.assertEquals(expectedObject.globalSearchBtnTextResId, actualObject.globalSearchBtnTextResId)
    }

    protected fun verifyChooseAddressWidgetLayoutSuccess(expectedResponse: RepurchaseLayoutUiModel) {
        val actualResponse = viewModel.getLayout.value
        val expectedObject = (expectedResponse.layoutList.firstOrNull { it is TokoNowChooseAddressWidgetUiModel } as TokoNowChooseAddressWidgetUiModel)
        val actualObject = ((actualResponse as Success).data.layoutList.firstOrNull { it is TokoNowChooseAddressWidgetUiModel } as TokoNowChooseAddressWidgetUiModel)
        Assert.assertEquals(expectedObject, actualObject)
    }

    protected fun verifyChooseAddressWidgetLayoutRemovedSuccess() {
        val actualResponse = viewModel.getLayout.value
        val actualObject = ((actualResponse as Success).data.layoutList.firstOrNull { it is TokoNowChooseAddressWidgetUiModel })
        Assert.assertTrue(actualObject == null)
    }

    protected fun verifyGetProductRecommendationWidgetLayoutSuccess(expectedResponse: RepurchaseLayoutUiModel) {
        val actualResponse = viewModel.getLayout.value
        val expectedObject = (expectedResponse.layoutList.firstOrNull { it is TokoNowRecommendationCarouselUiModel } as TokoNowRecommendationCarouselUiModel)
        val actualObject = ((actualResponse as Success).data.layoutList.firstOrNull { it is TokoNowRecommendationCarouselUiModel } as TokoNowRecommendationCarouselUiModel)

        Assert.assertEquals(expectedObject.pageName, actualObject.pageName)
        Assert.assertEquals(expectedObject.carouselData, actualObject.carouselData)
    }

    protected fun verifyGetCategoryGridLayoutSuccess(expectedResponse: RepurchaseLayoutUiModel) {
        val actualResponse = viewModel.getLayout.value
        println(viewModel.getLayout.value)
        val expectedObject = (expectedResponse.layoutList.firstOrNull { it is TokoNowCategoryGridUiModel } as TokoNowCategoryGridUiModel)
        val actualObject = ((actualResponse as Success).data.layoutList.firstOrNull { it is TokoNowCategoryGridUiModel } as TokoNowCategoryGridUiModel)
        Assert.assertEquals(expectedObject, actualObject)
    }

    protected fun verfifyGetChooseAddressSuccess(expectedResponse: GetStateChosenAddressResponse) {
        val actualResponse = viewModel.chooseAddress.value
        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    protected fun verifyGetChooseAddress() {
        coVerify { getChooseAddressWarehouseLocUseCase.getStateChosenAddress(any(), any(), any()) }
    }

    protected fun verifyGetChooseAddressFail() {
        val actualResponse = viewModel.chooseAddress.value
        Assert.assertTrue(actualResponse is Fail)
    }

    protected fun onGetChooseAddress_thenReturn(getStateChosenAddressResponse: GetStateChosenAddressQglResponse) {
        coEvery {
            getChooseAddressWarehouseLocUseCase.getStateChosenAddress(any(), any(), any())
        } answers {
            firstArg<(GetStateChosenAddressResponse)-> Unit>().invoke(getStateChosenAddressResponse.response)
        }
    }

    protected fun onGetChooseAddress_thenReturn(errorThrowable: Throwable) {
        coEvery {
            getChooseAddressWarehouseLocUseCase.getStateChosenAddress(any(), any(), any())
        } answers {
            secondArg<(Throwable)-> Unit>().invoke(errorThrowable)
        }
    }

    protected fun onGetProductRecommendation_thenReturn(response: List<RecommendationWidget>) {
        coEvery { getRecommendationUseCase.getData(any()) } returns response
    }

    protected fun onGetCategoryList_thenReturn(categoryListResponse: CategoryListResponse) {
        coEvery { getCategoryListUseCase.execute("1", TokoNowRecentPurchaseFragment.CATEGORY_LEVEL_DEPTH) } returns categoryListResponse
    }

    protected fun onGetRepurchaseProductList_thenReturn(repurchaseProductListResponse: TokoNowRepurchasePageResponse.GetRepurchaseProductListResponse) {
        coEvery { getRepurchaseProductListUseCase.execute(any()) } returns repurchaseProductListResponse
    }

    protected fun verifyGetCategoryListUseCaseCalled(){
        coVerify(exactly = 1) { getCategoryListUseCase.execute("1", TokoNowRecentPurchaseFragment.CATEGORY_LEVEL_DEPTH) }
    }

    protected fun verifyGetProductRecommendatioUseCaseCalled(){
        coVerify { getRecommendationUseCase.getData(any()) }
    }

}