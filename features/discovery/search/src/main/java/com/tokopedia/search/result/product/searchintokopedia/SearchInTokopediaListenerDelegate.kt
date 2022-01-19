package com.tokopedia.search.result.product.searchintokopedia

import android.content.Context
import com.tokopedia.applink.RouteManager
import java.lang.ref.WeakReference

class SearchInTokopediaListenerDelegate(
    context: Context?,
): SearchInTokopediaListener {

    private val weakContext: WeakReference<Context?> = WeakReference(context)

    override fun onSearchInTokopediaClick(applink: String) {
        val context = weakContext.get() ?: return

        RouteManager.route(context, applink)
    }
}
