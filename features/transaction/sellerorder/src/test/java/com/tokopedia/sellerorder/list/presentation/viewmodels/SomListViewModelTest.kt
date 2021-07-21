package com.tokopedia.sellerorder.list.presentation.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.sellerorder.common.SomOrderBaseViewModelTest
import com.tokopedia.sellerorder.common.util.BulkRequestPickupStatus
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.list.domain.model.SomListBulkRequestPickupResponse
import com.tokopedia.sellerorder.list.domain.model.SomListGetOrderListParam
import com.tokopedia.sellerorder.list.domain.usecases.*
import com.tokopedia.sellerorder.list.presentation.models.*
import com.tokopedia.sellerorder.util.observeAwaitValue
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.shop.common.domain.interactor.AuthorizeAccessUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.lang.RuntimeException
import java.lang.reflect.Field

class SomListViewModelTest : SomOrderBaseViewModelTest<SomListViewModel>() {

    @RelaxedMockK
    lateinit var somListGetTickerUseCase: SomListGetTickerUseCase

    @RelaxedMockK
    lateinit var somListGetFilterListUseCase: SomListGetFilterListUseCase

    @RelaxedMockK
    lateinit var somListGetWaitingPaymentUseCase: SomListGetWaitingPaymentUseCase

    @RelaxedMockK
    lateinit var somListGetOrderListUseCase: SomListGetOrderListUseCase

    @RelaxedMockK
    lateinit var somListGetTopAdsCategoryUseCase: SomListGetTopAdsCategoryUseCase

    @RelaxedMockK
    lateinit var bulkAcceptOrderStatusUseCase: SomListGetBulkAcceptOrderStatusUseCase

    @RelaxedMockK
    lateinit var bulkRequestPickupUseCase: SomListBulkRequestPickupUseCase

    @RelaxedMockK
    lateinit var multiShippingStatusUseCase: SomListGetMultiShippingStatusUseCase

    @RelaxedMockK
    lateinit var bulkAcceptOrderUseCase: SomListBulkAcceptOrderUseCase

    @RelaxedMockK
    lateinit var authorizeAccessUseCase: AuthorizeAccessUseCase

    @RelaxedMockK
    lateinit var authorizeMultiAcceptAccessUseCase: AuthorizeAccessUseCase

    private lateinit var somGetOrderListJobField: Field
    private lateinit var somGetFiltersJobField: Field
    private lateinit var somRefreshOrderJobField: Field
    private lateinit var somCanShowOrderDataField: Field
    private lateinit var retryRequestPickup: Field

    private val dispatcher: CoroutineDispatchers = CoroutineTestDispatchersProvider

    @Before
    override fun setUp() {
        super.setUp()
        viewModel = spyk(SomListViewModel(somAcceptOrderUseCase,
                somRejectOrderUseCase, somRejectCancelOrderUseCase, somEditRefNumUseCase, somValidateOrderUseCase,
                userSessionInterface, dispatcher, somListGetTickerUseCase, somListGetFilterListUseCase,
                somListGetWaitingPaymentUseCase, somListGetOrderListUseCase, somListGetTopAdsCategoryUseCase,
                bulkAcceptOrderStatusUseCase, bulkAcceptOrderUseCase, bulkRequestPickupUseCase, multiShippingStatusUseCase, authorizeAccessUseCase, authorizeMultiAcceptAccessUseCase))

        somGetOrderListJobField = viewModel::class.java.getDeclaredField("getOrderListJob").apply {
            isAccessible = true
        }
        somGetFiltersJobField = viewModel::class.java.getDeclaredField("getFiltersJob").apply {
            isAccessible = true
        }
        somRefreshOrderJobField = viewModel::class.java.getDeclaredField("refreshOrderJobs").apply {
            isAccessible = true
        }
        somCanShowOrderDataField = viewModel::class.java.getDeclaredField("_canShowOrderData").apply {
            isAccessible = true
        }
        retryRequestPickup = viewModel::class.java.getDeclaredField("retryRequestPickup").apply {
            isAccessible = true
        }
    }

    override fun acceptOrder_shouldReturnSuccess() {
        quickActionRefreshOrderTest(true) {
            super.acceptOrder_shouldReturnSuccess()
        }
    }

    override fun acceptOrder_shouldReturnFail() {
        quickActionRefreshOrderTest(false) {
            super.acceptOrder_shouldReturnFail()
        }
    }

    override fun rejectOrder_shouldReturnSuccess() {
        quickActionRefreshOrderTest(true) {
            super.rejectOrder_shouldReturnSuccess()
        }
    }

    override fun rejectOrder_shouldReturnFail() {
        quickActionRefreshOrderTest(false) {
            super.rejectOrder_shouldReturnFail()
        }
    }

    override fun editAwb_shouldReturnSuccess() {
        quickActionRefreshOrderTest(true) {
            super.editAwb_shouldReturnSuccess()
        }
    }

    override fun editAwb_shouldReturnFail() {
        quickActionRefreshOrderTest(false) {
            super.editAwb_shouldReturnFail()
        }
    }

    override fun rejectCancelOrder_shouldReturnSuccess() {
        quickActionRefreshOrderTest(true) {
            super.rejectCancelOrder_shouldReturnSuccess()
        }
    }

    override fun rejectCancelOrder_shouldReturnFail() {
        quickActionRefreshOrderTest(false) {
            super.rejectCancelOrder_shouldReturnFail()
        }
    }

    private fun quickActionRefreshOrderTest(shouldRefresh: Boolean, quickAction: () -> Unit) {
        every {
            viewModel.getFilters(false)
            viewModel.refreshSelectedOrder(orderId, invoice)
        } just runs

        quickAction()

        verify(inverse = !shouldRefresh) {
            viewModel.getFilters(false)
        }

        if (shouldRefresh) {
            val refreshOrderRequest = viewModel.refreshOrderRequest.observeAwaitValue()
            assert(refreshOrderRequest?.first == orderId)
            assert(refreshOrderRequest?.second == invoice)
        }
    }

    private fun doGetTopAdsCategory_shouldSuccess(result: Int = 1) {
        coEvery {
            somListGetTopAdsCategoryUseCase.executeOnBackground()
        } returns result

        viewModel.getTopAdsCategory()

        coVerify {
            somListGetTopAdsCategoryUseCase.executeOnBackground()
        }

        val topAdsCategoryResult = viewModel.topAdsCategoryResult.observeAwaitValue()
        assert(topAdsCategoryResult is Success && topAdsCategoryResult.data == result)
    }

    private fun setGetDataOrderListParams() {
        val newParams = viewModel.getDataOrderListParams().apply {
            search = "keyword"
            startDate = "12/12/2014"
            endDate = "12/12/2017"
            statusList = listOf(220, 400, 500)
            shippingList = mutableSetOf(12)
            orderTypeList = mutableSetOf(12)
            sortBy = SomConsts.SORT_BY_PAYMENT_DATE_ASCENDING
            nextOrderId = 123456L
        }
        viewModel.updateGetOrderListParams(newParams)
    }

