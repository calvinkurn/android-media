package com.tokopedia.filter.bottomsheet.keywordfilter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheetTypeFactory
import com.tokopedia.filter.common.data.Filter

internal data class KeywordFilterDataView(
    val filter: Filter = Filter(),
    val itemList: List<KeywordFilterItemDataView> = listOf(),
): Visitable<SortFilterBottomSheetTypeFactory> {

    override fun type(typeFactory: SortFilterBottomSheetTypeFactory?) = 0
}
