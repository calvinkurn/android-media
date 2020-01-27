package com.tokopedia.hotel.destination

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.hotel.HotelDispatchersProviderTest
import com.tokopedia.hotel.destination.data.model.RecentSearch
import com.tokopedia.hotel.destination.view.viewmodel.HotelDestinationViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * @author by jessica on 2020-01-24
 */

@RunWith(JUnit4::class)
class HotelDestinationViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    @RelaxedMockK
    lateinit var graphqlRepository: GraphqlRepository

    private val dispatcher = HotelDispatchersProviderTest()
    private lateinit var hotelDestinationViewModel: HotelDestinationViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        hotelDestinationViewModel = HotelDestinationViewModel(userSessionInterface, graphqlRepository, dispatcher)
    }

    @Test
    fun getHotelRecommendation_shouldReturnEmpty() {
        //given
        coEvery {
            userSessionInterface.isLoggedIn
        } returns true
        val gqlResponseSuccess = GraphqlResponse(
                mapOf(RecentSearch.Response::class.java to RecentSearch.Response()),
                mapOf(), false)
        coEvery {
            graphqlRepository.getReseponse(any())
        } returns gqlResponseSuccess

        //when
        hotelDestinationViewModel.getHotelRecommendation("", "")

        //then
        assert(hotelDestinationViewModel.recentSearch.value !is Success)
        assert((hotelDestinationViewModel.recentSearch.value as Success).data.isEmpty())


//        //given
//        coEvery {
//            getTravelCollectiveBannerUseCase.execute(any(), any(), any())
//        } returns Success(TravelCollectiveBannerModel())
//
//        //when
//        hotelDestinationViewModel.getHotelPromo("")
//
//        //then
//        val actual = hotelDestinationViewModel.promoData.value
//        assert(actual is Success)
    }

}