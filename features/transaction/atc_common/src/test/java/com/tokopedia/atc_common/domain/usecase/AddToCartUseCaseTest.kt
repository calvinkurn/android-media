package com.tokopedia.atc_common.domain.usecase

import com.tokopedia.atc_common.MockResponseProvider
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.data.model.request.chosenaddress.ChosenAddressAddToCartRequestHelper
import com.tokopedia.atc_common.data.model.response.AddToCartGqlResponse
import com.tokopedia.atc_common.domain.analytics.AddToCartBaseAnalytics
import com.tokopedia.atc_common.domain.mapper.AddToCartDataMapper
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import rx.Observable
import rx.observers.AssertableSubscriber
import java.lang.reflect.Type

class AddToCartUseCaseTest {

    @MockK
    private lateinit var addToCartDataMapper: AddToCartDataMapper

    @MockK
    private lateinit var chosenAddressAddToCartRequestHelper: ChosenAddressAddToCartRequestHelper

    @MockK(relaxUnitFun = true)
    private lateinit var graphqlUseCase: GraphqlUseCase

    private lateinit var addToCartUseCase: AddToCartUseCase

    lateinit var subscriber: AssertableSubscriber<AddToCartDataModel>
    private val requestParam = RequestParams.create().apply {
        putObject(REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST, AddToCartRequestParams())
    }

    val REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST = "REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST";

    @Before
    fun before() {
        MockKAnnotations.init(this)
        addToCartUseCase = AddToCartUseCase("mock_query", graphqlUseCase, addToCartDataMapper, chosenAddressAddToCartRequestHelper)
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

}