    @Test
    fun bulkAcceptOrder_shouldSuccess() = runBlocking {
        val orderIds = listOf("0", "1", "2")

        coEvery {
            bulkAcceptOrderUseCase.executeOnBackground()
        } returns SomListBulkAcceptOrderUiModel(SomListBulkAcceptOrderUiModel.Data())

        viewModel.bulkAcceptOrder(orderIds)

        coVerify(ordering = Ordering.SEQUENCE) {
            bulkAcceptOrderUseCase.setParams(orderIds, userSessionInterface.userId)
            bulkAcceptOrderUseCase.executeOnBackground()
        }

        assert(viewModel.bulkAcceptOrderResult.observeAwaitValue() is Success)
    }

    @Test
    fun bulkAcceptOrder_shouldFailed() = runBlocking {
        val orderIds = listOf("0", "1", "2")

        coEvery {
            bulkAcceptOrderUseCase.executeOnBackground()
        } throws Throwable()

        viewModel.bulkAcceptOrder(orderIds)

        assert(viewModel.bulkAcceptOrderResult.observeAwaitValue() is Fail)
    }

    @Test
    fun getBulkAcceptOrderStatus_shouldSuccess() = runBlocking {
        val orderIds = listOf("0", "1", "2")

        coEvery {
            bulkAcceptOrderUseCase.executeOnBackground()
        } returns SomListBulkAcceptOrderUiModel(SomListBulkAcceptOrderUiModel.Data())

        coEvery {
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        } returns SomListBulkAcceptOrderStatusUiModel(SomListBulkAcceptOrderStatusUiModel.Data(), listOf())

        viewModel.bulkAcceptOrder(orderIds)
        viewModel.bulkAcceptOrderStatusResult.observeAwaitValue()

        coVerify(ordering = Ordering.ORDERED) {
            bulkAcceptOrderUseCase.setParams(orderIds, userSessionInterface.userId)
            bulkAcceptOrderUseCase.executeOnBackground()
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        }

        assert(viewModel.bulkAcceptOrderStatusResult.value is Success)
    }

    @Test
    fun getBulkAcceptOrderStatus_shouldPartialSuccessThenFullSuccessAfterRetry() = runBlocking {
        var count = 0
        val orderIds = listOf("0", "1", "2")

        coEvery {
            bulkAcceptOrderUseCase.executeOnBackground()
        } returns SomListBulkAcceptOrderUiModel(SomListBulkAcceptOrderUiModel.Data())

        coEvery {
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        } coAnswers {
            count++
            if (count == 1) {
                SomListBulkAcceptOrderStatusUiModel(SomListBulkAcceptOrderStatusUiModel.Data(success = 1, totalOrder = 3), listOf())
            } else {
                SomListBulkAcceptOrderStatusUiModel(SomListBulkAcceptOrderStatusUiModel.Data(success = 3, totalOrder = 3), listOf())
            }
        }

        viewModel.bulkAcceptOrder(orderIds)
        viewModel.bulkAcceptOrderStatusResult.observeAwaitValue(10)

        coVerify(ordering = Ordering.ORDERED) {
            bulkAcceptOrderUseCase.setParams(orderIds, userSessionInterface.userId)
            bulkAcceptOrderUseCase.executeOnBackground()
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        }

        assert(viewModel.bulkAcceptOrderStatusResult.value is Success)
    }

    @Test
    fun getBulkAcceptOrderStatus_shouldFailedThenSuccessAfterAutoRetry() = runBlocking {
        var count = 0
        val orderIds = listOf("0", "1", "2")

        coEvery {
            bulkAcceptOrderUseCase.executeOnBackground()
        } returns SomListBulkAcceptOrderUiModel(SomListBulkAcceptOrderUiModel.Data())

        coEvery {
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        } coAnswers {
            count++
            if (count == 1) {
                throw Throwable()
            } else {
                SomListBulkAcceptOrderStatusUiModel(SomListBulkAcceptOrderStatusUiModel.Data(), listOf())
            }
        }

        viewModel.bulkAcceptOrder(orderIds)
        viewModel.bulkAcceptOrderStatusResult.observeAwaitValue(time = 10)

        coVerify(ordering = Ordering.ORDERED) {
            bulkAcceptOrderUseCase.setParams(orderIds, userSessionInterface.userId)
            bulkAcceptOrderUseCase.executeOnBackground()
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        }

        assert(viewModel.bulkAcceptOrderStatusResult.value is Success)
    }

    @Test
    fun getBulkAcceptOrderStatus_shouldAlwaysFailed() = runBlocking {
        val orderIds = listOf("0", "1", "2")

        coEvery {
            bulkAcceptOrderUseCase.executeOnBackground()
        } returns SomListBulkAcceptOrderUiModel(SomListBulkAcceptOrderUiModel.Data())

        coEvery {
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        } throws Throwable()

        viewModel.bulkAcceptOrder(orderIds)
        viewModel.bulkAcceptOrderStatusResult.observeAwaitValue(time = 60)

        coVerify(ordering = Ordering.ORDERED) {
            bulkAcceptOrderUseCase.setParams(orderIds, userSessionInterface.userId)
            bulkAcceptOrderUseCase.executeOnBackground()
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        }

        assert(viewModel.bulkAcceptOrderStatusResult.value == null)
    }

    @Test
    fun retryGetBulkAcceptOrderStatus_shouldSuccess() = runBlocking {
        var count = 0
        val orderIds = listOf("0", "1", "2")

        coEvery {
            bulkAcceptOrderUseCase.executeOnBackground()
        } returns SomListBulkAcceptOrderUiModel(SomListBulkAcceptOrderUiModel.Data())

        coEvery {
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        } coAnswers {
            count++
            if (count <= 20) {
                throw Throwable()
            } else {
                SomListBulkAcceptOrderStatusUiModel(SomListBulkAcceptOrderStatusUiModel.Data(), listOf())
            }
        }

        coEvery {
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        } returns SomListBulkAcceptOrderStatusUiModel(SomListBulkAcceptOrderStatusUiModel.Data(), listOf())

        viewModel.bulkAcceptOrder(orderIds)
        viewModel.bulkAcceptOrderStatusResult.observeAwaitValue()
        viewModel.retryGetBulkAcceptOrderStatus()
        viewModel.bulkAcceptOrderStatusResult.observeAwaitValue()

        coVerify(ordering = Ordering.ORDERED) {
            bulkAcceptOrderUseCase.setParams(orderIds, userSessionInterface.userId)
            bulkAcceptOrderUseCase.executeOnBackground()
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        }

        assert(viewModel.bulkAcceptOrderStatusResult.value is Success)
    }

