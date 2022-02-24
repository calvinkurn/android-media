package com.tokopedia.tradein.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common_tradein.model.TradeInPDPData
import com.tokopedia.tradein.model.TradeInDetailModel
import com.tokopedia.tradein.usecase.TradeInDetailUseCase
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
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
class TradeInHomePageFragmentVMTest {

    @RelaxedMockK
    lateinit var context: Context
    val tradeInDetailUseCase: TradeInDetailUseCase = mockk()
    var tradeInHomePageFragmentVM = spyk(TradeInHomePageFragmentVM(tradeInDetailUseCase))

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
    /**************************** getPDPData() *******************************************/
    @Test
    fun getPDPData() {
        val tradeInPDPData: TradeInPDPData = mockk<TradeInPDPData>(relaxed = true)

        assertEquals(tradeInHomePageFragmentVM.getPDPData(tradeInPDPData), tradeInPDPData)

    }
    /**************************** getPDPData() *******************************************/

    /**************************** startProgressBar() *******************************************/
    @Test
    fun startProgressBar() {
        tradeInHomePageFragmentVM.startProgressBar()
        assertEquals(tradeInHomePageFragmentVM.getProgBarVisibility().value, true)

    }
    /**************************** startProgressBar() *******************************************/

    /**************************** getTradeInDetail() *******************************************/
    @Test
    fun getTradeInDetail() {
        val errorMsg = "error message"
        val tradeInDetailModel: TradeInDetailModel = mockk<TradeInDetailModel>(relaxed = true)
        every { tradeInDetailModel.getTradeInDetail.errMessage } returns  errorMsg
        coEvery { tradeInDetailUseCase.getTradeInDetail(any(), any(), any()) } returns tradeInDetailModel

        tradeInHomePageFragmentVM.getTradeInDetail(mockk(), 1000, mockk())

        assertEquals(tradeInHomePageFragmentVM.tradeInDetailLiveData.value, tradeInDetailModel)
        assertEquals(tradeInHomePageFragmentVM.logisticData, tradeInDetailModel.getTradeInDetail.logisticOptions)
        assertEquals(tradeInHomePageFragmentVM.getWarningMessage().value, errorMsg)
        assertEquals(tradeInHomePageFragmentVM.getProgBarVisibility().value, false)

    }

    @Test
    fun getTradeInDetailException() {
        val exception = Exception("TradeIn Detail Data Exception")
        coEvery { tradeInDetailUseCase.getTradeInDetail(any(), any(), any()) } throws exception

        tradeInHomePageFragmentVM.getTradeInDetail(mockk(),1000, mockk())

        assertEquals(tradeInHomePageFragmentVM.getErrorMessage().value, exception)

    }

    /**************************** getTradeInDetail() *******************************************/
}