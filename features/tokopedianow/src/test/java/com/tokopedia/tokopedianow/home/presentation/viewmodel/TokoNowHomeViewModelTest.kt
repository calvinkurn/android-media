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
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalWarehouseModel
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.ProductCardModel.*
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.common.constant.ServiceType
import com.tokopedia.tokopedianow.data.*
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.home.analytic.HomeAddToCartTracker
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType.Companion.MAIN_QUEST
import com.tokopedia.tokopedianow.common.model.*
import com.tokopedia.tokopedianow.common.domain.model.RepurchaseProduct
import com.tokopedia.tokopedianow.common.domain.model.SetUserPreference.SetUserPreferenceData
import com.tokopedia.tokopedianow.common.domain.model.WarehouseData
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.VALUE.HOMEPAGE_TOKONOW
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.EMPTY_STATE_OUT_OF_COVERAGE
import com.tokopedia.tokopedianow.home.domain.model.GetRepurchaseResponse.*
import com.tokopedia.tokopedianow.home.domain.model.Grid
import com.tokopedia.tokopedianow.home.domain.model.Header
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.domain.model.HomeRemoveAbleWidget
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment.Companion.SOURCE
import com.tokopedia.tokopedianow.home.presentation.uimodel.*
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeSwitcherUiModel
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
        viewModel.getLayoutComponentData(warehouseId = "1")

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
    fun `when getHomeLayout two times should call use case twice`() {
        onGetHomeLayoutData_thenReturn(createHomeLayoutList())

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(warehouseId = "1")

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(warehouseId = "1")

        verifyGetHomeLayoutDataUseCaseCalled(times = 2)
    }

    @Test
    fun `when getting homeLayoutData should run and give the success result`() {
        onGetHomeLayoutData_thenReturn(createHomeLayoutListForBannerOnly())

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(warehouseId = "1")

        onGetHomeLayoutData_thenReturn(listOf(createHomeLayoutData()))

        viewModel.onScrollTokoMartHome(1, LocalCacheModel(), listOf())

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
        viewModel.getLayoutComponentData(warehouseId = "1")

        verifyGetHomeLayoutResponseFail()
    }

    @Test
    fun `when getting homeLayout should throw homeLayout's exception and get the failed result`() {
        onGetHomeLayoutData_thenReturn(Exception())

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(warehouseId = "1")

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
        viewModel.getLayoutComponentData(warehouseId = "1")

        //set second mock data to replace first mock data category list
        onGetCategoryList_thenReturn(createCategoryGridListSecondFetch())

        //prepare model that need to be changed
        val model = TokoNowCategoryGridUiModel(
                id="11111",
                title="Category Tokonow",
                categoryList = emptyList(),
                state= TokoNowLayoutState.SHOW
        )

        viewModel.getCategoryGrid(model, "1")

        //prepare model for expectedResult
        val expectedResponse = TokoNowCategoryGridUiModel(
            id = "11111",
            title = "Category Tokonow",
            categoryList = listOf(
                TokoNowCategoryItemUiModel(
                    id="",
                    title="",
                    imageUrl=null,
                    appLink="tokopedia-android-internal://now/category-list?warehouse_id={warehouse_id}",
                    warehouseId="1"
                ),
                TokoNowCategoryItemUiModel(
                    id = "1",
                    title = "Category 1",
                    imageUrl = "tokopedia://",
                    appLink = "tokoepdia://"
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
        viewModel.onScrollTokoMartHome(1, LocalCacheModel(), listOf())

        //set second mock data to replace first mock data category list
        onGetCategoryList_thenReturn(Exception())

        //prepare model that need to be changed
        val model = TokoNowCategoryGridUiModel(
                id="11111",
                title="Category Tokonow",
                categoryList = emptyList(),
                state= TokoNowLayoutState.SHOW
        )

        viewModel.getCategoryGrid(model, "1")

        //prepare model for expectedResult
        val expectedResponse = TokoNowCategoryGridUiModel(
            id = "11111",
            title = "Category Tokonow",
            categoryList = null,
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
        viewModel.getLayoutComponentData(warehouseId = "1")

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
        viewModel.getLayoutComponentData(warehouseId = "1")

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
        viewModel.getLayoutComponentData(warehouseId = "1")

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
        viewModel.getLayoutComponentData(warehouseId = "1")

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
        viewModel.getLayoutComponentData(warehouseId = "1")

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
        viewModel.getLayoutComponentData(warehouseId = "1")

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
        viewModel.getLayoutComponentData(warehouseId = "1")

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
                    categoryList = null,
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
    fun `given index is NOT between visible item index when getLayoutData should not call use case`() {
        val index = 1

        onGetHomeLayoutData_thenReturn(createHomeLayoutList())

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(warehouseId = "1")
        viewModel.onScrollTokoMartHome(index, LocalCacheModel(), listOf())
    }

    @Test
    fun `when removeTickerWidget should remove ticker from home layout list`() {
        onGetHomeLayoutData_thenReturn(createHomeLayoutList())

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(warehouseId = "1")
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
        viewModel.getLayoutComponentData(warehouseId = "1")

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
        viewModel.getLayoutComponentData(warehouseId = "1")
        viewModel.addProductToCart(productId, quantity, shopId, type)

        val chooseAddressWidget = TokoNowChooseAddressWidgetUiModel(id = "0")
        val repurchaseUiModel = TokoNowRepurchaseUiModel(
            id = "1001",
            title = "Kamu pernah beli",
            productList = listOf(
                createHomeProductCardUiModel(
                    productId = productId,
                    quantity = 4,
                    product =  ProductCardModel(
                        nonVariant = NonVariant(quantity, 3, 4)
                    )
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
    }

    @Test
    fun `given quantity is 0 when addProductToCart should update product quantity to 0`() {
        val warehouseId = "1"
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
        val miniCartItems = listOf(MiniCartItem(productId = productId, quantity = 1))
        val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRepurchaseWidget_thenReturn(repurchaseResponse)
        onGetMiniCart_thenReturn(miniCartResponse)
        onRemoveItemCart_thenReturn(RemoveFromCartData())
        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(warehouseId = "1")
        viewModel.getMiniCart(listOf(shopId), warehouseId)
        viewModel.addProductToCart(productId, quantity, shopId, type)

        val repurchaseUiModel = TokoNowRepurchaseUiModel(
            id = "1001",
            title = "Kamu pernah beli",
            productList = listOf(
                createHomeProductCardUiModel(
                    productId = productId,
                    quantity = 4,
                    product =  ProductCardModel(
                        hasAddToCartButton = true,
                        nonVariant = NonVariant(quantity, 3, 4)
                    )
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
    }

    @Test
    fun `given mini cart item is NOT null when addProductToCart should update product quantity`() {
        val warehouseId = "1"
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
        val miniCartItems = listOf(MiniCartItem(productId = productId, quantity = 1))
        val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRepurchaseWidget_thenReturn(repurchaseResponse)
        onGetMiniCart_thenReturn(miniCartResponse)
        onUpdateItemCart_thenReturn(UpdateCartV2Data())
        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(warehouseId = "1")
        viewModel.onScrollTokoMartHome(2, LocalCacheModel(), listOf())
        viewModel.getMiniCart(listOf(shopId), warehouseId)
        viewModel.addProductToCart(productId, 4, shopId, type)

        val expected = Success(UpdateCartV2Data())

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetRepurchaseWidgetUseCaseCalled()
        verifyGetMiniCartUseCaseCalled()
        verifyUpdateCartUseCaseCalled()

        viewModel.miniCartUpdate
            .verifySuccessEquals(expected)
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
        viewModel.getLayoutComponentData(warehouseId = "1")

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
        viewModel.getLayoutComponentData(warehouseId = "1")

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

        val miniCartItem = MiniCartItem(productId = productId)
        val miniCartResponse = MiniCartSimplifiedData(miniCartItems = listOf(miniCartItem))

        onGetMiniCart_thenReturn(miniCartResponse)
        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)

        viewModel.getMiniCart(listOf("1"), "2")

        val expectedResult = MiniCartItem(productId = productId)
        val actualResult = viewModel.getMiniCartItem(productId)

        verifyGetMiniCartUseCaseCalled()
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `given productId not match when getMiniCartItem should return NULL`() {
        val productId = "3"

        val miniCartItem = MiniCartItem(productId = "1")
        val miniCartResponse = MiniCartSimplifiedData(miniCartItems = listOf(miniCartItem))

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
        viewModel.getLayoutComponentData(warehouseId = "1")
        viewModel.onScrollTokoMartHome(2, LocalCacheModel(), listOf())

        val expected = listOf(
            createHomeProductCardUiModel(
                productId = productId,
                quantity = 4,
                product = ProductCardModel(
                    hasAddToCartButton = true,
                    nonVariant = NonVariant(minQuantity = 3, maxQuantity = 4)
                )
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
        viewModel.getLayoutComponentData(warehouseId = "1")

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
        viewModel.getLayoutComponentData(warehouseId = "1")

        val recomItemList = listOf(RecommendationItem(productId = 2, isRecomProductShowVariantAndCart = true, price = "0"))
        val recomWidget = RecommendationWidget(title = "Lagi Diskon", recommendationItemList = recomItemList)
        val homeRecomUiModel = HomeProductRecomUiModel(id = "1001", recomWidget = recomWidget)

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
        viewModel.getLayoutComponentData(warehouseId = "1")
        viewModel.onScrollTokoMartHome(2, LocalCacheModel(), listOf())

        val repurchaseUiModel = TokoNowRepurchaseUiModel(
            id = "1001",
            title = "Kamu pernah beli",
            productList = listOf(
                createHomeProductCardUiModel(
                    productId = productId,
                    quantity = 4,
                    product =  ProductCardModel(
                        hasAddToCartButton = true,
                        nonVariant = NonVariant(0, 3, 4)
                    )

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
        viewModel.getLayoutComponentData(warehouseId = "1")
        viewModel.addProductToCart(productId, quantity, shopId, type)

        val recomItemList = listOf(
            RecommendationItem(productId = 1, isRecomProductShowVariantAndCart = true, price = "0"),
            RecommendationItem(productId = 2, isRecomProductShowVariantAndCart = true, price = "0", quantity = quantity)
        )
        val recomWidget = RecommendationWidget(title = "Lagi Diskon", recommendationItemList = recomItemList)
        val homeRecomUiModel = HomeProductRecomUiModel(id = "1001", recomWidget = recomWidget)

        val expectedResult = HomeAddToCartTracker(
            position = 1,
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
        val miniCartItems = listOf(MiniCartItem(productId = productId, quantity = 1, cartId = cartId))
        val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetMiniCart_thenReturn(miniCartResponse)
        onUpdateItemCart_thenReturn(updateCartResponse)
        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(warehouseId = "1")
        viewModel.onScrollTokoMartHome(2, LocalCacheModel(), listOf())
        viewModel.getMiniCart(listOf(shopId), warehouseId)
        viewModel.addProductToCart(productId, 4, shopId, type)

        val recomItemList = listOf(
            RecommendationItem(productId = 1, isRecomProductShowVariantAndCart = true, price = "0", quantity = 4),
            RecommendationItem(productId = 2, isRecomProductShowVariantAndCart = true, price = "0", quantity = 0)
        )
        val recomWidget = RecommendationWidget(title = "Lagi Diskon", recommendationItemList = recomItemList)
        val homeRecomUiModel = HomeProductRecomUiModel(id = "1001", recomWidget = recomWidget)

        val expected = HomeAddToCartTracker(
            position = 0,
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

        val miniCartItems = listOf(MiniCartItem(productId = productId, quantity = 1, cartId = cartId))
        val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetMiniCart_thenReturn(miniCartResponse)
        onUpdateItemCart_thenReturn(error)
        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(warehouseId = "1")
        viewModel.onScrollTokoMartHome(2, LocalCacheModel(), listOf())
        viewModel.getMiniCart(listOf(shopId), warehouseId)
        viewModel.addProductToCart(productId, 4, shopId, type)

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetMiniCartUseCaseCalled()
        verifyUpdateCartUseCaseCalled()

        viewModel.miniCartUpdate
            .verifyErrorEquals(Fail(error))
    }

    @Test
    fun `when remove product recom from cart should track remove product recom`() {
        val warehouseId = "1"
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

        val miniCartItems = listOf(MiniCartItem(productId = productId, quantity = 1, cartId = cartId))
        val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetMiniCart_thenReturn(miniCartResponse)
        onRemoveItemCart_thenReturn(RemoveFromCartData())
        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(warehouseId = "1")
        viewModel.onScrollTokoMartHome(2, LocalCacheModel(), listOf())
        viewModel.getMiniCart(listOf(shopId), warehouseId)
        viewModel.addProductToCart(productId, 0, shopId, type)

        val recomItemList = listOf(
            RecommendationItem(productId = 1, isRecomProductShowVariantAndCart = true, price = "0", quantity = 0),
            RecommendationItem(productId = 2, isRecomProductShowVariantAndCart = true, price = "0", quantity = 0)
        )
        val recomWidget = RecommendationWidget(title = "Lagi Diskon", recommendationItemList = recomItemList)
        val homeRecomUiModel = HomeProductRecomUiModel(id = "1001", recomWidget = recomWidget)

        val expected = HomeAddToCartTracker(
            position = 0,
            quantity = 0,
            cartId = cartId,
            data = homeRecomUiModel
        )

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetMiniCartUseCaseCalled()
        verifyDeleteCartUseCaseCalled()

        viewModel.homeAddToCartTracker
            .verifyValueEquals(expected)
    }

    @Test
    fun `given delete cart error when remove product from cart should set miniCartRemove fail`() {
        val error = NullPointerException()
        val warehouseId = "1"
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

        val miniCartItems = listOf(MiniCartItem(productId = productId, quantity = 1, cartId = cartId))
        val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetMiniCart_thenReturn(miniCartResponse)
        onRemoveItemCart_thenReturn(error)
        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(warehouseId = "1")
        viewModel.onScrollTokoMartHome(2, LocalCacheModel(), listOf())
        viewModel.getMiniCart(listOf(shopId), warehouseId)
        viewModel.addProductToCart(productId, 0, shopId, type)

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetMiniCartUseCaseCalled()
        verifyDeleteCartUseCaseCalled()

        viewModel.miniCartRemove
            .verifyErrorEquals(Fail(error))
    }

    @Test
    fun `given homeLayoutResponse does NOT contain product recom when add to cart should NOT track add product`() {
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
        viewModel.getLayoutComponentData(warehouseId = "1")

        onGetHomeLayoutData_thenReturn(listOf(homeRecomResponse))

        viewModel.onScrollTokoMartHome(2, LocalCacheModel(), listOf())
        viewModel.addProductToCart(productId, quantity, shopId, type)

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyAddToCartUseCaseCalled()

        viewModel.homeAddToCartTracker
            .verifyValueEquals(expected = null)
    }

    @Test
    fun `given product NOT found when add product recom to cart should NOT track add product`() {
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
        viewModel.getLayoutComponentData(warehouseId = "1")
        viewModel.onScrollTokoMartHome(2, LocalCacheModel(), listOf())
        viewModel.addProductToCart("3", quantity, shopId, type)

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyAddToCartUseCaseCalled()

        viewModel.homeAddToCartTracker
            .verifyValueEquals(expected = null)
    }

    @Test
    fun `given recommendation list is empty when add product recom to cart should NOT track add product`() {
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
        viewModel.getLayoutComponentData(warehouseId = "1")

        onGetHomeLayoutData_thenReturn(listOf(homeRecomResponse))

        viewModel.onScrollTokoMartHome(2, LocalCacheModel(), listOf())
        viewModel.addProductToCart("3", quantity, shopId, type)

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyAddToCartUseCaseCalled()

        viewModel.homeAddToCartTracker
            .verifyValueEquals(expected = null)
    }

    @Test
    fun `when add repurchase product to cart should track add repurchase product`() {
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
        viewModel.getLayoutComponentData(warehouseId = "1")
        viewModel.onScrollTokoMartHome(2, LocalCacheModel(), listOf())
        viewModel.addProductToCart("2", 2, "100", TokoNowLayoutType.REPURCHASE_PRODUCT)

        val productCardUiModel = createHomeProductCardUiModel(
            productId = "2",
            quantity = 4,
            product =  ProductCardModel(
                hasAddToCartButton = true,
                nonVariant = NonVariant(0, 1, 4)
            )

        )

        val expected = HomeAddToCartTracker(
            position = 1,
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
        viewModel.getLayoutComponentData(warehouseId = "1")
        viewModel.onScrollTokoMartHome(2, LocalCacheModel(), listOf())
        viewModel.addProductToCart("4", 2, "100", TokoNowLayoutType.REPURCHASE_PRODUCT)

        verifyGetRepurchaseWidgetUseCaseCalled()
        verifyAddToCartUseCaseCalled()

        viewModel.homeAddToCartTracker
            .verifyValueEquals(expected = null)
    }

    @Test
    fun `given layout list does NOT contain repurchase when add product to cart should NOT track add to cart`() {
        val homeLayoutResponse = emptyList<HomeLayoutResponse>()
        val addToCartResponse = AddToCartDataModel(data = DataModel(cartId = "1999"))

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onAddToCart_thenReturn(addToCartResponse)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(warehouseId = "1")
        viewModel.onScrollTokoMartHome(2, LocalCacheModel(), listOf())
        viewModel.addProductToCart("4", 2, "100", TokoNowLayoutType.REPURCHASE_PRODUCT)

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyAddToCartUseCaseCalled()

        viewModel.homeAddToCartTracker
            .verifyValueEquals(expected = null)
    }

    @Test
    fun `given add to cart error when addProductToCart should set miniCartAdd value fail`() {
        val error = NullPointerException()
        val invalidLayoutType = "random layout type"
        val productId = "4"
        val quantity = 2
        val shopId = "100"

        onAddToCart_thenReturn(error)

        viewModel.addProductToCart(productId, quantity, shopId, invalidLayoutType)

        viewModel.miniCartAdd
            .verifyErrorEquals(Fail(error))
    }

    @Test
    fun `when layout type is NOT valid should NOT track add to cart`() {
        val invalidLayoutType = "random layout type"
        val productId = "4"
        val quantity = 2
        val shopId = "100"

        onAddToCart_thenReturn(AddToCartDataModel())

        viewModel.addProductToCart(productId, quantity, shopId, invalidLayoutType)

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
        viewModel.getLayoutComponentData(warehouseId = "1")

        onGetHomeLayoutData_thenReturn(listOf(secondBanner))

        viewModel.onScrollTokoMartHome(1, LocalCacheModel(), listOf())

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
        viewModel.getLayoutComponentData(warehouseId = "1")

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
        viewModel.getLayoutComponentData(warehouseId = "1")

        val items = listOf(
            TokoNowChooseAddressWidgetUiModel(id = "0"),
            HomeSharingEducationWidgetUiModel(
                id = channelId,
                HomeLayoutItemState.LOADED,
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
        viewModel.getLayoutComponentData(warehouseId = "1")

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
        viewModel.getLayoutComponentData(warehouseId = "1")

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
        viewModel.getLayoutComponentData(warehouseId = "1")

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

        viewModel.onScrollTokoMartHome(0, LocalCacheModel(), listOf())
        viewModel.onScrollTokoMartHome(1, LocalCacheModel(), listOf())

        verifyGetHomeLayoutDataUseCaseCalled(times = 2)
    }

    @Test
    fun `given load more error when scroll tokomart home should set result fail`() {
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
        viewModel.getLayoutComponentData(warehouseId = "1")

        val loadMoreError = NullPointerException()

        onGetHomeLayoutData_thenReturn(loadMoreError)

        viewModel.onScrollTokoMartHome(0, LocalCacheModel(), listOf())

        verifyGetHomeLayoutDataUseCaseCalled(times = 2)

        viewModel.homeLayoutList
            .verifyErrorEquals(Fail(loadMoreError))
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
        viewModel.getLayoutComponentData(warehouseId = "1")

        val progressBar = HomeLayoutItemUiModel(
            HomeProgressBarUiModel,
            HomeLayoutItemState.LOADED
        )
        addHomeLayoutItem(progressBar)

        viewModel.onScrollTokoMartHome(4, LocalCacheModel(), listOf())

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
        viewModel.getLayoutComponentData(warehouseId = "1")

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

        viewModel.getLayoutComponentData(warehouseId = "1")

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
        viewModel.getLayoutComponentData(warehouseId = "1")

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
        viewModel.getLayoutComponentData(warehouseId = "1")

        val layoutList = listOf(
            TokoNowChooseAddressWidgetUiModel("0"),
            HomeEducationalInformationWidgetUiModel(
                "2",
                HomeLayoutItemState.LOADED,
                ServiceType.NOW_2H
            ),
            HomeSwitcherUiModel.Home15mSwitcher
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
        viewModel.getLayoutComponentData(warehouseId = "1")

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
        viewModel.getLayoutComponentData(warehouseId = "1")

        val layoutList = listOf(
            TokoNowChooseAddressWidgetUiModel("0"),
            HomeEducationalInformationWidgetUiModel(
                "2",
                HomeLayoutItemState.LOADED,
                ServiceType.NOW_15M
            ),
            HomeSwitcherUiModel.Home2hSwitcher
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
        viewModel.getLayoutComponentData(warehouseId = "1")

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
        viewModel.getLayoutComponentData(warehouseId = "1")

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
        viewModel.getLayoutComponentData(warehouseId = "1")

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
}

