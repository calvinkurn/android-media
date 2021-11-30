package com.tokopedia.moneyin.usecase

import android.content.Context
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common_tradein.model.TradeInParams
import com.tokopedia.common_tradein.model.ValidateTradePDP
import com.tokopedia.common_tradein.utils.TradeInUtils
import com.tokopedia.moneyin.repository.MoneyInRepository
import com.tokopedia.moneyin.usecase.CheckMoneyInUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
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

@ExperimentalCoroutinesApi
class CheckMoneyInUseCaseTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    val tradeInRepository: MoneyInRepository = mockk(relaxed = true)
    val context: Context = mockk()
    val resources: Resources = mockk()
    val userSession: UserSessionInterface = mockk(relaxed = true)

    var checkMoneyInUseCase = spyk(CheckMoneyInUseCase(context, tradeInRepository, userSession))

    var tradeInParams = TradeInParams()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        coEvery { userSession.deviceId } returns "mockk()"
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
        val modelId = 1
        val userId = "3"
        val deviceId = "4"

        mockkStatic(TradeInUtils::class)
        every { TradeInUtils.getDeviceId(any()) } returns deviceId

        val variables = checkMoneyInUseCase.createRequestParams(modelId, tradeInParams, userId)

        assertEquals(tradeInParams.deviceId, deviceId)
        assertEquals(tradeInParams.userId, 3)
        assertEquals(tradeInParams.tradeInType, 2)
        assertEquals(tradeInParams.modelID, modelId)
        assertNotNull(variables["params"])
        assertEquals(variables["params"] as TradeInParams, tradeInParams)
    }

    @Test
    fun createRequestParamsException(){
        val modelId = 1
        val userId = "saas"
        val deviceId = "4"

        mockkStatic(TradeInUtils::class)
        every { TradeInUtils.getDeviceId(any()) } returns deviceId

        checkMoneyInUseCase.createRequestParams(modelId, tradeInParams, userId)

        assertEquals(tradeInParams.userId, 0)
    }

    /**************************** createRequestParams() *******************************************/

    /**************************** checkMoneyIn() *******************************************/

    @Test
    fun checkMoneyInMessage() {
        val validateTradePDP: ValidateTradePDP = mockk()
        runBlocking {
            mockkStatic(GraphqlHelper::class)
            every { GraphqlHelper.loadRawString(any(), any()) } returns ""
            coEvery { tradeInRepository.getGQLData(any(), ValidateTradePDP::class.java, any())} returns validateTradePDP

            checkMoneyInUseCase.checkMoneyIn(resources,1,tradeInParams, "2")

            coVerify { tradeInRepository.getGQLData(any(), ValidateTradePDP::class.java, any()) }
        }
    }

    /**************************** checkMoneyIn() *******************************************/

}