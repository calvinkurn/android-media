package com.tokopedia.flight.searchV2.data.db

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.tokopedia.flight.search.view.model.filter.RefundableEnum
import com.tokopedia.flight.searchV2.data.db.util.createFlightJourneyTable
import com.tokopedia.flight.searchV2.data.db.util.createRoutes
import com.tokopedia.flight.searchV2.presentation.model.filter.FlightFilterModel
import com.tokopedia.flight.searchV2.presentation.model.filter.TransitEnum
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
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
        val routes2 = createRoutes("2")
        flightSearchSingleDataDbSource.insert(JourneyAndRoutes(flightJourneyTable2, routes2))

        val flightFilterModel = FlightFilterModel()
        flightFilterModel.isBestPairing = true

        val testSubscriber = TestSubscriber<List<JourneyAndRoutes>>()

        flightSearchSingleDataDbSource.getFilteredJourneys(flightFilterModel)
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

        flightSearchSingleDataDbSource.getFilteredJourneys(flightFilterModel)
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
        flightFilterModel.refundableTypeList = arrayListOf(com.tokopedia.flight.searchV2.presentation.model.filter.RefundableEnum.REFUNDABLE)

        val testSubscriber = TestSubscriber<List<JourneyAndRoutes>>()

        flightSearchSingleDataDbSource.getFilteredJourneys(flightFilterModel)
                .subscribe(testSubscriber)

        assertThat(testSubscriber.onNextEvents[0].size, `is`(2))
        assertThat(testSubscriber.onNextEvents[0][0].flightJourneyTable.id, `is`("2"))
        assertThat(testSubscriber.onNextEvents[0][1].flightJourneyTable.id, `is`("3"))
        assertThat(testSubscriber.onNextEvents[0][0].flightJourneyTable.isRefundable, `is`(RefundableEnum.REFUNDABLE))
        assertThat(testSubscriber.onNextEvents[0][1].flightJourneyTable.isRefundable, `is`(RefundableEnum.REFUNDABLE))
    }

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

        flightSearchSingleDataDbSource.getFilteredJourneys(flightFilterModel)
                .subscribe(testSubscriber)

        assertThat(testSubscriber.onNextEvents[0].size, `is`(2))
        assertThat(testSubscriber.onNextEvents[0][0].flightJourneyTable.id, `is`("2"))
        assertThat(testSubscriber.onNextEvents[0][1].flightJourneyTable.id, `is`("3"))
        assertThat(testSubscriber.onNextEvents[0][0].flightJourneyTable.totalTransit, `is`(2))
        assertThat(testSubscriber.onNextEvents[0][1].flightJourneyTable.totalTransit, `is`(2))
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        flightSearchRoomDb.close()
    }

}