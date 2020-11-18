package com.tokopedia.buyerorder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.buyerorder.detail.view.presenter.OrderListDetailPresenter
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohFinishOrder
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohListOrder
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohListParam
import com.tokopedia.buyerorder.unifiedhistory.list.view.viewmodel.UohListViewModel
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
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
 * Created by fwidjaja on 18/11/20.
 */
@RunWith(JUnit4::class)
class OrderListDetailPresenterTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var orderListDetailPresenter: OrderListDetailPresenter

    @RelaxedMockK
    lateinit var graphqlUseCase: GraphqlUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        orderListDetailPresenter = OrderListDetailPresenter(graphqlUseCase)
    }

    // order_history_list
    @Test
    fun getOrderHistoryData_shouldReturnSuccess() {
        //given
        coEvery {
            orderListDetailPresenter.setOrderDetailsContent()
            uohListUseCase.execute(any(), any())
        } returns Success(UohListOrder.Data.UohOrders(listOrderHistory))

        //when
        uohListViewModel.loadOrderList("", UohListParam())

        //then
        assert(uohListViewModel.orderHistoryListResult.value is Success)
        assert((uohListViewModel.orderHistoryListResult.value as Success<UohListOrder.Data.UohOrders>).data.orders[0].orderUUID.equals("abc", true))
    }
}