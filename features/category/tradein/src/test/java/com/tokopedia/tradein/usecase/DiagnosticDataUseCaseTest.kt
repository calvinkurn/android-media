package com.tokopedia.tradein.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common_tradein.model.TradeInParams
import com.tokopedia.common_tradein.model.ValidateTradePDP
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.tradein.model.DeviceDiagGQL
import com.tokopedia.tradein.model.DeviceDiagParams
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

@ExperimentalCoroutinesApi
class DiagnosticDataUseCaseTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val tradeInRepository: TradeInRepository = mockk(relaxed = true)

    var diagnosticDataUseCase = spyk(DiagnosticDataUseCase(tradeInRepository))

    private val tradeInParams = mockk<TradeInParams>(relaxed = true)
    private val tradeInType = 1

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

    /**************************** createRequestParamsDeviceDiag() *******************************************/

    @Test
    fun createRequestParams() {
        tradeInParams.productId = 2

        val variables = diagnosticDataUseCase.createRequestParamsDeviceDiag(tradeInParams, tradeInType)

        assertEquals((variables["params"] as DeviceDiagParams).productId, tradeInParams.productId)
        assertEquals((variables["params"] as DeviceDiagParams).deviceId, tradeInParams.deviceId)
        assertEquals((variables["params"] as DeviceDiagParams).newPrice, tradeInParams.newPrice)
        assertEquals((variables["params"] as DeviceDiagParams).tradeInType, tradeInType)

        /** Null params**/
        val nullVariables = diagnosticDataUseCase.createRequestParamsDeviceDiag(null, tradeInType)

        assertEquals((nullVariables["params"] as DeviceDiagParams).productId, 0)
        assertEquals((nullVariables["params"] as DeviceDiagParams).deviceId, null)
        assertEquals((nullVariables["params"] as DeviceDiagParams).newPrice, 0)
        assertEquals((nullVariables["params"] as DeviceDiagParams).tradeInType, tradeInType)
    }

    /**************************** createRequestParamsDeviceDiag() *******************************************/

    /**************************** getDiagnosticData() *******************************************/

    @Test(expected = RuntimeException::class)
    fun getDiagnosticDataException() {
        val graphqlResponse = mockk<GraphqlResponse>(relaxed = true)
        runBlocking {
            mockkStatic(GraphqlHelper::class)
            every { GraphqlHelper.loadRawString(any(), any()) } returns ""
            coEvery { tradeInRepository.getGQLData(any(), any(), any(), any()) } returns graphqlResponse

            diagnosticDataUseCase.getDiagnosticData(tradeInParams, tradeInType)
        }
    }

    @Test
    fun getDiagnosticData() {
        val deviceDiagGQL = mockk<DeviceDiagGQL>(relaxed = true)
        runBlocking {
            mockkStatic(GraphqlHelper::class)
            every { GraphqlHelper.loadRawString(any(), any()) } returns ""
            coEvery { tradeInRepository.getGQLData(any(), DeviceDiagGQL::class.java, any(), CacheType.ALWAYS_CLOUD)} returns deviceDiagGQL

            val variable = diagnosticDataUseCase.getDiagnosticData(tradeInParams, tradeInType)

            assertEquals(variable, deviceDiagGQL.diagResponse)
        }
    }

    /**************************** getDiagnosticData() *******************************************/
}