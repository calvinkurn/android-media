package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.domain.model.SearchSameSessionRecommendationModel
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.product.samesessionrecommendation.SameSessionRecommendationDataView
import com.tokopedia.search.shouldBe
import com.tokopedia.usecase.RequestParams
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
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
    private val requestParamsSlot = slot<RequestParams>()
    private val selectedVisitableIndexSlot = slot<Int>()

    private val warehouseId = "2216"
    private val dummyChooseAddressData = LocalCacheModel(
        address_id = "123",
        city_id = "45",
        district_id = "123",
        lat = "10.2131",
        long = "12.01324",
        postal_code = "12345",
        warehouse_id = warehouseId,
    )

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

        `Given viewUpdater getItemAtPosition`(productItemDataViewIndex + 1)

        `When product item is clicked`(productItemDataView, productItemDataViewIndex)

        `Then verify same session recommendation API called once`()
        `Then assert same session request params`(productItemDataView, LocalCacheModel())
        `Then verify no recommendationItem added`()
    }

    @Test
    fun `Odd Position Product click return recommendation`() {
        `Setup choose address data`(dummyChooseAddressData)
        setUp()

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
        val nextProductItemDataView = visitableList[productItemDataViewIndex + 1] as ProductItemDataView

        `Given viewUpdater getItemAtPosition`(productItemDataViewIndex + 1)

        `When product item is clicked`(productItemDataView, productItemDataViewIndex)

        `Then verify same session recommendation API called once`()
        `Then assert same session request params`(productItemDataView, dummyChooseAddressData)
        `Then verify recommendationItem`(sameSessionRecommendation, nextProductItemDataView, productItemDataViewIndex)
    }

    @Test
    fun `Even Position Product click return recommendation`() {
        `Setup choose address data`(dummyChooseAddressData)
        setUp()

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

        val productItemDataViewIndex = visitableList.indexOfFirst { it is ProductItemDataView } + 1
        val productItemDataView = visitableList[productItemDataViewIndex] as ProductItemDataView

        `Given viewUpdater getItemAtPosition`(productItemDataViewIndex + 1)

        `When product item is clicked`(productItemDataView, productItemDataViewIndex)

        `Then verify same session recommendation API called once`()
        `Then assert same session request params`(productItemDataView, dummyChooseAddressData)
        `Then verify recommendationItem`(sameSessionRecommendation, productItemDataView, productItemDataViewIndex)
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
        every {
            sameSessionRecommendationUseCase.execute(
                capture(requestParamsSlot),
                any()
            )
        }.answers {
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

    private fun `Setup choose address data`(localCacheModel: LocalCacheModel) {
        every { chooseAddressView.chooseAddressData } returns localCacheModel
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
        `Given viewUpdater itemList return visitableList`()
        `Given viewUpdater itemCount return visitableList size`()
        `Given recyclerViewUpdater addSameSessionRecommendation`()
        `Given viewUpdater scrollToPosition`()
    }

    private fun `Given viewUpdater itemList return visitableList`() {
        every { viewUpdater.itemList } returns visitableList
    }

    private fun `Given viewUpdater getItemAtPosition`(position: Int) {
        every { viewUpdater.getItemAtIndex(position) } returns visitableList[position]
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

    private fun `Given viewUpdater scrollToPosition`() {
        every {
            viewUpdater.scrollToPosition(capture(selectedVisitableIndexSlot))
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
        expectedPreviousProduct: Visitable<*>,
        expectedProductPosition: Int,
    ) {
        verify {
            viewUpdater.removeFirstItemWithCondition(any())
            viewUpdater.insertItemAfter(
                recommendationSlot.captured,
                selectedVisitableSlot.captured
            )
            viewUpdater.scrollToPosition(selectedVisitableIndexSlot.captured)
        }
        expectedProductPosition shouldBe selectedVisitableIndexSlot.captured
        expectedPreviousProduct shouldBe selectedVisitableSlot.captured
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

    private fun `Then assert same session request params`(
        productItemDataView: ProductItemDataView,
        localCacheModel: LocalCacheModel,
    ) {
        val requestParams = requestParamsSlot.captured
        requestParams.assertRequestProductId(productItemDataView)
        requestParams.assertRequestLocationData(localCacheModel)
    }

    private fun RequestParams.assertRequestProductId(
        productItemDataView: ProductItemDataView,
    ) {
        getString(SearchApiConst.PRODUCT_ID, "") shouldBe productItemDataView.productID
    }

    private fun RequestParams.assertRequestLocationData(
        localCacheModel: LocalCacheModel,
    ) {
        getString(SearchApiConst.USER_CITY_ID, "") shouldBe localCacheModel.city_id
        getString(SearchApiConst.USER_ADDRESS_ID, "") shouldBe localCacheModel.address_id
        getString(SearchApiConst.USER_DISTRICT_ID, "") shouldBe localCacheModel.district_id
        getString(SearchApiConst.USER_POST_CODE, "") shouldBe localCacheModel.postal_code
        getString(SearchApiConst.USER_LAT, "") shouldBe localCacheModel.lat
        getString(SearchApiConst.USER_LONG, "") shouldBe localCacheModel.long
        getString(SearchApiConst.USER_WAREHOUSE_ID, "") shouldBe localCacheModel.warehouse_id
    }

}
