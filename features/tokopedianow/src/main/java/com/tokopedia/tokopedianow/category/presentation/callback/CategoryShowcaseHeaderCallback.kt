package com.tokopedia.tokopedianow.category.presentation.callback

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.tokopedianow.common.view.TokoNowDynamicHeaderView

class CategoryShowcaseHeaderCallback: TokoNowDynamicHeaderView.TokoNowDynamicHeaderListener {
    override fun onSeeAllClicked(context: Context, headerName: String, appLink: String) {
        RouteManager.route(context, appLink)
    }

    override fun onChannelExpired() { /* nothing to do */ }
}
