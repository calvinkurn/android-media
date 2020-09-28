package com.tokopedia.flight.homepage.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.travel.utils.TravelTestDispatcherProvider
import com.tokopedia.flight.dummy.FARE_CALENDAR_DATA
import com.tokopedia.flight.homepage.presentation.model.FlightFareData
import com.tokopedia.flight.shouldBe
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type
import java.util.*

/**
 * @author by furqan on 23/06/2020
 */
class FlightFareCalendarViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcherProvider = TravelTestDispatcherProvider()
    private val gqlRepository: GraphqlRepository = mockk()

    private lateinit var viewModel: FlightFareCalendarViewModel

    @Before
    fun setUp() {
        viewModel = FlightFareCalendarViewModel(dispatcherProvider, gqlRepository)
    }

    @Test
    fun getFareFlightCalendar_failedToFetch_flightFareCalendarDataShouldBeEmpty() {
        // given
        coEvery { gqlRepository.getReseponse(any()) } coAnswers { throw Throwable("Failed") }

        // when
        viewModel.getFareFlightCalendar("", hashMapOf(), Date(), Date())

        // then
        viewModel.fareFlightCalendarData.value?.size shouldBe 0
    }

    @Test
    fun getFareFlightCalendar_successToFetch_flightFareCalendarDataShouldBeDummy() {
        // given
        val result = hashMapOf<Type, Any>(
                FlightFareData::class.java to FARE_CALENDAR_DATA
        )
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { gqlRepository.getReseponse(any()) } returns gqlResponse

        // when
        viewModel.getFareFlightCalendar("", hashMapOf(), Date(), Date())

        // then
        viewModel.fareFlightCalendarData.value?.size shouldBe 0
    }
}