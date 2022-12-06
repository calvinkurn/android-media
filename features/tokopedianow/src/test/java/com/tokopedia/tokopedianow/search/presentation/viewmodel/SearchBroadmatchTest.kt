package com.tokopedia.tokopedianow.search.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.tokopedianow.search.domain.mapper.SearchBroadMatchMapper
import com.tokopedia.tokopedianow.search.domain.model.SearchModel
import com.tokopedia.tokopedianow.search.presentation.model.BroadMatchDataView
import com.tokopedia.tokopedianow.search.presentation.model.SuggestionDataView
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.util.SearchCategoryDummyUtils
import io.mockk.every
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as shouldBe

class SearchBroadmatchTest: SearchTestFixtures() {

    @Test
    fun `show broad match no result`() {
        val searchModel = "search/broadmatch/broadmatch-no-result.json".jsonToObject<SearchModel>()
        `Given get search first page use case will be successful`(searchModel)

        `When view created`()

        `Then assert broadmatch for no result`(searchModel)
    }

    private fun `Then assert broadmatch for no result`(searchModel: SearchModel) {
        val visitableList = tokoNowSearchViewModel.visitableListLiveData.value!!

        val expectedSuggestionIndex = 1
        assertBroadMatchInVisitableList(visitableList, expectedSuggestionIndex, searchModel)
    }

    private fun assertBroadMatchInVisitableList(
        visitableList: List<Visitable<*>>,
        expectedSuggestionIndex: Int,
        searchModel: SearchModel,
    ) {
        visitableList[expectedSuggestionIndex].assertSuggestionDataView(searchModel.getSuggestion())

        val expectedBroadMatchIndex = expectedSuggestionIndex + 1
        val broadMatchList = searchModel.getRelated().otherRelatedList
        broadMatchList.forEachIndexed { index, otherRelated ->
            visitableList[expectedBroadMatchIndex + index].assertBroadMatchViewModel(otherRelated)
        }
    }

    private fun Visitable<*>.assertBroadMatchViewModel(
        otherRelated: AceSearchProductModel.OtherRelated
    ) {
        assertThat(this, instanceOf(BroadMatchDataView::class.java))

        this as BroadMatchDataView

        val actualBroadMatch = SearchBroadMatchMapper.createBroadMatchDataView(
            otherRelated, cartService
        )

        assertEquals(this, actualBroadMatch)
    }

    @Test
    fun `show broad match low result`() {
        val searchModel = "search/broadmatch/broadmatch-low-result.json".jsonToObject<SearchModel>()
        `Given get search first page use case will be successful`(searchModel)

        `When view created`()

        `Then assert broadmatch for low result`(searchModel)
    }

    private fun `Then assert broadmatch for low result`(searchModel: SearchModel) {
        val visitableList = tokoNowSearchViewModel.visitableListLiveData.value!!
        val lastProductIndex = visitableList.indexOfLast { it is ProductItemDataView }

        val expectedSuggestionIndex = lastProductIndex + 1
        assertBroadMatchInVisitableList(visitableList, expectedSuggestionIndex, searchModel)
    }

    @Test
    fun `should not show broad match without correct response code`() {
        val searchModel = "search/broadmatch/broadmatch-invalid-response-code.json".jsonToObject<SearchModel>()
        `Given get search first page use case will be successful`(searchModel)

        `When view created`()

        `Then assert visitable list does not contain broad match`()
    }

    private fun `Then assert visitable list does not contain broad match`() {
        val visitableList = tokoNowSearchViewModel.visitableListLiveData.value!!

        assertThat(visitableList.any { it is SuggestionDataView }, shouldBe(false))
        assertThat(visitableList.any { it is BroadMatchDataView }, shouldBe(false))
    }

    @Test
    fun `onViewResumed should update broad match quantity from mini cart`() {
        val searchModel = "search/broadmatch/broadmatch-low-result.json".jsonToObject<SearchModel>()
        val miniCartSimplifiedData = SearchCategoryDummyUtils.miniCartSimplifiedData

        `Given get search first page use case will be successful`(searchModel)
        `Given view already created`()
        `Given get mini cart simplified use case will be successful`(miniCartSimplifiedData)

        `When view is resumed`()

        `Then assert broadmatch item quantity is updated`(miniCartSimplifiedData)
    }

