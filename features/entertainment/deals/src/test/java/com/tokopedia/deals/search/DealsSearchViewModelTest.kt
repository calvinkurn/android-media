package com.tokopedia.deals.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.tokopedia.deals.DealsJsonMapper
import com.tokopedia.deals.common.domain.DealsSearchUseCase
import com.tokopedia.deals.common.model.response.SearchData
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.deals.location_picker.model.response.Location
import com.tokopedia.deals.search.domain.usecase.DealsSearchInitialLoadUseCase
import com.tokopedia.deals.search.domain.viewmodel.DealsSearchViewModel
import com.tokopedia.deals.search.model.response.InitialLoadData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DealsSearchViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val dispatcher = CoroutineTestDispatchersProvider

    private val loadInitialDataUseCase: DealsSearchInitialLoadUseCase = mockk()
    private val searchUseCase: DealsSearchUseCase = mockk()

    private lateinit var viewModel: DealsSearchViewModel
    private lateinit var mockThrowable: Throwable
    private lateinit var mockSearchLoadMore: SearchData
    private lateinit var mockInitialLoadData: InitialLoadData
    private lateinit var mockEventSearchData: SearchData

    @Before
    fun setup() {
        viewModel = DealsSearchViewModel(loadInitialDataUseCase, searchUseCase, dispatcher)
        mockSearchLoadMore = Gson().fromJson(
                DealsJsonMapper.getJson("search_load_more.json"),
                SearchData::class.java
        )
        mockInitialLoadData = Gson().fromJson(
                DealsJsonMapper.getJson("search_initial_load.json"),
                InitialLoadData::class.java
        )
        mockEventSearchData = Gson().fromJson(
                DealsJsonMapper.getJson("event_search.json"),
                SearchData::class.java
        )
        mockThrowable = Throwable("Fetch failed")
    }

    @Test
    fun getInitialData_fetchFailed_initialLoadShouldBeFailed() {
        // given
        coEvery {
            loadInitialDataUseCase.getDealsInitialLoadResult(
                    any(), any(), any(), any(), any()
            )
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewModel.getInitialData(Location(), "1")

        val initialResponse = viewModel.dealsInitialResponse.value as Result
        assert(initialResponse is Fail)
        assertEquals(mockThrowable, (initialResponse as Fail).throwable)
    }

    @Test
    fun getInitialData_fetchSuccess_initialLoadShouldBeSuccess() {
        // given
        coEvery {
            loadInitialDataUseCase.getDealsInitialLoadResult(
                    any(), any(), any(), any(), any()
            )
        } coAnswers {
            firstArg<(InitialLoadData) -> Unit>().invoke(mockInitialLoadData)
        }

        // when
        viewModel.getInitialData(Location(), "1")

        // then
        assert((viewModel.dealsInitialResponse.value as Success).data == mockInitialLoadData)
    }

    @Test
    fun loadMoreData_fetchFailed_loadMoreShouldBeFailed() {
        // given
        coEvery {
            searchUseCase.getDealsSearchResult(
                    any(), any(), any(), any(), any(), any(), any(), any(), any()
            )
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        // when
        viewModel.loadMoreData("", Location(), "", 0)

        // then
        assert((viewModel.dealsLoadMoreResponse.value as Fail).throwable == mockThrowable)
    }

    @Test
    fun loadMoreData_fetchSuccess_loadMoreShouldBeSuccess() {
        // given
        coEvery {
            searchUseCase.getDealsSearchResult(
                    any(), any(), any(), any(), any(), any(), any(), any(), any()
            )
        } coAnswers {
            firstArg<(SearchData) -> Unit>().invoke(mockSearchLoadMore)
        }

        // when
        viewModel.loadMoreData("", Location(), "", 0)

        // then
        assert((viewModel.dealsLoadMoreResponse.value as Success).data == mockSearchLoadMore.eventSearch)
    }

    @Test
    fun searchDeals_fetchFailed_searchResponseShouldBeFailed() {
        // given
        coEvery {
            searchUseCase.getDealsSearchResult(
                    any(), any(), any(), any(), any(), any(), any(), any(), any()
            )
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        // when
        viewModel.searchDeals("", Location(), "", 0)

        // then
        assert((viewModel.dealsSearchResponse.value as Fail).throwable == mockThrowable)
    }

    @Test
    fun searchDeals_fetchSuccess_searchResponseShouldBeSuccess() {
        // given
        coEvery {
            searchUseCase.getDealsSearchResult(
                    any(), any(), any(), any(), any(), any(), any(), any(), any()
            )
        } coAnswers {
            firstArg<(SearchData) -> Unit>().invoke(mockEventSearchData)
        }

        // when
        viewModel.searchDeals("", Location(), "", 0)

        // then
        assert((viewModel.dealsSearchResponse.value as Success).data == mockEventSearchData.eventSearch)
    }

    @Test
    fun getInitialData_fetchSuccessCoordinateNotNull_initialLoadShouldBeSuccess() {
        // given
        coEvery {
            loadInitialDataUseCase.getDealsInitialLoadResult(
                    any(), any(), any(), any(), any()
            )
        } coAnswers {
            firstArg<(InitialLoadData) -> Unit>().invoke(mockInitialLoadData)
        }

        // when
        viewModel.getInitialData(Location(coordinates = "0,0"), "1")

        // then
        assert((viewModel.dealsInitialResponse.value as Success).data == mockInitialLoadData)
    }

    @Test
    fun getInitialData_fetchSuccessLocationNull_initialLoadShouldBeSuccess() {
        // given
        coEvery {
            loadInitialDataUseCase.getDealsInitialLoadResult(
                    any(), any(), any(), any(), any()
            )
        } coAnswers {
            firstArg<(InitialLoadData) -> Unit>().invoke(mockInitialLoadData)
        }

        // when
        viewModel.getInitialData(null, "1")

        // then
        assert((viewModel.dealsInitialResponse.value as Success).data == mockInitialLoadData)
    }
}