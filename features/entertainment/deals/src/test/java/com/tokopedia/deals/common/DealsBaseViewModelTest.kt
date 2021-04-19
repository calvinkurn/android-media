package com.tokopedia.deals.common

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.deals.common.domain.GetNearestLocationUseCase
import com.tokopedia.deals.common.ui.viewmodel.DealsBaseViewModel
import com.tokopedia.deals.common.utils.DealsLocationUtils
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.deals.location_picker.model.response.EventLocationSearch
import com.tokopedia.deals.location_picker.model.response.Location
import com.tokopedia.deals.location_picker.model.response.LocationData
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DealsBaseViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val dispatcher = CoroutineTestDispatchersProvider

    private val useCase: GetNearestLocationUseCase = mockk()
    private lateinit var viewModel: DealsBaseViewModel

    @Before
    fun setup() {
        viewModel = DealsBaseViewModel(dispatcher, useCase)
    }

    @Test
    fun getCurrentLocation_fetchFailed_locationShouldBeDefault() {
        // given
        coEvery { useCase.useParams(any()) } returns mockk()
        coEvery { useCase.executeOnBackground() } coAnswers { throw Exception("Fetch failed") }

        // when
        viewModel.getCurrentLocation("0,0")

        // then
        val location = viewModel.observableCurrentLocation.value as Location
        assert(location == DEFAULT_LOCATION)
    }

    @Test
    fun getCurrentLocation_fetchSuccessButEmptyData_locationShouldBeUnchanged() {
        // given
        coEvery { useCase.useParams(any()) } returns mockk()
        coEvery { useCase.executeOnBackground() } returns LocationData(
            eventLocationSearch = EventLocationSearch(
                locations = emptyList()
            )
        )

        // when
        viewModel.getCurrentLocation("0,0")

        // then
        assert(viewModel.observableCurrentLocation.value == null)
    }

    @Test
    fun getCurrentLocation_fetchSuccessAndContainsData_locationShouldContainsData() {
        // given
        coEvery { useCase.useParams(any()) } returns mockk()
        coEvery { useCase.executeOnBackground() } returns LocationData(
            eventLocationSearch = EventLocationSearch(
                locations = listOf(DUMMY_LOCATION)
            )
        )

        // when
        viewModel.getCurrentLocation("6,0")

        // then
        val location = viewModel.observableCurrentLocation.value as Location
        assert(location == DUMMY_LOCATION)
    }

    @Test
    fun getCurrentLocation_locationData_locationContainsData(){
        //given
        val location = Location()

        //when
        viewModel.setCurrentLocation(location)

        //then
        assertEquals(location, viewModel.observableCurrentLocation.value)
    }

    companion object {
        val DEFAULT_LOCATION = Location(
            id = DealsLocationUtils.DEFAULT_LOCATION_ID,
            cityId = DealsLocationUtils.DEFAULT_LOCATION_ID,
            name = DealsLocationUtils.DEFAULT_LOCATION_NAME,
            cityName = DealsLocationUtils.DEFAULT_LOCATION_NAME,
            coordinates = DealsLocationUtils.DEFAULT_LOCATION_COORDINATES
        )

        val DUMMY_LOCATION = Location(
            id = 1001,
            cityId = 1002,
            name = "Dummy Street",
            cityName = "Dummy City",
            coordinates = "103,104"
        )
    }

}