package com.tokopedia.affiliate.viewmodel

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.affiliate.AFFILIATE_SSA_SHOP
import com.tokopedia.affiliate.ON_REGISTERED
import com.tokopedia.affiliate.PAGE_ANNOUNCEMENT_PROMOSIKAN
import com.tokopedia.affiliate.model.response.AffiliateAnnouncementDataV2
import com.tokopedia.affiliate.model.response.AffiliateSearchData
import com.tokopedia.affiliate.model.response.AffiliateValidateUserData
import com.tokopedia.affiliate.usecase.AffiliateAnnouncementUseCase
import com.tokopedia.affiliate.usecase.AffiliateSearchUseCase
import com.tokopedia.affiliate.usecase.AffiliateValidateUserStatusUseCase
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.clearStaticMockk
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class AffiliatePromoViewModelTest {
    private val userSessionInterface: UserSessionInterface = mockk()
    private val affiliateSearchUseCase: AffiliateSearchUseCase = mockk()
    private val affiliateValidateUserStatus: AffiliateValidateUserStatusUseCase = mockk()
    private val affiliateAffiliateAnnouncementUseCase: AffiliateAnnouncementUseCase = mockk()
    private val affiliatePromoViewModel = spyk(
        AffiliatePromoViewModel(
            userSessionInterface,
            affiliateSearchUseCase,
            affiliateValidateUserStatus,
            affiliateAffiliateAnnouncementUseCase
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
        Dispatchers.setMain(TestCoroutineDispatcher())
        mockkStatic(RemoteConfigInstance::class)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
        clearStaticMockk(RemoteConfigInstance::class)
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
    fun getAnnouncementInformation() {
        val affiliateAnnouncementData: AffiliateAnnouncementDataV2 = mockk(relaxed = true)
        coEvery {
            affiliateAffiliateAnnouncementUseCase.getAffiliateAnnouncement(
                PAGE_ANNOUNCEMENT_PROMOSIKAN
            )
        } returns affiliateAnnouncementData

        affiliatePromoViewModel.getAnnouncementInformation()

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
                PAGE_ANNOUNCEMENT_PROMOSIKAN
            )
        } throws throwable

        affiliatePromoViewModel.getAnnouncementInformation()
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
    fun isSSAEnabled() {
        every {
            RemoteConfigInstance.getInstance().abTestPlatform.getString(
                AFFILIATE_SSA_SHOP,
                ""
            )
        } returns AFFILIATE_SSA_SHOP

        assertEquals(affiliatePromoViewModel.isAffiliateSSAShopEnabled(), true)
    }
}
