package com.tokopedia.tradein.usecase

import android.content.Context
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.JsonObject
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.tradein.model.AddressResult
import com.tokopedia.tradein.model.MoneyInKeroGetAddressResponse.ResponseData
import com.tokopedia.tradein.repository.TradeInRepository
import io.mockk.*
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File
import java.lang.reflect.Type

@ExperimentalCoroutinesApi
class GetAddressUseCaseTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    val tradeInRepository: TradeInRepository = mockk(relaxed = true)
    val resources: Resources = mockk()

    var getAddressUseCase = spyk(GetAddressUseCase(tradeInRepository))

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**************************** createRequestParams() *******************************************/

    @Test
    fun createRequestParams() {
        val variables = getAddressUseCase.createRequestParams()

        assertEquals(variables["is_default"], 1)
        assertEquals(variables["limit"], 1)
        assertEquals(variables["page"], 1)
        assertEquals(variables["show_corner"], false)
        assertEquals(variables["show_address"], true)
    }

    /**************************** createRequestParams() *******************************************/

    /**************************** getAddress() *******************************************/

    @Test
    fun getAddress() {
        val address: ResponseData = createMockGraphqlSuccessResponse()
        runBlocking {
            mockkStatic(GraphqlHelper::class)
            every { GraphqlHelper.loadRawString(any(), any()) } returns ""
            coEvery { tradeInRepository.getGQLData(any(), ResponseData::class.java, any()) } returns address

            val addressResult = getAddressUseCase.getAddress()

            assertEquals(addressResult, AddressResult(address.keroGetAddress.data.firstOrNull { it.isPrimary }, address.keroGetAddress.token))

            /**Empty Address case**/
            (address.keroGetAddress.data as MutableList).clear()

            val emptyAddressResult = getAddressUseCase.getAddress()

            assertEquals(emptyAddressResult, AddressResult(address.keroGetAddress.data.firstOrNull { it.isPrimary }, address.keroGetAddress.token))
        }
    }

    /**************************** getAddress() *******************************************/


    private fun createMockGraphqlSuccessResponse(): ResponseData {
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val jsonObject: JsonObject = CommonUtils.fromJson(
                getJsonFromFile("tradein_dummy_response_address.json"),
                JsonObject::class.java
        )
        val data = jsonObject.get(GraphqlConstant.GqlApiKeys.DATA)
        val objectType = ResponseData::class.java
        val obj: Any = CommonUtils.fromJson(data, objectType)
        result[objectType] = obj
        return GraphqlResponse(result, errors, false).getData(ResponseData::class.java)
    }

    private fun getJsonFromFile(path: String): String {
        val uri = ClassLoader.getSystemClassLoader().getResource(path)
        val file = File(uri.path)
        return String(file.readBytes())
    }
}