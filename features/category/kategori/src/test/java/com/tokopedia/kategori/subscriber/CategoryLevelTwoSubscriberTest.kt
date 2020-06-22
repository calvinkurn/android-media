package com.tokopedia.kategori.subscriber

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.JsonObject
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kategori.model.Data
import com.tokopedia.kategori.usecase.AllCategoryQueryUseCaseTest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import junit.framework.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File
import java.lang.reflect.Type

class CategoryLevelTwoSubscriberTest {

    lateinit var categoryLevelTwoSubscriber: CategoryLevelTwoSubscriber

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        categoryLevelTwoSubscriber = CategoryLevelTwoSubscriber("0")
    }


    @Test
    fun ` Test success result`() {
        val list = (createMockGraphQlSuccessResponse().getData(Data::class.java) as Data).categoryAllList

        categoryLevelTwoSubscriber.onNext(list)

        Assert.assertTrue(categoryLevelTwoSubscriber.mutableChildItem.value is Success)
    }

    @Test
    fun ` Test Fail result`() {
        categoryLevelTwoSubscriber.onError(Throwable("error"))

        Assert.assertTrue(categoryLevelTwoSubscriber.mutableChildItem.value is Fail)
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