package com.tokopedia.hotel.destination

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.hotel.HotelDispatchersProviderTest
import com.tokopedia.hotel.destination.view.viewmodel.HotelDestinationViewModel
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * @author by jessica on 2020-03-19
 */


@RunWith(JUnit4::class)
class HotelHomepageViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = HotelDispatchersProviderTest()
    private lateinit var hotelDestinationViewModel: HotelDestinationViewModel

    private val userSessionInterface = mockk<UserSessionInterface>()
    private val graphqlRepository = mockk<GraphqlRepository>()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        hotelDestinationViewModel = HotelDestinationViewModel(userSessionInterface, this.graphqlRepository, dispatcher)
    }

    @Test
    fun getHotelRecommendation_shouldReturnSuccessWithData() {

    }

    @Test
    fun getHotelRecommendation_shouldReturnFail() {

    }

    @Test
    fun getHotelSearchDestination_shouldReturnSuccessWithData() {

    }

    @Test
    fun getHotelSearchDestination_shouldReturnFail() {

    }

    @Test
    fun deleteRecentSearch_successInDeleteRecentSearch() {

    }

    @Test
    fun deleteRecentSearch_failDeleteRecentSearch() {

    }

    @Test
    fun setPermissionChecker_shouldSetPermissionChecker() {

    }

    @Test
    fun getCurrentLocation() {

    }

    @Test
    fun getCurrentLocationFromUpdates_shouldUpdateLongLat() {

    }

    @Test
    fun getCurrentLocationFromUpdates_shouldRepresentFailToFetch() {

    }
}