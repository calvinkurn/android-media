package com.tokopedia.tokopedianow.home.presentation.viewmodel

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressQglResponse
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.play.widget.data.PlayWidget
import com.tokopedia.play.widget.ui.PlayWidgetState
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryListResponse
import com.tokopedia.tokopedianow.categorylist.domain.usecase.GetCategoryListUseCase
import com.tokopedia.tokopedianow.common.domain.model.SetUserPreference.SetUserPreferenceData
import com.tokopedia.tokopedianow.common.domain.usecase.SetUserPreferenceUseCase
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryGridUiModel
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.VALUE.HOMEPAGE_TOKONOW
import com.tokopedia.tokopedianow.home.domain.model.GetQuestListResponse
import com.tokopedia.tokopedianow.home.domain.model.GetRepurchaseResponse.RepurchaseData
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.domain.model.KeywordSearchData
import com.tokopedia.tokopedianow.home.domain.model.ReferralEvaluateJoinResponse
import com.tokopedia.tokopedianow.home.domain.model.SearchPlaceholder
import com.tokopedia.tokopedianow.home.domain.model.TickerResponse
import com.tokopedia.tokopedianow.home.domain.usecase.GetHomeLayoutDataUseCase
import com.tokopedia.tokopedianow.home.domain.usecase.GetHomeReferralUseCase
import com.tokopedia.tokopedianow.home.domain.usecase.GetKeywordSearchUseCase
import com.tokopedia.tokopedianow.home.domain.usecase.GetQuestWidgetListUseCase
import com.tokopedia.tokopedianow.home.domain.usecase.GetRepurchaseWidgetUseCase
import com.tokopedia.tokopedianow.home.domain.usecase.GetTickerUseCase
import com.tokopedia.tokopedianow.home.domain.usecase.ReferralEvaluateJoinUseCase
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeTypeFactory
import com.tokopedia.tokopedianow.home.presentation.model.HomeReferralDataModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutListUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeQuestSequenceWidgetUiModel
import com.tokopedia.tokopedianow.util.TestUtils.getPrivateField
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
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString

abstract class TokoNowHomeViewModelTestFixture {

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
    lateinit var getRepurchaseWidgetUseCase: GetRepurchaseWidgetUseCase
    @RelaxedMockK
    lateinit var getQuestWidgetListUseCase: GetQuestWidgetListUseCase
    @RelaxedMockK
    lateinit var setUserPreferenceUseCase: SetUserPreferenceUseCase
    @RelaxedMockK
    lateinit var getHomeReferralUseCase: GetHomeReferralUseCase
    @RelaxedMockK
    lateinit var referralEvaluateJoinUseCase: ReferralEvaluateJoinUseCase
    @RelaxedMockK
    lateinit var playWidgetTools: PlayWidgetTools
    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel : TokoNowHomeViewModel

