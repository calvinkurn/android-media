package com.tokopedia.hotel.hoteldetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.hotel.HotelDispatchersProviderTest
import com.tokopedia.hotel.hoteldetail.presentation.model.HotelReviewParam
import com.tokopedia.hotel.hoteldetail.presentation.model.viewmodel.HotelReview
import com.tokopedia.hotel.hoteldetail.presentation.model.viewmodel.HotelReviewViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * @author by jessica on 27/03/20
 */

@RunWith(JUnit4::class)
class HotelReviewViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = HotelDispatchersProviderTest()
    private lateinit var hotelReviewViewModel: HotelReviewViewModel

    private val graphqlRepository = mockk<GraphqlRepository>()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        hotelReviewViewModel = HotelReviewViewModel(dispatcher, graphqlRepository)
    }

    @Test
    fun getReview_shouldReturnSuccessData() {
        //given
        val hotelReviewData = HotelReview.ReviewData(listOf(HotelReview(), HotelReview()))
        val graphqlSuccessResponse = GraphqlResponse(
                mapOf(HotelReview.Response::class.java to HotelReview.Response(hotelReviewData)),
                mapOf(),
                false)
        coEvery {
            graphqlRepository.getReseponse(any(), any())
        } returns graphqlSuccessResponse

        //when
        hotelReviewViewModel.getReview("", HotelReviewParam())

        //then
        assert(hotelReviewViewModel.reviewResult.value is Success)
        assert((hotelReviewViewModel.reviewResult.value as Success<HotelReview.ReviewData>).data.reviewList.size == 2)
    }

    @Test
    fun getReview_shouldReturnFailed() {
        //given
        val graphqlErrorResponse = GraphqlResponse(
                mapOf(),
                mapOf(HotelReview.Response::class.java to listOf(GraphqlError())),
                false)
        coEvery {
            graphqlRepository.getReseponse(any(), any())
        } returns graphqlErrorResponse

        //when
        hotelReviewViewModel.getReview("", HotelReviewParam())

        //then
        assert(hotelReviewViewModel.reviewResult.value is Fail)
    }
}