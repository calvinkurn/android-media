package com.tokopedia.affiliate.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.affiliate.PAGE_ZERO
import com.tokopedia.affiliate.model.response.AffiliateRecommendedProductData
import com.tokopedia.affiliate.ui.fragment.AffiliateRecommendedProductFragment
import com.tokopedia.affiliate.usecase.AffiliateRecommendedProductUseCase
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
class AffiliateRecommendedProductViewModelTest{
    private val userSessionInterface: UserSessionInterface = mockk()
    private val affiliateRecommendedProductUseCase: AffiliateRecommendedProductUseCase = mockk()
    var affiliateRecommendedProductViewModel = spyk(AffiliateRecommendedProductViewModel(userSessionInterface, affiliateRecommendedProductUseCase))

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

    /**************************** getAffiliateRecommendedProduct() *******************************************/
    @Test
    fun `Get Affiliate Recommended Product Success`() {
        val affiliateRecommendedProduct : AffiliateRecommendedProductData = mockk(relaxed = true)
        val card :AffiliateRecommendedProductData.RecommendedAffiliateProduct.Data.Card = mockk(relaxed = true)
        val data = AffiliateRecommendedProductData.RecommendedAffiliateProduct.Data(
            arrayListOf(card),null,null
        )
        affiliateRecommendedProduct.recommendedAffiliateProduct?.data = data
        coEvery { affiliateRecommendedProductUseCase.affiliateGetRecommendedProduct(any(),any(),any()) } returns affiliateRecommendedProduct

        val response = affiliateRecommendedProductViewModel.convertDataToVisitable(affiliateRecommendedProduct.recommendedAffiliateProduct?.data)

        affiliateRecommendedProductViewModel.getAffiliateRecommendedProduct(
            AffiliateRecommendedProductFragment.BOUGHT_IDENTIFIER, PAGE_ZERO
        )
        assertEquals(affiliateRecommendedProductViewModel.getAffiliateDataItems().value,response)
        assertEquals(affiliateRecommendedProductViewModel.getAffiliateItemCount().value,affiliateRecommendedProduct.recommendedAffiliateProduct?.data?.pageInfo)
        assertEquals(affiliateRecommendedProductViewModel.isUserBlackListed,false)
    }

    @Test
    fun `Get Affiliate Recommended Product Exception`() {
        val exception = "Validate Data Exception"
        coEvery { affiliateRecommendedProductUseCase.affiliateGetRecommendedProduct(any(),any(),any()) } throws Exception(exception)

        affiliateRecommendedProductViewModel.getAffiliateRecommendedProduct(
            AffiliateRecommendedProductFragment.BOUGHT_IDENTIFIER, PAGE_ZERO
        )

        assertEquals(affiliateRecommendedProductViewModel.getErrorMessage().value, exception)
        assertEquals(affiliateRecommendedProductViewModel.getShimmerVisibility().value,false)
    }
}