package com.tokopedia.search.result.presentation.view.listener

import com.tokopedia.search.result.presentation.model.BroadMatchItemViewModel
import com.tokopedia.search.result.presentation.model.BroadMatchViewModel

interface BroadMatchListener {
    fun onBroadMatchItemClicked(broadMatchItemViewModel: BroadMatchItemViewModel)

    fun onBroadMatchSeeMoreClicked(broadMatchViewModel: BroadMatchViewModel)

    fun onBroadMatchThreeDotsClicked(broadMatchItemViewModel: BroadMatchItemViewModel)
}