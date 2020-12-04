package com.tokopedia.buyerorder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.buyerorder.detail.domain.FinishOrderGqlUseCase
import com.tokopedia.buyerorder.detail.view.presenter.OrderListDetailContract
import com.tokopedia.buyerorder.detail.view.presenter.OrderListDetailPresenter
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohFinishOrder
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohListOrder
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohListParam
import com.tokopedia.buyerorder.unifiedhistory.list.view.viewmodel.UohListViewModel
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import rx.Observable
import rx.Subscriber
import java.util.*

/**
 * Created by fwidjaja on 18/11/20.
 */
@RunWith(JUnit4::class)
class OrderListDetailPresenterTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    val view: OrderListDetailContract.View = mockk(relaxed = true)
    val listMsgSuccess = listOf("success")
    private lateinit var orderListDetailPresenter: OrderListDetailPresenter

    @RelaxedMockK
    lateinit var graphqlUseCase: GraphqlUseCase

    @RelaxedMockK
    lateinit var finishOrderGqlUseCase: FinishOrderGqlUseCase

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        orderListDetailPresenter = OrderListDetailPresenter(graphqlUseCase, finishOrderGqlUseCase)
        orderListDetailPresenter.attachView(view)
    }

    // finish_order_gql
    @Test
    fun finishOrderGql_shouldReturnSuccess() {
        //given
        every {
            finishOrderGqlUseCase.execute(any())
        } answers {
            firstArg<Subscriber<UohFinishOrder.Data>>().onStart()
            firstArg<Subscriber<UohFinishOrder.Data>>().onCompleted()
            firstArg<Subscriber<UohFinishOrder.Data>>().onNext(UohFinishOrder.Data(UohFinishOrder.Data.FinishOrderBuyer(success = 1, message = listMsgSuccess)))
        }

        //when
        orderListDetailPresenter.finishOrderGql("", "", "", "")

        //then
        verify { finishOrderGqlUseCase.execute(any()) }
        verify { view.showSuccessMessage(listMsgSuccess[0]) }
    }
}