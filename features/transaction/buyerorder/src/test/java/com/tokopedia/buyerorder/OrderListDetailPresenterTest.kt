package com.tokopedia.buyerorder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.reflect.TypeToken
import com.tokopedia.buyerorder.detail.data.ActionButton
import com.tokopedia.buyerorder.detail.data.ActionButtonList
import com.tokopedia.buyerorder.detail.data.DataEmail
import com.tokopedia.buyerorder.detail.data.SendEventEmail
import com.tokopedia.buyerorder.detail.data.recommendation.recommendationMPPojo2.RecommendationDigiPersoResponse
import com.tokopedia.buyerorder.detail.domain.SendEventNotificationUseCase
import com.tokopedia.buyerorder.detail.domain.SetActionButtonUseCase
import com.tokopedia.buyerorder.detail.view.presenter.OrderListDetailContract
import com.tokopedia.buyerorder.detail.view.presenter.OrderListDetailPresenter
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import rx.Subscriber
import java.lang.reflect.Type

/**
 * Created by fwidjaja on 18/11/20.
 */
@RunWith(JUnit4::class)
class OrderListDetailPresenterTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    val view: OrderListDetailContract.View = mockk(relaxed = true)
    private lateinit var orderListDetailPresenter: OrderListDetailPresenter
    val actionButtonListPojo = ActionButtonList()

    @RelaxedMockK
    lateinit var setActionButtonUseCase: SetActionButtonUseCase

    @RelaxedMockK
    lateinit var sendEventNotificationUseCase: SendEventNotificationUseCase

    @RelaxedMockK
    lateinit var orderDetailsUseCase: GraphqlUseCase

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    @RelaxedMockK
    lateinit var viewOrderListDetail: OrderListDetailContract.ActionInterface

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        orderListDetailPresenter = OrderListDetailPresenter(
            orderDetailsUseCase, userSessionInterface, setActionButtonUseCase,
            sendEventNotificationUseCase)
        orderListDetailPresenter.attachView(view)
    }

    // getActionButtonGql
    @Test
    fun getActionButtonGql_shouldReturnSuccess() {
        val actionButton = ActionButton()
        actionButton.control = "control1"
        val listActionButton = arrayListOf<ActionButton>()
        listActionButton.add(actionButton)
        actionButtonListPojo.actionButtonList = listActionButton

        //given
        every {
            setActionButtonUseCase.execute(any())
        } answers {
            firstArg<Subscriber<ActionButtonList>>().onStart()
            firstArg<Subscriber<ActionButtonList>>().onCompleted()
            firstArg<Subscriber<ActionButtonList>>().onNext(actionButtonListPojo)
        }

        //when
        orderListDetailPresenter.getActionButtonGql("", listActionButton, viewOrderListDetail, 0, false)

        //then
        verify { setActionButtonUseCase.execute(any()) }
        verify { viewOrderListDetail.setActionButton(0, listActionButton) }
    }

    // sendEventNotificationo
    @Test
    fun sendEventNotification_shouldReturnSuccess() {
        val response = RestResponse(SendEventEmail(data = DataEmail(message = "test1")), 200, false)
        val responseMap = mapOf<Type, RestResponse>(
                SendEventEmail::class.java to response
        )

        val token = object : TypeToken<SendEventEmail?>() {}.type
        val restResponse: RestResponse? = responseMap[token]

        // val dataResponse = gson.fromJson(restResponse?.errorBody, SendEventEmail::class.java)

        //given
        every {
            sendEventNotificationUseCase.execute(any())
        } answers {
            firstArg<Subscriber<Map<Type, RestResponse>>>().onStart()
            firstArg<Subscriber<Map<Type, RestResponse>>>().onCompleted()
            firstArg<Subscriber<Map<Type, RestResponse>>>().onNext(responseMap)
        }

        //when
        orderListDetailPresenter.hitEventEmail(ActionButton(), "")

        //then
        verify { sendEventNotificationUseCase.execute(any()) }
        verify { view.setActionButtonLayoutClickable(false) }
        // verify { view.showSuccessMessageWithAction(dataResponse.data.message) }
    }

    // get_order_detail
    @Test
    fun orderDetail_shouldReturnSuccess() {
        //given
        val result = HashMap<Type, Any>()
        result[RecommendationDigiPersoResponse::class.java] = RecommendationDigiPersoResponse(null)

        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        every {
            orderDetailsUseCase.execute(any())
        } answers {
            firstArg<Subscriber<GraphqlResponse>>().onStart()
            firstArg<Subscriber<GraphqlResponse>>().onCompleted()
            firstArg<Subscriber<GraphqlResponse>>().onNext(gqlResponse)
        }

        //when
        orderListDetailPresenter.setOrderDetailsContent("", "", "")

        //then
        verify { orderDetailsUseCase.execute(any()) }
        verify { view.setRecommendation(RecommendationDigiPersoResponse(null)) }
    }
}