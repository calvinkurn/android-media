package com.tokopedia.tokopedianow.home.presentation.viewmodel

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.home_component.model.ChannelConfig
import com.tokopedia.home_component.model.ChannelHeader
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.ChannelStyle
import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalWarehouseModel
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.widget.analytic.ImpressionableModel
import com.tokopedia.play.widget.domain.PlayWidgetUseCase.WidgetType.TokoNowMediumWidget
import com.tokopedia.play.widget.domain.PlayWidgetUseCase.WidgetType.TokoNowSmallWidget
import com.tokopedia.play.widget.ui.PlayWidgetState
import com.tokopedia.play.widget.ui.model.PlayWidgetBannerUiModel
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.ProductCardModel.NonVariant
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.constant.ServiceType
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType.Companion.MAIN_QUEST
import com.tokopedia.tokopedianow.common.domain.model.RepurchaseProduct
import com.tokopedia.tokopedianow.common.domain.model.SetUserPreference.SetUserPreferenceData
import com.tokopedia.tokopedianow.common.domain.model.WarehouseData
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryGridUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryItemUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryListUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowRepurchaseUiModel
import com.tokopedia.tokopedianow.data.createCategoryGridDataModel
import com.tokopedia.tokopedianow.data.createCategoryGridListFirstFetch
import com.tokopedia.tokopedianow.data.createCategoryGridListSecondFetch
import com.tokopedia.tokopedianow.data.createCategoryGridWithAdultDataFetch
import com.tokopedia.tokopedianow.data.createCategoryList
import com.tokopedia.tokopedianow.data.createChooseAddress
import com.tokopedia.tokopedianow.data.createDynamicChannelLayoutList
import com.tokopedia.tokopedianow.data.createDynamicLegoBannerDataModel
import com.tokopedia.tokopedianow.data.createEmptyState
import com.tokopedia.tokopedianow.data.createHomeLayoutData
import com.tokopedia.tokopedianow.data.createHomeLayoutList
import com.tokopedia.tokopedianow.data.createHomeLayoutListForBannerOnly
import com.tokopedia.tokopedianow.data.createHomeLayoutListForQuestOnly
import com.tokopedia.tokopedianow.data.createHomeProductCardUiModel
import com.tokopedia.tokopedianow.data.createHomeTickerDataModel
import com.tokopedia.tokopedianow.data.createKeywordSearch
import com.tokopedia.tokopedianow.data.createLeftCarouselAtcDataModel
import com.tokopedia.tokopedianow.data.createLeftCarouselDataModel
import com.tokopedia.tokopedianow.data.createLoadingState
import com.tokopedia.tokopedianow.data.createLocalCacheModel
import com.tokopedia.tokopedianow.data.createMiniCartSimplifier
import com.tokopedia.tokopedianow.data.createPlayWidgetChannel
import com.tokopedia.tokopedianow.data.createPlayWidgetUiModel
import com.tokopedia.tokopedianow.data.createQuestWidgetList
import com.tokopedia.tokopedianow.data.createQuestWidgetListEmpty
import com.tokopedia.tokopedianow.data.createSliderBannerDataModel
import com.tokopedia.tokopedianow.data.createTicker
import com.tokopedia.tokopedianow.home.analytic.HomeAddToCartTracker
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.VALUE.HOMEPAGE_TOKONOW
import com.tokopedia.tokopedianow.home.analytic.HomeRemoveFromCartTracker
import com.tokopedia.tokopedianow.home.analytic.HomeSwitchServiceTracker
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.EMPTY_STATE_OUT_OF_COVERAGE
import com.tokopedia.tokopedianow.home.domain.model.GetRepurchaseResponse.RepurchaseData
import com.tokopedia.tokopedianow.home.domain.model.Grid
import com.tokopedia.tokopedianow.home.domain.model.Header
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.domain.model.HomeRemoveAbleWidget
import com.tokopedia.tokopedianow.home.domain.model.ReferralEvaluateJoinResponse
import com.tokopedia.tokopedianow.home.domain.model.Shop
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment.Companion.SOURCE
import com.tokopedia.tokopedianow.home.presentation.model.HomeReferralDataModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeEducationalInformationWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutListUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomePlayWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductRecomUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProgressBarUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeQuestSequenceWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeQuestWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeRealTimeRecomUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeSharingWidgetUiModel.HomeSharingEducationWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeSharingWidgetUiModel.HomeSharingReferralWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeSwitcherUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeTickerUiModel
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyString

class TokoNowHomeViewModelTest: TokoNowHomeViewModelTestFixture() {

    @Test
    fun `when tracking with setting screenName should give the same result`() {
        viewModel.trackOpeningScreen(HOMEPAGE_TOKONOW)
        verifyTrackOpeningScreen()
    }

    @Test
    fun `when getting homeLayout should run and give the success result`() {
        onGetTicker_thenReturn(createTicker())
        onGetHomeLayoutData_thenReturn(createHomeLayoutList())
        onGetCategoryList_thenReturn(createCategoryList(emptyList()))

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val expectedResponse = HomeLayoutListUiModel(
            items = listOf(
                TokoNowChooseAddressWidgetUiModel(id = "0"),
                createHomeTickerDataModel(),
                createDynamicLegoBannerDataModel(
                    "34923",
                    "",
                    "Lego Banner"
                ),
                createCategoryGridDataModel(
                    "11111",
                    "Category Tokonow",
                    null,
                    TokoNowLayoutState.HIDE
                ),
                createSliderBannerDataModel(
                    "2222",
                    "",
                    "Banner Tokonow"
                )
            ),
            state = TokoNowLayoutState.UPDATE
        )
        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetTickerUseCaseCalled()
        verifyGetHomeLayoutResponseSuccess(expectedResponse)
    }

    @Test
    fun `given homeLayoutList null when getLayoutComponentData should update live data FAIL`() {
        onGetTicker_thenReturn(createTicker())
        onGetHomeLayoutData_thenReturn(createHomeLayoutList())
        onGetCategoryList_thenReturn(createCategoryList(emptyList()))

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())

