package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.domain.model.SearchSameSessionRecommendationModel
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.product.samesessionrecommendation.SameSessionRecommendationDataView
import com.tokopedia.search.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert
import org.junit.Test
import rx.Subscriber

internal class SearchProductSameSessionRecommendationTest : ProductListPresenterTestFixtures() {
    private val searchProductLowIntentKeywordResponseJSON =
        "searchproduct/samesessionrecommendation/low-intention-response.json"
    private val sameSessionRecommendationResponseJSON =
        "searchproduct/samesessionrecommendation/same-session-recommendation.json"
    private val emptySameSessionRecommendationResponseJSON =
        "searchproduct/samesessionrecommendation/empty-same-session-recommendation.json"

    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val visitableList: List<Visitable<*>>
        get() = visitableListSlot.captured

    private val recommendationSlot = slot<SameSessionRecommendationDataView>()
    private val selectedVisitableSlot = slot<Visitable<*>>()

    @Test
    fun `Product click return empty recommendation`() {
        val lowIntentionKeywordResponse =
            searchProductLowIntentKeywordResponseJSON.jsonToObject<SearchProductModel>()
        val emptySameSessionRecommendation =
            emptySameSessionRecommendationResponseJSON.jsonToObject<SearchSameSessionRecommendationModel>()
        `Given view already load data`(lowIntentionKeywordResponse)
        `Given same search recommendationAPI will return SearchSameSessionRecommendationModel`(
            emptySameSessionRecommendation
        )
        `Given recyclerViewUpdater`()
        `Given same session recommendation preference will return empty`()
        `Given product filter indicator has default sorting and no active filter`()
        `Given queryKeyProvider queryKey return empty string`()

        val productItemDataViewIndex = visitableList.indexOfFirst { it is ProductItemDataView }
        val productItemDataView = visitableList[productItemDataViewIndex] as ProductItemDataView

        `When product item is clicked`(productItemDataView, productItemDataViewIndex)

        `Then verify same session recommendation API called once`()
        `Then verify no recommendationItem added`()
    }

    @Test
    fun `Product click return recommendation`() {
        val lowIntentionKeywordResponse =
            searchProductLowIntentKeywordResponseJSON.jsonToObject<SearchProductModel>()
        val sameSessionRecommendation =
            sameSessionRecommendationResponseJSON.jsonToObject<SearchSameSessionRecommendationModel>()
        `Given view already load data`(lowIntentionKeywordResponse)
        `Given same search recommendationAPI will return SearchSameSessionRecommendationModel`(
            sameSessionRecommendation
        )
        `Given recyclerViewUpdater`()
        `Given same session recommendation preference will return empty`()
        `Given product filter indicator has default sorting and no active filter`()
        `Given queryKeyProvider queryKey return empty string`()

        val productItemDataViewIndex = visitableList.indexOfFirst { it is ProductItemDataView }
        val productItemDataView = visitableList[productItemDataViewIndex] as ProductItemDataView

        `When product item is clicked`(productItemDataView, productItemDataViewIndex)

        `Then verify same session recommendation API called once`()
        `Then verify recommendationItem`(sameSessionRecommendation, productItemDataView)
    }

    @Test
    fun `Product click with filter active will not call recommendation use case`() {
        val lowIntentionKeywordResponse =
            searchProductLowIntentKeywordResponseJSON.jsonToObject<SearchProductModel>()
        val sameSessionRecommendation =
            sameSessionRecommendationResponseJSON.jsonToObject<SearchSameSessionRecommendationModel>()
        `Given view already load data`(lowIntentionKeywordResponse)
        `Given same search recommendationAPI will return SearchSameSessionRecommendationModel`(
            sameSessionRecommendation
        )
        `Given recyclerViewUpdater`()
        `Given same session recommendation preference will return empty`()
        `Given product filter indicator has active filter`()
        `Given queryKeyProvider queryKey return empty string`()

        val productItemDataViewIndex = visitableList.indexOfFirst { it is ProductItemDataView }
        val productItemDataView = visitableList[productItemDataViewIndex] as ProductItemDataView

        `When product item is clicked`(productItemDataView, productItemDataViewIndex)

        `Then verify same session recommendation API not called`()
        `Then verify no recommendationItem added`()
    }

    @Test
    fun `Product click with non-default sorting will not call recommendation use case`() {
        val lowIntentionKeywordResponse =
            searchProductLowIntentKeywordResponseJSON.jsonToObject<SearchProductModel>()
        val sameSessionRecommendation =
            sameSessionRecommendationResponseJSON.jsonToObject<SearchSameSessionRecommendationModel>()
        `Given view already load data`(lowIntentionKeywordResponse)
        `Given same search recommendationAPI will return SearchSameSessionRecommendationModel`(
            sameSessionRecommendation
        )
        `Given product filter indicator has non-default sorting`()
        `Given recyclerViewUpdater`()
        `Given same session recommendation preference will return empty`()
        `Given queryKeyProvider queryKey return empty string`()

        val productItemDataViewIndex = visitableList.indexOfFirst { it is ProductItemDataView }
        val productItemDataView = visitableList[productItemDataViewIndex] as ProductItemDataView

        `When product item is clicked`(productItemDataView, productItemDataViewIndex)

        `Then verify same session recommendation API not called`()
        `Then verify no recommendationItem added`()
    }

