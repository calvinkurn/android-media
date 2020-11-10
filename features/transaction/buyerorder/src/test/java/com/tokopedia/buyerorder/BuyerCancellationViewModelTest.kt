package com.tokopedia.buyerorder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.JsonArray
import com.tokopedia.atc_common.domain.model.response.AtcMultiData
import com.tokopedia.buyerorder.detail.data.getcancellationreason.BuyerGetCancellationReasonData
import com.tokopedia.buyerorder.detail.data.instantcancellation.BuyerInstantCancelData
import com.tokopedia.buyerorder.detail.domain.BuyerGetCancellationReasonUseCase
import com.tokopedia.buyerorder.detail.domain.BuyerInstantCancelUseCase
import com.tokopedia.buyerorder.detail.domain.BuyerRequestCancelUseCase
import com.tokopedia.buyerorder.detail.view.viewmodel.BuyerCancellationViewModel
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.*
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
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
 * Created by fwidjaja on 10/11/20.
 */

@RunWith(JUnit4::class)
class BuyerCancellationViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = BuyerTestDispatcherProvider()
    private lateinit var buyerCancellationViewModel: BuyerCancellationViewModel
    private var listOrderHistory = listOf<UohListOrder.Data.UohOrders.Order>()
    private var listRecommendation = listOf<RecommendationWidget>()
    private var finishOrderResult = UohFinishOrder.Data.FinishOrderBuyer()
    private var atcResult = listOf<AtcMultiData>()
    private var flightResendEmailResult = listOf<FlightResendEmail.Data>()
    private var trainResendEmailResult = listOf<TrainResendEmail.Data>()
    private var rechargeSetFailResult = listOf<RechargeSetFailData.Data>()
    private var listReason = arrayListOf<String>()

    @RelaxedMockK
    lateinit var getCancellationUseCase: BuyerGetCancellationReasonUseCase

    @RelaxedMockK
    lateinit var buyerInstantCancelUseCase: BuyerInstantCancelUseCase

    @RelaxedMockK
    lateinit var buyerRequestCancelUseCase: BuyerRequestCancelUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        buyerCancellationViewModel = BuyerCancellationViewModel(dispatcher, getCancellationUseCase,
                buyerInstantCancelUseCase, buyerRequestCancelUseCase)

        listReason.add("test1")
        listReason.add("test2")
        listReason.add("test3")

        val order1 = UohListOrder.Data.UohOrders.Order(orderUUID = "abc", verticalID = "",
                verticalCategory = "", userID = "", status = "", verticalStatus = "", searchableText = "",
                metadata = UohListOrder.Data.UohOrders.Order.Metadata(), createTime = "", createBy = "",
                updateTime = "", updateBy = "")

        val order2 = UohListOrder.Data.UohOrders.Order(orderUUID = "def", verticalID = "",
                verticalCategory = "", userID = "", status = "", verticalStatus = "", searchableText = "",
                metadata = UohListOrder.Data.UohOrders.Order.Metadata(), createTime = "", createBy = "",
                updateTime = "", updateBy = "")

        val order3 = UohListOrder.Data.UohOrders.Order(orderUUID = "ghi", verticalID = "",
                verticalCategory = "", userID = "", status = "", verticalStatus = "", searchableText = "",
                metadata = UohListOrder.Data.UohOrders.Order.Metadata(), createTime = "", createBy = "",
                updateTime = "", updateBy = "")

        listOrderHistory = arrayListOf(order1, order2, order3)

        val recomm1 = RecommendationWidget(tid = "123")
        val recomm2 = RecommendationWidget(tid = "456")
        val recomm3 = RecommendationWidget(tid = "789")
        listRecommendation = arrayListOf(recomm1, recomm2, recomm3)

        finishOrderResult = UohFinishOrder.Data.FinishOrderBuyer(1)

        listMsg.add("Test")
    }

    // get cancel reason data
    @Test
    fun getCancelReasonData_shouldReturnSuccess() {
        //given
        coEvery {
            getCancellationUseCase.execute(any(), any())
        } returns Success(BuyerGetCancellationReasonData.Data())

        //when
        buyerCancellationViewModel.getCancelReasons("", "", "")

        //then
        assert(buyerCancellationViewModel.cancelReasonResult.value is Success)
        assert((buyerCancellationViewModel.cancelReasonResult.value as Success<BuyerGetCancellationReasonData.Data>).data.getCancellationReason.reasons == listReason)
    }

    @Test
    fun getCancelReasonData_shouldReturnFail() {
        //given
        coEvery {
            getCancellationUseCase.execute(any(), any())
        } returns Fail(Throwable())

        //when
        buyerCancellationViewModel.getCancelReasons("", "", "", "")

        //then
        assert(buyerCancellationViewModel.cancelReasonResult.value is Fail)
    }

    @Test
    fun getCancelReasonData_shouldNotReturnEmpty() {
        //given
        coEvery {
            getCancellationUseCase.execute(any(), any())
        } returns Success(BuyerGetCancellationReasonData.Data())

        //when
        buyerCancellationViewModel.getCancelReasons("", "", "")

        //then
        assert(buyerCancellationViewModel.cancelReasonResult.value is Success)
        assert((buyerCancellationViewModel.cancelReasonResult.value as Success<BuyerGetCancellationReasonData.Data>).data.getCancellationReason.reasons.isNotEmpty())
    }

    // instant cancel
    @Test
    fun getInstantCancel_shouldReturnSuccess() {
        //given
        coEvery {
            buyerInstantCancelUseCase.execute(any(), any())
        } returns Success(BuyerInstantCancelData.Data)

        //when
        uohListViewModel.loadOrderList("", UohListParam())

        //then
        assert(uohListViewModel.orderHistoryListResult.value is Success)
        assert((uohListViewModel.orderHistoryListResult.value as Success<UohListOrder.Data.UohOrders>).data.orders[0].orderUUID.equals("abc", true))
    }

    @Test
    fun getRecommendation_shouldReturnFail() {
        //given
        coEvery {
            uohListUseCase.execute(any(), any())
        } returns Fail(Throwable())

        //when
        uohListViewModel.loadOrderList("", UohListParam())

        //then
        assert(uohListViewModel.orderHistoryListResult.value is Fail)
    }

    @Test
    fun getRecommendation_shouldNotReturnEmpty() {
        //given
        coEvery {
            uohListUseCase.execute(any(), any())
        } returns Success(UohListOrder.Data.UohOrders(listOrderHistory))

        //when
        uohListViewModel.loadOrderList("", UohListParam())

        //then
        assert(uohListViewModel.orderHistoryListResult.value is Success)
        assert((uohListViewModel.orderHistoryListResult.value as Success<UohListOrder.Data.UohOrders>).data.orders.size > 0)
    }

    // finish order result
    @Test
    fun finishOrderResult_shouldReturnSuccess() {
        //given
        coEvery {
            uohFinishOrderUseCase.execute(any(), any())
        } returns Success(UohFinishOrder.Data.FinishOrderBuyer(1))

        //when
        uohListViewModel.doFinishOrder("", UohFinishOrderParam())

        //then
        assert(uohListViewModel.finishOrderResult.value is Success)
        assert((uohListViewModel.finishOrderResult.value as Success<UohFinishOrder.Data.FinishOrderBuyer>).data.success == 1)
    }

    @Test
    fun finishOrderResult_shouldReturnFail() {
        //given
        coEvery {
            uohFinishOrderUseCase.execute(any(), any())
        } returns Fail(Throwable())

        //when
        uohListViewModel.doFinishOrder("", UohFinishOrderParam())

        //then
        assert(uohListViewModel.finishOrderResult.value is Fail)
    }

    @Test
    fun finishOrderResult_shouldNotReturnEmptyMessage() {
        //given
        coEvery {
            uohFinishOrderUseCase.execute(any(), any())
        } returns Success(UohFinishOrder.Data.FinishOrderBuyer(message = listMsg))

        //when
        uohListViewModel.doFinishOrder("", UohFinishOrderParam())

        //then
        assert(uohListViewModel.finishOrderResult.value is Success)
        assert((uohListViewModel.finishOrderResult.value as Success<UohFinishOrder.Data.FinishOrderBuyer>).data.message.isNotEmpty())
    }

    // atc
    @Test
    fun atc_shouldReturnSuccess() {
        //given
        coEvery {
            atcMultiProductsUseCase.execute(any(), any(), any())
        } returns Success(AtcMultiData(AtcMultiData.AtcMulti("", "", AtcMultiData.AtcMulti.BuyAgainData(success = 1))))

        //when
        uohListViewModel.doAtc("", "", JsonArray())

        //then
        assert(uohListViewModel.atcResult.value is Success)
        assert((uohListViewModel.atcResult.value as Success<AtcMultiData>).data.atcMulti.buyAgainData.success == 1)
    }

    @Test
    fun atc_shouldReturnFail() {
        //given
        coEvery {
            atcMultiProductsUseCase.execute(any(), any(), any())
        } returns Fail(Throwable())

        //when
        uohListViewModel.doAtc("", "", JsonArray())

        //then
        assert(uohListViewModel.atcResult.value is Fail)
    }

    @Test
    fun atc_shouldNotReturnEmptyMessage() {
        //given
        coEvery {
            atcMultiProductsUseCase.execute(any(), any(), any())
        } returns Success(AtcMultiData(AtcMultiData.AtcMulti("", "", AtcMultiData.AtcMulti.BuyAgainData(1, listMsg))))

        //when
        uohListViewModel.doAtc("", "", JsonArray())

        //then
        assert(uohListViewModel.atcResult.value is Success)
        assert((uohListViewModel.atcResult.value as Success<AtcMultiData>).data.atcMulti.buyAgainData.message.isNotEmpty())
    }

    // lsprint
    @Test
    fun lsprintFinishOrder_shouldReturnSuccess() {
        //given
        coEvery {
            lsPrintFinishOrderUseCase.execute(any(), any())
        } returns Success(LsPrintData.Data(LsPrintData.Data.Oiaction("", 1, LsPrintData.Data.Oiaction.Data())))

        //when
        uohListViewModel.doLsPrintFinishOrder("", "")

        //then
        assert(uohListViewModel.lsPrintFinishOrderResult.value is Success)
        assert((uohListViewModel.lsPrintFinishOrderResult.value as Success<LsPrintData.Data>).data.oiaction.status == 1)
    }

    @Test
    fun lsprintFinishOrder_shouldReturnFail() {
        //given
        coEvery {
            lsPrintFinishOrderUseCase.execute(any(), any())
        } returns Fail(Throwable())

        //when
        uohListViewModel.doLsPrintFinishOrder("", "")

        //then
        assert(uohListViewModel.lsPrintFinishOrderResult.value is Fail)
    }

    @Test
    fun lsprintFinishOrder_shouldNotReturnEmptyMessage() {
        //given
        coEvery {
            lsPrintFinishOrderUseCase.execute(any(), any())
        } returns Success(LsPrintData.Data(LsPrintData.Data.Oiaction("", 1, LsPrintData.Data.Oiaction.Data(message = "Test"))))

        //when
        uohListViewModel.doLsPrintFinishOrder("", "")

        //then
        assert(uohListViewModel.lsPrintFinishOrderResult.value is Success)
        assert((uohListViewModel.lsPrintFinishOrderResult.value as Success<LsPrintData.Data>).data.oiaction.data.message.isNotEmpty())
    }

    // flight
    @Test
    fun flightResendEmail_shouldReturnSuccess() {
        //given
        coEvery {
            flightResendEmailUseCase.execute(any(), any(), any())
        } returns Success(FlightResendEmail.Data(FlightResendEmail.Data.FlightResendEmailV2(FlightResendEmail.Data.FlightResendEmailV2.Meta(status = "Ok"))))

        //when
        uohListViewModel.doFlightResendEmail("", "", "")

        //then
        assert(uohListViewModel.flightResendEmailResult.value is Success)
        assert((uohListViewModel.flightResendEmailResult.value as Success<FlightResendEmail.Data>).data.flightResendEmailV2?.meta?.status.equals("Ok", true))
    }

    @Test
    fun flightResendEmail_shouldReturnFail() {
        //given
        coEvery {
            flightResendEmailUseCase.execute(any(), any(), any())
        } returns Fail(Throwable())

        //when
        uohListViewModel.doFlightResendEmail("", "", "")

        //then
        assert(uohListViewModel.flightResendEmailResult.value is Fail)
    }

    @Test
    fun flightResendEmail_shouldNotReturnEmptyMessage() {
        //given
        coEvery {
            flightResendEmailUseCase.execute(any(), any(), any())
        } returns Success(FlightResendEmail.Data(FlightResendEmail.Data.FlightResendEmailV2(FlightResendEmail.Data.FlightResendEmailV2.Meta(status = "Ok"))))

        //when
        uohListViewModel.doFlightResendEmail("", "", "")

        //then
        assert(uohListViewModel.flightResendEmailResult.value is Success)
        (uohListViewModel.flightResendEmailResult.value as Success<FlightResendEmail.Data>).data.flightResendEmailV2?.meta?.status?.isNotEmpty()?.let { assert(it) }
    }

    // train
    @Test
    fun trainResendEmail_shouldReturnSuccess() {
        //given
        coEvery {
            trainResendEmailUseCase.execute(any(), any())
        } returns Success(TrainResendEmail.Data(TrainResendEmail.Data.TrainResendBookingEmail(success = true)))

        //when
        uohListViewModel.doTrainResendEmail("", TrainResendEmailParam())

        //then
        assert(uohListViewModel.trainResendEmailResult.value is Success)
        assert((uohListViewModel.trainResendEmailResult.value as Success<TrainResendEmail.Data>).data.trainResendBookingEmail?.success == true)
    }

    @Test
    fun trainResendEmail_shouldReturnFail() {
        //given
        coEvery {
            trainResendEmailUseCase.execute(any(), any())
        } returns Fail(Throwable())

        //when
        uohListViewModel.doTrainResendEmail("", TrainResendEmailParam())

        //then
        assert(uohListViewModel.trainResendEmailResult.value is Fail)
    }

    // recharge
    @Test
    fun rechargeSetFail_shouldReturnSuccess() {
        //given
        coEvery {
            rechargeSetFailUseCase.execute(any(), any())
        } returns Success(RechargeSetFailData.Data(RechargeSetFailData.Data.RechargeSetOrderToFail(RechargeSetFailData.Data.RechargeSetOrderToFail.Attributes(-1, -1, true, ""))))

        //when
        uohListViewModel.doRechargeSetFail("", -1)

        //then
        assert(uohListViewModel.rechargeSetFailResult.value is Success)
        assert((uohListViewModel.rechargeSetFailResult.value as Success<RechargeSetFailData.Data>).data.rechargeSetOrderToFail.attributes.isSuccess)
    }

    @Test
    fun rechargeSetFail_shouldReturnFail() {
        //given
        coEvery {
            rechargeSetFailUseCase.execute(any(), any())
        } returns Fail(Throwable())

        //when
        uohListViewModel.doRechargeSetFail("", -1)

        //then
        assert(uohListViewModel.rechargeSetFailResult.value is Fail)
    }
}