    @Test
    fun retryGetBulkAcceptOrderStatus_shouldPartialSuccessThenFullSuccessAfterRetry() = runBlocking {
        var count = 0
        val orderIds = listOf("0", "1", "2")

        coEvery {
            bulkAcceptOrderUseCase.executeOnBackground()
        } returns SomListBulkAcceptOrderUiModel(SomListBulkAcceptOrderUiModel.Data())

        coEvery {
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        } coAnswers {
            count++
            if (count <= 20) {
                throw Throwable()
            } else if (count == 21) {
                SomListBulkAcceptOrderStatusUiModel(SomListBulkAcceptOrderStatusUiModel.Data(success = 1, totalOrder = 3), listOf())
            } else {
                SomListBulkAcceptOrderStatusUiModel(SomListBulkAcceptOrderStatusUiModel.Data(success = 3, totalOrder = 3), listOf())
            }
        }

        viewModel.bulkAcceptOrder(orderIds)
        viewModel.bulkAcceptOrderStatusResult.observeAwaitValue(time = 60)
        viewModel.retryGetBulkAcceptOrderStatus()
        viewModel.bulkAcceptOrderStatusResult.observeAwaitValue(time = 10)

        coVerify(ordering = Ordering.ORDERED) {
            bulkAcceptOrderUseCase.setParams(orderIds, userSessionInterface.userId)
            bulkAcceptOrderUseCase.executeOnBackground()
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        }

        assert(viewModel.bulkAcceptOrderStatusResult.value is Success)
    }

    @Test
    fun retryGetBulkAcceptOrderStatus_shouldFailedThenSuccessAfterAutoRetry() = runBlocking {
        var count = 0
        val orderIds = listOf("0", "1", "2")

        coEvery {
            bulkAcceptOrderUseCase.executeOnBackground()
        } returns SomListBulkAcceptOrderUiModel(SomListBulkAcceptOrderUiModel.Data())

        coEvery {
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        } coAnswers {
            count++
            if (count <= 21) {
                throw Throwable()
            } else {
                SomListBulkAcceptOrderStatusUiModel(SomListBulkAcceptOrderStatusUiModel.Data(), listOf())
            }
        }

        viewModel.bulkAcceptOrder(orderIds)
        viewModel.bulkAcceptOrderStatusResult.observeAwaitValue(time = 30)
        viewModel.retryGetBulkAcceptOrderStatus()
        viewModel.bulkAcceptOrderStatusResult.observeAwaitValue(time = 30)

        coVerify(ordering = Ordering.ORDERED) {
            bulkAcceptOrderUseCase.setParams(orderIds, userSessionInterface.userId)
            bulkAcceptOrderUseCase.executeOnBackground()
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        }
        val bulkAcceptOrderStatusResult = viewModel.bulkAcceptOrderStatusResult.value
        assert(bulkAcceptOrderStatusResult is Success)
    }

    @Test
    fun retryGetBulkAcceptOrderStatus_shouldAlwaysFailed() = runBlocking {
        val orderIds = listOf("0", "1", "2")

        coEvery {
            bulkAcceptOrderUseCase.executeOnBackground()
        } returns SomListBulkAcceptOrderUiModel(SomListBulkAcceptOrderUiModel.Data())

        coEvery {
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        } throws Throwable()

        viewModel.bulkAcceptOrder(orderIds)
        viewModel.bulkAcceptOrderStatusResult.observeAwaitValue(time = 60)
        viewModel.retryGetBulkAcceptOrderStatus()
        viewModel.bulkAcceptOrderStatusResult.observeAwaitValue(time = 60)

        coVerify(ordering = Ordering.ORDERED) {
            bulkAcceptOrderUseCase.setParams(orderIds, userSessionInterface.userId)
            bulkAcceptOrderUseCase.executeOnBackground()
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        }

        assert(viewModel.bulkAcceptOrderStatusResult.value == null)
    }

    @Test
    fun bulkRequestPickup_shouldSuccess() = runBlocking {
        val orderIds = listOf("0234", "1456", "2678")
        val DELAY_REQUEST_PICK_UP = 500L

        coEvery {
            bulkRequestPickupUseCase.executeOnBackground()
        } returns SomListBulkRequestPickupUiModel(data = SomListBulkRequestPickupUiModel.Data())

        viewModel.bulkRequestPickup(orderIds)

        delay(DELAY_REQUEST_PICK_UP)

        coVerify {
            bulkRequestPickupUseCase.setParams(orderIds)
            bulkRequestPickupUseCase.executeOnBackground()
        }

        assert(viewModel.bulkRequestPickupResult.observeAwaitValue() is Success)
    }

    @Test
    fun bulkRequestPickup_shouldFailed() = runBlocking {
        val orderIds = listOf("0", "1", "2")

        coEvery {
            bulkAcceptOrderUseCase.executeOnBackground()
        } throws Throwable()

        viewModel.bulkAcceptOrder(orderIds)

        assert(viewModel.bulkAcceptOrderResult.observeAwaitValue() is Fail)
    }

    @Test
    fun getMultiShippingStatus_shouldSuccess() = runBlocking {
        val orderIds = listOf("0", "1", "2")
        val batchId = "1234566"

        coEvery {
            bulkRequestPickupUseCase.executeOnBackground()
        } returns SomListBulkRequestPickupUiModel(SomListBulkRequestPickupUiModel.Data(totalOnProcess = 3, jobId = batchId))

        coEvery {
            multiShippingStatusUseCase.executeOnBackground()
        } returns MultiShippingStatusUiModel()

        viewModel.bulkRequestPickupFinalResultMediator.observe( {lifecycle}) {}

        viewModel.bulkRequestPickup(orderIds)

        coVerify {
            bulkRequestPickupUseCase.setParams(orderIds)
            bulkRequestPickupUseCase.executeOnBackground()
            multiShippingStatusUseCase.executeOnBackground()
        }
        assert(viewModel.bulkRequestPickupResult.observeAwaitValue() is Success)
    }

    @Test
    fun getMultiShippingStatus_shouldFail() = runBlocking {
        val orderIds = listOf("0", "1", "2")
        val batchId = "1234566"

        coEvery {
            bulkRequestPickupUseCase.executeOnBackground()
        } returns SomListBulkRequestPickupUiModel(SomListBulkRequestPickupUiModel.Data(totalOnProcess = 3, jobId = batchId))

        coEvery {
            multiShippingStatusUseCase.executeOnBackground()
        } throws Throwable()

        viewModel.bulkRequestPickupFinalResultMediator.observe( {lifecycle}) {}

        viewModel.bulkRequestPickup(orderIds)

        coVerify {
            bulkRequestPickupUseCase.setParams(orderIds)
            bulkRequestPickupUseCase.executeOnBackground()
            multiShippingStatusUseCase.executeOnBackground()
        }

        assert(viewModel.bulkRequestPickupStatusResult.observeAwaitValue() is Fail)
    }

    @Test
    fun bulkRequestPickup_shouldAllSuccess() = runBlocking {
        val orderIds = listOf("0", "1", "2")
        val batchId = "1234566"

        coEvery {
            bulkRequestPickupUseCase.executeOnBackground()
        } returns SomListBulkRequestPickupUiModel(SomListBulkRequestPickupUiModel.Data(totalOnProcess = 3, jobId = batchId))

        coEvery {
            multiShippingStatusUseCase.executeOnBackground()
        } returns MultiShippingStatusUiModel(total_order = 3, success = 3)

        viewModel.bulkRequestPickupFinalResultMediator.observe( {lifecycle}) {}

        viewModel.bulkRequestPickup(orderIds)

        coVerify {
            bulkRequestPickupUseCase.setParams(orderIds)
            bulkRequestPickupUseCase.executeOnBackground()
            multiShippingStatusUseCase.executeOnBackground()
        }

        assert(viewModel.bulkRequestPickupFinalResult.observeAwaitValue() is AllSuccess)
    }