    private val privateHomeLayoutItemList by lazy {
        viewModel.getPrivateField<MutableList<HomeLayoutItemUiModel?>>("homeLayoutItemList")
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = TokoNowHomeViewModel(
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
                getRepurchaseWidgetUseCase,
                getQuestWidgetListUseCase,
                setUserPreferenceUseCase,
                getHomeReferralUseCase,
                referralEvaluateJoinUseCase,
                playWidgetTools,
                userSession,
                CoroutineTestDispatchersProvider
        )
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

    protected fun verifyTrackOpeningScreen() {
        val actualResponse = viewModel.openScreenTracker.value
        Assert.assertEquals(HOMEPAGE_TOKONOW, actualResponse)
    }

    protected fun verfifyGetChooseAddressSuccess(expectedResponse: GetStateChosenAddressResponse) {
        val actualResponse = viewModel.chooseAddress.value
        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    protected fun verifyKeywordSearchResponseSuccess(expectedResponse: SearchPlaceholder) {
        val actualResponse = viewModel.keywordSearch.value
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    protected fun verifyGetCategoryListResponseSuccess(expectedResponse: Visitable<*>) {
        val homeLayoutList = viewModel.homeLayoutList.value
        val actualResponse = (homeLayoutList as Success).data.items.find { it is TokoNowCategoryGridUiModel }
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    protected fun verifyGetBannerResponseSuccess(expectedResponse: Visitable<*>) {
        val homeLayoutList = viewModel.homeLayoutList.value
        val actualResponse = (homeLayoutList as Success).data.items.find { it is BannerDataModel }
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    protected fun verifyGetQuestListResponseSuccess(expectedResponse: Visitable<*>?) {
        val homeLayoutList = viewModel.homeLayoutList.value
        val actualResponse = (homeLayoutList as Success).data.items.find { it is HomeQuestSequenceWidgetUiModel }
        Assert.assertEquals(expectedResponse, actualResponse)
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

    protected fun verifyGetReferralSenderHomeUseCaseCalled(slug: String) {
        coVerify { getHomeReferralUseCase.execute(slug) }
    }

    protected fun verifyGetHomeLayoutDataUseCaseCalled(
        localCacheModel: LocalCacheModel = LocalCacheModel(),
        times: Int = 1
    ) {
        coVerify(exactly = times) { getHomeLayoutDataUseCase.execute(any(), any(), localCacheModel) }
    }

    protected fun verifyGetRealTimeRecommendationCalled(pageName: String = any(), productId: List<String> = any()) {
        coVerify {
            getRecommendationUseCase.getData(
                GetRecommendationRequestParam(
                    pageName = pageName,
                    productIds = productId,
                    xSource = "recom_widget",
                    xDevice = "android"
                )
            )
        }
    }

    protected fun verifyGetRealTimeRecommendationNotCalled(pageName: String = any(), productId: List<String> = any()) {
        coVerify(exactly = 0) {
            getRecommendationUseCase.getData(
                GetRecommendationRequestParam(
                    pageName = pageName,
                    productIds = productId,
                    xSource = "recom_widget",
                    xDevice = "android"
                )
            )
        }
    }

    protected fun verifyGetTickerUseCaseCalled() {
        coVerify { getTickerUseCase.execute(pageSource = any(), localCacheModel = any()) }
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

    protected fun verifyGetRepurchaseWidgetUseCaseCalled() {
        coVerify { getRepurchaseWidgetUseCase.execute(any()) }
    }

    protected fun verifyGetRepurchaseWidgetUseCaseNotCalled() {
        coVerify(exactly = 0) { getRepurchaseWidgetUseCase.execute(any()) }
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

    protected fun verifyGetQuestWidgetListUseCaseCalled() {
        coVerify { getQuestWidgetListUseCase.execute(any()) }
    }

    protected fun verifySetUserPreferenceUseCaseCalled(
        localCacheModel: LocalCacheModel,
        serviceType: String
    ) {
        coVerify { setUserPreferenceUseCase.execute(localCacheModel, serviceType) }
    }

    protected fun verifyGetQuestWidgetListUseCaseNotCalled() {
        coVerify(exactly = 0) { getQuestWidgetListUseCase.execute(any()) }
    }

    protected fun onGetTicker_thenReturn(tickerResponse: TickerResponse) {
        coEvery { getTickerUseCase.execute(pageSource = any(), localCacheModel = any()) } returns tickerResponse
    }

    protected fun onGetHomeLayoutData_thenReturn(
        layoutResponse: List<HomeLayoutResponse>,
        localCacheModel: LocalCacheModel = LocalCacheModel()
    ) {
        coEvery { getHomeLayoutDataUseCase.execute(any(), any(), localCacheModel) } returns layoutResponse
    }

    protected fun onGetPlayWidget_thenReturn(playWidgetState: PlayWidgetState) {
        coEvery { playWidgetTools.getWidgetFromNetwork(any(), any()) } returns PlayWidget()
        coEvery { playWidgetTools.mapWidgetToModel(any(), any(), any()) } returns playWidgetState
    }

    protected fun onGetPlayWidget_thenReturn(error: Throwable) {
        coEvery { playWidgetTools.getWidgetFromNetwork(any(), any()) } throws error
        coEvery { playWidgetTools.mapWidgetToModel(any(), any(), any()) } throws error
    }

    protected fun onGetHomeLayoutData_thenReturn(
        error: Throwable,
        localCacheModel: LocalCacheModel = LocalCacheModel()
    ) {
        coEvery { getHomeLayoutDataUseCase.execute(any(), any(), localCacheModel) } throws error
    }

    protected fun onGetKeywordSearch_thenReturn(keywordSearchResponse: KeywordSearchData) {
        coEvery { getKeywordSearchUseCase.execute(anyBoolean(), anyString(), anyString()) } returns keywordSearchResponse
    }

    protected fun onGetKeywordSearch_thenReturn(error: Throwable) {
        coEvery { getKeywordSearchUseCase.execute(anyBoolean(), anyString(), anyString()) } throws error
    }

    protected fun onGetChooseAddress_thenReturn(getStateChosenAddressResponse: GetStateChosenAddressQglResponse) {
        coEvery {
            getChooseAddressWarehouseLocUseCase.getStateChosenAddress(any(), any(), any())
        } answers {
            firstArg<(GetStateChosenAddressResponse)-> Unit>().invoke(getStateChosenAddressResponse.response)
        }
    }

    protected fun onGetTicker_thenReturn(errorThrowable: Throwable) {
        coEvery { getTickerUseCase.execute(pageSource = any(), localCacheModel = any()) } throws errorThrowable
    }

    protected fun onGetCategoryList_thenReturn(errorThrowable: Throwable) {
        coEvery { getCategoryListUseCase.execute("1", 1) } throws errorThrowable
    }

    protected fun onGetCategoryList_thenReturn(categoryListResponse: CategoryListResponse) {
        coEvery { getCategoryListUseCase.execute("1", 1) } returns categoryListResponse
    }

    protected fun onGetQuestWidgetList_thenReturn(questListResponse: GetQuestListResponse) {
        coEvery { getQuestWidgetListUseCase.execute(any()) } returns questListResponse
    }

    protected fun onGetQuestWidgetList_thenReturn(errorThrowable: Throwable) {
        coEvery { getQuestWidgetListUseCase.execute(any()) } throws  errorThrowable
    }

    protected fun onGetRepurchaseWidget_thenReturn(response: RepurchaseData) {
        coEvery { getRepurchaseWidgetUseCase.execute(any()) } returns response
    }

    protected fun onGetRepurchaseWidget_thenReturn(error: Throwable) {
        coEvery { getRepurchaseWidgetUseCase.execute(any()) } throws error
    }

    protected fun onSetUserPreference_thenReturn(userPreferenceData: SetUserPreferenceData) {
        coEvery { setUserPreferenceUseCase.execute(any(), any()) } returns userPreferenceData
    }

    protected fun onSetUserPreference_thenReturn(error: Throwable) {
        coEvery { setUserPreferenceUseCase.execute(any(), any()) } throws error
    }

    protected fun onGetReferralSenderHome_thenReturn(slug: String, referral: HomeReferralDataModel) {
        coEvery { getHomeReferralUseCase.execute(slug) } returns referral
    }

    protected fun onGetReferralSenderHome_thenReturn(slug: String, errorThrowable: Throwable) {
        coEvery { getHomeReferralUseCase.execute(slug) } throws errorThrowable
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

    protected fun onGetMiniCart_throwException(exception: Throwable) {
        every {
            getMiniCartUseCase.execute(any(), any())
        } answers {
            throw exception
        }
    }

    protected fun onGetIsUserLoggedIn_thenReturn(userLoggedIn: Boolean) {
        every { userSession.isLoggedIn } returns userLoggedIn
    }

    protected fun onGetRecommendation_thenReturn(response: List<RecommendationWidget>) {
        coEvery { getRecommendationUseCase.getData(any()) } returns response
    }

    protected fun onGetRecommendation_thenReturn(error: Throwable) {
        coEvery { getRecommendationUseCase.getData(any()) } throws error
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

    protected fun addHomeLayoutItem(item: HomeLayoutItemUiModel?) {
        privateHomeLayoutItemList.add(item)
    }

    protected fun onGetHomeLayoutItemList_returnNull() {
        viewModel.mockPrivateField("homeLayoutItemList", null)
    }

    protected fun onGetUserSession_returnNull() {
        viewModel.mockPrivateField("userSession", null)
    }

    protected fun onGetReferralEvalute_thenReturn(response: ReferralEvaluateJoinResponse) {
        coEvery {
            referralEvaluateJoinUseCase.execute(any())
        } returns response
    }

    protected fun onGetReferralEvalute_thenReturn(error: Exception) {
        coEvery {
            referralEvaluateJoinUseCase.execute(any())
        } throws error
    }

    object UnknownHomeLayout: HomeLayoutUiModel("1") {
        override fun type(typeFactory: HomeTypeFactory?) = 0
    }

    object UnknownLayout: HomeComponentVisitable {
        override fun visitableId(): String? = null
        override fun equalsWith(b: Any?): Boolean = false
        override fun getChangePayloadFrom(b: Any?): Bundle? = null
        override fun type(typeFactory: HomeComponentTypeFactory?) = 0
    }
}
