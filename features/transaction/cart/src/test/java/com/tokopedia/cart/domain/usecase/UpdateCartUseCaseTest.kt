package com.tokopedia.cart.domain.usecase

import com.tokopedia.cart.data.model.request.UpdateCartWrapperRequest
import com.tokopedia.cart.domain.mapper.mapUpdateCartData
import com.tokopedia.cart.domain.model.updatecart.UpdateCartData
import com.tokopedia.cartcommon.data.response.updatecart.Data
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartGqlResponse
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.localizationchooseaddress.common.ChosenAddress
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
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

    private lateinit var updateCartUseCase: UpdateCartUseCase

    val params = UpdateCartWrapperRequest(
        updateCartRequestList = listOf(com.tokopedia.cartcommon.data.request.updatecart.UpdateCartRequest())
    )

    @Before
    fun before() {
        MockKAnnotations.init(this)
        updateCartUseCase = UpdateCartUseCase(graphqlRepository, chosenAddressRequestHelper)
    }

    @Test
    fun updateCartUseCaseRun_Success() {
        // Given
        val response = UpdateCartV2Data(data = Data(status = true), status = "OK")

        val result = HashMap<Type, Any>()
        result[UpdateCartGqlResponse::class.java] = UpdateCartGqlResponse(response)
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse
        every { chosenAddressRequestHelper.getChosenAddress() } returns ChosenAddress()

        runBlocking {
            // When
            updateCartUseCase.setParams(params.updateCartRequestList)
            val updateCartDataResponse = updateCartUseCase.executeOnBackground()
            val updateCartData = mapUpdateCartData(updateCartDataResponse)

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
            UpdateCartV2Data(data = Data(status = false), status = "ERROR", error = listOf(errorMessage))
        )
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse
        every { chosenAddressRequestHelper.getChosenAddress() } returns ChosenAddress()

        runBlocking {
            try {
                // When
                updateCartUseCase.setParams(params.updateCartRequestList)
                updateCartUseCase.executeOnBackground()
            } catch (e: Exception) {
                // Then
                assertEquals(errorMessage, e.message)
            }
        }
    }

    @Test
    fun updateCartUseCaseRun_FailedWithNoErrorMessage() {
        // Given
        val defaultErrorMessage = "Terjadi kesalahan, ulangi beberapa saat lagi"

        val result = HashMap<Type, Any>()
        result[UpdateCartGqlResponse::class.java] = UpdateCartGqlResponse(
            UpdateCartV2Data(data = Data(status = false), status = "ERROR")
        )
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse
        every { chosenAddressRequestHelper.getChosenAddress() } returns ChosenAddress()

        runBlocking {
            try {
                // When
                updateCartUseCase.setParams(params.updateCartRequestList)
                updateCartUseCase.executeOnBackground()
            } catch (e: Exception) {
                // Then
                assertEquals(defaultErrorMessage, e.message)
            }
        }
    }
}
