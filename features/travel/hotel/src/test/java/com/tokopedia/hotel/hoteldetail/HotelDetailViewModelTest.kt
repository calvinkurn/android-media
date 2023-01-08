package com.tokopedia.hotel.hoteldetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.travel.ticker.domain.TravelTickerCoroutineUseCase
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.hotel.DummyHotelGqlQueryInterfaceImpl
import com.tokopedia.hotel.homepage.presentation.model.HotelHomepageModel
import com.tokopedia.hotel.hoteldetail.data.entity.HotelNearbyLandmark
import com.tokopedia.hotel.hoteldetail.data.entity.PropertyDetailData
import com.tokopedia.hotel.hoteldetail.presentation.model.viewmodel.HotelDetailViewModel
import com.tokopedia.hotel.hoteldetail.presentation.model.viewmodel.HotelReview
import com.tokopedia.hotel.hoteldetail.usecase.GetHotelNearbyLandMark
import com.tokopedia.hotel.roomlist.usecase.GetHotelRoomListUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
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
class HotelDetailViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var hotelDetailViewModel: HotelDetailViewModel

    private val graphqlRepository = mockk<GraphqlRepository>()

    private val travelTickerCoroutineUseCase = mockk<TravelTickerCoroutineUseCase>()

    @RelaxedMockK
    lateinit var getHotelRoomListUseCase: GetHotelRoomListUseCase

    @RelaxedMockK
    lateinit var getHotelNearbyLandMark: GetHotelNearbyLandMark

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        hotelDetailViewModel = HotelDetailViewModel(graphqlRepository, dispatcher, getHotelRoomListUseCase, getHotelNearbyLandMark, travelTickerCoroutineUseCase)
    }

    @Test
    fun getHotelDetailData_allApiReturnSuccessData() {
        //given
        val fakeGqlInterface = DummyHotelGqlQueryInterfaceImpl()
        val graphqlSuccessResponse = GraphqlResponse(
                mapOf<Type, Any>(PropertyDetailData.Response::class.java to PropertyDetailData.Response()),
                mapOf<Type, List<GraphqlError>>(),
                false)

        val graphqlSuccessResponse1 = GraphqlResponse(
                mapOf<Type, Any>(HotelReview.Response::class.java to HotelReview.Response()),
                mapOf<Type, List<GraphqlError>>(),
                false)

        coEvery {
            graphqlRepository.response(any(), any())
        } returnsMany listOf(graphqlSuccessResponse, graphqlSuccessResponse1)

        coEvery {
            getHotelRoomListUseCase.execute(any(), any(), any())
        } returns Success(mutableListOf())

        coEvery {
            getHotelNearbyLandMark.execute(any() as DummyHotelGqlQueryInterfaceImpl ,any())
        } returns Success(HotelNearbyLandmark())

        //when
        hotelDetailViewModel.getHotelDetailData(fakeGqlInterface, fakeGqlInterface, fakeGqlInterface, fakeGqlInterface,0, HotelHomepageModel(), "")

        //then
        assert(hotelDetailViewModel.hotelInfoResult.value is Success)
        assert(hotelDetailViewModel.hotelReviewResult.value is Success)
        assert(hotelDetailViewModel.roomListResult.value is Success)
        assert(hotelDetailViewModel.hotelNearbyLandmarks.value is Success)
    }

    @Test
    fun getHotelDetailData_failedToFetchHotelInfo() {
        //given
        val fakeGqlInterface = DummyHotelGqlQueryInterfaceImpl()
        val graphqlResponse = GraphqlResponse(
                mapOf<Type, Any>(),
                mapOf<Type, List<GraphqlError>>(PropertyDetailData.Response::class.java to listOf(GraphqlError())),
                false)

        val graphqlResponse1 = GraphqlResponse(
                mapOf<Type, Any>(HotelReview.Response::class.java to HotelReview.Response()),
                mapOf<Type, List<GraphqlError>>(),
                false)

        coEvery {
            graphqlRepository.response(any(), any())
        } returnsMany listOf(graphqlResponse, graphqlResponse1)

        coEvery {
            getHotelRoomListUseCase.execute(any(), any(), any())
        } returns Success(mutableListOf())

        coEvery {
            getHotelNearbyLandMark.execute(any() as DummyHotelGqlQueryInterfaceImpl, any())
        } returns Success(HotelNearbyLandmark())

        //when
        hotelDetailViewModel.getHotelDetailData(fakeGqlInterface, fakeGqlInterface, fakeGqlInterface, fakeGqlInterface,0, HotelHomepageModel(), "")

        //then
        assert(hotelDetailViewModel.hotelInfoResult.value is Fail)
        assert(hotelDetailViewModel.hotelReviewResult.value is Success)
        assert(hotelDetailViewModel.roomListResult.value is Success)
        assert(hotelDetailViewModel.hotelNearbyLandmarks.value is Success)
    }

    @Test
    fun getHotelDetailData_failedToFetchReview() {
        //given
        val fakeGqlInterface = DummyHotelGqlQueryInterfaceImpl()
        val graphqlResponse = GraphqlResponse(
                mapOf<Type, Any>(PropertyDetailData.Response::class.java to PropertyDetailData.Response()),
                mapOf<Type, List<GraphqlError>>(),
                false)

        val graphqlResponse1 = GraphqlResponse(
                mapOf<Type, Any>(),
                mapOf<Type, List<GraphqlError>>(HotelReview.Response::class.java to listOf(GraphqlError())),
                false)

        coEvery {
            graphqlRepository.response(any(), any())
        } returnsMany listOf(graphqlResponse, graphqlResponse1)

        coEvery {
            getHotelRoomListUseCase.execute(any(), any(), any())
        } returns Success(mutableListOf())

        coEvery {
            getHotelNearbyLandMark.execute(any() as DummyHotelGqlQueryInterfaceImpl, any())
        } returns Success(HotelNearbyLandmark())

        //when
        hotelDetailViewModel.getHotelDetailData(fakeGqlInterface, fakeGqlInterface, fakeGqlInterface, fakeGqlInterface,0, HotelHomepageModel(), "")

        //then
        assert(hotelDetailViewModel.hotelInfoResult.value is Success)
        assert(hotelDetailViewModel.hotelReviewResult.value is Fail)
        assert(hotelDetailViewModel.roomListResult.value is Success)
        assert(hotelDetailViewModel.hotelNearbyLandmarks.value is Success)
    }

    @Test
    fun getHotelDetailData_failedToFetchRoomList() {
        //given
        val fakeGqlInterface = DummyHotelGqlQueryInterfaceImpl()
        val graphqlResponse = GraphqlResponse(
                mapOf<Type, Any>(PropertyDetailData.Response::class.java to PropertyDetailData.Response()),
                mapOf<Type, List<GraphqlError>>(),
                false)

        val graphqlResponse1 = GraphqlResponse(
                mapOf<Type, Any>(HotelReview.Response::class.java to HotelReview.Response()),
                mapOf<Type, List<GraphqlError>>(),
                false)

        coEvery {
            graphqlRepository.response(any(), any())
        } returnsMany listOf(graphqlResponse, graphqlResponse1)

        coEvery {
            getHotelRoomListUseCase.execute(any(), any(), any())
        } returns Fail(Throwable())

        //when
        hotelDetailViewModel.getHotelDetailData(fakeGqlInterface, fakeGqlInterface, fakeGqlInterface, fakeGqlInterface,0, HotelHomepageModel(), "")

        //then
        assert(hotelDetailViewModel.hotelInfoResult.value is Success)
        assert(hotelDetailViewModel.hotelReviewResult.value is Success)
        assert(hotelDetailViewModel.roomListResult.value is Fail)
    }

    @Test
    fun getHotelDetailDataWithoutRoom_allApiCallSuccess() {
        //given
        val fakeGqlInterface = DummyHotelGqlQueryInterfaceImpl()
        val graphqlSuccessResponse = GraphqlResponse(
                mapOf<Type, Any>(PropertyDetailData.Response::class.java to PropertyDetailData.Response()),
                mapOf<Type, List<GraphqlError>>(),
                false)

        val graphqlSuccessResponse1 = GraphqlResponse(
                mapOf<Type, Any>(HotelReview.Response::class.java to HotelReview.Response()),
                mapOf<Type, List<GraphqlError>>(),
                false)

        coEvery {
            graphqlRepository.response(any(), any())
        } returnsMany listOf(graphqlSuccessResponse, graphqlSuccessResponse1)

        //when
        hotelDetailViewModel.getHotelDetailDataWithoutRoom(fakeGqlInterface, fakeGqlInterface, fakeGqlInterface,0, "")

        //then
        assert(hotelDetailViewModel.hotelInfoResult.value is Success)
        assert(hotelDetailViewModel.hotelReviewResult.value is Success)
        assert(hotelDetailViewModel.roomListResult.value == null)
    }

    @Test
    fun getRoomWithoutHotelData_successCall() {
        //given
        coEvery {
            getHotelRoomListUseCase.execute(any(), any(), any())
        } returns Success(mutableListOf())

        //when
        hotelDetailViewModel.getRoomWithoutHotelData(DummyHotelGqlQueryInterfaceImpl(), HotelHomepageModel())

        //then
        assert(hotelDetailViewModel.hotelInfoResult.value == null)
        assert(hotelDetailViewModel.hotelReviewResult.value == null)
        assert(hotelDetailViewModel.roomListResult.value is Success)
    }

    @Test
    fun getTickerData_shouldData() {
        //given
        val title = "Title ABC"
        val message = "this is a message"
        val response = TravelTickerModel(title = title, message = message, url = "", type = 0, status = 0,
                endTime = "", startTime = "", instances = 0, page = "", isPeriod = true)
        coEvery {
            travelTickerCoroutineUseCase.execute(any(), any())
        } returns Success(response)

        //when
        hotelDetailViewModel.fetchTickerData()

        //then
        val actual = hotelDetailViewModel.tickerData.value
        assert(actual is Success)
        assert((actual as Success).data.title == title)
        assert(actual.data.message == message)
    }

    @Test
    fun getTickerData_shouldReturnFail() {
        //given
        coEvery {
            travelTickerCoroutineUseCase.execute(any(), any())
        } returns Fail(Throwable())

        //when
        hotelDetailViewModel.fetchTickerData()

        //then
        val actual = hotelDetailViewModel.tickerData.value
        assert(actual is Fail)
    }
}