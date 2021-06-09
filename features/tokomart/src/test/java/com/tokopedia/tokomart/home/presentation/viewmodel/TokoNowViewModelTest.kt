package com.tokopedia.tokomart.home.presentation.viewmodel

import com.tokopedia.tokomart.data.*
import com.tokopedia.tokomart.home.constant.HomeStaticLayoutId.Companion.EMPTY_STATE_NO_ADDRESS
import com.tokopedia.tokomart.home.domain.mapper.HomeLayoutMapper.mapHomeLayoutList
import com.tokopedia.tokomart.home.domain.mapper.TickerMapper.mapTickerData
import com.tokopedia.tokomart.home.presentation.fragment.TokoMartHomeFragment.Companion.SOURCE
import com.tokopedia.tokomart.home.presentation.uimodel.*
import org.junit.Test
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyString

class TokoMartHomeViewModelTest: TokoMartHomeViewModelTestFixture() {

    @Test
    fun `when getting homeLayout should run and give the success result`() {
        onGetTicker_thenReturn(createTicker())
        onGetHomeLayout_thenReturn(createHomeLayoutList())

        viewModel.getHomeLayout(hasTickerBeenRemoved = false)

        verifyGetTickerUseCaseCalled()
        verifyGetHomeLayoutUseCaseCalled()

        val expectedResponse = HomeLayoutListUiModel(
                result = mapHomeLayoutList(
                        createHomeLayoutList(),
                        mapTickerData(createTicker().ticker.tickerList)
                ),
                isInitialLoad = true,
                isHeaderBackgroundShowed = true
        )
        verifyGetHomeLayoutResponseSuccess(expectedResponse)
    }

    @Test
    fun `when getting homeLayout`() {
        onGetTicker_thenReturn(createTicker())
        onGetHomeLayout_thenReturn(createHomeLayoutList())

        viewModel.getHomeLayout(hasTickerBeenRemoved = false)

        verifyGetTickerUseCaseCalled()
        verifyGetHomeLayoutUseCaseCalled()

        val expectedResponse = HomeLayoutListUiModel(
                result = mapHomeLayoutList(
                        createHomeLayoutList(),
                        mapTickerData(createTicker().ticker.tickerList)
                ),
                isInitialLoad = true,
                isHeaderBackgroundShowed = true
        )
        verifyGetHomeLayoutResponseSuccess(expectedResponse)
    }

    @Test
    fun `when getting chooseAddressWidget should run and give the success result`() {
        viewModel.getChooseAddressWidget()

        val expectedResponse = createChooseAddressWidget()

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
        onGetMiniCart_thenReturn(createMiniCartSimplifier())

        viewModel.getMiniCart(shopId = listOf("123"))

        verifyMiniCartResponseSuccess(createMiniCartSimplifier())
    }

    @Test
    fun `when getting data for mini cart should throw mini cart exception`(){
        onGetMiniCart_thenReturn(Exception())

        viewModel.getMiniCart(shopId = listOf("123"))

        verifyMiniCartFail()
    }

    @Test
    fun `when getting layout home should run and give success result`(){
        onGetDataFromTokoMart_thenReturn(createCategoryListData())
        onGetHomeLayoutData_thenReturn(createHomeLayoutListwithBanner())
        onGetTicker_thenReturn(createTicker())
        onGetHomeLayout_thenReturn(createHomeLayoutListwithHome())

        viewModel.getHomeLayout(hasTickerBeenRemoved = false)

        viewModel.getLayoutData("1")

        verifyGetTickerUseCaseCalled()
        verifyGetHomeLayoutUseCaseCalled()
        verifyGetDataFromTokoMartCalled()
        verifyGetHomeLayoutResponseSuccess(createHomeLayoutListWithCategory())
    }

    @Test
    fun `when getting layout home should run throw exception`(){
        onGetDataFromTokoMart_thenReturn(Exception())
        onGetHomeLayoutData_thenReturn(Exception())
        onGetTicker_thenReturn(createTicker())
        onGetHomeLayout_thenReturn(createHomeLayoutListwithHome())

        viewModel.getHomeLayout(hasTickerBeenRemoved = false)

        viewModel.getLayoutData("1")

        verifyGetTickerUseCaseCalled()
        verifyGetHomeLayoutUseCaseCalled()
        verifyGetDataFromTokoMartCalled()
        verifyDataLayoutFail()
    }
}