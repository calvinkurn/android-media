package com.tokopedia.affiliate.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.affiliate.model.response.AffiliatePerformanceData
import com.tokopedia.affiliate.usecase.*
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
class AffiliatePromotionHistoryViewModelTest {
    private val affiliatePerformanceUseCase: AffiliatePerformanceUseCase = mockk()
    private var affiliatePromotionHistoryViewModel = spyk(AffiliatePromotionHistoryViewModel(affiliatePerformanceUseCase))

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

    /**************************** getAffiliatePerformance() *******************************************/
    @Test
    fun getAffiliatePerformance() {
        val affiliatePerformanceData: AffiliatePerformanceData = mockk(relaxed = true)
        val item: AffiliatePerformanceData.GetAffiliateItemsPerformanceList.Data.SectionData.Item = mockk(relaxed = true)
        val sectionData = AffiliatePerformanceData.GetAffiliateItemsPerformanceList.Data.SectionData(
            null,
            null,
            null,
            0,
            null,
            arrayListOf(item),
            null,
            null
        )
        affiliatePerformanceData.getAffiliateItemsPerformanceList?.itemPerformanceListData?.sectionData = sectionData
        coEvery { affiliatePerformanceUseCase.affiliatePerformance(any(), any()) } returns affiliatePerformanceData

        val response = affiliatePromotionHistoryViewModel.convertDataToVisitable(affiliatePerformanceData.getAffiliateItemsPerformanceList?.itemPerformanceListData?.sectionData!!)
        affiliatePromotionHistoryViewModel.getAffiliatePerformance(0)
        assertEquals(affiliatePromotionHistoryViewModel.getAffiliateDataItems().value, response)
        assertEquals(affiliatePromotionHistoryViewModel.getAffiliateItemCount().value, sectionData.itemTotalCount)
    }

    @Test
    fun getAffiliatePerformanceException() {
        val throwable = Throwable("Performance Data Exception")
        coEvery { affiliatePerformanceUseCase.affiliatePerformance(any(), any()) } throws throwable

        affiliatePromotionHistoryViewModel.getAffiliatePerformance(0)

        assertEquals(affiliatePromotionHistoryViewModel.getErrorMessage().value, throwable)
        assertEquals(affiliatePromotionHistoryViewModel.getShimmerVisibility().value, false)
    }
}
