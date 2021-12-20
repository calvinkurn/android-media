package com.tokopedia.affiliate.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.affiliate.model.response.AffiliateBalance
import com.tokopedia.affiliate.model.response.AffiliateSearchData
import com.tokopedia.affiliate.repository.AffiliateRepository
import com.tokopedia.affiliate.usecase.AffiliateBalanceDataUseCase
import com.tokopedia.affiliate.usecase.AffiliateSearchUseCase
import com.tokopedia.user.session.UserSessionInterface
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
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AffiliateIncomeViewModelTest{
    var affiliateIncomeViewModel = spyk(AffiliateIncomeViewModel())
    private val repository = spyk(AffiliateRepository())
    private val affiliateBalanceDataUseCase = spyk(AffiliateBalanceDataUseCase (repository))
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

    /**************************** getAffiliateBalance() *******************************************/
    @Test
    fun `Get Affiliate Balance`() {
        val affiliateBalance : AffiliateBalance = mockk(relaxed = true)
        coEvery { affiliateBalanceDataUseCase.getAffiliateBalance() } returns affiliateBalance

        affiliateIncomeViewModel.getAffiliateBalance()

//        assertEquals(affiliateIncomeViewModel.getAffiliateBalanceData().value , affiliateBalance)

    }

    @Test
    fun `Get Affiliate Balance Exception`() {

    }
}