    private fun `Given get mini cart simplified use case will be successful`(
        miniCartSimplifiedData: MiniCartSimplifiedData
    ) {
        every {
            getMiniCartListSimplifiedUseCase.execute(any(), any())
        } answers {
            firstArg<(MiniCartSimplifiedData) -> Unit>().invoke(miniCartSimplifiedData)
        }
    }

    private fun `When view is resumed`() {
        tokoNowSearchViewModel.onViewResumed()
    }

    private fun `Then assert broadmatch item quantity is updated`(
        miniCartSimplifiedData: MiniCartSimplifiedData
    ) {
        val miniCartItems = miniCartSimplifiedData.miniCartItems
        val visitableList = tokoNowSearchViewModel.visitableListLiveData.value!!
        val broadMatchList = visitableList.filterIsInstance<BroadMatchDataView>()

        `Then assert broad match item quantity`(miniCartItems, broadMatchList)
        `Then assert updated indices`(miniCartItems, visitableList)
    }

    private fun `Then assert broad match item quantity`(
            miniCartItems: Map<MiniCartItemKey, MiniCartItem>,
            broadMatchList: List<BroadMatchDataView>,
    ) {
        val broadMatchProductItems = broadMatchList.flatMap { it.broadMatchItemModelList }

        miniCartItems.forEach { miniCartItem ->
            val item = miniCartItem.value
            if (item is MiniCartItem.MiniCartItemProduct) {
                val broadMatchItem = broadMatchProductItems.find {
                    it.productCardModel.productId == item.productId
                } ?: return@forEach

                val reason = createInvalidNonVariantQtyReason(item)
                assertThat(reason, broadMatchItem.productCardModel.orderQuantity, shouldBe(item.quantity))
            }
        }
    }

    private fun createInvalidNonVariantQtyReason(miniCartItem: MiniCartItem.MiniCartItemProduct) =
        "Product \"${miniCartItem.productId}\" quantity is invalid."

    private fun `Then assert updated indices`(
            miniCartItems: Map<MiniCartItemKey, MiniCartItem>,
            visitableList: List<Visitable<*>>,
    ) {
        val expectedUpdatedIndices = createExpectedUpdatedIndices(miniCartItems, visitableList)
        val actualUpdatedIndices = tokoNowSearchViewModel.updatedVisitableIndicesLiveData.value!!

        val containsUpdatedBroadMatchIndices = actualUpdatedIndices.containsAll(expectedUpdatedIndices)
        assertThat(containsUpdatedBroadMatchIndices, shouldBe(true))
    }

    private fun createExpectedUpdatedIndices(
            miniCartItems: Map<MiniCartItemKey, MiniCartItem>,
            visitableList: List<Visitable<*>>,
    ): Set<Int> {
        val expectedUpdatedIndices = mutableSetOf<Int>()

        visitableList.forEachIndexed { index, visitable ->
            if (visitable is BroadMatchDataView) {
                miniCartItems.forEach { miniCartItem ->
                    val item = miniCartItem.value
                    if (item is MiniCartItem.MiniCartItemProduct) {
                        visitable.broadMatchItemModelList.forEach {
                            val isInMiniCart = it.productCardModel.productId == item.productId

                            if (isInMiniCart)
                                expectedUpdatedIndices.add(index)
                        }
                    }
                }
            }
        }

        return expectedUpdatedIndices
    }

    @Test
    fun `onViewUpdateCartItems should update broad match quantity from mini cart`() {
        val searchModel = "search/broadmatch/broadmatch-low-result.json".jsonToObject<SearchModel>()
        val miniCartSimplifiedData = SearchCategoryDummyUtils.miniCartSimplifiedData

        `Given get search first page use case will be successful`(searchModel)
        `Given view already created`()

        `When view update cart items`(miniCartSimplifiedData)

        `Then assert broadmatch item quantity is updated`(miniCartSimplifiedData)
    }

    private fun `When view update cart items`(miniCartSimplifiedData: MiniCartSimplifiedData) {
        tokoNowSearchViewModel.onViewUpdateCartItems(miniCartSimplifiedData)
    }
}
