package com.tokopedia.flight.searchV2.data.repository

import com.tokopedia.flight.airline.data.db.FlightAirlineDataListDBSource
import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB
import com.tokopedia.flight.airport.data.source.db.FlightAirportDataListDBSource
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB
import com.tokopedia.flight.search.data.cloud.FlightSearchDataCloudSource
import com.tokopedia.flight.search.data.cloud.model.response.Meta
import com.tokopedia.flight.searchV2.data.api.combined.FlightSearchCombinedDataApiSource
import com.tokopedia.flight.searchV2.data.db.*
import com.tokopedia.flight.searchV2.data.repository.mapper.FlightSearchMapper
import com.tokopedia.flight.searchV2.data.repository.util.createFlightDataResponse
import com.tokopedia.flight.searchV2.data.repository.util.createFlightListSearchDataResponse
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
        flightSearchRepository = FlightSearchRepository(
                flightSearchCombinedDataApiSource,
                flightSearchDataCloudSource,
                flightSearchCombinedDataDbSource,
                flightSearchSingleDataDbSource,
                flightAirportDataListDBSource,
                flightAirlineDataListDBSource,
                FlightSearchMapper())

        `when`(flightAirportDataListDBSource.getAirport(Matchers.anyString())).thenReturn(
                Observable.just(FlightAirportDB())
        )

        val flightAirlineDB = FlightAirlineDB()
        flightAirlineDB.id = "JT"
        flightAirlineDB.logo = "Logo Lion Air"

        `when`(flightAirlineDataListDBSource.getAirline(Matchers.anyString())).thenReturn(
                Observable.just(flightAirlineDB)
        )
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

    @Test
    fun `get search return combined`() {
        val flightDataResponse = createFlightListSearchDataResponse("2", "3")

        `when`(flightSearchDataCloudSource.getData(Mockito.any()))
                .thenReturn(Observable.just(flightDataResponse))

        val flightComboTableList = arrayListOf<FlightComboTable>()
        val flightComboTable = FlightComboTable("1", "2", "comboId", "adultPrice", "childPrice",
                "infantPrice", 0, 0, 0, true)
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

}