    @Test
    fun bulkRequestPickup_shouldPartialSuccessThereIsFailed() = runBlocking {
        val orderIds = listOf("0", "1", "2")
        val batchId = "1234566"
        val somListBulkRequestPickupUiModel = SomListBulkRequestPickupUiModel.Data(totalOnProcess = 5, jobId = batchId)
        val multiShippingStatusUiModel = MultiShippingStatusUiModel(fail = 2, success = 3)
        retryRequestPickup.set(viewModel, 10)

        coEvery {
            bulkRequestPickupUseCase.executeOnBackground()
        } returns SomListBulkRequestPickupUiModel(somListBulkRequestPickupUiModel)

        coEvery {
            multiShippingStatusUseCase.executeOnBackground()
        } returns multiShippingStatusUiModel

        viewModel.bulkRequestPickupFinalResultMediator.observe( {lifecycle}) {}

        viewModel.bulkRequestPickup(orderIds)

        coVerify {
            bulkRequestPickupUseCase.setParams(orderIds)
            bulkRequestPickupUseCase.executeOnBackground()
            multiShippingStatusUseCase.executeOnBackground()
        }

        assert(viewModel.bulkRequestPickupFinalResult.observeAwaitValue() is PartialSuccess)
    }

    @Test
    fun bulkRequestPickup_shouldPartialSuccessThereNotEligiblee() = runBlocking {
        val orderIds = listOf("0", "1", "2", "4", "5")
        val batchId = "1234566"
        val failList = mutableListOf<SomListBulkRequestPickupUiModel.ErrorBulkRequestPickup>().apply {
            add(SomListBulkRequestPickupUiModel.ErrorBulkRequestPickup(orderId = "166760344"))
            add(SomListBulkRequestPickupUiModel.ErrorBulkRequestPickup(orderId = "166760347"))
        }
        val somListBulkRequestPickupUiModel = SomListBulkRequestPickupUiModel.Data(totalOnProcess = 4, jobId = batchId)
        val multiShippingStatusUiModel = MultiShippingStatusUiModel(success = 4)
        retryRequestPickup.set(viewModel, 10)

        coEvery {
            bulkRequestPickupUseCase.executeOnBackground()
        } returns SomListBulkRequestPickupUiModel(data = somListBulkRequestPickupUiModel, errors = failList)

        coEvery {
            multiShippingStatusUseCase.executeOnBackground()
        } returns multiShippingStatusUiModel

        viewModel.bulkRequestPickupFinalResultMediator.observe( {lifecycle}) {}

        viewModel.bulkRequestPickup(orderIds)

        coVerify {
            bulkRequestPickupUseCase.setParams(orderIds)
            bulkRequestPickupUseCase.executeOnBackground()
            multiShippingStatusUseCase.executeOnBackground()
        }

        assert(viewModel.bulkRequestPickupFinalResult.observeAwaitValue() is PartialSuccessNotEligible)
    }

    @Test
    fun bulkRequestPickup_shouldPartialSuccessThereIsNotEligibleAndFailed() = runBlocking {
        val orderIds = listOf("0", "1", "2", "4", "5", "6", "7")
        val batchId = "1234566"
        val failList = mutableListOf<SomListBulkRequestPickupUiModel.ErrorBulkRequestPickup>().apply {
            add(SomListBulkRequestPickupUiModel.ErrorBulkRequestPickup(orderId = "166760344"))
            add(SomListBulkRequestPickupUiModel.ErrorBulkRequestPickup(orderId = "166760344"))
        }
        val somListBulkRequestPickupUiModel = SomListBulkRequestPickupUiModel.Data(totalOnProcess = 5, jobId = batchId)
        val multiShippingStatusUiModel = MultiShippingStatusUiModel(fail = 2, success = 3)
        retryRequestPickup.set(viewModel, 11)

        coEvery {
            bulkRequestPickupUseCase.executeOnBackground()
        } returns SomListBulkRequestPickupUiModel(data = somListBulkRequestPickupUiModel, errors = failList)

        coEvery {
            multiShippingStatusUseCase.executeOnBackground()
        } returns multiShippingStatusUiModel

        viewModel.bulkRequestPickupFinalResultMediator.observe( {lifecycle}) {}

        viewModel.bulkRequestPickup(orderIds)

        coVerify {
            bulkRequestPickupUseCase.setParams(orderIds)
            bulkRequestPickupUseCase.executeOnBackground()
            multiShippingStatusUseCase.executeOnBackground()
        }

        assert(viewModel.bulkRequestPickupFinalResult.observeAwaitValue() is PartialSuccessNotEligibleFail)
    }

    @Test
    fun bulkRequestPickup_shouldNotEligibleAndAllFailed() = runBlocking {
        val orderIds = listOf("0", "1", "2", "4", "5", "6", "7")
        val batchId = "1234566"
        val failList = mutableListOf<SomListBulkRequestPickupUiModel.ErrorBulkRequestPickup>().apply {
            add(SomListBulkRequestPickupUiModel.ErrorBulkRequestPickup(orderId = "166760344"))
            add(SomListBulkRequestPickupUiModel.ErrorBulkRequestPickup(orderId = "166760344"))
        }
        val somListBulkRequestPickupUiModel = SomListBulkRequestPickupUiModel.Data(totalOnProcess = 5, jobId = batchId)
        val multiShippingStatusUiModel = MultiShippingStatusUiModel(fail = 5)
        retryRequestPickup.set(viewModel, 11)

        coEvery {
            bulkRequestPickupUseCase.executeOnBackground()
        } returns SomListBulkRequestPickupUiModel(data = somListBulkRequestPickupUiModel, errors = failList)

        coEvery {
            multiShippingStatusUseCase.executeOnBackground()
        } returns multiShippingStatusUiModel

        viewModel.bulkRequestPickupFinalResultMediator.observe( {lifecycle}) {}

        viewModel.bulkRequestPickup(orderIds)

        coVerify {
            bulkRequestPickupUseCase.setParams(orderIds)
            bulkRequestPickupUseCase.executeOnBackground()
            multiShippingStatusUseCase.executeOnBackground()
        }

        assert(viewModel.bulkRequestPickupFinalResult.observeAwaitValue() is NotEligibleAndFail)
    }

