package com.tokopedia.hotel.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.hotel.HotelDispatchersProviderTest
import com.tokopedia.hotel.search.presentation.activity.HotelSearchResultActivity.Companion.TYPE_CITY
import com.tokopedia.hotel.search.presentation.viewmodel.HotelSearchResultViewModel
import io.mockk.MockKAnnotations
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * @author by jessica on 26/03/20
 */

@RunWith(JUnit4::class)
class HotelSearchResultViewModelTest  {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = HotelDispatchersProviderTest()
    private lateinit var hotelSearchResultViewModel: HotelSearchResultViewModel

    private val graphqlRepository = mockk<GraphqlRepository>()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        hotelSearchResultViewModel = HotelSearchResultViewModel(graphqlRepository, dispatcher)
    }

    @Test
    fun initSearchParam_typeCity_shouldInitSearchParam() {
        //given
        val destinationId = 100
        val type = TYPE_CITY
        val latitude = 0.0f
        val longitude = 0.0f
        val checkIn = "2020-12-20"
        val checkOut = "2020-12-22"
        val totalRoom = 2
        val totalAdult = 4

        //when
        hotelSearchResultViewModel.initSearchParam(destinationId, type,
                latitude, longitude, checkIn, checkOut, totalRoom, totalAdult)

        assert(hotelSearchResultViewModel.searchParam.location.cityID == destinationId)
        assert(hotelSearchResultViewModel.searchParam.location.latitude == latitude)
        assert(hotelSearchResultViewModel.searchParam.location.longitude == longitude)
        assert(hotelSearchResultViewModel.searchParam.checkIn == checkIn)
        assert(hotelSearchResultViewModel.searchParam.checkOut == checkOut)
        assert(hotelSearchResultViewModel.searchParam.room == totalRoom)
        assert(hotelSearchResultViewModel.searchParam.guest.adult == totalAdult)

    }
}