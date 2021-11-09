package com.tokopedia.moneyin.usecase

import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.moneyin.model.MoneyInCourierResponse.ResponseData
import com.tokopedia.moneyin.repository.MoneyInRepository
import com.tokopedia.moneyin.usecase.MoneyInCourierRatesUseCase
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

    private val tradeInRepository: MoneyInRepository = mockk(relaxed = true)
    private val resources: Resources = mockk()

    private var moneyInCourierRatesUseCase = spyk(MoneyInCourierRatesUseCase(tradeInRepository))

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

    @Test
    fun makeCheckoutMutation() {
        val responseData: ResponseData = mockk()
        runBlocking {
            mockkStatic(GraphqlHelper::class)
            every { GraphqlHelper.loadRawString(any(), any()) } returns ""
            coEvery { tradeInRepository.getGQLData(any(), ResponseData::class.java, any())} returns responseData

            moneyInCourierRatesUseCase.getCourierRates(resources, anyString())

            coVerify { tradeInRepository.getGQLData(any(), ResponseData::class.java, any()) }
        }
    }

    /**************************** makeCheckoutMutation() *******************************************/

}