    @Test
    fun bulkRequestPickup_shouldAllValidationFail() = runBlocking {
        val orderIds = listOf("0", "1", "2", "4", "5", "6", "7")
        val batchId = "1234566"
        val somListBulkRequestPickupUiModel = SomListBulkRequestPickupUiModel.Data(jobId = batchId)
        val multiShippingStatusUiModel = MultiShippingStatusUiModel()
        retryRequestPickup.set(viewModel, 11)

        coEvery {
            bulkRequestPickupUseCase.executeOnBackground()
        } returns SomListBulkRequestPickupUiModel(data = somListBulkRequestPickupUiModel, status = BulkRequestPickupStatus.SUCCESS_NOT_PROCESSED)

        coEvery {
            multiShippingStatusUseCase.executeOnBackground()
        } returns multiShippingStatusUiModel

        viewModel.bulkRequestPickupFinalResultMediator.observe( {lifecycle}) {}

        viewModel.bulkRequestPickup(orderIds)

        coVerify {
            bulkRequestPickupUseCase.setParams(orderIds)
            bulkRequestPickupUseCase.executeOnBackground()
            multiShippingStatusUseCase.executeOnBackground()
        }

        assert(viewModel.bulkRequestPickupFinalResult.observeAwaitValue() is AllValidationFail)
    }

    @Test
    fun bulkRequestPickup_shouldAllFailEligible() = runBlocking {
        val orderIds = listOf("0", "1", "2", "5", "6", "7")
        val batchId = "1234566"
        val somListBulkRequestPickupUiModel = SomListBulkRequestPickupUiModel.Data(jobId = batchId, totalOnProcess = 6)
        val multiShippingStatusUiModel = MultiShippingStatusUiModel(fail = 6, total_order = 6, success = 0)
        retryRequestPickup.set(viewModel, 11)

        coEvery {
            bulkRequestPickupUseCase.executeOnBackground()
        } returns SomListBulkRequestPickupUiModel(data = somListBulkRequestPickupUiModel, errors = emptyList(), status = null)

        coEvery {
            multiShippingStatusUseCase.executeOnBackground()
        } returns multiShippingStatusUiModel

        viewModel.bulkRequestPickupFinalResultMediator.observe( {lifecycle}) {}

        viewModel.bulkRequestPickup(orderIds)

        coVerify {
            bulkRequestPickupUseCase.setParams(orderIds)
            bulkRequestPickupUseCase.executeOnBackground()
            multiShippingStatusUseCase.executeOnBackground()
        }

        assert(viewModel.bulkRequestPickupFinalResult.observeAwaitValue() is AllFailEligible)
    }

    @Test
    fun bulkRequestPickup_shouldAllNotEligible() = runBlocking {
        val orderIds = listOf("0", "1", "2", "5", "6", "7")
        val batchId = "1234566"
        val somListBulkRequestPickupUiModel = SomListBulkRequestPickupUiModel.Data(jobId = batchId)
        val multiShippingStatusUiModel = MultiShippingStatusUiModel(fail = 0, success = 0)
        val failList = mutableListOf<SomListBulkRequestPickupUiModel.ErrorBulkRequestPickup>().apply {
            add(SomListBulkRequestPickupUiModel.ErrorBulkRequestPickup(orderId = "0"))
            add(SomListBulkRequestPickupUiModel.ErrorBulkRequestPickup(orderId = "1"))
            add(SomListBulkRequestPickupUiModel.ErrorBulkRequestPickup(orderId = "2"))
            add(SomListBulkRequestPickupUiModel.ErrorBulkRequestPickup(orderId = "5"))
            add(SomListBulkRequestPickupUiModel.ErrorBulkRequestPickup(orderId = "6"))
            add(SomListBulkRequestPickupUiModel.ErrorBulkRequestPickup(orderId = "7"))
        }
        retryRequestPickup.set(viewModel, 11)

        coEvery {
            bulkRequestPickupUseCase.executeOnBackground()
        } returns SomListBulkRequestPickupUiModel(data = somListBulkRequestPickupUiModel, errors = failList)

        coEvery {
            multiShippingStatusUseCase.executeOnBackground()
        } returns multiShippingStatusUiModel

        viewModel.bulkRequestPickupFinalResultMediator.observe( {lifecycle}) {}

        viewModel.bulkRequestPickup(orderIds)

        coVerify {
            bulkRequestPickupUseCase.setParams(orderIds)
            bulkRequestPickupUseCase.executeOnBackground()
            multiShippingStatusUseCase.executeOnBackground()
        }

        assert(viewModel.bulkRequestPickupFinalResult.observeAwaitValue() is AllNotEligible)
    }

    @Test
    fun bulkRequestPickup_shouldFailRetry() = runBlocking {
        val orderIds = listOf("0", "1", "2", "4", "5", "6", "7")
        val batchId = "1234566"
        val somListBulkRequestPickupUiModel = SomListBulkRequestPickupUiModel.Data(jobId = batchId)
        val multiShippingStatusUiModel = MultiShippingStatusUiModel()
        retryRequestPickup.set(viewModel, 11)

        coEvery {
            bulkRequestPickupUseCase.executeOnBackground()
        } returns SomListBulkRequestPickupUiModel(data = somListBulkRequestPickupUiModel)

        coEvery {
            multiShippingStatusUseCase.executeOnBackground()
        } returns multiShippingStatusUiModel

        viewModel.bulkRequestPickupFinalResultMediator.observe( {lifecycle}) {}

        viewModel.bulkRequestPickup(orderIds)

        coVerify {
            bulkRequestPickupUseCase.setParams(orderIds)
            bulkRequestPickupUseCase.executeOnBackground()
            multiShippingStatusUseCase.executeOnBackground()
        }

        assert(viewModel.bulkRequestPickupFinalResult.observeAwaitValue() is FailRetry)
    }

    @Test
    fun getTickers_shouldSuccess() {
        val ticker = mockk<TickerData>(relaxed = true)
        coEvery {
            somListGetTickerUseCase.executeOnBackground()
        } returns listOf(ticker)

        viewModel.getTickers()

        coVerify {
            somListGetTickerUseCase.executeOnBackground()
        }
        val tickerResult = viewModel.tickerResult.observeAwaitValue()
        assert(tickerResult is Success && tickerResult.data.isNotEmpty() && tickerResult.data.firstOrNull() == ticker)
    }

    @Test
    fun getTickers_shouldFailed() {
        coEvery {
            somListGetTickerUseCase.executeOnBackground()
        } throws Throwable()

        viewModel.getTickers()

        coVerify {
            somListGetTickerUseCase.executeOnBackground()
        }

        assert(viewModel.tickerResult.observeAwaitValue() is Fail)
    }

    @Test
    fun getFiltersFromCacheAndCloud_shouldSuccess() {
        coEvery {
            somListGetFilterListUseCase.executeOnBackground(any())
        } answers {
            SomListFilterUiModel(emptyList(), fromCache = args.first() as Boolean)
        }

        every {
            somListGetFilterListUseCase.isFirstLoad
        } returns true

        viewModel.getFilters(true)

        coVerify(exactly = 1) {
            somListGetFilterListUseCase.executeOnBackground(true)
            somListGetFilterListUseCase.executeOnBackground(false)
        }
    }

