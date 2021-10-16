package com.tokopedia.tokopedianow.home.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressQglResponse
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryListResponse
import com.tokopedia.tokopedianow.categorylist.domain.usecase.GetCategoryListUseCase
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.domain.model.KeywordSearchData
import com.tokopedia.tokopedianow.home.domain.model.SearchPlaceholder
import com.tokopedia.tokopedianow.home.domain.model.TickerResponse
import com.tokopedia.tokopedianow.home.domain.usecase.GetHomeLayoutDataUseCase
import com.tokopedia.tokopedianow.home.domain.usecase.GetHomeLayoutListUseCase
import com.tokopedia.tokopedianow.home.domain.usecase.GetKeywordSearchUseCase
import com.tokopedia.tokopedianow.home.domain.usecase.GetTickerUseCase
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryGridUiModel
import com.tokopedia.tokopedianow.home.domain.model.GetRecentPurchaseResponse.*
import com.tokopedia.tokopedianow.home.domain.usecase.GetRecentPurchaseUseCase
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutListUiModel
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
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import java.lang.reflect.Field

abstract class TokoNowHomeViewModelTestFixture {

    @RelaxedMockK
    lateinit var getHomeLayoutListUseCase: GetHomeLayoutListUseCase
    @RelaxedMockK
    lateinit var getHomeLayoutDataUseCase: GetHomeLayoutDataUseCase
    @RelaxedMockK
    lateinit var getCategoryListUseCase: GetCategoryListUseCase
    @RelaxedMockK
    lateinit var getKeywordSearchUseCase: GetKeywordSearchUseCase
    @RelaxedMockK
    lateinit var getTickerUseCase: GetTickerUseCase
    @RelaxedMockK
    lateinit var getMiniCartUseCase: GetMiniCartListSimplifiedUseCase
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
    lateinit var getRecentPurchaseUseCase: GetRecentPurchaseUseCase
    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel : TokoNowHomeViewModel

    protected lateinit var privateHomeLayoutItemList: Field

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = TokoNowHomeViewModel(
                getHomeLayoutListUseCase,
                getHomeLayoutDataUseCase,
                getCategoryListUseCase,
                getKeywordSearchUseCase,
                getTickerUseCase,
                getMiniCartUseCase,
                addToCartUseCase,
                updateCartUseCase,
                deleteCartUseCase,
                getRecommendationUseCase,
                getChooseAddressWarehouseLocUseCase,
                getRecentPurchaseUseCase,
                userSession,
                CoroutineTestDispatchersProvider
        )

