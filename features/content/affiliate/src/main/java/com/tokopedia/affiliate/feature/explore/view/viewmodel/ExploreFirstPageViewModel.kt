package com.tokopedia.affiliate.feature.explore.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * @author by milhamj on 14/03/19.
 */
data class ExploreFirstPageViewModel(
        val section: List<Visitable<*>> = arrayListOf(),
        val products: List<Visitable<*>> = arrayListOf(),
        val sortList: List<SortViewModel> = arrayListOf(),
        val nextCursor: String = ""
)