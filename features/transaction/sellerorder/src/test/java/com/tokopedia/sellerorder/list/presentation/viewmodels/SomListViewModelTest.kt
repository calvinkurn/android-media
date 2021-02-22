package com.tokopedia.sellerorder.list.presentation.viewmodels

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.sellerorder.common.SomOrderBaseViewModelTest
import com.tokopedia.sellerorder.common.domain.usecase.*
import com.tokopedia.sellerorder.common.presenter.model.Roles
import com.tokopedia.sellerorder.common.presenter.model.SomGetUserRoleUiModel
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.list.domain.model.SomListGetOrderListParam
import com.tokopedia.sellerorder.list.domain.usecases.*
import com.tokopedia.sellerorder.list.presentation.models.*
import com.tokopedia.sellerorder.util.observeAwaitValue
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.lang.reflect.Field

class SomListViewModelTest: SomOrderBaseViewModelTest<SomListViewModel>() {
    @RelaxedMockK
    lateinit var getUserRoleUseCase: SomGetUserRoleUseCase

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
    lateinit var bulkAcceptOrderUseCase: SomListBulkAcceptOrderUseCase

    private lateinit var somGetOrderListJobField: Field

    private lateinit var somGetFiltersJobField: Field
    private lateinit var somRefreshOrderJobField: Field

    private val dispatcher: CoroutineDispatchers = CoroutineTestDispatchersProvider

    @Before
    override fun setUp() {
        super.setUp()
        viewModel = spyk(SomListViewModel(getUserRoleUseCase, somAcceptOrderUseCase,
                somRejectOrderUseCase, somRejectCancelOrderUseCase, somEditRefNumUseCase, userSessionInterface,
                dispatcher, somListGetTickerUseCase, somListGetFilterListUseCase, somListGetWaitingPaymentUseCase,
                somListGetOrderListUseCase, somListGetTopAdsCategoryUseCase, bulkAcceptOrderStatusUseCase,
                bulkAcceptOrderUseCase))

        somGetOrderListJobField = viewModel::class.java.getDeclaredField("getOrderListJob").apply {
            isAccessible = true
        }
        somGetFiltersJobField = viewModel::class.java.getDeclaredField("getFiltersJob").apply {
            isAccessible = true
        }
        somRefreshOrderJobField = viewModel::class.java.getDeclaredField("refreshOrderJobs").apply {
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
            viewModel.refreshSelectedOrder(orderId, invoice)
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
    fun getFilters_shouldSuccess() {
        val tickerData = SomListFilterUiModel()
        coEvery {
            somListGetFilterListUseCase.executeOnBackground()
        } returns tickerData

        viewModel.getFilters(true)

        coVerify {
            somListGetFilterListUseCase.executeOnBackground()
        }
        val filterResult = viewModel.filterResult.observeAwaitValue()
        assert(filterResult is Success && filterResult.data == tickerData)
    }

    @Test
    fun getFilters_shouldSuccessAndCancelActiveJob() {
        val somGetFiltersJob = mockk<Job>(relaxed = true)
        somGetFiltersJobField.set(viewModel, somGetFiltersJob)

        every {
            somGetFiltersJob.cancel()
        } just runs

        getFilters_shouldSuccess()

        verify(exactly = 1) {
            somGetFiltersJob.cancel()
        }
    }

    @Test
    fun getFilters_shouldFailed() {
        coEvery {
            somListGetFilterListUseCase.executeOnBackground()
        } throws Throwable()

        viewModel.getFilters(true)

        coVerify {
            somListGetFilterListUseCase.executeOnBackground()
        }

        assert(viewModel.filterResult.observeAwaitValue() is Fail)
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
    fun getUserRoles_shouldSuccess() {
        coEvery {
            getUserRoleUseCase.execute()
        } returns SomGetUserRoleUiModel()

        viewModel.getUserRoles()

        coVerify { getUserRoleUseCase.execute() }

        assert(viewModel.userRoleResult.observeAwaitValue() is Success)
    }

    @Test
    fun getUserRoles_shouldFailed() {
        coEvery {
            getUserRoleUseCase.execute()
        } throws Throwable()

        viewModel.getUserRoles()

        coVerify { getUserRoleUseCase.execute() }

        assert(viewModel.userRoleResult.observeAwaitValue() is Fail)
    }

    @Test
    fun loadUserRoles_shouldNeverSendRequestWhenAnotherLoadUserRolesRequestIsOnProgress() = runBlocking {
        val getUserRolesJob = mockk<Job>()

        every {
            getUserRolesJob.isCompleted
        } returns false

        viewModel.setUserRolesJob(getUserRolesJob)
        viewModel.getUserRoles()

        coVerify(inverse = true) {
            getUserRoleUseCase.execute()
        }

        assert(viewModel.userRoleResult.observeAwaitValue() == null)
    }

    @Test
    fun loadUserRoles_shouldSuccessWhenAnotherLoadUserRolesRequestIsCompleted() = runBlocking {
        val getUserRolesJob = mockk<Job>()
        coEvery {
            getUserRoleUseCase.execute()
        } returns SomGetUserRoleUiModel()

        every {
            getUserRolesJob.isCompleted
        } returns true

        viewModel.setUserRolesJob(getUserRolesJob)
        viewModel.getUserRoles()

        coVerify {
            getUserRoleUseCase.execute()
        }

        assert(viewModel.userRoleResult.observeAwaitValue() is Success)
    }

    @Test
    fun getUserRolesTest() {
        val newUserRolesValue = SomGetUserRoleUiModel(listOf(Roles.MANAGE_SHOPSTATS))
        viewModel.setUserRoles(newUserRolesValue)
        val userRolesValue = viewModel.userRoleResult.observeAwaitValue()
        assert(userRolesValue is Success && userRolesValue.data == newUserRolesValue)
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
    fun clearUserRolesTest() {
        viewModel.clearUserRoles()

        assert(viewModel.userRoleResult.observeAwaitValue() == null)
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
}