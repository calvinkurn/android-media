package com.tokopedia.tokomart.search.presentation.typefactory

import com.tokopedia.tokomart.search.presentation.model.SuggestionDataView
import com.tokopedia.tokomart.searchcategory.presentation.typefactory.BaseSearchCategoryTypeFactory

interface SearchTypeFactory: BaseSearchCategoryTypeFactory {

    fun type(suggestionDataView: SuggestionDataView): Int
}