package com.tokopedia.sellerorder.list.presentation.viewmodels

import androidx.lifecycle.MediatorLiveData
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.sellerorder.common.SomOrderBaseViewModelTest
import com.tokopedia.sellerorder.common.util.BulkRequestPickupStatus
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.filter.SomFilterViewModelTestFixture
import com.tokopedia.sellerorder.filter.domain.mapper.GetSomFilterMapper
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModel
import com.tokopedia.sellerorder.list.domain.mapper.FilterResultMapper
import com.tokopedia.sellerorder.list.domain.model.SomListFilterResponse
import com.tokopedia.sellerorder.list.domain.model.SomListGetOrderListParam
import com.tokopedia.sellerorder.list.domain.usecases.SomListBulkAcceptOrderUseCase
import com.tokopedia.sellerorder.list.domain.usecases.SomListBulkRequestPickupUseCase
import com.tokopedia.sellerorder.list.domain.usecases.SomListGetBulkAcceptOrderStatusUseCase
import com.tokopedia.sellerorder.list.domain.usecases.SomListGetFilterListUseCase
import com.tokopedia.sellerorder.list.domain.usecases.SomListGetHeaderIconsInfoUseCase
import com.tokopedia.sellerorder.list.domain.usecases.SomListGetMultiShippingStatusUseCase
import com.tokopedia.sellerorder.list.domain.usecases.SomListGetOrderListUseCase
import com.tokopedia.sellerorder.list.domain.usecases.SomListGetTickerUseCase
import com.tokopedia.sellerorder.list.domain.usecases.SomListGetTopAdsCategoryUseCase
import com.tokopedia.sellerorder.list.presentation.models.AllFailEligible
import com.tokopedia.sellerorder.list.presentation.models.AllNotEligible
import com.tokopedia.sellerorder.list.presentation.models.AllSuccess
import com.tokopedia.sellerorder.list.presentation.models.AllValidationFail
import com.tokopedia.sellerorder.list.presentation.models.FailRetry
import com.tokopedia.sellerorder.list.presentation.models.MultiShippingStatusUiModel
import com.tokopedia.sellerorder.list.presentation.models.NotEligibleAndFail
import com.tokopedia.sellerorder.list.presentation.models.PartialSuccess
import com.tokopedia.sellerorder.list.presentation.models.PartialSuccessNotEligible
import com.tokopedia.sellerorder.list.presentation.models.PartialSuccessNotEligibleFail
import com.tokopedia.sellerorder.list.presentation.models.RefreshOrder
import com.tokopedia.sellerorder.list.presentation.models.SomListBulkAcceptOrderStatusUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListBulkAcceptOrderUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListBulkRequestPickupUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListEmptyStateUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListHeaderIconsInfoUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListOrderUiModel
import com.tokopedia.sellerorder.util.TestHelper
import com.tokopedia.sellerorder.util.TestHelper.invokeSuspend
import com.tokopedia.sellerorder.util.observeAwaitValue
import com.tokopedia.shop.common.domain.interactor.AuthorizeAccessUseCase
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.Ordering
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.lang.reflect.Field
import java.lang.reflect.Method
import kotlin.coroutines.Continuation

@ExperimentalCoroutinesApi
class SomListViewModelTest : SomOrderBaseViewModelTest<SomListViewModel>() {

    @RelaxedMockK
    lateinit var somListGetTickerUseCase: SomListGetTickerUseCase

    @RelaxedMockK
    lateinit var somListGetFilterListUseCase: SomListGetFilterListUseCase

    @RelaxedMockK
    lateinit var somListGetHeaderIconsInfoUseCase: SomListGetHeaderIconsInfoUseCase

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
    private lateinit var retryRequestPickupUser: Field
    private lateinit var getFailingOrderIdsFromBulkRequestPickupStatus: Method
    private lateinit var getSuccessBulkRequestPickupResultData: Method
    private lateinit var getTotalNotEligibleOrderFromBulkRequestPickupResult: Method
    private lateinit var getTotalEligibleOrderFromBulkRequestPickupResult: Method
    private lateinit var getBulkRequestPickupJobID: Method
    private lateinit var getBulkRequestPickupStatus: Method
    private lateinit var waitForGetFiltersCompleted: Method
    private lateinit var onReceiveBulkRequestPickupStatusResult: Method
    private lateinit var onReceiveBulkAcceptOrderStatusResult: Method

    private val partialSuccessGetBulkAcceptOrderStatus = SomListBulkAcceptOrderStatusUiModel(
        SomListBulkAcceptOrderStatusUiModel.Data(
            success = 1,
            totalOrder = 3
        ),
        listOf()
    )
    private val allSuccessBulkAcceptOrderStatus = SomListBulkAcceptOrderStatusUiModel(
        SomListBulkAcceptOrderStatusUiModel.Data(
            success = 3,
            totalOrder = 3
        ),
        listOf()
    )

    @Before
    override fun setUp() {
        super.setUp()
        viewModel = spyk(
            SomListViewModel(
                somAcceptOrderUseCase,
                somRejectOrderUseCase,
                somRejectCancelOrderUseCase,
                somEditRefNumUseCase,
                somValidateOrderUseCase,
                userSessionInterface,
                coroutineTestRule.dispatchers,
                somListGetTickerUseCase,
                somListGetFilterListUseCase,
                somListGetHeaderIconsInfoUseCase,
                somListGetOrderListUseCase,
                somListGetTopAdsCategoryUseCase,
                bulkAcceptOrderStatusUseCase,
                bulkAcceptOrderUseCase,
                bulkRequestPickupUseCase,
                multiShippingStatusUseCase,
                authorizeAccessUseCase,
                authorizeMultiAcceptAccessUseCase
            )
        )

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
        retryRequestPickupUser = viewModel::class.java.getDeclaredField("retryRequestPickupUser").apply {
            isAccessible = true
        }
        getFailingOrderIdsFromBulkRequestPickupStatus = viewModel::class.java.getDeclaredMethod("getFailingOrderIdsFromBulkRequestPickupStatus", MultiShippingStatusUiModel::class.java).apply {
            isAccessible = true
        }
        getSuccessBulkRequestPickupResultData = viewModel::class.java.getDeclaredMethod("getSuccessBulkRequestPickupResultData").apply {
            isAccessible = true
        }
        getTotalNotEligibleOrderFromBulkRequestPickupResult = viewModel::class.java.getDeclaredMethod("getTotalNotEligibleOrderFromBulkRequestPickupResult").apply {
            isAccessible = true
        }
        getTotalEligibleOrderFromBulkRequestPickupResult = viewModel::class.java.getDeclaredMethod("getTotalEligibleOrderFromBulkRequestPickupResult").apply {
            isAccessible = true
        }
        getBulkRequestPickupJobID = viewModel::class.java.getDeclaredMethod("getBulkRequestPickupJobID").apply {
            isAccessible = true
        }
        getBulkRequestPickupStatus = viewModel::class.java.getDeclaredMethod("getBulkRequestPickupStatus").apply {
            isAccessible = true
        }
        waitForGetFiltersCompleted = viewModel::class.java.getDeclaredMethod("waitForGetFiltersCompleted", Continuation::class.java).apply {
            isAccessible = true
        }
        onReceiveBulkRequestPickupStatusResult = viewModel::class.java.getDeclaredMethod("onReceiveBulkRequestPickupStatusResult", Result::class.java).apply {
            isAccessible = true
        }
        onReceiveBulkAcceptOrderStatusResult = viewModel::class.java.getDeclaredMethod("onReceiveBulkAcceptOrderStatusResult", Pair::class.java).apply {
            isAccessible = true
        }
    }

    override fun acceptOrder_shouldReturnSuccess() = coroutineTestRule.runTest {
        quickActionRefreshOrderTest(true) {
            super.acceptOrder_shouldReturnSuccess()
        }
    }

    override fun acceptOrder_shouldReturnFail() = coroutineTestRule.runTest {
        quickActionRefreshOrderTest(false) {
            super.acceptOrder_shouldReturnFail()
        }
    }

    override fun rejectOrder_shouldReturnSuccess() = coroutineTestRule.runTest {
        quickActionRefreshOrderTest(true) {
            super.rejectOrder_shouldReturnSuccess()
        }
    }

    override fun rejectOrder_shouldReturnFail() = coroutineTestRule.runTest {
        quickActionRefreshOrderTest(false) {
            super.rejectOrder_shouldReturnFail()
        }
    }

    override fun editAwb_shouldReturnSuccess() = coroutineTestRule.runTest {
        quickActionRefreshOrderTest(true) {
            super.editAwb_shouldReturnSuccess()
        }
    }

    override fun editAwb_shouldReturnFail() = coroutineTestRule.runTest {
        quickActionRefreshOrderTest(false) {
            super.editAwb_shouldReturnFail()
        }
    }

    override fun rejectCancelOrder_shouldReturnSuccess() = coroutineTestRule.runTest {
        quickActionRefreshOrderTest(true) {
            super.rejectCancelOrder_shouldReturnSuccess()
        }
    }

    override fun rejectCancelOrder_shouldReturnFail() = coroutineTestRule.runTest {
        quickActionRefreshOrderTest(false) {
            super.rejectCancelOrder_shouldReturnFail()
        }
    }

