package com.tokopedia.buyerorder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.atc_common.domain.model.response.AtcMultiData
import com.tokopedia.atc_common.domain.usecase.AddToCartMultiLegacyUseCase
import com.tokopedia.buyerorder.detail.data.ActionButton
import com.tokopedia.buyerorder.detail.data.ActionButtonList
import com.tokopedia.buyerorder.detail.data.DataEmail
import com.tokopedia.buyerorder.detail.data.Detail
import com.tokopedia.buyerorder.detail.data.DetailsData
import com.tokopedia.buyerorder.detail.data.OrderDetails
import com.tokopedia.buyerorder.detail.data.SendEventEmail
import com.tokopedia.buyerorder.detail.data.Title
import com.tokopedia.buyerorder.detail.data.recommendation.recommendationMPPojo.RechargeFavoriteRecommendationList
import com.tokopedia.buyerorder.detail.data.recommendation.recommendationMPPojo.RecommendationResponse
import com.tokopedia.buyerorder.detail.data.recommendation.recommendationMPPojo2.PersonalizedItems
import com.tokopedia.buyerorder.detail.data.recommendation.recommendationMPPojo2.RecommendationDigiPersoResponse
import com.tokopedia.buyerorder.detail.domain.BuyerGetRecommendationUseCase
import com.tokopedia.buyerorder.detail.domain.FinishOrderGqlUseCase
import com.tokopedia.buyerorder.detail.domain.SendEventNotificationUseCase
import com.tokopedia.buyerorder.detail.domain.SetActionButtonUseCase
import com.tokopedia.buyerorder.detail.view.OrderListAnalytics
import com.tokopedia.buyerorder.detail.view.presenter.OrderListDetailContract
import com.tokopedia.buyerorder.detail.view.presenter.OrderListDetailPresenter
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohFinishOrder
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import rx.Subscriber
import java.lang.reflect.Type
import org.mockito.ArgumentMatchers.*

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
    val actionButtonListPojo = ActionButtonList()

    @RelaxedMockK
    lateinit var finishOrderGqlUseCase: FinishOrderGqlUseCase

    @RelaxedMockK
    lateinit var setActionButtonUseCase: SetActionButtonUseCase

    @RelaxedMockK
    lateinit var sendEventNotificationUseCase: SendEventNotificationUseCase

    @RelaxedMockK
    lateinit var addToCartMultiLegacyUseCase: AddToCartMultiLegacyUseCase

    @RelaxedMockK
    lateinit var buyerGetRecommendationUseCase: BuyerGetRecommendationUseCase

    @RelaxedMockK
    lateinit var orderDetailsUseCase: GraphqlUseCase

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    @RelaxedMockK
    lateinit var viewOrderListDetail: OrderListDetailContract.ActionInterface

    @RelaxedMockK
    lateinit var gson: Gson

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        orderListDetailPresenter = OrderListDetailPresenter(orderDetailsUseCase, finishOrderGqlUseCase,
                addToCartMultiLegacyUseCase, userSessionInterface, setActionButtonUseCase,
                sendEventNotificationUseCase, buyerGetRecommendationUseCase)
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

    // add_to_cart_multi_legacy
    @Test
    fun addToCartMulti_shouldReturnSuccess() {
        //given
        every { userSessionInterface.userId } returns "0"

        every {
            addToCartMultiLegacyUseCase.execute(any())
        } answers {
            firstArg<Subscriber<AtcMultiData>>().onStart()
            firstArg<Subscriber<AtcMultiData>>().onCompleted()
            firstArg<Subscriber<AtcMultiData>>().onNext(AtcMultiData(AtcMultiData.AtcMulti(errorMessage = "", status = "", buyAgainData = AtcMultiData.AtcMulti.BuyAgainData(success = 1, message = listMsgSuccess))))
        }

        //when
        orderListDetailPresenter.onBuyAgainItems("", listOf(), "", "")

        //then
        verify { addToCartMultiLegacyUseCase.execute(any()) }
        verify { view.showSuccessMessageWithAction(listMsgSuccess[0]) }
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

    // buyer_get_recommendation
    @Test
    fun buyerGetRecommendation_shouldReturnSuccess() {
        //given
        every {
            buyerGetRecommendationUseCase.execute(any())
        } answers {
            firstArg<Subscriber<RecommendationResponse>>().onStart()
            firstArg<Subscriber<RecommendationResponse>>().onCompleted()
            firstArg<Subscriber<RecommendationResponse>>().onNext(RecommendationResponse(RechargeFavoriteRecommendationList(title = "test")))
        }

        //when
        orderListDetailPresenter.getRecommendation("")

        //then
        verify { buyerGetRecommendationUseCase.execute(any()) }
        verify { view.setRecommendation(RecommendationResponse(RechargeFavoriteRecommendationList(title = "test"))) }
    }

    // get_order_detail
    @Test
    fun orderDetail_shouldReturnSuccess() {
        //given
        mockkConstructor(OrderListAnalytics::class)

        val result = HashMap<Type, Any>()
        result[DetailsData::class.java] = DetailsData(getOrderDetail())
        result[RecommendationDigiPersoResponse::class.java] = RecommendationDigiPersoResponse(null)

        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        every {
            orderDetailsUseCase.execute(any())
        } answers {
            firstArg<Subscriber<GraphqlResponse>>().onStart()
            firstArg<Subscriber<GraphqlResponse>>().onCompleted()
            firstArg<Subscriber<GraphqlResponse>>().onNext(gqlResponse)
        }

        every { anyConstructed<OrderListAnalytics>().sendOrderDetailImpression(any(), any(), any()) } returns Unit

        //when
        orderListDetailPresenter.setOrderDetailsContent("", "", "", "", "", "")

        //then
        verify { orderDetailsUseCase.execute(any()) }
        verify { view.setRecommendation(any()) }
    }

    private fun getOrderDetail(): OrderDetails {
        return OrderDetails(
                null,
                null,
                mutableListOf(Title(
                        "Tokopedia", "...", "...", "...", "..."
                )),
                null,
                null,
                mutableListOf(Detail(
                        "Tokopedia", "...", "...", "...", "..."
                )),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null)
    }
}