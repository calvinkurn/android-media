package com.tokopedia.search

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.search.result.presentation.view.activity.SearchActivity
import com.tokopedia.search.result.presentation.view.adapter.ProductListAdapter

internal const val QUERY_PARAMS_WITH_KEYWORD = "?q=samsung"

internal fun disableOnBoarding(context: Context) {
    LocalCacheHandler(context, SearchConstant.FreeOngkir.FREE_ONGKIR_LOCAL_CACHE_NAME).also {
        it.putBoolean(SearchConstant.FreeOngkir.FREE_ONGKIR_SHOW_CASE_ALREADY_SHOWN, true)
        it.applyEditor()
    }

    LocalCacheHandler(context, SearchConstant.OnBoarding.LOCAL_CACHE_NAME).also {
        it.putBoolean(SearchConstant.OnBoarding.FILTER_ONBOARDING_SHOWN, true)
        it.applyEditor()
    }
}

internal fun createIntent(queryParams: String = QUERY_PARAMS_WITH_KEYWORD): Intent {
    return Intent(InstrumentationRegistry.getInstrumentation().targetContext, SearchActivity::class.java).also {
        it.data = Uri.parse(ApplinkConstInternalDiscovery.SEARCH_RESULT + queryParams)
    }
}

internal fun RecyclerView?.getProductListAdapter(): ProductListAdapter {
    val productListAdapter = this?.adapter as? ProductListAdapter

    if (productListAdapter == null) {
        val detailMessage = "Adapter is not ${ProductListAdapter::class.java.simpleName}"
        throw AssertionError(detailMessage)
    }

    return productListAdapter
}