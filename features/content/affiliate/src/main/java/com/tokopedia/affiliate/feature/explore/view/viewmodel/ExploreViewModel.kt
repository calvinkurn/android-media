package com.tokopedia.affiliate.feature.explore.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * @author by milhamj on 15/03/19.
 */
data class ExploreViewModel(
        val exploreProducts: List<Visitable<*>> = listOf(),
        val nextCursor: String = ""
)