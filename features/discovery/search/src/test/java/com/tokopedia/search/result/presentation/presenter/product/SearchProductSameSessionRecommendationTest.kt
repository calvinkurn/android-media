package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.domain.model.SearchSameSessionRecommendationModel
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.view.adapter.ProductListAdapter
import com.tokopedia.search.result.presentation.view.fragment.RecyclerViewUpdater
import com.tokopedia.search.result.product.QueryKeyProvider
import com.tokopedia.search.result.product.requestparamgenerator.RequestParamsGenerator
import com.tokopedia.search.result.product.samesessionrecommendation.SameSessionRecommendationConst.IRRELEVANT_RECOMMENDATION_ID
import com.tokopedia.search.result.product.samesessionrecommendation.SameSessionRecommendationDataView
import com.tokopedia.search.result.product.samesessionrecommendation.SameSessionRecommendationPreference
import com.tokopedia.search.result.product.samesessionrecommendation.SameSessionRecommendationPresenterDelegate
import com.tokopedia.search.shouldBe
import com.tokopedia.usecase.UseCase
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import rx.Subscriber

internal class SearchProductSameSessionRecommendationTest : ProductListPresenterTestFixtures() {
    private val searchProductLowIntentKeywordResponseJSON =
        "searchproduct/samesessionrecommendation/low-intention-response.json"
    private val sameSessionRecommendationResponseJSON =
        "searchproduct/samesessionrecommendation/same-session-recommendation.json"
    private val emptySameSessionRecommendationResponseJSON =
        "searchproduct/samesessionrecommendation/empty-same-session-recommendation.json"

    private val recyclerViewUpdater = mockk<RecyclerViewUpdater>(relaxed = true)
    private val sameSessionRecommendationUseCase =
        mockk<UseCase<SearchSameSessionRecommendationModel>>(relaxed = true)
    private val filterController = mockk<FilterController>(relaxed = true)
    private val sameSessionRecommendationPreference =
        mockk<SameSessionRecommendationPreference>(relaxed = true)
    private val queryKeyProvider = mockk<QueryKeyProvider>(relaxed = true)
    private val productListAdapter = mockk<ProductListAdapter>(relaxed = true)

    override lateinit var sameSessionRecommendationPresenterDelegate: SameSessionRecommendationPresenterDelegate

    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val visitableList: List<Visitable<*>>
        get() = visitableListSlot.captured

    private val recommendationSlot = slot<SameSessionRecommendationDataView>()
    private val targetPositionSlot = slot<Int>()

    @Before
    override fun setUp() {
        val requestParamsGenerator = RequestParamsGenerator(userSession, pagination)
        sameSessionRecommendationPresenterDelegate = SameSessionRecommendationPresenterDelegate(
            recyclerViewUpdater,
            requestParamsGenerator,
            sameSessionRecommendationUseCase,
            filterController,
            sameSessionRecommendationPreference,
            queryKeyProvider,
        )
        super.setUp()
    }

    @Test
    fun `Product click return empty recommendation`() {
        val lowIntentionKeywordResponse =
            searchProductLowIntentKeywordResponseJSON.jsonToObject<SearchProductModel>()
        val emptySameSessionRecommendation =
            emptySameSessionRecommendationResponseJSON.jsonToObject<SearchSameSessionRecommendationModel>()
        `Given view already load data`(lowIntentionKeywordResponse)
        `Given same search recommendationAPI will return SearchSameSessionRecommendationModel`(emptySameSessionRecommendation)
        `Given recyclerViewUpdater`()
        `Given same session recommendation preference will return empty`()
        `Given filter controller has no active filter`()
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
        `Given same search recommendationAPI will return SearchSameSessionRecommendationModel`(sameSessionRecommendation)
        `Given recyclerViewUpdater`()
        `Given same session recommendation preference will return empty`()
        `Given filter controller has no active filter`()
        `Given queryKeyProvider queryKey return empty string`()

        val productItemDataViewIndex = visitableList.indexOfFirst { it is ProductItemDataView }
        val productItemDataView = visitableList[productItemDataViewIndex] as ProductItemDataView
        val targetPosition = productItemDataViewIndex + 1

        `When product item is clicked`(productItemDataView, productItemDataViewIndex)

        `Then verify same session recommendation API called once`()
        `Then verify recommendationItem`(sameSessionRecommendation, targetPosition)
    }

