package com.tokopedia.cart.domain.usecase

import com.tokopedia.cart.data.model.response.shopgroupsimplified.ShopGroupSimplifiedGqlResponse
import com.tokopedia.cart.data.model.response.shopgroupsimplified.ShopGroupSimplifiedResponse
import com.tokopedia.cart.domain.mapper.CartSimplifiedMapper
import com.tokopedia.cart.domain.model.cartlist.CartListData
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.localizationchooseaddress.util.ChosenAddressRequestHelper
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.usecase.RequestParams
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import rx.Observable
import rx.observers.AssertableSubscriber
import java.lang.reflect.Type

class GetCartListSimplifiedUseCaseTest {

    @MockK
    private lateinit var cartMapperV3: CartSimplifiedMapper

    @MockK
    private lateinit var chosenAddressRequestHelper: ChosenAddressRequestHelper

    @MockK(relaxUnitFun = true)
    private lateinit var graphqlUseCase: GraphqlUseCase

    private lateinit var getCartListSimplifiedUseCase: GetCartListSimplifiedUseCase
    lateinit var subscriber: AssertableSubscriber<CartListData>
    lateinit var onErrorEvents: List<Throwable>

    private val params = RequestParams().apply {
        putObject(GetCartListSimplifiedUseCase.PARAM_GET_CART, emptyMap<String, Any?>())
    }

    @Before
    fun before() {
        MockKAnnotations.init(this)
        getCartListSimplifiedUseCase = GetCartListSimplifiedUseCase(graphqlUseCase, cartMapperV3, TestSchedulers, chosenAddressRequestHelper)
    }

    @Test
    fun getCartListSimplifiedUseCaseRun_Success() {
        // Given
        val result = HashMap<Type, Any>()
        result[ShopGroupSimplifiedGqlResponse::class.java] = ShopGroupSimplifiedGqlResponse(ShopGroupSimplifiedResponse("OK"))
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        every { graphqlUseCase.createObservable(any()) } returns Observable.just(gqlResponse)
        every { cartMapperV3.convertToCartItemDataList(any()) } returns CartListData()

        // When
        subscriber = getCartListSimplifiedUseCase.createObservable(params).test()

        // Then
        subscriber.assertValueCount(1)
        subscriber.assertValue(CartListData())
        subscriber.assertCompleted()
    }

    @Test
    fun getCartListSimplifiedUseCaseRun_Failed() {
        // Given
        val errorMessages = listOf("error message", "other message")

        val result = HashMap<Type, Any>()
        result[ShopGroupSimplifiedGqlResponse::class.java] = ShopGroupSimplifiedGqlResponse(ShopGroupSimplifiedResponse("FAIL", errorMessages = errorMessages))
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        every { graphqlUseCase.createObservable(any()) } returns Observable.just(gqlResponse)
        every { cartMapperV3.convertToCartItemDataList(any()) } returns CartListData()

        // When
        subscriber = getCartListSimplifiedUseCase.createObservable(params).test()

        // Then
        onErrorEvents = subscriber.onErrorEvents
        Assert.assertEquals(1, onErrorEvents.size)
        Assert.assertEquals(errorMessages.joinToString(), (onErrorEvents.first() as CartResponseErrorException).message)
    }

    @Test
    fun getCartListSimplifiedUseCaseRun_FailedUnexpected() {
        // Given
        val result = HashMap<Type, Any>()
        result[ShopGroupSimplifiedGqlResponse::class.java] = ShopGroupSimplifiedGqlResponse(ShopGroupSimplifiedResponse("FAIL"))
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        every { graphqlUseCase.createObservable(any()) } returns Observable.just(gqlResponse)
        every { cartMapperV3.convertToCartItemDataList(any()) } returns CartListData()

        // When
        subscriber = getCartListSimplifiedUseCase.createObservable(params).test()

        // Then
        onErrorEvents = subscriber.onErrorEvents
        Assert.assertEquals(1, onErrorEvents.size)
        Assert.assertEquals("Terjadi kesalahan, ulangi beberapa saat lagi", (onErrorEvents.first() as ResponseErrorException).message)
    }

}