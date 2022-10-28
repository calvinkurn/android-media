package com.tokopedia.tradein.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tradein.model.PromoTradeInModel
import com.tokopedia.tradein.usecase.PromoUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TradeInPromoDetailPageVMTest {

    val promoUseCase: PromoUseCase = mockk()
    var tradeInPromoDetailPageVM = spyk(TradeInPromoDetailPageVM(promoUseCase))

    @get:Rule
    var rule = InstantTaskExecutorRule()

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

    /**************************** getPromo() *******************************************/
    @Test
    fun getPromo() {
        val promoTradeInModel: PromoTradeInModel = mockk(relaxed = true)
        coEvery { promoUseCase.getPromo(any()) } returns promoTradeInModel

        tradeInPromoDetailPageVM.getPromo("FREE")

        assertEquals(tradeInPromoDetailPageVM.promoTradeInLiveData.value, promoTradeInModel)
        assertEquals(tradeInPromoDetailPageVM.getProgBarVisibility().value, false)

    }

    @Test
    fun getPromoException() {
        val exception = Exception("Promo Data Exception")
        coEvery { promoUseCase.getPromo(any()) } throws exception

        tradeInPromoDetailPageVM.getPromo("FREE")

        assertEquals(tradeInPromoDetailPageVM.getErrorMessage().value, exception)
        assertEquals(tradeInPromoDetailPageVM.getProgBarVisibility().value, false)

    }

    /**************************** getPromo() *******************************************/
}