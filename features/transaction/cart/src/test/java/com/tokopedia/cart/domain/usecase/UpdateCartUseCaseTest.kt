package com.tokopedia.cart.domain.usecase

import com.tokopedia.cart.data.model.request.UpdateCartWrapperRequest
import com.tokopedia.cart.data.model.response.updatecart.Data
import com.tokopedia.cart.data.model.response.updatecart.UpdateCartDataResponse
import com.tokopedia.cart.data.model.response.updatecart.UpdateCartGqlResponse
import com.tokopedia.cart.domain.model.updatecart.UpdateCartData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.localizationchooseaddress.common.ChosenAddress
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.lang.reflect.Type

class UpdateCartUseCaseTest {

    @MockK
    private lateinit var chosenAddressRequestHelper: ChosenAddressRequestHelper

    @MockK(relaxUnitFun = true)
    private lateinit var graphqlRepository: GraphqlRepository

    val coroutineTestDispatchers: CoroutineTestDispatchers = CoroutineTestDispatchers

    private lateinit var updateCartUseCase: UpdateCartUseCase

    val params = UpdateCartWrapperRequest(
        updateCartRequestList = listOf(com.tokopedia.cartcommon.data.request.updatecart.UpdateCartRequest())
    )

    @Before
    fun before() {
        MockKAnnotations.init(this)
        updateCartUseCase = UpdateCartUseCase(graphqlRepository, chosenAddressRequestHelper, coroutineTestDispatchers)
    }

    @Test
    fun updateCartUseCaseRun_Success() {
        // Given
        val response = UpdateCartDataResponse(data = Data(status = true), status = "OK")

        val result = HashMap<Type, Any>()
        result[UpdateCartGqlResponse::class.java] = UpdateCartGqlResponse(response)
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse
        every { chosenAddressRequestHelper.getChosenAddress() } returns ChosenAddress()

        runBlocking {
            // When
            val updateCartData = updateCartUseCase(params)
            
            // Then
            assertEquals(UpdateCartData(isSuccess = true, message = ""), updateCartData)
        }
    }

    @Test
    fun updateCartUseCaseRun_FailedWithErrorMessage() {
        // Given
        val errorMessage = "Something went wrong"

        val result = HashMap<Type, Any>()
        result[UpdateCartGqlResponse::class.java] = UpdateCartGqlResponse(
            UpdateCartDataResponse(data = Data(status = false, error = errorMessage), status = "ERROR")
        )
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse
        every { chosenAddressRequestHelper.getChosenAddress() } returns ChosenAddress()

        runBlocking {
            // When
            val updateCartData = updateCartUseCase(params)

            // Then
            assertEquals(UpdateCartData(isSuccess = false, message = errorMessage), updateCartData)
        }
    }

    @Test
    fun updateCartUseCaseRun_FailedWithNoErrorMessage() {
        // Given
        val result = HashMap<Type, Any>()
        result[UpdateCartGqlResponse::class.java] = UpdateCartGqlResponse(
            UpdateCartDataResponse(data = Data(status = false), status = "ERROR")
        )
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse
        every { chosenAddressRequestHelper.getChosenAddress() } returns ChosenAddress()

        runBlocking {
            // When
            val updateCartData = updateCartUseCase(params)

            // Then
            assertEquals(UpdateCartData(isSuccess = false, message = ""), updateCartData)
        }
    }
}
