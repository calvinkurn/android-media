package com.tokopedia.flight.search.data.repository

import com.tokopedia.flight.search.data.api.combined.FlightSearchCombinedDataApiSource
import com.tokopedia.flight.search.data.api.single.FlightSearchDataCloudSource
import com.tokopedia.flight.search.data.api.single.response.Meta
import com.tokopedia.flight.search.data.db.*
import com.tokopedia.flight.search.data.repository.mapper.FlightSearchMapper
import com.tokopedia.flight.search.data.repository.util.createCombine
import com.tokopedia.flight.search.data.repository.util.createFlightDataResponse
import com.tokopedia.flight.search.data.repository.util.createFlightListSearchDataResponse
import com.tokopedia.flight.search.presentation.model.FlightRouteModel
import com.tokopedia.flight.search.presentation.model.FlightSearchCombinedApiRequestModel
import com.tokopedia.usecase.RequestParams
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Matchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.runners.MockitoJUnitRunner
import rx.Observable
import rx.observers.TestSubscriber

/**
 * Created by Rizky on 09/10/18.
 */
@RunWith(MockitoJUnitRunner::class)
class FlightSearchRepositoryTest {

    private lateinit var flightSearchRepository: FlightSearchRepository

    @Mock
    private lateinit var flightSearchCombinedDataApiSource: FlightSearchCombinedDataApiSource

    @Mock
    private lateinit var flightSearchCombinedDataDbSource: FlightSearchCombinedDataDbSource

    @Mock
    private lateinit var flightSearchSingleDataDbSource: FlightSearchSingleDataDbSource

    @Mock
    private lateinit var flightSearchDataCloudSource: FlightSearchDataCloudSource

    @Before
    fun setup() {
        flightSearchRepository = FlightSearchRepository(
                flightSearchCombinedDataApiSource,
                flightSearchDataCloudSource,
                flightSearchCombinedDataDbSource,
                flightSearchSingleDataDbSource,
                FlightSearchMapper())
    }

    @Test
    fun `get search single`() {
        val journeyAndRoutes = JourneyAndRoutes()
        journeyAndRoutes.flightJourneyTable = FlightJourneyTable()
        journeyAndRoutes.routes = arrayListOf()

        val flightDataResponse = createFlightDataResponse("1")

        `when`(flightSearchDataCloudSource.getData(Mockito.any())).thenReturn(Observable.just(flightDataResponse))

        val testSubscriber = TestSubscriber<Meta>()

        val params = RequestParams.create()
        flightSearchRepository.getSearchSingle(params.parameters, false)
                .subscribe(testSubscriber)

        testSubscriber.assertCompleted()
        testSubscriber.assertNoErrors()

        verify(flightSearchSingleDataDbSource, times(1))
                .insertList(Matchers.anyListOf(JourneyAndRoutes::class.java))
    }

    @Test
    fun `get search onward combined`() {
        val flightDataResponse = createFlightDataResponse("1")

        `when`(flightSearchDataCloudSource.getData(Mockito.any()))
                .thenReturn(Observable.just(flightDataResponse))

        val flightComboTableList = arrayListOf<FlightComboTable>()

        `when`(flightSearchCombinedDataDbSource.getSearchOnwardCombined("1"))
                .thenReturn(Observable.just(flightComboTableList))

        val testSubscriber = TestSubscriber<Meta>()

        val params = RequestParams.create()
        flightSearchRepository.getSearchSingleCombined(params.parameters, false)
                .subscribe(testSubscriber)

        verify(flightSearchSingleDataDbSource, times(1))
                .insertList(Matchers.anyListOf(JourneyAndRoutes::class.java))
    }

    // onwardJourneyId = 1
    // returnJourneys  = 2, 3
    // combo = {1, 2}
    @Test
    fun `get search return combined`() {
        val flightDataResponse = createFlightListSearchDataResponse("2", "3")

        `when`(flightSearchDataCloudSource.getData(Mockito.any()))
                .thenReturn(Observable.just(flightDataResponse))

        val flightComboTableList = arrayListOf<FlightComboTable>()
        val flightComboTable = FlightComboTable("1",
                "Rp 250.000","childPrice","infantPrice",
                250000,0,0,
                "2",
                "Rp 250.000","returnChildPrice","returnInfantPrice",
                250000,0,0,
                "comboId", true)
        flightComboTableList.add(flightComboTable)

        `when`(flightSearchCombinedDataDbSource.getSearchReturnCombined("2"))
                .thenReturn(Observable.just(flightComboTableList))

        `when`(flightSearchCombinedDataDbSource.getSearchReturnCombined("3"))
                .thenReturn(Observable.just(null))

        val testSubscriber = TestSubscriber<Meta>()

        val params = RequestParams.create()
        flightSearchRepository.getSearchCombinedReturn(params.parameters, "1", true)
                .subscribe(testSubscriber)

        verify(flightSearchSingleDataDbSource, times(1))
                .insertList(Matchers.anyListOf(JourneyAndRoutes::class.java))
    }

    @Test
    fun `get search combined`() {
        val flightDataResponse = createCombine()

        `when`(flightSearchCombinedDataApiSource.getData(Mockito.any(FlightSearchCombinedApiRequestModel::class.java)))
                .thenReturn(Observable.just(flightDataResponse))

        val testSubscriber = TestSubscriber<Meta>()

        val routes = listOf<FlightRouteModel>()
        flightSearchRepository.getSearchCombined(FlightSearchCombinedApiRequestModel(routes, 0, 0, 0, 0))
                .subscribe(testSubscriber)

        verify(flightSearchCombinedDataDbSource, times(1))
                .insert(Matchers.anyListOf(FlightComboTable::class.java))
    }

}