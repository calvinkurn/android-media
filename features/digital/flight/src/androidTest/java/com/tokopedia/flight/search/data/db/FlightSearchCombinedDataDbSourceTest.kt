package com.tokopedia.flight.search.data.db

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.tokopedia.flight.search.data.db.util.createCombo
import com.tokopedia.flight.search.data.db.util.createFlightJourneyTable
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel
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
 * Created by Rizky on 07/10/18.
 */
@RunWith(AndroidJUnit4::class)
class FlightSearchCombinedDataDbSourceTest {

    private lateinit var flightSearchRoomDb: FlightSearchRoomDb

    private lateinit var flightJourneyDao: FlightJourneyDao
    private lateinit var flightComboDao: FlightComboDao

    private lateinit var flightSearchCombinedDataDbSource: FlightSearchCombinedDataDbSource

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getTargetContext()
        flightSearchRoomDb = Room.inMemoryDatabaseBuilder(context, FlightSearchRoomDb::class.java).build()
        flightJourneyDao = flightSearchRoomDb.flightJourneyDao()
        flightComboDao = flightSearchRoomDb.flightComboDao()
        flightSearchCombinedDataDbSource = FlightSearchCombinedDataDbSource(flightComboDao)
    }

    @Test
    fun getSearchCombined_findBestPairByOnwardJourneyId_returnJourneyIdEquals2() {
        val onwardJourney = createFlightJourneyTable("1")
        flightJourneyDao.insert(onwardJourney)
        val combo = createCombo("1", "2")
        flightSearchCombinedDataDbSource.insert(combo)

        val flightFilterModel = FlightFilterModel()
        flightFilterModel.isBestPairing = true

        val testSubscriber: TestSubscriber<List<FlightComboTable>> = TestSubscriber()

        flightSearchCombinedDataDbSource.getSearchReturnCombined("2").subscribe(testSubscriber)

        assertThat(testSubscriber.onNextEvents[0].size, `is`(1))
        assertThat(testSubscriber.onNextEvents[0][0].returnJourneyId, `is`("2"))
        assertThat(testSubscriber.onNextEvents[0][0].returnJourneyId, not("1"))
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        flightSearchRoomDb.close()
    }

}