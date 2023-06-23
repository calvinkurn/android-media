package com.tokopedia.affiliate.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.affiliate.ON_REGISTERED
import com.tokopedia.affiliate.PAGE_ANNOUNCEMENT_HOME
import com.tokopedia.affiliate.PAGE_ANNOUNCEMENT_PROMO_PERFORMA
import com.tokopedia.affiliate.model.response.AffiliateAnnouncementDataV2
import com.tokopedia.affiliate.model.response.AffiliateDiscoveryCampaignResponse
import com.tokopedia.affiliate.model.response.AffiliateSSAShopListResponse
import com.tokopedia.affiliate.model.response.AffiliateSearchData
import com.tokopedia.affiliate.model.response.AffiliateValidateUserData
import com.tokopedia.affiliate.usecase.AffiliateAnnouncementUseCase
import com.tokopedia.affiliate.usecase.AffiliateDiscoveryCampaignUseCase
import com.tokopedia.affiliate.usecase.AffiliateGetUnreadNotificationUseCase
import com.tokopedia.affiliate.usecase.AffiliateSSAShopUseCase
import com.tokopedia.affiliate.usecase.AffiliateSearchUseCase
import com.tokopedia.affiliate.usecase.AffiliateValidateUserStatusUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.universal_sharing.view.model.GenerateAffiliateLinkEligibility
import com.tokopedia.universal_sharing.view.usecase.AffiliateEligibilityCheckUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AffiliatePromoViewModelTest {
    private val userSessionInterface: UserSessionInterface = mockk()
    private val affiliateSearchUseCase: AffiliateSearchUseCase = mockk()
    private val affiliateValidateUserStatus: AffiliateValidateUserStatusUseCase = mockk()
    private val affiliateAffiliateAnnouncementUseCase: AffiliateAnnouncementUseCase = mockk()
    private val affiliateDiscoveryCampaignUseCase: AffiliateDiscoveryCampaignUseCase = mockk()
    private val affiliateSSAShopUseCase: AffiliateSSAShopUseCase = mockk()
    private val affiliateGetUnreadNotificationUseCase: AffiliateGetUnreadNotificationUseCase = mockk()
    private val graphqlRepository: GraphqlRepository = mockk()
    private val affiliatePromoViewModel = spyk(
        AffiliatePromoViewModel(
            userSessionInterface,
            affiliateSearchUseCase,
            affiliateValidateUserStatus,
            affiliateAffiliateAnnouncementUseCase,
            affiliateDiscoveryCampaignUseCase,
            affiliateSSAShopUseCase,
            affiliateGetUnreadNotificationUseCase,
            graphqlRepository
        )
    )

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        coEvery { userSessionInterface.userId } returns ""
        coEvery { userSessionInterface.email } returns ""

        MockKAnnotations.init(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**************************** getSearch() *******************************************/
    @Test
    fun `Search Product Success`() {
        val affiliateSearchData: AffiliateSearchData = mockk(relaxed = true)
        coEvery { affiliateSearchUseCase.affiliateSearchWithLink(any()) } returns affiliateSearchData

        affiliatePromoViewModel.getSearch("")

        assertEquals(affiliatePromoViewModel.getAffiliateSearchData().value, affiliateSearchData)
        assertEquals(affiliatePromoViewModel.progressBar().value, false)
    }

    @Test
    fun `Search Product Exception`() {
        val exception = "Validate Data Exception"
        coEvery { affiliateSearchUseCase.affiliateSearchWithLink(any()) } throws Exception(exception)

        affiliatePromoViewModel.getSearch("")

        assertEquals(affiliatePromoViewModel.getErrorMessage().value, exception)
        assertEquals(affiliatePromoViewModel.progressBar().value, false)
    }

    /**************************** getAnnouncementInformation() *******************************************/
    @Test
    fun `announcement information should be there for home`() {
        val affiliateAnnouncementData: AffiliateAnnouncementDataV2 = mockk(relaxed = true)
        coEvery {
            affiliateAffiliateAnnouncementUseCase.getAffiliateAnnouncement(
                PAGE_ANNOUNCEMENT_HOME
            )
        } returns affiliateAnnouncementData

        affiliatePromoViewModel.getAnnouncementInformation(true)

        assertEquals(
            affiliatePromoViewModel.getAffiliateAnnouncement().value,
            affiliateAnnouncementData
        )
    }

    @Test
    fun `announcement information should be there for promosikan`() {
        val affiliateAnnouncementData: AffiliateAnnouncementDataV2 = mockk(relaxed = true)
        coEvery {
            affiliateAffiliateAnnouncementUseCase.getAffiliateAnnouncement(
                PAGE_ANNOUNCEMENT_PROMO_PERFORMA
            )
        } returns affiliateAnnouncementData

        affiliatePromoViewModel.getAnnouncementInformation(false)

        assertEquals(
            affiliatePromoViewModel.getAffiliateAnnouncement().value,
            affiliateAnnouncementData
        )
    }

    @Test
    fun getAnnouncementValidateException() {
        val throwable = Throwable("Validate Data Exception")
        coEvery {
            affiliateAffiliateAnnouncementUseCase.getAffiliateAnnouncement(
                PAGE_ANNOUNCEMENT_PROMO_PERFORMA
            )
        } throws throwable

        affiliatePromoViewModel.getAnnouncementInformation(true)
    }

    /**************************** getAffiliateValidateUser() *******************************************/
    @Test
    fun getAffiliateValidateUser() {
        val affiliateValidateUserData: AffiliateValidateUserData = mockk(relaxed = true)
        coEvery { affiliateValidateUserStatus.validateUserStatus(any()) } returns affiliateValidateUserData

        affiliatePromoViewModel.getAffiliateValidateUser()

        assertEquals(affiliatePromoViewModel.getValidateUserdata().value, affiliateValidateUserData)
    }

    @Test
    fun getAffiliateValidateUserException() {
        val throwable = Throwable("Validate Data Exception")
        coEvery { affiliateValidateUserStatus.validateUserStatus(any()) } throws throwable

        affiliatePromoViewModel.getAffiliateValidateUser()
    }

    /**************************** setUserState() *******************************************/
    @Test
    fun setUserStateTest() {
        val state = ON_REGISTERED
        affiliatePromoViewModel.setValidateUserType(state)
        assertEquals(affiliatePromoViewModel.getValidateUserType().value, state)
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
        affiliatePromoViewModel.getDiscoBanners(0, 7)
        assertNotNull(affiliatePromoViewModel.getDiscoCampaignBanners().value)
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

        affiliatePromoViewModel.getDiscoBanners(0, 7)
        assertEquals(affiliatePromoViewModel.getDiscoCampaignBanners().value, null)
    }

    @Test
    fun getTokoNowException() {
        val generateAffiliateLinkEligibility = mockk<GenerateAffiliateLinkEligibility>()
        val gqlResponse = mockk<GraphqlResponse>(relaxed = true)
        val affiliateEligibilityCheckUseCase =
            mockk<AffiliateEligibilityCheckUseCase>(relaxed = true)
        val throwable = Throwable("Validate Data Exception")

        coEvery {
            affiliateEligibilityCheckUseCase.apply {
                params = any()
            }.executeOnBackground()
        } returns generateAffiliateLinkEligibility

        coEvery {
            graphqlRepository.response(any(), any())
        } returns gqlResponse

        coEvery {
            gqlResponse.getData<GenerateAffiliateLinkEligibility.Response>(any())
        } throws throwable

        assertEquals(affiliatePromoViewModel.getTokoNowBottomSheetData().value, null)
    }

    @Test
    fun getTokoNowBottomSheetData() {
        val generateAffiliateLinkEligibility = mockk<GenerateAffiliateLinkEligibility>()
        val generateAffiliateLinkEligibilityResponse =
            GenerateAffiliateLinkEligibility.Response(generateAffiliateLinkEligibility)
        val gqlResponse = mockk<GraphqlResponse>(relaxed = true)
        val affiliateEligibilityCheckUseCase =
            mockk<AffiliateEligibilityCheckUseCase>(relaxed = true)

        coEvery {
            affiliateEligibilityCheckUseCase.apply {
                params = any()
            }.executeOnBackground()
        } returns generateAffiliateLinkEligibility

        coEvery {
            graphqlRepository.response(any(), any())
        } returns gqlResponse
        coEvery {
            gqlResponse.getData<GenerateAffiliateLinkEligibility.Response>(any())
        } returns generateAffiliateLinkEligibilityResponse

        affiliatePromoViewModel.getTokoNowBottomSheetInfo("11530573")

        assertEquals(
            affiliatePromoViewModel.getTokoNowBottomSheetData().value,
            generateAffiliateLinkEligibility
        )
    }

    @Test
    fun `should have ssa shops on success response`() {
        val ssaShop = AffiliateSSAShopListResponse.Data.SSAShop.ShopDataItem(
            AffiliateSSAShopListResponse.Data.SSAShop.ShopDataItem.SSAShopDetail(),
            AffiliateSSAShopListResponse.Data.SSAShop.ShopDataItem.SSACommissionDetail()
        )
        val response = AffiliateSSAShopListResponse(
            AffiliateSSAShopListResponse.Data(
                AffiliateSSAShopListResponse.Data.SSAShop(
                    1,
                    AffiliateSSAShopListResponse.Data.SSAShop.PageInfo(
                        hasNext = true
                    ),
                    null,
                    listOf(ssaShop, ssaShop, ssaShop)
                )
            )
        )
        coEvery { affiliateSSAShopUseCase.getSSAShopList(any(), any()) } returns response
        affiliatePromoViewModel.fetchSSAShopList()
        assertFalse(affiliatePromoViewModel.getSSAShopList().value.isNullOrEmpty())
    }

    @Test
    fun `should have proper error message on error response`() {
        val emptyShopErrorMessage = "No Shops Found"
        val response = AffiliateSSAShopListResponse(
            AffiliateSSAShopListResponse.Data(
                AffiliateSSAShopListResponse.Data.SSAShop(
                    0,
                    null,
                    AffiliateSSAShopListResponse.Data.SSAShop.Error(
                        "No Shops Found"
                    )
                )
            )
        )
        coEvery { affiliateSSAShopUseCase.getSSAShopList(any(), any()) } returns response
        affiliatePromoViewModel.fetchSSAShopList()
        assertTrue(affiliatePromoViewModel.getSSAShopList().value.isNullOrEmpty())
        assertEquals(affiliatePromoViewModel.getErrorMessage().value, emptyShopErrorMessage)
    }

    @Test
    fun `should throw exception any error`() {
        val throwable = Throwable("Validate Data Exception")
        coEvery { affiliateSSAShopUseCase.getSSAShopList(any(), any()) } throws throwable
        affiliatePromoViewModel.fetchSSAShopList()
        assertTrue(affiliatePromoViewModel.getSSAShopList().value.isNullOrEmpty())
    }

    /**************************** userSession() *******************************************/

    @Test
    fun userSessionTest() {
        val name = "Testing"
        val profile = "Profile Testing"
        val isLoggedIn = false
        coEvery { userSessionInterface.name } returns name
        coEvery { userSessionInterface.profilePicture } returns profile
        coEvery { userSessionInterface.isLoggedIn } returns isLoggedIn

        assertEquals(affiliatePromoViewModel.getUserName(), name)
        assertEquals(affiliatePromoViewModel.getUserProfilePicture(), profile)
        assertEquals(affiliatePromoViewModel.isUserLoggedIn(), isLoggedIn)
    }

    @Test
    fun `successfully getting unread notification count`() {
        coEvery {
            affiliateGetUnreadNotificationUseCase.getUnreadNotifications()
        } returns 5

        affiliatePromoViewModel.fetchUnreadNotificationCount()
        assertEquals(5, affiliatePromoViewModel.getUnreadNotificationCount().value)
    }

    @Test
    fun `should reset notification count to zero`() {
        coEvery {
            affiliateGetUnreadNotificationUseCase.getUnreadNotifications()
        } returns 5

        affiliatePromoViewModel.fetchUnreadNotificationCount()
        assertEquals(5, affiliatePromoViewModel.getUnreadNotificationCount().value)

        affiliatePromoViewModel.resetNotificationCount()
        assertEquals(0, affiliatePromoViewModel.getUnreadNotificationCount().value)
    }
}
