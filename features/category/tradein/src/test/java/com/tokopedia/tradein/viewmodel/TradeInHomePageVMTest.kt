package com.tokopedia.tradein.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.laku6.tradeinsdk.api.TradeInApiService
import com.tokopedia.atc_common.domain.usecase.AddToCartOcsUseCase
import com.tokopedia.common_tradein.model.TradeInPDPData
import com.tokopedia.tradein.TradeinConstants
import com.tokopedia.tradein.usecase.InsertLogisticPreferenceUseCase
import com.tokopedia.user.session.UserSession
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*

@ExperimentalCoroutinesApi
class TradeInHomePageVMTest {
    val userSession: UserSession = mockk(relaxed = true)
    val insertLogisticPreferenceUseCase: InsertLogisticPreferenceUseCase = mockk(relaxed = true)
    val addToCartOcsUseCase: AddToCartOcsUseCase = mockk(relaxed = true)
    var tradeInHomePageVM = spyk(TradeInHomePageVM(userSession, insertLogisticPreferenceUseCase, addToCartOcsUseCase))

    @RelaxedMockK
    lateinit var laku6TradeIn: TradeInApiService

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

        Assert.assertEquals(tradeInHomePageVM.getPDPData(tradeInPDPData), tradeInPDPData)
    }
    /**************************** getPDPData() *******************************************/

    /**************************** checkLogin() *******************************************/

    @Test
    fun `check askUserLogin is LOGIN_REQUIRED`() {
        coEvery { userSession.isLoggedIn } returns false

        tradeInHomePageVM.checkLogin()

        Assert.assertEquals(
            tradeInHomePageVM.askUserLogin.value,
            TradeinConstants.LOGIN_REQUIRED
        )
    }

    @Test
    fun `check askUserLogin is LOGEED_IN`() {
        coEvery { userSession.isLoggedIn } returns true

        tradeInHomePageVM.checkLogin()

        Assert.assertEquals(tradeInHomePageVM.askUserLogin.value, TradeinConstants.LOGGED_IN)
    }
    /**************************** checkLogin() *******************************************/
}
