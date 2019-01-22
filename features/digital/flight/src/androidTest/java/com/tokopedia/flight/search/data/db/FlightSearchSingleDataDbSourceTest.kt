package com.tokopedia.flight.search.data.db

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.tokopedia.common.travel.constant.TravelSortOption
import com.tokopedia.flight.search.data.db.util.createFlightJourneyTable
import com.tokopedia.flight.search.data.db.util.createRoutes
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel
import com.tokopedia.flight.search.presentation.model.filter.RefundableEnum
import com.tokopedia.flight.search.presentation.model.filter.TransitEnum
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import rx.observers.TestSubscriber
import java.io.IOException

/**
 * Created by Rizky on 05/10/18.
 */
@RunWith(AndroidJUnit4::class)
class FlightSearchSingleDataDbSourceTest {

    private lateinit var flightSearchRoomDb: FlightSearchRoomDb

    private lateinit var flightJourneyDao: FlightJourneyDao
    private lateinit var flightRouteDao: FlightRouteDao

    private lateinit var flightSearchSingleDataDbSource: FlightSearchSingleDataDbSource

    @Before
    fun createDb() {
        MockitoAnnotations.initMocks(this)
        val context = InstrumentationRegistry.getTargetContext()
        flightSearchRoomDb = Room.inMemoryDatabaseBuilder(context, FlightSearchRoomDb::class.java).build()
        flightJourneyDao = flightSearchRoomDb.flightJourneyDao()
        flightRouteDao = flightSearchRoomDb.flightRouteDao()
        flightSearchSingleDataDbSource = FlightSearchSingleDataDbSource(flightJourneyDao, flightRouteDao)
    }

    @Test
    fun findAllJourneys_CheckJourneyIdAndSize_EqualsOne() {
        val flightJourneyTable = createFlightJourneyTable("1")
        val routes = createRoutes("1")
        flightSearchSingleDataDbSource.insert(JourneyAndRoutes(flightJourneyTable, routes))

        val testSubscriber = TestSubscriber<List<JourneyAndRoutes>>()

        flightSearchSingleDataDbSource.findAllJourneys().subscribe(testSubscriber)

        assertThat(testSubscriber.onNextEvents[0][0].flightJourneyTable.id, `is`("1"))
        assertThat(testSubscriber.onNextEvents[0][0].flightJourneyTable.id, not("45"))
        assertThat(testSubscriber.onNextEvents[0].size, `is`(1))
    }

    @Test
    fun findAllJourneys_CheckRoutesSize_EqualsOne() {
        val flightJourneyTable = createFlightJourneyTable("1")
        val routes = createRoutes("1")
        flightSearchSingleDataDbSource.insert(JourneyAndRoutes(flightJourneyTable, routes))

        val testSubscriber = TestSubscriber<List<JourneyAndRoutes>>()

        flightSearchSingleDataDbSource.findAllJourneys().subscribe(testSubscriber)

        assertThat(testSubscriber.onNextEvents[0][0].routes.size, `is`(1))
    }

    @Test
    fun getJourneyById_CheckJourneyId_EQualsOne() {
        val flightJourneyTable = createFlightJourneyTable("1")
        val routes = createRoutes("1")
        flightSearchSingleDataDbSource.insert(JourneyAndRoutes(flightJourneyTable, routes))

        val testSubscriber = TestSubscriber<JourneyAndRoutes>()

        flightSearchSingleDataDbSource.getJourneyById("1").subscribe(testSubscriber)

        assertThat(testSubscriber.onNextEvents[0].flightJourneyTable.id, not("10"))
        assertThat(testSubscriber.onNextEvents[0].flightJourneyTable.id, `is`("1"))
    }

