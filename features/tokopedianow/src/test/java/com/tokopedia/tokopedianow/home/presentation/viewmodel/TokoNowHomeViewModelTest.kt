package com.tokopedia.tokopedianow.home.presentation.viewmodel

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryGridUiModel
import com.tokopedia.tokopedianow.data.*
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryItemUiModel
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.EMPTY_STATE_NO_ADDRESS
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment.Companion.SOURCE
import com.tokopedia.tokopedianow.home.presentation.uimodel.*
import org.junit.Test
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyString

class TokoNowHomeViewModelTest: TokoNowHomeViewModelTestFixture() {

    @Test
    fun `when getting homeLayout should run and give the success result`() {
        onGetTicker_thenReturn(createTicker())
        onGetHomeLayout_thenReturn(createHomeLayoutList())

        viewModel.getHomeLayout(LocalCacheModel())

        val expectedResponse = HomeLayoutListUiModel(
            items = listOf(
                HomeLayoutItemUiModel(HomeChooseAddressWidgetUiModel(id = "0"), HomeLayoutItemState.LOADED),
                HomeLayoutItemUiModel(createHomeTickerDataModel(emptyList()), HomeLayoutItemState.NOT_LOADED),
                HomeLayoutItemUiModel(createDynamicLegoBannerDataModel(
                    "34923",
                    "",
                    "Lego Banner"
                ), HomeLayoutItemState.NOT_LOADED),
                HomeLayoutItemUiModel(createCategoryGridDataModel(
                    "11111",
                    "Category Tokonow",
                    emptyList(),
                    TokoNowLayoutState.LOADING
                ), HomeLayoutItemState.NOT_LOADED),
                HomeLayoutItemUiModel(createSliderBannerDataModel(
                    "2222",
                    "",
                    "Banner Tokonow"
                ), HomeLayoutItemState.NOT_LOADED)
            ),
            state = TokoNowLayoutState.SHOW,
            isInitialLoad = true
        )
        verifyGetHomeLayoutUseCaseCalled()
        verifyGetHomeLayoutResponseSuccess(expectedResponse)
    }

//    @Test
//    fun `when getting homeLayoutData should run and give the success result`() {
//        onGetHomeLayout_thenReturn(createHomeLayoutListForBannerOnly())
//        onGetHomeLayoutData_thenReturn(createHomeLayoutData())
//
//        viewModel.getHomeLayout(LocalCacheModel())
//
//        viewModel.getLayoutData(2, "1", 0, 2, LocalCacheModel())
//
//        val expectedResponse = HomeLayoutItemUiModel(
//            BannerDataModel(
//                channelModel= ChannelModel(
//                    id="2222",
//                    groupId="",
//                    style= ChannelStyle.ChannelHome,
//                    channelHeader= ChannelHeader(name="Banner Tokonow"),
//                    channelConfig=ChannelConfig(layout="banner_carousel_v2") ,
//                    layout="banner_carousel_v2")
//            ),
//            HomeLayoutItemState.LOADED
//        )
//
//        verifyGetHomeLayoutUseCaseCalled()
//        verifyGetHomeLayoutDataUseCaseCalled()
//        verifyGetBannerResponseSuccess(expectedResponse)
//    }

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

        viewModel.getHomeLayout(LocalCacheModel())

