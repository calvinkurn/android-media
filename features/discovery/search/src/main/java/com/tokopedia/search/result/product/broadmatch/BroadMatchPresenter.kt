package com.tokopedia.search.result.product.broadmatch

interface BroadMatchPresenter {

    fun onBroadMatchItemImpressed(broadMatchItemDataView: BroadMatchItemDataView)

    fun onBroadMatchItemClick(broadMatchItemDataView: BroadMatchItemDataView)

    fun onBroadMatchImpressed(broadMatchDataView: BroadMatchDataView)

    fun onBroadMatchSeeMoreClick(broadMatchDataView: BroadMatchDataView)

    fun onBroadMatchViewAllCardClicked(broadMatchDataView: BroadMatchDataView)
}
