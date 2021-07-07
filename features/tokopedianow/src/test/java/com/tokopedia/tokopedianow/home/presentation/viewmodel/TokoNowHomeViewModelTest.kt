package com.tokopedia.tokopedianow.home.presentation.viewmodel

import com.tokopedia.home_component.model.ChannelConfig
import com.tokopedia.home_component.model.ChannelHeader
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.ChannelStyle
import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.tokopedianow.data.*
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.constant.HomeLayoutState
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.CHOOSE_ADDRESS_WIDGET_ID
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.EMPTY_STATE_NO_ADDRESS
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapHomeLayoutList
import com.tokopedia.tokopedianow.home.domain.mapper.TickerMapper.mapTickerData
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

        viewModel.getHomeLayout(hasTickerBeenRemoved = false)

        val expectedResponse = HomeLayoutListUiModel(
                result = mapHomeLayoutList(
                        createHomeLayoutList(),
                        mapTickerData(createTicker().ticker.tickerList)
                ),
                state = HomeLayoutState.SHOW
        )
        verifyGetHomeLayoutResponseSuccess(expectedResponse)
        verifyGetTickerUseCaseCalled()
        verifyGetHomeLayoutUseCaseCalled()
    }

    @Test
    fun `when getting homeLayoutData should run and give the success result`() {
        onGetHomeLayout_thenReturn(createHomeLayoutListForBannerOnly())
        onGetHomeLayoutData_thenReturn(createHomeLayoutData())

        viewModel.getHomeLayout(hasTickerBeenRemoved = true)

        viewModel.getInitialLayoutData(1, "1", true)

        val expectedResponse = HomeLayoutItemUiModel(
            BannerDataModel(
                channelModel= ChannelModel(
                    id="2222",
                    groupId="",
                    style= ChannelStyle.ChannelHome,
                    channelHeader= ChannelHeader(name="Banner Tokonow"),
                    channelConfig=ChannelConfig(layout="banner_carousel_v2") ,
                    layout="banner_carousel_v2")
            ),
            HomeLayoutItemState.LOADED
        )

        verifyGetBannerResponseSuccess(expectedResponse)
        verifyGetHomeLayoutUseCaseCalled()
        verifyGetHomeLayoutDataUseCaseCalled()
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

        viewModel.getHomeLayout(hasTickerBeenRemoved = false)

        verifyGetHomeLayoutResponseFail()
    }

    @Test
    fun `when getting homeLayout should throw homeLayout's exception and get the failed result`() {
        onGetHomeLayout_thenReturn(Exception())

        viewModel.getHomeLayout(hasTickerBeenRemoved = false)

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
        viewModel.getHomeLayout(true)

        //fetch widget one by one
        viewModel.getInitialLayoutData(1, "1", true)

        //set second mock data to replace first mock data category list
        onGetCategoryList_thenReturn(createCategoryGridListSecondFetch())

        //prepare model that need to be changed
        val model = HomeCategoryGridUiModel(
                id="11111",
                title="Category Tokonow",
                categoryList = emptyList(),
                state=HomeLayoutState.SHOW
        )

        viewModel.getCategoryGrid(model, "1")

        //prepare model for expectedResult
        val expectedResponse = HomeLayoutItemUiModel(
            HomeCategoryGridUiModel(
                id="11111",
                title="Category Tokonow",
                categoryList = listOf(HomeCategoryItemUiModel(
                    id="1",
                    title="Category 1",
                    imageUrl="tokopedia://",
                    appLink="tokoepdia://"
                )),
                state=HomeLayoutState.SHOW
            ),
            HomeLayoutItemState.LOADED
        )

        // verify use case called and response
        verifyGetCategoryListResponseSuccess(expectedResponse)
        verifyGetCategoryListUseCaseCalled()
        verifyGetHomeLayoutUseCaseCalled()
    }

    @Test
    fun `when getting data category grid should run, throw exception and give the success result`() {
        //set mock data
        onGetHomeLayout_thenReturn(createHomeLayoutList())
        onGetCategoryList_thenReturn(createCategoryGridListFirstFetch())

        //fetch homeLayout
        viewModel.getHomeLayout(true)

        //fetch widget one by one
        viewModel.getInitialLayoutData(1, "1", true)

        //set second mock data to replace first mock data category list
        onGetCategoryList_thenReturn(Exception())

        //prepare model that need to be changed
        val model = HomeCategoryGridUiModel(
                id="11111",
                title="Category Tokonow",
                categoryList = emptyList(),
                state=HomeLayoutState.SHOW
        )

        viewModel.getCategoryGrid(model, "1")

        //prepare model for expectedResult
        val expectedResponse = HomeLayoutItemUiModel(
            HomeCategoryGridUiModel(
                id="11111",
                title="Category Tokonow",
                categoryList = null,
                state=HomeLayoutState.HIDE
            ),
            HomeLayoutItemState.LOADED
        )

        // verify use case called and response
        verifyGetCategoryListResponseSuccess(expectedResponse)
        verifyGetCategoryListUseCaseCalled()
        verifyGetHomeLayoutUseCaseCalled()
    }

    @Test
    fun `when getting moreLayoutData should run and give the success result`() {
        //set mock data
        onGetTicker_thenReturn(createTicker())
        onGetHomeLayout_thenReturn(createHomeLayoutList())
        onGetCategoryList_thenReturn(createCategoryGridListFirstFetch())

        //fetch homeLayout
        viewModel.getHomeLayout(true)

        viewModel.getMoreLayoutData("1", 2, 2)

        //prepare model for expectedResult
        val expectedResponse = HomeLayoutItemUiModel(
                HomeCategoryGridUiModel(
                        id="11111",
                        title="Category Tokonow",
                        categoryList = listOf(HomeCategoryItemUiModel(
                                id="3",
                                title="Category 3",
                                imageUrl="tokopedia://",
                                appLink="tokoepdia://"
                        )),
                        state=HomeLayoutState.SHOW
                ),
                HomeLayoutItemState.LOADED
        )

        // verify use case called and response
        verifyGetCategoryListResponseSuccess(expectedResponse)
        verifyGetCategoryListUseCaseCalled()
        verifyGetHomeLayoutUseCaseCalled()
    }

    @Test
    fun `when getting moreLayoutData should not run and homeLayout livedata is null`() {
        val homeLayout = createHomeLayoutItemUiModelList()

        privateHomeLayoutItemList.set(viewModel, homeLayout)

        viewModel.getMoreLayoutData("1", 0, 0)
        verifyGetHomeLayoutNullResponse()

        viewModel.getMoreLayoutData("1", 1, 1)
        verifyGetHomeLayoutNullResponse()

        viewModel.getMoreLayoutData("1", 2, 2)
        verifyGetHomeLayoutNullResponse()
    }
}