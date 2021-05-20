package com.tokopedia.hotel.cancellation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.hotel.cancellation.data.HotelCancellationModel
import com.tokopedia.hotel.cancellation.data.HotelCancellationSubmitModel
import com.tokopedia.hotel.cancellation.data.HotelCancellationSubmitParam
import com.tokopedia.hotel.cancellation.data.HotelCancellationSubmitResponse
import com.tokopedia.hotel.cancellation.presentation.viewmodel.HotelCancellationViewModel
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
 * @author by jessica on 13/05/20
 */

@RunWith(JUnit4::class)
class HotelCancellationViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var hotelCancellationViewModel: HotelCancellationViewModel

    private val graphqlRepository = mockk<GraphqlRepository>()

    @RelaxedMockK
    lateinit var multiRequestGraphqlUseCase: MultiRequestGraphqlUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        hotelCancellationViewModel = HotelCancellationViewModel(this.graphqlRepository, multiRequestGraphqlUseCase, dispatcher)
    }

    @Test
    fun getCancellationData_isFromCloud_shouldReturnData() {
        //given
        val dummyCancellationData = HotelCancellationModel.Response(
                HotelCancellationModel.CancellationDataAndMeta(HotelCancellationModel("123")))
        val graphqlSuccessResponse = GraphqlResponse(
                mapOf<Type, Any>(HotelCancellationModel.Response::class.java to dummyCancellationData),
                mapOf<Type, List<GraphqlError>>(),
                false)
        coEvery {
            multiRequestGraphqlUseCase.executeOnBackground()
        } returns graphqlSuccessResponse

        //when
        hotelCancellationViewModel.getCancellationData("")

        //then
        assert(hotelCancellationViewModel.cancellationData.value is Success)
        assert((hotelCancellationViewModel.cancellationData.value as Success<HotelCancellationModel>).data.cancelCartId == "123")
    }

    @Test
    fun getCancellationData_isFromLocal_shouldReturnData() {
        //given
        val dummyCancellationData = HotelCancellationModel.Response(
                HotelCancellationModel.CancellationDataAndMeta(HotelCancellationModel("123")))
        val graphqlSuccessResponse = GraphqlResponse(
                mapOf<Type, Any>(HotelCancellationModel.Response::class.java to dummyCancellationData),
                mapOf<Type, List<GraphqlError>>(),
                false)
        coEvery {
            multiRequestGraphqlUseCase.executeOnBackground()
        } returns graphqlSuccessResponse

        //when
        hotelCancellationViewModel.getCancellationData("", false)

        //then
        assert(hotelCancellationViewModel.cancellationData.value is Success)
        assert((hotelCancellationViewModel.cancellationData.value as Success<HotelCancellationModel>).data.cancelCartId == "123")
    }

    @Test
    fun getCancellationData_isFail_shouldRepresentFailed() {
        //given
        val graphqlFailResponse = GraphqlResponse(
                mapOf<Type, Any>(),
                mapOf<Type, List<GraphqlError>>(HotelCancellationModel.Response::class.java to listOf(GraphqlError())),
                false)
        coEvery {
            multiRequestGraphqlUseCase.executeOnBackground()
        } returns graphqlFailResponse

        //when
        hotelCancellationViewModel.getCancellationData("", true)

        //then
            assert(hotelCancellationViewModel.cancellationData.value is Fail)
    }

    @Test
    fun submitCancellationData_isSuccess_shouldReturnData() {
        //given
        val successData = HotelCancellationSubmitResponse(
                HotelCancellationSubmitResponse.CancellationSubmitMetaAndData(HotelCancellationSubmitModel(success = true)))
        val graphqlSuccessResponse = GraphqlResponse(
                mapOf<Type, Any>(HotelCancellationSubmitResponse::class.java to successData),
                mapOf<Type, List<GraphqlError>>(),
                false)
        coEvery {
            graphqlRepository.getReseponse(any(), any())
        } returns graphqlSuccessResponse

        //when
        hotelCancellationViewModel.submitCancellationData(HotelCancellationSubmitParam())

        //then
        assert(hotelCancellationViewModel.cancellationSubmitData.value is Success)
        assert((hotelCancellationViewModel.cancellationSubmitData.value as Success<HotelCancellationSubmitModel>).data.success)
    }

    @Test
    fun submitCancellationData_isFailedFromBE_shouldReturnData() {
        //given
        val successData = HotelCancellationSubmitResponse(
                HotelCancellationSubmitResponse.CancellationSubmitMetaAndData(HotelCancellationSubmitModel(success = false)))
        val graphqlSuccessResponse = GraphqlResponse(
                mapOf<Type, Any>(HotelCancellationSubmitResponse::class.java to successData),
                mapOf<Type, List<GraphqlError>>(),
                false)
        coEvery {
            graphqlRepository.getReseponse(any(), any())
        } returns graphqlSuccessResponse

        //when
        hotelCancellationViewModel.submitCancellationData(HotelCancellationSubmitParam())

        //then
        assert(hotelCancellationViewModel.cancellationSubmitData.value is Success)
        assert(!(hotelCancellationViewModel.cancellationSubmitData.value as Success<HotelCancellationSubmitModel>).data.success)
    }

    @Test
    fun submitCancellationData_isFail_shouldRepresentFailed() {
        //given
        val graphqlFailResponse = GraphqlResponse(
                mapOf<Type, Any>(),
                mapOf<Type, List<GraphqlError>>(HotelCancellationSubmitResponse::class.java to listOf(GraphqlError())),
                false)
        coEvery {
            graphqlRepository.getReseponse(any(), any())
        } returns graphqlFailResponse

        //when
        hotelCancellationViewModel.submitCancellationData(HotelCancellationSubmitParam())

        //then
        assert(hotelCancellationViewModel.cancellationSubmitData.value is Fail)
    }
}