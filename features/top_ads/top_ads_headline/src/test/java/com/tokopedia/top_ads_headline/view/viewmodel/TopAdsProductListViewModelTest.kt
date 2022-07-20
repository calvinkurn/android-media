package com.tokopedia.top_ads_headline.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.top_ads_headline.R
import com.tokopedia.top_ads_headline.data.Category
import com.tokopedia.top_ads_headline.data.GetRecommendedHeadlineProductsData
import com.tokopedia.top_ads_headline.data.Product
import com.tokopedia.top_ads_headline.data.TopAdsHeadlineTabModel
import com.tokopedia.top_ads_headline.usecase.GetRecommendedHeadlineProductsUseCase
import com.tokopedia.topads.common.data.response.Error
import com.tokopedia.topads.common.data.response.ResponseProductList
import com.tokopedia.topads.common.data.response.TopAdsProductModel
import com.tokopedia.topads.common.domain.usecase.TopAdsGetListProductUseCase
import io.mockk.*
import org.junit.Assert.*

import org.junit.After
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class TopAdsProductListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val topAdsGetListProductUseCase: TopAdsGetListProductUseCase = mockk(relaxed = true)
    private val getRecommendedHeadlineProductsUseCase: GetRecommendedHeadlineProductsUseCase =
        mockk(relaxed = true)
    private val viewModel = spyk(TopAdsProductListViewModel(topAdsGetListProductUseCase,
        getRecommendedHeadlineProductsUseCase))

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getTopAdsCategoryList list size should be 2`() {
        val list: ArrayList<TopAdsHeadlineTabModel> = viewModel.getTopAdsCategoryList()

        Assert.assertEquals(list.size, 2)
    }

    @Test
    fun `getTopAdsCategoryList 1st items check`() {
        val first = viewModel.getTopAdsCategoryList()[0]

        assertTrue(first.id == DEFAULT_RECOMMENDATION_TAB_ID && first.isSelected && first.name == R.string.topads_headline_recommendation_category)
    }

    @Test
    fun `getTopAdsCategoryList 2nd items check`() {
        val first = viewModel.getTopAdsCategoryList()[1]

        assertTrue(first.id == DEFAULT_ALL_PRODUCTS_TAB_ID && !first.isSelected && first.name == R.string.topads_headline_all_products_category)
    }

    @Test
    fun `getTopAdsProductList exception test`() {
        val err = "err"

        every { getRecommendedHeadlineProductsUseCase.setParams(any()) } throws Throwable(err)

        var actual = ""
        viewModel.getTopAdsProductList("", "", "", "", "", 0, 0, 0, "",
            { _, _ -> }, { actual = it.message.toString() })
        assertEquals(err, actual)
    }

    @Test
    fun `getTopAdsProductList getRecommendedHeadlineProductsUseCase called when tabid is 0`() {
        viewModel.getTopAdsProductList("11", "", "", "", "", 0, 0, 0, "", { _, _ -> }, {})

        coVerify { getRecommendedHeadlineProductsUseCase.setParams("11") }
        coVerify { getRecommendedHeadlineProductsUseCase.executeOnBackground() }
    }

    @Test
    fun `getTopAdsProductList error check when tabid is 0`() {
        val obj = mockk<GetRecommendedHeadlineProductsData>()

        every { obj.topadsGetRecommendedHeadlineProducts.errors } returns listOf(Error())
        coEvery { getRecommendedHeadlineProductsUseCase.executeOnBackground() } returns obj

        var error: Throwable? = null
        viewModel.getTopAdsProductList("11", "", "", "", "", 0, 0, 0, "",
            { _, _ -> }, { error = it })
        assertTrue(error != null)
    }

    @Test
    fun `getTopAdsProductList success should have list size double of products in reponse, when tabid is 0`() {
        val obj = mockk<GetRecommendedHeadlineProductsData>()

        every { obj.topadsGetRecommendedHeadlineProducts.recommendedProducts.products } returns
                listOf(Product(Category("", ""), "", "", "", 0, "", "", ""))
        every { obj.topadsGetRecommendedHeadlineProducts.errors } returns emptyList()
        coEvery { getRecommendedHeadlineProductsUseCase.executeOnBackground() } returns obj

        var actual: List<TopAdsProductModel>? = null
        viewModel.getTopAdsProductList("11", "", "", "", "", 0, 0, 0, "",
            { l, _ -> actual = l }, {})

        assertEquals(actual!!.size, 1)
    }


    @Test
    fun `getTopAdsProductList topAdsGetListProductUseCase when tabid is not 0`() {
        viewModel.getTopAdsProductList("1", "1", "1", "1", "1", 0, 0, 1, "1", { _, _ -> }, {})
        verify { topAdsGetListProductUseCase.setParams("1", "1", "1", "1", 0, 0, "1", "1") }
        coEvery { topAdsGetListProductUseCase.executeOnBackground() }
    }

    @Test
    fun `getTopAdsProductList success check when tabid is not 0`() {
        val obj = mockk<ResponseProductList.Result>()
        val expected = listOf(TopAdsProductModel())
        var actual: List<TopAdsProductModel>? = null

        every { obj.topadsGetListProduct.eof } returns true
        every { obj.topadsGetListProduct.data } returns expected
        coEvery { topAdsGetListProductUseCase.executeOnBackground() } returns obj

        viewModel.getTopAdsProductList("1", "1", "1", "1", "1", 0, 0, 1, "1",
            { l, _ -> actual = l}, {})

        assertEquals(expected, actual)
    }
}