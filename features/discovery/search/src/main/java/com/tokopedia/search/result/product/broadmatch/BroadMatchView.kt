package com.tokopedia.search.result.product.broadmatch

interface BroadMatchView {

    fun trackEventClickBroadMatchItem(broadMatchItemDataView: BroadMatchItemDataView)

    fun trackEventImpressionBroadMatchItem(broadMatchItemDataView: BroadMatchItemDataView)

    fun trackEventImpressionBroadMatch(broadMatchDataView: BroadMatchDataView)

    fun trackEventClickSeeMoreBroadMatch(broadMatchDataView: BroadMatchDataView)

    fun openLink(applink: String, url: String)
}
