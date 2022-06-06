package com.tokopedia.tradein.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tradein.model.TnCInfoModel
import com.tokopedia.tradein.usecase.TNCInfoUseCase
import io.mockk.*
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
class TradeInInfoViewModelTest {

    val tNCInfoUseCase: TNCInfoUseCase = mockk()
    var tradeInInfoViewModel = spyk(TradeInInfoViewModel(tNCInfoUseCase))

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

    /**************************** getTNC() *******************************************/
    @Test
    fun getTNC() {
        val type = 1
        val tnCInfoModel: TnCInfoModel = mockk(relaxed = true)
        every { tnCInfoModel.fetchTickerAndTnC.type } returns type
        coEvery { tNCInfoUseCase.getTNCInfo(any()) } returns tnCInfoModel

        tradeInInfoViewModel.getTNC(type)

        assertEquals(tradeInInfoViewModel.tncInfoLiveData.value, tnCInfoModel)
        assertEquals(tradeInInfoViewModel.getProgBarVisibility().value, false)

    }

    @Test
    fun getTNCException() {
        val exception = Exception("Info Data Exception")
        coEvery { tNCInfoUseCase.getTNCInfo(any()) } throws exception

        tradeInInfoViewModel.getTNC(1)

        assertEquals(tradeInInfoViewModel.getErrorMessage().value, exception)
        assertEquals(tradeInInfoViewModel.getProgBarVisibility().value, false)

    }

    /**************************** getTNC() *******************************************/
}