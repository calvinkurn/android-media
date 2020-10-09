package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.EmptySearchProductViewModel
import com.tokopedia.search.shouldBe
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import rx.Subscriber

private val emptySearchModel = "searchproduct/emptysearch/empty-search.json"

internal class SearchProductEmptySearchTest: ProductListPresenterTestFixtures() {

    @Test
    fun `test empty search by keyword`() {
        `Given search product API will return empty product`()

        `When load data`()

        `Then verify empty search product model`(false)
        `Then verify empty search recommendation use case is called`()
    }

    private fun `Given search product API will return empty product`() {
        val searchProductModel = emptySearchModel.jsonToObject<SearchProductModel>()
        every {
            searchProductFirstPageUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `When load data`() {
        productListPresenter.loadData(mapOf())
    }

    private fun `Then verify empty search product model`(expectedIsFilterActive: Boolean) {
        val emptySearchViewModelSlot = slot<EmptySearchProductViewModel>()

        verify {
            productListView.setEmptyProduct(null, capture(emptySearchViewModelSlot))
        }

        val emptySearchViewModel = emptySearchViewModelSlot.captured
        emptySearchViewModel.isBannerAdsAllowed shouldBe true
        emptySearchViewModel.isFilterActive shouldBe expectedIsFilterActive
    }

    private fun `Then verify empty search recommendation use case is called`() {
        verify {
            recommendationUseCase.execute(any(), any())
        }

        verify(exactly = 0) {
            getLocalSearchRecommendationUseCase.execute(any(), any())
        }
    }

    @Test
    fun `test empty search by filter`() {
        `Given search product API will return empty product`()
        `Given view has filter active`()

        `When load data`()

        `Then verify empty search product model`(true)
        `Then verify empty search recommendation use case is called`()
    }

    private fun `Given view has filter active`() {
        every { productListView.isAnyFilterActive } returns true
    }
}