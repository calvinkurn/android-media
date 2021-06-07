package com.tokopedia.tokomart.home.presentation.viewmodel

import com.tokopedia.tokomart.data.createHomeLayoutList
import com.tokopedia.tokomart.data.createKeywordSearch
import com.tokopedia.tokomart.data.createTicker
import com.tokopedia.tokomart.home.domain.mapper.HomeLayoutMapper.mapHomeLayoutList
import com.tokopedia.tokomart.home.domain.mapper.TickerMapper.mapTickerData
import com.tokopedia.tokomart.home.presentation.uimodel.HomeLayoutListUiModel
import org.junit.Test
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyString

class TokoNowViewModelTest: TokoNowViewModelTestFixture() {

    @Test
    fun `when getting homeLayout should run and give the success result`() {
        onGetTicker_thenReturn(createTicker())
        onGetHomeLayout_thenReturn(createHomeLayoutList())

        viewModel.getHomeLayout()

        verifyGetTickerUseCaseCalled()
        verifyGetHomeLayoutUseCaseCalled()

        val expectedResponse = HomeLayoutListUiModel(
                result = mapHomeLayoutList(
                        createHomeLayoutList(),
                        mapTickerData(createTicker().ticker.tickerList)
                ),
                isInitialLoad = true
        )
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
    fun `when getting homeLayout should throw ticker's exception and get the failed result`() {
        onGetTicker_thenReturn(Exception())

        viewModel.getHomeLayout()

        verifyGetHomeLayoutResponseFail()
    }

    @Test
    fun `when getting homeLayout should throw homeLayout's exception and get the failed result`() {
        onGetHomeLayout_thenReturn(Exception())

        viewModel.getHomeLayout()

        verifyGetHomeLayoutResponseFail()
    }

}