package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.recommendation_widget_common.data.RecommendationEntity
import com.tokopedia.recommendation_widget_common.extension.mappingToRecommendationModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.EmptySearchProductDataView
import com.tokopedia.search.shouldBe
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import rx.Subscriber

private const val emptySearchProductModelJSON = "searchproduct/emptysearch/empty-search.json"

internal class SearchProductEmptySearchTest: ProductListPresenterTestFixtures() {

    @Test
    fun `test empty search by keyword`() {
        `Given search product API will return empty product`()
        `Given recommendation API will return recommendation list`()

        `When load data`()

        `Then verify empty search product model`(false)
        `Then verify empty search recommendation use case is called`()
    }

    private fun `Given search product API will return empty product`() {
        val searchProductModel = emptySearchProductModelJSON.jsonToObject<SearchProductModel>()
        every {
            searchProductFirstPageUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `Given recommendation API will return recommendation list`() {
        every { recommendationUseCase.execute(any(), any()) } answers {
            val recommendationEntity = "searchproduct/emptysearchrecommendation/empty-search-recommendation.json".jsonToObject<RecommendationEntity>()
            val recommendationDataList = recommendationEntity.productRecommendationWidget?.data
                    ?: listOf()

            val recommendationWidgetList = recommendationDataList.mappingToRecommendationModel()

            secondArg<Subscriber<List<RecommendationWidget>>>().complete(recommendationWidgetList)
        }
    }

    private fun `When load data`() {
        productListPresenter.loadData(mapOf())
    }

    private fun `Then verify empty search product model`(expectedIsFilterActive: Boolean) {
        val emptySearchViewModelSlot = slot<EmptySearchProductDataView>()

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
        `Given recommendation API will return recommendation list`()
        `Given view has filter active`()

        `When load data`()

        `Then verify empty search product model`(true)
        `Then verify empty search recommendation use case is called`()
    }

    private fun `Given view has filter active`() {
        every { productListView.isAnyFilterActive } returns true
    }
}