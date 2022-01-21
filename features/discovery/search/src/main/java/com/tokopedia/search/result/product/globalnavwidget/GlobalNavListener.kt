package com.tokopedia.search.result.product.globalnavwidget

interface GlobalNavListener {

    fun onGlobalNavWidgetImpressed(globalNavDataView: GlobalNavDataView)

    fun onGlobalNavWidgetClicked(item: GlobalNavDataView.Item, keyword: String)

    fun onGlobalNavWidgetClickSeeAll(globalNavDataView: GlobalNavDataView)
}