package com.tokopedia.tokopedianow.search.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.search.domain.model.SearchModel
import com.tokopedia.tokopedianow.search.presentation.model.SuggestionDataView
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertThat
import org.junit.Test

class SearchSuggestionTest: SearchTestFixtures() {

    companion object {
        private const val SUGGESTION_DATA_VIEW_POSITION = 5
    }

    @Test
    fun `test show suggestion message`() {
        val searchModel = "search/suggestion/suggestion.json".jsonToObject<SearchModel>()

        `Given get search first page use case will be successful`(searchModel)

        `When view created`()

        val visitableList = searchViewModel.visitableListLiveData.value!!
        visitableList[SUGGESTION_DATA_VIEW_POSITION].assertSuggestionDataView(
                searchModel.searchProduct.data.suggestion,
        )
    }

    private fun Visitable<*>.assertSuggestionDataView(
            suggestion: AceSearchProductModel.Suggestion
    ) {
        assertThat(this, instanceOf(SuggestionDataView::class.java))

        val suggestionDataView = this as SuggestionDataView
        assertThat(suggestionDataView.text, `is`(suggestion.text))
        assertThat(suggestionDataView.query, `is`(suggestion.query))
        assertThat(suggestionDataView.suggestion, `is`(suggestion.suggestion))
    }
}