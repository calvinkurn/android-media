package com.tokopedia.topads.credit.history.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topads.common.data.response.Deposit
import com.tokopedia.topads.common.data.response.DepositAmount
import com.tokopedia.topads.common.data.response.Error
import com.tokopedia.topads.common.data.response.TopadsDashboardDeposits
import com.tokopedia.topads.common.domain.usecase.TopAdsGetDepositUseCase
import com.tokopedia.topads.credit.history.data.model.TopAdsCreditHistory
import com.tokopedia.topads.dashboard.data.model.ExpiryDateResponse
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsAutoTopUpUSeCase
import com.tokopedia.topads.dashboard.domain.interactor.TopadsGetFreeDepositUseCase
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpData
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpStatus
import com.tokopedia.topads.domain.usecase.TopadsCreditHistoryUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.*

class TopAdsCreditHistoryViewModelTest {
    lateinit var viewModel: TopAdsCreditHistoryViewModel

    @Mock
    lateinit var userSessionInterface: UserSessionInterface

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val pendingRewardUseCase: TopadsGetFreeDepositUseCase = mockk(relaxed = true)
    private var autoTopUpUSeCase: TopAdsAutoTopUpUSeCase = mockk(relaxed = true)
    private var topAdsGetShopDepositUseCase: TopAdsGetDepositUseCase = mockk(relaxed = true)
    private val topAdsCreditHistoryUseCase: TopadsCreditHistoryUseCase = mockk(relaxed = true)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = TopAdsCreditHistoryViewModel(
            userSessionInterface, autoTopUpUSeCase, topAdsGetShopDepositUseCase,
            topAdsCreditHistoryUseCase, Dispatchers.Main, pendingRewardUseCase
        )
        Mockito.`when`(userSessionInterface.userId).thenReturn("12345")
        Mockito.`when`(userSessionInterface.shopId).thenReturn("123456")
    }

    @Test
    fun `loadPendingReward test`() {
        val actual: ExpiryDateResponse.TopAdsGetFreeDeposit = mockk(relaxed = true)

        every { pendingRewardUseCase.execute(captureLambda()) } answers {
            firstArg<(ExpiryDateResponse.TopAdsGetFreeDeposit) -> Unit>().invoke(actual)
        }
        viewModel.loadPendingReward()

        Assert.assertEquals(viewModel.expiryDateHiddenTrial.value, actual.pendingRebateCredit)
    }

    @Test
    fun `getAutoTopUpStatus response null test, livedata should be fail`() {
        val mockObject = mockk<AutoTopUpData.Response>(relaxed = true)

        every { mockObject.response } returns null
        every { autoTopUpUSeCase.execute(captureLambda(), any()) } answers {
            firstArg<(AutoTopUpData.Response) -> Unit>().invoke(mockObject)
        }

        viewModel.getAutoTopUpStatus()
        Assert.assertTrue(viewModel.getAutoTopUpStatus.value is Fail)
    }

    @Test
    fun `getAutoTopUpStatus success - response not null and error is empty test, livedata should contain data`() {
        val mockObject = AutoTopUpData.Response(AutoTopUpData(AutoTopUpStatus()))

        every { autoTopUpUSeCase.execute(captureLambda(), any()) } answers {
            firstArg<(AutoTopUpData.Response) -> Unit>().invoke(mockObject)
        }

        viewModel.getAutoTopUpStatus()
        Assert.assertEquals((viewModel.getAutoTopUpStatus.value as Success).data,
            mockObject.response?.data)
    }

    @Test
    fun `getAutoTopUpStatus response not null and error not empty test, livedata should be fail`() {

        val actual = AutoTopUpData.Response(AutoTopUpData(errors = listOf(Error())))

        every { autoTopUpUSeCase.execute(captureLambda(), any()) } answers {
            firstArg<(AutoTopUpData.Response) -> Unit>().invoke(actual)
        }

        viewModel.getAutoTopUpStatus()
        Assert.assertTrue(viewModel.getAutoTopUpStatus.value is Fail)
    }

    @Test
    fun `getAutoTopUpStatus on exception occured test`() {
        val actual = Exception("it")

        every { autoTopUpUSeCase.execute(any(), captureLambda()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(actual)
        }

        viewModel.getAutoTopUpStatus()
        Assert.assertEquals((viewModel.getAutoTopUpStatus.value as Fail).throwable, actual)
    }

    @Test
    fun `auto topup status pass`() {
        viewModel.getAutoTopUpStatus()
        verify {
            autoTopUpUSeCase.execute(any(), any())
        }
    }

    @Test
    fun `getShopDeposit on success should contain value in livedata`() {
        val actual = Deposit(TopadsDashboardDeposits(DepositAmount(0, "10")))

        every { topAdsGetShopDepositUseCase.execute(captureLambda(), any()) } answers {
            firstArg<(Deposit) -> Unit>().invoke(actual)
        }

        viewModel.getShopDeposit()
        Assert.assertEquals(viewModel.creditAmount.value,
            actual.topadsDashboardDeposits.data.amountFmt)
    }

    @Test
    fun `getShopDeposit on exception ,livedata value should be null`() {

        every { topAdsGetShopDepositUseCase.execute(any(), captureLambda()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(Throwable())
        }
        viewModel.getShopDeposit()
        Assert.assertEquals(viewModel.creditAmount.value, null)
    }

    @Test
    fun `test result in getTopAdsDeposit`() {
        viewModel.getShopDeposit()
        verify {
            topAdsGetShopDepositUseCase.execute(any(), any())
        }
    }
}