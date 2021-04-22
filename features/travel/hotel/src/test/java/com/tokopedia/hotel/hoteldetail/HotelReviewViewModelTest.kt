package com.tokopedia.hotel.hoteldetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
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
import java.lang.reflect.Type

/**
 * @author by jessica on 27/03/20
 */

@RunWith(JUnit4::class)
class HotelReviewViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
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
                mapOf<Type, Any>(HotelReview.Response::class.java to HotelReview.Response(hotelReviewData)),
                mapOf<Type, List<GraphqlError>>(),
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
                mapOf<Type, Any>(),
                mapOf<Type, List<GraphqlError>>(HotelReview.Response::class.java to listOf(GraphqlError())),
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