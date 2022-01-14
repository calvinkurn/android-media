package com.tokopedia.search.result.product.searchintokopedia

import android.content.Context
import com.tokopedia.applink.RouteManager

class SearchInTokopediaListenerDelegate(
    private val context: Context?,
): SearchInTokopediaListener {

    override fun onSearchInTokopediaClick(applink: String) {
        context ?: return

        RouteManager.route(context, applink)
    }
}
