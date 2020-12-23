package com.tokopedia.sellerorder.list.presentation.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
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
import com.tokopedia.shop.common.constant.AccessId
import com.tokopedia.shop.common.domain.interactor.AuthorizeAccessUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.Ordering
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt

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

    @RelaxedMockK
    lateinit var authorizeAccessUseCase: AuthorizeAccessUseCase

    private val dispatcher: SomDispatcherProvider = SomTestDispatcherProvider()

    private lateinit var viewModel: SomListViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = SomListViewModel(getUserRoleUseCase, somAcceptOrderUseCase,
                somRejectOrderUseCase, somRejectCancelOrderRequest, somEditRefNumUseCase, userSession,
                dispatcher, somListGetTickerUseCase, somListGetFilterListUseCase, somListGetWaitingPaymentUseCase,
                somListGetOrderListUseCase, somListGetTopAdsCategoryUseCase, bulkAcceptOrderStatusUseCase,
                bulkAcceptOrderUseCase, authorizeAccessUseCase)
    }

    private fun doGetTopAdsCategory_shouldSuccess(result: Int = 1) {
        coEvery {
            somListGetTopAdsCategoryUseCase.execute()
        } returns Success(result)

        viewModel.getTopAdsCategory()

        coVerify {
            somListGetTopAdsCategoryUseCase.execute()
        }

        assert(viewModel.topAdsCategoryResult.observeAwaitValue() is Success)
    }

    private fun setGetDataOrderListParams() {
        val newParams = viewModel.getDataOrderListParams().apply {
            search = "keyword"
            startDate = "12/12/2014"
            endDate = "12/12/2017"
            statusList = listOf(220, 400, 500)
            shippingList = listOf(12)
            orderTypeList = listOf(12)
            sortBy = SomConsts.SORT_BY_PAYMENT_DATE_ASCENDING
            nextOrderId = 123456
        }
        viewModel.updateGetOrderListParams(newParams)
    }

    @Test
    fun bulkAcceptOrder_shouldSuccess() = runBlocking {
        val orderIds = listOf("0", "1", "2")

        coEvery {
            bulkAcceptOrderUseCase.execute()
        } returns SomListBulkAcceptOrderUiModel(SomListBulkAcceptOrderUiModel.Data())

        viewModel.bulkAcceptOrder(orderIds)

        coVerify(ordering = Ordering.SEQUENCE) {
            bulkAcceptOrderUseCase.setParams(orderIds, userSession.userId)
            bulkAcceptOrderUseCase.execute()
        }

        assert(viewModel.bulkAcceptOrderResult.observeAwaitValue() is Success)
    }

    @Test
    fun bulkAcceptOrder_shouldFailed() = runBlocking {
        val orderIds = listOf("0", "1", "2")

        coEvery {
            bulkAcceptOrderUseCase.execute()
        } throws Throwable()

        viewModel.bulkAcceptOrder(orderIds)

        assert(viewModel.bulkAcceptOrderResult.observeAwaitValue() is Fail)
    }

    @Test
    fun getBulkAcceptOrderStatus_shouldSuccess() = runBlocking {
        val orderIds = listOf("0", "1", "2")

        coEvery {
            bulkAcceptOrderUseCase.execute()
        } returns SomListBulkAcceptOrderUiModel(SomListBulkAcceptOrderUiModel.Data())

        coEvery {
            bulkAcceptOrderStatusUseCase.execute()
        } returns SomListBulkAcceptOrderStatusUiModel(SomListBulkAcceptOrderStatusUiModel.Data(), listOf())

        viewModel.bulkAcceptOrder(orderIds)
        viewModel.bulkAcceptOrderStatusResult.observeAwaitValue()

        coVerify(ordering = Ordering.ORDERED) {
            bulkAcceptOrderUseCase.setParams(orderIds, userSession.userId)
            bulkAcceptOrderUseCase.execute()
            bulkAcceptOrderStatusUseCase.execute()
        }

        assert(viewModel.bulkAcceptOrderStatusResult.value is Success)
    }

    @Test
    fun getBulkAcceptOrderStatus_shouldPartialSuccessThenFullSuccessAfterRetry() = runBlocking {
        var count = 0
        val orderIds = listOf("0", "1", "2")

        coEvery {
            bulkAcceptOrderUseCase.execute()
        } returns SomListBulkAcceptOrderUiModel(SomListBulkAcceptOrderUiModel.Data())

        coEvery {
            bulkAcceptOrderStatusUseCase.execute()
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
            bulkAcceptOrderUseCase.execute()
            bulkAcceptOrderStatusUseCase.execute()
        }

        assert(viewModel.bulkAcceptOrderStatusResult.value is Success)
    }

    @Test
    fun getBulkAcceptOrderStatus_shouldFailedThenSuccessAfterAutoRetry() = runBlocking {
        var count = 0
        val orderIds = listOf("0", "1", "2")

        coEvery {
            bulkAcceptOrderUseCase.execute()
        } returns SomListBulkAcceptOrderUiModel(SomListBulkAcceptOrderUiModel.Data())

        coEvery {
            bulkAcceptOrderStatusUseCase.execute()
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
            bulkAcceptOrderUseCase.execute()
            bulkAcceptOrderStatusUseCase.execute()
        }

        assert(viewModel.bulkAcceptOrderStatusResult.value is Success)
    }

    @Test
    fun getBulkAcceptOrderStatus_shouldAlwaysFailed() = runBlocking {
        val orderIds = listOf("0", "1", "2")

        coEvery {
            bulkAcceptOrderUseCase.execute()
        } returns SomListBulkAcceptOrderUiModel(SomListBulkAcceptOrderUiModel.Data())

        coEvery {
            bulkAcceptOrderStatusUseCase.execute()
        } throws Throwable()

        viewModel.bulkAcceptOrder(orderIds)
        viewModel.bulkAcceptOrderStatusResult.observeAwaitValue(time = 60)

        coVerify(ordering = Ordering.ORDERED) {
            bulkAcceptOrderUseCase.setParams(orderIds, userSession.userId)
            bulkAcceptOrderUseCase.execute()
            bulkAcceptOrderStatusUseCase.execute()
        }

        assert(viewModel.bulkAcceptOrderStatusResult.value == null)
    }

    @Test
    fun retryGetBulkAcceptOrderStatus_shouldSuccess() = runBlocking {
        var count = 0
        val orderIds = listOf("0", "1", "2")

        coEvery {
            bulkAcceptOrderUseCase.execute()
        } returns SomListBulkAcceptOrderUiModel(SomListBulkAcceptOrderUiModel.Data())

        coEvery {
            bulkAcceptOrderStatusUseCase.execute()
        } coAnswers {
            count++
            if (count <= 20) {
                throw Throwable()
            } else {
                SomListBulkAcceptOrderStatusUiModel(SomListBulkAcceptOrderStatusUiModel.Data(), listOf())
            }
        }

        coEvery {
            bulkAcceptOrderStatusUseCase.execute()
        } returns SomListBulkAcceptOrderStatusUiModel(SomListBulkAcceptOrderStatusUiModel.Data(), listOf())

        viewModel.bulkAcceptOrder(orderIds)
        viewModel.bulkAcceptOrderStatusResult.observeAwaitValue()
        viewModel.retryGetBulkAcceptOrderStatus()
        viewModel.bulkAcceptOrderStatusResult.observeAwaitValue()

        coVerify(ordering = Ordering.ORDERED) {
            bulkAcceptOrderUseCase.setParams(orderIds, userSession.userId)
            bulkAcceptOrderUseCase.execute()
            bulkAcceptOrderStatusUseCase.execute()
        }

        assert(viewModel.bulkAcceptOrderStatusResult.value is Success)
    }

    @Test
    fun retryGetBulkAcceptOrderStatus_shouldPartialSuccessThenFullSuccessAfterRetry() = runBlocking {
        var count = 0
        val orderIds = listOf("0", "1", "2")

        coEvery {
            bulkAcceptOrderUseCase.execute()
        } returns SomListBulkAcceptOrderUiModel(SomListBulkAcceptOrderUiModel.Data())

        coEvery {
            bulkAcceptOrderStatusUseCase.execute()
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
            bulkAcceptOrderUseCase.execute()
            bulkAcceptOrderStatusUseCase.execute()
        }

        assert(viewModel.bulkAcceptOrderStatusResult.value is Success)
    }

    @Test
    fun retryGetBulkAcceptOrderStatus_shouldFailedThenSuccessAfterAutoRetry() = runBlocking {
        var count = 0
        val orderIds = listOf("0", "1", "2")

        coEvery {
            bulkAcceptOrderUseCase.execute()
        } returns SomListBulkAcceptOrderUiModel(SomListBulkAcceptOrderUiModel.Data())

        coEvery {
            bulkAcceptOrderStatusUseCase.execute()
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
            bulkAcceptOrderUseCase.execute()
            bulkAcceptOrderStatusUseCase.execute()
        }

        assert(viewModel.bulkAcceptOrderStatusResult.value is Success)
    }

    @Test
    fun retryGetBulkAcceptOrderStatus_shouldAlwaysFailed() = runBlocking {
        val orderIds = listOf("0", "1", "2")

        coEvery {
            bulkAcceptOrderUseCase.execute()
        } returns SomListBulkAcceptOrderUiModel(SomListBulkAcceptOrderUiModel.Data())

        coEvery {
            bulkAcceptOrderStatusUseCase.execute()
        } throws Throwable()

        viewModel.bulkAcceptOrder(orderIds)
        viewModel.bulkAcceptOrderStatusResult.observeAwaitValue(time = 60)
        viewModel.retryGetBulkAcceptOrderStatus()
        viewModel.bulkAcceptOrderStatusResult.observeAwaitValue(time = 60)

        coVerify(ordering = Ordering.ORDERED) {
            bulkAcceptOrderUseCase.setParams(orderIds, userSession.userId)
            bulkAcceptOrderUseCase.execute()
            bulkAcceptOrderStatusUseCase.execute()
        }

        assert(viewModel.bulkAcceptOrderStatusResult.value == null)
    }

    @Test
    fun getTickers_shouldSuccess() {
        coEvery {
            somListGetTickerUseCase.execute()
        } returns Success(emptyList())

        viewModel.getTickers()

        coVerify {
            somListGetTickerUseCase.execute()
        }

        assert(viewModel.tickerResult.observeAwaitValue() is Success)
    }

    @Test
    fun getTickers_shouldFailed() {
        coEvery {
            somListGetTickerUseCase.execute()
        } throws Throwable()

        viewModel.getTickers()

        coVerify {
            somListGetTickerUseCase.execute()
        }

        assert(viewModel.tickerResult.observeAwaitValue() is Fail)
    }

    @Test
    fun getFilters_shouldSuccess() {
        coEvery {
            somListGetFilterListUseCase.execute()
        } returns Success(SomListFilterUiModel())

        viewModel.getFilters()

        coVerify {
            somListGetFilterListUseCase.execute()
        }

        assert(viewModel.filterResult.observeAwaitValue() is Success)
    }

    @Test
    fun getFilters_shouldFailed() {
        coEvery {
            somListGetFilterListUseCase.execute()
        } throws Throwable()

        viewModel.getFilters()

        coVerify {
            somListGetFilterListUseCase.execute()
        }

        assert(viewModel.filterResult.observeAwaitValue() is Fail)
    }

    @Test
    fun getFilters_shouldNotSuccess_whenCannotShowOrderData() {
        coEvery {
            somListGetFilterListUseCase.execute()
        } returns Success(SomListFilterUiModel())

        viewModel::class.java.getDeclaredField("_canShowOrderData").apply {
            isAccessible = true
        }.set(viewModel, MutableLiveData<Boolean>().apply { value = false })

        viewModel.getFilters()

        coVerify {
            somListGetFilterListUseCase.execute()
        }

        assertFalse(viewModel.filterResult.observeAwaitValue() is Success)
    }

    @Test
    fun getWaitingPaymentCounter_shouldSuccess() {
        coEvery {
            somListGetWaitingPaymentUseCase.execute()
        } returns Success(WaitingPaymentCounter())

        viewModel.getWaitingPaymentCounter()

        coVerify {
            somListGetWaitingPaymentUseCase.execute()
        }

        assert(viewModel.waitingPaymentCounterResult.observeAwaitValue() is Success)
    }

    @Test
    fun getWaitingPaymentCounter_shouldFailed() {
        coEvery {
            somListGetWaitingPaymentUseCase.execute()
        } throws Throwable()

        viewModel.getWaitingPaymentCounter()

        coVerify {
            somListGetWaitingPaymentUseCase.execute()
        }

        assert(viewModel.waitingPaymentCounterResult.observeAwaitValue() is Fail)
    }

    @Test
    fun getWaitingPaymentCounter_shouldNotSuccess_whenCannotShowOrderData() {
        coEvery {
            somListGetWaitingPaymentUseCase.execute()
        } returns Success(WaitingPaymentCounter())

        viewModel::class.java.getDeclaredField("_canShowOrderData").apply {
            isAccessible = true
        }.set(viewModel, MutableLiveData<Boolean>().apply { value = false })

        viewModel.getWaitingPaymentCounter()

        coVerify {
            somListGetWaitingPaymentUseCase.execute()
        }

        assertFalse(viewModel.waitingPaymentCounterResult.observeAwaitValue() is Success)
    }
    @Test
    fun getOrderList_shouldSuccess() {
        coEvery {
            somListGetOrderListUseCase.execute()
        } returns (0 to listOf())

        viewModel.getOrderList()

        coVerify {
            somListGetOrderListUseCase.execute()
        }

        assert(viewModel.orderListResult.observeAwaitValue() is Success)
    }

    @Test
    fun getOrderList_shouldFailed() {
        coEvery {
            somListGetOrderListUseCase.execute()
        } throws Throwable()

        viewModel.getOrderList()

        coVerify {
            somListGetOrderListUseCase.execute()
        }

        assert(viewModel.orderListResult.observeAwaitValue() is Fail)
    }

    @Test
    fun getOrderList_shouldNotSuccess_whenCannotShowOrderData() {
        coEvery {
            somListGetOrderListUseCase.execute()
        } returns (0 to listOf())

        viewModel::class.java.getDeclaredField("_canShowOrderData").apply {
            isAccessible = true
        }.set(viewModel, MutableLiveData<Boolean>().apply { value = false })

        viewModel.getOrderList()

        coVerify {
            somListGetOrderListUseCase.execute()
        }

        assertFalse(viewModel.orderListResult.observeAwaitValue() is Success)
    }

    @Test
    fun refreshSelectedOrder_shouldSuccess() {
        val invoice = "INV/20200922/XX/IX/123456789"

        coEvery {
            somListGetOrderListUseCase.execute()
        } returns (0 to listOf())

        viewModel.refreshSelectedOrder(invoice)

        coVerify {
            somListGetOrderListUseCase.execute()
        }

        assert(viewModel.orderListResult.observeAwaitValue() is Success)
    }

    @Test
    fun refreshSelectedOrder_shouldFailed() {
        val invoice = "INV/20200922/XX/IX/123456789"

        coEvery {
            somListGetOrderListUseCase.execute()
        } throws Throwable()

        viewModel.refreshSelectedOrder(invoice)

        coVerify {
            somListGetOrderListUseCase.execute()
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
            somListGetTopAdsCategoryUseCase.execute()
        } throws Throwable()

        viewModel.getTopAdsCategory()

        coVerify {
            somListGetTopAdsCategoryUseCase.execute()
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
    fun getUserRoles_shouldSkippedWhenAlreadyRunning() = runBlocking {
        viewModel::class.java.superclass!!.getDeclaredField("getUserRolesJob").apply {
            isAccessible = true
        }.set(viewModel, launch { delay(10000) })

        viewModel.getUserRoles()

        coVerify(inverse = true) { getUserRoleUseCase.execute() }

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }
        return@runBlocking
    }

    @Test
    fun getUserRolesTest() {
        val newUserRolesValue = SomGetUserRoleUiModel(listOf(Roles.MANAGE_SHOPSTATS))
        viewModel.setUserRoles(newUserRolesValue)
        val userRolesValue = viewModel.userRoleResult.observeAwaitValue()
        assert(userRolesValue is Success && userRolesValue.data == newUserRolesValue)
    }

    @Test
    fun getAdminPermission_shouldSuccess() {
        coEvery {
            authorizeAccessUseCase.execute(anyInt(), AccessId.SOM_LIST)
        } returns true
        coEvery {
            userSession.isShopOwner
        } returns false
        coEvery {
            userSession.isShopAdmin
        } returns true

        viewModel.getAdminPermission()

        coVerify {
            authorizeAccessUseCase.execute(anyInt(), AccessId.SOM_LIST)
        }

        assert(viewModel.isOrderManageEligible.observeAwaitValue() is Success)
    }

    @Test
    fun getAdminPermission_shouldFail() {
        coEvery {
            authorizeAccessUseCase.execute(anyInt(), AccessId.SOM_LIST)
        } throws Throwable()
        coEvery {
            userSession.isShopOwner
        } returns false
        coEvery {
            userSession.isShopAdmin
        } returns true

        viewModel.getAdminPermission()

        coVerify {
            authorizeAccessUseCase.execute(anyInt(), AccessId.SOM_LIST)
        }

        assert(viewModel.isOrderManageEligible.observeAwaitValue() is Fail)
    }

    @Test
    fun getAdminPermission_whenIsShopOwner_shouldNotRunAndUserShouldEligible() {
        coEvery {
            userSession.isShopOwner
        } returns true

        viewModel.getAdminPermission()

        coVerify(exactly = 0) {
            adminPermissionUseCase.execute(AdminPermissionGroup.ORDER)
        }
        assert((viewModel.isAdminEligible.value as? Success)?.data == true)
    }

    @Test
    fun getAdminPermission_whenIsNotShopOwnerButIsShopAdmin_shouldRun() {
        coEvery {
            userSession.isShopOwner
        } returns false
        coEvery {
            userSession.isShopAdmin
        } returns true

        viewModel.getAdminPermission()

        coVerify {
            adminPermissionUseCase.execute(AdminPermissionGroup.ORDER)
        }
    }

    @Test
    fun getAdminPermission_whenIsNotShopOwnerOrAdmin_shouldNotRunAndShouldNotEligible() {
        coEvery {
            userSession.isShopOwner
        } returns false
        coEvery {
            userSession.isShopAdmin
        } returns false

        viewModel.getAdminPermission()

        coVerify(exactly = 0) {
            authorizeAccessUseCase.execute(anyInt(), AccessId.SOM_LIST)
        }
        assert((viewModel.isAdminEligible.value as? Success)?.data == false)
    }

    @Test
    fun isTopAdsActive_shouldReturnFalseWhenSuccess() {
        doGetTopAdsCategory_shouldSuccess(0)

        assert(!viewModel.isTopAdsActive())
    }

    @Test
    fun isTopAdsActive_shouldReturnFalseWhenFailed() {
        getTopAdsCategory_shouldFailed()

        assert(!viewModel.isTopAdsActive())
    }

    @Test
    fun isTopAdsActive_shouldReturnTrue() {
        getTopAdsCategory_shouldSuccess()

        assert(!viewModel.isTopAdsActive())
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
        val orderTypes = listOf(1, 2, 3, 4, 5)
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
}