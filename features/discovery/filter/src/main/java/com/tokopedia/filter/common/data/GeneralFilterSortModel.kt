package com.tokopedia.filter.common.data

import com.tokopedia.filter.bottomsheet.filtergeneraldetail.GeneralFilterSort
import com.tokopedia.filter.bottomsheet.filtergeneraldetail.GeneralFilterSortOptions

class GeneralFilterSortModel(val optionList: List<GeneralFilterSortOptions>, val title: String) :
    GeneralFilterSort {
    override fun getItemOptions(): List<GeneralFilterSortOptions> {
        return optionList
    }

    override fun getTitleOptions(): String {
        return title
    }
}
