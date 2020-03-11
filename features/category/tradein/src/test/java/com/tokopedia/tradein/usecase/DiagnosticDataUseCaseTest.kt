package com.tokopedia.tradein.usecase

import android.content.Context
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common_tradein.model.TradeInParams
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.tradein.model.*
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
    val context: Context = mockk()
    private val resources: Resources = mockk()

    var diagnosticDataUseCase = spyk(DiagnosticDataUseCase(context, tradeInRepository))

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

    /**************************** createRequestParamsKYCStatus() *******************************************/

    @Test
    fun createRequestParamsKYCStatus() {
        val variables = diagnosticDataUseCase.createRequestParamsKYCStatus()

        assertEquals(variables["projectID"], 4)

    }
    /**************************** createRequestParamsKYCStatus() *******************************************/

    /**************************** getQueries() *******************************************/

    @Test
    fun `getQueries Is User KYC`() {
        every { tradeInParams.isUseKyc } returns 1
        mockkStatic(GraphqlHelper::class)
        every { GraphqlHelper.loadRawString(any(), any()) } returns ""
        every { context.resources } returns resources

        val queries = diagnosticDataUseCase.getQueries(tradeInParams)

        assertEquals(queries.size, 2)
    }

    @Test
    fun `getQueries Is User not KYC`() {
        every { tradeInParams.isUseKyc } returns 0
        mockkStatic(GraphqlHelper::class)
        every { GraphqlHelper.loadRawString(any(), any()) } returns ""
        every { context.resources } returns resources

        val queries = diagnosticDataUseCase.getQueries(tradeInParams)

        assertEquals(queries.size, 1)
    }

    /**************************** getQueries() *******************************************/

    /**************************** getDiagnosticData() *******************************************/

    @Test(expected = RuntimeException::class)
    fun getDiagnosticDataException() {
        val graphqlResponse = mockk<GraphqlResponse>(relaxed = true)
        runBlocking {
            mockkStatic(GraphqlHelper::class)
            every { GraphqlHelper.loadRawString(any(), any()) } returns ""
            every { context.resources } returns resources
            coEvery { graphqlResponse.getData<DeviceDiagGQL>(DeviceDiagGQL::class.java) } returns null
            coEvery { tradeInRepository.getGQLData(any(), any(), any()) } returns graphqlResponse

            diagnosticDataUseCase.getDiagnosticData(tradeInParams, tradeInType)
        }
    }

    @Test
    fun getDiagnosticData() {
        val graphqlResponse = mockk<GraphqlResponse>(relaxed = true)
        val deviceDiagGQL = mockk<DeviceDiagGQL>(relaxed = true)
        runBlocking {
            mockkStatic(GraphqlHelper::class)
            every { GraphqlHelper.loadRawString(any(), any()) } returns ""
            every { context.resources } returns resources
            every { tradeInParams.isUseKyc } returns 0
            coEvery { graphqlResponse.getData<DeviceDiagGQL>(DeviceDiagGQL::class.java) } returns deviceDiagGQL
            coEvery { graphqlResponse.getData<KYCDetailGQL>(KYCDetailGQL::class.java) } returns null
            coEvery { tradeInRepository.getGQLData(any(), any(), any()) } returns graphqlResponse

            val variable = diagnosticDataUseCase.getDiagnosticData(tradeInParams, tradeInType)

            assertEquals(variable, deviceDiagGQL.diagResponse)
        }
    }

    @Test
    fun getDiagnosticDataKYC() {
        val graphqlResponse = mockk<GraphqlResponse>(relaxed = true)
        val deviceDiagGQL = mockk<DeviceDiagGQL>(relaxed = true)
        val kycDetailGQL = mockk<KYCDetailGQL>(relaxed = true)
        val deviceDataResponse = DeviceDataResponse()
        runBlocking {
            mockkStatic(GraphqlHelper::class)
            every { GraphqlHelper.loadRawString(any(), any()) } returns ""
            every { context.resources } returns resources
            coEvery { tradeInParams.isUseKyc } returns 1
            coEvery { deviceDiagGQL.diagResponse } returns deviceDataResponse
            coEvery { graphqlResponse.getData<DeviceDiagGQL>(DeviceDiagGQL::class.java) } returns deviceDiagGQL
            coEvery { graphqlResponse.getData<KYCDetailGQL>(KYCDetailGQL::class.java) } returns kycDetailGQL
            coEvery { tradeInRepository.getGQLData(any(), any(), any()) } returns graphqlResponse

            val variable = diagnosticDataUseCase.getDiagnosticData(tradeInParams, tradeInType)

            assertEquals(variable?.kycDetails, kycDetailGQL.kycDetails)
        }
    }

    /**************************** getDiagnosticData() *******************************************/
}