package com.tokopedia.tokofood.feature.search.srp.presentation.customview

import com.tokopedia.sortfilter.SortFilter

class TokofoodSearchFilterTab(
    private val sortFilter: SortFilter,
    private val listener: Listener
) {

    interface Listener {
        fun onFullFilterClicked()
    }

}