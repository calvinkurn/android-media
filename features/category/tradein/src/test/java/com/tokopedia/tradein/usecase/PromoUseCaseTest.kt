package com.tokopedia.tradein.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tradein.model.PromoTradeInModel
import com.tokopedia.tradein.repository.TradeInRepository
import io.mockk.*
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
class PromoUseCaseTest {

    val tradeInRepository: TradeInRepository = mockk(relaxed = true)
    var promoUseCase = spyk(PromoUseCase(tradeInRepository))

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
    fun getPromoTest() {
        val promoTradein = mockk<PromoTradeInModel>()
        runBlocking {
            coEvery { tradeInRepository.getGQLData(any(), PromoTradeInModel::class.java, any())} returns promoTradein

            promoUseCase.getPromo("FREE")

            coVerify { tradeInRepository.getGQLData(any(), PromoTradeInModel::class.java, any()) }
        }
    }

    /**************************** getPromo() *******************************************/
}