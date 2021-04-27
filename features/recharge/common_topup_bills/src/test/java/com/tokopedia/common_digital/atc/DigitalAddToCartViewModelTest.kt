package com.tokopedia.common_digital.atc

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryData
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type
import java.net.UnknownHostException

class DigitalAddToCartViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
//
    private val dispatcher = TestCoroutineDispatcher()

    lateinit var digitalAddToCartViewModel: DigitalAddToCartViewModel

    lateinit var digitalAddToCartUseCase: DigitalAddToCartUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var restRepository: RestRepository

    @RelaxedMockK
    lateinit var rechargeAnalytics: RechargeAnalytics

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        digitalAddToCartUseCase = DigitalAddToCartUseCase(restRepository)
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

    fun addToCart_loggedIn_returnsSuccessData() {
        // TODO: fill unit test
    }

    fun addToCart_loggedIn_returnsDigitalFailGetCardId() {
        // TODO: fill unit test
    }
}