package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.utils.Dimension90Utils
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.SuggestionDataView
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsCollectionContaining.hasItem
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.hamcrest.core.IsNot.not
import org.junit.Test
import rx.Subscriber

internal class SearchProductSuggestionTest: ProductListPresenterTestFixtures() {

    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val visitableList by lazy { visitableListSlot.captured }

    @Test
    fun `show suggestion data view in visitable list`() {
        val suggestionJSON = "searchproduct/suggestion/suggestion-response-code-7.json"
        val searchProductModel = suggestionJSON.jsonToObject<SearchProductModel>()
        val typoKeyword = "smsung"
        val searchParameter = mapOf(
            SearchApiConst.Q to typoKeyword,
            SearchApiConst.NAVSOURCE to "campaign",
            SearchApiConst.SRP_PAGE_ID to "1234",
            SearchApiConst.SRP_PAGE_TITLE to "testing title",
        )

        `Given search product API will return suggestion`(searchProductModel)
        `Given view will return keyword`(typoKeyword)

        `When load data`(searchParameter)

        `Then verify view set product list`()
        `Then assert suggestion data view`(
            visitableList.getSuggestionDataView(),
            searchProductModel.searchProduct.data.suggestion,
            typoKeyword,
            Dimension90Utils.getDimension90(searchParameter)
        )
    }

    private fun `Given search product API will return suggestion`(
        searchProductModel: SearchProductModel
    ) {
        every {
            searchProductFirstPageUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `Given view will return keyword`(keyword: String) {
        every { productListView.queryKey } returns keyword
    }

    private fun `When load data`(searchParameter: Map<String, Any> = mapOf()) {
        productListPresenter.loadData(searchParameter)
    }

    private fun `Then verify view set product list`() {
        verify {
            productListView.setProductList(capture(visitableListSlot))
        }
    }

    private fun List<Visitable<*>>.getSuggestionDataView() =
        find { it is SuggestionDataView } as SuggestionDataView

    private fun `Then assert suggestion data view`(
        suggestionDataView: SuggestionDataView,
        expectedSuggestionModel: SearchProductModel.Suggestion,
        expectedKeyword: String,
        expectedDimension90: String,
    ) {
        assertThat(suggestionDataView.suggestedQuery, `is`(expectedSuggestionModel.query))
        assertThat(suggestionDataView.suggestion, `is`(expectedSuggestionModel.suggestion))
        assertThat(suggestionDataView.suggestionText, `is`(expectedSuggestionModel.text))
        assertThat(suggestionDataView.componentId, `is`(expectedSuggestionModel.componentId))
        assertThat(suggestionDataView.trackingOption, `is`(expectedSuggestionModel.trackingOption))
        assertThat(suggestionDataView.keyword, `is`(expectedKeyword))
        assertThat(suggestionDataView.dimension90, `is`(expectedDimension90))
    }

    @Test
    fun `do not show suggestion when response code is not 3 or 6 or 7`() {
        val suggestionJSON = "searchproduct/suggestion/suggestion-invalid-response-code.json"
        val searchProductModel = suggestionJSON.jsonToObject<SearchProductModel>()

        `Given search product API will return suggestion`(searchProductModel)

        `When load data`()

        `Then verify view set product list`()
        val notContainsSuggestionDataViewMatcher =
            not(hasItem(instanceOf<SuggestionDataView>(SuggestionDataView::class.java)))
        assertThat(visitableList, notContainsSuggestionDataViewMatcher)
    }

    @Test
    fun `suggestion data view tracking value is from related when response code is 3`() {
        val suggestionJSON = "searchproduct/suggestion/suggestion-response-code-3.json"
        val searchProductModel = suggestionJSON.jsonToObject<SearchProductModel>()
        `Given search product API will return suggestion`(searchProductModel)

        `When load data`()

        `Then verify view set product list`()
        assertThat(
            visitableList.getSuggestionDataView().trackingValue,
            `is`(searchProductModel.searchProduct.data.related.relatedKeyword)
        )
    }

    @Test
    fun `suggestion data view tracking value is from suggestion when response code is 6`() {
        val suggestionJSON = "searchproduct/suggestion/suggestion-response-code-6.json"
        `suggestion data view tracking value from suggestion`(suggestionJSON)
    }

    @Test
    fun `suggestion data view tracking value is from suggestion when response code is 7`() {
        val suggestionJSON = "searchproduct/suggestion/suggestion-response-code-7.json"
        `suggestion data view tracking value from suggestion`(suggestionJSON)
    }

    private fun `suggestion data view tracking value from suggestion`(suggestionJSON: String) {
        val searchProductModel = suggestionJSON.jsonToObject<SearchProductModel>()
        `Given search product API will return suggestion`(searchProductModel)

        `When load data`()

        `Then verify view set product list`()
        assertThat(
            visitableList.getSuggestionDataView().trackingValue,
            `is`(searchProductModel.searchProduct.data.suggestion.suggestion)
        )
    }
}