package com.tokopedia.affiliate.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.affiliate.PAGE_ZERO
import com.tokopedia.affiliate.model.response.AffiliateSSAShopListResponse
import com.tokopedia.affiliate.usecase.AffiliateSSAShopUseCase
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
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AffiliateSSAShopViewModelTest {
    private val affiliateSSAShopUseCase: AffiliateSSAShopUseCase = mockk()
    private val affiliateSSAShopViewModel: AffiliateSSAShopViewModel = spyk(
        AffiliateSSAShopViewModel(affiliateSSAShopUseCase)
    )

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

    @Test
    fun `has ssa shops on success response`() {
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
        assertEquals(affiliateSSAShopViewModel.progressBar().value, true)
        affiliateSSAShopViewModel.fetchSSAShopList(PAGE_ZERO)
        assertEquals(affiliateSSAShopViewModel.progressBar().value, false)
        assertEquals(affiliateSSAShopViewModel.noMoreDataAvailable().value, false)
        assertFalse(affiliateSSAShopViewModel.getSSAShopList().value.isNullOrEmpty())
    }

    @Test
    fun `toggle progress bar and error on failure response`() {
        val response = AffiliateSSAShopListResponse(
            AffiliateSSAShopListResponse.Data(
                AffiliateSSAShopListResponse.Data.SSAShop(
                    null,
                    null,
                    AffiliateSSAShopListResponse.Data.SSAShop.Error(message = "Server Error"),
                    null
                )
            )
        )
        val throwable = Throwable(response.getSSAShopList?.data?.error?.message)
        coEvery { affiliateSSAShopUseCase.getSSAShopList(any(), any()) } returns response

        affiliateSSAShopViewModel.fetchSSAShopList(PAGE_ZERO)

        assertEquals(affiliateSSAShopViewModel.progressBar().value, false)
        assertEquals(affiliateSSAShopViewModel.getErrorMessage().value?.message, throwable.message)
    }

    @Test
    fun `toggle progress bar and error on exception`() {
        val throwable = Throwable("Validate Data Exception")
        coEvery { affiliateSSAShopUseCase.getSSAShopList(any(), any()) } throws throwable

        affiliateSSAShopViewModel.fetchSSAShopList(PAGE_ZERO)

        assertEquals(affiliateSSAShopViewModel.progressBar().value, false)
        assertEquals(affiliateSSAShopViewModel.getErrorMessage().value, throwable)
    }
}