    private fun quickActionRefreshOrderTest(shouldRefresh: Boolean, quickAction: () -> Unit) = coroutineTestRule.runTest {
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

    private fun doGetTopAdsCategory_shouldSuccess(result: Int = 1) = coroutineTestRule.runTest {
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

    private fun setGetDataOrderListParams() = coroutineTestRule.runTest {
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
    fun bulkAcceptOrder_shouldSuccess() = coroutineTestRule.runTest {
        val orderIds = listOf("0", "1", "2")
        viewModel.bulkAcceptOrderResult.observe({ lifecycle }) {}
        doSuccessBulkAcceptOrder(orderIds)
        coVerify(ordering = Ordering.SEQUENCE) {
            bulkAcceptOrderUseCase.setParams(orderIds, userSessionInterface.userId)
            bulkAcceptOrderUseCase.executeOnBackground()
        }
        assert(viewModel.bulkAcceptOrderResult.value is Success)
    }

    @Test
    fun bulkAcceptOrder_shouldFailed() = coroutineTestRule.runTest {
        val orderIds = listOf("0", "1", "2")
        viewModel.bulkAcceptOrderResult.observe({ lifecycle }) {}
        doFailedBulkAcceptOrder(orderIds)
        coVerify(ordering = Ordering.SEQUENCE) {
            bulkAcceptOrderUseCase.setParams(orderIds, userSessionInterface.userId)
            bulkAcceptOrderUseCase.executeOnBackground()
        }
        assert(viewModel.bulkAcceptOrderResult.value is Fail)
    }

    @Test
    fun getBulkAcceptOrderStatus_shouldNotExecuteWhenBulkAcceptOrderIsFail() = coroutineTestRule.runTest {
        viewModel.bulkAcceptOrderStatusResult.observe({ lifecycle }) {}
        bulkAcceptOrder_shouldFailed()
        coVerify(inverse = true) {
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        }
        assertNull(viewModel.bulkAcceptOrderStatusResult.value)
    }

    @Test
    fun getBulkAcceptOrderStatus_shouldSuccess() = coroutineTestRule.runTest {
        val orderIds = listOf("0", "1", "2")
        coEvery {
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        } returns allSuccessBulkAcceptOrderStatus
        viewModel.bulkAcceptOrderStatusResult.observe({ lifecycle }) {}
        doSuccessBulkAcceptOrder(orderIds)
        coVerify(ordering = Ordering.ORDERED) {
            bulkAcceptOrderUseCase.setParams(orderIds, userSessionInterface.userId)
            bulkAcceptOrderUseCase.executeOnBackground()
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        }
        coVerify(exactly = 1) {
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        }
        val result = viewModel.bulkAcceptOrderStatusResult.value
        assert(result is Success)
        assertEquals((result as Success).data, allSuccessBulkAcceptOrderStatus)
    }

    @Test
    fun getBulkAcceptOrderStatus_shouldPartialSuccessThenFullSuccessAfterRetry() = coroutineTestRule.runTest {
        var count = 0
        val orderIds = listOf("0", "1", "2")
        coEvery {
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        } answers {
            count++
            if (count == 1) {
                partialSuccessGetBulkAcceptOrderStatus
            } else {
                allSuccessBulkAcceptOrderStatus
            }
        }
        viewModel.bulkAcceptOrderStatusResult.observe({ lifecycle }) {}
        doSuccessBulkAcceptOrder(orderIds)
        coVerify(ordering = Ordering.ORDERED) {
            bulkAcceptOrderUseCase.setParams(orderIds, userSessionInterface.userId)
            bulkAcceptOrderUseCase.executeOnBackground()
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        }
        coVerify(exactly = 2) {
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        }
        val result = viewModel.bulkAcceptOrderStatusResult.value
        assert(result is Success)
        assertEquals((result as Success).data, allSuccessBulkAcceptOrderStatus)
    }

    @Test
    fun getBulkAcceptOrderStatus_shouldFailedThenSuccessAfterAutoRetry() = runTest {
        var count = 0
        val orderIds = listOf("0", "1", "2")
        coEvery {
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        } answers {
            count++
            if (count == 1) {
                throw Throwable()
            } else {
                allSuccessBulkAcceptOrderStatus
            }
        }
        viewModel.bulkAcceptOrderStatusResult.observe({ lifecycle }) {}
        doSuccessBulkAcceptOrder(orderIds)
        advanceTimeBy(5000)
        coVerify(ordering = Ordering.ORDERED, timeout = 5000) {
            bulkAcceptOrderUseCase.setParams(orderIds, userSessionInterface.userId)
            bulkAcceptOrderUseCase.executeOnBackground()
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        }
        coVerify(exactly = 2, timeout = 5000) {
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        }
        val result = viewModel.bulkAcceptOrderStatusResult.value
        assert(result is Success)
        assertEquals((result as Success).data, allSuccessBulkAcceptOrderStatus)
    }

    @Test
    fun getBulkAcceptOrderStatus_shouldFinallySuccessAfterAutoRetryWithFailedInBetweenRetry() = coroutineTestRule.runTest {
        var count = 0
        val orderIds = listOf("0", "1", "2")
        coEvery {
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        } answers {
            count++
            if (count == 1) {
                partialSuccessGetBulkAcceptOrderStatus
            } else if (count == 2) {
                throw Throwable()
            } else {
                allSuccessBulkAcceptOrderStatus
            }
        }
        viewModel.bulkAcceptOrderStatusResult.observe({ lifecycle }) {}
        doSuccessBulkAcceptOrder(orderIds)
        coVerify(ordering = Ordering.ORDERED) {
            bulkAcceptOrderUseCase.setParams(orderIds, userSessionInterface.userId)
            bulkAcceptOrderUseCase.executeOnBackground()
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        }
        coVerify(exactly = 3) {
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        }
        val result = viewModel.bulkAcceptOrderStatusResult.value
        assert(result is Success)
        assertEquals((result as Success).data, allSuccessBulkAcceptOrderStatus)
    }

    @Test
    fun getBulkAcceptOrderStatus_shouldPartialSuccessWhenAlwaysGetPartialSuccessResult() {
        val orderIds = listOf("0", "1", "2")
        coEvery {
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        } returns partialSuccessGetBulkAcceptOrderStatus
        viewModel.bulkAcceptOrderStatusResult.observe({ lifecycle }) {}
        doSuccessBulkAcceptOrder(orderIds)
        coVerify(ordering = Ordering.ORDERED, timeout = 5000) {
            bulkAcceptOrderUseCase.setParams(orderIds, userSessionInterface.userId)
            bulkAcceptOrderUseCase.executeOnBackground()
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        }
        coVerify(exactly = 20, timeout = 5000) {
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        }
        val result = viewModel.bulkAcceptOrderStatusResult.value
        assert(result is Success)
        assertEquals((result as Success).data, partialSuccessGetBulkAcceptOrderStatus)
    }

    @Test
    fun getBulkAcceptOrderStatus_shouldShowLastPartialSuccessWhenNeverGetAllSuccessResult() {
        var count = 0
        val orderIds = listOf("0", "1", "2")
        coEvery {
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        } answers {
            count++
            if (count == 1) {
                partialSuccessGetBulkAcceptOrderStatus
            } else {
                throw Throwable()
            }
        }
        viewModel.bulkAcceptOrderStatusResult.observe({ lifecycle }) {}
        doSuccessBulkAcceptOrder(orderIds)
        coVerify(ordering = Ordering.ORDERED, timeout = 5000) {
            bulkAcceptOrderUseCase.setParams(orderIds, userSessionInterface.userId)
            bulkAcceptOrderUseCase.executeOnBackground()
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        }
        coVerify(exactly = 20, timeout = 5000) {
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        }
        val result = viewModel.bulkAcceptOrderStatusResult.value
        assert(result is Success)
        assertEquals((result as Success).data, partialSuccessGetBulkAcceptOrderStatus)
    }

    @Test
    fun getBulkAcceptOrderStatus_shouldAlwaysFailed() {
        val orderIds = listOf("0", "1", "2")
        coEvery {
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        } throws Throwable()
        viewModel.bulkAcceptOrderStatusResult.observe({ lifecycle }) {}
        doSuccessBulkAcceptOrder(orderIds)
        coVerify(ordering = Ordering.ORDERED, timeout = 5000) {
            bulkAcceptOrderUseCase.setParams(orderIds, userSessionInterface.userId)
            bulkAcceptOrderUseCase.executeOnBackground()
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        }
        coVerify(exactly = 20, timeout = 5000) {
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        }
        val result = viewModel.bulkAcceptOrderStatusResult.value
        assertTrue(result is Fail)
    }

    @Test
    fun retryGetBulkAcceptOrderStatus_shouldSuccess() {
        getBulkAcceptOrderStatus_shouldAlwaysFailed()
        coEvery {
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        } returns allSuccessBulkAcceptOrderStatus
        coVerify(exactly = 20, timeout = 5000) {
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        }
        runTest {
            viewModel.retryGetBulkAcceptOrderStatus()
            advanceTimeBy(5000)
        }
        coVerify(exactly = 21, timeout = 5000) {
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        }
        val result = viewModel.bulkAcceptOrderStatusResult.value
        assert(result is Success)
        assertEquals((result as Success).data, allSuccessBulkAcceptOrderStatus)
    }

    @Test
    fun retryGetBulkAcceptOrderStatus_shouldPartialSuccessThenFullSuccessAfterRetry() = runTest {
        getBulkAcceptOrderStatus_shouldAlwaysFailed()
        var count = 0
        coEvery {
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        } answers {
            count++
            if (count == 1) {
                partialSuccessGetBulkAcceptOrderStatus
            } else {
                allSuccessBulkAcceptOrderStatus
            }
        }
        viewModel.retryGetBulkAcceptOrderStatus()
        advanceTimeBy(20000)
        coVerify(exactly = 22, timeout = 5000) {
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        }
        val result = viewModel.bulkAcceptOrderStatusResult.value
        assert(result is Success)
        assertEquals((result as Success).data, allSuccessBulkAcceptOrderStatus)
    }

    @Test
    fun retryGetBulkAcceptOrderStatus_shouldFailedThenSuccessAfterAutoRetry() = runTest {
        getBulkAcceptOrderStatus_shouldAlwaysFailed()
        var count = 0
        coEvery {
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        } answers {
            count++
            if (count == 1) {
                throw Throwable()
            } else {
                allSuccessBulkAcceptOrderStatus
            }
        }

        viewModel.retryGetBulkAcceptOrderStatus()
        advanceTimeBy(20000)

        coVerify(exactly = 22) {
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        }
        val result = viewModel.bulkAcceptOrderStatusResult.value
        assert(result is Success)
        assertEquals((result as Success).data, allSuccessBulkAcceptOrderStatus)
    }

    @Test
    fun retryGetBulkAcceptOrderStatus_shouldAlwaysFailed() = runTest {
        getBulkAcceptOrderStatus_shouldAlwaysFailed()
        viewModel.retryGetBulkAcceptOrderStatus()
        advanceTimeBy(40000)
        coVerify(exactly = 40) {
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        }
        val result = viewModel.bulkAcceptOrderStatusResult.value
        assertTrue(result is Fail)
    }

    @Test
    fun bulkRequestPickup_shouldSuccess() = runTest {
        val bulkRequestPickupResultData = SomListBulkRequestPickupUiModel(
            data = SomListBulkRequestPickupUiModel.Data()
        )
        viewModel.bulkRequestPickupResult.observe({ lifecycle }) {}
        doSuccessBulkRequestPickup(bulkRequestPickupResultData)
        advanceTimeBy(5000)
        coVerify {
            bulkRequestPickupUseCase.executeOnBackground()
        }
        val result = viewModel.bulkRequestPickupResult.value
        assert(result is Success)
    }

    @Test
    fun bulkRequestPickup_shouldAllNotEligible() = runTest {
        val orderIds = listOf("0", "1", "2", "4", "5", "6", "7")
        val batchId = "1234566"
        val somListBulkRequestPickupUiModel = SomListBulkRequestPickupUiModel.Data(jobId = batchId, totalOnProcess = 0)

        coEvery {
            bulkRequestPickupUseCase.executeOnBackground()
        } returns SomListBulkRequestPickupUiModel(
            data = somListBulkRequestPickupUiModel,
            errors = listOf(
                SomListBulkRequestPickupUiModel.ErrorBulkRequestPickup(
                    orderId = "0",
                    message = "Error"
                ),
                SomListBulkRequestPickupUiModel.ErrorBulkRequestPickup(
                    orderId = "1",
                    message = "Error"
                ),
                SomListBulkRequestPickupUiModel.ErrorBulkRequestPickup(
                    orderId = "2",
                    message = "Error"
                ),
                SomListBulkRequestPickupUiModel.ErrorBulkRequestPickup(
                    orderId = "4",
                    message = "Error"
                ),
                SomListBulkRequestPickupUiModel.ErrorBulkRequestPickup(
                    orderId = "5",
                    message = "Error"
                ),
                SomListBulkRequestPickupUiModel.ErrorBulkRequestPickup(
                    orderId = "6",
                    message = "Error"
                ),
                SomListBulkRequestPickupUiModel.ErrorBulkRequestPickup(
                    orderId = "7",
                    message = "Error"
                )
            )
        )

        viewModel.bulkRequestPickupFinalResultMediator.observe({ lifecycle }) {}

        viewModel.bulkRequestPickup(orderIds)
        advanceTimeBy(20000)

        coVerify(exactly = 1) {
            bulkRequestPickupUseCase.executeOnBackground()
        }
        coVerify(inverse = true) {
            multiShippingStatusUseCase.executeOnBackground()
        }

        val result = viewModel.bulkRequestPickupFinalResult.value
        assert(result is AllNotEligible)
    }

    @Test
    fun bulkRequestPickup_shouldFailed() = coroutineTestRule.runTest {
        viewModel.bulkRequestPickupResult.observe({ lifecycle }) {}
        doFailedBulkRequestPickup()
        coVerify {
            bulkRequestPickupUseCase.executeOnBackground()
        }
        val result = viewModel.bulkRequestPickupResult.value
        assert(result is Fail)
    }

    @Test
    fun getMultiShippingStatus_shouldNotExecuteWhenBulkRequestPickupIsFail() =
        coroutineTestRule.runTest {
            viewModel.bulkRequestPickupStatusResult.observe({ lifecycle }) {}
            bulkRequestPickup_shouldFailed()
            coVerify(inverse = true) {
                multiShippingStatusUseCase.executeOnBackground()
            }
            val result = viewModel.bulkRequestPickupStatusResult.value
            assertNull(result)
        }

    @Test
    fun getMultiShippingStatus_shouldSuccess() = runTest {
        val orderIds = listOf("0", "1", "2")
        val batchId = "1234566"

        coEvery {
            bulkRequestPickupUseCase.executeOnBackground()
        } returns SomListBulkRequestPickupUiModel(SomListBulkRequestPickupUiModel.Data(totalOnProcess = 3, jobId = batchId))

        coEvery {
            multiShippingStatusUseCase.executeOnBackground()
        } returns MultiShippingStatusUiModel()

        viewModel.bulkRequestPickupFinalResultMediator.observe({ lifecycle }) {}

        viewModel.bulkRequestPickup(orderIds)
        advanceTimeBy(5000)

        coVerify(timeout = 5000) {
            bulkRequestPickupUseCase.executeOnBackground()
            multiShippingStatusUseCase.executeOnBackground()
        }
        val result = viewModel.bulkRequestPickupResult.value
        assert(result is Success)
    }

    @Test
    fun getMultiShippingStatus_shouldFail() = runTest {
        val orderIds = listOf("0", "1", "2")
        val batchId = "1234566"

        coEvery {
            bulkRequestPickupUseCase.executeOnBackground()
        } returns SomListBulkRequestPickupUiModel(SomListBulkRequestPickupUiModel.Data(totalOnProcess = 3, jobId = batchId))

        coEvery {
            multiShippingStatusUseCase.executeOnBackground()
        } throws Throwable()

        viewModel.bulkRequestPickupFinalResultMediator.observe({ lifecycle }) {}

        viewModel.bulkRequestPickup(orderIds)
        advanceTimeBy(20000)

        coVerify {
            bulkRequestPickupUseCase.executeOnBackground()
            multiShippingStatusUseCase.executeOnBackground()
        }

        val result = viewModel.bulkRequestPickupStatusResult.value
        assert(result is Fail)
    }

    @Test
    fun getMultiShippingStatus_shouldReturnAllSuccessAtFirstTry() = runTest {
        val orderIds = listOf("0", "1", "2", "4", "5", "6", "7")
        val batchId = "1234566"
        val somListBulkRequestPickupUiModel = SomListBulkRequestPickupUiModel.Data(jobId = batchId, totalOnProcess = 7)
        val multiShippingStatusUiModel = MultiShippingStatusUiModel(
            total_order = 7,
            success = 7,
            fail = 0,
            processed = 7
        )

        coEvery {
            bulkRequestPickupUseCase.executeOnBackground()
        } returns SomListBulkRequestPickupUiModel(data = somListBulkRequestPickupUiModel)

        coEvery {
            multiShippingStatusUseCase.executeOnBackground()
        } returns multiShippingStatusUiModel

        viewModel.bulkRequestPickupFinalResultMediator.observe({ lifecycle }) {}

        viewModel.bulkRequestPickup(orderIds)
        advanceTimeBy(5000)

        coVerify(exactly = 1, timeout = 5000) {
            bulkRequestPickupUseCase.executeOnBackground()
        }

        coVerify(exactly = 1, timeout = 5000) {
            multiShippingStatusUseCase.executeOnBackground()
        }

        val result = viewModel.bulkRequestPickupFinalResult.value
        assert(result is AllSuccess)
    }

    @Test
    fun getMultiShippingStatus_shouldReturnAllSuccessAfterAutoRetry() = runTest {
        val orderIds = listOf("0", "1", "2", "4", "5", "6", "7")
        val batchId = "1234566"
        val somListBulkRequestPickupUiModel = SomListBulkRequestPickupUiModel.Data(jobId = batchId, totalOnProcess = 7)
        var multiShippingStatusUiModel = MultiShippingStatusUiModel(
            total_order = 7,
            success = 0,
            fail = 0,
            processed = 0
        )

        coEvery {
            bulkRequestPickupUseCase.executeOnBackground()
        } returns SomListBulkRequestPickupUiModel(data = somListBulkRequestPickupUiModel)
        coEvery {
            multiShippingStatusUseCase.executeOnBackground()
        } returns multiShippingStatusUiModel

        viewModel.bulkRequestPickupFinalResultMediator.observe({ lifecycle }) {}
        viewModel.bulkRequestPickup(orderIds)
        advanceTimeBy(20000)
        coVerify(exactly = 1) {
            bulkRequestPickupUseCase.executeOnBackground()
        }
        coVerify(exactly = 11) {
            multiShippingStatusUseCase.executeOnBackground()
        }

        multiShippingStatusUiModel = MultiShippingStatusUiModel(
            total_order = 7,
            success = 7,
            fail = 0,
            processed = 7
        )
        coEvery {
            multiShippingStatusUseCase.executeOnBackground()
        } returns multiShippingStatusUiModel

        viewModel.bulkRequestPickup(orderIds)
        advanceTimeBy(20000)

        coVerify(exactly = 2) {
            bulkRequestPickupUseCase.executeOnBackground()
        }
        coVerify(exactly = 12) {
            multiShippingStatusUseCase.executeOnBackground()
        }

        val result = viewModel.bulkRequestPickupFinalResult.value
        assert(result is AllSuccess)
    }

    @Test
    fun getMultiShippingStatus_shouldReturnPartialSuccessNotEligibleFailAtFirstTry() = runTest {
        val orderIds = listOf("0", "1", "2", "4", "5", "6", "7")
        val batchId = "1234566"
        val somListBulkRequestPickupUiModel = SomListBulkRequestPickupUiModel.Data(jobId = batchId, totalOnProcess = 5)
        val multiShippingStatusUiModel = MultiShippingStatusUiModel(
            total_order = 5,
            success = 4,
            fail = 1,
            processed = 5
        )

        coEvery {
            bulkRequestPickupUseCase.executeOnBackground()
        } returns SomListBulkRequestPickupUiModel(
            data = somListBulkRequestPickupUiModel,
            errors = listOf(
                SomListBulkRequestPickupUiModel.ErrorBulkRequestPickup(
                    orderId = "0",
                    message = "Error"
                ),
                SomListBulkRequestPickupUiModel.ErrorBulkRequestPickup(
                    orderId = "0",
                    message = "Error"
                )
            )
        )

        coEvery {
            multiShippingStatusUseCase.executeOnBackground()
        } returns multiShippingStatusUiModel

        viewModel.bulkRequestPickupFinalResultMediator.observe({ lifecycle }) {}

        viewModel.bulkRequestPickup(orderIds)
        advanceTimeBy(20000)

        coVerify(exactly = 1) {
            bulkRequestPickupUseCase.executeOnBackground()
        }

        coVerify(exactly = 1) {
            multiShippingStatusUseCase.executeOnBackground()
        }

        val result = viewModel.bulkRequestPickupFinalResult.value
        assert(result is PartialSuccessNotEligibleFail)
    }

    @Test
    fun getMultiShippingStatus_shouldReturnFailRetryAfterPartialSuccessNotEligibleFailThenAutoRetry() = runTest {
        val orderIds = listOf("0", "1", "2", "4", "5", "6", "7")
        val batchId = "1234566"
        var somListBulkRequestPickupUiModel = SomListBulkRequestPickupUiModel.Data(jobId = batchId, totalOnProcess = 2)
        var multiShippingStatusUiModel = MultiShippingStatusUiModel(
            total_order = 2,
            success = 1,
            fail = 1,
            processed = 2
        )

        coEvery {
            bulkRequestPickupUseCase.executeOnBackground()
        } returns SomListBulkRequestPickupUiModel(
            data = somListBulkRequestPickupUiModel,
            errors = listOf(
                SomListBulkRequestPickupUiModel.ErrorBulkRequestPickup(
                    orderId = "0",
                    message = "Error"
                )
            )
        )
        coEvery {
            multiShippingStatusUseCase.executeOnBackground()
        } returns multiShippingStatusUiModel

        viewModel.bulkRequestPickupFinalResultMediator.observe({ lifecycle }) {}

        viewModel.bulkRequestPickup(orderIds)
        advanceTimeBy(20000)

        coVerify(exactly = 1) {
            bulkRequestPickupUseCase.executeOnBackground()
        }
        coVerify(exactly = 1) {
            multiShippingStatusUseCase.executeOnBackground()
        }

        somListBulkRequestPickupUiModel = SomListBulkRequestPickupUiModel.Data(jobId = batchId, totalOnProcess = 1)
        multiShippingStatusUiModel = MultiShippingStatusUiModel(
            total_order = 2,
            success = 1,
            fail = 1,
            processed = 2
        )
        coEvery {
            bulkRequestPickupUseCase.executeOnBackground()
        } returns SomListBulkRequestPickupUiModel(
            data = somListBulkRequestPickupUiModel,
            errors = listOf(
                SomListBulkRequestPickupUiModel.ErrorBulkRequestPickup(
                    orderId = "0",
                    message = "Error"
                )
            )
        )
        coEvery {
            multiShippingStatusUseCase.executeOnBackground()
        } returns multiShippingStatusUiModel

        viewModel.bulkRequestPickup(orderIds)
        advanceTimeBy(20000)

        coVerify(exactly = 2) {
            bulkRequestPickupUseCase.executeOnBackground()
        }
        coVerify(exactly = 2) {
            multiShippingStatusUseCase.executeOnBackground()
        }

        val result = viewModel.bulkRequestPickupFinalResult.value
        assert(result is FailRetry)
    }

    @Test
    fun getMultiShippingStatus_shouldReturnNotEligibleAndFailAtFirstTry() {
        val orderIds = listOf("0", "1", "2", "4", "5", "6", "7")
        val batchId = "1234566"
        val somListBulkRequestPickupUiModel = SomListBulkRequestPickupUiModel.Data(jobId = batchId, totalOnProcess = 6)
        val multiShippingStatusUiModel = MultiShippingStatusUiModel(
            total_order = 6,
            success = 0,
            fail = 6,
            processed = 6
        )

        coEvery {
            bulkRequestPickupUseCase.executeOnBackground()
        } returns SomListBulkRequestPickupUiModel(
            data = somListBulkRequestPickupUiModel,
            errors = listOf(
                SomListBulkRequestPickupUiModel.ErrorBulkRequestPickup(
                    orderId = "0",
                    message = "Error"
                )
            )
        )

        coEvery {
            multiShippingStatusUseCase.executeOnBackground()
        } returns multiShippingStatusUiModel

        viewModel.bulkRequestPickupFinalResultMediator.observe({ lifecycle }) {}

        runTest {
            viewModel.bulkRequestPickup(orderIds)
            advanceTimeBy(5000)
        }

        coVerify(exactly = 1, timeout = 5000) {
            bulkRequestPickupUseCase.executeOnBackground()
        }

        coVerify(exactly = 1, timeout = 5000) {
            multiShippingStatusUseCase.executeOnBackground()
        }

        val result = viewModel.bulkRequestPickupFinalResult.value
        assert(result is NotEligibleAndFail)
    }

    @Test
    fun getMultiShippingStatus_shouldReturnFailRetryAfterNotEligibleAndFailThenAutoRetry() = runTest {
        val orderIds = listOf("0", "1", "2", "4", "5", "6", "7")
        val batchId = "1234566"
        var somListBulkRequestPickupUiModel = SomListBulkRequestPickupUiModel.Data(jobId = batchId, totalOnProcess = 6)
        var multiShippingStatusUiModel = MultiShippingStatusUiModel(
            total_order = 6,
            success = 0,
            fail = 6,
            processed = 6
        )

        coEvery {
            bulkRequestPickupUseCase.executeOnBackground()
        } returns SomListBulkRequestPickupUiModel(
            data = somListBulkRequestPickupUiModel,
            errors = listOf(
                SomListBulkRequestPickupUiModel.ErrorBulkRequestPickup(
                    orderId = "0",
                    message = "Error"
                )
            )
        )
        coEvery {
            multiShippingStatusUseCase.executeOnBackground()
        } returns multiShippingStatusUiModel

        viewModel.bulkRequestPickupFinalResultMediator.observe({ lifecycle }) {}

        viewModel.bulkRequestPickup(orderIds)
        advanceTimeBy(20000)

        coVerify(exactly = 1) {
            bulkRequestPickupUseCase.executeOnBackground()
        }
        coVerify(exactly = 1) {
            multiShippingStatusUseCase.executeOnBackground()
        }

        somListBulkRequestPickupUiModel = SomListBulkRequestPickupUiModel.Data(jobId = batchId, totalOnProcess = 7)
        multiShippingStatusUiModel = MultiShippingStatusUiModel(
            total_order = 7,
            success = 0,
            fail = 7,
            processed = 7
        )

        coEvery {
            bulkRequestPickupUseCase.executeOnBackground()
        } returns SomListBulkRequestPickupUiModel(
            data = somListBulkRequestPickupUiModel
        )
        coEvery {
            multiShippingStatusUseCase.executeOnBackground()
        } returns multiShippingStatusUiModel

        viewModel.bulkRequestPickup(orderIds)
        advanceTimeBy(20000)

        coVerify(exactly = 2) {
            bulkRequestPickupUseCase.executeOnBackground()
        }
        coVerify(exactly = 2) {
            multiShippingStatusUseCase.executeOnBackground()
        }

        val result = viewModel.bulkRequestPickupFinalResult.value
        assert(result is FailRetry)
    }

    @Test
    fun getMultiShippingStatus_shouldReturnPartialSuccessAtFirstTry() {
        val orderIds = listOf("0", "1", "2", "4", "5", "6", "7")
        val batchId = "1234566"
        val somListBulkRequestPickupUiModel = SomListBulkRequestPickupUiModel.Data(jobId = batchId, totalOnProcess = 7)
        val multiShippingStatusUiModel = MultiShippingStatusUiModel(
            total_order = 7,
            success = 3,
            fail = 4,
            processed = 7
        )

        coEvery {
            bulkRequestPickupUseCase.executeOnBackground()
        } returns SomListBulkRequestPickupUiModel(
            data = somListBulkRequestPickupUiModel
        )

        coEvery {
            multiShippingStatusUseCase.executeOnBackground()
        } returns multiShippingStatusUiModel

        viewModel.bulkRequestPickupFinalResultMediator.observe({ lifecycle }) {}

        runTest {
            viewModel.bulkRequestPickup(orderIds)
            advanceTimeBy(5000)
        }

        coVerify(exactly = 1, timeout = 5000) {
            bulkRequestPickupUseCase.executeOnBackground()
        }

        coVerify(exactly = 1, timeout = 5000) {
            multiShippingStatusUseCase.executeOnBackground()
        }

        val result = viewModel.bulkRequestPickupFinalResult.value
        assert(result is PartialSuccess)
    }

    @Test
    fun getMultiShippingStatus_shouldReturnFailRetryAfterPartialSuccessThenAutoRetry() = runTest {
        val orderIds = listOf("0", "1", "2", "4", "5", "6", "7")
        val batchId = "1234566"
        var somListBulkRequestPickupUiModel = SomListBulkRequestPickupUiModel.Data(jobId = batchId, totalOnProcess = 7)
        var multiShippingStatusUiModel = MultiShippingStatusUiModel(
            total_order = 7,
            success = 3,
            fail = 4,
            processed = 7
        )

        coEvery {
            bulkRequestPickupUseCase.executeOnBackground()
        } returns SomListBulkRequestPickupUiModel(
            data = somListBulkRequestPickupUiModel
        )
        coEvery {
            multiShippingStatusUseCase.executeOnBackground()
        } returns multiShippingStatusUiModel

        viewModel.bulkRequestPickupFinalResultMediator.observe({ lifecycle }) {}

        viewModel.bulkRequestPickup(orderIds)
        advanceTimeBy(20000)

        coVerify(exactly = 1) {
            bulkRequestPickupUseCase.executeOnBackground()
        }
        coVerify(exactly = 1) {
            multiShippingStatusUseCase.executeOnBackground()
        }

        somListBulkRequestPickupUiModel = SomListBulkRequestPickupUiModel.Data(jobId = batchId, totalOnProcess = 4)
        multiShippingStatusUiModel = MultiShippingStatusUiModel(
            total_order = 4,
            success = 2,
            fail = 2,
            processed = 4
        )

        coEvery {
            bulkRequestPickupUseCase.executeOnBackground()
        } returns SomListBulkRequestPickupUiModel(
            data = somListBulkRequestPickupUiModel
        )
        coEvery {
            multiShippingStatusUseCase.executeOnBackground()
        } returns multiShippingStatusUiModel

        viewModel.bulkRequestPickup(orderIds)
        advanceTimeBy(20000)

        coVerify(exactly = 2) {
            bulkRequestPickupUseCase.executeOnBackground()
        }
        coVerify(exactly = 2) {
            multiShippingStatusUseCase.executeOnBackground()
        }

        val result = viewModel.bulkRequestPickupFinalResult.value
        assert(result is FailRetry)
    }

    @Test
    fun getMultiShippingStatus_shouldReturnPartialSuccessNotEligibleAtFirstTry() = runTest {
        val orderIds = listOf("0", "1", "2", "4", "5", "6", "7")
        val batchId = "1234566"
        val somListBulkRequestPickupUiModel = SomListBulkRequestPickupUiModel.Data(jobId = batchId, totalOnProcess = 6)
        val multiShippingStatusUiModel = MultiShippingStatusUiModel(
            total_order = 6,
            success = 6,
            fail = 0,
            processed = 6
        )

        coEvery {
            bulkRequestPickupUseCase.executeOnBackground()
        } returns SomListBulkRequestPickupUiModel(
            data = somListBulkRequestPickupUiModel,
            errors = listOf(
                SomListBulkRequestPickupUiModel.ErrorBulkRequestPickup(
                    orderId = "0",
                    message = "Error"
                )
            )
        )

        coEvery {
            multiShippingStatusUseCase.executeOnBackground()
        } returns multiShippingStatusUiModel

        viewModel.bulkRequestPickupFinalResultMediator.observe({ lifecycle }) {}

        viewModel.bulkRequestPickup(orderIds)
        advanceTimeBy(5000)

        coVerify(exactly = 1, timeout = 5000) {
            bulkRequestPickupUseCase.executeOnBackground()
        }

        coVerify(exactly = 1, timeout = 5000) {
            multiShippingStatusUseCase.executeOnBackground()
        }

        val result = viewModel.bulkRequestPickupFinalResult.value
        assert(result is PartialSuccessNotEligible)
    }

    @Test
    fun getMultiShippingStatus_shouldReturnFailRetryAfterPartialSuccessNotEligibleThenAutoRetry() = runTest {
        val orderIds = listOf("0", "1", "2", "4", "5", "6", "7")
        val batchId = "1234566"
        var somListBulkRequestPickupUiModel = SomListBulkRequestPickupUiModel.Data(jobId = batchId, totalOnProcess = 5)
        var multiShippingStatusUiModel = MultiShippingStatusUiModel(
            total_order = 5,
            success = 5,
            fail = 0,
            processed = 5
        )

        coEvery {
            bulkRequestPickupUseCase.executeOnBackground()
        } returns SomListBulkRequestPickupUiModel(
            data = somListBulkRequestPickupUiModel,
            errors = listOf(
                SomListBulkRequestPickupUiModel.ErrorBulkRequestPickup(
                    orderId = "0",
                    message = "Error"
                )
            )
        )
        coEvery {
            multiShippingStatusUseCase.executeOnBackground()
        } returns multiShippingStatusUiModel

        viewModel.bulkRequestPickupFinalResultMediator.observe({ lifecycle }) {}

        viewModel.bulkRequestPickup(orderIds)
        advanceTimeBy(20000)

        coVerify(exactly = 1) {
            bulkRequestPickupUseCase.executeOnBackground()
        }
        coVerify(exactly = 1) {
            multiShippingStatusUseCase.executeOnBackground()
        }

        somListBulkRequestPickupUiModel = SomListBulkRequestPickupUiModel.Data(jobId = batchId, totalOnProcess = 2)
        multiShippingStatusUiModel = MultiShippingStatusUiModel(
            total_order = 2,
            success = 1,
            fail = 0,
            processed = 1
        )

        coEvery {
            bulkRequestPickupUseCase.executeOnBackground()
        } returns SomListBulkRequestPickupUiModel(
            data = somListBulkRequestPickupUiModel
        )
        coEvery {
            multiShippingStatusUseCase.executeOnBackground()
        } returns multiShippingStatusUiModel

        viewModel.bulkRequestPickup(orderIds)
        advanceTimeBy(20000)

        coVerify(exactly = 2) {
            bulkRequestPickupUseCase.executeOnBackground()
        }
        coVerify(exactly = 12) {
            multiShippingStatusUseCase.executeOnBackground()
        }

        val result = viewModel.bulkRequestPickupFinalResult.value
        assert(result is FailRetry)
    }

    @Test
    fun bulkRequestPickup_shouldReturnAllValidationFail() = runTest {
        val orderIds = listOf("0", "1", "2", "4", "5", "6", "7")
        val batchId = "1234566"
        val somListBulkRequestPickupUiModel = SomListBulkRequestPickupUiModel.Data(jobId = batchId)
        val multiShippingStatusUiModel = MultiShippingStatusUiModel()

        coEvery {
            bulkRequestPickupUseCase.executeOnBackground()
        } returns SomListBulkRequestPickupUiModel(
            data = somListBulkRequestPickupUiModel,
            status = BulkRequestPickupStatus.SUCCESS_NOT_PROCESSED
        )

        coEvery {
            multiShippingStatusUseCase.executeOnBackground()
        } returns multiShippingStatusUiModel

        viewModel.bulkRequestPickupFinalResultMediator.observe({ lifecycle }) {}

        viewModel.bulkRequestPickup(orderIds)
        advanceTimeBy(20000)

        coVerify(exactly = 1) {
            bulkRequestPickupUseCase.executeOnBackground()
        }

        coVerify(exactly = 1) {
            multiShippingStatusUseCase.executeOnBackground()
        }

        val result = viewModel.bulkRequestPickupFinalResult.value
        assert(result is AllValidationFail)
    }

    @Test
    fun getMultiShippingStatus_shouldReturnAllFailEligibleAtFirstTry() = runTest {
        val orderIds = listOf("0", "1", "2", "4", "5", "6", "7")
        val batchId = "1234566"
        val somListBulkRequestPickupUiModel = SomListBulkRequestPickupUiModel.Data(jobId = batchId, totalOnProcess = 7)
        val multiShippingStatusUiModel = MultiShippingStatusUiModel(
            total_order = 7,
            success = 0,
            fail = 7,
            processed = 7
        )

        coEvery {
            bulkRequestPickupUseCase.executeOnBackground()
        } returns SomListBulkRequestPickupUiModel(
            data = somListBulkRequestPickupUiModel
        )

        coEvery {
            multiShippingStatusUseCase.executeOnBackground()
        } returns multiShippingStatusUiModel

        viewModel.bulkRequestPickupFinalResultMediator.observe({ lifecycle }) {}

        viewModel.bulkRequestPickup(orderIds)
        advanceTimeBy(20000)

        coVerify(exactly = 1) {
            bulkRequestPickupUseCase.executeOnBackground()
        }

        coVerify(exactly = 1) {
            multiShippingStatusUseCase.executeOnBackground()
        }

        val result = viewModel.bulkRequestPickupFinalResult.value
        assert(result is AllFailEligible)
    }

    @Test
    fun getMultiShippingStatus_shouldReturnFailRetryAfterAllFailEligibleThenAutoRetry() = runTest {
        val orderIds = listOf("0", "1", "2", "4", "5", "6", "7")
        val batchId = "1234566"
        var somListBulkRequestPickupUiModel = SomListBulkRequestPickupUiModel.Data(jobId = batchId, totalOnProcess = 7)
        var multiShippingStatusUiModel = MultiShippingStatusUiModel(
            total_order = 7,
            success = 0,
            fail = 7,
            processed = 7
        )

        coEvery {
            bulkRequestPickupUseCase.executeOnBackground()
        } returns SomListBulkRequestPickupUiModel(
            data = somListBulkRequestPickupUiModel
        )
        coEvery {
            multiShippingStatusUseCase.executeOnBackground()
        } returns multiShippingStatusUiModel

        viewModel.bulkRequestPickupFinalResultMediator.observe({ lifecycle }) {}
        viewModel.bulkRequestPickup(orderIds)
        advanceTimeBy(20000)

        coVerify(exactly = 1) {
            bulkRequestPickupUseCase.executeOnBackground()
        }
        coVerify(exactly = 1) {
            multiShippingStatusUseCase.executeOnBackground()
        }

        viewModel.bulkRequestPickup(orderIds)
        advanceTimeBy(20000)

        coVerify(exactly = 2) {
            bulkRequestPickupUseCase.executeOnBackground()
        }
        coVerify(exactly = 2) {
            multiShippingStatusUseCase.executeOnBackground()
        }

        val result = viewModel.bulkRequestPickupFinalResult.value
        assert(result is FailRetry)
    }

    @Test
    fun getTickers_shouldSuccess() = coroutineTestRule.runTest {
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
    fun getTickers_shouldFailed() = coroutineTestRule.runTest {
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
    fun `filterResult should equals to Success when get filters from cache and cloud is success`() = coroutineTestRule.runTest {
        val mockResponse = TestHelper.createSuccessResponse<SomListFilterResponse.Data>(
            "json/som_list_get_order_filter_success_response.json"
        )
        coEvery {
            somListGetFilterListUseCase.executeOnBackground()
        } answers {
            FilterResultMapper().mapResponseToUiModel(mockResponse, false)
        }

        getAdminPermission_shouldSuccess()
        (somCanShowOrderDataField.get(viewModel) as MediatorLiveData<*>).observeAwaitValue()
        viewModel.getFilters(true)

        coVerify(exactly = 1) {
            somListGetFilterListUseCase.executeOnBackground()
        }

        val result = viewModel.filterResult.observeAwaitValue()

        assert(result is Success && !result.data.fromCache)
        assertEquals("", viewModel.getTabActiveFromAppLink())
    }

    @Test
    fun `filterResult should equals to Success when get filters from cache is failed and cloud is success`() = coroutineTestRule.runTest {
        val mockResponse = TestHelper.createSuccessResponse<SomListFilterResponse.Data>(
            "json/som_list_get_order_filter_success_response.json"
        )
        coEvery {
            somListGetFilterListUseCase.executeOnBackground()
        } returns FilterResultMapper().mapResponseToUiModel(mockResponse, false)

        getAdminPermission_shouldSuccess()
        (somCanShowOrderDataField.get(viewModel) as MediatorLiveData<*>).observeAwaitValue()
        viewModel.getFilters(true)

        coVerify(exactly = 1) {
            somListGetFilterListUseCase.executeOnBackground()
        }

        val result = viewModel.filterResult.observeAwaitValue()

        assert(result is Success && !result.data.fromCache)
    }

    @Test
    fun `filterResult should equals to Fail when get filters from cache is success and cloud is failed`() = coroutineTestRule.runTest {
        val mockResponse = TestHelper.createSuccessResponse<SomListFilterResponse.Data>(
            "json/som_list_get_order_filter_success_response.json"
        )
        coEvery {
            somListGetFilterListUseCase.executeOnBackground()
        } throws Throwable()

        getAdminPermission_shouldSuccess()
        (somCanShowOrderDataField.get(viewModel) as MediatorLiveData<*>).observeAwaitValue()
        viewModel.getFilters(true)

        coVerify(exactly = 1) {
            somListGetFilterListUseCase.executeOnBackground()
        }

        val result = viewModel.filterResult.observeAwaitValue()

        assert(result is Fail)
    }

    @Test
    fun `filterResult should equals to Fail when get filters from cache and cloud is failed`() = coroutineTestRule.runTest {
        coEvery {
            somListGetFilterListUseCase.executeOnBackground()
        } throws Throwable()

        getAdminPermission_shouldSuccess()
        (somCanShowOrderDataField.get(viewModel) as MediatorLiveData<*>).observeAwaitValue()
        viewModel.getFilters(true)

        coVerify(exactly = 1) {
            somListGetFilterListUseCase.executeOnBackground()
        }

        val result = viewModel.filterResult.observeAwaitValue()

        assert(result is Fail)
    }

    @Test
    fun `filterResult#refreshOrder should true after get filter data from cloud when refresh order is requested and get filters from cache is failed and cloud is success`() = coroutineTestRule.runTest {
        val mockResponse = TestHelper.createSuccessResponse<SomListFilterResponse.Data>(
            "json/som_list_get_order_filter_success_response.json"
        )
        coEvery {
            somListGetFilterListUseCase.executeOnBackground()
        } returns FilterResultMapper().mapResponseToUiModel(mockResponse, false)

        getAdminPermission_shouldSuccess()
        (somCanShowOrderDataField.get(viewModel) as MediatorLiveData<*>).observeAwaitValue()
        viewModel.getFilters(true)

        coVerify(exactly = 1) {
            somListGetFilterListUseCase.executeOnBackground()
        }

        val result = viewModel.filterResult.observeAwaitValue()

        assert(result is Success && result.data.refreshOrder)
    }

    @Test
    fun `filterResult#refreshOrder should false after get filter data from cloud when refresh order is not requested and get filters from cache is failed and cloud is success`() = coroutineTestRule.runTest {
        val mockResponse = TestHelper.createSuccessResponse<SomListFilterResponse.Data>(
            "json/som_list_get_order_filter_success_response.json"
        )
        coEvery {
            somListGetFilterListUseCase.executeOnBackground()
        } returns FilterResultMapper().mapResponseToUiModel(mockResponse, false)

        getAdminPermission_shouldSuccess()
        (somCanShowOrderDataField.get(viewModel) as MediatorLiveData<*>).observeAwaitValue()
        viewModel.getFilters(false)

        coVerify(exactly = 1) {
            somListGetFilterListUseCase.executeOnBackground()
        }

        val result = viewModel.filterResult.observeAwaitValue()

        assert(result is Success && !result.data.refreshOrder)
    }

    @Test
    fun `filterResult#refreshOrder should false after get filter data from cloud when refresh order is requested and get filters from cache is success`() = coroutineTestRule.runTest {
        val mockResponse = TestHelper.createSuccessResponse<SomListFilterResponse.Data>(
            "json/som_list_get_order_filter_success_response.json"
        )

        coEvery {
            somListGetFilterListUseCase.executeOnBackground()
        } answers {
            FilterResultMapper().mapResponseToUiModel(mockResponse, false)
        }

        getAdminPermission_shouldSuccess()
        (somCanShowOrderDataField.get(viewModel) as MediatorLiveData<*>).observeAwaitValue()
        viewModel.getFilters(true)

        coVerify(exactly = 1) {
            somListGetFilterListUseCase.executeOnBackground()
        }

        val result = viewModel.filterResult.observeAwaitValue()

        assert(result is Success && result.data.refreshOrder)
    }

    @Test
    fun getFilters_shouldNotSendRequest_whenCannotShowOrderData() = coroutineTestRule.runTest {
        getAdminPermission_shouldFail()
        (somCanShowOrderDataField.get(viewModel) as MediatorLiveData<*>).observe({ lifecycle }) {}
        viewModel.getFilters(false)

        coVerify(exactly = 0) {
            somListGetFilterListUseCase.executeOnBackground(any())
        }

        assert(viewModel.filterResult.value == null)
    }

    @Test
    fun getHeaderIconsInfo_shouldSuccess() = coroutineTestRule.runTest {
        val headerIconsInfoData = SomListHeaderIconsInfoUiModel(mockk(relaxed = true), mockk(relaxed = true))
        coEvery {
            somListGetHeaderIconsInfoUseCase.executeOnBackground()
        } returns headerIconsInfoData

        viewModel.getHeaderIconsInfo()

        coVerify {
            somListGetHeaderIconsInfoUseCase.executeOnBackground()
        }
        val waitingPaymentCounterResult = viewModel.somListHeaderIconsInfoResult.observeAwaitValue()
        assert(waitingPaymentCounterResult is Success && waitingPaymentCounterResult.data == headerIconsInfoData)
    }

    @Test
    fun getHeaderIconsInfo_shouldFailed() = coroutineTestRule.runTest {
        coEvery {
            somListGetHeaderIconsInfoUseCase.executeOnBackground()
        } throws Throwable()

        viewModel.getHeaderIconsInfo()

        coVerify {
            somListGetHeaderIconsInfoUseCase.executeOnBackground()
        }

        assert(viewModel.somListHeaderIconsInfoResult.observeAwaitValue() is Fail)
    }

    @Test
    fun getHeaderIconsInfo_shouldNotSuccess_whenCannotShowOrderData() = coroutineTestRule.runTest {
        coEvery {
            somListGetHeaderIconsInfoUseCase.executeOnBackground(any())
        } returns SomListHeaderIconsInfoUiModel(mockk(relaxed = true), mockk(relaxed = true))

        somCanShowOrderDataField.set(viewModel, MediatorLiveData<Boolean>().apply { value = false })

        viewModel.getHeaderIconsInfo()

        coVerify(exactly = 0) {
            somListGetHeaderIconsInfoUseCase.executeOnBackground(any())
        }

        assertFalse(viewModel.somListHeaderIconsInfoResult.observeAwaitValue() is Success)
    }

    @Test
    fun getOrderList_shouldSuccessAndClearAllFailedRefreshOrder() = coroutineTestRule.runTest {
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
                Triple("0", listOf(), SomListEmptyStateUiModel())
            }
        }

        viewModel.refreshSelectedOrder(orderId, invoice)
        somGetOrderListJobField.set(viewModel, null)
        viewModel.getOrderList()

        coVerify {
            somListGetOrderListUseCase.executeOnBackground(any())
        }

        assert(viewModel.orderListWrapperResult.observeAwaitValue() is Success && !viewModel.hasNextPage() && !viewModel.containsFailedRefreshOrder)
        assert(viewModel.isLoadingOrder.observeAwaitValue() == false)
    }

    @Test
    fun getOrderList_shouldSuccessNotClearAllFailedRefreshOrder() = coroutineTestRule.runTest {
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
                Triple("0", listOf(), SomListEmptyStateUiModel())
            }
        }

        viewModel.refreshSelectedOrder(orderId, invoice)
        somGetOrderListJobField.set(viewModel, null)
        viewModel.getOrderList()

        coVerify {
            somListGetOrderListUseCase.executeOnBackground(any())
        }

        assert(viewModel.orderListWrapperResult.observeAwaitValue() is Success && !viewModel.hasNextPage() && viewModel.containsFailedRefreshOrder)
        assert(viewModel.isLoadingOrder.observeAwaitValue() == false)
    }

    @Test
    fun getOrderList_shouldCancelOldJobAndSuccess() = coroutineTestRule.runTest {
        val getOrderListJob = mockk<Job>(relaxed = true)
        coEvery {
            somListGetOrderListUseCase.executeOnBackground(any())
        } returns Triple("0", listOf(), SomListEmptyStateUiModel())

        somGetOrderListJobField.set(viewModel, getOrderListJob)
        viewModel.getOrderList()

        coVerify {
            somListGetOrderListUseCase.executeOnBackground(any())
            getOrderListJob.cancel()
        }

        assert(viewModel.orderListWrapperResult.observeAwaitValue() is Success && !viewModel.hasNextPage())
    }

    @Test
    fun getOrderList_shouldFailed() = coroutineTestRule.runTest {
        coEvery {
            somListGetOrderListUseCase.executeOnBackground(any())
        } throws Throwable()

        viewModel.getOrderList()

        coVerify {
            somListGetOrderListUseCase.executeOnBackground(any())
        }

        assert(viewModel.orderListWrapperResult.observeAwaitValue() is Fail)
        assert(viewModel.isLoadingOrder.observeAwaitValue() == false)
    }

    @Test
    fun getOrderList_shouldNotSuccess_whenCannotShowOrderData() = coroutineTestRule.runTest {
        coEvery {
            somListGetOrderListUseCase.executeOnBackground(any())
        } returns Triple("0", listOf(), SomListEmptyStateUiModel())

        somCanShowOrderDataField.set(viewModel, MediatorLiveData<Boolean>().apply { value = false })

        viewModel.getOrderList()

        coVerify(exactly = 0) {
            somListGetOrderListUseCase.executeOnBackground(any())
        }

        assertFalse(viewModel.orderListWrapperResult.observeAwaitValue() is Success)
    }

    @Test
    fun refreshSelectedOrder_shouldSuccess() = coroutineTestRule.runTest {
        val orderId = "1234567890"
        val invoice = "INV/20200922/XX/IX/123456789"
        val order = SomListOrderUiModel(searchParam = invoice, orderId = orderId)

        coEvery {
            somListGetOrderListUseCase.executeOnBackground(any())
        } returns Triple("0", listOf(order), null)

        viewModel.refreshSelectedOrder(orderId, invoice)

        coVerify {
            somListGetOrderListUseCase.executeOnBackground(any())
        }

        val refreshOrderResult = viewModel.refreshOrderResult.observeAwaitValue()
        assert(refreshOrderResult is Success && refreshOrderResult.data.orderId == orderId && refreshOrderResult.data.order == order)
        assert(viewModel.isLoadingOrder.observeAwaitValue() == false)
    }

    @Test
    fun refreshSelectedOrder_shouldFailed() = coroutineTestRule.runTest {
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
    fun refreshSelectedOrder_shouldNotRefreshingWhenFetchingInitialOrderList() = coroutineTestRule.runTest {
        val orderId = "1234567890"
        val invoice = "INV/20200922/XX/IX/123456789"
        val order = SomListOrderUiModel(searchParam = invoice, orderId = orderId)

        every {
            viewModel.isRefreshingAllOrder()
        } returns true
        coEvery {
            somListGetOrderListUseCase.executeOnBackground(any())
        } returns Triple("0", listOf(order), null)

        viewModel.refreshSelectedOrder(orderId, invoice)

        coVerify(inverse = true) {
            somListGetOrderListUseCase.executeOnBackground(any())
        }
        val refreshOrderResult = viewModel.refreshOrderResult.observeAwaitValue()
        assert(refreshOrderResult == null)
        assert(viewModel.isLoadingOrder.observeAwaitValue() == null)
    }

    @Test
    fun retryRefreshSelectedOrder_shouldSuccess() = coroutineTestRule.runTest {
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
    fun retryRefreshSelectedOrder_shouldNotRetryWhenThereIsNoFailedJob() = coroutineTestRule.runTest {
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
    fun getTopAdsCategory_shouldSuccess() = coroutineTestRule.runTest {
        doGetTopAdsCategory_shouldSuccess()
    }

    @Test
    fun getTopAdsCategory_shouldFailed() = coroutineTestRule.runTest {
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
    fun getAdminPermission_shouldSuccess() = coroutineTestRule.runTest {
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
    fun getAdminPermission_shouldFail() = coroutineTestRule.runTest {
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
    fun getAdminPermission_whenIsShopOwner_shouldNotRunAndUserShouldEligible() = coroutineTestRule.runTest {
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
    fun getAdminPermission_whenIsNotShopOwnerButIsShopAdmin_shouldRun() = coroutineTestRule.runTest {
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
    fun isTopAdsActive_shouldReturnFalseWhenSuccess() = coroutineTestRule.runTest {
        doGetTopAdsCategory_shouldSuccess()

        assert(!viewModel.isTopAdsActive())
    }

    @Test
    fun isTopAdsActive_shouldReturnFalse() = coroutineTestRule.runTest {
        doGetTopAdsCategory_shouldSuccess(2)

        assert(!viewModel.isTopAdsActive())
    }

    @Test
    fun isTopAdsActive_shouldReturnFalseWhenFailed() = coroutineTestRule.runTest {
        getTopAdsCategory_shouldFailed()

        assert(!viewModel.isTopAdsActive())
    }

    @Test
    fun isTopAdsActive_shouldReturnFalseWhenValueIsNull() = coroutineTestRule.runTest {
        assert(!viewModel.isTopAdsActive())
    }

    @Test
    fun isTopAdsActive_shouldReturnTrueWhenUsingManualAds() = coroutineTestRule.runTest {
        doGetTopAdsCategory_shouldSuccess(3)

        assert(viewModel.isTopAdsActive())
    }

    @Test
    fun isTopAdsActive_shouldReturnTrueWhenUsingAutoAds() = coroutineTestRule.runTest {
        doGetTopAdsCategory_shouldSuccess(4)

        assert(viewModel.isTopAdsActive())
    }

    @Test
    fun setStatusOrderFilterTest() = coroutineTestRule.runTest {
        val statusOrderIds = listOf(220, 400)
        val statusKey = "new_order"
        setGetDataOrderListParams()
        viewModel.setStatusOrderFilter(statusOrderIds, statusKey)

        assert(viewModel.getDataOrderListParams().statusList == statusOrderIds)
        assert(viewModel.getDataOrderListParams().statusKey == statusKey)
    }

    @Test
    fun resetGetOrderListParamTest() = coroutineTestRule.runTest {
        val somListGetOrderListParam = SomListGetOrderListParam()
        setGetDataOrderListParams()

        viewModel.resetGetOrderListParam()

        assert(viewModel.getDataOrderListParams() == somListGetOrderListParam)
    }

    @Test
    fun hasNextPage_shouldReturnFalse() = coroutineTestRule.runTest {
        viewModel.resetGetOrderListParam()
        assert(!viewModel.hasNextPage())
    }

    @Test
    fun hasNextPage_shouldReturnTrue() = coroutineTestRule.runTest {
        setGetDataOrderListParams()
        assert(viewModel.hasNextPage())
    }

    @Test
    fun setSortOrderByTest() = coroutineTestRule.runTest {
        val sortBy = SomConsts.SORT_BY_TOTAL_OPEN_DESCENDING
        setGetDataOrderListParams()
        viewModel.setSortOrderBy(sortBy)
        assert(viewModel.getDataOrderListParams().sortBy == sortBy)
    }

    @Test
    fun setIsMultiSelectEnabledTest() = coroutineTestRule.runTest {
        val mockMultiSelectEnabled = true
        viewModel.isMultiSelectEnabled = mockMultiSelectEnabled
        assert(viewModel.isMultiSelectEnabled == mockMultiSelectEnabled)
    }

    @Test
    fun setSearchParamTest() = coroutineTestRule.runTest {
        val searchParam = "Produk pembasmi hama"
        viewModel.setSearchParam(searchParam)
        assert(viewModel.getDataOrderListParams().search == searchParam)
    }

    @Test
    fun isRefreshingAllOrder_shouldReturnFalseWhenJobIsNull() = coroutineTestRule.runTest {
        somGetOrderListJobField.set(viewModel, null)
        assert(!viewModel.isRefreshingAllOrder())
    }

    @Test
    fun isRefreshingAllOrder_shouldReturnFalseWhenJobIsCompleted() = coroutineTestRule.runTest {
        val somGetOrderListJob = mockk<Job>(relaxed = true)
        somGetOrderListJobField.set(viewModel, somGetOrderListJob)

        every {
            somGetOrderListJob.isCompleted
        } returns true

        assert(!viewModel.isRefreshingAllOrder())
    }

    @Test
    fun isRefreshingAllOrder_shouldReturnTrueWhenJobIsNotYetComplete() = coroutineTestRule.runTest {
        val somGetOrderListJob = mockk<Job>(relaxed = true)
        somGetOrderListJobField.set(viewModel, somGetOrderListJob)

        every {
            somGetOrderListJob.isCompleted
        } returns false

        assert(viewModel.isRefreshingAllOrder())
    }

    @Test
    fun isRefreshingSelectedOrder_shouldReturnFalseWhenThereIsNoRunningJob() = coroutineTestRule.runTest {
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
    fun isRefreshingSelectedOrder_shouldReturnTrueWhenThereIsAnyRunningJob() = coroutineTestRule.runTest {
        val somRefreshOrderJob = mockk<Job>(relaxed = true)
        val somRefreshOrder = RefreshOrder("", "", somRefreshOrderJob)
        somRefreshOrderJobField.set(viewModel, arrayListOf(somRefreshOrder))

        every {
            somRefreshOrderJob.isCompleted
        } returns false

        assert(viewModel.isRefreshingSelectedOrder())
    }

    @Test
    fun isRefreshingOrder_shouldReturnFalseWhenNotRefreshingOrderAndNotFetchingOrderList() = coroutineTestRule.runTest {
        isRefreshingAllOrder_shouldReturnFalseWhenJobIsCompleted()
        isRefreshingSelectedOrder_shouldReturnFalseWhenThereIsNoRunningJob()
        assert(!viewModel.isRefreshingOrder())
    }

    @Test
    fun isRefreshingOrder_shouldReturnTrueWhenFetchingOrderList() = coroutineTestRule.runTest {
        isRefreshingAllOrder_shouldReturnTrueWhenJobIsNotYetComplete()
        isRefreshingSelectedOrder_shouldReturnFalseWhenThereIsNoRunningJob()
        assert(viewModel.isRefreshingOrder())
    }

    @Test
    fun isRefreshingOrder_shouldReturnTrueWhenRefreshingOrder() = coroutineTestRule.runTest {
        isRefreshingAllOrder_shouldReturnFalseWhenJobIsCompleted()
        isRefreshingSelectedOrder_shouldReturnTrueWhenThereIsAnyRunningJob()
        assert(viewModel.isRefreshingOrder())
    }

    @Test
    fun isRefreshingOrder_shouldReturnTrueWhenRefreshingOrderAndFetchingOrderList() = coroutineTestRule.runTest {
        isRefreshingAllOrder_shouldReturnTrueWhenJobIsNotYetComplete()
        isRefreshingSelectedOrder_shouldReturnTrueWhenThereIsAnyRunningJob()
        assert(viewModel.isRefreshingOrder())
    }

    @Test
    fun getFailingOrderIdsFromBulkRequestPickupStatus_shouldReturnNonEmptyList() = coroutineTestRule.runTest {
        val multiShippingStatusResult = MultiShippingStatusUiModel(
            listError = listOf(
                MultiShippingStatusUiModel.ErrorMultiShippingStatusUiModel("Error", "123")
            )
        )
        val result = getFailingOrderIdsFromBulkRequestPickupStatus.invoke(viewModel, multiShippingStatusResult)

        assert((result as List<*>).isNotEmpty())
    }

    @Test
    fun getFailingOrderIdsFromBulkRequestPickupStatus_shouldReturnEmptyList() = coroutineTestRule.runTest {
        val multiShippingStatusResult = MultiShippingStatusUiModel()
        val result = getFailingOrderIdsFromBulkRequestPickupStatus.invoke(viewModel, multiShippingStatusResult)

        assert((result as List<*>).isEmpty())
    }

    @Test
    fun getSuccessBulkRequestPickupResultData_shouldReturnNonNullData() = coroutineTestRule.runTest {
        bulkRequestPickup_shouldSuccess()
        val result = getSuccessBulkRequestPickupResultData.invoke(viewModel)

        assert(result != null)
    }

    @Test
    fun getSuccessBulkRequestPickupResultData_shouldReturnNullData() = coroutineTestRule.runTest {
        bulkRequestPickup_shouldFailed()
        val result = getSuccessBulkRequestPickupResultData.invoke(viewModel)
        assert(result == null)
    }

    @Test
    fun getTotalNotEligibleOrderFromBulkRequestPickupResult_shouldReturnNonZeroWhenThereIsAnyNotEligibleOrder() = coroutineTestRule.runTest {
        val expectedResult = 1
        val bulkRequestPickupResultData = SomListBulkRequestPickupUiModel(
            data = SomListBulkRequestPickupUiModel.Data(),
            errors = listOf(
                SomListBulkRequestPickupUiModel.ErrorBulkRequestPickup(
                    message = "Error",
                    orderId = "123"
                )
            )
        )

        doSuccessBulkRequestPickup(bulkRequestPickupResultData)
        val result = getTotalNotEligibleOrderFromBulkRequestPickupResult.invoke(viewModel)

        assert(result == expectedResult)
    }

    @Test
    fun getTotalNotEligibleOrderFromBulkRequestPickupResult_shouldReturnZeroWhenBulkRequestPickupResultIsFail() = coroutineTestRule.runTest {
        bulkRequestPickup_shouldFailed()
        val result = getTotalNotEligibleOrderFromBulkRequestPickupResult.invoke(viewModel)
        assert(result == Int.ZERO)
    }

    @Test
    fun getTotalEligibleOrderFromBulkRequestPickupResult_shouldReturnNonZeroWhenThereIsAnyEligibleOrder() = coroutineTestRule.runTest {
        val expectedResult = 1
        val bulkRequestPickupResultData = SomListBulkRequestPickupUiModel(
            data = SomListBulkRequestPickupUiModel.Data(
                totalOnProcess = 1
            )
        )

        doSuccessBulkRequestPickup(bulkRequestPickupResultData)
        val result = getTotalEligibleOrderFromBulkRequestPickupResult.invoke(viewModel)

        assert(result == expectedResult)
    }

    @Test
    fun getTotalEligibleOrderFromBulkRequestPickupResult_shouldReturnZeroWhenThereIsNoEligibleOrder() = coroutineTestRule.runTest {
        val bulkRequestPickupResultData = SomListBulkRequestPickupUiModel(
            data = SomListBulkRequestPickupUiModel.Data()
        )
        doSuccessBulkRequestPickup(bulkRequestPickupResultData)
        val result = getTotalEligibleOrderFromBulkRequestPickupResult.invoke(viewModel)

        assert(result == Int.ZERO)
    }

    @Test
    fun getTotalEligibleOrderFromBulkRequestPickupResult_shouldReturnZeroWhenBulkRequestPickupResultIsFail() = coroutineTestRule.runTest {
        bulkRequestPickup_shouldFailed()
        val result = getTotalEligibleOrderFromBulkRequestPickupResult.invoke(viewModel)
        assert(result == Int.ZERO)
    }

    @Test
    fun getBulkRequestPickupJobID_shouldReturnExpectedJobID() = coroutineTestRule.runTest {
        val expectedJobID = "12345"
        val bulkRequestPickupResultData = SomListBulkRequestPickupUiModel(
            data = SomListBulkRequestPickupUiModel.Data(
                jobId = expectedJobID
            )
        )
        doSuccessBulkRequestPickup(bulkRequestPickupResultData)
        val result = getBulkRequestPickupJobID.invoke(viewModel)
        assert(result == expectedJobID)
    }

    @Test
    fun getBulkRequestPickupJobID_shouldReturnEmptyStringWhenBulkRequestPickupResultIsFail() = coroutineTestRule.runTest {
        val expectedJobID = ""
        bulkRequestPickup_shouldFailed()
        val result = getBulkRequestPickupJobID.invoke(viewModel)
        assert(result == expectedJobID)
    }

    @Test
    fun getBulkRequestPickupStatus_shouldReturnExpectedStatus() = coroutineTestRule.runTest {
        val expectedStatus = 220
        val bulkRequestPickupResultData = SomListBulkRequestPickupUiModel(
            status = 220
        )
        doSuccessBulkRequestPickup(bulkRequestPickupResultData)
        val result = getBulkRequestPickupStatus.invoke(viewModel)
        assert(result == expectedStatus)
    }

    @Test
    fun getBulkRequestPickupStatus_shouldReturnZeroWhenBulkRequestPickupResultIsFail() = coroutineTestRule.runTest {
        val expectedStatus = 0
        bulkRequestPickup_shouldFailed()
        val result = getBulkRequestPickupStatus.invoke(viewModel)
        assert(result == expectedStatus)
    }

    @Test
    fun waitForGetFiltersCompleted_shouldWaitUntilComplete() = coroutineTestRule.runTest {
        somGetFiltersJobField.set(viewModel, launch { delay(1000) })
        waitForGetFiltersCompleted.invokeSuspend(viewModel)
        assert((somGetFiltersJobField.get(viewModel) as? Job)?.isCompleted == true)
    }

    @Test
    fun onReceiveBulkRequestPickupStatusResult_shouldDoNothingIfReceiveNull() = coroutineTestRule.runTest {
        onReceiveBulkRequestPickupStatusResult.invoke(viewModel, null)
        coVerify(inverse = true) {
            multiShippingStatusUseCase.executeOnBackground()
        }
        assertNull(viewModel.bulkRequestPickupFinalResult.value)
    }

    @Test
    fun onReceiveBulkAcceptOrderStatusResult_shouldDoNothingIfReceiveNull() = coroutineTestRule.runTest {
        onReceiveBulkAcceptOrderStatusResult.invoke(viewModel, null)
        coVerify(inverse = true) {
            bulkAcceptOrderStatusUseCase.executeOnBackground()
        }
        assertNull(viewModel.bulkAcceptOrderStatusResult.value)
    }

    @Test
    fun addOrderTypeFilter_shouldUpdateGetOrderListParams() {
        val orderTypeFilterId = 10L
        viewModel.addOrderTypeFilter(orderTypeFilterId)
        assertEquals(mutableSetOf(orderTypeFilterId), viewModel.getDataOrderListParams().orderTypeList)
    }

    @Test
    fun removeOrderTypeFilter_shouldUpdateGetOrderListParams() {
        val orderTypeFilterId = 10L
        viewModel.addOrderTypeFilter(orderTypeFilterId)
        viewModel.removeOrderTypeFilter(orderTypeFilterId)
        assertEquals(mutableSetOf<Long>(), viewModel.getDataOrderListParams().orderTypeList)
    }

    @Test
    fun addShippingFilter_shouldUpdateGetOrderListParams() {
        val shippingFilterId = 10L
        viewModel.addShippingFilter(shippingFilterId)
        assertEquals(mutableSetOf(shippingFilterId), viewModel.getDataOrderListParams().shippingList)
    }

    @Test
    fun removeShippingFilter_shouldUpdateGetOrderListParams() {
        val shippingFilterId = 10L
        viewModel.addShippingFilter(shippingFilterId)
        viewModel.removeShippingFilter(shippingFilterId)
        assertEquals(mutableSetOf<Long>(), viewModel.getDataOrderListParams().shippingList)
    }

    @Test
    fun updateSomFilterUiModelList_shouldUpdateSomFilterUiModelList() {
        val somFilterUiModelList = GetSomFilterMapper.mapToSomFilterVisitable(
            TestHelper.createSuccessResponse(
                SomFilterViewModelTestFixture.SOM_FILTER_SUCCESS_RESPONSE
            )
        ).filterIsInstance<SomFilterUiModel>()
        viewModel.updateSomFilterUiModelList(somFilterUiModelList)
        assertEquals(somFilterUiModelList, viewModel.getSomFilterUi())
    }

    @Test
    fun clearSomFilterUiModelList_shouldUpdateSomFilterUiModelList() {
        updateSomFilterUiModelList_shouldUpdateSomFilterUiModelList()
        viewModel.clearSomFilterUiModelList()
        assertEquals(emptyList<SomFilterUiModel>(), viewModel.getSomFilterUi())
    }

    @Test
    fun getTabActive_shouldReturnCorrespondingOrderStatusFilterKeyWhenFilterResultIsNotNull() {
        `filterResult should equals to Success when get filters from cache and cloud is success`()
        val statusKey = "new_order"
        viewModel.setStatusOrderFilter(listOf(220), statusKey)
        assertEquals("new_order", viewModel.getTabActive())
        assertEquals(statusKey, viewModel.getDataOrderListParams().statusKey)
    }

    @Test
    fun getTabActive_shouldReturnOrderStatusFilterFromAppLinkWhenFilterResultIsNull() {
        viewModel.setTabActiveFromAppLink("new_order")
        assertEquals("new_order", viewModel.getTabActiveFromAppLink())
    }

    @Test
    fun getIsFirstPageOpened_shouldReturnSuccessWhenFirstPageOpenedIsTrue() {
        viewModel.setFirstPageOpened(true)
        assertEquals(true, viewModel.getIsFirstPageOpened())
    }

    @Test
    fun getIsFirstPageOpened_shouldReturnSuccessWhenFirstPageOpenedIsFalse() {
        viewModel.setFirstPageOpened(false)
        assertEquals(false, viewModel.getIsFirstPageOpened())
    }

    private fun doSuccessBulkAcceptOrder(
        orderIds: List<String>
    ) = runTest {
        coEvery {
            bulkAcceptOrderUseCase.executeOnBackground()
        } returns SomListBulkAcceptOrderUiModel(SomListBulkAcceptOrderUiModel.Data())
        viewModel.bulkAcceptOrder(orderIds)
        advanceTimeBy(5000)
    }

    private fun doFailedBulkAcceptOrder(
        orderIds: List<String>
    ) = coroutineTestRule.runTest {
        coEvery {
            bulkAcceptOrderUseCase.executeOnBackground()
        } throws Throwable()
        viewModel.bulkAcceptOrder(orderIds)
    }

    private fun doSuccessBulkRequestPickup(
        data: SomListBulkRequestPickupUiModel
    ) = runTest {
        val orderIds = listOf("0234", "1456", "2678")
        coEvery {
            bulkRequestPickupUseCase.executeOnBackground()
        } returns data
        viewModel.bulkRequestPickup(orderIds)
        advanceTimeBy(5000)
    }

    private fun doFailedBulkRequestPickup() = runTest {
        val orderIds = listOf("0", "1", "2")
        coEvery {
            bulkRequestPickupUseCase.executeOnBackground()
        } throws Throwable()
        viewModel.bulkRequestPickup(orderIds)
        advanceTimeBy(5000)
    }
}