    @Test
    fun getFilterJourneys_findBestPairing_showBestPairingJourneys() {
        val flightJourneyTable1 = createFlightJourneyTable("1")
        flightJourneyTable1.isBestPairing = false
        val routes = createRoutes("1")
        flightSearchSingleDataDbSource.insert(JourneyAndRoutes(flightJourneyTable1, routes))

        val flightJourneyTable2 = createFlightJourneyTable("2")
        flightJourneyTable2.isBestPairing = true
        flightJourneyTable2.isReturn = true
        val routes2 = createRoutes("2")
        flightSearchSingleDataDbSource.insert(JourneyAndRoutes(flightJourneyTable2, routes2))

        val flightFilterModel = FlightFilterModel()
        flightFilterModel.isBestPairing = true
        flightFilterModel.isReturn = true

        val testSubscriber = TestSubscriber<List<JourneyAndRoutes>>()

        flightSearchSingleDataDbSource.getFilteredJourneys(flightFilterModel, TravelSortOption.NO_PREFERENCE)
                .subscribe(testSubscriber)

        assertThat(testSubscriber.onNextEvents[0].size, `is`(1))
        assertThat(testSubscriber.onNextEvents[0][0].flightJourneyTable.id, `is`("2"))
        assertThat(testSubscriber.onNextEvents[0][0].flightJourneyTable.isBestPairing, `is`(true))
    }

    @Test
    fun getFilterJourneys_findSpecialPrice_showSpecialPriceJourneys() {
        val flightJourneyTable1 = createFlightJourneyTable("1")
        flightJourneyTable1.isSpecialPrice = false
        val routes = createRoutes("1")
        flightSearchSingleDataDbSource.insert(JourneyAndRoutes(flightJourneyTable1, routes))

        val flightJourneyTable2 = createFlightJourneyTable("2")
        flightJourneyTable2.isSpecialPrice = true
        val routes2 = createRoutes("2")
        flightSearchSingleDataDbSource.insert(JourneyAndRoutes(flightJourneyTable2, routes2))

        val flightJourneyTable3 = createFlightJourneyTable("3")
        flightJourneyTable3.isSpecialPrice = true
        val routes3 = createRoutes("3")
        flightSearchSingleDataDbSource.insert(JourneyAndRoutes(flightJourneyTable3, routes3))

        val flightFilterModel = FlightFilterModel()
        flightFilterModel.isSpecialPrice = true

        val testSubscriber = TestSubscriber<List<JourneyAndRoutes>>()

        flightSearchSingleDataDbSource.getFilteredJourneys(flightFilterModel, TravelSortOption.NO_PREFERENCE)
                .subscribe(testSubscriber)

        assertThat(testSubscriber.onNextEvents[0].size, `is`(2))
        assertThat(testSubscriber.onNextEvents[0][0].flightJourneyTable.id, `is`("2"))
        assertThat(testSubscriber.onNextEvents[0][1].flightJourneyTable.id, `is`("3"))
        assertThat(testSubscriber.onNextEvents[0][0].flightJourneyTable.isSpecialPrice, `is`(true))
        assertThat(testSubscriber.onNextEvents[0][1].flightJourneyTable.isSpecialPrice, `is`(true))
    }

    @Test
    fun getFilterJourneys_findRefundable_showRefundableJourneys() {
        val flightJourneyTable1 = createFlightJourneyTable("1")
        flightJourneyTable1.isRefundable = RefundableEnum.NOT_REFUNDABLE
        val routes = createRoutes("1")
        flightSearchSingleDataDbSource.insert(JourneyAndRoutes(flightJourneyTable1, routes))

        val flightJourneyTable2 = createFlightJourneyTable("2")
        flightJourneyTable2.isRefundable = RefundableEnum.REFUNDABLE
        val routes2 = createRoutes("2")
        flightSearchSingleDataDbSource.insert(JourneyAndRoutes(flightJourneyTable2, routes2))

        val flightJourneyTable3 = createFlightJourneyTable("3")
        flightJourneyTable3.isRefundable = RefundableEnum.REFUNDABLE
        val routes3 = createRoutes("3")
        flightSearchSingleDataDbSource.insert(JourneyAndRoutes(flightJourneyTable3, routes3))

        val flightFilterModel = FlightFilterModel()
        flightFilterModel.refundableTypeList = arrayListOf(RefundableEnum.REFUNDABLE)

        val testSubscriber = TestSubscriber<List<JourneyAndRoutes>>()

        flightSearchSingleDataDbSource.getFilteredJourneys(flightFilterModel, TravelSortOption.NO_PREFERENCE)
                .subscribe(testSubscriber)

        assertThat(testSubscriber.onNextEvents[0].size, `is`(2))
        assertThat(testSubscriber.onNextEvents[0][0].flightJourneyTable.id, `is`("2"))
        assertThat(testSubscriber.onNextEvents[0][1].flightJourneyTable.id, `is`("3"))
        assertThat(testSubscriber.onNextEvents[0][0].flightJourneyTable.isRefundable, `is`(RefundableEnum.REFUNDABLE))
        assertThat(testSubscriber.onNextEvents[0][1].flightJourneyTable.isRefundable, `is`(RefundableEnum.REFUNDABLE))
    }


