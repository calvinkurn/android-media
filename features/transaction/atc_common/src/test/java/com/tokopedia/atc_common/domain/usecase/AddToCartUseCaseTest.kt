package com.tokopedia.atc_common.domain.usecase

import com.tokopedia.atc_common.MockResponseProvider
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.data.model.request.chosenaddress.ChosenAddressAddToCartRequestHelper
import com.tokopedia.atc_common.data.model.response.AddToCartGqlResponse
import com.tokopedia.atc_common.domain.analytics.AddToCartBaseAnalytics
import com.tokopedia.atc_common.domain.mapper.AddToCartDataMapper
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase.Companion.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import rx.Observable
import rx.observers.AssertableSubscriber
import java.lang.reflect.Type

class AddToCartUseCaseTest {

    @MockK
    private lateinit var addToCartDataMapper: AddToCartDataMapper

    @MockK(relaxed = true)
    private lateinit var chosenAddressAddToCartRequestHelper: ChosenAddressAddToCartRequestHelper

    @MockK(relaxUnitFun = true)
    private lateinit var graphqlUseCase: GraphqlUseCase

    private lateinit var addToCartUseCase: AddToCartUseCase

    lateinit var subscriber: AssertableSubscriber<AddToCartDataModel>
    private val requestParam = RequestParams.create().apply {
        putObject(REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST, AddToCartRequestParams())
    }

    @Before
    fun before() {
        MockKAnnotations.init(this)
        mockkObject(AddToCartBaseAnalytics)
        addToCartUseCase = AddToCartUseCase("mock_query", graphqlUseCase, addToCartDataMapper, chosenAddressAddToCartRequestHelper)
    }

    @After
    fun after() {
        unmockkObject(AddToCartBaseAnalytics)
    }

    @Test
    fun addToCartUseCaseRun_Success() {
        // Given
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = AddToCartGqlResponse::class.java
        result[objectType] = MockResponseProvider.getResponseAtcSuccess()

        every { graphqlUseCase.createObservable(any()) } returns
                Observable.just(GraphqlResponse(result, errors, false))

        every { addToCartDataMapper.mapAddToCartResponse(any()) } returns AddToCartDataModel(status = "OK", data = DataModel(success = 1))

        // When
        subscriber = addToCartUseCase.createObservable(requestParam).test()

        // Then

        // request param is not empty
        Assert.assertTrue(requestParam.parameters.isNotEmpty())

        // request param has field `REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST`
        Assert.assertNotNull(requestParam.getObject(REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST))

        // should run sequence task
        verifySequence {
            graphqlUseCase.clearRequest()
            graphqlUseCase.addRequest(any())
            graphqlUseCase.createObservable(any())
        }

        // should not give error
        subscriber.assertNoErrors()

        // should has 1 value
        subscriber.assertValueCount(1)

        // value should be success AddToCartDataModel
        subscriber.assertValue(AddToCartDataModel(status = "OK", data = DataModel(success = 1)))

        // should run analytics
        verify {
            AddToCartBaseAnalytics.sendAppsFlyerTracking(any(), any(), any(), any(), any())
            AddToCartBaseAnalytics.sendBranchIoTracking(any(), any(), any(), any(), any(),
                    any(), any(), any(), any(), any(), any(), any())
        }

        // should complete
        subscriber.assertCompleted()
    }

    @Test
    fun addToCartUseCaseRun_Error() {
        // Given
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = AddToCartGqlResponse::class.java
        result[objectType] = MockResponseProvider.getResponseAtcError()
        every { graphqlUseCase.createObservable(any()) } returns Observable.just(GraphqlResponse(result, errors, false))

        every { addToCartDataMapper.mapAddToCartResponse(any()) } returns AddToCartDataModel(status = "OK", data = DataModel(success = 0))

        // When
        val subscriber = addToCartUseCase.createObservable(requestParam).test()

        // Then
        // should run sequence task
        verifySequence {
            graphqlUseCase.clearRequest()
            graphqlUseCase.addRequest(any())
            graphqlUseCase.createObservable(any())
        }

        // Should not give error
        subscriber.assertNoErrors()

        // Should has 1 value
        subscriber.assertValueCount(1)

        // Value should be failed AddToCartDataModel
        subscriber.assertValue(AddToCartDataModel(status = "OK", data = DataModel(success = 0)))

        // Should not run analytics
        verify(inverse = true) {
            AddToCartBaseAnalytics.sendAppsFlyerTracking(any(), any(), any(), any(), any())
            AddToCartBaseAnalytics.sendBranchIoTracking(any(), any(), any(), any(), any(),
                    any(), any(), any(), any(), any(), any(), any())
        }

        // Should complete
        subscriber.assertCompleted()
    }
}