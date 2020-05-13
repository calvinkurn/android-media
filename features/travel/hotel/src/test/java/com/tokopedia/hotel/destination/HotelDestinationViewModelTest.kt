package com.tokopedia.hotel.destination

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.travel.utils.TravelTestDispatcherProvider
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.hotel.destination.data.model.HotelSuggestion
import com.tokopedia.hotel.destination.data.model.PopularSearch
import com.tokopedia.hotel.destination.data.model.RecentSearch
import com.tokopedia.hotel.destination.data.model.SearchDestination
import com.tokopedia.hotel.destination.view.viewmodel.HotelDestinationViewModel
import com.tokopedia.hotel.destination.view.viewmodel.Loaded
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.lang.reflect.Type

/**
 * @author by jessica on 2020-03-19
 */


@RunWith(JUnit4::class)
class HotelDestinationViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = TravelTestDispatcherProvider()
    private lateinit var hotelDestinationViewModel: HotelDestinationViewModel

    private val userSessionInterface = mockk<UserSessionInterface>()
    private val graphqlRepository = mockk<GraphqlRepository>()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        hotelDestinationViewModel = HotelDestinationViewModel(userSessionInterface, this.graphqlRepository, dispatcher)
    }

    @Test
    fun getHotelRecommendationIsLogin_shouldReturnSuccessWithEmptyData() {
        //given
        val graphqlSuccessResponse = GraphqlResponse(
                mapOf<Type, Any>(PopularSearch.Response::class.java to PopularSearch.Response(),
                        RecentSearch.Response::class.java to RecentSearch.Response()),
                mapOf<Type, List<GraphqlError>>(),
                false)
        coEvery {
            graphqlRepository.getReseponse(any(), any())
        } returns graphqlSuccessResponse

        coEvery {
            userSessionInterface.isLoggedIn
        } returns true

        coEvery {
            userSessionInterface.userId
        } returns "0"

        //when
        hotelDestinationViewModel.getHotelRecommendation("", "")

        //then
        assert(hotelDestinationViewModel.popularSearch.value == null)
        assert(hotelDestinationViewModel.recentSearch.value == null)
    }

    @Test
    fun getHotelRecommendationIsLogin_shouldReturnSuccessWithData() {
        //given
        val popularSearches = mutableListOf<PopularSearch>()
        val recentSearches = mutableListOf<RecentSearch>()
        for (i in 0..3) {
            popularSearches.add(PopularSearch(i.toLong()))
            recentSearches.add(RecentSearch(i.toString()))
        }
        val graphqlSuccessResponse = GraphqlResponse(
                mapOf<Type, Any>(PopularSearch.Response::class.java to PopularSearch.Response(popularSearches),
                        RecentSearch.Response::class.java to RecentSearch.Response(recentSearches)),
                mapOf<Type, List<GraphqlError>>(),
                false)
        coEvery {
            graphqlRepository.getReseponse(any(), any())
        } returns graphqlSuccessResponse

        coEvery {
            userSessionInterface.isLoggedIn
        } returns true

        coEvery {
            userSessionInterface.userId
        } returns "0"

        //when
        hotelDestinationViewModel.getHotelRecommendation("", "")

        //then
        assert(hotelDestinationViewModel.popularSearch.value is Success)
        assert(hotelDestinationViewModel.recentSearch.value is Success)
    }

    @Test
    fun getHotelRecommendationIsLogin_shouldFail() {
        //given
        val graphqlErrorResponse = GraphqlResponse(
                mapOf<Type, Any>(),
                mapOf<Type, List<GraphqlError>>(PopularSearch.Response::class.java to listOf(GraphqlError()),
                        RecentSearch.Response::class.java to listOf(GraphqlError())),
                false)
        coEvery {
            graphqlRepository.getReseponse(any(), any())
        } returns graphqlErrorResponse

        coEvery {
            userSessionInterface.isLoggedIn
        } returns true

        coEvery {
            userSessionInterface.userId
        } returns "0"

        //when
        hotelDestinationViewModel.getHotelRecommendation("", "")

        //then
        assert(hotelDestinationViewModel.popularSearch.value == null)
        assert(hotelDestinationViewModel.recentSearch.value == null)
    }

    @Test
    fun getHotelRecommendationIsNotLogin_shouldReturnSuccessWithData() {
        //given
        val popularSearches = mutableListOf<PopularSearch>()
        for (i in 0..3) {
            popularSearches.add(PopularSearch(i.toLong()))
        }
        val graphqlSuccessResponse = GraphqlResponse(
                mapOf<Type, Any>(PopularSearch.Response::class.java to PopularSearch.Response(popularSearches)),
                mapOf<Type, List<GraphqlError>>(),
                false)
        coEvery {
            graphqlRepository.getReseponse(any(), any())
        } returns graphqlSuccessResponse

        coEvery {
            userSessionInterface.isLoggedIn
        } returns false

        //when
        hotelDestinationViewModel.getHotelRecommendation("", "")

        //then
        assert(hotelDestinationViewModel.popularSearch.value is Success)
        assert(hotelDestinationViewModel.recentSearch.value == null)
    }

    @Test
    fun getHotelRecommendationIsNotLogin_shouldReturnFail() {
        //given
        val popularSearches = mutableListOf<PopularSearch>()
        for (i in 0..3) {
            popularSearches.add(PopularSearch(i.toLong()))
        }
        val graphqlErrorResponse = GraphqlResponse(
                mapOf<Type, Any>(),
                mapOf<Type, List<GraphqlError>>(PopularSearch.Response::class.java to listOf(GraphqlError())),
                false)
        coEvery {
            graphqlRepository.getReseponse(any(), any())
        } returns graphqlErrorResponse

        coEvery {
            userSessionInterface.isLoggedIn
        } returns false

        //when
        hotelDestinationViewModel.getHotelRecommendation("", "")

        //then
        assert(hotelDestinationViewModel.popularSearch.value == null)
        assert(hotelDestinationViewModel.recentSearch.value == null)
    }

    @Test
    fun getHotelSearchDestination_shouldReturnSuccessWithData() {
        //given
        val searchDestinations = mutableListOf<SearchDestination>()
        for (i in 0..3) {
            searchDestinations.add(SearchDestination(i.toLong()))
        }
        val graphqlSuccessResponse = GraphqlResponse(
                mapOf<Type, Any>(HotelSuggestion.Response::class.java to HotelSuggestion.Response(HotelSuggestion(searchDestinationList = searchDestinations))),
                mapOf<Type, List<GraphqlError>>(),
                false)
        coEvery {
            graphqlRepository.getReseponse(any(), any())
        } returns graphqlSuccessResponse

        //when
        hotelDestinationViewModel.getHotelSearchDestination("", "")

        //then
        assert(hotelDestinationViewModel.searchDestination.value is Loaded)
        assert((hotelDestinationViewModel.searchDestination.value as Loaded).data is Success)
    }

    @Test
    fun getHotelSearchDestination_shouldReturnFail() {
        //given
        val graphqlErrorResponse = GraphqlResponse(
                mapOf<Type, Any>(),
                mapOf<Type, List<GraphqlError>>(HotelSuggestion.Response::class.java to listOf(GraphqlError())),
                false)
        coEvery {
            graphqlRepository.getReseponse(any(), any())
        } returns graphqlErrorResponse

        //when
        hotelDestinationViewModel.getHotelSearchDestination("", "")

        //then
        assert(hotelDestinationViewModel.searchDestination.value is Loaded)
        assert((hotelDestinationViewModel.searchDestination.value as Loaded).data is Fail)
    }

    @Test
    fun deleteRecentSearch_successInDeleteRecentSearch() {
        //given
        val graphqlSuccessResponse = GraphqlResponse(
                mapOf<Type, Any>(RecentSearch.DeleteResponse::class.java to RecentSearch.DeleteResponse(RecentSearch.DeleteResponse.DeleteResult(true))),
                mapOf<Type, List<GraphqlError>>(),
                false)
        coEvery {
            graphqlRepository.getReseponse(any(), any())
        } returns graphqlSuccessResponse

        coEvery {
            userSessionInterface.userId
        } returns "0"

        //when
        hotelDestinationViewModel.deleteRecentSearch("", "")

        //then
        assert(hotelDestinationViewModel.deleteSuccess.value as Boolean)
    }

    @Test
    fun deleteRecentSearch_failDeleteRecentSearch() {
        //given
        val graphqlErrorResponse = GraphqlResponse(
                mapOf<Type, Any>(),
                mapOf<Type, List<GraphqlError>>(RecentSearch.DeleteResponse::class.java to listOf(GraphqlError())),
                false)
        coEvery {
            graphqlRepository.getReseponse(any(), any())
        } returns graphqlErrorResponse

        coEvery {
            userSessionInterface.userId
        } returns "0"

        //when
        hotelDestinationViewModel.deleteRecentSearch("", "")

        //then
        assert(!(hotelDestinationViewModel.deleteSuccess.value as Boolean))
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