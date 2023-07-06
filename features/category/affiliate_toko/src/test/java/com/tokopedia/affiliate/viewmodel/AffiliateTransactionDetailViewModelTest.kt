package com.tokopedia.affiliate.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.affiliate.model.response.AffiliateCommissionDetailsData
import com.tokopedia.affiliate.model.response.AffiliateTrafficCommissionCardDetails
import com.tokopedia.affiliate.usecase.*
import com.tokopedia.universal_sharing.usecase.ExtractBranchLinkUseCase
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
class AffiliateTransactionDetailViewModelTest {
    private val affiliateCommissionDetailUserCase: AffiliateCommissionDetailsUseCase = mockk()
    private val extractBranchLinkUseCase: ExtractBranchLinkUseCase = mockk()
    private var affiliateTransactionDetailViewModel = spyk(AffiliateTransactionDetailViewModel(affiliateCommissionDetailUserCase, extractBranchLinkUseCase))

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
        val cardList = arrayListOf(
            AffiliateCommissionDetailsData.GetAffiliateCommissionDetail.Data.Detail(null, null, "divider", null, null, null, null),
            AffiliateCommissionDetailsData.GetAffiliateCommissionDetail.Data.Detail(null, null, "non-divider", null, null, null, null)
        )
        val affiliateCommisionDetails = AffiliateCommissionDetailsData(
            AffiliateCommissionDetailsData.GetAffiliateCommissionDetail(
                AffiliateCommissionDetailsData.GetAffiliateCommissionDetail.Data(
                    null, "", "",
                    cardList, "Title", null, null, null, "", "", "PRODUCT", "", ""
                )
            )
        )
        coEvery { affiliateCommissionDetailUserCase.affiliateCommissionDetails(any()) } returns affiliateCommisionDetails
        val affiliateTrafficCommissionCardDetails = AffiliateTrafficCommissionCardDetails(
            AffiliateTrafficCommissionCardDetails.GetAffiliateTrafficCommissionDetailCards(
                AffiliateTrafficCommissionCardDetails.GetAffiliateTrafficCommissionDetailCards.Data(
                    null,
                    null,
                    null,
                    null,
                    arrayListOf(AffiliateTrafficCommissionCardDetails.GetAffiliateTrafficCommissionDetailCards.Data.TrafficCommissionCardDetail("", "", null))
                )
            )
        )
        coEvery { affiliateCommissionDetailUserCase.affiliateTrafficCardDetails(any(), any(), any()) } returns affiliateTrafficCommissionCardDetails
        val response = affiliateTransactionDetailViewModel.getDetailListOrganize(affiliateCommisionDetails?.getAffiliateCommissionDetail?.commissionDetailData?.detail)
        affiliateTransactionDetailViewModel.affiliateCommission("16d106d0-38ad-43b3-9245-99cab79eb09f")

        assertEquals(Gson().toJson(affiliateTransactionDetailViewModel.getDetailList().value), Gson().toJson(response))
        assertEquals(affiliateTransactionDetailViewModel.getCommissionData().value, affiliateCommisionDetails.getAffiliateCommissionDetail)
        assertEquals(affiliateTransactionDetailViewModel.progressBar().value, false)
        assertEquals(affiliateTransactionDetailViewModel.commissionType, "PRODUCT")
        affiliateCommisionDetails.getAffiliateCommissionDetail?.commissionDetailData?.commissionType = "TRAFFIC"
        affiliateTransactionDetailViewModel.affiliateCommission("16d106d0-38ad-43b3-9245-99cab79eb09f")
        affiliateTransactionDetailViewModel.affiliateCommission("16d106d0-38ad-43b3-9245-99cab79eb09f", 1)
        assertEquals(affiliateTransactionDetailViewModel.getShimmerVisibility().value, false)
        assertEquals(affiliateTransactionDetailViewModel.getDetailTitle().value, "Title")
    }

    @Test
    fun getAffiliateCommissionException() {
        val exception = java.lang.Exception("Validate Data Exception")
        coEvery { affiliateCommissionDetailUserCase.affiliateCommissionDetails(any()) } throws exception
        coEvery { affiliateCommissionDetailUserCase.affiliateTrafficCardDetails(any(), any(), any()) } throws exception

        affiliateTransactionDetailViewModel.affiliateCommission("16d106d0-38ad-43b3-9245-99cab79eb09f")

        assertEquals(affiliateTransactionDetailViewModel.getErrorMessage().value, exception)
        assertEquals(affiliateTransactionDetailViewModel.progressBar().value, false)

        affiliateTransactionDetailViewModel.affiliateCommission("16d106d0-38ad-43b3-9245-99cab79eb09f", 1)
        assertEquals(affiliateTransactionDetailViewModel.getShimmerVisibility().value, false)
    }

    @Test
    fun extractBranchLinkTest() {
        coEvery { extractBranchLinkUseCase.invoke(any()).android_deeplink } returns ""
        affiliateTransactionDetailViewModel.extractBranchLink("")
        assertNotNull(affiliateTransactionDetailViewModel.getAppLink())
    }

    @Test
    fun extractBranchLinkExceptionTest() {
        val exception = java.lang.Exception("Validate Data Exception")

        coEvery { extractBranchLinkUseCase.invoke(any()).android_deeplink } throws exception
        affiliateTransactionDetailViewModel.extractBranchLink("")

        assertEquals(affiliateTransactionDetailViewModel.getErrorMessage().value, exception)
    }
}
