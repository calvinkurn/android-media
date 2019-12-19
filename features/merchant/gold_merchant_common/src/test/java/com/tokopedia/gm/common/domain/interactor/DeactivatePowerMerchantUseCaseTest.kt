package com.tokopedia.gm.common.domain.interactor

import com.google.gson.JsonObject
import com.tokopedia.gm.common.data.source.cloud.model.GoldDeactivationSubscription
import com.tokopedia.gm.common.data.source.cloud.model.GoldGetPmOsStatus
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Matchers.anyString
import rx.Observable
import java.io.File
import java.lang.reflect.Type
import kotlin.collections.HashMap

class DeactivatePowerMerchantUseCaseTest {

    @RelaxedMockK
    lateinit var graphqlUseCase: GraphqlUseCase

    @RelaxedMockK
    lateinit var requestParams: RequestParams

    private val deactivatePowerMerchantUseCase by lazy {
        DeactivatePowerMerchantUseCase(
                graphqlUseCase,
                anyString()
        )
    }

    companion object {
        const val GOLD_TURN_OFF_SUBSCRIPTION_JSON_FILE_PATH = "json/gql_gold_turn_off_subscription.json"
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testCreateObservableSuccess() {
        every {
            graphqlUseCase.createObservable(any())
        } returns Observable.just(createMockGraphqlSuccessResponse())
        val testSubscriber = deactivatePowerMerchantUseCase.createObservable(requestParams).test()
        verifySequence {
            graphqlUseCase.clearRequest()
            graphqlUseCase.addRequest(any())
            graphqlUseCase.createObservable(requestParams)
        }
        testSubscriber.assertCompleted()
        testSubscriber.assertNoErrors()
        assertTrue(testSubscriber.onNextEvents.size != 0)
        assertEquals(true, testSubscriber.onNextEvents[0])
    }

    private fun createMockGraphqlSuccessResponse(): GraphqlResponse {
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val jsonObject: JsonObject = CommonUtils.fromJson(
                getJsonFromFile(GOLD_TURN_OFF_SUBSCRIPTION_JSON_FILE_PATH),
                JsonObject::class.java
        )
        val data = jsonObject.get(GraphqlConstant.GqlApiKeys.DATA)
        val objectType = GoldDeactivationSubscription::class.java
        val obj: Any = CommonUtils.fromJson(data, objectType)
        result[objectType] = obj
        return GraphqlResponse(result, errors, false)
    }

    private fun getJsonFromFile(path: String): String {
        val uri = ClassLoader.getSystemClassLoader().getResource(path)
        val file = File(uri.path)
        return String(file.readBytes())
    }

}