package com.tokopedia.flight.searchV2.data.repository

import com.tokopedia.flight.airline.data.db.FlightAirlineDataListDBSource
import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB
import com.tokopedia.flight.airport.data.source.db.FlightAirportDataListDBSource
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB
import com.tokopedia.flight.search.data.cloud.FlightSearchDataCloudSource
import com.tokopedia.flight.search.data.cloud.model.response.Meta
import com.tokopedia.flight.searchV2.data.api.combined.FlightSearchCombinedDataApiSource
import com.tokopedia.flight.searchV2.data.db.*
import com.tokopedia.flight.searchV2.data.repository.util.createFlightDataResponse
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

    @Mock
    private lateinit var flightAirportDataListDBSource: FlightAirportDataListDBSource

    @Mock
    private lateinit var flightAirlineDataListDBSource: FlightAirlineDataListDBSource

    @Before
    fun setup() {
        flightSearchRepository = FlightSearchRepository(flightSearchCombinedDataApiSource, flightSearchDataCloudSource,
                flightSearchCombinedDataDbSource,
                flightSearchSingleDataDbSource, flightAirportDataListDBSource, flightAirlineDataListDBSource)
    }

    @Test
    fun `get search single`() {
        val journeyAndRoutes = JourneyAndRoutes()
        journeyAndRoutes.flightJourneyTable = FlightJourneyTable()
        journeyAndRoutes.routes = arrayListOf()
        val journeyAndRoutesList = listOf<JourneyAndRoutes>()
        val observable = Observable.create<List<JourneyAndRoutes>> {
            it.onNext(journeyAndRoutesList)
        }

        `when`(flightSearchSingleDataDbSource.findAllJourneys()).thenReturn(observable)

        val flightDataResponse = createFlightDataResponse()

        `when`(flightSearchDataCloudSource.getData(Mockito.any())).thenReturn(Observable.just(flightDataResponse))

        `when`(flightAirportDataListDBSource.getAirport(Matchers.anyString())).thenReturn(
                Observable.just(FlightAirportDB())
        )

        val flightAirlineDB = FlightAirlineDB()
        flightAirlineDB.id = "JT"
        flightAirlineDB.logo = "Logo Lion Air"

        `when`(flightAirlineDataListDBSource.getAirline(Matchers.anyString())).thenReturn(
                Observable.just(flightAirlineDB)
        )

        val testSubscriber = TestSubscriber<Meta>()

        val params = RequestParams.create()
        flightSearchRepository.getSearchSingle(params.parameters)
                .subscribe(testSubscriber)

        testSubscriber.assertCompleted()
        testSubscriber.assertNoErrors()

        verify(flightSearchSingleDataDbSource, times(1))
                .insertList(Matchers.anyListOf(JourneyAndRoutes::class.java))
    }

    @Test
    fun `get search single combined`() {
        val flightDataResponse = createFlightDataResponse()

        `when`(flightSearchDataCloudSource.getData(Mockito.any()))
                .thenReturn(Observable.just(flightDataResponse))

        val flightComboTableList = arrayListOf<FlightComboTable>()

        `when`(flightSearchCombinedDataDbSource.getSearchCombined("1"))
                .thenReturn(Observable.just(flightComboTableList))

        val testSubscriber = TestSubscriber<Meta>()

        val params = RequestParams.create()
        flightSearchRepository.getSearchSingleCombined(params.parameters)
                .subscribe(testSubscriber)

        verify(flightSearchSingleDataDbSource, times(1))
                .insertList(Matchers.anyListOf(JourneyAndRoutes::class.java))
    }

}