package com.tokopedia.logisticseller.ui.confirmshipping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.ConfirmShippingErrorState
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.ConfirmShippingErrorStateSource
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.ConfirmShippingEvent
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.ConfirmShippingMode
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.ConfirmShippingResult
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.SomChangeCourier
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.SomConfirmShipping
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.SomCourierList
import com.tokopedia.logisticseller.ui.confirmshipping.domain.usecase.ChangeCourierUseCase
import com.tokopedia.logisticseller.ui.confirmshipping.domain.usecase.GetConfirmShippingResultUseCase
import com.tokopedia.logisticseller.ui.confirmshipping.domain.usecase.GetCourierListUseCase
import com.tokopedia.logisticseller.ui.confirmshipping.ui.ConfirmShippingComposeViewModel
import com.tokopedia.targetedticker.domain.GetTargetedTickerResponse
import com.tokopedia.targetedticker.domain.GetTargetedTickerUseCase
import com.tokopedia.targetedticker.domain.TargetedTickerMapper
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
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
class ConfirmShippingViewModelComposeTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var confirmShippingViewModel: ConfirmShippingComposeViewModel
    private var listMsg = listOf<String>()
    private var listCourier =
        listOf<SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment>()

    @RelaxedMockK
    lateinit var somGetConfirmShippingResultUseCase: GetConfirmShippingResultUseCase

    @RelaxedMockK
    lateinit var somGetCourierListUseCase: GetCourierListUseCase

    @RelaxedMockK
    lateinit var somChangeCourierUseCase: ChangeCourierUseCase

    @RelaxedMockK
    lateinit var targetedTickerUseCase: GetTargetedTickerUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        confirmShippingViewModel = ConfirmShippingComposeViewModel(
            coroutineTestRule.dispatchers,
            somGetConfirmShippingResultUseCase,
            somGetCourierListUseCase,
            somChangeCourierUseCase,
            targetedTickerUseCase
        )

        listMsg = listOf("Ok1", "Ok2", "Ok3")

        val courier1 =
            SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment(shipmentId = "123")
        val courier2 =
            SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment(shipmentId = "456")
        val courier3 =
            SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment(shipmentId = "789")
        listCourier = listOf(courier1, courier2, courier3)
    }

    // confirm_shipping_result
    @Test
    fun getConfirmShippingData_shouldReturnSuccess() = coroutineTestRule.runTest {
        // given
        coEvery {
            somGetConfirmShippingResultUseCase.execute(any(), any())
        } returns SomConfirmShipping.Data.MpLogisticConfirmShipping(listMsg)

        // when
        confirmShippingViewModel.onEvent(
            ConfirmShippingEvent.OnCreate(
                "123",
                ConfirmShippingMode.CONFIRM_SHIPPING
            )
        )
        confirmShippingViewModel.onEvent(ConfirmShippingEvent.Submit("123"))

        // then
        runCollectingResult {
            assert(it.last() == ConfirmShippingResult.ShippingConfirmed("Ok1"))
        }
    }

    @Test
    fun getConfirmShippingData_shouldReturnFail() = coroutineTestRule.runTest {
        // given
        val throwable = Throwable()
        coEvery {
            somGetConfirmShippingResultUseCase.execute(any(), any())
        } throws throwable

        // when
        confirmShippingViewModel.onEvent(
            ConfirmShippingEvent.OnCreate(
                "123",
                ConfirmShippingMode.CONFIRM_SHIPPING
            )
        )
        confirmShippingViewModel.onEvent(ConfirmShippingEvent.Submit("123"))

        // then
        runCollectingResult {
            assert(it.last() == ConfirmShippingResult.FailedConfirmShipping(throwable))
        }
    }


    // courier_list
    @Test
    fun getCourierListData_shouldReturnSuccess() = coroutineTestRule.runTest {
        // given
        coEvery {
            somGetCourierListUseCase.execute("123")
        } returns SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment(listCourier)

        // when
        confirmShippingViewModel.onEvent(
            ConfirmShippingEvent.OnCreate(
                "123",
                ConfirmShippingMode.CONFIRM_SHIPPING
            )
        )

        // then
        assert(confirmShippingViewModel.uiState.value.courierList == listCourier)
        assert(!confirmShippingViewModel.uiState.value.loading)
    }

    @Test
    fun getCourierListData_shouldReturnFail() = coroutineTestRule.runTest {
        val deliveryId = "123"
        val throwable = Throwable()

        // given
        coEvery {
            somGetCourierListUseCase.execute(deliveryId)
        } throws throwable

        // when
        confirmShippingViewModel.onEvent(
            ConfirmShippingEvent.OnCreate(
                deliveryId,
                ConfirmShippingMode.CONFIRM_SHIPPING
            )
        )

        // then
        runCollectingError {
            assert(
                it.last() == ConfirmShippingErrorState(
                    throwable,
                    ConfirmShippingErrorStateSource.COURIER_LIST
                )
            )
        }
    }


    // change_courier
    @Test
    fun getChangeCourierData_shouldReturnSuccess() = coroutineTestRule.runTest {
        // given
        coEvery {
            somChangeCourierUseCase.execute(any(), any(), any(), any())
        } returns SomChangeCourier.Data(SomChangeCourier.Data.MpLogisticChangeCourier(listMsg))

        // when
        confirmShippingViewModel.onEvent(
            ConfirmShippingEvent.OnCreate(
                "123",
                ConfirmShippingMode.CHANGE_COURIER
            )
        )
        confirmShippingViewModel.onEvent(
            ConfirmShippingEvent.Submit(
                "123"
            )
        )

        // then
        runCollectingResult {
            assert(it.last() == ConfirmShippingResult.CourierChanged("Ok1"))
        }
    }

    @Test
    fun getChangeCourierData_shouldReturnFail() = coroutineTestRule.runTest {
        // given
        val throwable = Throwable()
        coEvery {
            somChangeCourierUseCase.execute(any(), any(), any(), any())
        } throws throwable

        // when
        confirmShippingViewModel.onEvent(
            ConfirmShippingEvent.OnCreate(
                "123",
                ConfirmShippingMode.CHANGE_COURIER
            )
        )
        confirmShippingViewModel.onEvent(
            ConfirmShippingEvent.Submit(
                "123"
            )
        )

        // then
        runCollectingResult {
            assert(it.last() == ConfirmShippingResult.FailedChangeCourier(throwable))
        }
    }

    @Test
    fun `Targeted Ticker Success`() {
        val targetedTickerParam =
            SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.TickerUnificationParams(
                target = listOf(
                    SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.TickerUnificationTargets(
                        type = "type",
                        values = listOf("a", "b", "c")
                    )
                )
            )

        // given
        coEvery {
            somGetCourierListUseCase.execute("123")
        } returns SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment(listCourier, targetedTickerParam)
        val response = GetTargetedTickerResponse()
        coEvery { targetedTickerUseCase(any()) } returns response

        // when
        confirmShippingViewModel.onEvent(
            ConfirmShippingEvent.OnCreate(
                "123",
                ConfirmShippingMode.CONFIRM_SHIPPING
            )
        )



        val result = confirmShippingViewModel.uiState.value
        val tickerResult = TargetedTickerMapper.convertTargetedTickerToUiModel(response.getTargetedTickerData)

        assertNotNull(result.tickerData)
        assert(tickerResult == result.tickerData)
    }

    private fun runCollectingResult(block: (List<ConfirmShippingResult>) -> Unit) {
        val scope = CoroutineScope(coroutineTestRule.dispatchers.coroutineDispatcher)
        val uiEvent = mutableListOf<ConfirmShippingResult>()
        val uiEventCollectorJob = scope.launch {
            confirmShippingViewModel.result.toList(uiEvent)
        }
        block.invoke(uiEvent)
        uiEventCollectorJob.cancel()
    }

    private fun runCollectingError(block: (List<ConfirmShippingErrorState>) -> Unit) {
        val scope = CoroutineScope(coroutineTestRule.dispatchers.coroutineDispatcher)
        val uiEvent = mutableListOf<ConfirmShippingErrorState>()
        val uiEventCollectorJob = scope.launch {
            confirmShippingViewModel.error.toList(uiEvent)
        }
        block.invoke(uiEvent)
        uiEventCollectorJob.cancel()
    }
}
