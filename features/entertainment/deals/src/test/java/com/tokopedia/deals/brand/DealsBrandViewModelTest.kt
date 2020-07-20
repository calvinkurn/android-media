package com.tokopedia.deals.brand

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.deals.brand.domain.viewmodel.DealsBrandViewModel
import com.tokopedia.deals.common.domain.DealsSearchUseCase
import com.tokopedia.deals.common.model.response.SearchData
import com.tokopedia.deals.common.ui.dataview.DealsBrandsDataView
import com.tokopedia.deals.common.utils.DealsTestDispatcherProvider
import com.tokopedia.deals.location_picker.model.response.Location
import com.tokopedia.deals.search.model.response.InitialLoadData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DealsBrandViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = DealsTestDispatcherProvider()
    private val useCase: DealsSearchUseCase = mockk()
    private lateinit var viewModel: DealsBrandViewModel
    private val mockThrowable = Throwable("Fetch failed")

    @Before
    fun setup() {
        viewModel = DealsBrandViewModel(useCase, dispatcher)
    }

    @Test
    fun getInitialData_shimmerDataShouldBeSuccess() {
        // when
        viewModel.getInitialData()

        // then
        val shimmerData = viewModel.dealsShimmerData.value as Success
        assert(shimmerData.data.isNotEmpty())
    }

    @Test
    fun getBrandList_fetchFailed_searchResponseShouldBeFailed() {
        // given
        coEvery {
            useCase.getDealsSearchResult(
                any(), any(), any(), any(), any(), any(), any(), any(), any()
            )
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        // when
        viewModel.getBrandList("", Location(), "", 0)

        // then
        assert((viewModel.dealsSearchResponse.value as Fail).throwable == mockThrowable)
    }


    @Test
    fun getBrandList_fetchSuccess_searchResponseShouldBeSuccess() {
        val mockSearhData = SearchData()

        // given
        coEvery {
            useCase.getDealsSearchResult(
                any(), any(), any(), any(), any(), any(), any(), any(), any()
            )
        } coAnswers {
            firstArg<(SearchData) -> Unit>().invoke(mockSearhData)
        }

        // when
        viewModel.getBrandList("", Location(), "1", 0)

        // then
        assert(viewModel.dealsSearchResponse.value is Success)
    }
}