        onGetHomeLayoutItemList_returnNull()

        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        viewModel.homeLayoutList
            .verifyErrorEquals(Fail(NullPointerException()))
    }

    @Test
    fun `when getHomeLayout two times should call use case twice`() {
        onGetHomeLayoutData_thenReturn(createHomeLayoutList())

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        verifyGetHomeLayoutDataUseCaseCalled(times = 2)
    }

    @Test
    fun `when getting homeLayoutData should run and give the success result`() {
        onGetHomeLayoutData_thenReturn(createHomeLayoutListForBannerOnly())

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        onGetHomeLayoutData_thenReturn(listOf(createHomeLayoutData()))

        viewModel.onScroll(1, LocalCacheModel(), listOf())

        val expectedResponse = BannerDataModel(
            channelModel = ChannelModel(
                id = "2222",
                groupId = "",
                style = ChannelStyle.ChannelHome,
                channelHeader = ChannelHeader(name = "Banner Tokonow"),
                channelConfig = ChannelConfig(layout = "banner_carousel_v2"),
                layout = "banner_carousel_v2"
            )
        )

        verifyGetHomeLayoutDataUseCaseCalled(times = 2)
        verifyGetBannerResponseSuccess(expectedResponse)
    }

    @Test
    fun `when scroll home error should do nothing`() {
        onGetHomeLayoutData_thenReturn(createHomeLayoutListForBannerOnly())

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        onGetHomeLayoutData_thenReturn(MessageErrorException())

        viewModel.onScroll(1, LocalCacheModel(), listOf())

        verifyGetHomeLayoutDataUseCaseCalled(times = 2)
    }

    @Test
    fun `when getting loadingState should run and give the success result`() {
        viewModel.getLoadingState()

        val expectedResponse = createLoadingState()

        verifyGetHomeLayoutResponseSuccess(expectedResponse)
    }

    @Test
    fun `when getting emptyState should run and give the success result`() {
        val serviceType = "2h"
        val idEmptyState = EMPTY_STATE_OUT_OF_COVERAGE

        viewModel.getEmptyState(idEmptyState, serviceType)

        val expectedResponse = createEmptyState(idEmptyState, serviceType)

        verifyGetHomeLayoutResponseSuccess(expectedResponse)
    }

    @Test
    fun `when getEmptyState error should set live data value FAIL`() {
        val serviceType = "2h"
        val idEmptyState = EMPTY_STATE_OUT_OF_COVERAGE

        onGetHomeLayoutItemList_returnNull()

        viewModel.getEmptyState(idEmptyState, serviceType)

        viewModel.homeLayoutList
            .verifyErrorEquals(Fail(NullPointerException()))
    }

    @Test
    fun `when getting keywordSearch should run and give the success result`() {
        onGetKeywordSearch_thenReturn(createKeywordSearch())

        viewModel.getKeywordSearch(anyBoolean(), anyString(), anyString())

        verifyGetKeywordSearchUseCaseCalled()

        val expectedResponse = createKeywordSearch()
        verifyKeywordSearchResponseSuccess(expectedResponse.searchData)
    }

    @Test
    fun `given get keyword error when getKeywordSearch should NOT set keywordSearch value`() {
        onGetKeywordSearch_thenReturn(NullPointerException())

        viewModel.getKeywordSearch(anyBoolean(), anyString(), anyString())

        verifyGetKeywordSearchUseCaseCalled()

        viewModel.keywordSearch
            .verifyValueEquals(null)
    }

    @Test
    fun `when getting chooseAddress should run and give the success result`() {
        onGetChooseAddress_thenReturn(createChooseAddress())

        viewModel.getChooseAddress(SOURCE)

        verifyGetChooseAddress()

        val expectedResponse = createChooseAddress().response
        verfifyGetChooseAddressSuccess(expectedResponse)
    }

    @Test
    fun `when getting homeLayout should throw ticker's exception and get the failed result`() {
        onGetTicker_thenReturn(Exception())

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        verifyGetHomeLayoutResponseFail()
    }

    @Test
    fun `when getting homeLayout should throw homeLayout's exception and get the failed result`() {
        onGetHomeLayoutData_thenReturn(Exception())

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        verifyGetHomeLayoutResponseFail()
    }

    @Test
    fun `when getting chooseAddress should throw chooseAddress's exception and get failed result`() {
        onGetChooseAddress_thenReturn(Exception())

        viewModel.getChooseAddress(SOURCE)

        verifyGetChooseAddressFail()
    }

    @Test
    fun `when getting data for mini cart should run and give success result`(){
        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)
        onGetMiniCart_thenReturn(createMiniCartSimplifier())

        viewModel.getMiniCart(shopId = listOf("123"), warehouseId = "233")

        verifyMiniCartResponseSuccess(createMiniCartSimplifier())
    }

    @Test
    fun `when getting data for mini cart should throw mini cart exception`(){
        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)
        onGetMiniCart_thenReturn(Exception())

        viewModel.getMiniCart(shopId = listOf("123"), warehouseId = "233")

        verifyMiniCartFail()
    }

    @Test
    fun `when getMiniCart throw exception should set miniCart value fail`(){
        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)
        onGetMiniCart_throwException(Exception())

        viewModel.getMiniCart(shopId = listOf("123"), warehouseId = "233")

        viewModel.miniCart
            .verifyErrorEquals(Fail(Exception()))
    }

    @Test
    fun `when getMiniCart twice should run and give success result`(){
        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)
        onGetMiniCart_thenReturn(createMiniCartSimplifier())

        viewModel.getMiniCart(shopId = listOf("123"), warehouseId = "233")
        viewModel.getMiniCart(shopId = listOf("123"), warehouseId = "233")

        verifyMiniCartResponseSuccess(createMiniCartSimplifier())
    }

    @Test
    fun `when shopId is empty should get null for category livedata`(){
        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)
        onGetMiniCart_thenReturn(createMiniCartSimplifier())

        viewModel.getMiniCart(shopId = listOf(), warehouseId = "233")

        verifyMiniCartNullResponse()
    }

    @Test
    fun `when warehouseId is zero should get null for category livedata`(){
        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)
        onGetMiniCart_thenReturn(createMiniCartSimplifier())

        viewModel.getMiniCart(shopId = listOf("123"), warehouseId = "0")

        verifyMiniCartNullResponse()
    }

    @Test
    fun `given user is not logged in when getMiniCart should not call use case`() {
        onGetIsUserLoggedIn_thenReturn(userLoggedIn = false)

        viewModel.getMiniCart(listOf("123"), "1")

        verifyGetMiniCartUseCaseNotCalled()
        verifyMiniCartNullResponse()
    }

    @Test
    fun `when getting data category grid should run and give the success result`() {
        val localCacheModel = LocalCacheModel(
            warehouse_id = "1"
        )

        //set mock data
        onGetHomeLayoutData_thenReturn(createHomeLayoutList(), localCacheModel)
        onGetCategoryList_thenReturn(createCategoryGridListFirstFetch())

        //fetch homeLayout
        viewModel.getHomeLayout(localCacheModel = localCacheModel, removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = localCacheModel)

        //set second mock data to replace first mock data category list
        onGetCategoryList_thenReturn(createCategoryGridListSecondFetch())

        //prepare model that need to be changed
        val model = TokoNowCategoryGridUiModel(
                id="11111",
                title="Category Tokonow",
                categoryListUiModel = null,
                state= TokoNowLayoutState.SHOW
        )

        viewModel.getCategoryGrid(model, "1")

        //prepare model for expectedResult
        val expectedResponse = TokoNowCategoryGridUiModel(
            id = "11111",
            title = "Category Tokonow",
            categoryListUiModel = TokoNowCategoryListUiModel(
                categoryList = listOf(
                    TokoNowCategoryItemUiModel(
                        id = "",
                        title = "",
                        imageUrl = null,
                        appLink = "tokopedia-android-internal://now/category-list?warehouse_id={warehouse_id}",
                        warehouseId = "1",
                    ),
                    TokoNowCategoryItemUiModel(
                        id = "1",
                        title = "Category 1",
                        imageUrl = "tokopedia://",
                        appLink = "tokoepdia://",
                        headerName = "Category Tokonow"
                    )
                )
            ),
            state = TokoNowLayoutState.SHOW
        )

        // verify use case called and response
        verifyGetHomeLayoutDataUseCaseCalled(localCacheModel)
        verifyGetCategoryListUseCaseCalled(count = 2)
        verifyGetCategoryListResponseSuccess(expectedResponse)
    }

    @Test
    fun `when get category grid should not add adult category to category list`() {
        val localCacheModel = LocalCacheModel(
            warehouse_id = "1"
        )

        //set mock data
        onGetHomeLayoutData_thenReturn(createHomeLayoutList(), localCacheModel)
        onGetCategoryList_thenReturn(createCategoryGridWithAdultDataFetch())

        //fetch homeLayout
        viewModel.getHomeLayout(localCacheModel = localCacheModel, removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = localCacheModel)

        //prepare model that need to be changed
        val model = TokoNowCategoryGridUiModel(
            id="11111",
            title="Category Tokonow",
            categoryListUiModel = null,
            state= TokoNowLayoutState.SHOW
        )

        viewModel.getCategoryGrid(model, "1")

        //prepare model for expectedResult
        val expectedResponse = TokoNowCategoryGridUiModel(
            id = "11111",
            title = "Category Tokonow",
            categoryListUiModel = TokoNowCategoryListUiModel(
                categoryList = listOf(
                    TokoNowCategoryItemUiModel(
                        id = "",
                        title = "",
                        imageUrl = null,
                        appLink = "tokopedia-android-internal://now/category-list?warehouse_id={warehouse_id}",
                        warehouseId = "1"
                    ),
                    TokoNowCategoryItemUiModel(
                        id = "1",
                        title = "Category 1",
                        imageUrl = "tokopedia://",
                        appLink = "tokoepdia://",
                        headerName = "Category Tokonow"
                    ),
                    TokoNowCategoryItemUiModel(
                        id="3",
                        title="Category 3",
                        imageUrl="tokopedia://",
                        appLink="tokoepdia://",
                        headerName = "Category Tokonow"
                    )
                )
            ),
            state = TokoNowLayoutState.SHOW
        )

        // verify use case called and response
        verifyGetHomeLayoutDataUseCaseCalled(localCacheModel)
        verifyGetCategoryListUseCaseCalled(count = 2)
        verifyGetCategoryListResponseSuccess(expectedResponse)
    }

    @Test
    fun `when getting data category grid should run, throw exception and give the success result`() {
        //set mock data
        onGetHomeLayoutData_thenReturn(createHomeLayoutList())
        onGetCategoryList_thenReturn(createCategoryGridListFirstFetch())

        //fetch homeLayout
        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())

        //fetch widget one by one
        viewModel.onScroll(1, LocalCacheModel(), listOf())

        //set second mock data to replace first mock data category list
        onGetCategoryList_thenReturn(Exception())

        //prepare model that need to be changed
        val model = TokoNowCategoryGridUiModel(
                id="11111",
                title="Category Tokonow",
                categoryListUiModel = null,
                state= TokoNowLayoutState.SHOW
        )

        viewModel.getCategoryGrid(model, "1")

        //prepare model for expectedResult
        val expectedResponse = TokoNowCategoryGridUiModel(
            id = "11111",
            title = "Category Tokonow",
            categoryListUiModel = null,
            state = TokoNowLayoutState.HIDE
        )

        // verify use case called and response
        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetCategoryListUseCaseCalled()
        verifyGetCategoryListResponseSuccess(expectedResponse)
    }

    @Test
    fun `when getting data quest list should run successfully with empty list result`() {
        //set mock data
        val successCode = "200"
        onGetHomeLayoutData_thenReturn(createHomeLayoutListForQuestOnly())
        onGetQuestWidgetList_thenReturn(createQuestWidgetListEmpty(code = successCode))

        //fetch homeLayout
        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        // verify use case called and response
        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetQuestWidgetListUseCaseCalled()
        verifyGetQuestListResponseSuccess(null)
    }

    @Test
    fun `when getting data quest list should return error code not two hundred`() {
        //set mock data
        val errorCode = "12231"
        onGetHomeLayoutData_thenReturn(createHomeLayoutListForQuestOnly())
        onGetQuestWidgetList_thenReturn(createQuestWidgetListEmpty(code = errorCode))

        //fetch homeLayout
        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        //prepare model for expectedResult
        val expectedResponse = HomeQuestSequenceWidgetUiModel(
            id = MAIN_QUEST,
            state = HomeLayoutItemState.NOT_LOADED
        )

        // verify use case called and response
        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetQuestWidgetListUseCaseCalled()
        verifyGetQuestListResponseSuccess(expectedResponse)
    }

    @Test
    fun `when getting data quest list should run successfully with quest list data`() {
        //set mock data
        val successCode = "200"
        onGetHomeLayoutData_thenReturn(createHomeLayoutListForQuestOnly())
        onGetQuestWidgetList_thenReturn(createQuestWidgetList(successCode))

        //fetch homeLayout
        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        //prepare model for expectedResult
        val expectedResponse = HomeQuestSequenceWidgetUiModel(
            id = MAIN_QUEST,
            state = HomeLayoutItemState.LOADED,
            questList = listOf(
                HomeQuestWidgetUiModel(
                    id = "1233",
                    status = "Idle",
                    currentProgress = 0f,
                    totalProgress = 0f
                )
            )
        )

        // verify use case called and response
        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetQuestWidgetListUseCaseCalled()
        verifyGetQuestListResponseSuccess(expectedResponse)
    }

    @Test
    fun `when getting data quest list should throw an exception and remove the widget`() {
        //set mock data
        onGetHomeLayoutData_thenReturn(createHomeLayoutListForQuestOnly())
        onGetQuestWidgetList_thenReturn(Exception())

        //fetch homeLayout
        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        // verify use case called and response
        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetQuestWidgetListUseCaseCalled()
        verifyGetQuestListResponseSuccess(null)
    }

    @Test
    fun `when getting data quest list should run, fetch quest list again and give the success result`() {
        //set the code here to make it error and need to refresh
        var code = "12300"
        onGetHomeLayoutData_thenReturn(createHomeLayoutListForQuestOnly())
        onGetQuestWidgetList_thenReturn(createQuestWidgetList(code))

        //fetch homeLayout
        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        //set the code to make it success and get the list
        code = "200"
        onGetQuestWidgetList_thenReturn(createQuestWidgetList(code))

        //put home sequence ui model as param and re-fetch quest list
        viewModel.refreshQuestList()

        //prepare model for expectedResult
        val expectedResponse = HomeQuestSequenceWidgetUiModel(
            id = MAIN_QUEST,
            state = HomeLayoutItemState.LOADED,
            questList = listOf(
                HomeQuestWidgetUiModel(
                    id = "1233",
                    status = "Idle",
                    currentProgress = 0f,
                    totalProgress = 0f
                )
            )
        )

        // verify use case called and response
        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetQuestWidgetListUseCaseCalled()
        verifyGetQuestListResponseSuccess(expectedResponse)
    }

    @Test
    fun `when getting data quest list should run, fetch quest list again, throw an exception and remove the widget`() {
        //set mock data
        val successCode = "200"
        onGetHomeLayoutData_thenReturn(createHomeLayoutListForQuestOnly())
        onGetQuestWidgetList_thenReturn(createQuestWidgetList(successCode))

        //fetch homeLayout
        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        //set quest widget list to throw an exception
        onGetQuestWidgetList_thenReturn(Exception())

        //put home sequence ui model as param and re-fetch quest list
        viewModel.refreshQuestList()

        // verify use case called and response
        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetQuestWidgetListUseCaseCalled()
        verifyGetQuestListResponseSuccess(null)
    }

    @Test
    fun `given layout list does NOT contain quest ui model when refreshQuestList should NOT call use case`() {
        val layoutList = listOf(
            HomeLayoutResponse(
                id = "2222",
                layout = "banner_carousel_v2",
                header = Header(
                    name = "Banner Tokonow",
                    serverTimeUnix = 0
                ),
                token = "==aff1ed" // Dummy token
            )
        )

        onGetHomeLayoutData_thenReturn(layoutList)

        //fetch homeLayout
        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )

        viewModel.refreshQuestList()

        verifyGetQuestWidgetListUseCaseNotCalled()
    }

    @Test
    fun `when get category grid data error should map data with null category list`() {
        val warehouseId = "1"
        val localCacheModel = LocalCacheModel(
            warehouse_id = warehouseId
        )

        onGetHomeLayoutData_thenReturn(
            layoutResponse = createHomeLayoutList(),
            localCacheModel = localCacheModel
        )
        onGetCategoryList_thenReturn(Exception())

        viewModel.getHomeLayout(localCacheModel = localCacheModel, removeAbleWidgets = emptyList())
        viewModel.getLayoutComponentData(localCacheModel = localCacheModel)

        val data = HomeLayoutListUiModel(
            items = listOf(
                TokoNowChooseAddressWidgetUiModel(id = "0"),
                createDynamicLegoBannerDataModel(
                    "34923",
                    "",
                    "Lego Banner"
                ),
                TokoNowCategoryGridUiModel(
                    id = "11111",
                    title = "Category Tokonow",
                    categoryListUiModel = null,
                    state = TokoNowLayoutState.HIDE
                ),
                createSliderBannerDataModel(
                    "2222",
                    "",
                    "Banner Tokonow"
                )
            ),
            state = TokoNowLayoutState.UPDATE
        )
        val expectedResult = Success(data)

        verifyGetHomeLayoutDataUseCaseCalled(localCacheModel)
        verifyGetCategoryListUseCaseCalled()

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when getHomeLayout should add all dynamic channel to home layout list`() {
        val channelId = "2322"
        val warehouseId = "1"
        val localCacheModel = LocalCacheModel(warehouse_id = warehouseId)

        val recommendationItems = emptyList<RecommendationItem>()
        val recommendationWidget = RecommendationWidget(
            title = "Product Recommendation",
            recommendationItemList = recommendationItems
        )

        onGetHomeLayoutData_thenReturn(
            layoutResponse = createDynamicChannelLayoutList(),
            localCacheModel = localCacheModel
        )

        viewModel.getHomeLayout(localCacheModel = localCacheModel, removeAbleWidgets = emptyList())
        viewModel.getLayoutComponentData(localCacheModel = localCacheModel)

        val realTimeRecom = HomeRealTimeRecomUiModel(
            channelId = channelId,
            headerName = "Product Recommendation",
            warehouseId = warehouseId
        )

        val data = HomeLayoutListUiModel(
            items = listOf(
                TokoNowChooseAddressWidgetUiModel(id = "0"),
                createDynamicLegoBannerDataModel(
                    id = "34923",
                    groupId = "",
                    headerName = "Lego Banner",
                    layout = "lego_3_image"
                ),
                createDynamicLegoBannerDataModel(
                    id = "11111",
                    groupId = "",
                    headerName = "Lego 6",
                    layout = "6_image"
                ),
                createSliderBannerDataModel(
                    "2222",
                    "",
                    "Banner Tokonow"
                ),
                HomeProductRecomUiModel(
                    id = "2322",
                    recomWidget = recommendationWidget,
                    realTimeRecom = realTimeRecom
                ),
                createLeftCarouselAtcDataModel(
                    id = "2122",
                    headerName = "Mix Left Atc Carousel",
                    warehouseId = warehouseId
                ),
                createLeftCarouselDataModel(
                    id = "2333",
                    groupId = "",
                    headerName = "Mix Left Carousel",
                    layout = "left_carousel"
                )
            ),
            state = TokoNowLayoutState.UPDATE
        )
        val expectedResult = Success(data)

        verifyGetHomeLayoutDataUseCaseCalled(localCacheModel)

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when removeLeftCarouselAtc should remove left carousel atc widget from layout list`() {
        val warehouseId = "1"
        val localCacheModel = LocalCacheModel(warehouse_id = warehouseId)
        val dynamicChannelResponse = createDynamicChannelLayoutList(
            listOf(
                HomeLayoutResponse(
                    id = "2122",
                    layout = "left_carousel_atc",
                    header = Header(
                        name = "Mix Left Atc Carousel",
                        serverTimeUnix = 0
                    )
                )
            )
        )

        onGetHomeLayoutData_thenReturn(
            layoutResponse = dynamicChannelResponse,
            localCacheModel = localCacheModel
        )

        viewModel.getHomeLayout(
            localCacheModel = localCacheModel,
            removeAbleWidgets = emptyList()
        )
        viewModel.getLayoutComponentData(
            localCacheModel = localCacheModel
        )

        val expectedResultWithLeftCarouselAtcWidget = Success(
            HomeLayoutListUiModel(
                items = listOf(
                    TokoNowChooseAddressWidgetUiModel(id = "0"),
                    createLeftCarouselAtcDataModel(
                        id = "2122",
                        headerName = "Mix Left Atc Carousel",
                        warehouseId = warehouseId
                    )
                ),
                state = TokoNowLayoutState.UPDATE
            )
        )

        verifyGetHomeLayoutDataUseCaseCalled(localCacheModel)

        viewModel.homeLayoutList.verifySuccessEquals(expectedResultWithLeftCarouselAtcWidget)

        viewModel.removeLeftCarouselAtc("2122")

        val expectedResultWithoutLeftCarouselAtcWidget = Success(
            HomeLayoutListUiModel(
                items = listOf(
                    TokoNowChooseAddressWidgetUiModel(id = "0"),
                ),
                state = TokoNowLayoutState.UPDATE
            )
        )

        verifyGetHomeLayoutDataUseCaseCalled(localCacheModel)

        viewModel.homeLayoutList.verifySuccessEquals(expectedResultWithoutLeftCarouselAtcWidget)
    }

    @Test
    fun `when removeLeftCarouselAtc throw exception should not set homeLayoutList value`() {
        onGetHomeLayoutItemList_returnNull()

        viewModel.removeLeftCarouselAtc("1")

        viewModel.homeLayoutList
            .verifyValueEquals(null)
    }


    @Test
    fun `given index is NOT between visible item index when getLayoutData should not call use case`() {
        val index = 1

        onGetHomeLayoutData_thenReturn(createHomeLayoutList())

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
        viewModel.onScroll(index, LocalCacheModel(), listOf())
    }

    @Test
    fun `when removeTickerWidget should remove ticker from home layout list`() {
        onGetHomeLayoutData_thenReturn(createHomeLayoutList())

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
        viewModel.removeTickerWidget("1")

        val expectedResult = HomeLayoutListUiModel(
            items = listOf(
                TokoNowChooseAddressWidgetUiModel(id = "0"),
                createDynamicLegoBannerDataModel(
                    "34923",
                    "",
                    "Lego Banner"
                ),
                createCategoryGridDataModel(
                    "11111",
                    "Category Tokonow",
                    null,
                    TokoNowLayoutState.HIDE
                ),
                createSliderBannerDataModel(
                    "2222",
                    "",
                    "Banner Tokonow"
                )
            ),
            state = TokoNowLayoutState.UPDATE
        )

        verifyGetHomeLayoutResponseSuccess(expectedResult)
    }

    @Test
    fun `given getTicker error when getLayoutData should remove ticker from home layout list`() {
        onGetHomeLayoutData_thenReturn(createHomeLayoutList())
        onGetTicker_thenReturn(NullPointerException())

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val expectedResult = HomeLayoutListUiModel(
            items = listOf(
                TokoNowChooseAddressWidgetUiModel(id = "0"),
                createDynamicLegoBannerDataModel(
                    "34923",
                    "",
                    "Lego Banner"
                ),
                createCategoryGridDataModel(
                    "11111",
                    "Category Tokonow",
                    null,
                    TokoNowLayoutState.HIDE
                ),
                createSliderBannerDataModel(
                    "2222",
                    "",
                    "Banner Tokonow"
                )
            ),
            state = TokoNowLayoutState.UPDATE
        )

        verifyGetHomeLayoutResponseSuccess(expectedResult)
    }

    @Test
    fun `when getProductRecomOoc success should add product recom to home layout list`() {
        val recommendationItems = listOf(RecommendationItem())
        val recommendationWidget = RecommendationWidget(
            recommendationItemList = recommendationItems
        )

        onGetRecommendation_thenReturn(listOf(recommendationWidget))

        viewModel.getProductRecomOoc()

        val homeLayoutItems = listOf(
            HomeProductRecomUiModel(
                "6",
                recommendationWidget
            )
        )

        val expectedResult = HomeLayoutListUiModel(
            items = homeLayoutItems,
            state = TokoNowLayoutState.HIDE
        )

        verifyGetHomeLayoutResponseSuccess(expectedResult)
    }

    @Test
    fun `given recom items empty when getProductRecomOoc should NOT add product recom to home layout list`() {
        val recommendationItems = emptyList<RecommendationItem>()
        val recommendationWidget = RecommendationWidget(
            recommendationItemList = recommendationItems
        )

        onGetRecommendation_thenReturn(listOf(recommendationWidget))

        viewModel.getProductRecomOoc()

        viewModel.homeLayoutList
            .verifyValueEquals(null)
    }

    @Test
    fun `given recom widget list empty when getProductRecomOoc should NOT add product recom to home layout list`() {
        val recommendationWidgetList = emptyList<RecommendationWidget>()
        onGetRecommendation_thenReturn(recommendationWidgetList)

        viewModel.getProductRecomOoc()

        viewModel.homeLayoutList
            .verifyValueEquals(null)
    }

    @Test
    fun `given mini cart item is null when addProductToCart should update product quantity`() {
        val channelId = "1001"
        val productId = "1"
        val quantity = 5
        val shopId = "5"
        val type = TokoNowLayoutType.REPURCHASE_PRODUCT

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = "1001",
                layout = "recent_purchase_tokonow",
                header = Header(
                    name = "Kamu pernah beli",
                    serverTimeUnix = 0
                )
            )
        )
        val repurchaseResponse = RepurchaseData(
            title = "Kamu pernah beli",
            products = listOf(RepurchaseProduct(id = productId, stock = 5, maxOrder = 4, minOrder = 3))
        )
        val addToCartResponse = AddToCartDataModel()

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRepurchaseWidget_thenReturn(repurchaseResponse)
        onAddToCart_thenReturn(addToCartResponse)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
        viewModel.addProductToCart(channelId, productId, quantity, shopId, type)

        val chooseAddressWidget = TokoNowChooseAddressWidgetUiModel(id = "0")
        val repurchaseUiModel = TokoNowRepurchaseUiModel(
            id = "1001",
            title = "Kamu pernah beli",
            productList = listOf(
                createHomeProductCardUiModel(
                    channelId = channelId,
                    productId = productId,
                    quantity = 4,
                    product =  ProductCardModel(
                        nonVariant = NonVariant(quantity, 3, 4)
                    ),
                    position = 1,
                    headerName = "Kamu pernah beli"
                )
            ),
            state = TokoNowLayoutState.SHOW
        )

        val homeLayoutItems = listOf(
            chooseAddressWidget,
            repurchaseUiModel
        )

        val expectedResult = Success(
            HomeLayoutListUiModel(
                items = homeLayoutItems,
                state = TokoNowLayoutState.UPDATE
            )
        )

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetRepurchaseWidgetUseCaseCalled()
        verifyAddToCartUseCaseCalled()

        viewModel.atcQuantity
            .verifySuccessEquals(expectedResult)

        viewModel.miniCartAdd
            .verifySuccessEquals(Success(AddToCartDataModel()))

        viewModel.updateToolbarNotification
            .verifyValueEquals(true)
    }

    @Test
    fun `given quantity is 0 when addProductToCart should update product quantity to 0`() {
        val warehouseId = "1"
        val channelId = "1001"
        val productId = "100"
        val quantity = 0
        val shopId = "5"
        val type = TokoNowLayoutType.REPURCHASE_PRODUCT

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = "1001",
                layout = "recent_purchase_tokonow",
                header = Header(
                    name = "Kamu pernah beli",
                    serverTimeUnix = 0
                )
            )
        )
        val repurchaseResponse = RepurchaseData(
            title = "Kamu pernah beli",
            products = listOf(RepurchaseProduct(id = productId, stock = 5, maxOrder = 4, minOrder = 3))
        )
        val miniCartItems = mapOf(MiniCartItemKey(productId) to MiniCartItem.MiniCartItemProduct(productId = productId, quantity = 1))
        val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRepurchaseWidget_thenReturn(repurchaseResponse)
        onGetMiniCart_thenReturn(miniCartResponse)
        onRemoveItemCart_thenReturn(RemoveFromCartData())
        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
        viewModel.getMiniCart(listOf(shopId), warehouseId)
        viewModel.addProductToCart(channelId, productId, quantity, shopId, type)

        val repurchaseUiModel = TokoNowRepurchaseUiModel(
            id = "1001",
            title = "Kamu pernah beli",
            productList = listOf(
                createHomeProductCardUiModel(
                    channelId = channelId,
                    productId = productId,
                    quantity = 4,
                    product =  ProductCardModel(
                        hasAddToCartButton = true,
                        nonVariant = NonVariant(quantity, 3, 4)
                    ),
                    position = 1,
                    headerName = "Kamu pernah beli"
                )
            ),
            state = TokoNowLayoutState.SHOW
        )

        val homeLayoutItems = listOf(
            TokoNowChooseAddressWidgetUiModel(id = "0"),
            repurchaseUiModel
        )

        val expectedResult = Success(
            HomeLayoutListUiModel(
                items = homeLayoutItems,
                state = TokoNowLayoutState.UPDATE
            )
        )

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetRepurchaseWidgetUseCaseCalled()
        verifyGetMiniCartUseCaseCalled()
        verifyDeleteCartUseCaseCalled()

        viewModel.atcQuantity
            .verifySuccessEquals(expectedResult)

        viewModel.miniCartRemove
            .verifySuccessEquals(Success(Pair(productId, "")))

        viewModel.updateToolbarNotification
            .verifyValueEquals(true)
    }

    @Test
    fun `given mini cart item is NOT null when addProductToCart should update product quantity`() {
        val warehouseId = "1"
        val channelId = "1001"
        val productId = "100"
        val shopId = "5"
        val type = TokoNowLayoutType.REPURCHASE_PRODUCT

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = "1001",
                layout = "recent_purchase_tokonow",
                header = Header(
                    name = "Kamu pernah beli",
                    serverTimeUnix = 0
                )
            )
        )
        val repurchaseResponse = RepurchaseData(
            title = "Kamu pernah beli",
            products = listOf(RepurchaseProduct(id = productId, maxOrder = 5, minOrder = 3))
        )
        val miniCartItems = mapOf(MiniCartItemKey(productId) to MiniCartItem.MiniCartItemProduct(productId = productId, quantity = 1))
        val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRepurchaseWidget_thenReturn(repurchaseResponse)
        onGetMiniCart_thenReturn(miniCartResponse)
        onUpdateItemCart_thenReturn(UpdateCartV2Data())
        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
        viewModel.onScroll(2, LocalCacheModel(), listOf())
        viewModel.getMiniCart(listOf(shopId), warehouseId)
        viewModel.addProductToCart(channelId, productId, 4, shopId, type)

        val expected = Success(UpdateCartV2Data())

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetRepurchaseWidgetUseCaseCalled()
        verifyGetMiniCartUseCaseCalled()
        verifyUpdateCartUseCaseCalled()

        viewModel.miniCartUpdate
            .verifySuccessEquals(expected)

        viewModel.updateToolbarNotification
            .verifyValueEquals(true)
    }

    @Test
    fun `given getRepurchaseProduct error when getHomeLayout should NOT add repurchase widget`() {
        val error = NullPointerException()

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = "1001",
                layout = "recent_purchase_tokonow",
                header = Header(
                    name = "Kamu pernah beli",
                    serverTimeUnix = 0
                )
            )
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRepurchaseWidget_thenReturn(error)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val homeLayoutItems = listOf(
            TokoNowChooseAddressWidgetUiModel(id = "0")
        )

        val expectedResult = Success(
            HomeLayoutListUiModel(
                items = homeLayoutItems,
                state = TokoNowLayoutState.UPDATE
            )
        )

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetRepurchaseWidgetUseCaseCalled()

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `given getRepurchaseProduct empty when getHomeLayout should NOT add repurchase widget`() {
        val repurchaseProductResponse = RepurchaseData(
            title = "Kamu pernah beli",
            products = listOf()
        )

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = "1001",
                layout = "recent_purchase_tokonow",
                header = Header(
                    name = "Kamu pernah beli",
                    serverTimeUnix = 0
                )
            )
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRepurchaseWidget_thenReturn(repurchaseProductResponse)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val homeLayoutItems = listOf(
            TokoNowChooseAddressWidgetUiModel(id = "0")
        )

        val expectedResult = Success(
            HomeLayoutListUiModel(
                items = homeLayoutItems,
                state = TokoNowLayoutState.UPDATE
            )
        )

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetRepurchaseWidgetUseCaseCalled()

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `given miniCartSimplifiedData is NULL when getMiniCartItem should return NULL`() {
        val productId = "1"

        val expectedResult = null
        val actualResult = viewModel.getMiniCartItem(productId)

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when getMiniCartItem should return mini cart item by ID`() {
        val productId = "1"

        val miniCartItem = MiniCartItem.MiniCartItemProduct(productId = productId)
        val miniCartResponse = MiniCartSimplifiedData(miniCartItems = mapOf(MiniCartItemKey(productId) to miniCartItem))

        onGetMiniCart_thenReturn(miniCartResponse)
        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)

        viewModel.getMiniCart(listOf("1"), "2")

        val expectedResult = MiniCartItem.MiniCartItemProduct(productId = productId)
        val actualResult = viewModel.getMiniCartItem(productId)

        verifyGetMiniCartUseCaseCalled()
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `given productId not match when getMiniCartItem should return NULL`() {
        val productId = "3"

        val miniCartItem = MiniCartItem.MiniCartItemProduct(productId = "1")
        val miniCartResponse = MiniCartSimplifiedData(miniCartItems = mapOf(MiniCartItemKey("1") to miniCartItem))

        onGetMiniCart_thenReturn(miniCartResponse)
        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)

        viewModel.getMiniCart(listOf("1"), "2")

        val expectedResult = null
        val actualResult = viewModel.getMiniCartItem(productId)

        verifyGetMiniCartUseCaseCalled()
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `given homeLayoutItemList contains repurchase when getRepurchaseWidgetProducts should product list`() {
        val channelId = "1001"
        val productId = "1"

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = "1001",
                layout = "recent_purchase_tokonow",
                header = Header(
                    name = "Kamu pernah beli",
                    serverTimeUnix = 0
                )
            )
        )
        val repurchaseProduct = RepurchaseProduct(
            id = productId,
            stock = 5,
            maxOrder = 4,
            minOrder = 3
        )
        val repurchaseResponse = RepurchaseData(
            title = "Kamu pernah beli",
            products = listOf(repurchaseProduct)
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRepurchaseWidget_thenReturn(repurchaseResponse)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
        viewModel.onScroll(2, LocalCacheModel(), listOf())

        val expected = listOf(
            createHomeProductCardUiModel(
                channelId = channelId,
                productId = productId,
                quantity = 4,
                product = ProductCardModel(
                    hasAddToCartButton = true,
                    nonVariant = NonVariant(minQuantity = 3, maxQuantity = 4)
                ),
                position = 1,
                headerName = "Kamu pernah beli"
            )
        )
        val actual = viewModel.getRepurchaseProducts()

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetRepurchaseWidgetUseCaseCalled()
        assertEquals(expected, actual)
    }

    @Test
    fun `given homeLayoutItemList does NOT contain repurchase when getRepurchaseWidgetProducts should return empty list`() {
        onGetHomeLayoutData_thenReturn(createHomeLayoutList())

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val expected = emptyList<TokoNowProductCardUiModel>()
        val actual = viewModel.getRepurchaseProducts()

        verifyGetHomeLayoutDataUseCaseCalled()
        assertEquals(expected, actual)
    }

    @Test
    fun `given homeLayoutItemList is empty when getRepurchaseWidgetProducts should return empty list`() {
        val expected = emptyList<TokoNowProductCardUiModel>()
        val actual = viewModel.getRepurchaseProducts()

        assertEquals(expected, actual)
    }

    @Test
    fun `when get home recom success should add recom widget to home layout list`() {
        val channelId = "1001"
        val homeRecomResponse = HomeLayoutResponse(
            id = "1001",
            layout = "top_carousel_tokonow",
            header = Header(
                name = "Lagi Diskon",
                serverTimeUnix = 0
            ),
            grids = listOf(Grid(id = "2"))
        )
        val homeLayoutResponse = listOf(homeRecomResponse)
        onGetHomeLayoutData_thenReturn(homeLayoutResponse)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val recomItemList = listOf(RecommendationItem(productId = 2, isRecomProductShowVariantAndCart = true, price = "0", position = 1))
        val recomWidget = RecommendationWidget(title = "Lagi Diskon", recommendationItemList = recomItemList)
        val realTimeRecom = HomeRealTimeRecomUiModel(channelId = channelId, headerName = "Lagi Diskon")

        val homeRecomUiModel = HomeProductRecomUiModel(
            id = "1001",
            recomWidget = recomWidget,
            realTimeRecom = realTimeRecom
        )

        val homeLayoutItems = listOf(
            TokoNowChooseAddressWidgetUiModel(id = "0"),
            homeRecomUiModel
        )

        val expectedResult = Success(
            HomeLayoutListUiModel(
                items = homeLayoutItems,
                state = TokoNowLayoutState.UPDATE
            )
        )

        verifyGetHomeLayoutDataUseCaseCalled()

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when get repurchase success should add repurchase to home layout list`() {
        val channelId = "1001"
        val productId = "1"

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = "1001",
                layout = "recent_purchase_tokonow",
                header = Header(
                    name = "Kamu pernah beli",
                    serverTimeUnix = 0
                )
            )
        )
        val repurchaseProduct = RepurchaseProduct(
            id = productId,
            stock = 5,
            maxOrder = 4,
            minOrder = 3
        )
        val repurchaseResponse = RepurchaseData(
            title = "Kamu pernah beli",
            products = listOf(repurchaseProduct)
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRepurchaseWidget_thenReturn(repurchaseResponse)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
        viewModel.onScroll(2, LocalCacheModel(), listOf())

        val repurchaseUiModel = TokoNowRepurchaseUiModel(
            id = "1001",
            title = "Kamu pernah beli",
            productList = listOf(
                createHomeProductCardUiModel(
                    channelId = channelId,
                    productId = productId,
                    quantity = 4,
                    product =  ProductCardModel(
                        hasAddToCartButton = true,
                        nonVariant = NonVariant(0, 3, 4)
                    ),
                    position = 1,
                    headerName = "Kamu pernah beli"
                )
            ),
            state = TokoNowLayoutState.SHOW
        )

        val homeLayoutItems = listOf(
            TokoNowChooseAddressWidgetUiModel(id = "0"),
            repurchaseUiModel
        )

        val expectedResult = Success(
            HomeLayoutListUiModel(
                items = homeLayoutItems,
                state = TokoNowLayoutState.UPDATE
            )
        )

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetRepurchaseWidgetUseCaseCalled()

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when add product recom to cart should track add product recom to cart`() {
        val channelId = "1001"
        val productId = "2"
        val quantity = 5
        val shopId = "5"
        val cartId = "1999"
        val type = TokoNowLayoutType.PRODUCT_RECOM

        val homeRecomResponse = HomeLayoutResponse(
            id = "1001",
            layout = "top_carousel_tokonow",
            header = Header(
                name = "Lagi Diskon",
                serverTimeUnix = 0
            ),
            grids = listOf(Grid(id = "1"), Grid(id = "2"))
        )
        val homeLayoutResponse = listOf(homeRecomResponse)

        val addToCartResponse = AddToCartDataModel(data = DataModel(cartId = cartId))

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onAddToCart_thenReturn(addToCartResponse)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
        viewModel.addProductToCart(channelId, productId, quantity, shopId, type)

        val recomItemList = listOf(
            RecommendationItem(productId = 1, isRecomProductShowVariantAndCart = true, price = "0", position = 1),
            RecommendationItem(productId = 2, isRecomProductShowVariantAndCart = true, price = "0", quantity = quantity, position = 2)
        )
        val recomWidget = RecommendationWidget(title = "Lagi Diskon", recommendationItemList = recomItemList)
        val realTimeRecom = HomeRealTimeRecomUiModel(channelId = channelId, headerName = "Lagi Diskon")

        val homeRecomUiModel = HomeProductRecomUiModel(
            id = "1001",
            recomWidget = recomWidget,
            realTimeRecom = realTimeRecom
        )

        val expectedResult = HomeAddToCartTracker(
            position = 2,
            quantity = quantity,
            cartId = cartId,
            homeRecomUiModel
        )

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyAddToCartUseCaseCalled()

        viewModel.homeAddToCartTracker
            .verifyValueEquals(expectedResult)
    }

    @Test
    fun `when update product recom cart item should track update product recom`() {
        val warehouseId = "1"
        val channelId = "1001"
        val productId = "1"
        val shopId = "5"
        val cartId = "1999"
        val type = TokoNowLayoutType.PRODUCT_RECOM

        val homeRecomResponse = HomeLayoutResponse(
            id = "1001",
            layout = "top_carousel_tokonow",
            header = Header(
                name = "Lagi Diskon",
                serverTimeUnix = 0
            ),
            grids = listOf(Grid(id = "1"), Grid(id = "2"))
        )

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = "1001",
                layout = "top_carousel_tokonow",
                header = Header(
                    name = "Lagi Diskon",
                    serverTimeUnix = 0
                )
            ),
            homeRecomResponse
        )

        val updateCartResponse = UpdateCartV2Data()
        val miniCartItems = mapOf(MiniCartItemKey(productId) to MiniCartItem.MiniCartItemProduct(productId = productId, quantity = 1, cartId = cartId))
        val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetMiniCart_thenReturn(miniCartResponse)
        onUpdateItemCart_thenReturn(updateCartResponse)
        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
        viewModel.onScroll(2, LocalCacheModel(), listOf())
        viewModel.getMiniCart(listOf(shopId), warehouseId)
        viewModel.addProductToCart(channelId, productId, 4, shopId, type)

        val recomItemList = listOf(
            RecommendationItem(productId = 1, isRecomProductShowVariantAndCart = true, price = "0", quantity = 4, position = 1),
            RecommendationItem(productId = 2, isRecomProductShowVariantAndCart = true, price = "0", quantity = 0, position = 2)
        )
        val recomWidget = RecommendationWidget(title = "Lagi Diskon", recommendationItemList = recomItemList)
        val realTimeRecom = HomeRealTimeRecomUiModel(channelId = channelId, headerName = "Lagi Diskon")

        val homeRecomUiModel = HomeProductRecomUiModel(
            id = "1001",
            recomWidget = recomWidget,
            realTimeRecom = realTimeRecom
        )

        val expected = HomeAddToCartTracker(
            position = 1,
            quantity = 4,
            cartId = cartId,
            data = homeRecomUiModel
        )

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetMiniCartUseCaseCalled()
        verifyUpdateCartUseCaseCalled()

        viewModel.homeAddToCartTracker
            .verifyValueEquals(expected)
    }

    @Test
    fun `given update cart error when update product cart item should set miniCartRemove value fail`() {
        val error = NullPointerException()
        val warehouseId = "1"
        val channelId = "1001"
        val productId = "1"
        val shopId = "5"
        val cartId = "1999"
        val type = TokoNowLayoutType.PRODUCT_RECOM

        val homeRecomResponse = HomeLayoutResponse(
            id = "1001",
            layout = "top_carousel_tokonow",
            header = Header(
                name = "Lagi Diskon",
                serverTimeUnix = 0
            ),
            grids = listOf(Grid(id = "1"), Grid(id = "2"))
        )

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = "1001",
                layout = "top_carousel_tokonow",
                header = Header(
                    name = "Lagi Diskon",
                    serverTimeUnix = 0
                )
            ),
            homeRecomResponse
        )

        val miniCartItems = mapOf(MiniCartItemKey(productId) to MiniCartItem.MiniCartItemProduct(productId = productId, quantity = 1, cartId = cartId))
        val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetMiniCart_thenReturn(miniCartResponse)
        onUpdateItemCart_thenReturn(error)
        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
        viewModel.onScroll(2, LocalCacheModel(), listOf())
        viewModel.getMiniCart(listOf(shopId), warehouseId)
        viewModel.addProductToCart(channelId, productId, 4, shopId, type)

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetMiniCartUseCaseCalled()
        verifyUpdateCartUseCaseCalled()

        viewModel.miniCartUpdate
            .verifyErrorEquals(Fail(error))
    }

    @Test
    fun `when remove product recom from cart should track remove product recom`() {
        val warehouseId = "1"
        val channelId = "1001"
        val productId = "1"
        val shopId = "5"
        val cartId = "1999"
        val type = TokoNowLayoutType.PRODUCT_RECOM

        val homeRecomResponse = HomeLayoutResponse(
            id = "1001",
            layout = "top_carousel_tokonow",
            header = Header(
                name = "Lagi Diskon",
                serverTimeUnix = 0
            ),
            grids = listOf(Grid(id = "1"), Grid(id = "2"))
        )

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = "1001",
                layout = "top_carousel_tokonow",
                header = Header(
                    name = "Lagi Diskon",
                    serverTimeUnix = 0
                )
            ),
            homeRecomResponse
        )

        val miniCartItems = mapOf(MiniCartItemKey(productId) to MiniCartItem.MiniCartItemProduct(productId = productId, quantity = 1, cartId = cartId))
        val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetMiniCart_thenReturn(miniCartResponse)
        onRemoveItemCart_thenReturn(RemoveFromCartData())
        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
        viewModel.onScroll(2, LocalCacheModel(), listOf())
        viewModel.getMiniCart(listOf(shopId), warehouseId)
        viewModel.addProductToCart(channelId, productId, 0, shopId, type)

        val recomItemList = listOf(
            RecommendationItem(productId = 1, isRecomProductShowVariantAndCart = true, price = "0", quantity = 0, position = 1),
            RecommendationItem(productId = 2, isRecomProductShowVariantAndCart = true, price = "0", quantity = 0, position = 2)
        )
        val recomWidget = RecommendationWidget(title = "Lagi Diskon", recommendationItemList = recomItemList)
        val realTimeRecom = HomeRealTimeRecomUiModel(channelId = channelId, headerName = "Lagi Diskon")

        val homeRecomUiModel = HomeProductRecomUiModel(
            id = "1001",
            recomWidget = recomWidget,
            realTimeRecom = realTimeRecom
        )

        val expected = HomeRemoveFromCartTracker(
            position = 1,
            quantity = 0,
            cartId = cartId,
            data = homeRecomUiModel
        )

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetMiniCartUseCaseCalled()
        verifyDeleteCartUseCaseCalled()

        viewModel.homeRemoveFromCartTracker
            .verifyValueEquals(expected)
    }


    @Test
    fun `homeLayoutItemList does NOT contain product recom when remove from cart should NOT track the product`() {
        val warehouseId = "1"
        val channelId = "1001"
        val productId = "1"
        val shopId = "5"
        val cartId = "1999"
        val type = TokoNowLayoutType.PRODUCT_RECOM

        val miniCartItems = mapOf(MiniCartItemKey(productId) to MiniCartItem.MiniCartItemProduct(productId = productId, quantity = 1, cartId = cartId))
        val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

        onGetMiniCart_thenReturn(miniCartResponse)
        onRemoveItemCart_thenReturn(RemoveFromCartData())
        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)

        viewModel.getMiniCart(listOf(shopId), warehouseId)
        viewModel.addProductToCart(channelId, productId, 0, shopId, type)

        viewModel.homeRemoveFromCartTracker
            .verifyValueEquals(null)
    }

    @Test
    fun `given delete cart error when remove product from cart should set miniCartRemove fail`() {
        val error = NullPointerException()
        val warehouseId = "1"
        val channelId = "1001"
        val productId = "1"
        val shopId = "5"
        val cartId = "1999"
        val type = TokoNowLayoutType.PRODUCT_RECOM

        val homeRecomResponse = HomeLayoutResponse(
            id = "1001",
            layout = "top_carousel_tokonow",
            header = Header(
                name = "Lagi Diskon",
                serverTimeUnix = 0
            ),
            grids = listOf(Grid(id = "1"), Grid(id = "2"))
        )

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = "1001",
                layout = "top_carousel_tokonow",
                header = Header(
                    name = "Lagi Diskon",
                    serverTimeUnix = 0
                )
            ),
            homeRecomResponse
        )

        val miniCartItems = mapOf(MiniCartItemKey(productId) to MiniCartItem.MiniCartItemProduct(productId = productId, quantity = 1, cartId = cartId))
        val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetMiniCart_thenReturn(miniCartResponse)
        onRemoveItemCart_thenReturn(error)
        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
        viewModel.onScroll(2, LocalCacheModel(), listOf())
        viewModel.getMiniCart(listOf(shopId), warehouseId)
        viewModel.addProductToCart(channelId, productId, 0, shopId, type)

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetMiniCartUseCaseCalled()
        verifyDeleteCartUseCaseCalled()

        viewModel.miniCartRemove
            .verifyErrorEquals(Fail(error))
    }

    @Test
    fun `given homeLayoutResponse does NOT contain product recom when add to cart should NOT track add product`() {
        val channelId = "1001"
        val productId = "2"
        val quantity = 5
        val shopId = "5"
        val cartId = "1999"
        val type = TokoNowLayoutType.PRODUCT_RECOM

        val homeLayoutResponse = emptyList<HomeLayoutResponse>()
        val homeRecomResponse = HomeLayoutResponse(
            id = "1001",
            layout = "top_carousel_tokonow",
            header = Header(
                name = "Lagi Diskon",
                serverTimeUnix = 0
            ),
            grids = listOf(Grid(id = "1"), Grid(id = "2"))
        )

        val addToCartResponse = AddToCartDataModel(data = DataModel(cartId = cartId))

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onAddToCart_thenReturn(addToCartResponse)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        onGetHomeLayoutData_thenReturn(listOf(homeRecomResponse))

        viewModel.onScroll(2, LocalCacheModel(), listOf())
        viewModel.addProductToCart(channelId, productId, quantity, shopId, type)

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyAddToCartUseCaseCalled()

        viewModel.homeAddToCartTracker
            .verifyValueEquals(expected = null)
    }

    @Test
    fun `given product NOT found when add product recom to cart should NOT track add product`() {
        val channelId = "1001"
        val quantity = 5
        val shopId = "5"
        val cartId = "1999"
        val type = TokoNowLayoutType.PRODUCT_RECOM

        val homeRecomResponse = HomeLayoutResponse(
            id = "1001",
            layout = "top_carousel_tokonow",
            header = Header(
                name = "Lagi Diskon",
                serverTimeUnix = 0
            ),
            grids = listOf(Grid(id = "1"), Grid(id = "2"))
        )

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = "1001",
                layout = "top_carousel_tokonow",
                header = Header(
                    name = "Lagi Diskon",
                    serverTimeUnix = 0
                )
            ),
            homeRecomResponse
        )

        val addToCartResponse = AddToCartDataModel(data = DataModel(cartId = cartId))

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onAddToCart_thenReturn(addToCartResponse)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
        viewModel.onScroll(2, LocalCacheModel(), listOf())
        viewModel.addProductToCart(channelId, "3", quantity, shopId, type)

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyAddToCartUseCaseCalled()

        viewModel.homeAddToCartTracker
            .verifyValueEquals(expected = null)
    }

    @Test
    fun `given recommendation list is empty when add product recom to cart should NOT track add product`() {
        val channelId = "1001"
        val quantity = 5
        val shopId = "5"
        val cartId = "1999"
        val type = TokoNowLayoutType.PRODUCT_RECOM

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = "1001",
                layout = "top_carousel_tokonow",
                header = Header(
                    name = "Lagi Diskon",
                    serverTimeUnix = 0
                )
            )
        )

        val homeRecomResponse = HomeLayoutResponse(
            id = "1001",
            layout = "top_carousel_tokonow",
            header = Header(
                name = "Lagi Diskon",
                serverTimeUnix = 0
            ),
            grids = emptyList()
        )

        val addToCartResponse = AddToCartDataModel(data = DataModel(cartId = cartId))

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onAddToCart_thenReturn(addToCartResponse)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        onGetHomeLayoutData_thenReturn(listOf(homeRecomResponse))

        viewModel.onScroll(2, LocalCacheModel(), listOf())
        viewModel.addProductToCart(channelId, "3", quantity, shopId, type)

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyAddToCartUseCaseCalled()

        viewModel.homeAddToCartTracker
            .verifyValueEquals(expected = null)
    }

    @Test
    fun `when add repurchase product to cart should track add repurchase product`() {
        val channelId = "1001"

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = "1001",
                layout = "recent_purchase_tokonow",
                header = Header(
                    name = "Kamu pernah beli",
                    serverTimeUnix = 0
                )
            )
        )

        val repurchaseResponse = RepurchaseData(
            title = "Kamu pernah beli",
            products = listOf(
                RepurchaseProduct(
                    id = "1",
                    stock = 5,
                    maxOrder = 4,
                    minOrder = 3
                ),
                RepurchaseProduct(
                    id = "2",
                    stock = 3,
                    maxOrder = 4,
                    minOrder = 1
                )
            )
        )

        val addToCartResponse = AddToCartDataModel(data = DataModel(cartId = "1999"))

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRepurchaseWidget_thenReturn(repurchaseResponse)
        onAddToCart_thenReturn(addToCartResponse)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
        viewModel.onScroll(2, LocalCacheModel(), listOf())
        viewModel.addProductToCart(channelId, "2", 2, "100", TokoNowLayoutType.REPURCHASE_PRODUCT)

        val productCardUiModel = createHomeProductCardUiModel(
            channelId = channelId,
            productId = "2",
            quantity = 4,
            product =  ProductCardModel(
                hasAddToCartButton = true,
                nonVariant = NonVariant(0, 1, 4)
            ),
            position = 2,
            headerName = "Kamu pernah beli"
        )

        val expected = HomeAddToCartTracker(
            position = 2,
            quantity = 2,
            cartId = "1999",
            productCardUiModel
        )

        verifyGetRepurchaseWidgetUseCaseCalled()
        verifyAddToCartUseCaseCalled()

        viewModel.homeAddToCartTracker
            .verifyValueEquals(expected)
    }

    @Test
    fun `given product not found when add repurchase product to cart should NOT track add to cart`() {
        val channelId = "1001"

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = "1001",
                layout = "recent_purchase_tokonow",
                header = Header(
                    name = "Kamu pernah beli",
                    serverTimeUnix = 0
                )
            )
        )

        val repurchaseResponse = RepurchaseData(
            title = "Kamu pernah beli",
            products = listOf(
                RepurchaseProduct(
                    id = "1",
                    maxOrder = 5,
                    minOrder = 3
                ),
                RepurchaseProduct(
                    id = "2",
                    maxOrder = 3,
                    minOrder = 1
                )
            )
        )

        val addToCartResponse = AddToCartDataModel(data = DataModel(cartId = "1999"))

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRepurchaseWidget_thenReturn(repurchaseResponse)
        onAddToCart_thenReturn(addToCartResponse)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
        viewModel.onScroll(2, LocalCacheModel(), listOf())
        viewModel.addProductToCart(channelId, "4", 2, "100", TokoNowLayoutType.REPURCHASE_PRODUCT)

        verifyGetRepurchaseWidgetUseCaseCalled()
        verifyAddToCartUseCaseCalled()

        viewModel.homeAddToCartTracker
            .verifyValueEquals(expected = null)
    }

    @Test
    fun `given layout list does NOT contain repurchase when add product to cart should NOT track add to cart`() {
        val channelId = "1001"
        val homeLayoutResponse = emptyList<HomeLayoutResponse>()
        val addToCartResponse = AddToCartDataModel(data = DataModel(cartId = "1999"))

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onAddToCart_thenReturn(addToCartResponse)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
        viewModel.onScroll(2, LocalCacheModel(), listOf())
        viewModel.addProductToCart(channelId, "4", 2, "100", TokoNowLayoutType.REPURCHASE_PRODUCT)

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyAddToCartUseCaseCalled()

        viewModel.homeAddToCartTracker
            .verifyValueEquals(expected = null)
    }

    @Test
    fun `when add mix left atc product to cart should track add mix left atc product`() {
        val channelId = "1001"

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = "2122",
                layout = "left_carousel_atc",
                header = Header(
                    name = "Mix Left Carousel",
                    serverTimeUnix = 0
                ),
                grids = listOf(
                    Grid(
                        id = "2",
                        shop = Shop(shopId = "100")
                    )
                )
            )
        )

        val addToCartResponse = AddToCartDataModel(data = DataModel(cartId = "1999"))

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onAddToCart_thenReturn(addToCartResponse)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
        viewModel.addProductToCart(channelId, "2", 2, "100", TokoNowLayoutType.MIX_LEFT_CAROUSEL_ATC)

        val productCardUiModel = HomeLeftCarouselAtcProductCardUiModel(
            id = "2",
            channelHeaderName = "Mix Left Carousel",
            shopId = "100",
            channelId = "2122",
            productCardModel = ProductCardModel(formattedPrice = "0", nonVariant = NonVariant(quantity=0, minQuantity=0, maxQuantity=0)),
            position = 1
        )

        val expected = HomeAddToCartTracker(
            position = 1,
            quantity = 2,
            cartId = "1999",
            productCardUiModel
        )

        verifyAddToCartUseCaseCalled()

        viewModel.homeAddToCartTracker
            .verifyValueEquals(expected)
    }

    @Test
    fun `given product not found when add mix left product to cart should NOT track add to cart`() {
        val channelId = "1001"

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = "2122",
                layout = "left_carousel",
                header = Header(
                    name = "Mix Left Carousel",
                    serverTimeUnix = 0
                ),
                grids = listOf(
                    Grid(
                        id = "2",
                        shop = Shop(shopId = "100")
                    )
                )
            )
        )

        val addToCartResponse = AddToCartDataModel(data = DataModel(cartId = "1999"))

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onAddToCart_thenReturn(addToCartResponse)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
        viewModel.onScroll(2, LocalCacheModel(), listOf())
        viewModel.addProductToCart(channelId, "4", 2, "100", TokoNowLayoutType.MIX_LEFT_CAROUSEL_ATC)

        verifyAddToCartUseCaseCalled()

        viewModel.homeAddToCartTracker
            .verifyValueEquals(expected = null)
    }

    @Test
    fun `given no product in response grid when add mix left product to cart should NOT track add to cart`() {
        val channelId = "1001"

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = "2122",
                layout = "left_carousel_atc",
                header = Header(
                    name = "Mix Left Carousel",
                    serverTimeUnix = 0
                ),
                grids = listOf()
            )
        )

        val addToCartResponse = AddToCartDataModel(data = DataModel(cartId = "1999"))

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onAddToCart_thenReturn(addToCartResponse)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
        viewModel.onScroll(2, LocalCacheModel(), listOf())
        viewModel.addProductToCart(channelId, "4", 2, "100", TokoNowLayoutType.MIX_LEFT_CAROUSEL_ATC)

        verifyAddToCartUseCaseCalled()

        viewModel.homeAddToCartTracker
            .verifyValueEquals(expected = null)
    }

    @Test
    fun `given layout list does NOT contain mix left when add product to cart should NOT track add to cart`() {
        val channelId = "1001"
        val homeLayoutResponse = emptyList<HomeLayoutResponse>()
        val addToCartResponse = AddToCartDataModel(data = DataModel(cartId = "1999"))

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onAddToCart_thenReturn(addToCartResponse)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
        viewModel.onScroll(2, LocalCacheModel(), listOf())
        viewModel.addProductToCart(channelId, "4", 2, "100", TokoNowLayoutType.MIX_LEFT_CAROUSEL_ATC)

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyAddToCartUseCaseCalled()

        viewModel.homeAddToCartTracker
            .verifyValueEquals(expected = null)
    }

    @Test
    fun `given add to cart error when addProductToCart should set miniCartAdd value fail`() {
        val error = NullPointerException()
        val invalidLayoutType = "random layout type"
        val channelId = "1001"
        val productId = "4"
        val quantity = 2
        val shopId = "100"

        onAddToCart_thenReturn(error)

        viewModel.addProductToCart(channelId, productId, quantity, shopId, invalidLayoutType)

        viewModel.miniCartAdd
            .verifyErrorEquals(Fail(error))
    }

    @Test
    fun `when layout type is NOT valid should NOT track add to cart`() {
        val invalidLayoutType = "random layout type"
        val channelId = "1001"
        val productId = "4"
        val quantity = 2
        val shopId = "100"

        onAddToCart_thenReturn(AddToCartDataModel())

        viewModel.addProductToCart(channelId, productId, quantity, shopId, invalidLayoutType)

        viewModel.homeAddToCartTracker
            .verifyValueEquals(expected = null)
    }

    @Test
    fun `given user scroll tokomart home when load more should add all banner to home layout list`() {
        val firstBanner = HomeLayoutResponse(
            id = "2222",
            layout = "banner_carousel_v2",
            header = Header(
                name = "Banner Tokonow",
                serverTimeUnix = 0
            ),
            token = "==advdf299c" // dummy token
        )

        val secondBanner = HomeLayoutResponse(
            id = "3333",
            layout = "banner_carousel_v2",
            header = Header(
                name = "Banner Tokonow",
                serverTimeUnix = 0
            )
        )

        val homeLayoutResponse = listOf(firstBanner)
        onGetHomeLayoutData_thenReturn(homeLayoutResponse)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        onGetHomeLayoutData_thenReturn(listOf(secondBanner))

        viewModel.onScroll(1, LocalCacheModel(), listOf())

        val layoutList = listOf(
            TokoNowChooseAddressWidgetUiModel(id = "0"),
            BannerDataModel(
                channelModel = ChannelModel(
                    id = "2222",
                    groupId = "",
                    style = ChannelStyle.ChannelHome,
                    channelHeader = ChannelHeader(name = "Banner Tokonow"),
                    channelConfig = ChannelConfig(layout = "banner_carousel_v2"),
                    layout = "banner_carousel_v2"
                )
            ),
            BannerDataModel(
                channelModel = ChannelModel(
                    id = "3333",
                    groupId = "",
                    style = ChannelStyle.ChannelHome,
                    channelHeader = ChannelHeader(name = "Banner Tokonow"),
                    channelConfig = ChannelConfig(layout = "banner_carousel_v2"),
                    layout = "banner_carousel_v2"
                )
            )
        )

        val expected = Success(
            HomeLayoutListUiModel(
                items = layoutList,
                state = TokoNowLayoutState.LOAD_MORE
            )
        )

        verifyGetHomeLayoutDataUseCaseCalled(times = 2)

        viewModel.homeLayoutList
            .verifySuccessEquals(expected)
    }

    @Test
    fun `when removeSharingEducationWidget should remove education widget from home layout list`() {
        val channelId = "34923"
        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = channelId,
                layout = "tokonow_usp",
                header = Header(
                    name = "Education",
                    serverTimeUnix = 0
                )
            )
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        verifyGetHomeLayoutDataUseCaseCalled()

        viewModel.removeWidget(channelId)

        val items = listOf(
            TokoNowChooseAddressWidgetUiModel(id = "0")
        )

        val expectedResult = HomeLayoutListUiModel(
            items = items,
            state = TokoNowLayoutState.UPDATE
        )

        viewModel.homeLayoutList
            .verifySuccessEquals(Success(expectedResult))
    }

    @Test
    fun `given user has repurchase product when getHomeLayout should add sharing education widget to home layout list`() {
        val channelId = "34923"
        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = channelId,
                layout = "tokonow_share",
                header = Header(
                    name = "Education",
                    serverTimeUnix = 0
                )
            )
        )
        val repurchaseResponse = RepurchaseData(
            title = "Kamu pernah beli",
            products = listOf(RepurchaseProduct(id = "1", stock = 5, maxOrder = 4, minOrder = 3))
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRepurchaseWidget_thenReturn(repurchaseResponse)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val items = listOf(
            TokoNowChooseAddressWidgetUiModel(id = "0"),
            HomeSharingEducationWidgetUiModel(
                id = channelId,
                HomeLayoutItemState.LOADED,
                R.string.tokopedianow_home_sharing_education_button,
                ""
            )
        )

        val expectedResult = HomeLayoutListUiModel(
            items = items,
            state = TokoNowLayoutState.UPDATE
        )

        verifyGetRepurchaseWidgetUseCaseCalled()

        viewModel.homeLayoutList
            .verifySuccessEquals(Success(expectedResult))
    }

    @Test
    fun `given hasSharingEducationBeenRemoved true when getHomeLayout should NOT add sharing education`() {
        val hasSharingEducationBeenRemoved = true
        val channelId = "34923"
        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = channelId,
                layout = "tokonow_share",
                header = Header(
                    name = "Education",
                    serverTimeUnix = 0
                )
            )
        )
        val repurchaseResponse = RepurchaseData(
            title = "Kamu pernah beli",
            products = listOf(RepurchaseProduct(id = "1", stock = 5, maxOrder = 4, minOrder = 3))
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRepurchaseWidget_thenReturn(repurchaseResponse)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = mutableListOf(
                HomeRemoveAbleWidget(TokoNowLayoutType.SHARING_EDUCATION, hasSharingEducationBeenRemoved)
            )
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val items = listOf(
            TokoNowChooseAddressWidgetUiModel(id = "0")
        )

        val expectedResult = HomeLayoutListUiModel(
            items = items,
            state = TokoNowLayoutState.UPDATE
        )

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetRepurchaseWidgetUseCaseNotCalled()

        viewModel.homeLayoutList
            .verifySuccessEquals(Success(expectedResult))
    }

    @Test
    fun `given repurchase product empty when getHomeLayout should NOT add sharing education widget`() {
        val channelId = "34923"
        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = channelId,
                layout = "tokonow_share",
                header = Header(
                    name = "Education",
                    serverTimeUnix = 0
                )
            )
        )
        val repurchaseResponse = RepurchaseData(
            title = "Kamu pernah beli",
            products = emptyList()
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRepurchaseWidget_thenReturn(repurchaseResponse)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val items = listOf(
            TokoNowChooseAddressWidgetUiModel(id = "0")
        )

        val expectedResult = HomeLayoutListUiModel(
            items = items,
            state = TokoNowLayoutState.UPDATE
        )

        verifyGetRepurchaseWidgetUseCaseCalled()

        viewModel.homeLayoutList
            .verifySuccessEquals(Success(expectedResult))
    }

    @Test
    fun `given load more token is empty when scroll tokomart home should call use case once`() {
        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = "12345",
                layout = "tokonow_share",
                header = Header(
                    name = "Education",
                    serverTimeUnix = 0
                ),
                token = "==abcd" // dummy token
            )
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val loadMoreLayoutResponse = listOf(
            HomeLayoutResponse(
                id = "12346",
                layout = "tokonow_share",
                header = Header(
                    name = "Education",
                    serverTimeUnix = 0
                ),
                token = "" // dummy token
            )
        )

        onGetHomeLayoutData_thenReturn(loadMoreLayoutResponse)

        viewModel.onScroll(0, LocalCacheModel(), listOf())
        viewModel.onScroll(1, LocalCacheModel(), listOf())

        verifyGetHomeLayoutDataUseCaseCalled(times = 2)
    }

    @Test
    fun `given home contains progress bar when onScrollTokoMartHome should call use case once`() {
        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = "34923",
                layout = "lego_3_image",
                header = Header(
                    name = "Lego Banner",
                    serverTimeUnix = 0
                ),
                token = "==sfvf" // dummy token
            ),
            HomeLayoutResponse(
                id = "11111",
                layout = "category_tokonow",
                header = Header(
                    name = "Category Tokonow",
                    serverTimeUnix = 0
                )
            ),
            HomeLayoutResponse(
                id = "2222",
                layout = "banner_carousel_v2",
                header = Header(
                    name = "Banner Tokonow",
                    serverTimeUnix = 0
                )
            )
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val progressBar = HomeLayoutItemUiModel(
            HomeProgressBarUiModel,
            HomeLayoutItemState.LOADED
        )
        addHomeLayoutItem(progressBar)

        viewModel.onScroll(4, LocalCacheModel(), listOf())

        verifyGetHomeLayoutDataUseCaseCalled(times = 1)
    }

    @Test
    fun `given unknown layout when getHomeLayout should not call other use case`() {
        val layout = "unknown"

        val firstHomeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = "34923",
                layout = layout,
                header = Header(
                    name = "Lego Banner",
                    serverTimeUnix = 0
                ),
                token = "==sfvf" // dummy token
            )
        )

        onGetHomeLayoutData_thenReturn(firstHomeLayoutResponse)

        val progressBar = HomeLayoutItemUiModel(
            HomeProgressBarUiModel,
            HomeLayoutItemState.NOT_LOADED
        )
        addHomeLayoutItem(progressBar)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        verifyGetHomeLayoutDataUseCaseCalled()

        // Other Use Case
        verifyGetCategoryListUseCaseNotCalled()
        verifyGetRepurchaseWidgetUseCaseNotCalled()
    }

    @Test
    fun `when setProductAddToCartQuantity throw exception should not set homeLayoutList value`() {
        onGetHomeLayoutItemList_returnNull()

        viewModel.setProductAddToCartQuantity(MiniCartSimplifiedData())

        viewModel.homeLayoutList
            .verifyValueEquals(null)
    }

    @Test
    fun `when removeTickerWidget throw exception should not set homeLayoutList value`() {
        onGetHomeLayoutItemList_returnNull()

        viewModel.removeTickerWidget("1")

        viewModel.homeLayoutList
            .verifyValueEquals(null)
    }

    @Test
    fun `when removeSharingEducationWidget throw exception should not set homeLayoutList value`() {
        onGetHomeLayoutItemList_returnNull()

        viewModel.removeWidget("1")

        viewModel.homeLayoutList
            .verifyValueEquals(null)
    }

    @Test
    fun `given unknown layout when getLayoutComponentData should not call any use case`() {
        val unknownLayout = HomeLayoutItemUiModel(
            UnknownLayout,
            HomeLayoutItemState.NOT_LOADED
        )
        val unknownHomeLayout = HomeLayoutItemUiModel(
            UnknownHomeLayout,
            HomeLayoutItemState.NOT_LOADED
        )
        val nullHomeLayout = HomeLayoutItemUiModel(
            null,
            HomeLayoutItemState.NOT_LOADED
        )

        addHomeLayoutItem(unknownLayout)
        addHomeLayoutItem(unknownHomeLayout)
        addHomeLayoutItem(nullHomeLayout)

        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        verifyGetCategoryListUseCaseNotCalled()
        verifyGetRepurchaseWidgetUseCaseNotCalled()
        verifyGetMiniCartUseCaseNotCalled()

        val expected = Success(
            HomeLayoutListUiModel(
                items = emptyList(),
                state = TokoNowLayoutState.UPDATE
            )
        )

        viewModel.homeLayoutList
            .verifyValueEquals(expected)
    }

    @Test
    fun `given null layout when removeTickerWidget should filter null layout from list`() {
        val nullLayout = HomeLayoutItemUiModel(
            layout = null,
            state = HomeLayoutItemState.NOT_LOADED
        )

        onGetTicker_thenReturn(createTicker())
        onGetHomeLayoutData_thenReturn(createHomeLayoutList())

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        addHomeLayoutItem(nullLayout)

        viewModel.removeTickerWidget(id = "1")

        val expectedResult = Success(HomeLayoutListUiModel(
            items = listOf(
                TokoNowChooseAddressWidgetUiModel(id = "0"),
                createDynamicLegoBannerDataModel(
                    "34923",
                    "",
                    "Lego Banner"
                ),
                createCategoryGridDataModel(
                    "11111",
                    "Category Tokonow",
                    null,
                    TokoNowLayoutState.HIDE
                ),
                createSliderBannerDataModel(
                    "2222",
                    "",
                    "Banner Tokonow"
                )
            ),
            state = TokoNowLayoutState.UPDATE
        ))

        verifyGetHomeLayoutDataUseCaseCalled()

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `given current serviceType and intended serviceType when switchService success should switch the service`() {
        val currentServiceType = "15m"
        val intendedServiceType = "2h"

        val localCacheModel = LocalCacheModel(
            service_type = currentServiceType
        )

        val userPreferenceData = SetUserPreferenceData(
            shopId = "1",
            warehouseId = "2",
            serviceType = "2h",
            warehouses = listOf(
                WarehouseData(
                    warehouseId = "2",
                    serviceType = "2h"
                )
            )
        )

        onSetUserPreference_thenReturn(userPreferenceData)

        viewModel.switchService(localCacheModel, intendedServiceType)

        val expectedResult = Success(SetUserPreferenceData(
            shopId = "1",
            warehouseId = "2",
            serviceType = "2h",
            warehouses = listOf(
                WarehouseData(
                    warehouseId = "2",
                    serviceType = "2h"
                )
            )
        ))

        verifySetUserPreferenceUseCaseCalled(
            localCacheModel = localCacheModel,
            serviceType = "2h"
        )

        viewModel.setUserPreference
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when switchService with intended service error should set live data value fail`() {
        val localCacheModel = LocalCacheModel(
            service_type = "2h"
        )
        val intendedServiceType = "15m"
        val error = NullPointerException()

        onSetUserPreference_thenReturn(error)

        viewModel.switchService(localCacheModel, intendedServiceType)

        val expectedResult = Fail(NullPointerException())

        verifySetUserPreferenceUseCaseCalled(
            localCacheModel = localCacheModel,
            serviceType = "15m"
        )

        viewModel.setUserPreference
            .verifyErrorEquals(expectedResult)
    }

    @Test
    fun `check whether need to show on board toaster or not`() {
        // 2 hour state and 20m coachmark has been shown
        var result = viewModel.needToShowOnBoardToaster(
            serviceType = ServiceType.NOW_2H,
            has20mCoachMarkBeenShown = true,
            has2hCoachMarkBeenShown = false,
            isWarehouseIdZero = false
        )
        assertEquals(true, result)

        // 20m state and 2 hour coachmark has been shown
        result = viewModel.needToShowOnBoardToaster(
            serviceType = ServiceType.NOW_15M,
            has20mCoachMarkBeenShown = false,
            has2hCoachMarkBeenShown = true,
            isWarehouseIdZero = false
        )
        assertEquals(true, result)

        // 2 hour state and 20m coachmark has not been shown
        result = viewModel.needToShowOnBoardToaster(
            serviceType = ServiceType.NOW_2H,
            has20mCoachMarkBeenShown = false,
            has2hCoachMarkBeenShown = false,
            isWarehouseIdZero = false
        )
        assertEquals(false, result)

        // 20m state and 2 hour coachmark has not been shown
        result = viewModel.needToShowOnBoardToaster(
            serviceType = ServiceType.NOW_15M,
            has20mCoachMarkBeenShown = false,
            has2hCoachMarkBeenShown = false,
            isWarehouseIdZero = false
        )
        assertEquals(false, result)

        // ooc state
        result = viewModel.needToShowOnBoardToaster(
            serviceType = ServiceType.NOW_OOC,
            has20mCoachMarkBeenShown = false,
            has2hCoachMarkBeenShown = false,
            isWarehouseIdZero = true
        )
        assertEquals(false, result)
    }

    @Test
    fun `given current serviceType 2h and external serviceType 20m when switchService success should switch service to 15m`() {
        val currentServiceType = "2h"

        val localCacheModel = LocalCacheModel(
            service_type = currentServiceType
        )

        val userPreferenceData = SetUserPreferenceData(
            shopId = "1",
            warehouseId = "3",
            serviceType = "2h",
            warehouses = listOf(
                WarehouseData(
                    warehouseId = "2",
                    serviceType = "15m"
                ),
                WarehouseData(
                    warehouseId = "3",
                    serviceType = "2h"
                )
            )
        )

        onSetUserPreference_thenReturn(userPreferenceData)

        viewModel.switchServiceOrLoadLayout("20m", localCacheModel)

        val expectedResult = Success(SetUserPreferenceData(
            shopId = "1",
            warehouseId = "3",
            serviceType = "2h",
            warehouses = listOf(
                WarehouseData(
                    warehouseId = "2",
                    serviceType = "15m"
                ),
                WarehouseData(
                    warehouseId = "3",
                    serviceType = "2h"
                )
            )
        ))

        verifySetUserPreferenceUseCaseCalled(
            localCacheModel = localCacheModel,
            serviceType = "20m"
        )

        viewModel.setUserPreference
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `given current serviceType 15m and external serviceType 2h when switchService success should switch service to 2h`() {
        val currentServiceType = "15m"

        val localCacheModel = LocalCacheModel(
            service_type = currentServiceType
        )

        val userPreferenceData = SetUserPreferenceData(
            shopId = "1",
            warehouseId = "2",
            serviceType = "15m",
            warehouses = listOf(
                WarehouseData(
                    warehouseId = "2",
                    serviceType = "15m"
                ),
                WarehouseData(
                    warehouseId = "3",
                    serviceType = "2h"
                )
            )
        )

        onSetUserPreference_thenReturn(userPreferenceData)

        viewModel.switchServiceOrLoadLayout("2h", localCacheModel)

        val expectedResult = Success(SetUserPreferenceData(
            shopId = "1",
            warehouseId = "2",
            serviceType = "15m",
            warehouses = listOf(
                WarehouseData(
                    warehouseId = "2",
                    serviceType = "15m"
                ),
                WarehouseData(
                    warehouseId = "3",
                    serviceType = "2h"
                )
            )
        ))

        verifySetUserPreferenceUseCaseCalled(
            localCacheModel = localCacheModel,
            serviceType = "2h"
        )

        viewModel.setUserPreference
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when external serviceType empty should not switch service`() {
        val currentServiceType = "15m"

        val localCacheModel = LocalCacheModel(
            service_type = currentServiceType
        )

        viewModel.switchServiceOrLoadLayout("", localCacheModel)

        val expectedResponse = createLoadingState()

        verifyGetHomeLayoutResponseSuccess(expectedResponse)
    }

    @Test
    fun `when external serviceType equals to current serviceType should not switch service`() {
        val currentServiceType = "2h"

        val localCacheModel = LocalCacheModel(
            service_type = currentServiceType
        )

        viewModel.switchServiceOrLoadLayout("2h", localCacheModel)

        val expectedResponse = createLoadingState()

        verifyGetHomeLayoutResponseSuccess(expectedResponse)
    }

    @Test
    fun `given current serviceType 15m when switchService success should switch service to 2h`() {
        val currentServiceType = "15m"

        val localCacheModel = LocalCacheModel(
            service_type = currentServiceType
        )

        val userPreferenceData = SetUserPreferenceData(
            shopId = "1",
            warehouseId = "2",
            serviceType = "2h",
            warehouses = listOf(
                WarehouseData(
                    warehouseId = "2",
                    serviceType = "2h"
                )
            )
        )

        onSetUserPreference_thenReturn(userPreferenceData)

        viewModel.switchService(localCacheModel)

        val expectedResult = Success(SetUserPreferenceData(
            shopId = "1",
            warehouseId = "2",
            serviceType = "2h",
            warehouses = listOf(
                WarehouseData(
                    warehouseId = "2",
                    serviceType = "2h"
                )
            )
        ))

        verifySetUserPreferenceUseCaseCalled(
            localCacheModel = localCacheModel,
            serviceType = "2h"
        )

        viewModel.setUserPreference
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `given current serviceType OOC when switchService success should switch service to 2h`() {
        val currentServiceType = "ooc"

        val localCacheModel = LocalCacheModel(
            service_type = currentServiceType
        )

        val userPreferenceData = SetUserPreferenceData(
            shopId = "1",
            warehouseId = "2",
            serviceType = "2h",
            warehouses = listOf(
                WarehouseData(
                    warehouseId = "2",
                    serviceType = "2h"
                )
            )
        )

        onSetUserPreference_thenReturn(userPreferenceData)

        viewModel.switchService(localCacheModel)

        val expectedResult = Success(SetUserPreferenceData(
            shopId = "1",
            warehouseId = "2",
            serviceType = "2h",
            warehouses = listOf(
                WarehouseData(
                    warehouseId = "2",
                    serviceType = "2h"
                )
            )
        ))

        verifySetUserPreferenceUseCaseCalled(
            localCacheModel = localCacheModel,
            serviceType = "2h"
        )

        viewModel.setUserPreference
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `given current serviceType 2h when switchService success should switch service to 15m`() {
        val currentServiceType = "2h"

        val localCacheModel = LocalCacheModel(
            service_type = currentServiceType
        )

        val userPreferenceData = SetUserPreferenceData(
            shopId = "1",
            warehouseId = "2",
            serviceType = "15m",
            warehouses = listOf(
                WarehouseData(
                    warehouseId = "2",
                    serviceType = "15m"
                )
            )
        )

        onSetUserPreference_thenReturn(userPreferenceData)

        viewModel.switchService(localCacheModel)

        val expectedResult = Success(SetUserPreferenceData(
            shopId = "1",
            warehouseId = "2",
            serviceType = "15m",
            warehouses = listOf(
                WarehouseData(
                    warehouseId = "2",
                    serviceType = "15m"
                )
            )
        ))

        verifySetUserPreferenceUseCaseCalled(
            localCacheModel = localCacheModel,
            serviceType = "15m"
        )

        viewModel.setUserPreference
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when switchService error should set live data value fail`() {
        val localCacheModel = LocalCacheModel(
            service_type = "2h"
        )
        val error = NullPointerException()

        onSetUserPreference_thenReturn(error)

        viewModel.switchService(localCacheModel)

        val expectedResult = Fail(NullPointerException())

        verifySetUserPreferenceUseCaseCalled(
            localCacheModel = localCacheModel,
            serviceType = "15m"
        )

        viewModel.setUserPreference
            .verifyErrorEquals(expectedResult)
    }

    @Test
    fun `given eligible for NOW15 when getHomeLayout should add NOW15 switcher widget to layout list`() {
        val serviceType = "2h"
        val localCacheModel = LocalCacheModel(
            warehouses = listOf(
                LocalWarehouseModel(warehouse_id = 12530, service_type = "15m"),
                LocalWarehouseModel(warehouse_id = 15021, service_type = "2h")
            ),
            service_type = serviceType
        )

        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)

        onGetHomeLayoutData_thenReturn(listOf(
            HomeLayoutResponse(
                id = "2",
                layout = "tokonow_usp",
                header = Header(
                    name = "Tokonow USP",
                    serverTimeUnix = 0
                ),
                token = ""
            )
        ), localCacheModel)

        viewModel.getHomeLayout(localCacheModel = localCacheModel, removeAbleWidgets = emptyList())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val layoutList = listOf(
            TokoNowChooseAddressWidgetUiModel("0"),
            HomeEducationalInformationWidgetUiModel(
                "2",
                HomeLayoutItemState.LOADED,
                ServiceType.NOW_2H
            ),
            HomeSwitcherUiModel.Home20mSwitcher()
        )

        val expectedResult = Success(HomeLayoutListUiModel(
            items = layoutList,
            state = TokoNowLayoutState.UPDATE
        ))

        verifyGetHomeLayoutDataUseCaseCalled(localCacheModel)

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `given NOT eligible for NOW15 when getHomeLayout should NOT add NOW15 switcher widget to layout list`() {
        val serviceType = "2h"
        val localCacheModel = LocalCacheModel(
            warehouses = listOf(
                LocalWarehouseModel(warehouse_id = 0, service_type = "15m"),
                LocalWarehouseModel(warehouse_id = 15021, service_type = "2h")
            ),
            service_type = serviceType
        )

        onGetHomeLayoutData_thenReturn(listOf(
            HomeLayoutResponse(
                id = "2",
                layout = "tokonow_usp",
                header = Header(
                    name = "Tokonow USP",
                    serverTimeUnix = 0
                ),
                token = ""
            )
        ), localCacheModel)

        viewModel.getHomeLayout(localCacheModel = localCacheModel, removeAbleWidgets = emptyList())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val layoutList = listOf(
            TokoNowChooseAddressWidgetUiModel("0"),
            HomeEducationalInformationWidgetUiModel(
                "2",
                HomeLayoutItemState.LOADED,
                ServiceType.NOW_2H
            )
        )

        val expectedResult = Success(HomeLayoutListUiModel(
            items = layoutList,
            state = TokoNowLayoutState.UPDATE
        ))

        verifyGetHomeLayoutDataUseCaseCalled(localCacheModel)

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `given eligible for NOW2H when getHomeLayout should add NOW2H switcher widget to layout list`() {
        val serviceType = "15m"
        val localCacheModel = LocalCacheModel(
            warehouses = listOf(
                LocalWarehouseModel(warehouse_id = 12530, service_type = "15m"),
                LocalWarehouseModel(warehouse_id = 15021, service_type = "2h")
            ),
            service_type = serviceType
        )

        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)

        onGetHomeLayoutData_thenReturn(listOf(
            HomeLayoutResponse(
                id = "2",
                layout = "tokonow_usp",
                header = Header(
                    name = "Tokonow USP",
                    serverTimeUnix = 0
                ),
                token = ""
            )
        ), localCacheModel)

        viewModel.getHomeLayout(localCacheModel = localCacheModel, removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val layoutList = listOf(
            TokoNowChooseAddressWidgetUiModel("0"),
            HomeEducationalInformationWidgetUiModel(
                "2",
                HomeLayoutItemState.LOADED,
                ServiceType.NOW_15M
            ),
            HomeSwitcherUiModel.Home2hSwitcher()
        )

        val expectedResult = Success(HomeLayoutListUiModel(
            items = layoutList,
            state = TokoNowLayoutState.UPDATE
        ))

        verifyGetHomeLayoutDataUseCaseCalled(localCacheModel)

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `given NOT eligible for NOW2H when getHomeLayout should NOT add NOW2H switcher widget to layout list`() {
        val serviceType = "15m"
        val localCacheModel = LocalCacheModel(
            warehouses = listOf(
                LocalWarehouseModel(warehouse_id = 12521, service_type = "15m"),
                LocalWarehouseModel(warehouse_id = 0, service_type = "2h")
            ),
            service_type = serviceType
        )

        onGetHomeLayoutData_thenReturn(listOf(
            HomeLayoutResponse(
                id = "2",
                layout = "tokonow_usp",
                header = Header(
                    name = "Tokonow USP",
                    serverTimeUnix = 0
                ),
                token = ""
            )
        ), localCacheModel)

        viewModel.getHomeLayout(localCacheModel = localCacheModel, removeAbleWidgets = emptyList())
        viewModel.getLayoutComponentData(localCacheModel = localCacheModel)

        val layoutList = listOf(
            TokoNowChooseAddressWidgetUiModel("0"),
            HomeEducationalInformationWidgetUiModel(
                "2",
                HomeLayoutItemState.LOADED,
                ServiceType.NOW_15M
            )
        )

        val expectedResult = Success(HomeLayoutListUiModel(
            items = layoutList,
            state = TokoNowLayoutState.UPDATE
        ))

        verifyGetHomeLayoutDataUseCaseCalled(localCacheModel)

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `given EMPTY warehouses when getHomeLayout should NOT add home switcher widget to layout list`() {
        val serviceType = "2h"
        val localCacheModel = LocalCacheModel(
            warehouses = emptyList(),
            service_type = serviceType
        )

        onGetHomeLayoutData_thenReturn(listOf(
            HomeLayoutResponse(
                id = "2",
                layout = "tokonow_usp",
                header = Header(
                    name = "Tokonow USP",
                    serverTimeUnix = 0
                ),
                token = ""
            )
        ), localCacheModel)

        viewModel.getHomeLayout(localCacheModel = localCacheModel, removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val layoutList = listOf(
            TokoNowChooseAddressWidgetUiModel("0"),
            HomeEducationalInformationWidgetUiModel(
                "2",
                HomeLayoutItemState.LOADED,
                ServiceType.NOW_2H
            )
        )

        val expectedResult = Success(HomeLayoutListUiModel(
            items = layoutList,
            state = TokoNowLayoutState.UPDATE
        ))

        verifyGetHomeLayoutDataUseCaseCalled(localCacheModel)

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `given user NOT logged in when getHomeLayout should NOT add switcher widget to layout list`() {
        val serviceType = "2h"
        val userLoggedIn = false

        val localCacheModel = LocalCacheModel(
            warehouses = listOf(
                LocalWarehouseModel(warehouse_id = 12530, service_type = "15m"),
                LocalWarehouseModel(warehouse_id = 15021, service_type = "2h")
            ),
            service_type = serviceType
        )

        onGetIsUserLoggedIn_thenReturn(userLoggedIn)

        onGetHomeLayoutData_thenReturn(listOf(
            HomeLayoutResponse(
                id = "2",
                layout = "tokonow_usp",
                header = Header(
                    name = "Tokonow USP",
                    serverTimeUnix = 0
                ),
                token = ""
            )
        ), localCacheModel)

        viewModel.getHomeLayout(localCacheModel = localCacheModel, removeAbleWidgets = emptyList())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val layoutList = listOf(
            TokoNowChooseAddressWidgetUiModel("0"),
            HomeEducationalInformationWidgetUiModel(
                "2",
                HomeLayoutItemState.LOADED,
                ServiceType.NOW_2H
            )
        )

        val expectedResult = Success(HomeLayoutListUiModel(
            items = layoutList,
            state = TokoNowLayoutState.UPDATE
        ))

        verifyGetHomeLayoutDataUseCaseCalled(localCacheModel)

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when get referral sender home success with code equals to 200 should return sharingUrlParam`() {
        val id = "155"
        val slug = "slug"
        val ogImage = "https://tkpd.com/image.png"
        val ogTitle = "Title"
        val ogDescription = "Desc"
        val textDescription = "Text desc"
        val sharingUrlParam = "123"
        val maxReward = "Rp 123.000"
        val userStatus = "100"
        val isSender = true
        val warehouseId = "15021"
        val serviceType = "2h"
        val titleSection = "Tokonow Referral"

        val localCacheModel = LocalCacheModel(
            warehouse_id = warehouseId,
            warehouses = listOf(
                LocalWarehouseModel(warehouse_id = 12530, service_type = "15m"),
                LocalWarehouseModel(warehouse_id = 15021, service_type = "2h")
            ),
            service_type = serviceType
        )

        val layoutResponse = listOf(
            HomeLayoutResponse(
                id = id,
                layout = "tokonow_referral",
                header = Header(
                    name = "Tokonow Referral",
                    serverTimeUnix = 0
                ),
                widgetParam = slug,
                token = ""
            )
        )

        val referral = HomeReferralDataModel(
            ogImage = ogImage,
            ogTitle = ogTitle,
            ogDescription = ogDescription,
            textDescription = textDescription,
            sharingUrlParam = sharingUrlParam,
            userStatus = userStatus,
            maxReward = maxReward,
            isSender = isSender,
            isEligible = true
        )

        val homeSharingWidgetUiModel = HomeSharingReferralWidgetUiModel(
            id = id,
            state = HomeLayoutItemState.LOADED,
            slug = slug,
            ogImage = ogImage,
            ogTitle = ogTitle,
            ogDescription = ogDescription,
            textDescription = textDescription,
            sharingUrlParam = sharingUrlParam,
            userStatus = userStatus,
            maxReward = maxReward,
            isSender = isSender,
            warehouseId = warehouseId,
            titleSection = titleSection
        )

        onGetHomeLayoutData_thenReturn(layoutResponse, localCacheModel)
        onGetReferralSenderHome_thenReturn(slug, referral)

        viewModel.getHomeLayout(localCacheModel = localCacheModel, removeAbleWidgets = emptyList())
        viewModel.getLayoutComponentData(localCacheModel = localCacheModel)

        val layoutList = listOf(
            TokoNowChooseAddressWidgetUiModel("0"),
            homeSharingWidgetUiModel
        )

        val expectedResult = Success(HomeLayoutListUiModel(
            items = layoutList,
            state = TokoNowLayoutState.UPDATE,
        ))

        verifyGetReferralSenderHomeUseCaseCalled(slug)

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)

        viewModel.getReferralResult
            .verifySuccessEquals(Success(referral))
    }

    @Test
    fun `when get referral not eligible should not add referral widget to home layout list`() {
        val id = "155"
        val slug = "slug"
        val isEligible = false
        val localCacheModel = LocalCacheModel()

        val layoutResponse = listOf(
            HomeLayoutResponse(
                id = id,
                layout = "tokonow_referral",
                header = Header(
                    name = "Tokonow Referral",
                    serverTimeUnix = 0
                ),
                widgetParam = slug,
                token = ""
            )
        )

        val referral = HomeReferralDataModel(isEligible = isEligible)

        onGetHomeLayoutData_thenReturn(layoutResponse, localCacheModel)
        onGetReferralSenderHome_thenReturn(slug, referral)

        viewModel.getHomeLayout(localCacheModel = localCacheModel, removeAbleWidgets = emptyList())
        viewModel.getLayoutComponentData(localCacheModel = localCacheModel)

        val layoutList = listOf(TokoNowChooseAddressWidgetUiModel("0"))
        val expectedResult = Success(HomeLayoutListUiModel(
            items = layoutList,
            state = TokoNowLayoutState.UPDATE
        ))

        verifyGetReferralSenderHomeUseCaseCalled(slug)

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)

        viewModel.getReferralResult
            .verifyValueEquals(null)
    }

    @Test
    fun `when get referral sender home error should not add referral widget to home layout list`() {
        val id = "155"
        val slug = "slug"
        val messageError = "this is an error"
        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = id,
                layout = "tokonow_referral",
                header = Header(
                    name = "Tokonow Referral",
                    serverTimeUnix = 0
                ),
                widgetParam = slug,
                token = ""
            )
        )
        val localCacheModel = LocalCacheModel()

        onGetHomeLayoutData_thenReturn(homeLayoutResponse, localCacheModel)
        onGetReferralSenderHome_thenReturn(slug, MessageErrorException(messageError))

        viewModel.getHomeLayout(localCacheModel = localCacheModel, removeAbleWidgets = emptyList())
        viewModel.getLayoutComponentData(localCacheModel = localCacheModel)

        val layoutList = listOf(TokoNowChooseAddressWidgetUiModel("0"))
        val expectedResult = Success(HomeLayoutListUiModel(
            items = layoutList,
            state = TokoNowLayoutState.UPDATE
        ))

        verifyGetReferralSenderHomeUseCaseCalled(slug)

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)

        viewModel.getReferralResult
            .verifyErrorEquals(Fail(MessageErrorException(messageError)))
    }

    @Test
    fun `when impress switcher widget should track the widget`() {
        val isImpressionTracker = true
        val localCacheModel = createLocalCacheModel()

        viewModel.trackSwitchService(localCacheModel, isImpressionTracker)
        viewModel.homeSwitchServiceTracker.verifyValueEquals(HomeSwitchServiceTracker(
            userId = "",
            whIdOrigin = "111111",
            whIdDestination = "222222",
            isNow15 = false,
            isImpressionTracker = isImpressionTracker
        ))
    }

    @Test
    fun `when trackSwitchService throws error should do nothing`() {
        val isImpressionTracker = true
        val localCacheModel = createLocalCacheModel()

        onGetUserSession_returnNull()

        viewModel.trackSwitchService(localCacheModel, isImpressionTracker)
        viewModel.homeSwitchServiceTracker.verifyValueEquals(null)
    }

    @Test
    fun `when click switcher widget should track the widget`() {
        val isImpressionTracker = false
        val localCacheModel = createLocalCacheModel(
            warehouseId = "222222",
            warehouses = listOf(
                LocalWarehouseModel(
                    warehouse_id = 111111,
                    service_type = ServiceType.NOW_2H
                ),
                LocalWarehouseModel(
                    warehouse_id = 222222,
                    service_type = ServiceType.NOW_15M
                )
            ),
            serviceType = ServiceType.NOW_15M
        )

        viewModel.trackSwitchService(localCacheModel, isImpressionTracker)
        viewModel.homeSwitchServiceTracker.verifyValueEquals(HomeSwitchServiceTracker(
            userId = "",
            whIdOrigin = "222222",
            whIdDestination = "111111",
            isNow15 = true,
            isImpressionTracker = isImpressionTracker
        ))
    }

    @Test
    fun `when click or impress switcher widget and destination warehouse id is not found`() {
        val isImpressionTracker = false
        val localCacheModel = createLocalCacheModel(
            warehouseId = "222222",
            warehouses = listOf(
                LocalWarehouseModel(
                    warehouse_id = 222222,
                    service_type = ServiceType.NOW_15M
                )
            ),
            serviceType = ServiceType.NOW_15M
        )

        viewModel.trackSwitchService(localCacheModel, isImpressionTracker)
        viewModel.homeSwitchServiceTracker.verifyValueEquals(HomeSwitchServiceTracker(
            userId = "",
            whIdOrigin = "222222",
            whIdDestination = "0",
            isNow15 = true,
            isImpressionTracker = isImpressionTracker
        ))
    }

    @Test
    fun `when get medium play widget success should add medium play widget to home layout list`() {
        val id = "1001"
        val title = "Medium Play Widget"
        val channelTag = "channel_tag"
        val appLink = "tokopedia://now"

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = id,
                layout = "play_carousel",
                header = Header(
                    name = title,
                    applink = appLink,
                    serverTimeUnix = 0
                ),
                widgetParam = channelTag
            )
        )
        val playWidgetState = PlayWidgetState()

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetPlayWidget_thenReturn(playWidgetState)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val widgetModel = playWidgetState.model.copy(title = title, actionAppLink = appLink)
        val widgetState = playWidgetState.copy(model = widgetModel)

        val playWidgetUiModel = HomePlayWidgetUiModel(
            id = id,
            widgetType = TokoNowMediumWidget(channelTag),
            playWidgetState = widgetState,
            isAutoRefresh = false
        )

        val homeLayoutItems = listOf(
            TokoNowChooseAddressWidgetUiModel(id = "0"),
            playWidgetUiModel
        )

        val expectedResult = Success(HomeLayoutListUiModel(
            items = homeLayoutItems,
            state = TokoNowLayoutState.UPDATE
        ))

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)

        viewModel.invalidatePlayImpression
            .verifyValueEquals(true)
    }

    @Test
    fun `when get small play widget success should add small play widget to home layout list`() {
        val id = "1001"
        val title = "Small Play Widget"
        val channelTag = "channel_tag"
        val appLink = "tokopedia://now"

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = id,
                layout = "play_carousel_small",
                header = Header(
                    name = title,
                    applink = appLink,
                    serverTimeUnix = 0
                ),
                widgetParam = channelTag
            )
        )
        val playWidgetState = PlayWidgetState()

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetPlayWidget_thenReturn(playWidgetState)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val widgetModel = playWidgetState.model.copy(title = title, actionAppLink = appLink)
        val widgetState = playWidgetState.copy(model = widgetModel)

        val playWidgetUiModel = HomePlayWidgetUiModel(
            id = id,
            widgetType = TokoNowSmallWidget(channelTag),
            playWidgetState = widgetState,
            isAutoRefresh = false
        )

        val homeLayoutItems = listOf(
            TokoNowChooseAddressWidgetUiModel(id = "0"),
            playWidgetUiModel
        )

        val expectedResult = Success(HomeLayoutListUiModel(
            items = homeLayoutItems,
            state = TokoNowLayoutState.UPDATE
        ))

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)

        viewModel.invalidatePlayImpression
            .verifyValueEquals(true)
    }

    @Test
    fun `when get play widget error should remove play widget from home layout list`() {
        val id = "1001"
        val title = "Play Widget"
        val channelTag = "channel_tag"
        val appLink = "tokopedia://now"

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = id,
                layout = "play_carousel",
                header = Header(
                    name = title,
                    applink = appLink,
                    serverTimeUnix = 0
                ),
                widgetParam = channelTag
            )
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetPlayWidget_thenReturn(Exception())

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val homeLayoutItems = listOf(TokoNowChooseAddressWidgetUiModel(id = "0"))

        val expectedResult = Success(HomeLayoutListUiModel(
            items = homeLayoutItems,
            state = TokoNowLayoutState.UPDATE
        ))

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when auto refresh play widget should map play widget isAutoRefresh true`() {
        val id = "1200"
        val title = "Auto Refresh Widget"
        val channelTag = "channel_tag"
        val appLink = "tokopedia://now"

        val impressHolder = object : ImpressionableModel {
            override val impressHolder: ImpressHolder = ImpressHolder()
        }

        val playWidgetState = PlayWidgetState(
            model = createPlayWidgetUiModel(
                title = title,
                appLink = appLink
            ),
            impressHolder = impressHolder
        )

        val playWidgetUiModel = HomePlayWidgetUiModel(
            id = "1200",
            widgetType = TokoNowMediumWidget(channelTag),
            playWidgetState = playWidgetState,
            isAutoRefresh = false
        )

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = id,
                layout = "play_carousel",
                header = Header(
                    name = title,
                    applink = appLink,
                    serverTimeUnix = 0
                ),
                widgetParam = channelTag
            )
        )

        val widgetState = PlayWidgetState(
            model = createPlayWidgetUiModel(),
            impressHolder = impressHolder
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetPlayWidget_thenReturn(widgetState)

        viewModel.getHomeLayout(LocalCacheModel(), emptyList())
        viewModel.autoRefreshPlayWidget(playWidgetUiModel)

        val homePlayWidgetUiModel = HomePlayWidgetUiModel(
            id = "1200",
            widgetType = TokoNowMediumWidget(channelTag),
            playWidgetState = playWidgetState,
            isAutoRefresh = true
        )

        val homeLayoutItems = listOf(
            TokoNowChooseAddressWidgetUiModel(id = "0"),
            HomeTickerUiModel(id = "1", tickers = emptyList()),
            homePlayWidgetUiModel
        )

        val expectedResult = Success(HomeLayoutListUiModel(
            items = homeLayoutItems,
            state = TokoNowLayoutState.UPDATE
        ))

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when update play widget should map total view to play widget`() {
        val id = "1200"
        val channelId = "1"
        val totalView = "30rb"
        val title = "Update Play Widget"
        val channelTag = "channel_tag"
        val appLink = "tokopedia://now"
        val playWidgetState = PlayWidgetState(
            model = createPlayWidgetUiModel(
                title = title,
                appLink = appLink,
                items = listOf(
                    PlayWidgetBannerUiModel(appLink = "", imageUrl = ""),
                    createPlayWidgetChannel(channelId = channelId, totalView = "10rb"),
                    createPlayWidgetChannel(channelId = "2", totalView = "20rb")
                )
            )
        )

        val widget = HomePlayWidgetUiModel(
            id = "1200",
            widgetType = TokoNowMediumWidget(channelTag),
            playWidgetState = playWidgetState,
            isAutoRefresh = false
        )

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = id,
                layout = "play_carousel",
                header = Header(
                    name = title,
                    applink = appLink,
                    serverTimeUnix = 0
                ),
                widgetParam = channelTag
            )
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetPlayWidget_thenReturn(playWidgetState)

        viewModel.getHomeLayout(LocalCacheModel(), emptyList())
        viewModel.getLayoutComponentData(LocalCacheModel())
        viewModel.updatePlayWidget(channelId, totalView)

        val widgetModel = playWidgetState.model.copy(
            items = listOf(
                PlayWidgetBannerUiModel(appLink = "", imageUrl = ""),
                createPlayWidgetChannel(channelId = channelId, totalView = totalView),
                createPlayWidgetChannel(channelId = "2", totalView = "20rb")
            )
        )
        val widgetState = playWidgetState.copy(model = widgetModel)

        val playWidgetUiModel = widget.copy(
            playWidgetState = widgetState,
            isAutoRefresh = false
        )

        val homeLayoutItems = listOf(
            TokoNowChooseAddressWidgetUiModel(id = "0"),
            playWidgetUiModel
        )

        val expectedResult = Success(HomeLayoutListUiModel(
            items = homeLayoutItems,
            state = TokoNowLayoutState.UPDATE
        ))

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when update play widget error should do nothing`() {
        onGetHomeLayoutItemList_returnNull()

        viewModel.updatePlayWidget("1", "20rb")

        viewModel.homeLayoutList.verifyValueEquals(null)
    }

    @Test
    fun `when auto refresh play widget error should remove widget from home layout list`() {
        val id = "1200"
        val title = "Auto Refresh Widget"
        val channelTag = "channel_tag"
        val appLink = "tokopedia://now"
        val playWidgetState = PlayWidgetState()

        val widget = HomePlayWidgetUiModel(
            id = "1200",
            widgetType = TokoNowSmallWidget(channelTag),
            playWidgetState = playWidgetState,
            isAutoRefresh = false
        )

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = id,
                layout = "play_carousel",
                header = Header(
                    name = title,
                    applink = appLink,
                    serverTimeUnix = 0
                ),
                widgetParam = channelTag
            )
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetPlayWidget_thenReturn(Exception())

        viewModel.getHomeLayout(LocalCacheModel(), emptyList())
        viewModel.autoRefreshPlayWidget(widget)

        val homeLayoutItems = listOf(
            TokoNowChooseAddressWidgetUiModel(id = "0"),
            HomeTickerUiModel(id = "1", tickers = emptyList())
        )

        val expectedResult = Success(HomeLayoutListUiModel(
            items = homeLayoutItems,
            state = TokoNowLayoutState.UPDATE
        ))

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when get evaluate join return success response`() {
        // Given
        val response = ReferralEvaluateJoinResponse(
            ReferralEvaluateJoinResponse.GamiReferralEvaluateJoinResponse()
        )
        val expectedResult = Success(response.gamiReferralEvaluteJoinResponse.toHomeReceiverDialogUiModel())

        // When
        onGetReferralEvalute_thenReturn(response)
        viewModel.getReceiverHomeDialog(anyString())

        // Then
        viewModel.referralEvaluate.verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when get evaluate join return error response`() {
        // Given
        val error = Exception()
        val expectedError = Fail(error)

        // When
        onGetReferralEvalute_thenReturn(error)
        viewModel.getReceiverHomeDialog(anyString())

        // Then
        viewModel.referralEvaluate.verifyErrorEquals(expectedError)

    }
}

