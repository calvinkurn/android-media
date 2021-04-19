package com.tokopedia.hotel.orderdetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.travel.data.entity.TravelCrossSelling
import com.tokopedia.common.travel.domain.TravelCrossSellingUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.hotel.orderdetail.data.model.HotelOrderDetail
import com.tokopedia.hotel.orderdetail.presentation.viewmodel.HotelOrderDetailViewModel
import com.tokopedia.hotel.orderdetail.usecase.GetHotelOrderDetailUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * @author by jessica on 27/03/20
 */

@RunWith(JUnit4::class)
class HotelOrderDetailViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var hotelOrderDetailViewModel: HotelOrderDetailViewModel

    @RelaxedMockK
    lateinit var getHotelOrderDetailUseCase: GetHotelOrderDetailUseCase

    @RelaxedMockK
    lateinit var crossSellingUseCase: TravelCrossSellingUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        hotelOrderDetailViewModel = HotelOrderDetailViewModel(dispatcher, getHotelOrderDetailUseCase, crossSellingUseCase)
    }

    @Test
    fun getOrderDetail_shouldSuccessFetchData() {
        //given
        coEvery {
            getHotelOrderDetailUseCase.execute(any(), any(), any(), any())
        } returns Success(HotelOrderDetail(HotelOrderDetail.Status(status = 33)))

        //when
        hotelOrderDetailViewModel.getOrderDetail("", null, "", "")

        //assert
        assert(hotelOrderDetailViewModel.orderDetailData.value is Success)
        assert((hotelOrderDetailViewModel.orderDetailData.value as Success<HotelOrderDetail>).data.status.status == 33)
        assert(hotelOrderDetailViewModel.crossSellData.value == null)
    }

    @Test
    fun getOrderDetail_shouldFailFetchData() {
        //given
        coEvery {
            getHotelOrderDetailUseCase.execute(any(), any(), any(), any())
        } returns Fail(Throwable())

        //when
        hotelOrderDetailViewModel.getOrderDetail("", null, "", "")

        //then
        assert(hotelOrderDetailViewModel.orderDetailData.value is Fail)
        assert(hotelOrderDetailViewModel.crossSellData.value == null)
    }

    @Test
    fun getOrderDetail_withCrossSellData_shouldSuccessFetchData() {
        //given
        coEvery {
            getHotelOrderDetailUseCase.execute(any(), any(), any(), any())
        } returns Success(HotelOrderDetail(HotelOrderDetail.Status(status = 33)))

        coEvery {
            crossSellingUseCase.execute(any(), any(), any())
        } returns Success(TravelCrossSelling(listOf(TravelCrossSelling.Item(), TravelCrossSelling.Item())))

        //when
        hotelOrderDetailViewModel.getOrderDetail("", "query", "", "")

        //then
        assert(hotelOrderDetailViewModel.crossSellData.value is Success)
        assert((hotelOrderDetailViewModel.crossSellData.value as Success).data.items.size == 2)
    }

    @Test
    fun getOrderDetail_withCrossSellData_shouldFailFetchData() {
        coEvery {
            getHotelOrderDetailUseCase.execute(any(), any(), any(), any())
        } returns Success(HotelOrderDetail(HotelOrderDetail.Status(status = 33)))

        coEvery {
            crossSellingUseCase.execute(any(), any(), any())
        } returns Fail(Throwable())

        //when
        hotelOrderDetailViewModel.getOrderDetail("", "query", "", "")

        //then
        assert(hotelOrderDetailViewModel.crossSellData.value is Fail)
    }
}