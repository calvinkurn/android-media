package com.tokopedia.topads.credit.history.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetDepositUseCase
import com.tokopedia.topads.credit.history.data.model.TopAdsCreditHistory
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsAutoTopUpUSeCase
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

    private var autoTopUpUSeCase: TopAdsAutoTopUpUSeCase = mockk(relaxed = true)
    private var topAdsGetShopDepositUseCase: TopAdsGetDepositUseCase = mockk(relaxed = true)
    private val topAdsCreditHistoryUseCase: GraphqlUseCase<TopAdsCreditHistory.CreditsResponse> = mockk(relaxed = true)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = TopAdsCreditHistoryViewModel(userSessionInterface, autoTopUpUSeCase, topAdsGetShopDepositUseCase, topAdsCreditHistoryUseCase, Dispatchers.Main)
        Mockito.`when`(userSessionInterface.userId).thenReturn("12345")
        Mockito.`when`(userSessionInterface.shopId).thenReturn("123456")
    }

    @Test
    fun `credit history execute`() {
        viewModel.getCreditHistory("", Date(), Date())
        verify {
            topAdsCreditHistoryUseCase.execute(any(), any())
        }
    }

    @Test
    fun `credit history`() {
        val expected = 5.0f
        var actual = 0.0f
        val data = TopAdsCreditHistory.CreditsResponse(response = TopAdsCreditHistory.Response(dataHistory = TopAdsCreditHistory(totalAddition = expected)))
        every { topAdsCreditHistoryUseCase.execute(captureLambda(), any()) } answers {
            actual = data.response.dataHistory.totalAddition
        }
        viewModel.getCreditHistory("", Date(), Date())
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `auto topup status pass`() {
        viewModel.getAutoTopUpStatus("")
        verify {
            autoTopUpUSeCase.execute(any(), any())
        }
    }

    @Test
    fun `test result in getTopAdsDeposit`() {
        viewModel.getShopDeposit()
        verify {
            topAdsGetShopDepositUseCase.execute(any(), any())
        }
    }
}