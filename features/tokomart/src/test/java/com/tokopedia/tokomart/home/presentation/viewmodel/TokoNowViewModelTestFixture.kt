package com.tokopedia.tokomart.home.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.tokomart.categorylist.domain.usecase.GetCategoryListUseCase
import com.tokopedia.tokomart.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokomart.home.domain.model.KeywordSearchData
import com.tokopedia.tokomart.home.domain.model.SearchPlaceholder
import com.tokopedia.tokomart.home.domain.model.TickerResponse
import com.tokopedia.tokomart.home.domain.usecase.GetHomeLayoutDataUseCase
import com.tokopedia.tokomart.home.domain.usecase.GetHomeLayoutListUseCase
import com.tokopedia.tokomart.home.domain.usecase.GetKeywordSearchUseCase
import com.tokopedia.tokomart.home.domain.usecase.GetTickerUseCase
import com.tokopedia.tokomart.home.presentation.uimodel.HomeLayoutListUiModel
import com.tokopedia.tokomart.util.getOrAwaitValue
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.mockito.ArgumentMatchers.*

abstract class TokoNowViewModelTestFixture {

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

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: TokoMartHomeViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = TokoMartHomeViewModel(
                getHomeLayoutListUseCase,
                getHomeLayoutDataUseCase,
                getCategoryListUseCase,
                getKeywordSearchUseCase,
                getTickerUseCase,
                getMiniCartUseCase,
                CoroutineTestDispatchersProvider
        )
    }

    protected fun verifyGetHomeLayoutResponseSuccess(expectedResponse: HomeLayoutListUiModel) {
        val actualResponse = viewModel.homeLayoutList.getOrAwaitValue()
        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    protected fun verifyKeywordSearchResponseSuccess(expectedResponse: SearchPlaceholder) {
        val actualResponse = viewModel.keywordSearch.getOrAwaitValue()
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    protected fun verifyGetHomeLayoutResponseFail() {
        val actualResponse = viewModel.homeLayoutList.getOrAwaitValue()
        Assert.assertTrue(actualResponse is Fail)
    }

    protected fun verifyGetHomeLayoutUseCaseCalled() {
        coVerify { getHomeLayoutListUseCase.execute() }
    }

    protected fun verifyGetTickerUseCaseCalled() {
        coVerify { getTickerUseCase.execute(any()) }
    }

    protected fun verifyGetKeywordSearchUseCaseCalled() {
        coVerify { getKeywordSearchUseCase.execute(anyBoolean(), anyString(), anyString()) }
    }

    protected fun onGetTicker_thenReturn(tickerResponse: TickerResponse) {
        coEvery { getTickerUseCase.execute(any()) } returns tickerResponse
    }

    protected fun onGetHomeLayout_thenReturn(layoutResponse: List<HomeLayoutResponse>) {
        coEvery { getHomeLayoutListUseCase.execute() } returns layoutResponse
    }

    protected fun onGetKeywordSearch_thenReturn(keywordSearchResponse: KeywordSearchData) {
        coEvery { getKeywordSearchUseCase.execute(anyBoolean(), anyString(), anyString()) } returns keywordSearchResponse
    }

    protected fun onGetTicker_thenReturn(errorThrowable: Throwable) {
        coEvery { getTickerUseCase.execute(any()) } throws errorThrowable
    }

    protected fun onGetHomeLayout_thenReturn(errorThrowable: Throwable) {
        coEvery { getHomeLayoutListUseCase.execute() } throws errorThrowable
    }

}