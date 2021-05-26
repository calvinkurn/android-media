package com.tokopedia.deals.brand

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.deals.brand.domain.viewmodel.DealsBrandViewModel
import com.tokopedia.deals.brand.mapper.DealsBrandMapper
import com.tokopedia.deals.common.domain.DealsSearchUseCase
import com.tokopedia.deals.common.model.response.SearchData
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.deals.location_picker.model.response.Location
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.tokopedia.deals.DealsJsonMapper.getJson
import junit.framework.Assert.assertEquals

class DealsBrandViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private val useCase: DealsSearchUseCase = mockk()
    private lateinit var viewModel: DealsBrandViewModel

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
        val mockThrowable = Throwable("Fetch failed")
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
        assertEquals((viewModel.dealsSearchResponse.value as Fail).throwable,mockThrowable)
    }


    @Test
    fun getBrandList_fetchSuccess_searchResponseShouldBeSuccess() {
        val mockSearhData = Gson().fromJson(getJson("brandpage.json"), SearchData::class.java)
        val mockBrand = DealsBrandMapper.mapBrandToBaseItemViewModel(mockSearhData.eventSearch.brands, 0)
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
        assertEquals((viewModel.dealsSearchResponse.value as Success).data, mockBrand)
    }

    @Test
    fun getBrandList_locationCoordinateNotNull_searchResponseShouldBeSuccess() {
        val mockSearhData = Gson().fromJson(getJson("brandpage.json"), SearchData::class.java)
        val mockBrand = DealsBrandMapper.mapBrandToBaseItemViewModel(mockSearhData.eventSearch.brands, 0)
        // given
        coEvery {
            useCase.getDealsSearchResult(
                    any(), any(), any(), any(), any(), any(), any(), any(), any()
            )
        } coAnswers {
            firstArg<(SearchData) -> Unit>().invoke(mockSearhData)
        }

        // when
        viewModel.getBrandList("", Location(coordinates = "0.0"), "1", 0)

        // then
        assert(viewModel.dealsSearchResponse.value is Success)
        assertEquals((viewModel.dealsSearchResponse.value as Success).data, mockBrand)
    }

    @Test
    fun getBrandList_locationNull_searchResponseShouldBeSuccess() {
        val mockSearhData = Gson().fromJson(getJson("brandpage.json"), SearchData::class.java)
        val mockBrand = DealsBrandMapper.mapBrandToBaseItemViewModel(mockSearhData.eventSearch.brands, 0)
        // given
        coEvery {
            useCase.getDealsSearchResult(
                    any(), any(), any(), any(), any(), any(), any(), any(), any()
            )
        } coAnswers {
            firstArg<(SearchData) -> Unit>().invoke(mockSearhData)
        }

        // when
        viewModel.getBrandList("", null, "1", 0)

        // then
        assert(viewModel.dealsSearchResponse.value is Success)
        assertEquals((viewModel.dealsSearchResponse.value as Success).data, mockBrand)
    }
}