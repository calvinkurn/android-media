package com.tokopedia.deals.location_picker

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.deals.common.model.response.SearchData
import com.tokopedia.deals.common.utils.DealsTestDispatcherProvider
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
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DealsLocationViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = DealsTestDispatcherProvider()
    private val searchLocationUseCase: DealsSearchLocationUseCase = mockk()
    private val popularCitiesUseCase: DealsPopularCitiesUseCase = mockk()
    private val popularLocationUseCase: DealsPopularLocationUseCase = mockk()
    private val landmarkLocationUseCase: DealsLandmarkLocationUseCase = mockk()

    private lateinit var viewModel: DealsLocationViewModel
    private val mockThrowable = Throwable("Fetch failed")

    @Before
    fun setup() {
        viewModel = DealsLocationViewModel(
            searchLocationUseCase,
            popularCitiesUseCase,
            popularLocationUseCase,
            landmarkLocationUseCase,
            dispatcher
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
        assert((viewModel.dealsSearchedLocationResponse.value as Fail).throwable == mockThrowable)
    }

    @Test
    fun getSearchedLocation_fetchSuccess_searchLocationShouldBeSuccess() {
        val mockData = LocationData()

        // given
        coEvery {
            searchLocationUseCase.getSearchedLocation(any(), any(), any(), any())
        } coAnswers {
            firstArg<(LocationData) -> Unit>().invoke(mockData)
        }

        // when
        viewModel.getSearchedLocation("")

        // then
        assert(viewModel.dealsSearchedLocationResponse.value is Success)
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
        assert((viewModel.dealsLoadMoreSearchedLocationResponse.value as Fail).throwable == mockThrowable)
    }

    @Test
    fun getLoadMoreSearchedLocation_fetchSuccess_searchLocationShouldBeSuccess() {
        val mockData = LocationData()

        // given
        coEvery {
            searchLocationUseCase.getSearchedLocation(any(), any(), any(), any())
        } coAnswers {
            firstArg<(LocationData) -> Unit>().invoke(mockData)
        }

        // when
        viewModel.getLoadMoreSearchedLocation("", "2")

        // then
        assert(viewModel.dealsLoadMoreSearchedLocationResponse.value is Success)
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
        assert((viewModel.dealsPopularCitiesResponse.value as Fail).throwable == mockThrowable)
    }

    @Test
    fun getInitialPopularCities_fetchSuccess_popularCitiesShouldBeSuccess() {
        val mockData = LocationData()

        // given
        coEvery {
            popularCitiesUseCase.getPopularCities(any(), any())
        } coAnswers {
            firstArg<(LocationData) -> Unit>().invoke(mockData)
        }

        // when
        viewModel.getInitialPopularCities()

        // then
        assert(viewModel.dealsPopularCitiesResponse.value is Success)
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
        assert((viewModel.dealsPopularLocationResponse.value as Fail).throwable == mockThrowable)
    }

    @Test
    fun getInitialPopularLocation_fetchSuccess_popularLocationShouldBeSuccess() {
        val mockData = LocationData()

        // given
        coEvery {
            popularLocationUseCase.getPopularLocations(any(), any(), any(), any())
        } coAnswers {
            firstArg<(LocationData) -> Unit>().invoke(mockData)
        }

        // when
        viewModel.getInitialPopularLocation("")

        // then
        assert(viewModel.dealsPopularLocationResponse.value is Success)
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
        assert((viewModel.dealsLoadMorePopularLocationResponse.value as Fail).throwable == mockThrowable)
    }

    @Test
    fun getLoadMoreDataLocation_fetchSuccess_loadMoreLocationShouldBeSuccess() {
        val mockData = LocationData()

        // given
        coEvery {
            popularLocationUseCase.getPopularLocations(any(), any(), any(), any())
        } coAnswers {
            firstArg<(LocationData) -> Unit>().invoke(mockData)
        }

        // when
        viewModel.getLoadMoreDataLocation("", "2")

        // then
        assert(viewModel.dealsLoadMorePopularLocationResponse.value is Success)
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
        assert((viewModel.dealsDataLandmarkLocationResponse.value as Fail).throwable == mockThrowable)
    }

    @Test
    fun getInitialDataLandmarkLocation_fetchSuccess_landmarkLocationShouldBeSuccess() {
        val mockData = LocationData()

        // given
        coEvery {
            landmarkLocationUseCase.getLandmarkLocation(any(), any(), any(), any())
        } coAnswers {
            firstArg<(LocationData) -> Unit>().invoke(mockData)
        }

        // when
        viewModel.getInitialDataLandmarkLocation("")

        // then
        assert(viewModel.dealsDataLandmarkLocationResponse.value is Success)
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
        assert((viewModel.dealsLoadMoreDataLandmarkLocationResponse.value as Fail).throwable == mockThrowable)
    }

    @Test
    fun getLoadMoreDataLandmarkLocation_fetchSuccess_loadMoreLandmarkLocationShouldBeSuccess() {
        val mockData = LocationData()

        // given
        coEvery {
            landmarkLocationUseCase.getLandmarkLocation(any(), any(), any(), any())
        } coAnswers {
            firstArg<(LocationData) -> Unit>().invoke(mockData)
        }

        // when
        viewModel.getLoadMoreDataLandmarkLocation("", "2")

        // then
        assert(viewModel.dealsLoadMoreDataLandmarkLocationResponse.value is Success)
    }
}