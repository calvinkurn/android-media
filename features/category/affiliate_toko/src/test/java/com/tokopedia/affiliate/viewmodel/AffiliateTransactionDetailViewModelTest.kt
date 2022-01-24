package com.tokopedia.affiliate.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.affiliate.PAGE_ZERO
import com.tokopedia.affiliate.model.response.AffiliateCommissionDetailsData
import com.tokopedia.affiliate.model.response.AffiliatePerformanceData
import com.tokopedia.affiliate.model.response.AffiliateValidateUserData
import com.tokopedia.affiliate.ui.fragment.AffiliateRecommendedProductFragment
import com.tokopedia.affiliate.usecase.*
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
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
class AffiliateTransactionDetailViewModelTest{
    private val affiliateCommissionDetailUserCase: AffiliateCommissionDetailsUseCase = mockk()
    private var affiliateTransactionDetailViewModel = spyk(AffiliateTransactionDetailViewModel(affiliateCommissionDetailUserCase))

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

    /**************************** getAffiliateCommission() *******************************************/
    @Test
    fun getAffiliateCommission() {
        val affiliateCommisionDetails : AffiliateCommissionDetailsData = mockk(relaxed = true)
        val cardDetails : AffiliateCommissionDetailsData.GetAffiliateCommissionDetail.Data.Detail = mockk(relaxed = true)
        val data = AffiliateCommissionDetailsData.GetAffiliateCommissionDetail.Data(null,"","",
            arrayListOf(cardDetails),null,null,null,null,"","")
        affiliateCommisionDetails.getAffiliateCommissionDetail?.data = data
        coEvery { affiliateCommissionDetailUserCase.affiliateCommissionDetails(any()) } returns affiliateCommisionDetails

        val response = affiliateTransactionDetailViewModel.getDetailListOrganize(affiliateCommisionDetails?.getAffiliateCommissionDetail?.data?.detail)

        affiliateTransactionDetailViewModel.affiliateCommission("16d106d0-38ad-43b3-9245-99cab79eb09f")

        assertEquals(affiliateTransactionDetailViewModel.getDetailList().value,response)
        assertEquals(affiliateTransactionDetailViewModel.getCommissionData().value,affiliateCommisionDetails.getAffiliateCommissionDetail)
        assertEquals(affiliateTransactionDetailViewModel.progressBar().value,false)
    }

    @Test
    fun getAffiliateCommissionException() {

        val exception = java.lang.Exception("Validate Data Exception")
        coEvery { affiliateCommissionDetailUserCase.affiliateCommissionDetails(any()) } throws exception

        affiliateTransactionDetailViewModel.affiliateCommission("16d106d0-38ad-43b3-9245-99cab79eb09f")

        assertEquals(affiliateTransactionDetailViewModel.getErrorMessage().value, exception)
        assertEquals(affiliateTransactionDetailViewModel.progressBar().value,false)

    }
}