package com.tokopedia.hotel.evoucher

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.hotel.evoucher.data.entity.SharePdfDataResponse
import com.tokopedia.hotel.evoucher.presentation.viewmodel.HotelEVoucherViewModel
import com.tokopedia.hotel.orderdetail.data.model.HotelOrderDetail
import com.tokopedia.hotel.orderdetail.usecase.GetHotelOrderDetailUseCase
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
class HotelEvoucherViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var hotelEVoucherViewModel: HotelEVoucherViewModel

    private val graphqlRepository = mockk<GraphqlRepository>()

    @RelaxedMockK
    lateinit var getHotelOrderDetailUseCase: GetHotelOrderDetailUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        hotelEVoucherViewModel = HotelEVoucherViewModel(graphqlRepository, dispatcher, getHotelOrderDetailUseCase)
    }

    @Test
    fun getOrderDetail_shouldReturnSuccess() {
        //given
        coEvery {
            getHotelOrderDetailUseCase.execute(any(), any(), any(), any())
        } returns Success(HotelOrderDetail(status = HotelOrderDetail.Status(status = 30)))

        //when
        hotelEVoucherViewModel.getOrderDetail("", "")

        //then
        assert(hotelEVoucherViewModel.orderDetailData.value is Success)
        assert((hotelEVoucherViewModel.orderDetailData.value as Success<HotelOrderDetail>).data.status.status == 30)
    }

    @Test
    fun getOrderDetail_shouldReturnFail() {
        //given
        coEvery {
            getHotelOrderDetailUseCase.execute(any(), any(), any(), any())
        } returns Fail(Throwable())

        //when
        hotelEVoucherViewModel.getOrderDetail("", "")

        //then
        assert(hotelEVoucherViewModel.orderDetailData.value is Fail)
    }

    @Test
    fun sendPdf_shouldBeSuccess() {
        //given
        val graphqlSuccessResponse = GraphqlResponse(
                mapOf<Type, Any>(SharePdfDataResponse::class.java to SharePdfDataResponse(true)),
                mapOf<Type, List<GraphqlError>>(),
                false)
        coEvery {
            graphqlRepository.getReseponse(any(), any())
        } returns graphqlSuccessResponse

        //when
        hotelEVoucherViewModel.sendPdf("", listOf(), "")

        //then
        assert(hotelEVoucherViewModel.sharePdfData.value is Success)
        assert((hotelEVoucherViewModel.sharePdfData.value as Success).data.success)
    }

    @Test
    fun sendPdf_shouldBeFailed() {
        //given
        val graphqlErrorResponse = GraphqlResponse(
                mapOf<Type, Any>(),
                mapOf<Type, List<GraphqlError>>(SharePdfDataResponse::class.java to listOf(GraphqlError())),
                false)
        coEvery {
            graphqlRepository.getReseponse(any(), any())
        } returns graphqlErrorResponse

        //when
        hotelEVoucherViewModel.sendPdf("", listOf(), "")

        //then
        assert(hotelEVoucherViewModel.sharePdfData.value is Fail)
    }
}