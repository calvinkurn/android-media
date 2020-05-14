package com.tokopedia.common_tradein.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.tokopedia.common_tradein.model.TradeInParams
import com.tokopedia.common_tradein.model.ValidateTradeInResponse
import com.tokopedia.common_tradein.usecase.CheckTradeInUseCase
import io.mockk.*
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TradeInTextViewModelTest {
    val useCase = mockk<CheckTradeInUseCase>()
    val tradeInParams = TradeInParams()
    val application = mockk<Application>()
    var tradeInTextViewModel = spyk(TradeInTextViewModel(useCase))

    val tradeInResponse = mockk<ValidateTradeInResponse>(relaxed = true)

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
        coEvery { tradeInTextViewModel.getQuery(application) } returns ""
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**************************** checkTradeIn() *******************************************/

    @Test
    fun checkTradeIn(){
        val hide = false
        mockkStatic(LocalBroadcastManager::class)
        coEvery { LocalBroadcastManager.getInstance(any()).sendBroadcast(any()) } returns true
        coEvery { tradeInResponse.isEligible } returns true
        coEvery { tradeInResponse.isUseKyc } returns true
        coEvery { tradeInResponse.usedPrice } returns 200
        coEvery { tradeInResponse.remainingPrice } returns 1000
        coEvery { useCase.checkTradeIn(any(),any()).response } returns tradeInResponse

        tradeInTextViewModel.setActivity(mockk())
        tradeInTextViewModel.checkTradeIn(tradeInParams, hide, application)

        assertEquals(tradeInResponse, tradeInTextViewModel.responseData.value)
        assertEquals(tradeInParams.isEligible, 1)
        assertEquals(tradeInParams.usedPrice, tradeInResponse.usedPrice)
        assertEquals(tradeInParams.remainingPrice, tradeInResponse.remainingPrice)
        assertEquals(tradeInParams.isUseKyc, 1)
    }

    @Test
    fun checkTradeInNotEligible(){
        var hide = false
        tradeInParams.remainingPrice = 100
        tradeInParams.isUseKyc = 1
        tradeInParams.usedPrice = 200
        tradeInParams.isEligible = 1

        /**Hide False Case**/

        tradeInTextViewModel.checkTradeIn(tradeInParams, hide, application)

        assertEquals(tradeInTextViewModel.responseData.value?.isEligible, true)
        assertEquals(tradeInTextViewModel.responseData.value?.remainingPrice, tradeInParams.remainingPrice)
        assertEquals(tradeInTextViewModel.responseData.value?.usedPrice, tradeInParams.usedPrice)
        assertEquals(tradeInTextViewModel.responseData.value?.isUseKyc, tradeInParams.isUseKyc != 0)

        /**Hide True Case**/
        hide = true

        tradeInTextViewModel.checkTradeIn(tradeInParams, hide, application)

        assertEquals(tradeInTextViewModel.responseData.value?.isEligible, false)

    }
    /**************************** checkTradeIn() *******************************************/


}