    @Test
    fun `Product click with hide recommendation active will not call recommendation use case`() {
        val lowIntentionKeywordResponse =
            searchProductLowIntentKeywordResponseJSON.jsonToObject<SearchProductModel>()
        val sameSessionRecommendation =
            sameSessionRecommendationResponseJSON.jsonToObject<SearchSameSessionRecommendationModel>()
        `Given view already load data`(lowIntentionKeywordResponse)
        `Given same search recommendationAPI will return SearchSameSessionRecommendationModel`(
            sameSessionRecommendation
        )
        `Given recyclerViewUpdater`()
        `Given same session recommendation preference will return non-empty`()
        `Given product filter indicator has default sorting and no active filter`()
        `Given queryKeyProvider queryKey return empty string`()

        val productItemDataViewIndex = visitableList.indexOfFirst { it is ProductItemDataView }
        val productItemDataView = visitableList[productItemDataViewIndex] as ProductItemDataView

        `When product item is clicked`(productItemDataView, productItemDataViewIndex)

        `Then verify same session recommendation API not called`()
        `Then verify no recommendationItem added`()
    }

    private fun `Given view already load data`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }

        every { productListView.setProductList(capture(visitableListSlot)) } just runs

        productListPresenter.loadData(mapOf())
    }

    private fun `Given same search recommendationAPI will return SearchSameSessionRecommendationModel`(
        searchSameSessionRecommendationModel: SearchSameSessionRecommendationModel
    ) {
        every { sameSessionRecommendationUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchSameSessionRecommendationModel>>().complete(
                searchSameSessionRecommendationModel
            )
        }
    }

    private fun `Given same session recommendation preference will return non-empty`() {
        every { sameSessionRecommendationPreference.hideRecommendationTimestamp } returns System.currentTimeMillis() - 10_000L
    }

    private fun `Given same session recommendation preference will return empty`() {
        every { sameSessionRecommendationPreference.hideRecommendationTimestamp } returns 0L
    }

    private fun `Given product filter indicator has active filter`() {
        every { productFilterIndicator.isAnyFilterOrSortActive } returns true
    }

    private fun `Given product filter indicator has non-default sorting`() {
        every {
            productFilterIndicator.isAnyFilterOrSortActive
        } returns true
    }

    private fun `Given product filter indicator has default sorting and no active filter`() {
        every { productFilterIndicator.isAnyFilterOrSortActive } returns false
    }

    private fun `Given recyclerViewUpdater`() {
        `Given viewUpdater itemCount return visitableList size`()
        `Given recyclerViewUpdater addSameSessionRecommendation`()
    }

    private fun `Given viewUpdater itemCount return visitableList size`() {
        every { viewUpdater.itemCount } returns visitableList.size
    }

    private fun `Given recyclerViewUpdater addSameSessionRecommendation`() {
        every {
            viewUpdater.insertItemAfter(
                capture(recommendationSlot),
                capture(selectedVisitableSlot)
            )
        } just runs
    }

    private fun `Given queryKeyProvider queryKey return empty string`() {
        every { queryKeyProvider.queryKey } returns ""
    }

    private fun `When product item is clicked`(
        productItemDataView: ProductItemDataView,
        adapterPosition: Int,
    ) {
        productListPresenter.onProductClick(productItemDataView, adapterPosition)
    }

    private fun `Then verify recommendationItem`(
        expectedRecommendation: SearchSameSessionRecommendationModel,
        expectedSelectedProduct: Visitable<*>,
    ) {
        verify {
            viewUpdater.removeFirstItemWithCondition(any())
            viewUpdater.insertItemAfter(
                recommendationSlot.captured,
                selectedVisitableSlot.captured
            )
        }
        Assert.assertEquals(expectedSelectedProduct, selectedVisitableSlot.captured)
        expectedRecommendation.assertRecommendation(recommendationSlot.captured)
    }

    private fun SearchSameSessionRecommendationModel.assertRecommendation(
        recommendation: SameSessionRecommendationDataView,
    ) {
        title shouldBe recommendation.title
        componentId shouldBe recommendation.componentId
        trackingOption.toIntOrZero() shouldBe recommendation.trackingOption
        feedback.assertFeedback(recommendation.feedback)
    }

    private fun SearchSameSessionRecommendationModel.Feedback.assertFeedback(
        feedback: SameSessionRecommendationDataView.Feedback
    ) {
        title shouldBe feedback.title
        componentId shouldBe feedback.componentId
        trackingOption.toIntOrZero() shouldBe feedback.trackingOption
        data.size shouldBe feedback.items.size
        data.forEachIndexed { index, data ->
            data.assertFeedbackItem(feedback.items[index], trackingOption)
        }
    }

    private fun SearchSameSessionRecommendationModel.Feedback.Data.assertFeedbackItem(
        feedbackItem: SameSessionRecommendationDataView.Feedback.FeedbackItem,
        trackingOption: String,
    ) {
        name shouldBe feedbackItem.name
        applink shouldBe feedbackItem.applink
        url shouldBe feedbackItem.url
        imageUrl shouldBe feedbackItem.imageUrl
        componentId shouldBe feedbackItem.componentId
        titleOnClick shouldBe feedbackItem.titleOnClick
        messageOnClick shouldBe feedbackItem.messageOnClick
        trackingOption.toIntOrZero() shouldBe feedbackItem.trackingOption
    }

    private fun `Then verify no recommendationItem added`() {
        verify(exactly = 0) {
            viewUpdater.removeFirstItemWithCondition(any())
            viewUpdater.insertItemAfter(any(), any())
        }
    }

    private fun `Then verify same session recommendation API not called`() {
        verify(exactly = 0) {
            sameSessionRecommendationUseCase.execute(any(), any())
        }
    }

    private fun `Then verify same session recommendation API called once`() {
        verify(exactly = 1) {
            sameSessionRecommendationUseCase.execute(any(), any())
        }
    }

}
