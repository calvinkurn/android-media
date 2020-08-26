package com.tokopedia.buyerorder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.JsonArray
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.*
import com.tokopedia.buyerorder.unifiedhistory.list.domain.*
import com.tokopedia.buyerorder.unifiedhistory.list.view.viewmodel.UohListViewModel
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
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
 * Created by fwidjaja on 2020-05-07.
 */

@RunWith(JUnit4::class)
class UohListViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = UohTestDispatcherProvider()
    private lateinit var uohListViewModel: UohListViewModel
    private var listOrderHistory = listOf<UohListOrder.Data.UohOrders.Order>()
    private var listRecommendation = listOf<RecommendationWidget>()
    private var finishOrderResult = UohFinishOrder.Data.FinishOrderBuyer()
    private var atcResult = listOf<AtcMultiData>()
    private var flightResendEmailResult = listOf<FlightResendEmail.Data>()
    private var trainResendEmailResult = listOf<TrainResendEmail.Data>()
    private var rechargeSetFailResult = listOf<RechargeSetFailData.Data>()
    private var listMsg = arrayListOf<String>()

    @RelaxedMockK
    lateinit var uohListUseCase: UohListUseCase

    @RelaxedMockK
    lateinit var getRecommendationUseCase: GetRecommendationUseCase

    @RelaxedMockK
    lateinit var uohFinishOrderUseCase: UohFinishOrderUseCase

    @RelaxedMockK
    lateinit var atcMultiProductsUseCase: AtcMultiProductsUseCase

    @RelaxedMockK
    lateinit var lsPrintFinishOrderUseCase: LsPrintFinishOrderUseCase

    @RelaxedMockK
    lateinit var flightResendEmailUseCase: FlightResendEmailUseCase

    @RelaxedMockK
    lateinit var trainResendEmailUseCase: TrainResendEmailUseCase

    @RelaxedMockK
    lateinit var rechargeSetFailUseCase: RechargeSetFailUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        uohListViewModel = UohListViewModel(dispatcher, uohListUseCase,
                getRecommendationUseCase, uohFinishOrderUseCase, atcMultiProductsUseCase,
                lsPrintFinishOrderUseCase, flightResendEmailUseCase, trainResendEmailUseCase, rechargeSetFailUseCase)

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

    // order_history_list
    @Test
    fun getOrderHistoryData_shouldReturnSuccess() {
        //given
        coEvery {
            uohListUseCase.execute(any(), any())
        } returns Success(UohListOrder.Data.UohOrders(listOrderHistory))

        //when
        uohListViewModel.loadOrderList("", UohListParam())

        //then
        assert(uohListViewModel.orderHistoryListResult.value is Success)
        assert((uohListViewModel.orderHistoryListResult.value as Success<UohListOrder.Data.UohOrders>).data.orders[0].orderUUID.equals("abc", true))
    }

    @Test
    fun getOrderHistoryData_shouldReturnFail() {
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
    fun getOrderHistoryData_shouldNotReturnEmpty() {
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

    // recommendation
    @Test
    fun getRecommendation_shouldReturnSuccess() {
        //given
        coEvery {
            uohListUseCase.execute(any(), any())
        } returns Success(UohListOrder.Data.UohOrders(listOrderHistory))

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
            atcMultiProductsUseCase.execute(any(), any())
        } returns Success(AtcMultiData(AtcMultiData.AtcMulti("", "", AtcMultiData.AtcMulti.BuyAgainData(success = 1))))

        //when
        uohListViewModel.doAtc("", JsonArray())

        //then
        assert(uohListViewModel.atcResult.value is Success)
        assert((uohListViewModel.atcResult.value as Success<AtcMultiData>).data.atcMulti.buyAgainData.success == 1)
    }

    @Test
    fun atc_shouldReturnFail() {
        //given
        coEvery {
            atcMultiProductsUseCase.execute(any(), any())
        } returns Fail(Throwable())

        //when
        uohListViewModel.doAtc("", JsonArray())

        //then
        assert(uohListViewModel.atcResult.value is Fail)
    }

    @Test
    fun atc_shouldNotReturnEmptyMessage() {
        //given
        coEvery {
            atcMultiProductsUseCase.execute(any(), any())
        } returns Success(AtcMultiData(AtcMultiData.AtcMulti("", "", AtcMultiData.AtcMulti.BuyAgainData(1, listMsg))))

        //when
        uohListViewModel.doAtc("", JsonArray())

        //then
        assert(uohListViewModel.atcResult.value is Success)
        assert((uohListViewModel.atcResult.value as Success<AtcMultiData>).data.atcMulti.buyAgainData.message.isNotEmpty())
    }
}