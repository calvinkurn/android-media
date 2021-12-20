package com.tokopedia.affiliate.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.affiliate.usecase.*
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AffiliateWithdrawalDetailViewModelTest{
    private val affiliateWithdrawalDetailsUseCase: AffiliateWithdrawalDetailsUseCase = mockk()
    private var affiliateWithdrawalDetailViewModel = spyk(WithdrawalDetailViewModel(affiliateWithdrawalDetailsUseCase))

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

    /**************************** getWithdrawalInfo() *******************************************/
    @Test
    fun getWithdrawalInfo() {

    }

    @Test
    fun getAffiliateCommissionException() {


    }
}