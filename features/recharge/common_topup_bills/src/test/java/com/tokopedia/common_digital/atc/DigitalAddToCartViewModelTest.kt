package com.tokopedia.common_digital.atc

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.common.topupbills.response.CommonTopupbillsDummyData.getDummyCartData
import com.tokopedia.common_digital.atc.DigitalAddToCartViewModel.Companion.MESSAGE_ERROR_NON_LOGIN
import com.tokopedia.common_digital.atc.data.response.DigitalSubscriptionParams
import com.tokopedia.common_digital.atc.data.response.ResponseCartData
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.RechargeAnalytics
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type

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
        assertTrue(throwable.message == MESSAGE_ERROR_NON_LOGIN)
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
        val errorMessage = "this is error message"
        val throwable = Throwable(errorMessage)
        coEvery { digitalAddToCartUseCase.executeOnBackground() } throws throwable
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

        val throwableResult = (resultData as Fail).throwable
        assert(throwableResult.message == errorMessage)
    }
}