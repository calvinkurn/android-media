package com.tokopedia.search.result.presentation.view.listener

import com.tokopedia.search.result.presentation.model.BroadMatchItemDataView
import com.tokopedia.search.result.presentation.model.BroadMatchDataView

interface BroadMatchListener {
    fun onBroadMatchItemClicked(broadMatchItemDataView: BroadMatchItemDataView)

    fun onBroadMatchSeeMoreClicked(broadMatchDataView: BroadMatchDataView)

    fun onBroadMatchThreeDotsClicked(broadMatchItemDataView: BroadMatchItemDataView)

    fun onBroadMatchItemImpressed(broadMatchItemDataView: BroadMatchItemDataView)
}