package com.tokopedia.kategori.usecase

import android.content.Context
import android.content.res.Resources
import com.google.gson.JsonObject
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.kategori.model.Data
import com.tokopedia.usecase.RequestParams
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import rx.Observable
import java.io.File
import java.lang.reflect.Type

class AllCategoryQueryUseCaseTest {

    @RelaxedMockK
    lateinit var graphqlUseCase: GraphqlUseCase

    @RelaxedMockK
    lateinit var requestParams: RequestParams

    val context: Context = mockk()

    val mockRes = mockk<Resources>()

   /* private val allCategoryQueryUseCase by lazy {
        AllCategoryQueryUseCase(
                context,
                graphqlUseCase
        )
    }*/

    companion object {
        const val ALL_CATEGORY_GQL_RESPONSE_JSON_FILE_PATH = "json/gql_all_category.json"
    }


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testCreateObservableSuccess() {
        every { context.resources } returns mockRes
        mockkStatic(GraphqlHelper::class)
        every { GraphqlHelper.loadRawString(any(), any()) } returns ""
        every {
            graphqlUseCase.createObservable(any())
        } returns Observable.just(createMockGraphQlSuccessResponse())

      //  val testSubscriber = allCategoryQueryUseCase.createObservable(requestParams).test()

        verifySequence {
            graphqlUseCase.clearRequest()
            graphqlUseCase.setCacheStrategy(any())
            graphqlUseCase.addRequest(any())
            graphqlUseCase.createObservable(requestParams)
        }
/*        testSubscriber.assertCompleted()
        testSubscriber.assertNoErrors()
        Assert.assertTrue(testSubscriber.onNextEvents.size != 0)
        Assert.assertEquals((createMockGraphQlSuccessResponse().getData(Data::class.java) as Data).categoryAllList, testSubscriber.onNextEvents[0])*/
    }


    private fun createMockGraphQlSuccessResponse(): GraphqlResponse {
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val jsonObject: JsonObject = CommonUtils.fromJson(
                getJsonFromFile(ALL_CATEGORY_GQL_RESPONSE_JSON_FILE_PATH),
                JsonObject::class.java
        )
        val data = jsonObject.get(GraphqlConstant.GqlApiKeys.DATA)
        val objectType = Data::class.java
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