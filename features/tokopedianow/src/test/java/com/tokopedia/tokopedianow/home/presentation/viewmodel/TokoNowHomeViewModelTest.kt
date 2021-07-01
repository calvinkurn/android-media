package com.tokopedia.tokopedianow.home.presentation.viewmodel

import com.tokopedia.home_component.model.ChannelConfig
import com.tokopedia.home_component.model.ChannelHeader
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.ChannelStyle
import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.tokopedianow.data.*
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.constant.HomeLayoutState
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.EMPTY_STATE_NO_ADDRESS
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapHomeLayoutList
import com.tokopedia.tokopedianow.home.domain.mapper.TickerMapper.mapTickerData
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment.Companion.SOURCE
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeCategoryGridUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeCategoryItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutListUiModel
import org.junit.Test
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyString

class TokoNowHomeViewModelTest: TokoNowHomeViewModelTestFixture() {

    @Test
    fun `when getting homeLayout should run and give the success result`() {
        onGetTicker_thenReturn(createTicker())
        onGetHomeLayout_thenReturn(createHomeLayoutList())

        viewModelTokoNow.getHomeLayout(hasTickerBeenRemoved = false)

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

        viewModelTokoNow.getHomeLayout(hasTickerBeenRemoved = true)

        viewModelTokoNow.getInitialLayoutData(1, "1", true)

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
        viewModelTokoNow.getLoadingState()

        val expectedResponse = createLoadingState()

        verifyGetHomeLayoutResponseSuccess(expectedResponse)
    }

    @Test
    fun `when getting emptyState should run and give the success result`() {
        val idEmptyState = EMPTY_STATE_NO_ADDRESS

        viewModelTokoNow.getEmptyState(idEmptyState)

        val expectedResponse = createEmptyState(idEmptyState)

        verifyGetHomeLayoutResponseSuccess(expectedResponse)
    }


    @Test
    fun `when getting keywordSearch should run and give the success result`() {
        onGetKeywordSearch_thenReturn(createKeywordSearch())

        viewModelTokoNow.getKeywordSearch(anyBoolean(), anyString(), anyString())

        verifyGetKeywordSearchUseCaseCalled()

        val expectedResponse = createKeywordSearch()
        verifyKeywordSearchResponseSuccess(expectedResponse.searchData)
    }

    @Test
    fun `when getting chooseAddress should run and give the success result`() {
        onGetChooseAddress_thenReturn(createChooseAddress())

        viewModelTokoNow.getChooseAddress(SOURCE)

        verifyGetChooseAddress()

        val expectedResponse = createChooseAddress().response
        verfifyGetChooseAddressSuccess(expectedResponse)
    }

    @Test
    fun `when getting homeLayout should throw ticker's exception and get the failed result`() {
        onGetTicker_thenReturn(Exception())

        viewModelTokoNow.getHomeLayout(hasTickerBeenRemoved = false)

        verifyGetHomeLayoutResponseFail()
    }

    @Test
    fun `when getting homeLayout should throw homeLayout's exception and get the failed result`() {
        onGetHomeLayout_thenReturn(Exception())

        viewModelTokoNow.getHomeLayout(hasTickerBeenRemoved = false)

        verifyGetHomeLayoutResponseFail()
    }

    @Test
    fun `when getting chooseAddress should throw chooseAddress's exception and get failed result`() {
        onGetChooseAddress_thenReturn(Exception())

        viewModelTokoNow.getChooseAddress(SOURCE)

        verifyGetChooseAddressFail()
    }

    @Test
    fun `when getting data for mini cart should run and give success result`(){
        onGetMiniCart_thenReturn(createMiniCartSimplifier())

        viewModelTokoNow.getMiniCart(shopId = listOf("123"), "233")

        verifyMiniCartResponseSuccess(createMiniCartSimplifier())
    }

    @Test
    fun `when getting data for mini cart empty category should run`(){
        onGetMiniCart_thenReturn(createMiniCartSimplifier())

        viewModelTokoNow.getMiniCart(shopId = listOf(), "233")

        verifyMiniCartNullResponse()
    }

    @Test
    fun `when getting data for mini cart should throw mini cart exception`(){
        onGetMiniCart_thenReturn(Exception())

        viewModelTokoNow.getMiniCart(shopId = listOf("123"), "233")

        verifyMiniCartFail()
    }

    @Test
    fun `when getting data category grid should run and give the success result`() {
        //set mock data
        onGetHomeLayout_thenReturn(createHomeLayoutList())
        onGetCategoryList_thenReturn(createCategoryGridListFirstFetch())

        //fetch homeLayout
        viewModelTokoNow.getHomeLayout(true)

        //fetch widget one by one
        viewModelTokoNow.getInitialLayoutData(1, "1", true)

        //set second mock data to replace first mock data category list
        onGetCategoryList_thenReturn(createCategoryGridListSecondFetch())

        //prepare model that need to be changed
        val model = HomeCategoryGridUiModel(
                id="11111",
                title="Category Tokonow",
                categoryList = emptyList(),
                state=HomeLayoutState.SHOW
        )

        viewModelTokoNow.getCategoryGrid(model, "1")

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
        viewModelTokoNow.getHomeLayout(true)

        //fetch widget one by one
        viewModelTokoNow.getInitialLayoutData(1, "1", true)

        //set second mock data to replace first mock data category list
        onGetCategoryList_thenReturn(Exception())

        //prepare model that need to be changed
        val model = HomeCategoryGridUiModel(
                id="11111",
                title="Category Tokonow",
                categoryList = emptyList(),
                state=HomeLayoutState.SHOW
        )

        viewModelTokoNow.getCategoryGrid(model, "1")

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
        viewModelTokoNow.getHomeLayout(true)

        //fetch widget one by one
        viewModelTokoNow.getInitialLayoutData(0, "1", true)

        viewModelTokoNow.getMoreLayoutData("1", 2, 2)

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
}