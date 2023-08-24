package com.tokopedia.logisticseller.ui.confirmshipping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.SomChangeCourier
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.SomConfirmShipping
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.SomCourierList
import com.tokopedia.logisticseller.ui.confirmshipping.domain.usecase.ChangeCourierUseCase
import com.tokopedia.logisticseller.ui.confirmshipping.domain.usecase.GetConfirmShippingResultUseCase
import com.tokopedia.logisticseller.ui.confirmshipping.domain.usecase.GetCourierListUseCase
import com.tokopedia.logisticseller.ui.confirmshipping.presentation.viewmodel.ConfirmShippingViewModel
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by fwidjaja on 2020-05-08.
 */
@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class ConfirmShippingViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var confirmShippingViewModel: ConfirmShippingViewModel
    private var listMsg = listOf<String>()
    private var listCourier = listOf<SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment>()

    @RelaxedMockK
    lateinit var somGetConfirmShippingResultUseCase: GetConfirmShippingResultUseCase

    @RelaxedMockK
    lateinit var somGetCourierListUseCase: GetCourierListUseCase

    @RelaxedMockK
    lateinit var somChangeCourierUseCase: ChangeCourierUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        confirmShippingViewModel = ConfirmShippingViewModel(
            coroutineTestRule.dispatchers,
            somGetConfirmShippingResultUseCase,
            somGetCourierListUseCase,
            somChangeCourierUseCase
        )

        listMsg = listOf("Ok1", "Ok2", "Ok3")

        val courier1 = SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment(shipmentId = "123")
        val courier2 = SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment(shipmentId = "456")
        val courier3 = SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment(shipmentId = "789")
        listCourier = listOf(courier1, courier2, courier3)
    }

    // confirm_shipping_result
    @Test
    fun getConfirmShippingData_shouldReturnSuccess() = coroutineTestRule.runTest {
        //given
        coEvery {
            somGetConfirmShippingResultUseCase.execute(any(), any())
        } returns SomConfirmShipping.Data.MpLogisticConfirmShipping(listMsg)

        //when
        confirmShippingViewModel.confirmShipping("", "")

        //then
        assert(confirmShippingViewModel.confirmShippingResult.value is Success)
        assert((confirmShippingViewModel.confirmShippingResult.value as Success<SomConfirmShipping.Data.MpLogisticConfirmShipping>).data.listMessage.first() == "Ok1")
    }

    @Test
    fun getConfirmShippingData_shouldReturnFail() = coroutineTestRule.runTest {
        //given
        coEvery {
            somGetConfirmShippingResultUseCase.execute(any(), any())
        } throws Throwable()

        //when
        confirmShippingViewModel.confirmShipping("", "")

        //then
        assert(confirmShippingViewModel.confirmShippingResult.value is Fail)
    }

    @Test
    fun getConfirmShippingData_shouldNotReturnEmpty() = coroutineTestRule.runTest {
        //given
        coEvery {
            somGetConfirmShippingResultUseCase.execute(any(), any())
        } returns SomConfirmShipping.Data.MpLogisticConfirmShipping(listMsg)

        //when
        confirmShippingViewModel.confirmShipping("", "")

        //then
        assert(confirmShippingViewModel.confirmShippingResult.value is Success)
        assert((confirmShippingViewModel.confirmShippingResult.value as Success<SomConfirmShipping.Data.MpLogisticConfirmShipping>).data.listMessage.isNotEmpty())
    }

    // courier_list
    @Test
    fun getCourierListData_shouldReturnSuccess() = coroutineTestRule.runTest {
        //given
        coEvery {
            somGetCourierListUseCase.execute()
        } returns SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment(listCourier).listShipment.toMutableList()

        //when
        confirmShippingViewModel.getCourierList()

        //then
        assert(confirmShippingViewModel.courierListResult.value is Success)
        assert((confirmShippingViewModel.courierListResult.value as Success<MutableList<SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment>>).data.first().shipmentId == "123")
    }

    @Test
    fun getCourierListData_shouldReturnFail() = coroutineTestRule.runTest {
        //given
        coEvery {
            somGetCourierListUseCase.execute()
        } throws Throwable()

        //when
        confirmShippingViewModel.getCourierList()

        //then
        assert(confirmShippingViewModel.courierListResult.value is Fail)
    }

    @Test
    fun getCourierListData_shouldNotReturnEmpty() = coroutineTestRule.runTest {
        //given
        coEvery {
            somGetCourierListUseCase.execute()
        } returns SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment(listCourier).listShipment.toMutableList()

        //when
        confirmShippingViewModel.getCourierList()

        //then
        assert(confirmShippingViewModel.courierListResult.value is Success)
        assert((confirmShippingViewModel.courierListResult.value as Success<MutableList<SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment>>).data.size > 0)
    }

    // change_courier
    @Test
    fun getChangeCourierData_shouldReturnSuccess() = coroutineTestRule.runTest {
        //given
        coEvery {
            somChangeCourierUseCase.execute(any(), any(), any(), any())
        } returns SomChangeCourier.Data(SomChangeCourier.Data.MpLogisticChangeCourier(listMsg))

        //when
        confirmShippingViewModel.changeCourier("", "", 0, 0)

        //then
        assert(confirmShippingViewModel.changeCourierResult.value is Success)
        assert((confirmShippingViewModel.changeCourierResult.value as Success<SomChangeCourier.Data>).data.mpLogisticChangeCourier.listMessage.first() == "Ok1")
    }

    @Test
    fun getChangeCourierData_shouldReturnFail() = coroutineTestRule.runTest {
        //given
        coEvery {
            somChangeCourierUseCase.execute(any(), any(), any(), any())
        } throws Throwable()

        //when
        confirmShippingViewModel.changeCourier("", "", 0, 0)

        //then
        assert(confirmShippingViewModel.changeCourierResult.value is Fail)
    }

    @Test
    fun getChangeCourierData_shouldNotReturnEmpty() = coroutineTestRule.runTest {
        //given
        coEvery {
            somChangeCourierUseCase.execute(any(), any(), any(), any())
        } returns SomChangeCourier.Data(SomChangeCourier.Data.MpLogisticChangeCourier(listMsg))

        //when
        confirmShippingViewModel.changeCourier("", "", 0, 0)

        //then
        assert(confirmShippingViewModel.changeCourierResult.value is Success)
        assert((confirmShippingViewModel.changeCourierResult.value as Success<SomChangeCourier.Data>).data.mpLogisticChangeCourier.listMessage.isNotEmpty())
    }
}
