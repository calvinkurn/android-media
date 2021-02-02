package com.tokopedia.sellerorder.list.presentation.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.sellerorder.SomTestDispatcherProvider
import com.tokopedia.sellerorder.common.SomDispatcherProvider
import com.tokopedia.sellerorder.common.domain.usecase.*
import com.tokopedia.sellerorder.common.presenter.model.Roles
import com.tokopedia.sellerorder.common.presenter.model.SomGetUserRoleUiModel
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.list.domain.model.SomListGetOrderListParam
import com.tokopedia.sellerorder.list.domain.usecases.*
import com.tokopedia.sellerorder.list.presentation.models.SomListBulkAcceptOrderStatusUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListBulkAcceptOrderUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListFilterUiModel
import com.tokopedia.sellerorder.list.presentation.models.WaitingPaymentCounter
import com.tokopedia.sellerorder.util.observeAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Field

class SomListViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getUserRoleUseCase: SomGetUserRoleUseCase

    @RelaxedMockK
    lateinit var somAcceptOrderUseCase: SomAcceptOrderUseCase

    @RelaxedMockK
    lateinit var somRejectOrderUseCase: SomRejectOrderUseCase

    @RelaxedMockK
    lateinit var somRejectCancelOrderRequest: SomRejectCancelOrderUseCase

    @RelaxedMockK
    lateinit var somEditRefNumUseCase: SomEditRefNumUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

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

    private val dispatcher: SomDispatcherProvider = SomTestDispatcherProvider()

    private lateinit var viewModel: SomListViewModel

    private lateinit var somGetOrderListJobField: Field

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = SomListViewModel(getUserRoleUseCase, somAcceptOrderUseCase,
                somRejectOrderUseCase, somRejectCancelOrderRequest, somEditRefNumUseCase, userSession,
                dispatcher, somListGetTickerUseCase, somListGetFilterListUseCase, somListGetWaitingPaymentUseCase,
                somListGetOrderListUseCase, somListGetTopAdsCategoryUseCase, bulkAcceptOrderStatusUseCase,
                bulkAcceptOrderUseCase)

        somGetOrderListJobField = viewModel::class.java.getDeclaredField("getOrderListJob").apply {
            isAccessible = true
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

        assert(viewModel.topAdsCategoryResult.observeAwaitValue() is Success)
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
            nextOrderId = 123456
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
            bulkAcceptOrderUseCase.setParams(orderIds, userSession.userId)
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
            bulkAcceptOrderUseCase.setParams(orderIds, userSession.userId)
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
            bulkAcceptOrderUseCase.setParams(orderIds, userSession.userId)
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
            bulkAcceptOrderUseCase.setParams(orderIds, userSession.userId)
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
            bulkAcceptOrderUseCase.setParams(orderIds, userSession.userId)
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
            bulkAcceptOrderUseCase.setParams(orderIds, userSession.userId)
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
            bulkAcceptOrderUseCase.setParams(orderIds, userSession.userId)
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
        viewModel.bulkAcceptOrderStatusResult.observeAwaitValue(time = 10)
        viewModel.retryGetBulkAcceptOrderStatus()
        viewModel.bulkAcceptOrderStatusResult.observeAwaitValue(time = 10)

        coVerify(ordering = Ordering.ORDERED) {
            bulkAcceptOrderUseCase.setParams(orderIds, userSession.userId)
            bulkAcceptOrderUseCase.executeOnBackground()
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        }

        assert(viewModel.bulkAcceptOrderStatusResult.value is Success)
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
            bulkAcceptOrderUseCase.setParams(orderIds, userSession.userId)
            bulkAcceptOrderUseCase.executeOnBackground()
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        }

        assert(viewModel.bulkAcceptOrderStatusResult.value == null)
    }

    @Test
    fun getTickers_shouldSuccess() {
        coEvery {
            somListGetTickerUseCase.executeOnBackground()
        } returns emptyList()

        viewModel.getTickers()

        coVerify {
            somListGetTickerUseCase.executeOnBackground()
        }

        assert(viewModel.tickerResult.observeAwaitValue() is Success)
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
            SomListFilterUiModel(emptyList(), args.first() as Boolean)
        }

        every {
            somListGetFilterListUseCase.isFirstLoad
        } returns true

        viewModel.getFilters()

        coVerify(exactly = 1) {
            somListGetFilterListUseCase.executeOnBackground(true)
            somListGetFilterListUseCase.executeOnBackground(false)
        }

        assert(viewModel.filterResult.observeAwaitValue() is Success)
    }

    @Test
    fun getFiltersFromCloud_shouldSuccess() {
        coEvery {
            somListGetFilterListUseCase.executeOnBackground(any())
        } answers {
            SomListFilterUiModel(emptyList(), args.first() as Boolean)
        }

        every {
            somListGetFilterListUseCase.isFirstLoad
        } returns false

        viewModel.getFilters()

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

        viewModel.getFilters()

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

        viewModel.getFilters()

        coVerify(exactly = 1) {
            somListGetFilterListUseCase.executeOnBackground(false)
        }

        assert(viewModel.filterResult.observeAwaitValue() is Fail)
    }

    @Test
    fun getWaitingPaymentCounter_shouldSuccess() {
        coEvery {
            somListGetWaitingPaymentUseCase.executeOnBackground()
        } returns WaitingPaymentCounter()

        viewModel.getWaitingPaymentCounter()

        coVerify {
            somListGetWaitingPaymentUseCase.executeOnBackground()
        }

        assert(viewModel.waitingPaymentCounterResult.observeAwaitValue() is Success)
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
    fun getOrderList_shouldSuccess() {
        coEvery {
            somListGetOrderListUseCase.executeOnBackground()
        } returns (0 to listOf())

        somGetOrderListJobField.set(viewModel, null)
        viewModel.getOrderList()

        coVerify {
            somListGetOrderListUseCase.executeOnBackground()
        }

        assert(viewModel.orderListResult.observeAwaitValue() is Success && !viewModel.hasNextPage())
    }

    @Test
    fun getOrderList_shouldCancelOldJobAndSuccess() {
        val getOrderListJob = mockk<Job>(relaxed = true)
        coEvery {
            somListGetOrderListUseCase.executeOnBackground()
        } returns (0 to listOf())

        somGetOrderListJobField.set(viewModel, getOrderListJob)
        viewModel.getOrderList()

        coVerify {
            somListGetOrderListUseCase.executeOnBackground()
            getOrderListJob.cancel()
        }

        assert(viewModel.orderListResult.observeAwaitValue() is Success && !viewModel.hasNextPage())
    }

    @Test
    fun getOrderList_shouldFailed() {
        coEvery {
            somListGetOrderListUseCase.executeOnBackground()
        } throws Throwable()

        viewModel.getOrderList()

        coVerify {
            somListGetOrderListUseCase.executeOnBackground()
        }

        assert(viewModel.orderListResult.observeAwaitValue() is Fail)
    }

    @Test
    fun refreshSelectedOrder_shouldSuccess() {
        val invoice = "INV/20200922/XX/IX/123456789"

        coEvery {
            somListGetOrderListUseCase.executeOnBackground()
        } returns (0 to listOf())

        viewModel.refreshSelectedOrder(invoice)

        coVerify {
            somListGetOrderListUseCase.executeOnBackground()
        }

        assert(viewModel.orderListResult.observeAwaitValue() is Success)
    }

    @Test
    fun refreshSelectedOrder_shouldFailed() {
        val invoice = "INV/20200922/XX/IX/123456789"

        coEvery {
            somListGetOrderListUseCase.executeOnBackground()
        } throws Throwable()

        viewModel.refreshSelectedOrder(invoice)

        coVerify {
            somListGetOrderListUseCase.executeOnBackground()
        }

        assert(viewModel.orderListResult.observeAwaitValue() is Fail)
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
        } returns Success(SomGetUserRoleUiModel())

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
        } returns Success(SomGetUserRoleUiModel())

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
        assertEquals(viewModel.isMultiSelectEnabled, mockMultiSelectEnabled)
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