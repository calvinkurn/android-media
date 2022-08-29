package com.tokopedia.tradein.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tradein.model.InsertTradeInLogisticModel
import com.tokopedia.tradein.repository.TradeInRepository
import com.tokopedia.user.session.UserSession
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
class InsertLogisticPreferenceUseCaseTest {

    val tradeInRepository: TradeInRepository = mockk(relaxed = true)
    val userSession: UserSession = mockk(relaxed = true)
    var insertLogisticPreferenceUseCase = spyk(InsertLogisticPreferenceUseCase(tradeInRepository, userSession))

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
    /**************************** insertLogistic() *******************************************/

    @Test
    fun getPromoTest() {
        val insertTradeInLogisticModel = mockk<InsertTradeInLogisticModel>()
        runBlocking {
            coEvery { tradeInRepository.getGQLData(any(), InsertTradeInLogisticModel::class.java, any())} returns insertTradeInLogisticModel

            insertLogisticPreferenceUseCase.insertLogistic(false, 0.0,0.0,"", "", mockk(relaxed = true))

            coVerify { tradeInRepository.getGQLData(any(), InsertTradeInLogisticModel::class.java, any()) }
        }
    }

    /**************************** insertLogistic() *******************************************/
}