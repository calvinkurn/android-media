package com.tokopedia.tokopedianow.search.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.search.presentation.typefactory.SearchTypeFactory

data class SuggestionDataView(
        val text: String = "",
        val query: String = "",
        val suggestion: String = "",
): Visitable<SearchTypeFactory> {

    override fun type(typeFactory: SearchTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}
