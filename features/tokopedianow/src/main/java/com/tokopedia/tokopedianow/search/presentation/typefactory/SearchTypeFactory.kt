package com.tokopedia.tokopedianow.search.presentation.typefactory

import com.tokopedia.tokopedianow.search.presentation.model.BroadMatchDataView
import com.tokopedia.tokopedianow.search.presentation.model.CTATokopediaNowHomeDataView
import com.tokopedia.tokopedianow.search.presentation.model.CategoryJumperDataView
import com.tokopedia.tokopedianow.search.presentation.model.SuggestionDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.typefactory.BaseSearchCategoryTypeFactory

interface SearchTypeFactory: BaseSearchCategoryTypeFactory {

    fun type(suggestionDataView: SuggestionDataView): Int

    fun type(categoryJumperDataView: CategoryJumperDataView): Int

    fun type(ctaTokopediaNowHomeDataView: CTATokopediaNowHomeDataView): Int

    fun type(broadMatchDataView: BroadMatchDataView): Int
}