    @Test
    fun `Product click with filter active will not call recommendation use case`() {
        val lowIntentionKeywordResponse =
            searchProductLowIntentKeywordResponseJSON.jsonToObject<SearchProductModel>()
        val sameSessionRecommendation =
            sameSessionRecommendationResponseJSON.jsonToObject<SearchSameSessionRecommendationModel>()
        `Given view already load data`(lowIntentionKeywordResponse)
        `Given same search recommendationAPI will return SearchSameSessionRecommendationModel`(sameSessionRecommendation)
        `Given recyclerViewUpdater`()
        `Given same session recommendation preference will return empty`()
        `Given filter controller has active filter`()
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
        `Given same search recommendationAPI will return SearchSameSessionRecommendationModel`(sameSessionRecommendation)
        `Given recyclerViewUpdater`()
        `Given same session recommendation preference will return non-empty`()
        `Given filter controller has no active filter`()
        `Given queryKeyProvider queryKey return empty string`()

        val productItemDataViewIndex = visitableList.indexOfFirst { it is ProductItemDataView }
        val productItemDataView = visitableList[productItemDataViewIndex] as ProductItemDataView

        `When product item is clicked`(productItemDataView, productItemDataViewIndex)

        `Then verify same session recommendation API not called`()
        `Then verify no recommendationItem added`()
    }

    @Test
    fun `Product click after irrelevant recommendation selected will not call recommendation use case again`() {
        val lowIntentionKeywordResponse =
            searchProductLowIntentKeywordResponseJSON.jsonToObject<SearchProductModel>()
        val sameSessionRecommendation =
            sameSessionRecommendationResponseJSON.jsonToObject<SearchSameSessionRecommendationModel>()
        `Given view already load data`(lowIntentionKeywordResponse)
        `Given same search recommendationAPI will return SearchSameSessionRecommendationModel`(sameSessionRecommendation)
        `Given recyclerViewUpdater`()
        `Given same session recommendation preference will return empty`()
        `Given filter controller has no active filter`()
        `Given queryKeyProvider queryKey return empty string`()

        val productItemDataViewIndex = visitableList.indexOfFirst { it is ProductItemDataView }
        val productItemDataView = visitableList[productItemDataViewIndex] as ProductItemDataView
        val targetPosition = productItemDataViewIndex + 1

        `When product item is clicked`(productItemDataView, productItemDataViewIndex)

        `Then verify same session recommendation API called once`()
        `Then verify recommendationItem`(sameSessionRecommendation, targetPosition)

        `When irrelevant product is clicked`(recommendationSlot.captured)
        `Then verify same session recommendation API called once`()
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

    private fun `Given filter controller has active filter`() {
        every { filterController.isFilterActive() } returns true
    }

    private fun `Given filter controller has no active filter`() {
        every { filterController.isFilterActive() } returns false
    }

    private fun `Given recyclerViewUpdater`() {
        `Given recyclerViewUpdater productListAdapter`()
        `Given productListAdapter return item count`()
        `Given productListAdapter itemList return visitableList`()
        `Given recyclerViewUpdater addSameSessionRecommendation`()
    }

    private fun `Given recyclerViewUpdater productListAdapter`() {
        every { recyclerViewUpdater.productListAdapter } returns productListAdapter
    }

    private fun `Given productListAdapter return item count`() {
        every { productListAdapter.itemCount } returns visitableList.size
    }

    private fun `Given productListAdapter itemList return visitableList`() {
        every { productListAdapter.itemList } returns visitableList.toMutableList()
    }

    private fun `Given recyclerViewUpdater addSameSessionRecommendation`() {
        every {
            recyclerViewUpdater.insertItemAtIndex(capture(recommendationSlot), capture(targetPositionSlot))
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

    private fun `When irrelevant product is clicked`(recommendation: SameSessionRecommendationDataView) {
        val irrelevantRecommendationFeedback = recommendation.feedback.items.first { IRRELEVANT_RECOMMENDATION_ID == it.componentId }
        sameSessionRecommendationPresenterDelegate.handleFeedbackItemClick(irrelevantRecommendationFeedback)
    }

    private fun `Then verify recommendationItem`(
        expectedRecommendation: SearchSameSessionRecommendationModel,
        expectedTargetPosition: Int,
    ) {
        verify {
            productListAdapter.removeLastSameSessionRecommendation()
            recyclerViewUpdater.insertItemAtIndex(recommendationSlot.captured, targetPositionSlot.captured)
        }
        Assert.assertEquals(expectedTargetPosition, targetPositionSlot.captured)
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
            productListAdapter.removeLastSameSessionRecommendation()
            recyclerViewUpdater.insertItemAtIndex(any(), any())
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
