package com.tokopedia.tokopedianow.category.presentation.callback

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.tokopedianow.common.view.TokoNowDynamicHeaderView

class CategoryShowcaseHeaderCallback(
    private val onClickSeeMore: (widgetId: String) -> Unit
): TokoNowDynamicHeaderView.TokoNowDynamicHeaderListener {
    override fun onSeeAllClicked(
        context: Context,
        headerName: String,
        appLink: String,
        widgetId: String
    ) {
        RouteManager.route(context, appLink)
        onClickSeeMore(widgetId)
    }

    override fun onChannelExpired() { /* nothing to do */ }
}
