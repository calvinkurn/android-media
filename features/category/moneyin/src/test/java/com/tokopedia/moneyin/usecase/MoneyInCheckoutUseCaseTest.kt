package com.tokopedia.moneyin.usecase

import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.moneyin.model.MoneyInCheckoutMutationResponse.ResponseData
import com.tokopedia.moneyin.usecase.MoneyInCheckoutUseCase
import com.tokopedia.moneyin.model.MoneyInMutationRequest.Cart.CartInfo.Field
import com.tokopedia.moneyin.model.MoneyInMutationRequest
import com.tokopedia.moneyin.repository.MoneyInRepository
import io.mockk.*
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
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

@ExperimentalCoroutinesApi
class MoneyInCheckoutUseCaseTest{
    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val tradeInRepository: MoneyInRepository = mockk(relaxed = true)
    private val resources: Resources = mockk()

    private var moneyInCheckoutUseCase = spyk(MoneyInCheckoutUseCase(tradeInRepository))

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
    fun createRequestParams(){
        val hardwareId = "1"
        val addressId = 2
        val spId = 3
        val pickupTimeStart = 4
        val pickupTimeEnd = 5
        val metadataMap = HashMap<String,String>()
        metadataMap["hardware_id"] = hardwareId

        val variables = moneyInCheckoutUseCase.createRequestParams(hardwareId, addressId, spId, pickupTimeStart, pickupTimeEnd)

        val metadata = (variables["params"] as MoneyInMutationRequest).
                carts.first { it.businessType == 2 }.metaData
        val fields = ((variables["params"] as MoneyInMutationRequest).
                carts.first { it.businessType == 2 }.
                cartInfo.first { it.dataType == "shipping" }).fields

        assertTrue(fields.contains(Field("origin_address_id", addressId)))
        assertTrue(fields.contains(Field("sp_id", spId)))
        assertTrue(fields.contains(Field("pickup_time_start", pickupTimeStart)))
        assertTrue(fields.contains(Field("pickup_time_end", pickupTimeEnd)))
        assertEquals(metadata, Gson().toJson(metadataMap))
    }

    /**************************** createRequestParams() *******************************************/

    /**************************** makeCheckoutMutation() *******************************************/


    @Test
    fun makeCheckoutMutation() {
        val responseData: ResponseData = mockk()
        runBlocking {
            mockkStatic(GraphqlHelper::class)
            every { GraphqlHelper.loadRawString(any(), any()) } returns ""
            coEvery { tradeInRepository.getGQLData(any(), ResponseData::class.java, any())} returns responseData

            moneyInCheckoutUseCase.makeCheckoutMutation(resources, HashMap())

            coVerify { tradeInRepository.getGQLData(any(), ResponseData::class.java, any()) }
        }
    }

    /**************************** makeCheckoutMutation() *******************************************/

}