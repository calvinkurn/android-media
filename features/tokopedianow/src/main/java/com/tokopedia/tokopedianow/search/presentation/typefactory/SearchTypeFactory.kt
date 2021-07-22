package com.tokopedia.tokopedianow.search.presentation.typefactory

import com.tokopedia.tokopedianow.search.presentation.model.SuggestionDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.typefactory.BaseSearchCategoryTypeFactory

interface SearchTypeFactory: BaseSearchCategoryTypeFactory {

    fun type(suggestionDataView: SuggestionDataView): Int
}