    @Test
    fun getFiltersFromCloud_shouldSuccess() {
        coEvery {
            somListGetFilterListUseCase.executeOnBackground(any())
        } answers {
            SomListFilterUiModel(emptyList(), fromCache = args.first() as Boolean)
        }

        every {
            somListGetFilterListUseCase.isFirstLoad
        } returns false

        viewModel.getFilters(true)

        coVerify(exactly = 1) {
            somListGetFilterListUseCase.executeOnBackground(false)
        }

        val result = viewModel.filterResult.observeAwaitValue()

        assert(result is Success && !result.data.fromCache)
    }

    @Test
    fun getFiltersFromCacheAndCloud_shouldFailed() {
        coEvery {
            somListGetFilterListUseCase.executeOnBackground(any())
        } throws Throwable()

        every {
            somListGetFilterListUseCase.isFirstLoad
        } returns true

        viewModel.getFilters(true)

        coVerify(exactly = 1) {
            somListGetFilterListUseCase.executeOnBackground(true)
            somListGetFilterListUseCase.executeOnBackground(false)
        }

        assert(viewModel.filterResult.observeAwaitValue() is Fail)
    }

    @Test
    fun getFiltersFromCloud_shouldFailed() {
        coEvery {
            somListGetFilterListUseCase.executeOnBackground(any())
        } throws Throwable()

        every {
            somListGetFilterListUseCase.isFirstLoad
        } returns false

        viewModel.getFilters(true)

        coVerify(exactly = 1) {
            somListGetFilterListUseCase.executeOnBackground(false)
        }

        assert(viewModel.filterResult.observeAwaitValue() is Fail)
    }

    @Test
    fun getFilters_shouldNotSuccess_whenCannotShowOrderData() {
        coEvery {
            somListGetFilterListUseCase.executeOnBackground(any())
        } returns SomListFilterUiModel(fromCache = false)

        somCanShowOrderDataField.set(viewModel, MediatorLiveData<Boolean>().apply { value = false })

        viewModel.getFilters(false)

        coVerify(exactly = 0) {
            somListGetFilterListUseCase.executeOnBackground(any())
        }

        assertFalse(viewModel.filterResult.observeAwaitValue() is Fail)
    }

    @Test
    fun getWaitingPaymentCounter_shouldSuccess() {
        val waitingPaymentCounterData = WaitingPaymentCounter()
        coEvery {
            somListGetWaitingPaymentUseCase.executeOnBackground()
        } returns waitingPaymentCounterData

        viewModel.getWaitingPaymentCounter()

        coVerify {
            somListGetWaitingPaymentUseCase.executeOnBackground()
        }
        val waitingPaymentCounterResult = viewModel.waitingPaymentCounterResult.observeAwaitValue()
        assert(waitingPaymentCounterResult is Success && waitingPaymentCounterResult.data == waitingPaymentCounterData)
    }

    @Test
    fun getWaitingPaymentCounter_shouldFailed() {
        coEvery {
            somListGetWaitingPaymentUseCase.executeOnBackground()
        } throws Throwable()

        viewModel.getWaitingPaymentCounter()

        coVerify {
            somListGetWaitingPaymentUseCase.executeOnBackground()
        }

        assert(viewModel.waitingPaymentCounterResult.observeAwaitValue() is Fail)
    }

    @Test
    fun getWaitingPaymentCounter_shouldNotSuccess_whenCannotShowOrderData() {
        coEvery {
            somListGetWaitingPaymentUseCase.executeOnBackground(any())
        } returns WaitingPaymentCounter()

        somCanShowOrderDataField.set(viewModel, MediatorLiveData<Boolean>().apply { value = false })

        viewModel.getWaitingPaymentCounter()

        coVerify(exactly = 0) {
            somListGetWaitingPaymentUseCase.executeOnBackground(any())
        }

        assertFalse(viewModel.waitingPaymentCounterResult.observeAwaitValue() is Success)
    }
    @Test
    fun getOrderList_shouldSuccessAndClearAllFailedRefreshOrder() {
        val orderId = "1234567890"
        val invoice = "INV/20200922/XX/IX/123456789"
        var counter = 0
        viewModel.resetNextOrderId()

        coEvery {
            somListGetOrderListUseCase.executeOnBackground(any())
        } answers {
            if (counter++ == 0) {
                throw Throwable()
            } else {
                ("0" to listOf())
            }
        }

        viewModel.refreshSelectedOrder(orderId, invoice)
        somGetOrderListJobField.set(viewModel, null)
        viewModel.getOrderList()

        coVerify {
            somListGetOrderListUseCase.executeOnBackground(any())
        }

        assert(viewModel.orderListResult.observeAwaitValue() is Success && !viewModel.hasNextPage() && !viewModel.containsFailedRefreshOrder)
        assert(viewModel.isLoadingOrder.observeAwaitValue() == false)
    }

    @Test
    fun getOrderList_shouldSuccessNotClearAllFailedRefreshOrder() {
        val orderId = "1234567890"
        val invoice = "INV/20200922/XX/IX/123456789"
        var counter = 0
        viewModel.getDataOrderListParams().nextOrderId = 100L

        coEvery {
            somListGetOrderListUseCase.executeOnBackground(any())
        } answers {
            if (counter++ == 0) {
                throw Throwable()
            } else {
                ("0" to listOf())
            }
        }

        viewModel.refreshSelectedOrder(orderId, invoice)
        somGetOrderListJobField.set(viewModel, null)
        viewModel.getOrderList()

        coVerify {
            somListGetOrderListUseCase.executeOnBackground(any())
        }

        assert(viewModel.orderListResult.observeAwaitValue() is Success && !viewModel.hasNextPage() && viewModel.containsFailedRefreshOrder)
        assert(viewModel.isLoadingOrder.observeAwaitValue() == false)
    }

    @Test
    fun getOrderList_shouldCancelOldJobAndSuccess() {
        val getOrderListJob = mockk<Job>(relaxed = true)
        coEvery {
            somListGetOrderListUseCase.executeOnBackground(any())
        } returns ("0" to listOf())

        somGetOrderListJobField.set(viewModel, getOrderListJob)
        viewModel.getOrderList()

        coVerify {
            somListGetOrderListUseCase.executeOnBackground(any())
            getOrderListJob.cancel()
        }

        assert(viewModel.orderListResult.observeAwaitValue() is Success && !viewModel.hasNextPage())
    }

    @Test
    fun getOrderList_shouldFailed() {
        coEvery {
            somListGetOrderListUseCase.executeOnBackground(any())
        } throws Throwable()

        viewModel.getOrderList()

        coVerify {
            somListGetOrderListUseCase.executeOnBackground(any())
        }

        assert(viewModel.orderListResult.observeAwaitValue() is Fail)
        assert(viewModel.isLoadingOrder.observeAwaitValue() == false)
    }

    @Test
    fun getOrderList_shouldNotSuccess_whenCannotShowOrderData() {
        coEvery {
            somListGetOrderListUseCase.executeOnBackground(any())
        } returns ("0" to listOf())

        somCanShowOrderDataField.set(viewModel, MediatorLiveData<Boolean>().apply { value = false })

        viewModel.getOrderList()

        coVerify(exactly = 0) {
            somListGetOrderListUseCase.executeOnBackground(any())
        }

        assertFalse(viewModel.orderListResult.observeAwaitValue() is Success)
    }

