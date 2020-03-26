package com.tokopedia.common_tradein.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common_tradein.model.TradeInParams
import com.tokopedia.common_tradein.model.ValidateTradePDP
import com.tokopedia.common_tradein.repository.CommonTradeInRepository
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
class CheckTradeInUseCaseTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()
    val tradeInParams = TradeInParams()

    val repository : CommonTradeInRepository = mockk(relaxed = true)

    var useCase = spyk(CheckTradeInUseCase(repository))

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
        val variables = useCase.createRequestParams(tradeInParams)

        assertEquals(variables["params"] as TradeInParams, tradeInParams)
    }

    /**************************** createRequestParams() *******************************************/

    /**************************** checkTradeIn() *******************************************/

    @Test
    fun checkTradeIn() {
        val validateTradePDP: ValidateTradePDP = mockk()
        runBlocking {
            coEvery { repository.getGQLData(any(), ValidateTradePDP::class.java, any())} returns validateTradePDP

            useCase.checkTradeIn("", tradeInParams)

            coVerify { repository.getGQLData(any(), ValidateTradePDP::class.java, any()) }
        }
    }

    /**************************** checkTradeIn() *******************************************/

}