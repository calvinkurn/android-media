package com.tokopedia.tokopedianow.repurchase.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressQglResponse
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.tokopedianow.common.domain.model.GetCategoryListResponse.CategoryListResponse
import com.tokopedia.tokopedianow.common.domain.model.SetUserPreference
import com.tokopedia.tokopedianow.common.domain.model.WarehouseData
import com.tokopedia.tokopedianow.common.domain.usecase.GetCategoryListUseCase
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.common.domain.usecase.SetUserPreferenceUseCase
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateNoResultUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateOocUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowServerErrorUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.common.service.NowAffiliateService
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics.VALUE.REPURCHASE_TOKONOW
import com.tokopedia.tokopedianow.repurchase.domain.model.TokoNowRepurchasePageResponse.GetRepurchaseProductListResponse
import com.tokopedia.tokopedianow.repurchase.domain.param.GetRepurchaseProductListParam
import com.tokopedia.tokopedianow.repurchase.domain.usecase.GetRepurchaseProductListUseCase
import com.tokopedia.tokopedianow.repurchase.presentation.fragment.TokoNowRepurchaseFragment
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseEmptyStateNoHistoryUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseLayoutUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseSortFilterUiModel
import com.tokopedia.tokopedianow.util.TestUtils.getPrivateMethod
import com.tokopedia.tokopedianow.util.TestUtils.mockPrivateField
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import java.lang.reflect.Field

@ExperimentalCoroutinesApi
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
    lateinit var setUserPreferenceUseCase: SetUserPreferenceUseCase

    @RelaxedMockK
    lateinit var affiliateService: NowAffiliateService

    @RelaxedMockK
    lateinit var getTargetedTicker: GetTargetedTickerUseCase

    @RelaxedMockK
    lateinit var addressData: TokoNowLocalAddress

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    protected lateinit var viewModel: TokoNowRepurchaseViewModel

    protected lateinit var privateLocalCacheModel: Field

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = TokoNowRepurchaseViewModel(
            getRepurchaseProductListUseCase,
            getMiniCartUseCase,
            getCategoryListUseCase,
            getChooseAddressWarehouseLocUseCase,
            setUserPreferenceUseCase,
            userSession,
            addToCartUseCase,
            updateCartUseCase,
            deleteCartUseCase,
            affiliateService,
            getTargetedTicker,
            addressData,
            coroutineTestRule.dispatchers
        )

        privateLocalCacheModel = viewModel::class.java.getDeclaredField("localCacheModel").apply {
            isAccessible = true
        }

        onGetUserLoggedIn_thenReturn(isLoggedIn = true)
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

    protected fun verifyProductRecommendationWidgetLayoutSuccess(expectedResponse: RepurchaseLayoutUiModel) {
        val actualResponse = viewModel.getLayout.value
        val expectedObject = (expectedResponse.layoutList.firstOrNull { it is TokoNowProductRecommendationUiModel } as TokoNowProductRecommendationUiModel)
        val actualObject = ((actualResponse as Success).data.layoutList.firstOrNull { it is TokoNowProductRecommendationUiModel } as TokoNowProductRecommendationUiModel)
        Assert.assertEquals(expectedObject, actualObject)
    }

    protected fun verifyProductRecommendationWidgetRemovedSuccess() {
        val actualResponse = viewModel.getLayout.value
        val actualObject = ((actualResponse as Success).data.layoutList.firstOrNull { it is TokoNowProductRecommendationUiModel })
        Assert.assertTrue(actualObject == null)
    }

    protected fun verifyGetCategoryGridLayoutSuccess(expectedResponse: RepurchaseLayoutUiModel) {
        val actualResponse = viewModel.getLayout.value
        val expectedObject = (expectedResponse.layoutList.firstOrNull { it is TokoNowCategoryMenuUiModel } as TokoNowCategoryMenuUiModel)
        val actualObject = ((actualResponse as Success).data.layoutList.firstOrNull { it is TokoNowCategoryMenuUiModel } as TokoNowCategoryMenuUiModel)
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
            firstArg<(GetStateChosenAddressResponse) -> Unit>().invoke(getStateChosenAddressResponse.response)
        }
    }

    protected fun onGetChooseAddress_thenReturn(errorThrowable: Throwable) {
        coEvery {
            getChooseAddressWarehouseLocUseCase.getStateChosenAddress(any(), any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(errorThrowable)
        }
    }

    protected fun onGetCategoryList_thenReturn(categoryListResponse: CategoryListResponse) {
        coEvery { getCategoryListUseCase.execute(warehouses = any(), depth = any()) } returns categoryListResponse
    }

    protected fun onGetCategoryList_thenReturn(error: Throwable) {
        coEvery { getCategoryListUseCase.execute(warehouses = any(), depth = any()) } throws error
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
            getMiniCartUseCase.executeOnBackground()
        } returns response
    }

    protected fun onGetMiniCart_throwException(error: Throwable) {
        coEvery {
            getMiniCartUseCase.executeOnBackground()
        } throws error
    }

    protected fun onGetUserLoggedIn_thenReturn(isLoggedIn: Boolean) {
        every { userSession.isLoggedIn } returns isLoggedIn
    }

    protected fun onSetUserPreference_thenReturn(userPreferenceData: SetUserPreference.SetUserPreferenceData) {
        coEvery { setUserPreferenceUseCase.execute(any(), any()) } returns userPreferenceData
    }

    protected fun onSetUserPreference_thenReturn(error: Throwable) {
        coEvery { setUserPreferenceUseCase.execute(any(), any()) } throws error
    }

    protected fun verifyGetCategoryListUseCaseCalled(warehouses: List<WarehouseData>, times: Int = 1) {
        coVerify(exactly = times) { getCategoryListUseCase.execute(warehouses, TokoNowRepurchaseFragment.CATEGORY_LEVEL_DEPTH) }
    }

    protected fun onGetLayoutList_thenReturnNull() {
        viewModel.mockPrivateField("layoutList", null)
    }

    protected fun verifyGetMiniCartUseCaseCalled() {
        coVerify { getMiniCartUseCase.executeOnBackground() }
    }

    protected fun verifyGetMiniCartUseCaseNotCalled() {
        coVerify(exactly = 0) { getMiniCartUseCase.executeOnBackground() }
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
        coVerify { getRepurchaseProductListUseCase.execute(param = any()) }
    }

    protected fun verifyGetProductUseCaseCalled(param: GetRepurchaseProductListParam) {
        coVerify { getRepurchaseProductListUseCase.execute(param = param) }
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

    protected fun verifySetUserPreferenceUseCaseCalled(
        localCacheModel: LocalCacheModel,
        serviceType: String
    ) {
        coVerify { setUserPreferenceUseCase.execute(localCacheModel, serviceType) }
    }

    protected fun verifySetUserPreferenceUseCaseNotCalled() {
        coVerify(exactly = 0) { setUserPreferenceUseCase.execute(any(), any()) }
    }

    protected fun callPrivateLoadMoreProduct() {
        viewModel.getPrivateMethod("loadMoreProduct").invoke(viewModel)
    }
}
