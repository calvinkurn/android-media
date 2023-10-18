package com.tokopedia.affiliate.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.affiliate.model.response.AffiliateDiscoveryCampaignResponse
import com.tokopedia.affiliate.usecase.AffiliateDiscoveryCampaignUseCase
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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AffiliateDiscoPromoListViewModelTest {
    private val userSessionInterface: UserSessionInterface = mockk()
    private val affiliateDiscoveryCampaignUseCase: AffiliateDiscoveryCampaignUseCase = mockk()
    private val affiliatePromoViewModel =
        spyk(AffiliateDiscoPromoListViewModel(affiliateDiscoveryCampaignUseCase))

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        coEvery { userSessionInterface.userId } returns ""
        coEvery { userSessionInterface.email } returns ""

        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getDiscoBanners() {
        val discoveryCampaignResponse = mockk<AffiliateDiscoveryCampaignResponse>()
        coEvery {
            affiliateDiscoveryCampaignUseCase.getAffiliateDiscoveryCampaign(
                any(),
                any()
            )
        } returns discoveryCampaignResponse
        affiliatePromoViewModel.getDiscoBanners(0, 20)
        assertNotNull(affiliatePromoViewModel.getDiscoCampaignBanners().value)
        assertEquals(false, affiliatePromoViewModel.progressBar().value)
    }

    @Test
    fun getDiscoBannersException() {
        val throwable = Throwable("Validate Data Exception")
        coEvery {
            affiliateDiscoveryCampaignUseCase.getAffiliateDiscoveryCampaign(
                any(),
                any()
            )
        } throws throwable

        affiliatePromoViewModel.getDiscoBanners(0, 20)
        assertEquals(affiliatePromoViewModel.getErrorMessage().value, throwable)
        assertEquals(true, affiliatePromoViewModel.noMoreDataAvailable().value)
        assertEquals(false, affiliatePromoViewModel.progressBar().value)
    }

    @Test
    fun `no more data when list is null or empty`() {
        val responseWithEmptyBanners = AffiliateDiscoveryCampaignResponse(
            AffiliateDiscoveryCampaignResponse.RecommendedAffiliateDiscoveryCampaign(
                data = AffiliateDiscoveryCampaignResponse.RecommendedAffiliateDiscoveryCampaign.Data()
            )
        )
        coEvery {
            affiliateDiscoveryCampaignUseCase.getAffiliateDiscoveryCampaign(
                any(),
                any()
            )
        } returns responseWithEmptyBanners

        affiliatePromoViewModel.getDiscoBanners(0, 20)
        assertEquals(true, affiliatePromoViewModel.noMoreDataAvailable().value)
        assertEquals(false, affiliatePromoViewModel.progressBar().value)
    }

    @Test
    fun `no more data when list is less than page size`() {
        val responseWithFewBanners = AffiliateDiscoveryCampaignResponse(
            AffiliateDiscoveryCampaignResponse.RecommendedAffiliateDiscoveryCampaign(
                data = AffiliateDiscoveryCampaignResponse.RecommendedAffiliateDiscoveryCampaign.Data(
                    listOf(
                        AffiliateDiscoveryCampaignResponse.RecommendedAffiliateDiscoveryCampaign.Data.Campaign(),
                        AffiliateDiscoveryCampaignResponse.RecommendedAffiliateDiscoveryCampaign.Data.Campaign()
                    )
                )
            )
        )
        coEvery {
            affiliateDiscoveryCampaignUseCase.getAffiliateDiscoveryCampaign(
                any(),
                any()
            )
        } returns responseWithFewBanners

        affiliatePromoViewModel.getDiscoBanners(0, 20)
        assertEquals(true, affiliatePromoViewModel.noMoreDataAvailable().value)
        assertEquals(false, affiliatePromoViewModel.progressBar().value)
    }
}
