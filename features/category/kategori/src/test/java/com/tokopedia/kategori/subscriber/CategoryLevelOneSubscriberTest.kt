package com.tokopedia.kategori.subscriber

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.JsonObject
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kategori.model.Data
import com.tokopedia.kategori.usecase.AllCategoryQueryUseCaseTest
import com.tokopedia.kategori.view.PerformanceMonitoringListener
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.mockk
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File
import java.lang.reflect.Type

class CategoryLevelOneSubscriberTest {

    val performanceMonitoringListener: PerformanceMonitoringListener = mockk(relaxed = true)

    lateinit var categoryLevelOneSubscriber: CategoryLevelOneSubscriber

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        categoryLevelOneSubscriber = CategoryLevelOneSubscriber(performanceMonitoringListener)
    }


    @Test
    fun ` Test success result`() {
        val list = (createMockGraphQlSuccessResponse().getData(Data::class.java) as Data).categoryAllList

        categoryLevelOneSubscriber.onNext(list)

        assertTrue(categoryLevelOneSubscriber.categoryListLiveData.value is Success)
    }

    @Test
    fun ` Test Fail result`() {
        categoryLevelOneSubscriber.onError(Throwable("error"))

        assertTrue(categoryLevelOneSubscriber.categoryListLiveData.value is Fail)
    }


    private fun createMockGraphQlSuccessResponse(): GraphqlResponse {
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val jsonObject: JsonObject = CommonUtils.fromJson(
                getJsonFromFile(AllCategoryQueryUseCaseTest.ALL_CATEGORY_GQL_RESPONSE_JSON_FILE_PATH),
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