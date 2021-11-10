package com.tokopedia.tokopedianow.repurchase.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressQglResponse
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryListResponse
import com.tokopedia.tokopedianow.categorylist.domain.usecase.GetCategoryListUseCase
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.SCREEN_NAME_TOKONOW_OOC
import com.tokopedia.tokopedianow.common.model.*
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.VALUE.HOMEPAGE_TOKONOW
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics.VALUE.REPURCHASE_TOKONOW
import com.tokopedia.tokopedianow.repurchase.domain.model.TokoNowRepurchasePageResponse.*
import com.tokopedia.tokopedianow.repurchase.domain.param.GetRepurchaseProductListParam
import com.tokopedia.tokopedianow.repurchase.domain.usecase.GetRepurchaseProductListUseCase
import com.tokopedia.tokopedianow.repurchase.presentation.fragment.TokoNowRepurchaseFragment
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseEmptyStateNoHistoryUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseLayoutUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseSortFilterUiModel
import com.tokopedia.tokopedianow.util.TestUtils.getPrivateMethod
import com.tokopedia.tokopedianow.util.TestUtils.mockPrivateField
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import junit.framework.Assert.assertTrue
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
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
    lateinit var getChooseAddressWarehouseLocUseCase: GetChosenAddressWarehouseLocUseCase
    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel : TokoNowRepurchaseViewModel

    protected lateinit var privateLocalCacheModel: Field

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = TokoNowRepurchaseViewModel(
                getRepurchaseProductListUseCase,
                getMiniCartUseCase,
                getCategoryListUseCase,
                addToCartUseCase,
                updateCartUseCase,
                deleteCartUseCase,
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

    protected fun verifyGetCategoryGridLayoutSuccess(expectedResponse: RepurchaseLayoutUiModel) {
        val actualResponse = viewModel.getLayout.value
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

    protected fun verifyTrackOpeningScreen() {
        val actualResponse = viewModel.openScreenTracker.value
        Assert.assertEquals(REPURCHASE_TOKONOW, actualResponse)
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

    protected fun onGetCategoryList_thenReturn(categoryListResponse: CategoryListResponse) {
        coEvery { getCategoryListUseCase.execute("1", TokoNowRepurchaseFragment.CATEGORY_LEVEL_DEPTH) } returns categoryListResponse
    }

    protected fun onGetRepurchaseProductList_thenReturn(response: GetRepurchaseProductListResponse) {
        coEvery { getRepurchaseProductListUseCase.execute(any()) } returns response
    }

    protected fun onGetRepurchaseProductList_thenReturn(error: Throwable) {
        coEvery { getRepurchaseProductListUseCase.execute(any()) } throws error
    }

    protected fun onAddToCart_thenReturn(response: AddToCartDataModel) {
        every {
            addToCartUseCase.execute(any(), any())
        } answers {
            firstArg<(AddToCartDataModel) -> Unit>().invoke(response)
        }
    }

    protected fun onAddToCart_thenReturn(error: Throwable) {
        every {
            addToCartUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(error)
        }
    }

    protected fun onRemoveItemCart_thenReturn(response: RemoveFromCartData) {
        every {
            deleteCartUseCase.execute(any(), any())
        } answers {
            firstArg<(RemoveFromCartData) -> Unit>().invoke(response)
        }
    }

    protected fun onRemoveItemCart_thenReturn(error: Throwable) {
        every {
            deleteCartUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(error)
        }
    }

    protected fun onUpdateItemCart_thenReturn(response: UpdateCartV2Data) {
        every {
            updateCartUseCase.execute(any(), any())
        } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(response)
        }
    }

    protected fun onUpdateItemCart_thenReturn(error: Throwable) {
        every {
            updateCartUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(error)
        }
    }

    protected fun onGetMiniCart_thenReturn(response: MiniCartSimplifiedData) {
        coEvery {
            getMiniCartUseCase.execute(any(), any())
        } answers {
            firstArg<(MiniCartSimplifiedData)-> Unit>().invoke(response)
        }
    }

    protected fun onGetMiniCart_thenReturn(error: Throwable) {
        coEvery {
            getMiniCartUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable)-> Unit>().invoke(error)
        }
    }

    protected fun onGetMiniCart_throwException(error: Throwable) {
        coEvery {
            getMiniCartUseCase.execute(any(), any())
        } answers {
            throw error
        }
    }

    protected fun onGetUserLoggedIn_thenReturn(isLoggedIn: Boolean) {
        every { userSession.isLoggedIn } returns isLoggedIn
    }

    protected fun verifyGetCategoryListUseCaseCalled(){
        coVerify(exactly = 1) { getCategoryListUseCase.execute("1", TokoNowRepurchaseFragment.CATEGORY_LEVEL_DEPTH) }
    }

    protected fun onGetLayoutList_thenReturnNull() {
        viewModel.mockPrivateField("layoutList", null)
    }

    protected fun verifyGetMiniCartUseCaseCalled(){
        coVerify { getMiniCartUseCase.execute(any(), any()) }

    }

    protected fun verifyGetMiniCartUseCaseNotCalled(){
        coVerify(exactly = 0) { getMiniCartUseCase.execute(any(), any()) }
    }

    protected fun verifyAddToCartUseCaseCalled() {
        verify { addToCartUseCase.execute(any(), any()) }
    }

    protected fun verifyDeleteCartUseCaseCalled() {
        verify { deleteCartUseCase.execute(any(), any()) }
    }

    protected fun verifyUpdateCartUseCaseCalled() {
        verify { updateCartUseCase.execute(any(), any()) }
    }

    protected fun verifyGetProductUseCaseCalled() {
        coVerify { getRepurchaseProductListUseCase.execute(any()) }
    }

    protected fun verifyGetProductUseCaseCalled(param: GetRepurchaseProductListParam) {
        coVerify { getRepurchaseProductListUseCase.execute(param) }
    }

    protected fun verifyGetProductUseCaseCalled(times: Int = 1) {
        coVerify(exactly = times) { getRepurchaseProductListUseCase.execute(any()) }
    }

    protected fun verifyGetProductUseCaseNotCalled() {
        coVerify(exactly = 0) { getRepurchaseProductListUseCase.execute(any()) }
    }

    protected fun verifyLayoutListContains(layout: Visitable<*>) {
        val data = (viewModel.getLayout.value as Success<RepurchaseLayoutUiModel>).data
        val condition = data.layoutList.firstOrNull { it::class.java == layout::class.java } != null
        assertTrue(condition)
    }

    protected fun callPrivateLoadMoreProduct() {
        viewModel.getPrivateMethod("loadMoreProduct").invoke(viewModel)
    }
}