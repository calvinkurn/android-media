package com.tokopedia.sellerappwidget.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.sellerappwidget.common.Const
import com.tokopedia.sellerappwidget.domain.usecase.GetOrderUseCase
import com.tokopedia.sellerappwidget.view.model.OrderItemUiModel
import com.tokopedia.sellerappwidget.view.model.OrderUiModel
import com.tokopedia.sellerappwidget.view.viewmodel.view.AppWidgetView
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers

/**
 * Created By @ilhamsuaib on 21/12/20
 */

@ExperimentalCoroutinesApi
class OrderAppWidgetViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @RelaxedMockK
    lateinit var mView: AppWidgetView<OrderUiModel>

    @RelaxedMockK
    lateinit var getNewOrderUseCase: GetOrderUseCase

    @RelaxedMockK
    lateinit var getReadyToShipUseCase: GetOrderUseCase

    private lateinit var mViewModel: OrderAppWidgetViewModel

    private val startDate = ArgumentMatchers.anyString()
    private val endDate = ArgumentMatchers.anyString()
    private val newOrderParams = GetOrderUseCase.createParams(startDate, endDate,
            Const.OrderStatusId.NEW_ORDER, Const.OrderListSortBy.SORT_BY_PAYMENT_DATE_DESCENDING)
    private val readyToShipParams = GetOrderUseCase.createParams(startDate, endDate,
            Const.OrderStatusId.READY_TO_SHIP, Const.OrderListSortBy.SORT_BY_PAYMENT_DATE_ASCENDING)

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        mViewModel = OrderAppWidgetViewModel(getNewOrderUseCase, getReadyToShipUseCase, coroutineTestRule.dispatchers)
        mViewModel.bindView(mView)
    }

    @Test
    fun `returns the order list and notify the UI when get the order list successfully`() = coroutineTestRule.runBlockingTest {

        getNewOrderUseCase.params = newOrderParams
        getReadyToShipUseCase.params = readyToShipParams

        val newOrderModel = OrderUiModel(orders = listOf(OrderItemUiModel(statusId = Const.OrderStatusId.NEW_ORDER)))

        val readyToShipModel = OrderUiModel(orders = listOf(OrderItemUiModel(statusId = Const.OrderStatusId.READY_TO_SHIP)))

        coEvery {
            getNewOrderUseCase.executeOnBackground()
        } returns newOrderModel

        coEvery {
            getReadyToShipUseCase.executeOnBackground()
        } returns readyToShipModel

        mViewModel.getOrderList(startDate, endDate)

        coVerify {
            getNewOrderUseCase.executeOnBackground()
        }

        coVerify {
            getReadyToShipUseCase.executeOnBackground()
        }

        val newOrderResult = getNewOrderUseCase.executeOnBackground()
        val readyToShipResult = getReadyToShipUseCase.executeOnBackground()

        val result = OrderUiModel(
                orders = newOrderResult.orders.orEmpty().plus(readyToShipResult.orders.orEmpty()),
                sellerOrderStatus = readyToShipResult.sellerOrderStatus
        )

        val newOrderList = newOrderResult.orders
        val readyToShipList = readyToShipResult.orders

        newOrderList?.forEach {
            assertEquals(Const.OrderStatusId.NEW_ORDER, it.statusId)
        }

        readyToShipList?.forEach {
            assertEquals(Const.OrderStatusId.READY_TO_SHIP, it.statusId)
        }

        coVerify {
            mView.onSuccess(result)
        }
    }

    @Test
    fun `getReadyToShipUseCase won't called when getNewOrderUseCase throw Exception and notify the UI to show error state`() {
        val throwable = RuntimeException("")
        getNewOrderUseCase.params = newOrderParams
        getReadyToShipUseCase.params = readyToShipParams

        coEvery {
            getNewOrderUseCase.executeOnBackground()
        } throws throwable

        coEvery {
            getReadyToShipUseCase.executeOnBackground()
        } throws throwable

        mViewModel.getOrderList(startDate, endDate)

        coVerify {
            getNewOrderUseCase.executeOnBackground()
        }

        coVerify {
            mView.onError(any())
        }
    }

    @Test
    fun `getReadyToShipUseCase called but throw Exception when getNewOrderUseCase returns success result and notify the UI to show error state`() {
        val throwable = RuntimeException("")
        getNewOrderUseCase.params = newOrderParams
        getReadyToShipUseCase.params = readyToShipParams

        val newOrderModel = OrderUiModel(orders = listOf(OrderItemUiModel(statusId = Const.OrderStatusId.NEW_ORDER)))

        coEvery {
            getNewOrderUseCase.executeOnBackground()
        } returns newOrderModel

        coEvery {
            getReadyToShipUseCase.executeOnBackground()
        } throws throwable

        mViewModel.getOrderList(startDate, endDate)

        coVerify {
            getNewOrderUseCase.executeOnBackground()
        }

        coVerify {
            getReadyToShipUseCase.executeOnBackground()
        }

        coVerify {
            mView.onError(any())
        }
    }
}