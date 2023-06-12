package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.recommendation_widget_common.data.RecommendationEntity
import com.tokopedia.recommendation_widget_common.extension.mappingToRecommendationModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.product.emptystate.EmptyStateFilterDataView
import com.tokopedia.search.result.product.emptystate.EmptyStateKeywordDataView
import com.tokopedia.search.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.Test
import rx.Subscriber

private const val emptySearchProductModelJSON = "searchproduct/emptysearch/empty-search.json"

internal class SearchProductEmptySearchTest: ProductListPresenterTestFixtures() {

    private val keyword = "samsung"
    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val visitableList by lazy { visitableListSlot.captured }

    @Test
    fun `test empty search by keyword`() {
        `Given search product API will return empty product`()
        `Given recommendation API will return recommendation list`()
        `Given view has keyword`()
        `Given view will set visitable list`()

        `When load data`()

        `Then verify empty search product by keyword`()
        `Then verify empty search recommendation use case is not called`()
        `Then verify not showing quick filter`()
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

    private fun `Given view has keyword`() {
        every { productListView.queryKey } returns keyword
    }

    private fun `Given view will set visitable list`() {
        every { productListView.setProductList(capture(visitableListSlot)) } just runs
    }

    private fun `When load data`() {
        productListPresenter.loadData(mapOf(SearchApiConst.Q to keyword))
    }

    private fun `Then verify empty search product by keyword`() {
        val emptyStateDataView = visitableList.filterIsInstance<EmptyStateKeywordDataView>().first()

        emptyStateDataView.keyword shouldBe keyword
        emptyStateDataView.verticalSeparator.hasBottomSeparator shouldBe true
    }

    private fun `Then verify empty search recommendation use case is not called`() {
        verify(exactly = 0) {
            recommendationUseCase.execute(any(), any())
        }

        verify(exactly = 0) {
            getLocalSearchRecommendationUseCase.execute(any(), any())
        }
    }

    private fun `Then verify not showing quick filter`() {
        verify { productListView.hideQuickFilterShimmering() }
        verify(exactly = 0) { productListView.setQuickFilter(any()) }
    }

    @Test
    fun `Empty search by filter`() {
        `Given search product API will return empty product`()
        `Given recommendation API will return recommendation list`()
        `Given view has filter active`()
        `Given view will set visitable list`()

        `When load data`()

        `Then verify view init quick filter`()
        `Then verify empty search product by filter`()
    }

    private fun `Given view has filter active`() {
        every { productListView.isAnyFilterActive } returns true
    }

    private fun `Then verify view init quick filter`() {
        verify { productListView.setQuickFilter(any()) }
    }

    private fun `Then verify empty search product by filter`() {
        assertThat(visitableList.first(), instanceOf(EmptyStateFilterDataView::class.java))
    }
}
