package com.tokopedia.tradein.usecase

import android.content.Context
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common_tradein.model.TradeInParams
import com.tokopedia.tradein.model.DeviceDiagInput
import com.tokopedia.tradein.model.DeviceDiagInputResponse
import com.tokopedia.tradein.model.DeviceDiagnostics
import com.tokopedia.tradein.repository.TradeInRepository
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
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
import java.lang.ClassCastException

@ExperimentalCoroutinesApi
class ProcessMessageUseCaseTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    val tradeInRepository: TradeInRepository = mockk(relaxed = true)
    val resources: Resources = mockk()

    var processMessageUseCase = spyk(ProcessMessageUseCase(tradeInRepository))

    @RelaxedMockK
    lateinit var deviceDiagnostics: DeviceDiagnostics

    @RelaxedMockK
    lateinit var tradeInParams: TradeInParams

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
    fun createRequestParamsTest() {
        every { deviceDiagnostics.brand } returns "1"
        every { deviceDiagnostics.grade } returns "2"
        every { deviceDiagnostics.imei } returns "3"
        every { deviceDiagnostics.model } returns "4"
        every { deviceDiagnostics.modelId } returns 5
        every { deviceDiagnostics.ram } returns "6"
        every { deviceDiagnostics.storage } returns "7"
        every { deviceDiagnostics.tradeInUniqueCode } returns "8"
        every { deviceDiagnostics.reviewDetails } returns listOf()
        every { deviceDiagnostics.tradeInPrice } returns 10

        val variable = processMessageUseCase.createRequestParams(tradeInParams, deviceDiagnostics)

        assertNotNull(variable["params"])
        assertEquals((variable["params"] as DeviceDiagInput).deviceAttr.brand, deviceDiagnostics.brand)
        assertEquals((variable["params"] as DeviceDiagInput).deviceAttr.grade, deviceDiagnostics.grade)
        assertEquals((variable["params"] as DeviceDiagInput).deviceAttr.imei[0], deviceDiagnostics.imei)
        assertEquals((variable["params"] as DeviceDiagInput).deviceAttr.model, deviceDiagnostics.model)
        assertEquals((variable["params"] as DeviceDiagInput).deviceAttr.modelId, deviceDiagnostics.modelId)
        assertEquals((variable["params"] as DeviceDiagInput).deviceAttr.ram, deviceDiagnostics.ram)
        assertEquals((variable["params"] as DeviceDiagInput).deviceAttr.storage, deviceDiagnostics.storage)
        assertEquals((variable["params"] as DeviceDiagInput).uniqueCode, deviceDiagnostics.tradeInUniqueCode)
        assertEquals((variable["params"] as DeviceDiagInput).deviceId, deviceDiagnostics.imei)
        assertEquals((variable["params"] as DeviceDiagInput).deviceReview, deviceDiagnostics.reviewDetails)
        assertEquals((variable["params"] as DeviceDiagInput).newPrice, tradeInParams.newPrice)
        assertEquals((variable["params"] as DeviceDiagInput).oldPrice, deviceDiagnostics.tradeInPrice)
    }

    @Test
    fun createRequestParamsTestException() {
        every { deviceDiagnostics.brand } throws Exception("Params Exception")

        val variable = processMessageUseCase.createRequestParams(tradeInParams, deviceDiagnostics)

        assertEquals(variable.size, 0)
    }
    /**************************** createRequestParams() *******************************************/

    /**************************** processMessage() *******************************************/

    @Test
    fun processMessage() {
        val deviceDiagInputResponse: DeviceDiagInputResponse = mockk()
        runBlocking {
            mockkStatic(GraphqlHelper::class)
            every { GraphqlHelper.loadRawString(any(), any()) } returns ""
            coEvery { tradeInRepository.getGQLData(any(), DeviceDiagInputResponse::class.java, any())} returns deviceDiagInputResponse

            processMessageUseCase.processMessage(tradeInParams, deviceDiagnostics)

            coVerify { tradeInRepository.getGQLData(any(), DeviceDiagInputResponse::class.java, any()) }
        }
    }

    /**************************** processMessage() *******************************************/

}