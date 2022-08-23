package com.tokopedia.tradein.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tradein.model.TradeInDetailModel
import com.tokopedia.tradein.model.TradeInValidateImeiModel
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
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TradeInValidateImeiUseCaseTest {

    val tradeInRepository: TradeInRepository = mockk(relaxed = true)
    val userSession: UserSession = mockk(relaxed = true)
    var tradeInValidateImeiUseCase = spyk(TradeInValidateImeiUseCase(tradeInRepository, userSession))
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
    /**************************** validateImei() *******************************************/

    @Test
    fun validateImeiTest() {
        val tradeInValidateImeiModel = mockk<TradeInValidateImeiModel>()
        runBlocking {
            coEvery { tradeInRepository.getGQLData(any(), TradeInValidateImeiModel::class.java, any())} returns tradeInValidateImeiModel

            tradeInValidateImeiUseCase.validateImei(mockk(relaxed = true),"12213")

            coVerify { tradeInRepository.getGQLData(any(), TradeInValidateImeiModel::class.java, any()) }
        }
    }

    /**************************** validateImei() *******************************************/
}