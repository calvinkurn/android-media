package com.tokopedia.cart.domain.usecase

import com.tokopedia.cart.data.model.request.UpdateCartRequest
import com.tokopedia.cart.data.model.response.updatecart.Data
import com.tokopedia.cart.data.model.response.updatecart.UpdateCartDataResponse
import com.tokopedia.cart.data.model.response.updatecart.UpdateCartGqlResponse
import com.tokopedia.cart.domain.model.updatecart.UpdateCartData
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.localizationchooseaddress.util.ChosenAddress
import com.tokopedia.localizationchooseaddress.util.ChosenAddressRequestHelper
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.usecase.RequestParams
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test
import rx.Observable
import rx.observers.AssertableSubscriber
import java.lang.reflect.Type

class UpdateCartUseCaseTest {

    @MockK
    private lateinit var chosenAddressRequestHelper: ChosenAddressRequestHelper

    @MockK(relaxUnitFun = true)
    private lateinit var graphqlUseCase: GraphqlUseCase

    private lateinit var updateCartUseCase: UpdateCartUseCase
    lateinit var subscriber: AssertableSubscriber<UpdateCartData>

    val params = RequestParams().apply {
        putObject(UpdateCartUseCase.PARAM_UPDATE_CART_REQUEST, arrayListOf(UpdateCartRequest()))
    }

    @Before
    fun before() {
        MockKAnnotations.init(this)
        updateCartUseCase = UpdateCartUseCase(graphqlUseCase, TestSchedulers, chosenAddressRequestHelper)
    }

    @Test
    fun updateCartUseCaseRun_Success() {
        // Given
        val response = UpdateCartDataResponse(data = Data(status = true), status = "OK")

        val result = HashMap<Type, Any>()
        result[UpdateCartGqlResponse::class.java] = UpdateCartGqlResponse(response)
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        every { graphqlUseCase.createObservable(any()) } returns Observable.just(gqlResponse)
        every { chosenAddressRequestHelper.getChosenAddress() } returns ChosenAddress()

        // When
        subscriber = updateCartUseCase.createObservable(params).test()

        // Then
        subscriber.assertValue(UpdateCartData(isSuccess = true, message = ""))
    }

    @Test
    fun updateCartUseCaseRun_FailedWithErrorMessage() {
        // Given
        val errorMessage = "Something went wrong"

        val result = HashMap<Type, Any>()
        result[UpdateCartGqlResponse::class.java] = UpdateCartGqlResponse(
                UpdateCartDataResponse(data = Data(status = false, error = errorMessage), status = "ERROR"))
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        every { graphqlUseCase.createObservable(any()) } returns Observable.just(gqlResponse)
        every { chosenAddressRequestHelper.getChosenAddress() } returns ChosenAddress()

        // When
        subscriber = updateCartUseCase.createObservable(params).test()

        // Then
        subscriber.assertValue(UpdateCartData(isSuccess = false, message = errorMessage))
    }

    @Test
    fun updateCartUseCaseRun_FailedWithNoErrorMessage() {
        // Given
        val result = HashMap<Type, Any>()
        result[UpdateCartGqlResponse::class.java] = UpdateCartGqlResponse(
                UpdateCartDataResponse(data = Data(status = false), status = "ERROR"))
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        every { graphqlUseCase.createObservable(any()) } returns Observable.just(gqlResponse)
        every { chosenAddressRequestHelper.getChosenAddress() } returns ChosenAddress()

        // When
        subscriber = updateCartUseCase.createObservable(params).test()

        // Then
        subscriber.assertValue(UpdateCartData(isSuccess = false, message = ""))
    }

}