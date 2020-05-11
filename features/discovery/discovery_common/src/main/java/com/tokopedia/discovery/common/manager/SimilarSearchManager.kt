@file:JvmName("SimilarSearchManager")

package com.tokopedia.discovery.common.manager

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.discovery.common.constants.SearchConstant.SimilarSearch.QUERY

fun startSimilarSearch(context: Context, productId: String, query: String) {

    val intent = RouteManager.getIntent(context, ApplinkConstInternalDiscovery.SIMILAR_SEARCH_RESULT, productId)

    intent.putExtra(QUERY, query)

    context.startActivity(intent)
}