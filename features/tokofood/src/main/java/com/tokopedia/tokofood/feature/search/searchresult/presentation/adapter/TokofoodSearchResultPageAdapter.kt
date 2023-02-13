package com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.common.presentation.adapter.BaseTokoFoodListAdapter

class TokofoodSearchResultPageAdapter(
    typeFactory: TokofoodSearchResultAdapterTypeFactory,
    differ: TokofoodSearchResultDiffer
) : BaseTokoFoodListAdapter<Visitable<*>, TokofoodSearchResultAdapterTypeFactory>(
    typeFactory,
    differ
)