        verifyGetHomeLayoutResponseFail()
    }

    @Test
    fun `when getting homeLayout should throw homeLayout's exception and get the failed result`() {
        onGetHomeLayout_thenReturn(Exception())

        viewModel.getHomeLayout(LocalCacheModel())

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
        //set mock data
        onGetHomeLayout_thenReturn(createHomeLayoutList())
        onGetCategoryList_thenReturn(createCategoryGridListFirstFetch())

        //fetch homeLayout
        viewModel.getHomeLayout(LocalCacheModel())

        //fetch widget one by one
        viewModel.getLayoutData(1, "1", 0, 1, LocalCacheModel())

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
        verifyGetHomeLayoutUseCaseCalled()
        verifyGetCategoryListUseCaseCalled()
        verifyGetCategoryListResponseSuccess(expectedResponse)
    }

    @Test
    fun `when getting data category grid should run, throw exception and give the success result`() {
        //set mock data
        onGetHomeLayout_thenReturn(createHomeLayoutList())
        onGetCategoryList_thenReturn(createCategoryGridListFirstFetch())

        //fetch homeLayout
        viewModel.getHomeLayout(LocalCacheModel())

        //fetch widget one by one
        viewModel.getLayoutData(1, "1", 0, 1, LocalCacheModel())

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
        verifyGetHomeLayoutUseCaseCalled()
        verifyGetCategoryListUseCaseCalled()
        verifyGetCategoryListResponseSuccess(expectedResponse)
    }

    @Test
    fun `when getting moreLayoutData should run and give the success result`() {
        //set mock data
        onGetTicker_thenReturn(createTicker())
        onGetHomeLayout_thenReturn(createHomeLayoutList())
        onGetCategoryList_thenReturn(createCategoryGridListFirstFetch())

        //fetch homeLayout
        viewModel.getHomeLayout(LocalCacheModel())

        viewModel.getMoreLayoutData("1", 1, 4, LocalCacheModel())

        //prepare model for expectedResult
        val expectedResponse = HomeLayoutItemUiModel(
                TokoNowCategoryGridUiModel(
                        id="11111",
                        title="Category Tokonow",
                        categoryList = listOf(
                            TokoNowCategoryItemUiModel(
                                id="3",
                                title="Category 3",
                                imageUrl="tokopedia://",
                                appLink="tokoepdia://"
                        )
                        ),
                        state= TokoNowLayoutState.SHOW
                ),
                HomeLayoutItemState.LOADED
        )

        // verify use case called and response
        verifyGetCategoryListUseCaseCalled()
        verifyGetHomeLayoutUseCaseCalled()
        verifyGetCategoryListResponseSuccess(expectedResponse)
    }

    @Test
    fun `when getting moreLayoutData should not run and homeLayout livedata is null`() {
        val homeLayout = createHomeLayoutItemUiModelList()

        privateHomeLayoutItemList.set(viewModel, homeLayout)

        viewModel.getMoreLayoutData("1", 0, 0, LocalCacheModel())
        verifyGetHomeLayoutNullResponse()

        viewModel.getMoreLayoutData("1", 1, 1, LocalCacheModel())
        verifyGetHomeLayoutNullResponse()

        viewModel.getMoreLayoutData("1", 2, 2, LocalCacheModel())
        verifyGetHomeLayoutNullResponse()
    }

    @Test
    fun `given index is null when getLayoutData should not call get layout data use case`() {
        val index = null
        val warehouseId = "1"
        val firstVisibleItemIndex = 0
        val lastVisibleItemIndex = 3

        onGetHomeLayout_thenReturn(createHomeLayoutList())

        viewModel.getHomeLayout(LocalCacheModel())
        viewModel.getLayoutData(index, warehouseId, firstVisibleItemIndex , lastVisibleItemIndex, LocalCacheModel())

        // verify all get layout data use case not called
        verifyGetHomeLayoutDataUseCaseNotCalled()
        verifyGetCategoryListUseCaseNotCalled()
        verifyGetTickerUseCaseNotCalled()
    }

    @Test
    fun `given index is NOT between visible item index when getLayoutData should not call use case`() {
        val index = 1
        val warehouseId = "1"
        val firstVisibleItemIndex = 2
        val lastVisibleItemIndex = 3

        onGetHomeLayout_thenReturn(createHomeLayoutList())

        viewModel.getHomeLayout(LocalCacheModel())
        viewModel.getLayoutData(index, warehouseId, firstVisibleItemIndex , lastVisibleItemIndex, LocalCacheModel())

        val expectedResult = HomeLayoutListUiModel(
            items = listOf(
                HomeLayoutItemUiModel(HomeChooseAddressWidgetUiModel(id = "0"), HomeLayoutItemState.LOADED),
                HomeLayoutItemUiModel(createHomeTickerDataModel(emptyList()), HomeLayoutItemState.NOT_LOADED),
                HomeLayoutItemUiModel(createDynamicLegoBannerDataModel(
                    "34923",
                    "",
                    "Lego Banner"
                ), HomeLayoutItemState.NOT_LOADED),
                HomeLayoutItemUiModel(createCategoryGridDataModel(
                    "11111",
                    "Category Tokonow",
                    emptyList(),
                    TokoNowLayoutState.LOADING
                ), HomeLayoutItemState.NOT_LOADED),
                HomeLayoutItemUiModel(createSliderBannerDataModel(
                    "2222",
                    "",
                    "Banner Tokonow"
                ), HomeLayoutItemState.NOT_LOADED)
            ),
            state = TokoNowLayoutState.SHOW,
            isInitialLoad = false,
            isLoadDataFinished = true,
            nextItemIndex = 1
        )

        verifyGetTickerUseCaseNotCalled()
        verifyGetCategoryListUseCaseNotCalled()
        verifyGetHomeLayoutDataUseCaseNotCalled()
        verifyGetHomeLayoutResponseSuccess(expectedResult)
    }

    @Test
    fun `given index is between visible item index when getLayoutData should call use case`() {
        val index = 1
        val warehouseId = "1"
        val firstVisibleItemIndex = 0
        val lastVisibleItemIndex = 3

        onGetHomeLayout_thenReturn(createHomeLayoutList())
        onGetTicker_thenReturn(createTicker())

        viewModel.getHomeLayout(LocalCacheModel())
        viewModel.getLayoutData(index, warehouseId, firstVisibleItemIndex , lastVisibleItemIndex, LocalCacheModel())

        val expectedResult = HomeLayoutListUiModel(
            items = listOf(
                HomeLayoutItemUiModel(HomeChooseAddressWidgetUiModel(id = "0"), HomeLayoutItemState.LOADED),
                HomeLayoutItemUiModel(createHomeTickerDataModel(), HomeLayoutItemState.LOADED),
                HomeLayoutItemUiModel(createDynamicLegoBannerDataModel(
                    "34923",
                    "",
                    "Lego Banner"
                ), HomeLayoutItemState.NOT_LOADED),
                HomeLayoutItemUiModel(createCategoryGridDataModel(
                    "11111",
                    "Category Tokonow",
                    emptyList(),
                    TokoNowLayoutState.LOADING
                ), HomeLayoutItemState.NOT_LOADED),
                HomeLayoutItemUiModel(createSliderBannerDataModel(
                    "2222",
                    "",
                    "Banner Tokonow"
                ), HomeLayoutItemState.NOT_LOADED)
            ),
            state = TokoNowLayoutState.SHOW,
            isInitialLoad = false,
            isLoadDataFinished = false,
            nextItemIndex = 2
        )

        verifyGetTickerUseCaseCalled()
        verifyGetHomeLayoutResponseSuccess(expectedResult)
    }

    @Test
    fun `when removeTickerWidget should remove ticker from home layout list`() {
        onGetHomeLayout_thenReturn(createHomeLayoutList())

        viewModel.getHomeLayout(LocalCacheModel())
        viewModel.removeTickerWidget("1")

        val expectedResult = HomeLayoutListUiModel(
            items = listOf(
                HomeLayoutItemUiModel(HomeChooseAddressWidgetUiModel(id = "0"), HomeLayoutItemState.LOADED),
                HomeLayoutItemUiModel(createDynamicLegoBannerDataModel(
                    "34923",
                    "",
                    "Lego Banner"
                ), HomeLayoutItemState.NOT_LOADED),
                HomeLayoutItemUiModel(createCategoryGridDataModel(
                    "11111",
                    "Category Tokonow",
                    emptyList(),
                    TokoNowLayoutState.LOADING
                ), HomeLayoutItemState.NOT_LOADED),
                HomeLayoutItemUiModel(createSliderBannerDataModel(
                    "2222",
                    "",
                    "Banner Tokonow"
                ), HomeLayoutItemState.NOT_LOADED)
            ),
            state = TokoNowLayoutState.SHOW,
            isInitialLoad = false,
            isLoadDataFinished = true,
            nextItemIndex = 0
        )

        verifyGetHomeLayoutResponseSuccess(expectedResult)
    }

    @Test
    fun `given getTicker error when getLayoutData should remove ticker from home layout list`() {
        onGetHomeLayout_thenReturn(createHomeLayoutList())
        onGetTicker_thenReturn(NullPointerException())

        viewModel.getHomeLayout(LocalCacheModel())
        viewModel.getLayoutData(1, "1", 0, 1, LocalCacheModel())

        val expectedResult = HomeLayoutListUiModel(
            items = listOf(
                HomeLayoutItemUiModel(HomeChooseAddressWidgetUiModel(id = "0"), HomeLayoutItemState.LOADED),
                HomeLayoutItemUiModel(createDynamicLegoBannerDataModel(
                    "34923",
                    "",
                    "Lego Banner"
                ), HomeLayoutItemState.NOT_LOADED),
                HomeLayoutItemUiModel(createCategoryGridDataModel(
                    "11111",
                    "Category Tokonow",
                    emptyList(),
                    TokoNowLayoutState.LOADING
                ), HomeLayoutItemState.NOT_LOADED),
                HomeLayoutItemUiModel(createSliderBannerDataModel(
                    "2222",
                    "",
                    "Banner Tokonow"
                ), HomeLayoutItemState.NOT_LOADED)
            ),
            state = TokoNowLayoutState.SHOW,
            isInitialLoad = false,
            isLoadDataFinished = false,
            nextItemIndex = 1
        )

        verifyGetHomeLayoutResponseSuccess(expectedResult)
    }

    @Test
    fun `given get dynamic lego layout data error when getLayoutData should remove dynamic lego from layout list`() {
        onGetHomeLayout_thenReturn(createHomeLayoutList())
        onGetHomeLayoutData_thenReturn(NullPointerException())

        viewModel.getHomeLayout(LocalCacheModel())
        viewModel.getLayoutData(2, "1", 0, 2, LocalCacheModel())

        val expectedResult = HomeLayoutListUiModel(
            items = listOf(
                HomeLayoutItemUiModel(HomeChooseAddressWidgetUiModel(id = "0"), HomeLayoutItemState.LOADED),
                HomeLayoutItemUiModel(createHomeTickerDataModel(tickers = emptyList()), HomeLayoutItemState.NOT_LOADED),
                HomeLayoutItemUiModel(createCategoryGridDataModel(
                    "11111",
                    "Category Tokonow",
                    emptyList(),
                    TokoNowLayoutState.LOADING
                ), HomeLayoutItemState.NOT_LOADED),
                HomeLayoutItemUiModel(createSliderBannerDataModel(
                    "2222",
                    "",
                    "Banner Tokonow"
                ), HomeLayoutItemState.NOT_LOADED)
            ),
            state = TokoNowLayoutState.SHOW,
            isInitialLoad = false,
            isLoadDataFinished = false,
            nextItemIndex = 1
        )

        verifyGetHomeLayoutResponseSuccess(expectedResult)
    }
}