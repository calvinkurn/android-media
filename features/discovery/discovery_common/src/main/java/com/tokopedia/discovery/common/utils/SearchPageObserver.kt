package com.tokopedia.discovery.common.utils

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.EC_SEARCH_SESSION_ID

class SearchPageObserver(private val key: String): DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        val hasSRP = SearchNavigationRecord.containsPageKey(SRP)
        if (!hasSRP) {
            val sessionId = System.currentTimeMillis().toString()
            val sessionIdEntry = mapOf(EC_SEARCH_SESSION_ID to sessionId)

            SearchNavigationRecord.addPage(key, sessionIdEntry)
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)

        SearchNavigationRecord.removePage(key)
    }
}
