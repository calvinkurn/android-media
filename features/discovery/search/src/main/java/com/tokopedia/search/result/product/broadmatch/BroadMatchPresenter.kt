package com.tokopedia.search.result.product.broadmatch

interface BroadMatchPresenter {

    fun onBroadMatchItemImpressed(broadMatchItemDataView: BroadMatchItemDataView, adapterPosition: Int)

    fun onBroadMatchItemClick(broadMatchItemDataView: BroadMatchItemDataView, adapterPosition: Int)

    fun onBroadMatchImpressed(broadMatchDataView: BroadMatchDataView, adapterPosition: Int)

    fun onBroadMatchSeeMoreClick(broadMatchDataView: BroadMatchDataView)

    fun onBroadMatchViewAllCardClicked(broadMatchDataView: BroadMatchDataView)
}