    /**
     *  // condition
     *  journey 1 total transit = 0
     *  journey 2 total transit = 2
     *  journey 3 total transit = 2
     *  // action
     *  Filter journeys by number of transit = 2
     *  // expected result
     *  size of journeys = 2
     *  item[0] = journey 2
     *  item[1] = journey 3
     *  item[0].totalTransit = 2
     *  item[1].totalTransit = 2
     **/
    @Test
    fun getFilterJourneys_findTransit_showTransitJourneys() {
        val flightJourneyTable1 = createFlightJourneyTable("1")
        flightJourneyTable1.totalTransit = 0
        val routes = createRoutes("1")
        flightSearchSingleDataDbSource.insert(JourneyAndRoutes(flightJourneyTable1, routes))

        val flightJourneyTable2 = createFlightJourneyTable("2")
        flightJourneyTable2.totalTransit = 2
        val routes2 = createRoutes("2")
        flightSearchSingleDataDbSource.insert(JourneyAndRoutes(flightJourneyTable2, routes2))

        val flightJourneyTable3 = createFlightJourneyTable("3")
        flightJourneyTable3.totalTransit = 2
        val routes3 = createRoutes("3")
        flightSearchSingleDataDbSource.insert(JourneyAndRoutes(flightJourneyTable3, routes3))

        val flightFilterModel = FlightFilterModel()
        flightFilterModel.transitTypeList = arrayListOf(TransitEnum.TWO)

        val testSubscriber = TestSubscriber<List<JourneyAndRoutes>>()

        flightSearchSingleDataDbSource.getFilteredJourneys(flightFilterModel, TravelSortOption.NO_PREFERENCE)
                .subscribe(testSubscriber)

        assertThat(testSubscriber.onNextEvents[0].size, `is`(2))
        assertThat(testSubscriber.onNextEvents[0][0].flightJourneyTable.id, `is`("2"))
        assertThat(testSubscriber.onNextEvents[0][1].flightJourneyTable.id, `is`("3"))
        assertThat(testSubscriber.onNextEvents[0][0].flightJourneyTable.totalTransit, `is`(2))
        assertThat(testSubscriber.onNextEvents[0][1].flightJourneyTable.totalTransit, `is`(2))
    }

    /**
     * // condition
     * journey 1 airline = JT
     * journey 2 airline = JT
     * journey 3 airline = JT
     * // action
     * Filter by airline = JT
     * // expected result
     * size of journeys = 3\
     * journey 1 airline = JT
     * journey 2 airline = JT
     * journey 3 airline = JT
     **/
    @Test
    fun getFilterJourneys_findAirline_showAirlineJourneys() {
        val flightJourneyTable1 = createFlightJourneyTable("1")
        val routes = createRoutes("1")
        flightSearchSingleDataDbSource.insert(JourneyAndRoutes(flightJourneyTable1, routes))

        val flightJourneyTable2 = createFlightJourneyTable("2")
        val routes2 = createRoutes("2")
        flightSearchSingleDataDbSource.insert(JourneyAndRoutes(flightJourneyTable2, routes2))

        val flightJourneyTable3 = createFlightJourneyTable("3")
        val routes3 = createRoutes("3")
        flightSearchSingleDataDbSource.insert(JourneyAndRoutes(flightJourneyTable3, routes3))

        val flightFilterModel = FlightFilterModel()
        flightFilterModel.airlineList = arrayListOf("JT")

        val testSubscriber = TestSubscriber<List<JourneyAndRoutes>>()

        flightSearchSingleDataDbSource.getFilteredJourneys(flightFilterModel, TravelSortOption.NO_PREFERENCE)
                .subscribe(testSubscriber)

        assertThat(testSubscriber.onNextEvents[0].size, `is`(3))
        assertThat(testSubscriber.onNextEvents[0][0].flightJourneyTable.id, `is`("1"))
        assertThat(testSubscriber.onNextEvents[0][1].flightJourneyTable.id, `is`("2"))
        assertThat(testSubscriber.onNextEvents[0][2].flightJourneyTable.id, `is`("3"))
        assertThat(testSubscriber.onNextEvents[0][0].routes[0].airline, `is`("JT"))
        assertThat(testSubscriber.onNextEvents[0][1].routes[0].airline, `is`("JT"))
        assertThat(testSubscriber.onNextEvents[0][2].routes[0].airline, `is`("JT"))
    }

