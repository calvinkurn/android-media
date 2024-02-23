package com.tokopedia.search.result.product.broadmatch

interface BroadMatchView {

    fun trackEventClickBroadMatchItem(broadMatchItemDataView: BroadMatchItemDataView, adapterPosition: Int)

    fun trackEventImpressionBroadMatchItem(broadMatchItemDataView: BroadMatchItemDataView, adapterPosition: Int)

    fun trackEventImpressionBroadMatch(broadMatchDataView: BroadMatchDataView, adapterPosition: Int)

    fun trackEventClickSeeMoreBroadMatch(broadMatchDataView: BroadMatchDataView)

    fun openLink(applink: String, url: String)

    fun openLink(broadMatchItemDataView: BroadMatchItemDataView)
}
