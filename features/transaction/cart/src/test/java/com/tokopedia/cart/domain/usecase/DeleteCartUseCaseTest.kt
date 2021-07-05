package com.tokopedia.cart.domain.usecase

import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.cart.data.model.request.RemoveCartRequest
import com.tokopedia.cart.data.model.response.deletecart.Data
import com.tokopedia.cart.data.model.response.deletecart.DeleteCartDataResponse
import com.tokopedia.cart.data.model.response.deletecart.DeleteCartGqlResponse
import com.tokopedia.cart.domain.model.cartlist.DeleteCartData
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
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

class DeleteCartUseCaseTest {

    @MockK
    private lateinit var updateCartCounterUseCase: UpdateCartCounterUseCase

    @MockK(relaxUnitFun = true)
    private lateinit var graphqlUseCase: GraphqlUseCase

    private lateinit var deleteCartUseCase: DeleteCartUseCase
    lateinit var subscriber: AssertableSubscriber<DeleteCartData>

    private val params = RequestParams().apply {
        putObject(DeleteCartUseCase.PARAM_REMOVE_CART_REQUEST, RemoveCartRequest())
    }

    @Before
    fun before() {
        MockKAnnotations.init(this)
        deleteCartUseCase = DeleteCartUseCase(graphqlUseCase, TestSchedulers)
    }

    @Test
    fun deleteCartUseCaseRun_Success() {
        // Given
        val result = HashMap<Type, Any>()
        result[DeleteCartGqlResponse::class.java] = DeleteCartGqlResponse(
                DeleteCartDataResponse(status = "OK", data = Data(0))
        )
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        every { graphqlUseCase.createObservable(any()) } returns Observable.just(gqlResponse)
        every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(0)

        // When
        subscriber = deleteCartUseCase.createObservable(params).test()

        // Then
        subscriber.assertValue(DeleteCartData(true, ""))
    }

    @Test
    fun deleteCartUseCaseRun_FailedWithErrorMessage() {
        // Given
        val errorMessage = "something went wrong"
        val errorMessage2 = "something went wrong2"

        val result = HashMap<Type, Any>()
        result[DeleteCartGqlResponse::class.java] = DeleteCartGqlResponse(
                DeleteCartDataResponse(status = "ERROR", errorMessage = arrayListOf(errorMessage, errorMessage2), data = Data(0))
        )
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        every { graphqlUseCase.createObservable(any()) } returns Observable.just(gqlResponse)
        every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(0)

        // When
        subscriber = deleteCartUseCase.createObservable(params).test()

        // Then
        subscriber.assertValue(DeleteCartData(message = errorMessage))
    }

    @Test
    fun deleteCartUseCaseRun_FailedWithNoErrorMessage() {
        // Given
        val result = HashMap<Type, Any>()
        result[DeleteCartGqlResponse::class.java] = DeleteCartGqlResponse(
                DeleteCartDataResponse(status = "ERROR", data = Data(0))
        )
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        every { graphqlUseCase.createObservable(any()) } returns Observable.just(gqlResponse)
        every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(0)

        // When
        subscriber = deleteCartUseCase.createObservable(params).test()

        // Then
        subscriber.assertValue(DeleteCartData(message = ""))

    }

}