        privateHomeLayoutItemList = viewModel::class.java.getDeclaredField("homeLayoutItemList").apply {
            isAccessible = true
        }
    }

    protected fun verifyGetHomeLayoutResponseSuccess(expectedResponse: HomeLayoutListUiModel) {
        val actualResponse = viewModel.homeLayoutList.value
        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    protected fun verifyMiniCartResponseSuccess(expectedResponse: MiniCartSimplifiedData) {
        val actualResponse = viewModel.miniCart.value
        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    protected fun verifyMiniCartNullResponse() {
        val actualResponse = viewModel.miniCart.value
        Assert.assertNull(actualResponse)
    }

    protected fun verfifyGetChooseAddressSuccess(expectedResponse: GetStateChosenAddressResponse) {
        val actualResponse = viewModel.chooseAddress.value
        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    protected fun verifyKeywordSearchResponseSuccess(expectedResponse: SearchPlaceholder) {
        val actualResponse = viewModel.keywordSearch.value
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    protected fun verifyGetCategoryListResponseSuccess(expectedResponse: HomeLayoutItemUiModel) {
        val homeLayoutList = viewModel.homeLayoutList.value
        val actualResponse = (homeLayoutList as Success).data.items.find { it?.layout is TokoNowCategoryGridUiModel }
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    protected fun verifyGetBannerResponseSuccess(expectedResponse: HomeLayoutItemUiModel) {
        val homeLayoutList = viewModel.homeLayoutList.value
        val actualResponse = (homeLayoutList as Success).data.items.find { it?.layout is BannerDataModel }
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    protected fun verifyGetHomeLayoutNullResponse() {
        val actualResponse = viewModel.homeLayoutList.value
        Assert.assertNull(actualResponse)
    }

    protected fun verifyGetHomeLayoutResponseFail() {
        val actualResponse = viewModel.homeLayoutList.value
        Assert.assertTrue(actualResponse is Fail)
    }

    protected fun verifyMiniCartFail() {
        val actualResponse = viewModel.miniCart.value
        Assert.assertTrue(actualResponse is Fail)
    }

    protected fun verifyGetChooseAddressFail() {
        val actualResponse = viewModel.chooseAddress.value
        Assert.assertTrue(actualResponse is Fail)
    }

    protected fun verifyGetHomeLayoutUseCaseCalled() {
        coVerify { getHomeLayoutListUseCase.execute(any()) }
    }

    protected fun verifyGetHomeLayoutDataUseCaseCalled(
        localCacheModel: LocalCacheModel = LocalCacheModel(),
        times: Int = 1
    ) {
        coVerify(exactly = times) { getHomeLayoutDataUseCase.execute(any(), localCacheModel) }
    }

    protected fun verifyGetHomeLayoutDataUseCaseNotCalled(
        localCacheModel: LocalCacheModel = LocalCacheModel()
    ) {
        coVerify(exactly = 0) { getHomeLayoutDataUseCase.execute(any(), localCacheModel) }
    }

    protected fun verifyGetTickerUseCaseCalled() {
        coVerify { getTickerUseCase.execute(any()) }
    }

    protected fun verifyGetTickerUseCaseNotCalled() {
        coVerify(exactly = 0) { getTickerUseCase.execute(any()) }
    }

    protected fun verifyGetChooseAddress() {
        coVerify { getChooseAddressWarehouseLocUseCase.getStateChosenAddress(any(), any(), any()) }
    }

    protected fun verifyGetKeywordSearchUseCaseCalled() {
        coVerify { getKeywordSearchUseCase.execute(anyBoolean(), anyString(), anyString()) }
    }

    protected fun verifyGetCategoryListUseCaseCalled(count: Int = 1){
        coVerify(exactly = count) { getCategoryListUseCase.execute("1", 1) }
    }

    protected fun verifyGetCategoryListUseCaseNotCalled(){
        coVerify(exactly = 0) { getCategoryListUseCase.execute(anyString(), anyInt()) }
    }

    protected fun verifyGetMiniCartUseCaseCalled() {
        verify { getMiniCartUseCase.execute(any(), any()) }
    }

    protected fun verifyGetMiniCartUseCaseNotCalled() {
        verify(exactly = 0) { getMiniCartUseCase.execute(any(), any()) }
    }

    protected fun verifyGetRecentPurchaseUseCaseCalled() {
        coVerify { getRecentPurchaseUseCase.execute(any()) }
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

    protected fun onGetTicker_thenReturn(tickerResponse: TickerResponse) {
        coEvery { getTickerUseCase.execute(any()) } returns tickerResponse
    }

    protected fun onGetHomeLayout_thenReturn(layoutResponse: List<HomeLayoutResponse>) {
        coEvery { getHomeLayoutListUseCase.execute(any()) } returns layoutResponse
    }

    protected fun onGetHomeLayoutData_thenReturn(
        layoutResponse: HomeLayoutResponse,
        localCacheModel: LocalCacheModel = LocalCacheModel()
    ) {
        coEvery { getHomeLayoutDataUseCase.execute(any(), localCacheModel) } returns layoutResponse
    }

    protected fun onGetHomeLayoutData_thenReturn(error: Throwable) {
        coEvery { getHomeLayoutDataUseCase.execute(anyString(), any()) } throws error
    }

    protected fun onGetKeywordSearch_thenReturn(keywordSearchResponse: KeywordSearchData) {
        coEvery { getKeywordSearchUseCase.execute(anyBoolean(), anyString(), anyString()) } returns keywordSearchResponse
    }

    protected fun onGetChooseAddress_thenReturn(getStateChosenAddressResponse: GetStateChosenAddressQglResponse) {
        coEvery {
            getChooseAddressWarehouseLocUseCase.getStateChosenAddress(any(), any(), any())
        } answers {
            firstArg<(GetStateChosenAddressResponse)-> Unit>().invoke(getStateChosenAddressResponse.response)
        }
    }

    protected fun onGetTicker_thenReturn(errorThrowable: Throwable) {
        coEvery { getTickerUseCase.execute(any()) } throws errorThrowable
    }

    protected fun onGetHomeLayout_thenReturn(errorThrowable: Throwable) {
        coEvery { getHomeLayoutListUseCase.execute(any()) } throws errorThrowable
    }

    protected fun onGetCategoryList_thenReturn(errorThrowable: Throwable) {
        coEvery { getCategoryListUseCase.execute("1", 1) } throws errorThrowable
    }

    protected fun onGetCategoryList_thenReturn(categoryListResponse: CategoryListResponse) {
        coEvery { getCategoryListUseCase.execute("1", 1) } returns categoryListResponse
    }

    protected fun onGetChooseAddress_thenReturn(errorThrowable: Throwable) {
        coEvery {
            getChooseAddressWarehouseLocUseCase.getStateChosenAddress(any(), any(), any())
        } answers {
            secondArg<(Throwable)-> Unit>().invoke(errorThrowable)
        }
    }

    protected fun onGetMiniCart_thenReturn(miniCartSimplifiedData: MiniCartSimplifiedData) {
        every {
            getMiniCartUseCase.execute(any(), any())
        } answers {
            firstArg<(MiniCartSimplifiedData) -> Unit>().invoke(miniCartSimplifiedData)
        }
    }

    protected fun onGetMiniCart_thenReturn(errorThrowable: Throwable) {
        every {
            getMiniCartUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(errorThrowable)
        }
    }

    protected fun onGetIsUserLoggedIn_thenReturn(userLoggedIn: Boolean) {
        every { userSession.isLoggedIn } returns userLoggedIn
    }

    protected fun onGetRecommendation_thenReturn(response: List<RecommendationWidget>) {
        coEvery { getRecommendationUseCase.getData(any()) } returns response
    }

    protected fun onAddToCart_thenReturn(response: AddToCartDataModel) {
        every {
            addToCartUseCase.execute(any(), any())
        } answers {
            firstArg<(AddToCartDataModel) -> Unit>().invoke(response)
        }
    }

    protected fun onRemoveItemCart_thenReturn(response: RemoveFromCartData) {
        every {
            deleteCartUseCase.execute(any(), any())
        } answers {
            firstArg<(RemoveFromCartData) -> Unit>().invoke(response)
        }
    }

    protected fun onUpdateItemCart_thenReturn(response: UpdateCartV2Data) {
        every {
            updateCartUseCase.execute(any(), any())
        } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(response)
        }
    }

    protected fun onGetRecentPurchase_thenReturn(response: RecentPurchaseData) {
        coEvery { getRecentPurchaseUseCase.execute(any()) } returns response
    }
}