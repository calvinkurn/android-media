package com.tokopedia.power_merchant.subscribe.domain.interactor

import com.google.gson.JsonObject
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.power_merchant.subscribe.data.model.GoldCancellationsQuestionaire
import com.tokopedia.usecase.RequestParams
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Matchers.anyString
import rx.Observable
import java.io.File
import java.lang.reflect.Type
import kotlin.collections.HashMap

class GetGoldCancellationsQuestionaireUseCaseTest {

    @RelaxedMockK
    lateinit var graphqlUseCase: GraphqlUseCase

    @RelaxedMockK
    lateinit var requestParams: RequestParams

    private val getGoldCancellationsQuestionaireUseCase by lazy {
        GetGoldCancellationsQuestionaireUseCase(
                graphqlUseCase,
                anyString()
        )
    }

    companion object {
        const val GOLD_CANCELLATION_QUESTIONNAIRE_JSON_FILE_PATH = "json/gql_gold_cancellation_questionnaire_response.json"
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
        val testSubscriber = getGoldCancellationsQuestionaireUseCase.createObservable(requestParams).test()
        verifySequence {
            graphqlUseCase.clearRequest()
            graphqlUseCase.addRequest(any())
            graphqlUseCase.createObservable(requestParams)
        }
        testSubscriber.assertCompleted()
        testSubscriber.assertNoErrors()
        assertTrue(testSubscriber.onNextEvents.size != 0)
        assertTrue(testSubscriber.onNextEvents[0].result.data.questionList.size == 3)
    }

    private fun createMockGraphqlSuccessResponse(): GraphqlResponse {
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val jsonObject: JsonObject = CommonUtils.fromJson(
                getJsonFromFile(GOLD_CANCELLATION_QUESTIONNAIRE_JSON_FILE_PATH),
                JsonObject::class.java
        )
        val data = jsonObject.get(GraphqlConstant.GqlApiKeys.DATA)
        val objectType = GoldCancellationsQuestionaire::class.java
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