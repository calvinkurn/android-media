@file:JvmName("SimilarSearchManager")

package com.tokopedia.discovery.common.manager

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.discovery.common.constants.SearchConstant.SimilarSearch.SIMILAR_SEARCH_SELECTED_PRODUCT
import com.tokopedia.discovery.common.model.SimilarSearchSelectedProduct

fun startSimilarSearch(context: Context, similarSearchSelectedProduct: SimilarSearchSelectedProduct) {

    val intent = RouteManager.getIntent(context, ApplinkConstInternalDiscovery.SIMILAR_SEARCH_RESULT, similarSearchSelectedProduct.id)

    intent.putExtra(SIMILAR_SEARCH_SELECTED_PRODUCT, similarSearchSelectedProduct)

    context.startActivity(intent)
}