    /**
     * // condition
     * journey 1 total transit = 0
     * journey 2 total transit = 2
     * journey 3 total transit = 2
     * // action
     * Get number of journeys filtered by number of transit = 2
     * // expected result
     * Return 2
     **/
    @Test
    fun getSearchCount_filterByNumberOfTransitEqualsToTwo_ReturnTwo() {
        val flightJourneyTable1 = createFlightJourneyTable("1")
        flightJourneyTable1.totalTransit = 0
        val routes = createRoutes("1")
        flightSearchSingleDataDbSource.insert(JourneyAndRoutes(flightJourneyTable1, routes))

        val flightJourneyTable2 = createFlightJourneyTable("2")
        flightJourneyTable2.totalTransit = 2
        val routes2 = createRoutes("2")
        flightSearchSingleDataDbSource.insert(JourneyAndRoutes(flightJourneyTable2, routes2))

        val flightJourneyTable3 = createFlightJourneyTable("3")
        flightJourneyTable3.totalTransit = 2
        val routes3 = createRoutes("3")
        flightSearchSingleDataDbSource.insert(JourneyAndRoutes(flightJourneyTable3, routes3))

        val flightFilterModel = FlightFilterModel()
        flightFilterModel.transitTypeList = arrayListOf(TransitEnum.TWO)

        val testSubscriber = TestSubscriber<Int>()

        flightSearchSingleDataDbSource.getSearchCount(flightFilterModel)
                .subscribe(testSubscriber)

        assertThat(testSubscriber.onNextEvents[0], `is`(2))
    }

    /**
     * // condition
     * journey 1 price = Rp 1.000.000
     * journey 2 price = Rp 1.500.000
     * journey 3 price = Rp   500.000
     * // action
     * FlightSortOption = CHEAPEST
     * // expected result
     * journey 3 price = Rp   500.000
     * journey 1 price = Rp 1.000.000
     * journey 2 price = Rp 1.500.000
     **/
    @Test
    fun getFilteredJourneys_sortByCheapestPrice_journeysSortedByCheapestPrice() {
        val flightJourneyTable1 = createFlightJourneyTable("1")
        flightJourneyTable1.sortPrice = "Rp 1.000.000"
        val routes = createRoutes("1")
        flightSearchSingleDataDbSource.insert(JourneyAndRoutes(flightJourneyTable1, routes))

        val flightJourneyTable2 = createFlightJourneyTable("2")
        flightJourneyTable2.sortPrice = "Rp 1.000.000"
        val routes2 = createRoutes("2")
        flightSearchSingleDataDbSource.insert(JourneyAndRoutes(flightJourneyTable2, routes2))

        val flightJourneyTable3 = createFlightJourneyTable("3")
        flightJourneyTable3.sortPrice = "Rp 500.000"
        val routes3 = createRoutes("3")
        flightSearchSingleDataDbSource.insert(JourneyAndRoutes(flightJourneyTable3, routes3))

        val flightFilterModel = FlightFilterModel()

        val testSubscriber = TestSubscriber<List<JourneyAndRoutes>>()

        flightSearchSingleDataDbSource.getFilteredJourneys(flightFilterModel, TravelSortOption.CHEAPEST)
                .subscribe(testSubscriber)

        assertThat(testSubscriber.onNextEvents[0].size, `is`(3))
        assertThat(testSubscriber.onNextEvents[0][0].flightJourneyTable.id, `is`("3"))
        assertThat(testSubscriber.onNextEvents[0][1].flightJourneyTable.id, `is`("1"))
        assertThat(testSubscriber.onNextEvents[0][2].flightJourneyTable.id, `is`("2"))
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        flightSearchRoomDb.close()
    }

}