package com.tokopedia.common_digital.atc

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryData
import com.tokopedia.common.topupbills.response.CommonTopupbillsDummyData.getDummyCartData
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel
import com.tokopedia.common_digital.atc.DigitalAddToCartViewModel.Companion.MESSAGE_ERROR_NON_LOGIN
import com.tokopedia.common_digital.atc.data.request.RequestBodyAtcDigital
import com.tokopedia.common_digital.atc.data.response.DigitalSubscriptionParams
import com.tokopedia.common_digital.atc.data.response.ResponseCartData
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.RechargeAnalytics
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.constant.ErrorNetMessage
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.verify
import junit.framework.Assert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Subscriber
import java.lang.reflect.Type
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class DigitalAddToCartViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = TestCoroutineDispatcher()

    lateinit var digitalAddToCartViewModel: DigitalAddToCartViewModel

    @RelaxedMockK
    lateinit var digitalAddToCartUseCase: DigitalAddToCartUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var rechargeAnalytics: RechargeAnalytics

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        digitalAddToCartViewModel = DigitalAddToCartViewModel(
                digitalAddToCartUseCase, userSession, dispatcher, rechargeAnalytics)
    }

    @Test
    fun addToCart_isNotLoggedIn_returnsNotLoginException() {
        // Given
        every { userSession.isLoggedIn } returns false

        // When
        digitalAddToCartViewModel.addToCart(
                DigitalCheckoutPassData(),
                RequestBodyIdentifier(),
                DigitalSubscriptionParams()
        )

        // Then
        val actualData = digitalAddToCartViewModel.addToCartResult.value
        assertNotNull(actualData)
        assert(actualData is Fail)

        val throwable = (actualData as Fail).throwable
        assertTrue(throwable is DigitalAddToCartViewModel.DigitalUserNotLoginException)
    }

    @Test
    fun addToCart_loggedIn_returnsSuccessData() {
        // Given
        val dataResponse = DataResponse<ResponseCartData>()
        dataResponse.data = getDummyCartData(isNull = false)

        val token = object : TypeToken<DataResponse<ResponseCartData>>() {}.type
        val response = RestResponse(dataResponse, 200, false)
        val responseMap = mapOf<Type, RestResponse>(token to response)

        coEvery { digitalAddToCartUseCase.executeOnBackground() } returns responseMap
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "123"

        // When
        val digitalCheckoutPassData = DigitalCheckoutPassData()
        digitalCheckoutPassData.categoryId = "1"
        digitalAddToCartViewModel.addToCart(digitalCheckoutPassData,
                RequestBodyIdentifier(), DigitalSubscriptionParams())

        // Then
        val resultData = digitalAddToCartViewModel.addToCartResult.value
        Assert.assertNotNull(resultData)
        assert(resultData is Success)
    }

    @Test
    fun addToCart_loggedInNullId_returnsNoConnectionError() {
        // Given
        val dataResponse = DataResponse<ResponseCartData>()
        dataResponse.data = getDummyCartData(isNull = true)

        val token = object : TypeToken<DataResponse<ResponseCartData>>() {}.type
        val response = RestResponse(dataResponse, 200, false)
        val responseMap = mapOf<Type, RestResponse>(token to response)

        coEvery { digitalAddToCartUseCase.executeOnBackground() } returns responseMap
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "123"

        // When
        val digitalCheckoutPassData = DigitalCheckoutPassData()
        digitalCheckoutPassData.categoryId = "1"
        digitalAddToCartViewModel.addToCart(digitalCheckoutPassData,
                RequestBodyIdentifier(), DigitalSubscriptionParams())

        // Then
        val resultData = digitalAddToCartViewModel.addToCartResult.value
        assertNotNull(resultData)
        assert(resultData is Fail)

        val throwable = (resultData as Fail).throwable
        assertTrue(throwable.cause is DigitalAddToCartViewModel.DigitalFailGetCartId)
    }

    @Test
    fun addToCart_loggedIn_returnsDigitalFail() {
        // Given
        every {
            digitalAddToCartUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<Map<Type, RestResponse>>>().onStart()
            secondArg<Subscriber<Map<Type, RestResponse>>>().onCompleted()
            secondArg<Subscriber<Map<Type, RestResponse>>>().onError(Throwable())
        }
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "123"

        // When
        val digitalCheckoutPassData = DigitalCheckoutPassData()
        digitalCheckoutPassData.categoryId = "1"
        digitalAddToCartViewModel.addToCart(digitalCheckoutPassData,
                RequestBodyIdentifier(), DigitalSubscriptionParams())

        // Then
        val resultData = digitalAddToCartViewModel.addToCartResult.value
        Assert.assertNotNull(resultData)
        assert(resultData is Fail)

        val throwable = (resultData as Fail).throwable
        assert(throwable.message == ErrorNetMessage.MESSAGE_ERROR_DEFAULT)
    }
}