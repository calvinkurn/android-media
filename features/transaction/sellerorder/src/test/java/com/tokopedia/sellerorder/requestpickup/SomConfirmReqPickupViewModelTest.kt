package com.tokopedia.sellerorder.requestpickup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.sellerorder.requestpickup.data.model.SomConfirmReqPickup
import com.tokopedia.sellerorder.requestpickup.data.model.SomConfirmReqPickupParam
import com.tokopedia.sellerorder.requestpickup.data.model.SomProcessReqPickup
import com.tokopedia.sellerorder.requestpickup.data.model.SomProcessReqPickupParam
import com.tokopedia.sellerorder.requestpickup.domain.SomConfirmReqPickupUseCase
import com.tokopedia.sellerorder.requestpickup.domain.SomProcessReqPickupUseCase
import com.tokopedia.sellerorder.requestpickup.presentation.viewmodel.SomConfirmReqPickupViewModel
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by fwidjaja on 2020-05-12.
 */
@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class SomConfirmReqPickupViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    private lateinit var somConfirmReqPickupViewModel: SomConfirmReqPickupViewModel
    private var listCourier = listOf<SomConfirmReqPickup.Data.MpLogisticPreShipInfo.DataSuccess.Detail.Shipper>()
    private var listMsg = listOf<String>()

    @RelaxedMockK
    lateinit var somConfirmReqPickupUseCase: SomConfirmReqPickupUseCase

    @RelaxedMockK
    lateinit var somProcessReqPickupUseCase: SomProcessReqPickupUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        somConfirmReqPickupViewModel = SomConfirmReqPickupViewModel(
            coroutineTestRule.dispatchers,
            somConfirmReqPickupUseCase,
            somProcessReqPickupUseCase
        )

        val courier1 = SomConfirmReqPickup.Data.MpLogisticPreShipInfo.DataSuccess.Detail.Shipper(name = "JNETesting")
        listCourier = arrayListOf(courier1)

        listMsg = arrayListOf("Ok1")
    }

    // confirm_req_pickup
    @Test
    fun confirmReqPickup_shouldReturnSuccess() = coroutineTestRule.runBlockingTest {
        //given
        coEvery {
            somConfirmReqPickupUseCase.execute(any(), any())
        } returns SomConfirmReqPickup.Data(SomConfirmReqPickup.Data.MpLogisticPreShipInfo(
                dataSuccess = SomConfirmReqPickup.Data.MpLogisticPreShipInfo.DataSuccess(
                        detail = SomConfirmReqPickup.Data.MpLogisticPreShipInfo.DataSuccess.Detail(
                                listShippers = listCourier))))

        //when
        somConfirmReqPickupViewModel.loadConfirmRequestPickup("", SomConfirmReqPickupParam())

        //then
        assert(somConfirmReqPickupViewModel.confirmReqPickupResult.value is Success)
        assert((somConfirmReqPickupViewModel.confirmReqPickupResult.value as Success<SomConfirmReqPickup.Data>).data.mpLogisticPreShipInfo.dataSuccess.detail.listShippers.first().name == "JNETesting")
    }

    @Test
    fun getShippingList_shouldReturnFail() = coroutineTestRule.runBlockingTest {
        //given
        coEvery {
            somConfirmReqPickupUseCase.execute(any(), any())
        } throws Throwable()

        //when
        somConfirmReqPickupViewModel.loadConfirmRequestPickup("", SomConfirmReqPickupParam())

        //then
        assert(somConfirmReqPickupViewModel.confirmReqPickupResult.value is Fail)
    }

    @Test
    fun getShippingList_shouldNotReturnEmpty() = coroutineTestRule.runBlockingTest {
        //given
        coEvery {
            somConfirmReqPickupUseCase.execute(any(), any())
        } returns SomConfirmReqPickup.Data(SomConfirmReqPickup.Data.MpLogisticPreShipInfo(
                dataSuccess = SomConfirmReqPickup.Data.MpLogisticPreShipInfo.DataSuccess(
                        detail = SomConfirmReqPickup.Data.MpLogisticPreShipInfo.DataSuccess.Detail(
                                listShippers = listCourier))))

        //when
        somConfirmReqPickupViewModel.loadConfirmRequestPickup("", SomConfirmReqPickupParam())

        //then
        assert(somConfirmReqPickupViewModel.confirmReqPickupResult.value is Success)
        assert((somConfirmReqPickupViewModel.confirmReqPickupResult.value as Success<SomConfirmReqPickup.Data>).data.mpLogisticPreShipInfo.dataSuccess.detail.listShippers.isNotEmpty())
    }

    // process_req_pickup
    @Test
    fun processReqPickup_shouldReturnSuccess() = coroutineTestRule.runBlockingTest {
        //given
        coEvery {
            somProcessReqPickupUseCase.execute(any(), any())
        } returns SomProcessReqPickup.Data(SomProcessReqPickup.Data.MpLogisticRequestPickup(listMessage = listMsg))

        //when
        somConfirmReqPickupViewModel.processRequestPickup("", SomProcessReqPickupParam())

        //then
        assert(somConfirmReqPickupViewModel.processReqPickupResult.value is Success)
        assert((somConfirmReqPickupViewModel.processReqPickupResult.value as Success<SomProcessReqPickup.Data>).data.mpLogisticRequestPickup.listMessage.first() == "Ok1")
    }

    @Test
    fun processReqPickup_shouldReturnFail() = coroutineTestRule.runBlockingTest {
        //given
        coEvery {
            somProcessReqPickupUseCase.execute(any(), any())
        } throws Throwable()

        //when
        somConfirmReqPickupViewModel.processRequestPickup("", SomProcessReqPickupParam())

        //then
        assert(somConfirmReqPickupViewModel.processReqPickupResult.value is Fail)
    }

    @Test
    fun processReqPickup_shouldNotReturnEmpty() = coroutineTestRule.runBlockingTest {
        //given
        coEvery {
            somProcessReqPickupUseCase.execute(any(), any())
        } returns SomProcessReqPickup.Data(SomProcessReqPickup.Data.MpLogisticRequestPickup(listMessage = listMsg))

        //when
        somConfirmReqPickupViewModel.processRequestPickup("", SomProcessReqPickupParam())

        //then
        assert(somConfirmReqPickupViewModel.processReqPickupResult.value is Success)
        assert((somConfirmReqPickupViewModel.processReqPickupResult.value as Success<SomProcessReqPickup.Data>).data.mpLogisticRequestPickup.listMessage.isNotEmpty())
    }
}