    @Test
    fun refreshSelectedOrder_shouldSuccess() {
        val orderId = "1234567890"
        val invoice = "INV/20200922/XX/IX/123456789"
        val order = SomListOrderUiModel(searchParam = invoice, orderId = orderId)

        coEvery {
            somListGetOrderListUseCase.executeOnBackground(any())
        } returns ("0" to listOf(order))

        viewModel.refreshSelectedOrder(orderId, invoice)

        coVerify {
            somListGetOrderListUseCase.executeOnBackground(any())
        }

        val refreshOrderResult = viewModel.refreshOrderResult.observeAwaitValue()
        assert(refreshOrderResult is Success && refreshOrderResult.data.orderId == orderId && refreshOrderResult.data.order == order)
        assert(viewModel.isLoadingOrder.observeAwaitValue() == false)
    }

    @Test
    fun refreshSelectedOrder_shouldFailed() {
        val orderId = "1234567890"
        val invoice = "INV/20200922/XX/IX/123456789"

        coEvery {
            somListGetOrderListUseCase.executeOnBackground(any())
        } throws Throwable()

        viewModel.refreshSelectedOrder(orderId, invoice)

        coVerify {
            somListGetOrderListUseCase.executeOnBackground(any())
        }
        val refreshOrderResult = viewModel.refreshOrderResult.observeAwaitValue()
        assert(refreshOrderResult is Fail)
        assert(viewModel.containsFailedRefreshOrder)
        assert(viewModel.isLoadingOrder.observeAwaitValue() == false)
    }

    @Test
    fun refreshSelectedOrder_shouldNotRefreshingWhenFetchingInitialOrderList() {
        val orderId = "1234567890"
        val invoice = "INV/20200922/XX/IX/123456789"
        val order = SomListOrderUiModel(searchParam = invoice, orderId = orderId)

        every {
            viewModel.isRefreshingAllOrder()
        } returns true
        coEvery {
            somListGetOrderListUseCase.executeOnBackground(any())
        } returns ("0" to listOf(order))

        viewModel.refreshSelectedOrder(orderId, invoice)

        coVerify(inverse = true) {
            somListGetOrderListUseCase.executeOnBackground(any())
        }
        val refreshOrderResult = viewModel.refreshOrderResult.observeAwaitValue()
        assert(refreshOrderResult == null)
        assert(viewModel.isLoadingOrder.observeAwaitValue() == null)
    }

    @Test
    fun retryRefreshSelectedOrder_shouldSuccess() {
        val somRefreshOrderJob1 = mockk<Job>(relaxed = true)
        val somRefreshOrderJob2 = mockk<Job>(relaxed = true)
        val orderId1 = "1234567890"
        val orderId2 = "0987654321"
        val invoice1 = "INV/20200922/XX/IX/123456789"
        val invoice2 = "INV/20200922/XX/IX/123456790"
        val refreshOrder1 = RefreshOrder(orderId1, invoice1, somRefreshOrderJob1)
        val refreshOrder2 = RefreshOrder(orderId2, invoice2, somRefreshOrderJob2)
        somRefreshOrderJobField.set(viewModel, arrayListOf(refreshOrder1, refreshOrder2))

        every {
            somRefreshOrderJob1.isCompleted
        } returns true
        every {
            somRefreshOrderJob2.isCompleted
        } returns true

        viewModel.retryRefreshSelectedOrder()

        verify(exactly = 1) {
            viewModel.refreshSelectedOrder(orderId1, invoice1)
            viewModel.refreshSelectedOrder(orderId2, invoice2)
        }
    }

    @Test
    fun retryRefreshSelectedOrder_shouldNotRetryWhenThereIsNoFailedJob() {
        val somRefreshOrderJob1 = mockk<Job>(relaxed = true)
        val orderId1 = "1234567890"
        val invoice1 = "INV/20200922/XX/IX/123456789"
        val refreshOrder1 = RefreshOrder(orderId1, invoice1, somRefreshOrderJob1)
        somRefreshOrderJobField.set(viewModel, arrayListOf(refreshOrder1))

        every {
            somRefreshOrderJob1.isCompleted
        } returns false

        viewModel.retryRefreshSelectedOrder()

        verify(inverse = true) {
            viewModel.refreshSelectedOrder(any(), any())
        }
    }

    @Test
    fun getTopAdsCategory_shouldSuccess() {
        doGetTopAdsCategory_shouldSuccess()
    }

    @Test
    fun getTopAdsCategory_shouldFailed() {
        coEvery {
            somListGetTopAdsCategoryUseCase.executeOnBackground()
        } throws Throwable()

        viewModel.getTopAdsCategory()

        coVerify {
            somListGetTopAdsCategoryUseCase.executeOnBackground()
        }

        assert(viewModel.topAdsCategoryResult.observeAwaitValue() is Fail)
    }

    @Test
    fun getAdminPermission_shouldSuccess() {
        coEvery {
            authorizeAccessUseCase.execute(any())
        } returns true
        coEvery {
            authorizeMultiAcceptAccessUseCase.execute(any())
        } returns true
        coEvery {
            userSessionInterface.isShopOwner
        } returns false
        coEvery {
            userSessionInterface.isShopAdmin
        } returns true

        viewModel.getAdminPermission()

        coVerify {
            authorizeAccessUseCase.execute(any())
        }

        assert(viewModel.isOrderManageEligible.observeAwaitValue() is Success)
    }

    @Test
    fun getAdminPermission_shouldFail() {
        coEvery {
            authorizeAccessUseCase.execute(any())
        } throws Throwable()
        coEvery {
            userSessionInterface.isShopOwner
        } returns false
        coEvery {
            userSessionInterface.isShopAdmin
        } returns true

        viewModel.getAdminPermission()

        coVerify {
            authorizeAccessUseCase.execute(any())
        }

        assert(viewModel.isOrderManageEligible.observeAwaitValue() is Fail)
    }

    @Test
    fun getAdminPermission_whenIsShopOwner_shouldNotRunAndUserShouldEligible() {
        coEvery {
            userSessionInterface.isShopOwner
        } returns true

        viewModel.getAdminPermission()

        coVerify(exactly = 0) {
            authorizeAccessUseCase.execute(any())
        }
        assert((viewModel.isOrderManageEligible.value as? Success)?.data?.first == true)
    }

    @Test
    fun getAdminPermission_whenIsNotShopOwnerButIsShopAdmin_shouldRun() {
        coEvery {
            userSessionInterface.isShopOwner
        } returns false
        coEvery {
            userSessionInterface.isShopAdmin
        } returns true

        viewModel.getAdminPermission()

        coVerify {
            authorizeAccessUseCase.execute(any())
        }
    }

    @Test
    fun isTopAdsActive_shouldReturnFalseWhenSuccess() {
        doGetTopAdsCategory_shouldSuccess()

        assert(!viewModel.isTopAdsActive())
    }

    @Test
    fun isTopAdsActive_shouldReturnFalse() {
        doGetTopAdsCategory_shouldSuccess(2)

        assert(!viewModel.isTopAdsActive())
    }

