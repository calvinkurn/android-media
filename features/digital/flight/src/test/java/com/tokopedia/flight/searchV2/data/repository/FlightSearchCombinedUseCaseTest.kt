package com.tokopedia.flight.searchV2.data.repository

import com.tokopedia.flight.searchV2.domain.usecase.FlightSearchCombinedUseCase
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

/**
 * Created by Rizky on 11/10/18.
 */
class FlightSearchCombinedUseCaseTest {

    private lateinit var flightSearchCombinedUseCase: FlightSearchCombinedUseCase

    @Mock
    private lateinit var flightSearchRepository: FlightSearchRepository

    @Before
    fun setup() {
        flightSearchCombinedUseCase = FlightSearchCombinedUseCase(flightSearchRepository)
    }

    @Test
    fun testCreateObservable() {

    }

}