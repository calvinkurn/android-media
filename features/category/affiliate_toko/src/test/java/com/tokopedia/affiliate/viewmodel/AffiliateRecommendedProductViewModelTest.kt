package com.tokopedia.affiliate.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.affiliate.PAGE_ZERO
import com.tokopedia.affiliate.model.response.AffiliateRecommendedProductData
import com.tokopedia.affiliate.ui.fragment.AffiliateRecommendedProductFragment
import com.tokopedia.affiliate.usecase.AffiliateRecommendedProductUseCase
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
class AffiliateRecommendedProductViewModelTest {
    private val affiliateRecommendedProductUseCase: AffiliateRecommendedProductUseCase = mockk()
    var affiliateRecommendedProductViewModel = spyk(AffiliateRecommendedProductViewModel(affiliateRecommendedProductUseCase))

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

    /**************************** getAffiliateRecommendedProduct() *******************************************/
    @Test
    fun `Get Affiliate Recommended Product Success`() {
        val cardItem: AffiliateRecommendedProductData.RecommendedAffiliateProduct.Data.Card.Item = mockk(relaxed = true)
        val cardList = MutableList(2) {
            cardItem
        }
        val card = AffiliateRecommendedProductData.RecommendedAffiliateProduct.Data.Card(null, cardList, null)
        val list = MutableList(2) {
            card
        }
        val data = AffiliateRecommendedProductData.RecommendedAffiliateProduct.Data(
            list,
            null,
            null
        )
        val affiliateRecommendedProduct = AffiliateRecommendedProductData(AffiliateRecommendedProductData.RecommendedAffiliateProduct(data, null))
        coEvery { affiliateRecommendedProductUseCase.affiliateGetRecommendedProduct(any(), any(), any()) } returns affiliateRecommendedProduct

        val response = affiliateRecommendedProductViewModel.convertDataToVisitable(affiliateRecommendedProduct.recommendedAffiliateProduct?.productData)

        affiliateRecommendedProductViewModel.getAffiliateRecommendedProduct(
            AffiliateRecommendedProductFragment.BOUGHT_IDENTIFIER,
            PAGE_ZERO
        )
        assertEquals(Gson().toJson(affiliateRecommendedProductViewModel.getAffiliateDataItems().value), Gson().toJson(response))
        assertEquals(affiliateRecommendedProductViewModel.getAffiliateItemCount().value, affiliateRecommendedProduct.recommendedAffiliateProduct?.productData?.pageInfo)
        assertEquals(affiliateRecommendedProductViewModel.isUserBlackListed, false)
    }

    @Test
    fun `Get Affiliate Recommended Product Exception`() {
        val exception = "Validate Data Exception"
        coEvery { affiliateRecommendedProductUseCase.affiliateGetRecommendedProduct(any(), any(), any()) } throws Exception(exception)

        affiliateRecommendedProductViewModel.getAffiliateRecommendedProduct(
            AffiliateRecommendedProductFragment.BOUGHT_IDENTIFIER,
            PAGE_ZERO
        )

        assertEquals(affiliateRecommendedProductViewModel.getErrorMessage().value, exception)
        assertEquals(affiliateRecommendedProductViewModel.getShimmerVisibility().value, false)
    }
}
