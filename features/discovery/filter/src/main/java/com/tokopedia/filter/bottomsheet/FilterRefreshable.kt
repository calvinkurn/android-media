package com.tokopedia.filter.bottomsheet

import com.tokopedia.filter.bottomsheet.filter.OptionViewModel
import com.tokopedia.filter.common.data.Filter

internal interface FilterRefreshable {
    val isWillSortOptionList: Boolean
    val filter: Filter
    var optionViewModelList: MutableList<OptionViewModel>
}