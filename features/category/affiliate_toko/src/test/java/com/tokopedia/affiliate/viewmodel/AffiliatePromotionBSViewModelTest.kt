package com.tokopedia.affiliate.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.affiliate.model.response.AffiliateGenerateLinkData
import com.tokopedia.affiliate.usecase.AffiliateGenerateLinkUseCase
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
class AffiliatePromotionBSViewModelTest {
    private val userSessionInterface: UserSessionInterface = mockk()
    private val affiliateGenerateLinkUseCase: AffiliateGenerateLinkUseCase = mockk()

    var affiliatePromotionBSViewModel = spyk(AffiliatePromotionBSViewModel(userSessionInterface, affiliateGenerateLinkUseCase))

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        coEvery { userSessionInterface.userId } returns ""

        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**************************** affiliateGenerateLink() *******************************************/
    @Test
    fun affiliateGenerateLink() {
        val data: AffiliateGenerateLinkData.AffiliateGenerateLink.Data = mockk(relaxed = true)
        coEvery { affiliateGenerateLinkUseCase.affiliateGenerateLink(any(), any(), any()) } returns data

        affiliatePromotionBSViewModel.affiliateGenerateLink(0, "", "")

        assertEquals(affiliatePromotionBSViewModel.generateLinkData().value, data)
        assertEquals(affiliatePromotionBSViewModel.loading().value, false)

    }

    @Test
    fun affiliateGenerateLinkException() {
        val exception = "Generate Link Exception"
        coEvery { affiliateGenerateLinkUseCase.affiliateGenerateLink(any(), any(), any()) } throws Exception(exception)

        affiliatePromotionBSViewModel.affiliateGenerateLink(0, "", "")

        assertEquals(affiliatePromotionBSViewModel.getErrorMessage().value, exception)
        assertEquals(affiliatePromotionBSViewModel.loading().value, false)
    }
}