    @Test
    fun isTopAdsActive_shouldReturnFalseWhenFailed() {
        getTopAdsCategory_shouldFailed()

        assert(!viewModel.isTopAdsActive())
    }

    @Test
    fun isTopAdsActive_shouldReturnFalseWhenValueIsNull() {
        assert(!viewModel.isTopAdsActive())
    }

    @Test
    fun isTopAdsActive_shouldReturnTrueWhenUsingManualAds() {
        doGetTopAdsCategory_shouldSuccess(3)

        assert(viewModel.isTopAdsActive())
    }

    @Test
    fun isTopAdsActive_shouldReturnTrueWhenUsingAutoAds() {
        doGetTopAdsCategory_shouldSuccess(4)

        assert(viewModel.isTopAdsActive())
    }

    @Test
    fun setStatusOrderFilterTest() {
        val statusOrderIds = listOf(220, 400)
        setGetDataOrderListParams()
        viewModel.setStatusOrderFilter(statusOrderIds)

        assert(viewModel.getDataOrderListParams().statusList == statusOrderIds)
    }

    @Test
    fun resetGetOrderListParamTest() {
        val somListGetOrderListParam = SomListGetOrderListParam()
        setGetDataOrderListParams()

        viewModel.resetGetOrderListParam()

        assert(viewModel.getDataOrderListParams() == somListGetOrderListParam)
    }

    @Test
    fun hasNextPage_shouldReturnFalse() {
        viewModel.resetGetOrderListParam()
        assert(!viewModel.hasNextPage())
    }

    @Test
    fun hasNextPage_shouldReturnTrue() {
        setGetDataOrderListParams()
        assert(viewModel.hasNextPage())
    }

    @Test
    fun setOrderTypeFilterTest() {
        val orderTypes = mutableSetOf(1, 2, 3, 4, 5)
        setGetDataOrderListParams()
        viewModel.setOrderTypeFilter(orderTypes)
        assert(viewModel.getDataOrderListParams().orderTypeList == orderTypes)
    }

    @Test
    fun setSortOrderByTest() {
        val sortBy = SomConsts.SORT_BY_TOTAL_OPEN_DESCENDING
        setGetDataOrderListParams()
        viewModel.setSortOrderBy(sortBy)
        assert(viewModel.getDataOrderListParams().sortBy == sortBy)
    }

    @Test
    fun setIsMultiSelectEnabledTest() {
        val mockMultiSelectEnabled = true
        viewModel.isMultiSelectEnabled = mockMultiSelectEnabled
        assert(viewModel.isMultiSelectEnabled == mockMultiSelectEnabled)
    }

    @Test
    fun setSearchParamTest() {
        val searchParam = "Produk pembasmi hama"
        viewModel.setSearchParam(searchParam)
        assert(viewModel.getDataOrderListParams().search == searchParam)
    }

    @Test
    fun isRefreshingAllOrder_shouldReturnFalseWhenJobIsNull() {
        somGetOrderListJobField.set(viewModel, null)
        assert(!viewModel.isRefreshingAllOrder())
    }

    @Test
    fun isRefreshingAllOrder_shouldReturnFalseWhenJobIsCompleted() {
        val somGetOrderListJob = mockk<Job>(relaxed = true)
        somGetOrderListJobField.set(viewModel, somGetOrderListJob)

        every {
            somGetOrderListJob.isCompleted
        } returns true

        assert(!viewModel.isRefreshingAllOrder())
    }

    @Test
    fun isRefreshingAllOrder_shouldReturnTrueWhenJobIsNotYetComplete() {
        val somGetOrderListJob = mockk<Job>(relaxed = true)
        somGetOrderListJobField.set(viewModel, somGetOrderListJob)

        every {
            somGetOrderListJob.isCompleted
        } returns false

        assert(viewModel.isRefreshingAllOrder())
    }

    @Test
    fun isRefreshingSelectedOrder_shouldReturnFalseWhenThereIsNoRunningJob() {
        val somRefreshOrderJob = mockk<Job>(relaxed = true)
        val somRefreshOrder = RefreshOrder("", "", somRefreshOrderJob)
        somRefreshOrderJobField.set(viewModel, arrayListOf(somRefreshOrder))

        every {
            somRefreshOrderJob.isCompleted
        } returns true
        every {
            somRefreshOrderJob.isCancelled
        } returns true

        assert(!viewModel.isRefreshingSelectedOrder())
    }

    @Test
    fun isRefreshingSelectedOrder_shouldReturnTrueWhenThereIsAnyRunningJob() {
        val somRefreshOrderJob = mockk<Job>(relaxed = true)
        val somRefreshOrder = RefreshOrder("", "", somRefreshOrderJob)
        somRefreshOrderJobField.set(viewModel, arrayListOf(somRefreshOrder))

        every {
            somRefreshOrderJob.isCompleted
        } returns false

        assert(viewModel.isRefreshingSelectedOrder())
    }

    @Test
    fun isRefreshingOrder_shouldReturnFalseWhenNotRefreshingOrderAndNotFetchingOrderList() {
        isRefreshingAllOrder_shouldReturnFalseWhenJobIsCompleted()
        isRefreshingSelectedOrder_shouldReturnFalseWhenThereIsNoRunningJob()
        assert(!viewModel.isRefreshingOrder())
    }

    @Test
    fun isRefreshingOrder_shouldReturnTrueWhenFetchingOrderList() {
        isRefreshingAllOrder_shouldReturnTrueWhenJobIsNotYetComplete()
        isRefreshingSelectedOrder_shouldReturnFalseWhenThereIsNoRunningJob()
        assert(viewModel.isRefreshingOrder())
    }

    @Test
    fun isRefreshingOrder_shouldReturnTrueWhenRefreshingOrder() {
        isRefreshingAllOrder_shouldReturnFalseWhenJobIsCompleted()
        isRefreshingSelectedOrder_shouldReturnTrueWhenThereIsAnyRunningJob()
        assert(viewModel.isRefreshingOrder())
    }

    @Test
    fun isRefreshingOrder_shouldReturnTrueWhenRefreshingOrderAndFetchingOrderList() {
        isRefreshingAllOrder_shouldReturnTrueWhenJobIsNotYetComplete()
        isRefreshingSelectedOrder_shouldReturnTrueWhenThereIsAnyRunningJob()
        assert(viewModel.isRefreshingOrder())
    }

    @Test
    fun isOrderStatusIdsChanged_shouldReturnFalse() {
        viewModel.setStatusOrderFilter(listOf(1, 2, 3, 4, 5))
        val isChanged = viewModel.isOrderStatusIdsChanged(listOf(1, 2, 3, 4, 5))
        assertFalse(isChanged)
    }

    @Test
    fun isOrderStatusIdsChanged_shouldReturnTrue() {
        viewModel.setStatusOrderFilter(listOf())
        val isChanged = viewModel.isOrderStatusIdsChanged(listOf(1, 2, 3, 4, 5))
        assertTrue(isChanged)
    }
}