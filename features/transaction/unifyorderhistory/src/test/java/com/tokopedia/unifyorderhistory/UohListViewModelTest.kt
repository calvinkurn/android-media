package com.tokopedia.unifyorderhistory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.data.model.request.AddToCartOccMultiRequestParams
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.request.AddToCartMultiParam
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.AtcMultiData
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartMultiUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
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
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class UohListViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var uohListViewModel: UohListViewModel
    private var listOrderHistory = listOf<UohListOrder.UohOrders.Order>()
    private var listRecommendation = listOf<RecommendationWidget>()
    private var finishOrderResult = UohFinishOrder.FinishOrderBuyer()
    private var listMsg = arrayListOf<String>()
    private var atcOccResponseSuccess = AddToCartDataModel()
    private var atcOccResponseFailed = AddToCartDataModel()
    private var atcOccResponseFailedErrorNull = AddToCartDataModel()

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
    lateinit var atcOccMultiUseCase: AddToCartOccMultiUseCase

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
    lateinit var getUohPmsCounterUseCase: GetUohPmsCounterUseCase

    @RelaxedMockK
    lateinit var atcUseCase: AddToCartUseCase

    private val testShopId = "123"

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        uohListViewModel = UohListViewModel(
            dispatcher, getUohFilterCategoryUseCase, uohListUseCase,
            getRecommendationUseCase, uohFinishOrderUseCase, atcMultiProductsUseCase, atcOccMultiUseCase,
            lsPrintFinishOrderUseCase, flightResendEmailUseCase, trainResendEmailUseCase,
            rechargeSetFailUseCase, topAdsImageViewUseCase, atcUseCase, getUohPmsCounterUseCase
        )

        val order1 = UohListOrder.UohOrders.Order(
            orderUUID = "abc", verticalID = "",
            verticalCategory = "", userID = "", status = "", verticalStatus = "", searchableText = "",
            metadata = UohListOrder.UohOrders.Order.Metadata(), createTime = "", createBy = "",
            updateTime = "", updateBy = ""
        )

        val order2 = UohListOrder.UohOrders.Order(
            orderUUID = "def", verticalID = "",
            verticalCategory = "", userID = "", status = "", verticalStatus = "", searchableText = "",
            metadata = UohListOrder.UohOrders.Order.Metadata(), createTime = "", createBy = "",
            updateTime = "", updateBy = ""
        )

        val order3 = UohListOrder.UohOrders.Order(
            orderUUID = "ghi", verticalID = "",
            verticalCategory = "", userID = "", status = "", verticalStatus = "", searchableText = "",
            metadata = UohListOrder.UohOrders.Order.Metadata(), createTime = "", createBy = "",
            updateTime = "", updateBy = ""
        )

        listOrderHistory = arrayListOf(order1, order2, order3)

        val recomm1 = RecommendationWidget(tid = "123")
        val recomm2 = RecommendationWidget(tid = "456")
        val recomm3 = RecommendationWidget(tid = "789")
        listRecommendation = arrayListOf(recomm1, recomm2, recomm3)

        finishOrderResult = UohFinishOrder.FinishOrderBuyer(1)

        listMsg.add("Test")

        atcOccResponseSuccess = AddToCartDataModel(data = DataModel(success = 1, productId = 2147818593L), status = "OK")
        atcOccResponseFailed = AddToCartDataModel(data = DataModel(success = 0), status = "", errorMessage = arrayListOf("error"))
        atcOccResponseFailedErrorNull = AddToCartDataModel(data = DataModel(success = 0, message = arrayListOf()), status = "", errorMessage = arrayListOf())
    }

    // uoh filter category
    @Test
    fun getFilterCategoryData_shouldReturnSuccess() {
        // given
        coEvery {
            getUohFilterCategoryUseCase(Unit)
        } returns UohFilterCategory()

        // when
        uohListViewModel.loadFilterCategory()

        // then
        assert(uohListViewModel.filterCategoryResult.value is Success)
    }

    @Test
    fun getFilterCategoryData_shouldReturnFail() {
        // given
        coEvery {
            getUohFilterCategoryUseCase(Unit)
        } throws Exception()

        // when
        uohListViewModel.loadFilterCategory()

        // then
        assert(uohListViewModel.filterCategoryResult.value is Fail)
    }

    @Test
    fun getFilterCategoryData_shouldNotReturnEmpty() {
        // given
        coEvery {
            getUohFilterCategoryUseCase(Unit)
        } returns UohFilterCategory(
            uohFilterCategoryData = UohFilterCategory.UohFilterCategoryData(
                v2Filters = listOf(UohFilterCategory.UohFilterCategoryData.FilterV2()),
                categories = listOf(UohFilterCategory.UohFilterCategoryData.Category())
            )
        )

        // when
        uohListViewModel.loadFilterCategory()

        // then
        assert(uohListViewModel.filterCategoryResult.value is Success)
        assert((uohListViewModel.filterCategoryResult.value as Success<UohFilterCategory>).data.uohFilterCategoryData.v2Filters.isNotEmpty())
        assert((uohListViewModel.filterCategoryResult.value as Success<UohFilterCategory>).data.uohFilterCategoryData.categories.isNotEmpty())
    }

    // order_history_list
    @Test
    fun getOrderHistoryData_shouldReturnSuccess() {
        // given
        coEvery {
            uohListUseCase(any())
        } returns UohListOrder(uohOrders = UohListOrder.UohOrders(listOrderHistory))

        // when
        uohListViewModel.loadOrderList(UohListParam())

        // then
        assert(uohListViewModel.orderHistoryListResult.value is Success)
        assert((uohListViewModel.orderHistoryListResult.value as Success<UohListOrder.UohOrders>).data.orders[0].orderUUID.equals("abc", true))
    }

    @Test
    fun getOrderHistoryData_shouldReturnFail() {
        // given
        coEvery {
            uohListUseCase(any())
        } throws Exception()

        // when
        uohListViewModel.loadOrderList(UohListParam())

        // then
        assert(uohListViewModel.orderHistoryListResult.value is Fail)
    }

    @Test
    fun getOrderHistoryData_shouldNotReturnEmpty() {
        // given
        coEvery {
            uohListUseCase(any())
        } returns UohListOrder(uohOrders = UohListOrder.UohOrders(listOrderHistory))

        // when
        uohListViewModel.loadOrderList(UohListParam())

        // then
        assert(uohListViewModel.orderHistoryListResult.value is Success)
        assert((uohListViewModel.orderHistoryListResult.value as Success<UohListOrder.UohOrders>).data.orders.isNotEmpty())
    }

    // recommendation
    @Test
    fun getRecommendation_shouldReturnSuccess() {
        // given
        coEvery {
            getRecommendationUseCase.getData(any())
        } returns emptyList()

        // when
        uohListViewModel.loadRecommendationList(0)

        // then
        assert(uohListViewModel.recommendationListResult.value is Success)
    }

    @Test
    fun getRecommendation_shouldReturnFail() {
        // given
        coEvery {
            getRecommendationUseCase.getData(any())
        } throws Exception()

        // when
        uohListViewModel.loadRecommendationList(0)

        // then
        assert(uohListViewModel.recommendationListResult.value is Fail)
    }

    @Test
    fun getRecommendation_shouldNotReturnEmpty() {
        // given
        coEvery {
            getRecommendationUseCase.getData(any())
        } returns listRecommendation

        // when
        uohListViewModel.loadRecommendationList(0)

        // then
        assert(uohListViewModel.recommendationListResult.value is Success)
        assert((uohListViewModel.recommendationListResult.value as Success).data == listRecommendation)
    }

    // finish order result
    @Test
    fun finishOrderResult_shouldReturnSuccess() {
        // given
        coEvery {
            uohFinishOrderUseCase(any())
        } returns UohFinishOrder(UohFinishOrder.FinishOrderBuyer(1))

        // when
        uohListViewModel.doFinishOrder(UohFinishOrderParam())

        // then
        assert(uohListViewModel.finishOrderResult.value is Success)
        assert((uohListViewModel.finishOrderResult.value as Success<UohFinishOrder.FinishOrderBuyer>).data.success == 1)
    }

    @Test
    fun finishOrderResult_shouldReturnFail() {
        // given
        coEvery {
            uohFinishOrderUseCase(any())
        } throws Exception()

        // when
        uohListViewModel.doFinishOrder(UohFinishOrderParam())

        // then
        assert(uohListViewModel.finishOrderResult.value is Fail)
    }

    @Test
    fun finishOrderResult_shouldNotReturnEmptyMessage() {
        // given
        coEvery {
            uohFinishOrderUseCase(any())
        } returns UohFinishOrder(UohFinishOrder.FinishOrderBuyer(message = listMsg))

        // when
        uohListViewModel.doFinishOrder(UohFinishOrderParam())

        // then
        assert(uohListViewModel.finishOrderResult.value is Success)
        assert((uohListViewModel.finishOrderResult.value as Success<UohFinishOrder.FinishOrderBuyer>).data.message.isNotEmpty())
    }

    // train
    @Test
    fun trainResendEmail_shouldReturnSuccess() {
        // given
        coEvery {
            trainResendEmailUseCase(any())
        } returns TrainResendEmail(TrainResendEmail.TrainResendBookingEmail(success = true))

        // when
        uohListViewModel.doTrainResendEmail(TrainResendEmailParam())

        // then
        assert(uohListViewModel.trainResendEmailResult.value is Success)
        assert((uohListViewModel.trainResendEmailResult.value as Success<TrainResendEmail>).data.trainResendBookingEmail?.success == true)
    }

    @Test
    fun trainResendEmail_shouldReturnFail() {
        // given
        coEvery {
            trainResendEmailUseCase(any())
        } throws Exception()

        // when
        uohListViewModel.doTrainResendEmail(TrainResendEmailParam())

        // then
        assert(uohListViewModel.trainResendEmailResult.value is Fail)
    }

    // recharge
    @Test
    fun rechargeSetFail_shouldReturnSuccess() {
        // given
        coEvery {
            rechargeSetFailUseCase(any())
        } returns RechargeSetFailData(RechargeSetFailData.RechargeSetOrderToFail(RechargeSetFailData.RechargeSetOrderToFail.Attributes(-1, -1, true, "")))

        // when
        uohListViewModel.doRechargeSetFail(-1)

        // then
        assert(uohListViewModel.rechargeSetFailResult.value is Success)
        assert((uohListViewModel.rechargeSetFailResult.value as Success<RechargeSetFailData>).data.rechargeSetOrderToFail.attributes.isSuccess)
    }

    @Test
    fun rechargeSetFail_shouldReturnFail() {
        // given
        coEvery {
            rechargeSetFailUseCase(any())
        } throws Exception()

        // when
        uohListViewModel.doRechargeSetFail(-1)

        // then
        assert(uohListViewModel.rechargeSetFailResult.value is Fail)
    }

    // lsprint
    @Test
    fun lsprintFinishOrder_shouldReturnSuccess() {
        // given
        coEvery {
            lsPrintFinishOrderUseCase(any())
        } returns LsPrintData(LsPrintData.Oiaction("", 1, LsPrintData.Oiaction.Data()))

        // when
        uohListViewModel.doLsPrintFinishOrder("")

        // then
        assert(uohListViewModel.lsPrintFinishOrderResult.value is Success)
        assert((uohListViewModel.lsPrintFinishOrderResult.value as Success<LsPrintData>).data.oiaction.status == 1)
    }

    @Test
    fun lsprintFinishOrder_shouldReturnFail() {
        // given
        coEvery {
            lsPrintFinishOrderUseCase(any())
        } throws Exception()

        // when
        uohListViewModel.doLsPrintFinishOrder("")

        // then
        assert(uohListViewModel.lsPrintFinishOrderResult.value is Fail)
    }

    @Test
    fun lsprintFinishOrder_shouldNotReturnEmptyMessage() {
        // given
        coEvery {
            lsPrintFinishOrderUseCase(any())
        } returns LsPrintData(LsPrintData.Oiaction("", 1, LsPrintData.Oiaction.Data(message = "Test")))

        // when
        uohListViewModel.doLsPrintFinishOrder("")

        // then
        assert(uohListViewModel.lsPrintFinishOrderResult.value is Success)
        assert((uohListViewModel.lsPrintFinishOrderResult.value as Success<LsPrintData>).data.oiaction.data.message.isNotEmpty())
    }

    // atc multi
    @Test
    fun atcMulti_shouldReturnSuccess() {
        // given
        coEvery {
            atcMultiProductsUseCase.execute(any(), any(), any())
        } returns Success(AtcMultiData(AtcMultiData.AtcMulti("", "", AtcMultiData.AtcMulti.BuyAgainData(success = 1))))

        // when
        uohListViewModel.doAtcMulti("", "", arrayListOf(AddToCartMultiParam()), "")

        // then
        assert(uohListViewModel.atcMultiResult.value is Success)
        assert((uohListViewModel.atcMultiResult.value as Success<AtcMultiData>).data.atcMulti.buyAgainData.success == 1)
    }

    @Test
    fun atcMulti_shouldReturnFail() {
        // given
        coEvery {
            atcMultiProductsUseCase.execute(any(), any(), any())
        } returns Fail(Throwable())

        // when
        uohListViewModel.doAtcMulti("", "", arrayListOf(), "")

        // then
        assert(uohListViewModel.atcMultiResult.value is Fail)
    }

    @Test
    fun atcMulti_shouldNotReturnEmptyMessage() {
        // given
        coEvery {
            atcMultiProductsUseCase.execute(any(), any(), any())
        } returns Success(AtcMultiData(AtcMultiData.AtcMulti("", "", AtcMultiData.AtcMulti.BuyAgainData(1, listMsg))))

        // when
        uohListViewModel.doAtcMulti("", "", arrayListOf(), "")

        // then
        assert(uohListViewModel.atcMultiResult.value is Success)
        assert((uohListViewModel.atcMultiResult.value as Success<AtcMultiData>).data.atcMulti.buyAgainData.message.isNotEmpty())
    }

    // atc occ multi
    @Test
    fun atcOccMulti_shouldReturnSuccess() {
        // given
        coEvery {
            atcOccMultiUseCase.setParams(any()).executeOnBackground().mapToAddToCartDataModel()
        } returns atcOccResponseSuccess

        // when
        uohListViewModel.doAtcOccMulti(AddToCartOccMultiRequestParams(carts = emptyList()))

        // then
        coVerify {
            atcOccMultiUseCase.setParams(any()).executeOnBackground()
        }
        assert(uohListViewModel.atcOccMultiResult.value is Success)
        assert((uohListViewModel.atcOccMultiResult.value as Success<AddToCartDataModel>).data.data.success == 1)
    }

    @Test
    fun atcOccMulti_shouldReturnError() {
        // given
        coEvery {
            atcOccMultiUseCase.setParams(any()).executeOnBackground().mapToAddToCartDataModel()
        } returns atcOccResponseFailed

        // when
        uohListViewModel.doAtcOccMulti(AddToCartOccMultiRequestParams(carts = emptyList()))

        // then
        coVerify {
            atcOccMultiUseCase.setParams(any()).executeOnBackground()
        }
        assert(uohListViewModel.atcOccMultiResult.value is Fail)
    }

    @Test
    fun atcOccMulti_shouldReturnError_butErrorisNull() {
        // given
        coEvery {
            atcOccMultiUseCase.setParams(any()).executeOnBackground().mapToAddToCartDataModel()
        } returns atcOccResponseFailedErrorNull

        // when
        uohListViewModel.doAtcOccMulti(AddToCartOccMultiRequestParams(carts = emptyList()))

        // then
        coVerify {
            atcOccMultiUseCase.setParams(any()).executeOnBackground()
        }
        assert(uohListViewModel.atcOccMultiResult.value is Fail)
    }

    // flight
    @Test
    fun flightResendEmail_shouldReturnSuccess() {
        // given
        coEvery {
            flightResendEmailUseCase.executeSuspend(any(), any())
        } returns Success(FlightResendEmail.Data(FlightResendEmail.Data.FlightResendEmailV2(FlightResendEmail.Data.FlightResendEmailV2.Meta(status = "Ok"))))

        // when
        uohListViewModel.doFlightResendEmail("", "")

        // then
        assert(uohListViewModel.flightResendEmailResult.value is Success)
        assert((uohListViewModel.flightResendEmailResult.value as Success<FlightResendEmail.Data>).data.flightResendEmailV2?.meta?.status.equals("Ok", true))
    }

    @Test
    fun flightResendEmail_shouldReturnFail() {
        // given
        coEvery {
            flightResendEmailUseCase.executeSuspend(any(), any())
        } returns Fail(Throwable())

        // when
        uohListViewModel.doFlightResendEmail("", "")

        // then
        assert(uohListViewModel.flightResendEmailResult.value is Fail)
    }

    @Test
    fun flightResendEmail_shouldNotReturnEmptyMessage() {
        // given
        coEvery {
            flightResendEmailUseCase.executeSuspend(any(), any())
        } returns Success(FlightResendEmail.Data(FlightResendEmail.Data.FlightResendEmailV2(FlightResendEmail.Data.FlightResendEmailV2.Meta(status = "Ok"))))

        // when
        uohListViewModel.doFlightResendEmail("", "")

        // then
        assert(uohListViewModel.flightResendEmailResult.value is Success)
        (uohListViewModel.flightResendEmailResult.value as Success<FlightResendEmail.Data>).data.flightResendEmailV2?.meta?.status?.isNotEmpty()?.let { assert(it) }
    }

    // atc
    @Test
    fun atc_shouldReturnSuccess() {
        // given

        coEvery {
            atcUseCase.executeOnBackground()
        } returns AddToCartDataModel(
            status = AddToCartDataModel.STATUS_OK,
            data = DataModel(success = 1)
        )

        // when
        uohListViewModel.doAtc(AddToCartRequestParams())

        // then
        assert(uohListViewModel.atcResult.value is Success)
        assert((uohListViewModel.atcResult.value as Success<AddToCartDataModel>).data.data.success == 1)
    }

    @Test
    fun atc_shouldReturnFail() {
        // given
        coEvery {
            atcUseCase.executeOnBackground()
        } throws Exception()

        // when
        uohListViewModel.doAtc(AddToCartRequestParams())

        // then
        assert(uohListViewModel.atcResult.value is Fail)
    }

    @Test
    fun atc_shouldNotReturnEmptyMessage() {
        // given
        coEvery {
            atcUseCase.executeOnBackground()
        } returns AddToCartDataModel(
            status = AddToCartDataModel.STATUS_OK,
            data = DataModel(success = 1, message = listMsg)
        )

        // when
        uohListViewModel.doAtc(AddToCartRequestParams())

        // then
        assert(uohListViewModel.atcResult.value is Success)
        assert((uohListViewModel.atcResult.value as Success<AddToCartDataModel>).data.data.message.isNotEmpty())
    }

    // tdn
    @Test
    fun tdn_shouldReturnSuccess() {
        // given
        coEvery { topAdsImageViewUseCase.getImageData(any()) }.answers {
            arrayListOf(TopAdsImageViewModel(imageUrl = "url"))
        }

        // when
        uohListViewModel.loadTdnBanner()

        // then
        assert(uohListViewModel.tdnBannerResult.value is Success)
        assert((uohListViewModel.tdnBannerResult.value as Success<TopAdsImageViewModel>).data.imageUrl == "url")
    }

    @Test
    fun tdn_shouldReturnEmpty() {
        // given
        coEvery { topAdsImageViewUseCase.getImageData(any()) }.answers {
            arrayListOf()
        }

        // when
        uohListViewModel.loadTdnBanner()

        // then
        assert(uohListViewModel.tdnBannerResult.value == null)
    }

    @Test
    fun tdn_shouldReturnFail() {
        // given
        coEvery { topAdsImageViewUseCase.getImageData(any()) } throws Exception()

        // when
        uohListViewModel.loadTdnBanner()

        // then
        assert(uohListViewModel.recommendationListResult.value is Fail)
    }

    @Test
    fun getUohCounterSuccess_shouldReturnSuccess() {
        // given
        coEvery {
            getUohPmsCounterUseCase.executeSuspend(any())
        }.answers {
            Success(
                PmsNotification(
                    notifications = Notifications(
                        buyerOrderStatus = BuyerOrderStatus(
                            paymentStatus = 1
                        )
                    )
                )
            )
        }

        // when
        uohListViewModel.loadPmsCounter(testShopId)

        // then
        assert(uohListViewModel.getUohPmsCounterResult.value is Success)
    }

    @Test
    fun getUohCounterSuccess_counterShouldBeCorrect() {
        // given
        val counter = 6
        coEvery {
            getUohPmsCounterUseCase.executeSuspend(any())
        }.answers {
            Success(
                PmsNotification(
                    notifications = Notifications(
                        buyerOrderStatus = BuyerOrderStatus(
                            paymentStatus = counter
                        )
                    )
                )
            )
        }

        // when
        uohListViewModel.loadPmsCounter(testShopId)

        // then
        val result = uohListViewModel.getUohPmsCounterResult.value as Success<PmsNotification>
        assert(result.data.notifications.buyerOrderStatus.paymentStatus == counter)
    }

    @Test
    fun getUohCounterFail_shouldReturnFail() {
        // given
        coEvery {
            getUohPmsCounterUseCase.executeSuspend(any())
        } returns Fail(Exception())

        // when
        uohListViewModel.loadPmsCounter(testShopId)

        // then
        assert(uohListViewModel.getUohPmsCounterResult.value is Fail)
    }
}
