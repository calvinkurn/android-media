package com.tokopedia.tradein.usecase

import android.content.Context
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.tradein.model.MoneyInCourierResponse.ResponseData
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
import org.mockito.ArgumentMatchers.anyString

@ExperimentalCoroutinesApi
class MoneyInCourierRatesUseCaseTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val tradeInRepository: TradeInRepository = mockk(relaxed = true)
    val context: Context = mockk()
    private val resources: Resources = mockk()

    private var moneyInCourierRatesUseCase = spyk(MoneyInCourierRatesUseCase(context, tradeInRepository))

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
        val destination = "destination"

        val variables = moneyInCourierRatesUseCase.createRequestParams(destination)
        val input = (variables["input"] as HashMap<*, *>)

        assertEquals(input["features"], "money_in")
        assertEquals(input["weight"], "1")
        assertEquals(input["destination"], destination)
        assertEquals(input["from"], "client")
        assertEquals(input["type"], "android")
        assertEquals(input["lang"], "en")

    }

    /**************************** createRequestParams() *******************************************/

    /**************************** makeCheckoutMutation() *******************************************/

    @Test(expected = ClassCastException::class)
    fun makeCheckoutMutationException() {
        val responseData: ResponseData? = null
        runBlocking {
            mockkStatic(GraphqlHelper::class)
            every { GraphqlHelper.loadRawString(any(), any()) } returns ""
            every { context.resources } returns resources
            coEvery { tradeInRepository.getGQLData(any(), ResponseData::class.java, any())} returns responseData

            moneyInCourierRatesUseCase.getCourierRates(anyString())
        }
    }

    @Test
    fun makeCheckoutMutation() {
        val responseData: ResponseData = mockk()
        runBlocking {
            mockkStatic(GraphqlHelper::class)
            every { GraphqlHelper.loadRawString(any(), any()) } returns ""
            every { context.resources } returns resources
            coEvery { tradeInRepository.getGQLData(any(), ResponseData::class.java, any())} returns responseData

            moneyInCourierRatesUseCase.getCourierRates(anyString())

            coVerify { tradeInRepository.getGQLData(any(), ResponseData::class.java, any()) }
        }
    }

    /**************************** makeCheckoutMutation() *******************************************/

}