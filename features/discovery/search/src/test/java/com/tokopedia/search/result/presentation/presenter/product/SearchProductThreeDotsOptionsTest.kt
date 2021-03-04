package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchConstant.ABTestRemoteConfigKey.*
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.discovery.common.model.ProductCardOptionsModel.AddToCartParams
import com.tokopedia.search.analytics.SearchEventTracking
import com.tokopedia.search.analytics.SearchTracking
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import com.tokopedia.search.shouldBe
import io.mockk.*
import org.junit.Test
import rx.Subscriber

internal class SearchProductThreeDotsOptionsTest : ProductListPresenterTestFixtures() {

    private val query = "samsung"
    private val productCardOptionsModelSlot = slot<ProductCardOptionsModel>()
    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val visitableList by lazy { visitableListSlot.captured }

    @Test
    fun `Click three dots option for organic product`() {
        `Given AB Test will return full options variant`()
        setUp()
        `Given view getQueryKey will return keyword`()
        `Given search product API will success`()
        `Given view already load data`()

        val indexedProductItem = visitableList.getProductItemWithIndex(false)
        val productItemViewModel = indexedProductItem.value as ProductItemViewModel
        val position = indexedProductItem.index

        `When click three dots`(productItemViewModel, position)

        `Then verify click three dots option`(productItemViewModel)
        `Then verify product card options model`(productItemViewModel)
    }

    private fun `Given AB Test will return full options variant`() {
        every {
            productListView.abTestRemoteConfig.getString(AB_TEST_KEY_THREE_DOTS_SEARCH)
        } returns AB_TEST_THREE_DOTS_SEARCH_FULL_OPTIONS
    }

    private fun `Given view getQueryKey will return keyword`() {
        every { productListView.queryKey } returns query
    }

    private fun `Given search product API will success`() {
        every { searchProductFirstPageUseCase.execute(any(), any()) } answers {
            secondArg<Subscriber<SearchProductModel>>().complete("searchproduct/with-topads.json".jsonToObject<SearchProductModel>())
        }
    }

    private fun `Given view already load data`() {
        every { productListView.setProductList(capture(visitableListSlot)) } just runs

        productListPresenter.loadData(mapOf())
    }

    private fun `When click three dots`(productItemViewModel: ProductItemViewModel, position: Int) {
        productListPresenter.onThreeDotsClick(productItemViewModel, position)
    }

    private fun <T> List<T>.getProductItemWithIndex(isAds: Boolean): IndexedValue<T> {
        return withIndex().find {
            it.value is ProductItemViewModel && ((it.value as ProductItemViewModel).isAds == isAds)
        }!!
    }

    private fun `Then verify click three dots option`(productItemViewModel: ProductItemViewModel) {
        verify {
            productListView.trackEventLongPress(productItemViewModel.productID)
            productListView.showProductCardOptions(capture(productCardOptionsModelSlot))
        }
    }

    private fun `Then verify product card options model`(
            productItemViewModel: ProductItemViewModel
    ) {
        val productCardOptionsModel = productCardOptionsModelSlot.captured

        productCardOptionsModel.hasWishlist shouldBe true
        productCardOptionsModel.hasSimilarSearch shouldBe true
        productCardOptionsModel.isWishlisted shouldBe productItemViewModel.isWishlisted
        productCardOptionsModel.keyword shouldBe query
        productCardOptionsModel.productId shouldBe productItemViewModel.productID
        productCardOptionsModel.isTopAds shouldBe productItemViewModel.isAds
        productCardOptionsModel.topAdsWishlistUrl shouldBe productItemViewModel.topadsWishlistUrl
        productCardOptionsModel.isRecommendation shouldBe false
        productCardOptionsModel.screenName shouldBe SearchEventTracking.Category.SEARCH_RESULT
        productCardOptionsModel.seeSimilarProductEvent shouldBe SearchTracking.EVENT_CLICK_SEARCH_RESULT
        productCardOptionsModel.hasAddToCart shouldBe true
        productCardOptionsModel.addToCartParams shouldBe AddToCartParams(productItemViewModel.minOrder)
        productCardOptionsModel.categoryName shouldBe productItemViewModel.categoryString
        productCardOptionsModel.productName shouldBe productItemViewModel.productName
        productCardOptionsModel.formattedPrice shouldBe productItemViewModel.price
        productCardOptionsModel.shop shouldBe ProductCardOptionsModel.Shop(
                productItemViewModel.shopID,
                productItemViewModel.shopName,
                productItemViewModel.shopUrl
        )
        productCardOptionsModel.hasVisitShop shouldBe true
        productCardOptionsModel.hasShareProduct shouldBe true
        productCardOptionsModel.productImageUrl shouldBe productItemViewModel.imageUrl
        productCardOptionsModel.productUrl shouldBe productItemViewModel.productUrl
    }

    @Test
    fun `Click three dots option for ads product`() {
        `Given AB Test will return full options variant`()
        setUp()
        `Given view getQueryKey will return keyword`()
        `Given search product API will success`()
        `Given view already load data`()

        val indexedProductItem = visitableList.getProductItemWithIndex(true)
        val productItemViewModel = indexedProductItem.value as ProductItemViewModel
        val position = indexedProductItem.index

        `When click three dots`(productItemViewModel, position)

        `Then verify click three dots option`(productItemViewModel)
        `Then verify product card options model`(productItemViewModel)
    }

    @Test
    fun `Click three dots options for control variant`() {
        `Given AB Test will return control variant`()
        setUp()
        `Given search product API will success`()
        `Given view already load data`()

        val indexedProductItem = visitableList.getProductItemWithIndex(false)
        val productItemViewModel = indexedProductItem.value as ProductItemViewModel
        val position = indexedProductItem.index
        `When click three dots`(productItemViewModel, position)

        `Then verify click three dots option`(productItemViewModel)

        `Then verify product card options does not have full options `()
    }

    private fun `Given AB Test will return control variant`() {
        every {
            productListView.abTestRemoteConfig.getString(AB_TEST_KEY_THREE_DOTS_SEARCH)
        } returns ""
    }

    private fun `Then verify product card options does not have full options `() {
        val productCardOptionsModel = productCardOptionsModelSlot.captured

        productCardOptionsModel.hasWishlist shouldBe true
        productCardOptionsModel.hasSimilarSearch shouldBe true
        productCardOptionsModel.hasAddToCart shouldBe false
        productCardOptionsModel.hasShareProduct shouldBe false
        productCardOptionsModel.hasVisitShop shouldBe false
    }
}