package com.tokopedia.tokopedianow.search.presentation.viewmodel

import com.tokopedia.tokopedianow.search.domain.model.SearchModel
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
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

        val visitableList = tokoNowSearchViewModel.visitableListLiveData.value!!
        visitableList[SUGGESTION_DATA_VIEW_POSITION].assertSuggestionDataView(
                searchModel.getSuggestion(),
        )
    }
}