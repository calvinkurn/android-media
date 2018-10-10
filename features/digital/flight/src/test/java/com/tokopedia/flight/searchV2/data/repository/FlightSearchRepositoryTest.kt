package com.tokopedia.flight.searchV2.data.repository

import com.tokopedia.flight.airline.data.db.FlightAirlineDataListDBSource
import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB
import com.tokopedia.flight.airport.data.source.db.FlightAirportDataListDBSource
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB
import com.tokopedia.flight.search.data.cloud.FlightSearchDataCloudSource
import com.tokopedia.flight.search.data.cloud.model.response.*
import com.tokopedia.flight.searchV2.data.api.combined.FlightSearchCombinedDataApiSource
import com.tokopedia.flight.searchV2.data.db.FlightJourneyTable
import com.tokopedia.flight.searchV2.data.db.FlightSearchCombinedDataDbSource
import com.tokopedia.flight.searchV2.data.db.FlightSearchSingleDataDbSource
import com.tokopedia.flight.searchV2.data.db.JourneyAndRoutes
import com.tokopedia.usecase.RequestParams
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Matchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyString
import org.mockito.runners.MockitoJUnitRunner
import rx.Observable
import rx.observers.TestSubscriber
import rx.schedulers.TestScheduler
import java.util.*

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
    fun `when movie`() {
        val journeyAndRoutes = JourneyAndRoutes()
        journeyAndRoutes.flightJourneyTable = FlightJourneyTable()
        journeyAndRoutes.routes = arrayListOf()
        val journeyAndRoutesList = listOf<JourneyAndRoutes>()
        val observable = Observable.create<List<JourneyAndRoutes>> {
            it.onNext(journeyAndRoutesList)
        }

        `when`(flightSearchSingleDataDbSource.findAllJourneys()).thenReturn(observable)

        val flightDataResponse = FlightDataResponse<List<FlightSearchData>>()
        val flightSearchDataList = ArrayList<FlightSearchData>()
        val flightSearchData = FlightSearchData()
        var attributes = Attributes()
        val routes = arrayListOf<Route>()
        val route = Route("JT", "CGK", "123", "DPS",
                "234", "2j", "layover", null, "JT123",
                true, null, 1, null, "Lion Air",
                "logo", "Cengkareng", "Jakarta",
                "Denpasar", "Denpasar")
        routes.add(route)
        attributes.routes = routes
        attributes.departureAirport = "CGK"
        attributes.departureTime = "16:00"
        attributes.departureTimeInt = 123
        attributes.arrivalAirport = "DPS"
        attributes.arrivalTime = "18:00"
        attributes.arrivalTimeInt = 234
        attributes.addDayArrival = 0
        attributes.aid = ""
        attributes.duration = "2j"
        attributes.durationMinute = 12345
        attributes.beforeTotal = "Rp 550.000"
        attributes.total = "Rp 500.000"
        attributes.totalNumeric = 500000
        attributes.term = "CGKDPS"
        attributes.totalStop = 1
        attributes.totalTransit = 0
        val fare = Fare()
        fare.adult = "Rp 500.000"
        fare.adultNumeric = 500000
        fare.child = "Rp 0"
        fare.childNumeric = 0
        fare.infant = "Rp 0"
        fare.infantNumeric = 0
        attributes.fare = fare
        flightSearchData.id = "1"
        flightSearchData.attributes = attributes
        flightSearchDataList.add(flightSearchData)
        flightDataResponse.data = flightSearchDataList
        flightDataResponse.meta = Meta()

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

//        testSubscriber.assertComplete()
//        testSubscriber.assertNoErrors()
//        testObserver.assertValueCount(1)

//        verify(flightSearchSingleDataDbSource, times(1)).insert(JourneyAndRoutes())
    }

}