package com.tokopedia.sellerorder.confirmshipping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.sellerorder.confirmshipping.data.model.SomChangeCourier
import com.tokopedia.sellerorder.confirmshipping.data.model.SomConfirmShipping
import com.tokopedia.sellerorder.confirmshipping.data.model.SomCourierList
import com.tokopedia.sellerorder.confirmshipping.domain.SomChangeCourierUseCase
import com.tokopedia.sellerorder.confirmshipping.domain.SomGetConfirmShippingResultUseCase
import com.tokopedia.sellerorder.confirmshipping.domain.SomGetCourierListUseCase
import com.tokopedia.sellerorder.confirmshipping.presentation.viewmodel.SomConfirmShippingViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
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
 * Created by fwidjaja on 2020-05-08.
 */
@RunWith(JUnit4::class)
class SomConfirmShippingViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var somConfirmShippingViewModel: SomConfirmShippingViewModel
    private var listMsg = listOf<String>()
    private var listCourier = listOf<SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment>()

    @RelaxedMockK
    lateinit var somGetConfirmShippingResultUseCase: SomGetConfirmShippingResultUseCase

    @RelaxedMockK
    lateinit var somGetCourierListUseCase: SomGetCourierListUseCase

    @RelaxedMockK
    lateinit var somChangeCourierUseCase: SomChangeCourierUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        somConfirmShippingViewModel = SomConfirmShippingViewModel(dispatcher,
                somGetConfirmShippingResultUseCase, somGetCourierListUseCase, somChangeCourierUseCase)

        listMsg = listOf("Ok1", "Ok2", "Ok3")

        val courier1 = SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment(shipmentId = "123")
        val courier2 = SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment(shipmentId = "456")
        val courier3 = SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment(shipmentId = "789")
        listCourier = listOf(courier1, courier2, courier3)
    }

    // confirm_shipping_result
    @Test
    fun getConfirmShippingData_shouldReturnSuccess() {
        //given
        coEvery {
            somGetConfirmShippingResultUseCase.execute(any())
        } returns Success(SomConfirmShipping.Data.MpLogisticConfirmShipping(listMsg))

        //when
        somConfirmShippingViewModel.confirmShipping("")

        //then
        assert(somConfirmShippingViewModel.confirmShippingResult.value is Success)
        assert((somConfirmShippingViewModel.confirmShippingResult.value as Success<SomConfirmShipping.Data.MpLogisticConfirmShipping>).data.listMessage.first() == "Ok1")
    }

    @Test
    fun getConfirmShippingData_shouldReturnFail() {
        //given
        coEvery {
            somGetConfirmShippingResultUseCase.execute(any())
        } returns Fail(Throwable())

        //when
        somConfirmShippingViewModel.confirmShipping("")

        //then
        assert(somConfirmShippingViewModel.confirmShippingResult.value is Fail)
    }

    @Test
    fun getConfirmShippingData_shouldNotReturnEmpty() {
        //given
        coEvery {
            somGetConfirmShippingResultUseCase.execute(any())
        } returns Success(SomConfirmShipping.Data.MpLogisticConfirmShipping(listMsg))

        //when
        somConfirmShippingViewModel.confirmShipping("")

        //then
        assert(somConfirmShippingViewModel.confirmShippingResult.value is Success)
        assert((somConfirmShippingViewModel.confirmShippingResult.value as Success<SomConfirmShipping.Data.MpLogisticConfirmShipping>).data.listMessage.isNotEmpty())
    }

    // courier_list
    @Test
    fun getCourierListData_shouldReturnSuccess() {
        //given
        coEvery {
            somGetCourierListUseCase.execute(any())
        } returns Success(SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment(listCourier).listShipment.toMutableList())

        //when
        somConfirmShippingViewModel.getCourierList("")

        //then
        assert(somConfirmShippingViewModel.courierListResult.value is Success)
        assert((somConfirmShippingViewModel.courierListResult.value as Success<MutableList<SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment>>).data.first().shipmentId == "123")
    }

    @Test
    fun getCourierListData_shouldReturnFail() {
        //given
        coEvery {
            somGetCourierListUseCase.execute(any())
        } returns Fail(Throwable())

        //when
        somConfirmShippingViewModel.getCourierList("")

        //then
        assert(somConfirmShippingViewModel.courierListResult.value is Fail)
    }

    @Test
    fun getCourierListData_shouldNotReturnEmpty() {
        //given
        coEvery {
            somGetCourierListUseCase.execute(any())
        } returns Success(SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment(listCourier).listShipment.toMutableList())

        //when
        somConfirmShippingViewModel.getCourierList("")

        //then
        assert(somConfirmShippingViewModel.courierListResult.value is Success)
        assert((somConfirmShippingViewModel.courierListResult.value as Success<MutableList<SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment>>).data.size > 0)
    }

    // change_courier
    @Test
    fun getChangeCourierData_shouldReturnSuccess() {
        //given
        coEvery {
            somChangeCourierUseCase.execute(any())
        } returns Success(SomChangeCourier.Data(SomChangeCourier.Data.MpLogisticChangeCourier(listMsg)))

        //when
        somConfirmShippingViewModel.changeCourier("")

        //then
        assert(somConfirmShippingViewModel.changeCourierResult.value is Success)
        assert((somConfirmShippingViewModel.changeCourierResult.value as Success<SomChangeCourier.Data>).data.mpLogisticChangeCourier.listMessage.first() == "Ok1")
    }

    @Test
    fun getChangeCourierData_shouldReturnFail() {
        //given
        coEvery {
            somChangeCourierUseCase.execute(any())
        } returns Fail(Throwable())

        //when
        somConfirmShippingViewModel.changeCourier("")

        //then
        assert(somConfirmShippingViewModel.changeCourierResult.value is Fail)
    }

    @Test
    fun getChangeCourierData_shouldNotReturnEmpty() {
        //given
        coEvery {
            somChangeCourierUseCase.execute(any())
        } returns Success(SomChangeCourier.Data(SomChangeCourier.Data.MpLogisticChangeCourier(listMsg)))

        //when
        somConfirmShippingViewModel.changeCourier("")

        //then
        assert(somConfirmShippingViewModel.changeCourierResult.value is Success)
        assert((somConfirmShippingViewModel.changeCourierResult.value as Success<SomChangeCourier.Data>).data.mpLogisticChangeCourier.listMessage.isNotEmpty())
    }
}
