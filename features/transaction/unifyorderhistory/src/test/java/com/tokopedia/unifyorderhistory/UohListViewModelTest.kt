package com.tokopedia.unifyorderhistory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.request.AddToCartMultiParam
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.AtcMultiData
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartMultiUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.unifyorderhistory.data.model.*
import com.tokopedia.unifyorderhistory.domain.*
import com.tokopedia.unifyorderhistory.view.viewmodel.UohListViewModel
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
 * Created by fwidjaja on 2020-05-07.
 */

@RunWith(JUnit4::class)
class UohListViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var uohListViewModel: UohListViewModel
    private var listOrderHistory = listOf<UohListOrder.Data.UohOrders.Order>()
    private var listRecommendation = listOf<RecommendationWidget>()
    private var finishOrderResult = UohFinishOrder.Data.FinishOrderBuyer()
    private var listMsg = arrayListOf<String>()

    @RelaxedMockK
    lateinit var getUohFilterCategoryUseCase: GetUohFilterCategoryUseCase

    @RelaxedMockK
    lateinit var uohListUseCase: UohListUseCase

    @RelaxedMockK
    lateinit var getRecommendationUseCase: GetRecommendationUseCase

    @RelaxedMockK
    lateinit var uohFinishOrderUseCase: UohFinishOrderUseCase

    @RelaxedMockK
    lateinit var atcMultiProductsUseCase: AddToCartMultiUseCase

    @RelaxedMockK
    lateinit var lsPrintFinishOrderUseCase: LsPrintFinishOrderUseCase

    @RelaxedMockK
    lateinit var flightResendEmailUseCase: FlightResendEmailUseCase

    @RelaxedMockK
    lateinit var trainResendEmailUseCase: TrainResendEmailUseCase

    @RelaxedMockK
    lateinit var rechargeSetFailUseCase: RechargeSetFailUseCase

    @RelaxedMockK
    lateinit var topAdsImageViewUseCase: TopAdsImageViewUseCase

    @RelaxedMockK
    lateinit var atcUseCase: AddToCartUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        uohListViewModel = UohListViewModel(dispatcher, getUohFilterCategoryUseCase, uohListUseCase,
                getRecommendationUseCase, uohFinishOrderUseCase, atcMultiProductsUseCase,
                lsPrintFinishOrderUseCase, flightResendEmailUseCase, trainResendEmailUseCase,
                rechargeSetFailUseCase, topAdsImageViewUseCase, atcUseCase)

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

    // uoh filter category
    @Test
    fun getFilterCategoryData_shouldReturnSuccess() {
        //given
        coEvery {
            getUohFilterCategoryUseCase.executeSuspend()
        } returns Success(UohFilterCategory.Data())

        //when
        uohListViewModel.loadFilterCategory()

        //then
        assert(uohListViewModel.filterCategoryResult.value is Success)
    }

    @Test
    fun getFilterCategoryData_shouldReturnFail() {
        //given
        coEvery {
            getUohFilterCategoryUseCase.executeSuspend()
        } returns Fail(Throwable())

        //when
        uohListViewModel.loadFilterCategory()

        //then
        assert(uohListViewModel.filterCategoryResult.value is Fail)
    }

    @Test
    fun getFilterCategoryData_shouldNotReturnEmpty() {
        //given
        coEvery {
            getUohFilterCategoryUseCase.executeSuspend()
        } returns Success(UohFilterCategory.Data(uohFilterCategoryData = UohFilterCategory.Data.UohFilterCategoryData(
                v2Filters = listOf(UohFilterCategory.Data.UohFilterCategoryData.FilterV2()),
                categories = listOf(UohFilterCategory.Data.UohFilterCategoryData.Category())
        )))

        //when
        uohListViewModel.loadFilterCategory()

        //then
        assert(uohListViewModel.filterCategoryResult.value is Success)
        assert((uohListViewModel.filterCategoryResult.value as Success<UohFilterCategory.Data>).data.uohFilterCategoryData.v2Filters.isNotEmpty())
        assert((uohListViewModel.filterCategoryResult.value as Success<UohFilterCategory.Data>).data.uohFilterCategoryData.categories.isNotEmpty())
    }

    // order_history_list
    @Test
    fun getOrderHistoryData_shouldReturnSuccess() {
        //given
        coEvery {
            uohListUseCase.executeSuspend(any())
        } returns Success(UohListOrder.Data.UohOrders(listOrderHistory))

        //when
        uohListViewModel.loadOrderList(UohListParam())

        //then
        assert(uohListViewModel.orderHistoryListResult.value is Success)
        assert((uohListViewModel.orderHistoryListResult.value as Success<UohListOrder.Data.UohOrders>).data.orders[0].orderUUID.equals("abc", true))
    }

    @Test
    fun getOrderHistoryData_shouldReturnFail() {
        //given
        coEvery {
            uohListUseCase.executeSuspend(any())
        } returns Fail(Throwable())

        //when
        uohListViewModel.loadOrderList(UohListParam())

        //then
        assert(uohListViewModel.orderHistoryListResult.value is Fail)
    }

    @Test
    fun getOrderHistoryData_shouldNotReturnEmpty() {
        //given
        coEvery {
            uohListUseCase.executeSuspend(any())
        } returns Success(UohListOrder.Data.UohOrders(listOrderHistory))

        //when
        uohListViewModel.loadOrderList(UohListParam())

        //then
        assert(uohListViewModel.orderHistoryListResult.value is Success)
        assert((uohListViewModel.orderHistoryListResult.value as Success<UohListOrder.Data.UohOrders>).data.orders.isNotEmpty())
    }

    // recommendation
    @Test
    fun getRecommendation_shouldReturnSuccess() {
        //given
        coEvery {
            getRecommendationUseCase.getData(any())
        } returns emptyList()

        //when
        uohListViewModel.loadRecommendationList(0)

        //then
        assert(uohListViewModel.recommendationListResult.value is Success)
    }

    @Test
    fun getRecommendation_shouldReturnFail() {
        //given
        coEvery {
            getRecommendationUseCase.getData(any())
        } throws Exception()

        //when
        uohListViewModel.loadRecommendationList(0)

        //then
        assert(uohListViewModel.recommendationListResult.value is Fail)
    }

    @Test
    fun getRecommendation_shouldNotReturnEmpty() {
        //given
        coEvery {
            getRecommendationUseCase.getData(any())
        } returns listRecommendation

        //when
        uohListViewModel.loadRecommendationList(0)

        //then
        assert(uohListViewModel.recommendationListResult.value is Success)
        assert((uohListViewModel.recommendationListResult.value as Success).data == listRecommendation)
    }

    // finish order result
    @Test
    fun finishOrderResult_shouldReturnSuccess() {
        //given
        coEvery {
            uohFinishOrderUseCase.executeSuspend(any())
        } returns Success(UohFinishOrder.Data.FinishOrderBuyer(1))

        //when
        uohListViewModel.doFinishOrder(UohFinishOrderParam())

        //then
        assert(uohListViewModel.finishOrderResult.value is Success)
        assert((uohListViewModel.finishOrderResult.value as Success<UohFinishOrder.Data.FinishOrderBuyer>).data.success == 1)
    }

    @Test
    fun finishOrderResult_shouldReturnFail() {
        //given
        coEvery {
            uohFinishOrderUseCase.executeSuspend(any())
        } returns Fail(Throwable())

        //when
        uohListViewModel.doFinishOrder(UohFinishOrderParam())

        //then
        assert(uohListViewModel.finishOrderResult.value is Fail)
    }

    @Test
    fun finishOrderResult_shouldNotReturnEmptyMessage() {
        //given
        coEvery {
            uohFinishOrderUseCase.executeSuspend(any())
        } returns Success(UohFinishOrder.Data.FinishOrderBuyer(message = listMsg))

        //when
        uohListViewModel.doFinishOrder(UohFinishOrderParam())

        //then
        assert(uohListViewModel.finishOrderResult.value is Success)
        assert((uohListViewModel.finishOrderResult.value as Success<UohFinishOrder.Data.FinishOrderBuyer>).data.message.isNotEmpty())
    }

    // atc multi
    @Test
    fun atcMulti_shouldReturnSuccess() {
        //given
        coEvery {
            atcMultiProductsUseCase.execute(any(), any(), any())
        } returns Success(AtcMultiData(AtcMultiData.AtcMulti("", "", AtcMultiData.AtcMulti.BuyAgainData(success = 1))))

        //when
        uohListViewModel.doAtcMulti("", "", arrayListOf(AddToCartMultiParam()), "")

        //then
        assert(uohListViewModel.atcMultiResult.value is Success)
        assert((uohListViewModel.atcMultiResult.value as Success<AtcMultiData>).data.atcMulti.buyAgainData.success == 1)
    }

    @Test
    fun atcMulti_shouldReturnFail() {
        //given
        coEvery {
            atcMultiProductsUseCase.execute(any(), any(), any())
        } returns Fail(Throwable())

        //when
        uohListViewModel.doAtcMulti("", "", arrayListOf(), "")

        //then
        assert(uohListViewModel.atcMultiResult.value is Fail)
    }

    @Test
    fun atcMulti_shouldNotReturnEmptyMessage() {
        //given
        coEvery {
            atcMultiProductsUseCase.execute(any(), any(), any())
        } returns Success(AtcMultiData(AtcMultiData.AtcMulti("", "", AtcMultiData.AtcMulti.BuyAgainData(1, listMsg))))

        //when
        uohListViewModel.doAtcMulti("", "", arrayListOf(), "")

        //then
        assert(uohListViewModel.atcMultiResult.value is Success)
        assert((uohListViewModel.atcMultiResult.value as Success<AtcMultiData>).data.atcMulti.buyAgainData.message.isNotEmpty())
    }

    // lsprint
    @Test
    fun lsprintFinishOrder_shouldReturnSuccess() {
        //given
        coEvery {
            lsPrintFinishOrderUseCase.executeSuspend(any())
        } returns Success(LsPrintData.Data(LsPrintData.Data.Oiaction("", 1, LsPrintData.Data.Oiaction.Data())))

        //when
        uohListViewModel.doLsPrintFinishOrder("")

        //then
        assert(uohListViewModel.lsPrintFinishOrderResult.value is Success)
        assert((uohListViewModel.lsPrintFinishOrderResult.value as Success<LsPrintData.Data>).data.oiaction.status == 1)
    }

    @Test
    fun lsprintFinishOrder_shouldReturnFail() {
        //given
        coEvery {
            lsPrintFinishOrderUseCase.executeSuspend(any())
        } returns Fail(Throwable())

        //when
        uohListViewModel.doLsPrintFinishOrder("")

        //then
        assert(uohListViewModel.lsPrintFinishOrderResult.value is Fail)
    }

    @Test
    fun lsprintFinishOrder_shouldNotReturnEmptyMessage() {
        //given
        coEvery {
            lsPrintFinishOrderUseCase.executeSuspend(any())
        } returns Success(LsPrintData.Data(LsPrintData.Data.Oiaction("", 1, LsPrintData.Data.Oiaction.Data(message = "Test"))))

        //when
        uohListViewModel.doLsPrintFinishOrder("")

        //then
        assert(uohListViewModel.lsPrintFinishOrderResult.value is Success)
        assert((uohListViewModel.lsPrintFinishOrderResult.value as Success<LsPrintData.Data>).data.oiaction.data.message.isNotEmpty())
    }

    // flight
    @Test
    fun flightResendEmail_shouldReturnSuccess() {
        //given
        coEvery {
            flightResendEmailUseCase.executeSuspend(any(), any())
        } returns Success(FlightResendEmail.Data(FlightResendEmail.Data.FlightResendEmailV2(FlightResendEmail.Data.FlightResendEmailV2.Meta(status = "Ok"))))

        //when
        uohListViewModel.doFlightResendEmail("", "")

        //then
        assert(uohListViewModel.flightResendEmailResult.value is Success)
        assert((uohListViewModel.flightResendEmailResult.value as Success<FlightResendEmail.Data>).data.flightResendEmailV2?.meta?.status.equals("Ok", true))
    }

    @Test
    fun flightResendEmail_shouldReturnFail() {
        //given
        coEvery {
            flightResendEmailUseCase.executeSuspend(any(), any())
        } returns Fail(Throwable())

        //when
        uohListViewModel.doFlightResendEmail("", "")

        //then
        assert(uohListViewModel.flightResendEmailResult.value is Fail)
    }

    @Test
    fun flightResendEmail_shouldNotReturnEmptyMessage() {
        //given
        coEvery {
            flightResendEmailUseCase.executeSuspend(any(), any())
        } returns Success(FlightResendEmail.Data(FlightResendEmail.Data.FlightResendEmailV2(FlightResendEmail.Data.FlightResendEmailV2.Meta(status = "Ok"))))

        //when
        uohListViewModel.doFlightResendEmail("", "")

        //then
        assert(uohListViewModel.flightResendEmailResult.value is Success)
        (uohListViewModel.flightResendEmailResult.value as Success<FlightResendEmail.Data>).data.flightResendEmailV2?.meta?.status?.isNotEmpty()?.let { assert(it) }
    }

    // train
    @Test
    fun trainResendEmail_shouldReturnSuccess() {
        //given
        coEvery {
            trainResendEmailUseCase.executeSuspend(any())
        } returns Success(TrainResendEmail.Data(TrainResendEmail.Data.TrainResendBookingEmail(success = true)))

        //when
        uohListViewModel.doTrainResendEmail(TrainResendEmailParam())

        //then
        assert(uohListViewModel.trainResendEmailResult.value is Success)
        assert((uohListViewModel.trainResendEmailResult.value as Success<TrainResendEmail.Data>).data.trainResendBookingEmail?.success == true)
    }

    @Test
    fun trainResendEmail_shouldReturnFail() {
        //given
        coEvery {
            trainResendEmailUseCase.executeSuspend(any())
        } returns Fail(Throwable())

        //when
        uohListViewModel.doTrainResendEmail(TrainResendEmailParam())

        //then
        assert(uohListViewModel.trainResendEmailResult.value is Fail)
    }

    // recharge
    @Test
    fun rechargeSetFail_shouldReturnSuccess() {
        //given
        coEvery {
            rechargeSetFailUseCase.executeSuspend(any())
        } returns Success(RechargeSetFailData.Data(RechargeSetFailData.Data.RechargeSetOrderToFail(RechargeSetFailData.Data.RechargeSetOrderToFail.Attributes(-1, -1, true, ""))))

        //when
        uohListViewModel.doRechargeSetFail(-1)

        //then
        assert(uohListViewModel.rechargeSetFailResult.value is Success)
        assert((uohListViewModel.rechargeSetFailResult.value as Success<RechargeSetFailData.Data>).data.rechargeSetOrderToFail.attributes.isSuccess)
    }

    @Test
    fun rechargeSetFail_shouldReturnFail() {
        //given
        coEvery {
            rechargeSetFailUseCase.executeSuspend(any())
        } returns Fail(Throwable())

        //when
        uohListViewModel.doRechargeSetFail(-1)

        //then
        assert(uohListViewModel.rechargeSetFailResult.value is Fail)
    }

    // atc
    @Test
    fun atc_shouldReturnSuccess() {
        //given

        coEvery {
            atcUseCase.executeOnBackground()
        } returns AddToCartDataModel(
                        status = AddToCartDataModel.STATUS_OK,
                        data = DataModel(success = 1)
                )

        //when
        uohListViewModel.doAtc(AddToCartRequestParams())

        //then
        assert(uohListViewModel.atcResult.value is Success)
        assert((uohListViewModel.atcResult.value as Success<AddToCartDataModel>).data.data.success == 1)
    }

    @Test
    fun atc_shouldReturnFail() {
        //given
        coEvery {
            atcUseCase.executeOnBackground()
        } throws Exception()

        //when
        uohListViewModel.doAtc(AddToCartRequestParams())

        //then
        assert(uohListViewModel.atcResult.value is Fail)
    }

    @Test
    fun atc_shouldNotReturnEmptyMessage() {
        //given
        coEvery {
            atcUseCase.executeOnBackground()
        } returns AddToCartDataModel(
                status = AddToCartDataModel.STATUS_OK,
                data = DataModel(success = 1, message = listMsg)
        )

        //when
        uohListViewModel.doAtc(AddToCartRequestParams())

        //then
        assert(uohListViewModel.atcResult.value is Success)
        assert((uohListViewModel.atcResult.value as Success<AddToCartDataModel>).data.data.message.isNotEmpty())
    }

    // tdn
    @Test
    fun tdn_shouldReturnSuccess() {
        //given
        coEvery { topAdsImageViewUseCase.getImageData(any()) }.answers{
            arrayListOf(TopAdsImageViewModel(imageUrl = "url"))
        }

        //when
        uohListViewModel.loadTdnBanner()

        //then
        assert(uohListViewModel.tdnBannerResult.value is Success)
        assert((uohListViewModel.tdnBannerResult.value as Success<TopAdsImageViewModel>).data.imageUrl == "url")
    }

    @Test
    fun tdn_shouldReturnEmpty() {
        //given
        coEvery { topAdsImageViewUseCase.getImageData(any()) }.answers{
            arrayListOf()
        }

        //when
        uohListViewModel.loadTdnBanner()

        //then
        assert(uohListViewModel.tdnBannerResult.value == null)
    }

    @Test
    fun tdn_shouldReturnFail() {
        //given
        coEvery { topAdsImageViewUseCase.getImageData(any()) } throws Exception()

        //when
        uohListViewModel.loadTdnBanner()

        //then
        assert(uohListViewModel.recommendationListResult.value is Fail)
    }
}