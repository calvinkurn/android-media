package com.tokopedia.tradein.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tradein.model.TradeInValidateImeiModel
import com.tokopedia.tradein.usecase.TradeInValidateImeiUseCase
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
class TradeInImeiBSViewModelTest {

    val tradeInValidateImeiUseCase: TradeInValidateImeiUseCase = mockk()
    var tradeInImeiBSViewModel = spyk(TradeInImeiBSViewModel(tradeInValidateImeiUseCase))

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
    fun validateImei() {
        val tradeInValidateImeiModel: TradeInValidateImeiModel = mockk<TradeInValidateImeiModel>(relaxed = true)
        coEvery { tradeInValidateImeiUseCase.validateImei(any(), any()) } returns tradeInValidateImeiModel

        tradeInImeiBSViewModel.validateImei(mockk(), "121212")

        assertEquals(tradeInImeiBSViewModel.tradeInImeiLiveData.value, tradeInValidateImeiModel)
        assertEquals(tradeInImeiBSViewModel.getProgBarVisibility().value, false)

    }

    @Test
    fun validateImeiException() {
        val exception = Exception("IMEI Validate Data Exception")
        coEvery { tradeInValidateImeiUseCase.validateImei(any(), any()) } throws exception

        tradeInImeiBSViewModel.validateImei(mockk(),"212121221")

        assertEquals(tradeInImeiBSViewModel.getErrorMessage().value, exception)
        assertEquals(tradeInImeiBSViewModel.getProgBarVisibility().value, false)

    }

    /**************************** getTradeInDetail() *******************************************/
}