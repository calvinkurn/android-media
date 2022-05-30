package com.tokopedia.tradein.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tradein.model.TnCInfoModel
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
class TNCInfoUseCaseTest {

    val tradeInRepository: TradeInRepository = mockk(relaxed = true)
    var tncUseCase = spyk(TNCInfoUseCase(tradeInRepository))

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
    /**************************** getTNCInfo() *******************************************/

    @Test
    fun getTNCInfoTest() {
        val tncTradein = mockk<TnCInfoModel>()
        runBlocking {
            coEvery { tradeInRepository.getGQLData(any(), TnCInfoModel::class.java, any())} returns tncTradein

            tncUseCase.getTNCInfo(1)

            coVerify { tradeInRepository.getGQLData(any(), TnCInfoModel::class.java, any()) }
        }
    }

    /**************************** getTNCInfo() *******************************************/
}