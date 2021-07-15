package com.tokopedia.search.result.presentation.view.listener

import com.tokopedia.search.result.presentation.model.GlobalNavDataView

interface GlobalNavListener {

    fun onGlobalNavWidgetClicked(item: GlobalNavDataView.Item?, keyword: String?)

    fun onGlobalNavWidgetClickSeeAll(globalNavDataView: GlobalNavDataView?)
}