package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import com.tokopedia.topads.sdk.domain.model.Data
import io.mockk.*
import org.junit.Before
import org.junit.Test
import rx.Subscriber

internal class SearchProductHandleVisitShop : ProductListPresenterTestFixtures() {

    private val searchProductModel = "searchproduct/with-topads.json".jsonToObject<SearchProductModel>()
    private val className = "SearchProductClassName"
    private val productCardOptionsModelSlot = slot<ProductCardOptionsModel>()
    private val productCardOptionModel by lazy { productCardOptionsModelSlot.captured }
    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val visitableList by lazy { visitableListSlot.captured }

    @Before
    fun setUpAddToCartTest() {
        `Given view already load data`()
    }

    private fun `Given view already load data`() {
        every { searchProductFirstPageUseCase.execute(any(), any()) } answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }

        every { productListView.setProductList(capture(visitableListSlot)) } just runs

        every { productListView.className } returns className

        productListPresenter.loadData(mapOf())
    }

    @Test
    fun `Handle visit shop action for top ads product`() {
        val topAdsData = searchProductModel.topAdsModel.data[0]
        val productItemViewModel = visitableList.findProductItemWith(topAdsData.product.id, true)

        `Given view click three dots`(productItemViewModel)

        `When handle visit shop`()

        `Then verify top ads url hitter for click shop`(topAdsData)
    }

    private fun List<Visitable<*>>.findProductItemWith(productId: String, isAds: Boolean): ProductItemViewModel {
        return find {
            it is ProductItemViewModel && it.productID == productId && it.isAds == isAds
        } as ProductItemViewModel
    }

    private fun `Given view click three dots`(productItemViewModel: ProductItemViewModel) {
        productListPresenter.onThreeDotsClick(productItemViewModel, 0)
    }

    private fun `When handle visit shop`() {
        productListPresenter.handleVisitShopAction()
    }

    private fun `Then verify top ads url hitter for click shop`(topAdsData: Data) {
        verify {
            topAdsUrlHitter.hitClickUrl(
                    className,
                    topAdsData.shopClickUrl,
                    topAdsData.product.id,
                    topAdsData.product.name,
                    topAdsData.product.image.s_ecs,
                    SearchConstant.TopAdsComponent.TOP_ADS
            )
        }
    }

    @Test
    fun `Handle visit shop action for organic product`() {
        val productItem = searchProductModel.searchProduct.data.productList[0]
        val productItemViewModel = visitableList.findProductItemWith(productItem.id, false)

        `Given view click three dots`(productItemViewModel)

        `When handle visit shop`()

        verify (exactly = 0) {
            topAdsUrlHitter.hitClickUrl(any<String>(), any(), any(), any(), any(), any())
        }
    }
}