package com.tokopedia.deals.location_picker

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.deals.DealsJsonMapper
import com.tokopedia.deals.common.model.response.SearchData
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.deals.location_picker.domain.usecase.DealsLandmarkLocationUseCase
import com.tokopedia.deals.location_picker.domain.usecase.DealsPopularCitiesUseCase
import com.tokopedia.deals.location_picker.domain.usecase.DealsPopularLocationUseCase
import com.tokopedia.deals.location_picker.domain.usecase.DealsSearchLocationUseCase
import com.tokopedia.deals.location_picker.domain.viewmodel.DealsLocationViewModel
import com.tokopedia.deals.location_picker.model.response.Location
import com.tokopedia.deals.location_picker.model.response.LocationData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DealsLocationViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private val searchLocationUseCase: DealsSearchLocationUseCase = mockk()
    private val popularCitiesUseCase: DealsPopularCitiesUseCase = mockk()
    private val popularLocationUseCase: DealsPopularLocationUseCase = mockk()
    private val landmarkLocationUseCase: DealsLandmarkLocationUseCase = mockk()

    private lateinit var viewModel: DealsLocationViewModel
    private lateinit var mockThrowable: Throwable
    private lateinit var mockEventLocationSearch: LocationData

    @Before
    fun setup() {
        viewModel = DealsLocationViewModel(
                searchLocationUseCase,
                popularCitiesUseCase,
                popularLocationUseCase,
                landmarkLocationUseCase,
                dispatcher
        )
        mockThrowable = Throwable("Fetch failed")
        mockEventLocationSearch = Gson().fromJson(
                DealsJsonMapper.getJson("event_location_search.json"),
                LocationData::class.java
        )
    }

    @Test
    fun getSearchedLocation_fetchFailed_searchLocationShouldBeFailed() {
        // given
        coEvery {
            searchLocationUseCase.getSearchedLocation(any(), any(), any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        // when
        viewModel.getSearchedLocation("")

        // then
        assertEquals(
                mockThrowable,
                (viewModel.dealsSearchedLocationResponse.value as Fail).throwable
        )
    }

    @Test
    fun getSearchedLocation_fetchSuccess_searchLocationShouldBeSuccess() {
        // given
        coEvery {
            searchLocationUseCase.getSearchedLocation(any(), any(), any(), any())
        } coAnswers {
            firstArg<(LocationData) -> Unit>().invoke(mockEventLocationSearch)
        }

        // when
        viewModel.getSearchedLocation("")

        // then
        val result = viewModel.dealsSearchedLocationResponse.value
        assert(result is Success)
        assertEquals(mockEventLocationSearch.eventLocationSearch, (result as Success).data)
    }

    @Test
    fun getLoadMoreSearchedLocation_fetchFailed_searchLocationShouldBeFailed() {
        // given
        coEvery {
            searchLocationUseCase.getSearchedLocation(any(), any(), any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        // when
        viewModel.getLoadMoreSearchedLocation("", "2")

        // then
        assertEquals(
                mockThrowable,
                (viewModel.dealsLoadMoreSearchedLocationResponse.value as Fail).throwable
        )
    }

    @Test
    fun getLoadMoreSearchedLocation_fetchSuccess_searchLocationShouldBeSuccess() {
        // given
        coEvery {
            searchLocationUseCase.getSearchedLocation(any(), any(), any(), any())
        } coAnswers {
            firstArg<(LocationData) -> Unit>().invoke(mockEventLocationSearch)
        }

        // when
        viewModel.getLoadMoreSearchedLocation("", "2")

        // then
        val result = viewModel.dealsLoadMoreSearchedLocationResponse.value
        assert(result is Success)
        assertEquals(mockEventLocationSearch.eventLocationSearch, (result as Success).data)
    }

    @Test
    fun getInitialPopularCities_fetchFailed_popularCitiesShouldBeFailed() {
        // given
        coEvery {
            popularCitiesUseCase.getPopularCities(any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        // when
        viewModel.getInitialPopularCities()

        // then
        assertEquals(mockThrowable, (viewModel.dealsPopularCitiesResponse.value as Fail).throwable)
    }

    @Test
    fun getInitialPopularCities_fetchSuccess_popularCitiesShouldBeSuccess() {
        // given
        coEvery {
            popularCitiesUseCase.getPopularCities(any(), any())
        } coAnswers {
            firstArg<(LocationData) -> Unit>().invoke(mockEventLocationSearch)
        }

        // when
        viewModel.getInitialPopularCities()

        // then
        val result = viewModel.dealsPopularCitiesResponse.value
        assert(result is Success)
        assertEquals(mockEventLocationSearch.eventLocationSearch, (result as Success).data)
    }

    @Test
    fun getInitialPopularLocation_fetchFailed_popularLocationShouldBeFailed() {
        // given
        coEvery {
            popularLocationUseCase.getPopularLocations(any(), any(), any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        // when
        viewModel.getInitialPopularLocation("")

        // then
        assertEquals(mockThrowable, (viewModel.dealsPopularLocationResponse.value as Fail).throwable)
    }

    @Test
    fun getInitialPopularLocation_fetchSuccess_popularLocationShouldBeSuccess() {
        // given
        coEvery {
            popularLocationUseCase.getPopularLocations(any(), any(), any(), any())
        } coAnswers {
            firstArg<(LocationData) -> Unit>().invoke(mockEventLocationSearch)
        }

        // when
        viewModel.getInitialPopularLocation("")

        // then
        val result = viewModel.dealsPopularLocationResponse.value
        assert(result is Success)
        assertEquals(mockEventLocationSearch.eventLocationSearch, (result as Success).data)
    }

    @Test
    fun getLoadMoreDataLocation_fetchFailed_loadMoreLocationShouldBeFailed() {
        // given
        coEvery {
            popularLocationUseCase.getPopularLocations(any(), any(), any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        // when
        viewModel.getLoadMoreDataLocation("", "2")

        // then
        assertEquals(
                mockThrowable,
                (viewModel.dealsLoadMorePopularLocationResponse.value as Fail).throwable
        )
    }

    @Test
    fun getLoadMoreDataLocation_fetchSuccess_loadMoreLocationShouldBeSuccess() {
        // given
        coEvery {
            popularLocationUseCase.getPopularLocations(any(), any(), any(), any())
        } coAnswers {
            firstArg<(LocationData) -> Unit>().invoke(mockEventLocationSearch)
        }

        // when
        viewModel.getLoadMoreDataLocation("", "2")

        // then
        val result = viewModel.dealsLoadMorePopularLocationResponse.value
        assert(result is Success)
        assertEquals(mockEventLocationSearch.eventLocationSearch,(result as Success).data)
    }

    @Test
    fun getInitialDataLandmarkLocation_fetchFailed_landmarkLocationShouldBeFailed() {
        // given
        coEvery {
            landmarkLocationUseCase.getLandmarkLocation(any(), any(), any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        // when
        viewModel.getInitialDataLandmarkLocation("")

        // then
        assertEquals(
                mockThrowable,
                (viewModel.dealsDataLandmarkLocationResponse.value as Fail).throwable
        )
    }

    @Test
    fun getInitialDataLandmarkLocation_fetchSuccess_landmarkLocationShouldBeSuccess() {
        // given
        coEvery {
            landmarkLocationUseCase.getLandmarkLocation(any(), any(), any(), any())
        } coAnswers {
            firstArg<(LocationData) -> Unit>().invoke(mockEventLocationSearch)
        }

        // when
        viewModel.getInitialDataLandmarkLocation("")

        // then
        val result = viewModel.dealsDataLandmarkLocationResponse.value
        assert(result is Success)
        assertEquals(mockEventLocationSearch.eventLocationSearch, (result as Success).data)
    }

    @Test
    fun getLoadMoreDataLandmarkLocation_fetchFailed_loadMoreLandmarkLocationShouldBeFailed() {
        // given
        coEvery {
            landmarkLocationUseCase.getLandmarkLocation(any(), any(), any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        // when
        viewModel.getLoadMoreDataLandmarkLocation("", "2")

        // then
        assertEquals(
                mockThrowable,
                (viewModel.dealsLoadMoreDataLandmarkLocationResponse.value as Fail).throwable
        )
    }

    @Test
    fun getLoadMoreDataLandmarkLocation_fetchSuccess_loadMoreLandmarkLocationShouldBeSuccess() {
        // given
        coEvery {
            landmarkLocationUseCase.getLandmarkLocation(any(), any(), any(), any())
        } coAnswers {
            firstArg<(LocationData) -> Unit>().invoke(mockEventLocationSearch)
        }

        // when
        viewModel.getLoadMoreDataLandmarkLocation("", "2")

        // then
        val result = viewModel.dealsLoadMoreDataLandmarkLocationResponse.value
        assert(result is Success)
        assertEquals(mockEventLocationSearch.eventLocationSearch, (result as Success).data)
    }
}