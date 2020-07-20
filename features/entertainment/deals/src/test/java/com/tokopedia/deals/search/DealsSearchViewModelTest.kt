package com.tokopedia.deals.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.deals.common.domain.DealsSearchUseCase
import com.tokopedia.deals.common.model.response.SearchData
import com.tokopedia.deals.common.utils.DealsTestDispatcherProvider
import com.tokopedia.deals.location_picker.model.response.Location
import com.tokopedia.deals.search.domain.usecase.DealsSearchInitialLoadUseCase
import com.tokopedia.deals.search.domain.viewmodel.DealsSearchViewModel
import com.tokopedia.deals.search.model.response.InitialLoadData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DealsSearchViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val dispatcher = DealsTestDispatcherProvider()

    private val loadInitialDataUseCase: DealsSearchInitialLoadUseCase = mockk()
    private val searchUseCase: DealsSearchUseCase = mockk()

    private lateinit var viewModel: DealsSearchViewModel

    private val mockThrowable = Throwable("Fetch failed")

    @Before
    fun setup() {
        viewModel = DealsSearchViewModel(loadInitialDataUseCase, searchUseCase, dispatcher)
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
    }

    @Test
    fun getInitialData_fetchSuccess_initialLoadShouldBeSuccess() {
        val mockInitialLoadData = InitialLoadData()

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
        val mockSearchData = SearchData()

        // given
        coEvery {
            searchUseCase.getDealsSearchResult(
                any(), any(), any(), any(), any(), any(), any(), any(), any()
            )
        } coAnswers {
            firstArg<(SearchData) -> Unit>().invoke(mockSearchData)
        }

        // when
        viewModel.loadMoreData("", Location(), "", 0)

        // then
        assert((viewModel.dealsLoadMoreResponse.value as Success).data == mockSearchData.eventSearch)
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
        val mockSearchData = SearchData()

        // given
        coEvery {
            searchUseCase.getDealsSearchResult(
                any(), any(), any(), any(), any(), any(), any(), any(), any()
            )
        } coAnswers {
            firstArg<(SearchData) -> Unit>().invoke(mockSearchData)
        }

        // when
        viewModel.searchDeals("", Location(), "", 0)

        // then
        assert((viewModel.dealsSearchResponse.value as Success).data == mockSearchData.eventSearch)
    }
}