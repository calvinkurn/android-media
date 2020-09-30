package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import io.mockk.*
import org.junit.Test
import rx.Subscriber

internal class SearchProductThreeDotsOptionsTest : ProductListPresenterTestFixtures() {

    @Test
    fun `Click three dots option for organic product`() {
        every { searchProductFirstPageUseCase.execute(any(), any()) } answers {
            secondArg<Subscriber<SearchProductModel>>().complete("searchproduct/with-topads.json".jsonToObject<SearchProductModel>())
        }

        val visitableListSlot = slot<List<Visitable<*>>>()
        every { productListView.setProductList(capture(visitableListSlot)) } just runs

        productListPresenter.loadData(mapOf())

        val visitableList = visitableListSlot.captured
        val indexedProductItem = visitableList.withIndex().find { it.value is ProductItemViewModel && !(it.value as ProductItemViewModel).isAds }!!
        val productItemViewModel = indexedProductItem.value as ProductItemViewModel
        val position = indexedProductItem.index

        productListPresenter.onThreeDotsClick(productItemViewModel, position)

        val productCardOptionsModelSlot = slot<ProductCardOptionsModel>()

        verify {
            productListView.trackEventLongPress(productItemViewModel.productID)
            productListView.showProductCardOptions(capture(productCardOptionsModelSlot))
        }

        val productCardOptionsModel = productCardOptionsModelSlot.captured
    }
}