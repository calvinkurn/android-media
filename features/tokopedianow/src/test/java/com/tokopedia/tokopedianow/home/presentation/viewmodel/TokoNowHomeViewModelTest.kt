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
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.ProductCardModel.*
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.data.*
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.home.analytic.HomeAddToCartTracker
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType
import com.tokopedia.tokopedianow.common.model.*
import com.tokopedia.tokopedianow.common.domain.model.RepurchaseProduct
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.EMPTY_STATE_NO_ADDRESS
import com.tokopedia.tokopedianow.home.domain.model.GetRecentPurchaseResponse.*
import com.tokopedia.tokopedianow.home.domain.model.Grid
import com.tokopedia.tokopedianow.home.domain.model.Header
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment.Companion.SOURCE
import com.tokopedia.tokopedianow.home.presentation.uimodel.*
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
    fun `when getting homeLayout should run and give the success result`() {
        onGetTicker_thenReturn(createTicker())
        onGetHomeLayoutData_thenReturn(createHomeLayoutList())
        onGetCategoryList_thenReturn(createCategoryList(emptyList()))

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )

        val expectedResponse = HomeLayoutListUiModel(
            items = listOf(
                HomeLayoutItemUiModel(TokoNowChooseAddressWidgetUiModel(id = "0"), HomeLayoutItemState.LOADED),
                HomeLayoutItemUiModel(createHomeTickerDataModel(), HomeLayoutItemState.LOADED),
                HomeLayoutItemUiModel(createDynamicLegoBannerDataModel(
                    "34923",
                    "",
                    "Lego Banner"
                ), HomeLayoutItemState.LOADED),
                HomeLayoutItemUiModel(createCategoryGridDataModel(
                    "11111",
                    "Category Tokonow",
                    null,
                    TokoNowLayoutState.HIDE
                ), HomeLayoutItemState.LOADED),
                HomeLayoutItemUiModel(createSliderBannerDataModel(
                    "2222",
                    "",
                    "Banner Tokonow"
                ), HomeLayoutItemState.LOADED)
            ),
            state = TokoNowLayoutState.SHOW
        )
        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetTickerUseCaseCalled()
        verifyGetHomeLayoutResponseSuccess(expectedResponse)
    }

    @Test
    fun `when getHomeLayout two times should call use case twice`() {
        onGetHomeLayoutData_thenReturn(createHomeLayoutList())

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )

        verifyGetHomeLayoutDataUseCaseCalled(times = 2)
    }

    @Test
    fun `when getting homeLayoutData should run and give the success result`() {
        onGetHomeLayoutData_thenReturn(createHomeLayoutListForBannerOnly())

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )

        onGetHomeLayoutData_thenReturn(listOf(createHomeLayoutData()))

        viewModel.onScrollTokoMartHome(1, LocalCacheModel(), false)

        val expectedResponse = HomeLayoutItemUiModel(
            BannerDataModel(
                channelModel= ChannelModel(
                    id="2222",
                    groupId="",
                    style= ChannelStyle.ChannelHome,
                    channelHeader= ChannelHeader(name="Banner Tokonow"),
                    channelConfig= ChannelConfig(layout="banner_carousel_v2") ,
                    layout="banner_carousel_v2")
            ),
            HomeLayoutItemState.LOADED
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
        val idEmptyState = EMPTY_STATE_NO_ADDRESS

        viewModel.getEmptyState(idEmptyState)

        val expectedResponse = createEmptyState(idEmptyState)

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

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )

        verifyGetHomeLayoutResponseFail()
    }

    @Test
    fun `when getting homeLayout should throw homeLayout's exception and get the failed result`() {
        onGetHomeLayoutData_thenReturn(Exception())

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )

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
        viewModel.getHomeLayout(
            localCacheModel = localCacheModel,
            hasSharingEducationBeenRemoved = false
        )

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
        val expectedResponse = HomeLayoutItemUiModel(
            TokoNowCategoryGridUiModel(
                id="11111",
                title="Category Tokonow",
                categoryList = listOf(
                    TokoNowCategoryItemUiModel(
                    id="1",
                    title="Category 1",
                    imageUrl="tokopedia://",
                    appLink="tokoepdia://"
                )
                ),
                state= TokoNowLayoutState.SHOW
            ),
            HomeLayoutItemState.LOADED
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
        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )

        //fetch widget one by one
        viewModel.onScrollTokoMartHome(1, LocalCacheModel(), false)

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
        val expectedResponse = HomeLayoutItemUiModel(
            TokoNowCategoryGridUiModel(
                id="11111",
                title="Category Tokonow",
                categoryList = null,
                state= TokoNowLayoutState.HIDE
            ),
            HomeLayoutItemState.LOADED
        )

        // verify use case called and response
        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetCategoryListUseCaseCalled()
        verifyGetCategoryListResponseSuccess(expectedResponse)
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

        viewModel.getHomeLayout(
            localCacheModel = localCacheModel,
            hasSharingEducationBeenRemoved = false
        )

        val data = HomeLayoutListUiModel(
            items = listOf(
                HomeLayoutItemUiModel(
                    TokoNowChooseAddressWidgetUiModel(id = "0"),
                    HomeLayoutItemState.LOADED
                ),
                HomeLayoutItemUiModel(createDynamicLegoBannerDataModel(
                    "34923",
                    "",
                    "Lego Banner"
                ), HomeLayoutItemState.LOADED),
                HomeLayoutItemUiModel(TokoNowCategoryGridUiModel(
                    id="11111",
                    title="Category Tokonow",
                    categoryList = null,
                    state= TokoNowLayoutState.HIDE
                ), HomeLayoutItemState.LOADED),
                HomeLayoutItemUiModel(createSliderBannerDataModel(
                    "2222",
                    "",
                    "Banner Tokonow"
                ), HomeLayoutItemState.LOADED)
            ),
            state = TokoNowLayoutState.SHOW
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

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )
        viewModel.onScrollTokoMartHome(index, LocalCacheModel(), false)
    }

    @Test
    fun `when removeTickerWidget should remove ticker from home layout list`() {
        onGetHomeLayoutData_thenReturn(createHomeLayoutList())

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )
        viewModel.removeTickerWidget("1")

        val expectedResult = HomeLayoutListUiModel(
            items = listOf(
                HomeLayoutItemUiModel(TokoNowChooseAddressWidgetUiModel(id = "0"), HomeLayoutItemState.LOADED),
                HomeLayoutItemUiModel(createDynamicLegoBannerDataModel(
                    "34923",
                    "",
                    "Lego Banner"
                ), HomeLayoutItemState.LOADED),
                HomeLayoutItemUiModel(createCategoryGridDataModel(
                    "11111",
                    "Category Tokonow",
                    null,
                    TokoNowLayoutState.HIDE
                ), HomeLayoutItemState.LOADED),
                HomeLayoutItemUiModel(createSliderBannerDataModel(
                    "2222",
                    "",
                    "Banner Tokonow"
                ), HomeLayoutItemState.LOADED)
            ),
            state = TokoNowLayoutState.SHOW
        )

        verifyGetHomeLayoutResponseSuccess(expectedResult)
    }

    @Test
    fun `given getTicker error when getLayoutData should remove ticker from home layout list`() {
        onGetHomeLayoutData_thenReturn(createHomeLayoutList())
        onGetTicker_thenReturn(NullPointerException())

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )

        val expectedResult = HomeLayoutListUiModel(
            items = listOf(
                HomeLayoutItemUiModel(TokoNowChooseAddressWidgetUiModel(id = "0"), HomeLayoutItemState.LOADED),
                HomeLayoutItemUiModel(createDynamicLegoBannerDataModel(
                    "34923",
                    "",
                    "Lego Banner"
                ), HomeLayoutItemState.LOADED),
                HomeLayoutItemUiModel(createCategoryGridDataModel(
                    "11111",
                    "Category Tokonow",
                    null,
                    TokoNowLayoutState.HIDE
                ), HomeLayoutItemState.LOADED),
                HomeLayoutItemUiModel(createSliderBannerDataModel(
                    "2222",
                    "",
                    "Banner Tokonow"
                ), HomeLayoutItemState.LOADED)
            ),
            state = TokoNowLayoutState.SHOW
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
            HomeLayoutItemUiModel(
                HomeProductRecomUiModel(
                    "6",
                    recommendationWidget
                ),
                HomeLayoutItemState.LOADED
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
        val type = TokoNowLayoutType.RECENT_PURCHASE

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
        val recentPurchaseResponse = RecentPurchaseData(
            title = "Kamu pernah beli",
            products = listOf(RepurchaseProduct(id = productId, stock = 5, maxOrder = 4, minOrder = 3))
        )
        val addToCartResponse = AddToCartDataModel()

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRecentPurchase_thenReturn(recentPurchaseResponse)
        onAddToCart_thenReturn(addToCartResponse)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )
        viewModel.addProductToCart(productId, quantity, shopId, type)

        val chooseAddressWidget = TokoNowChooseAddressWidgetUiModel(id = "0")
        val recentPurchaseUiModel = TokoNowRecentPurchaseUiModel(
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
            HomeLayoutItemUiModel(chooseAddressWidget, HomeLayoutItemState.LOADED),
            HomeLayoutItemUiModel(recentPurchaseUiModel, HomeLayoutItemState.LOADED)
        )

        val expectedResult = HomeLayoutListUiModel(
            items = homeLayoutItems,
            state = TokoNowLayoutState.SHOW
        )

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetRecentPurchaseUseCaseCalled()
        verifyAddToCartUseCaseCalled()
        verifyGetHomeLayoutResponseSuccess(expectedResult)

        viewModel.miniCartAdd
            .verifySuccessEquals(Success(AddToCartDataModel()))
    }

    @Test
    fun `given quantity is 0 when addProductToCart should update product quantity to 0`() {
        val warehouseId = "1"
        val productId = "100"
        val quantity = 0
        val shopId = "5"
        val type = TokoNowLayoutType.RECENT_PURCHASE

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
        val recentPurchaseResponse = RecentPurchaseData(
            title = "Kamu pernah beli",
            products = listOf(RepurchaseProduct(id = productId, stock = 5, maxOrder = 4, minOrder = 3))
        )
        val miniCartItems = listOf(MiniCartItem(productId = productId, quantity = 1))
        val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRecentPurchase_thenReturn(recentPurchaseResponse)
        onGetMiniCart_thenReturn(miniCartResponse)
        onRemoveItemCart_thenReturn(RemoveFromCartData())
        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )
        viewModel.getMiniCart(listOf(shopId), warehouseId)
        viewModel.addProductToCart(productId, quantity, shopId, type)

        val recentPurchaseUiModel = TokoNowRecentPurchaseUiModel(
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
            HomeLayoutItemUiModel(TokoNowChooseAddressWidgetUiModel(id = "0"), HomeLayoutItemState.LOADED),
            HomeLayoutItemUiModel(
                recentPurchaseUiModel,
                HomeLayoutItemState.LOADED
            )
        )

        val expectedResult = HomeLayoutListUiModel(
            items = homeLayoutItems,
            state = TokoNowLayoutState.SHOW
        )

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetRecentPurchaseUseCaseCalled()
        verifyGetMiniCartUseCaseCalled()
        verifyDeleteCartUseCaseCalled()
        verifyGetHomeLayoutResponseSuccess(expectedResult)

        viewModel.miniCartRemove
            .verifySuccessEquals(Success(Pair(productId, "")))
    }

    @Test
    fun `given mini cart item is NOT null when addProductToCart should update product quantity`() {
        val warehouseId = "1"
        val productId = "100"
        val shopId = "5"
        val type = TokoNowLayoutType.RECENT_PURCHASE

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
        val recentPurchaseResponse = RecentPurchaseData(
            title = "Kamu pernah beli",
            products = listOf(RepurchaseProduct(id = productId, maxOrder = 5, minOrder = 3))
        )
        val miniCartItems = listOf(MiniCartItem(productId = productId, quantity = 1))
        val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRecentPurchase_thenReturn(recentPurchaseResponse)
        onGetMiniCart_thenReturn(miniCartResponse)
        onUpdateItemCart_thenReturn(UpdateCartV2Data())
        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )
        viewModel.onScrollTokoMartHome(2, LocalCacheModel(), false)
        viewModel.getMiniCart(listOf(shopId), warehouseId)
        viewModel.addProductToCart(productId, 4, shopId, type)

        val expected = Success(UpdateCartV2Data())

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetRecentPurchaseUseCaseCalled()
        verifyGetMiniCartUseCaseCalled()
        verifyUpdateCartUseCaseCalled()

        viewModel.miniCartUpdate
            .verifySuccessEquals(expected)
    }

    @Test
    fun `when getProductAddToCartQuantity success should set product quantity from mini cart`() {
        val productId = "1"
        val warehouseId = "2"

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
        val recentPurchaseProduct = RepurchaseProduct(
            id = productId,
            stock = 5,
            maxOrder = 4,
            minOrder = 3
        )
        val recentPurchaseResponse = RecentPurchaseData(
            title = "Kamu pernah beli",
            products = listOf(recentPurchaseProduct)
        )
        val miniCartItems = listOf(MiniCartItem(productId = productId, quantity = 5))
        val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRecentPurchase_thenReturn(recentPurchaseResponse)
        onGetMiniCart_thenReturn(miniCartResponse)
        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )
        viewModel.onScrollTokoMartHome(2, LocalCacheModel(), false)
        viewModel.getProductAddToCartQuantity(listOf("1"), warehouseId)

        val recentPurchaseUiModel = TokoNowRecentPurchaseUiModel(
            id = "1001",
            title = "Kamu pernah beli",
            productList = listOf(
                createHomeProductCardUiModel(
                    productId = productId,
                    quantity = 4,
                    product =  ProductCardModel(
                        nonVariant = NonVariant(5, 3, 4)
                    )
                )
            ),
            state = TokoNowLayoutState.SHOW
        )

        val homeLayoutItems = listOf(
            HomeLayoutItemUiModel(TokoNowChooseAddressWidgetUiModel(id = "0"), HomeLayoutItemState.LOADED),
            HomeLayoutItemUiModel(recentPurchaseUiModel, HomeLayoutItemState.LOADED)
        )

        val expectedResult = Success(
            HomeLayoutListUiModel(
                items = homeLayoutItems,
                state = TokoNowLayoutState.SHOW
            )
        )

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetRecentPurchaseUseCaseCalled()

        viewModel.atcQuantity
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `given getMiniCart error when getProductAddToCartQuantity should set atcQuantity fail`() {
        val productId = "1"
        val warehouseId = "2"
        val miniCartError = NullPointerException()

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
        val recentPurchaseProduct = RepurchaseProduct(
            id = productId,
            stock = 5,
            maxOrder = 4,
            minOrder = 3
        )
        val recentPurchaseResponse = RecentPurchaseData(
            title = "Kamu pernah beli",
            products = listOf(recentPurchaseProduct)
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRecentPurchase_thenReturn(recentPurchaseResponse)
        onGetMiniCart_thenReturn(miniCartError)
        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )
        viewModel.onScrollTokoMartHome(2, LocalCacheModel(), false)
        viewModel.getProductAddToCartQuantity(listOf("1"), warehouseId)

        val expectedResult = Fail(miniCartError)

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetRecentPurchaseUseCaseCalled()

        viewModel.atcQuantity
            .verifyErrorEquals(expectedResult)
    }

    @Test
    fun `given getMiniCart throw exception when getProductAddToCartQuantity should set atcQuantity fail`() {
        val productId = "1"
        val warehouseId = "2"
        val miniCartException = NullPointerException()

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
        val recentPurchaseProduct = RepurchaseProduct(
            id = productId,
            stock = 5,
            maxOrder = 4,
            minOrder = 3
        )
        val recentPurchaseResponse = RecentPurchaseData(
            title = "Kamu pernah beli",
            products = listOf(recentPurchaseProduct)
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRecentPurchase_thenReturn(recentPurchaseResponse)
        onGetMiniCart_throwException(miniCartException)
        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )
        viewModel.onScrollTokoMartHome(2, LocalCacheModel(), false)
        viewModel.getProductAddToCartQuantity(listOf("1"), warehouseId)

        val expectedResult = Fail(miniCartException)

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetRecentPurchaseUseCaseCalled()

        viewModel.atcQuantity
            .verifyErrorEquals(expectedResult)
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
        onGetRecentPurchase_thenReturn(error)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )

        val homeLayoutItems = listOf(
            HomeLayoutItemUiModel(
                TokoNowChooseAddressWidgetUiModel(id = "0"),
                HomeLayoutItemState.LOADED
            ),
        )

        val expectedResult = Success(
            HomeLayoutListUiModel(
                items = homeLayoutItems,
                state = TokoNowLayoutState.SHOW
            )
        )

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetRecentPurchaseUseCaseCalled()

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `given getRepurchaseProduct empty when getHomeLayout should NOT add repurchase widget`() {
        val repurchaseProductResponse = RecentPurchaseData(
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
        onGetRecentPurchase_thenReturn(repurchaseProductResponse)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )

        val homeLayoutItems = listOf(
            HomeLayoutItemUiModel(
                TokoNowChooseAddressWidgetUiModel(id = "0"),
                HomeLayoutItemState.LOADED
            ),
        )

        val expectedResult = Success(
            HomeLayoutListUiModel(
                items = homeLayoutItems,
                state = TokoNowLayoutState.SHOW
            )
        )

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetRecentPurchaseUseCaseCalled()

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `given shopId is empty when getProductAddToCartQuantity should NOT call get mini cart use case`() {
        val shopId = emptyList<String>()
        val warehouseId = "1"

        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)

        viewModel.getProductAddToCartQuantity(shopId, warehouseId)

        verifyGetMiniCartUseCaseNotCalled()
    }



    @Test
    fun `given warehouseId is 0 when getProductAddToCartQuantity should NOT call get mini cart use case`() {
        val shopId = listOf("1")
        val warehouseId = "0"

        onGetIsUserLoggedIn_thenReturn(userLoggedIn = true)

        viewModel.getProductAddToCartQuantity(shopId, warehouseId)

        verifyGetMiniCartUseCaseNotCalled()
    }

    @Test
    fun `given user is NOT logged in when getProductAddToCartQuantity should NOT call get mini cart use case`() {
        val shopId = listOf("1")
        val warehouseId = "1"
        val userLoggedIn = false

        onGetIsUserLoggedIn_thenReturn(userLoggedIn)

        viewModel.getProductAddToCartQuantity(shopId, warehouseId)

        verifyGetMiniCartUseCaseNotCalled()
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
    fun `given homeLayoutItemList contains recent purchase when getRecentPurchaseProducts should product list`() {
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
        val recentPurchaseProduct = RepurchaseProduct(
            id = productId,
            stock = 5,
            maxOrder = 4,
            minOrder = 3
        )
        val recentPurchaseResponse = RecentPurchaseData(
            title = "Kamu pernah beli",
            products = listOf(recentPurchaseProduct)
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRecentPurchase_thenReturn(recentPurchaseResponse)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )
        viewModel.onScrollTokoMartHome(2, LocalCacheModel(), false)

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
        val actual = viewModel.getRecentPurchaseProducts()

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetRecentPurchaseUseCaseCalled()
        assertEquals(expected, actual)
    }

    @Test
    fun `given homeLayoutItemList does NOT contain recent purchase when getRecentPurchaseProducts should return empty list`() {
        onGetHomeLayoutData_thenReturn(createHomeLayoutList())

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )

        val expected = emptyList<TokoNowProductCardUiModel>()
        val actual = viewModel.getRecentPurchaseProducts()

        verifyGetHomeLayoutDataUseCaseCalled()
        assertEquals(expected, actual)
    }

    @Test
    fun `given homeLayoutItemList is empty when getRecentPurchaseProducts should return empty list`() {
        val expected = emptyList<TokoNowProductCardUiModel>()
        val actual = viewModel.getRecentPurchaseProducts()

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

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )

        val recomItemList = listOf(RecommendationItem(productId = 2, isRecomProductShowVariantAndCart = true, price = "0"))
        val recomWidget = RecommendationWidget(title = "Lagi Diskon", recommendationItemList = recomItemList)
        val homeRecomUiModel = HomeProductRecomUiModel(id = "1001", recomWidget = recomWidget)

        val homeLayoutItems = listOf(
            HomeLayoutItemUiModel(
                TokoNowChooseAddressWidgetUiModel(id = "0"),
                HomeLayoutItemState.LOADED
            ),
            HomeLayoutItemUiModel(
                homeRecomUiModel,
                HomeLayoutItemState.LOADED
            )
        )

        val expectedResult = Success(
            HomeLayoutListUiModel(
                items = homeLayoutItems,
                state = TokoNowLayoutState.SHOW
            )
        )

        verifyGetHomeLayoutDataUseCaseCalled()

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when get recent purchase success should add recent purchase to home layout list`() {
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
        val recentPurchaseProduct = RepurchaseProduct(
            id = productId,
            stock = 5,
            maxOrder = 4,
            minOrder = 3
        )
        val recentPurchaseResponse = RecentPurchaseData(
            title = "Kamu pernah beli",
            products = listOf(recentPurchaseProduct)
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRecentPurchase_thenReturn(recentPurchaseResponse)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )
        viewModel.onScrollTokoMartHome(2, LocalCacheModel(), false)

        val recentPurchaseUiModel = TokoNowRecentPurchaseUiModel(
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
            HomeLayoutItemUiModel(TokoNowChooseAddressWidgetUiModel(id = "0"), HomeLayoutItemState.LOADED),
            HomeLayoutItemUiModel(recentPurchaseUiModel, HomeLayoutItemState.LOADED)
        )

        val expectedResult = Success(
            HomeLayoutListUiModel(
                items = homeLayoutItems,
                state = TokoNowLayoutState.SHOW
            )
        )

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetRecentPurchaseUseCaseCalled()

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

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )
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

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )
        viewModel.onScrollTokoMartHome(2, LocalCacheModel(), false)
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

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )
        viewModel.onScrollTokoMartHome(2, LocalCacheModel(), false)
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

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )
        viewModel.onScrollTokoMartHome(2, LocalCacheModel(), false)
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

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )
        viewModel.onScrollTokoMartHome(2, LocalCacheModel(), false)
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

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )

        onGetHomeLayoutData_thenReturn(listOf(homeRecomResponse))

        viewModel.onScrollTokoMartHome(2, LocalCacheModel(), false)
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

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )
        viewModel.onScrollTokoMartHome(2, LocalCacheModel(), false)
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

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )

        onGetHomeLayoutData_thenReturn(listOf(homeRecomResponse))

        viewModel.onScrollTokoMartHome(2, LocalCacheModel(), false)
        viewModel.addProductToCart("3", quantity, shopId, type)

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyAddToCartUseCaseCalled()

        viewModel.homeAddToCartTracker
            .verifyValueEquals(expected = null)
    }

    @Test
    fun `when add recent purchase product to cart should track add recent purchase product`() {
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

        val recentPurchaseResponse = RecentPurchaseData(
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
        onGetRecentPurchase_thenReturn(recentPurchaseResponse)
        onAddToCart_thenReturn(addToCartResponse)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )
        viewModel.onScrollTokoMartHome(2, LocalCacheModel(), false)
        viewModel.addProductToCart("2", 2, "100", TokoNowLayoutType.RECENT_PURCHASE)

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

        verifyGetRecentPurchaseUseCaseCalled()
        verifyAddToCartUseCaseCalled()

        viewModel.homeAddToCartTracker
            .verifyValueEquals(expected)
    }

    @Test
    fun `given product not found when add recent purchase product to cart should NOT track add to cart`() {
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

        val recentPurchaseResponse = RecentPurchaseData(
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
        onGetRecentPurchase_thenReturn(recentPurchaseResponse)
        onAddToCart_thenReturn(addToCartResponse)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )
        viewModel.onScrollTokoMartHome(2, LocalCacheModel(), false)
        viewModel.addProductToCart("4", 2, "100", TokoNowLayoutType.RECENT_PURCHASE)

        verifyGetRecentPurchaseUseCaseCalled()
        verifyAddToCartUseCaseCalled()

        viewModel.homeAddToCartTracker
            .verifyValueEquals(expected = null)
    }

    @Test
    fun `given layout list does NOT contain recent purchase when add product to cart should NOT track add to cart`() {
        val homeLayoutResponse = emptyList<HomeLayoutResponse>()
        val addToCartResponse = AddToCartDataModel(data = DataModel(cartId = "1999"))

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onAddToCart_thenReturn(addToCartResponse)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )
        viewModel.onScrollTokoMartHome(2, LocalCacheModel(), false)
        viewModel.addProductToCart("4", 2, "100", TokoNowLayoutType.RECENT_PURCHASE)

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

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )

        onGetHomeLayoutData_thenReturn(listOf(secondBanner))

        viewModel.onScrollTokoMartHome(1, LocalCacheModel(), false)

        val layoutList = listOf(
            HomeLayoutItemUiModel(TokoNowChooseAddressWidgetUiModel(id = "0"), HomeLayoutItemState.LOADED),
            HomeLayoutItemUiModel(
                BannerDataModel(
                    channelModel= ChannelModel(
                        id="2222",
                        groupId="",
                        style= ChannelStyle.ChannelHome,
                        channelHeader= ChannelHeader(name="Banner Tokonow"),
                        channelConfig= ChannelConfig(layout="banner_carousel_v2") ,
                        layout="banner_carousel_v2")
                ),
                HomeLayoutItemState.LOADED
            ),
            HomeLayoutItemUiModel(
                BannerDataModel(
                    channelModel= ChannelModel(
                        id="3333",
                        groupId="",
                        style= ChannelStyle.ChannelHome,
                        channelHeader= ChannelHeader(name="Banner Tokonow"),
                        channelConfig= ChannelConfig(layout="banner_carousel_v2") ,
                        layout="banner_carousel_v2")
                ),
                HomeLayoutItemState.LOADED
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

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )

        verifyGetHomeLayoutDataUseCaseCalled()

        viewModel.removeSharingEducationWidget(channelId)

        val items = listOf(
            HomeLayoutItemUiModel(
                TokoNowChooseAddressWidgetUiModel(id = "0"),
                HomeLayoutItemState.LOADED
            )
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
        val repurchaseResponse = RecentPurchaseData(
            title = "Kamu pernah beli",
            products = listOf(RepurchaseProduct(id = "1", stock = 5, maxOrder = 4, minOrder = 3))
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRecentPurchase_thenReturn(repurchaseResponse)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )

        val items = listOf(
            HomeLayoutItemUiModel(
                TokoNowChooseAddressWidgetUiModel(id = "0"),
                HomeLayoutItemState.LOADED
            ),
            HomeLayoutItemUiModel(
                HomeSharingEducationWidgetUiModel(
                    id = channelId,
                    HomeLayoutItemState.LOADED
                ),
                HomeLayoutItemState.LOADED
            )
        )

        val expectedResult = HomeLayoutListUiModel(
            items = items,
            state = TokoNowLayoutState.SHOW
        )

        verifyGetRecentPurchaseUseCaseCalled()

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
        val repurchaseResponse = RecentPurchaseData(
            title = "Kamu pernah beli",
            products = listOf(RepurchaseProduct(id = "1", stock = 5, maxOrder = 4, minOrder = 3))
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRecentPurchase_thenReturn(repurchaseResponse)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = hasSharingEducationBeenRemoved
        )

        val items = listOf(
            HomeLayoutItemUiModel(
                TokoNowChooseAddressWidgetUiModel(id = "0"),
                HomeLayoutItemState.LOADED
            )
        )

        val expectedResult = HomeLayoutListUiModel(
            items = items,
            state = TokoNowLayoutState.SHOW
        )

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetRecentPurchaseUseCaseNotCalled()

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
        val repurchaseResponse = RecentPurchaseData(
            title = "Kamu pernah beli",
            products = emptyList()
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRecentPurchase_thenReturn(repurchaseResponse)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )

        val items = listOf(
            HomeLayoutItemUiModel(
                TokoNowChooseAddressWidgetUiModel(id = "0"),
                HomeLayoutItemState.LOADED
            )
        )

        val expectedResult = HomeLayoutListUiModel(
            items = items,
            state = TokoNowLayoutState.SHOW
        )

        verifyGetRecentPurchaseUseCaseCalled()

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

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )

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

        viewModel.onScrollTokoMartHome(0, LocalCacheModel(), false)
        viewModel.onScrollTokoMartHome(1, LocalCacheModel(), false)

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

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )

        val loadMoreError = NullPointerException()

        onGetHomeLayoutData_thenReturn(loadMoreError)

        viewModel.onScrollTokoMartHome(0, LocalCacheModel(), false)

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

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )

        val progressBar = HomeLayoutItemUiModel(
            HomeProgressBarUiModel,
            HomeLayoutItemState.LOADED
        )
        addHomeLayoutItem(progressBar)

        viewModel.onScrollTokoMartHome(4, LocalCacheModel(), false)

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

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            hasSharingEducationBeenRemoved = false
        )

        verifyGetHomeLayoutDataUseCaseCalled()

        // Other Use Case
        verifyGetCategoryListUseCaseNotCalled()
        verifyGetRecentPurchaseUseCaseNotCalled()
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

        viewModel.removeSharingEducationWidget("1")

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
        val unknownTokoNowLayout = HomeLayoutItemUiModel(
            UnknownTokoNowLayout,
            HomeLayoutItemState.NOT_LOADED
        )

        addHomeLayoutItem(unknownLayout)
        addHomeLayoutItem(unknownHomeLayout)
        addHomeLayoutItem(unknownTokoNowLayout)

        getLayoutComponentData(warehouseId = "1")

        verifyGetCategoryListUseCaseNotCalled()
        verifyGetRecentPurchaseUseCaseNotCalled()
        verifyGetMiniCartUseCaseNotCalled()

        viewModel.homeLayoutList
            .verifyValueEquals(null)
    }
}

