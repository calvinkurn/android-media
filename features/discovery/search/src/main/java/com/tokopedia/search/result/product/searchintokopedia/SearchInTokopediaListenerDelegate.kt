package com.tokopedia.search.result.product.searchintokopedia

import android.content.Context
import com.tokopedia.search.utils.applinkopener.ApplinkOpener
import com.tokopedia.search.utils.applinkopener.ApplinkOpenerDelegate
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider

class SearchInTokopediaListenerDelegate(
    context: Context?,
    applinkOpener: ApplinkOpener = ApplinkOpenerDelegate
) : SearchInTokopediaListener,
    ApplinkOpener by applinkOpener,
    ContextProvider by WeakReferenceContextProvider(context) {

    override fun onSearchInTokopediaClick(applink: String) {
        openApplink(context, applink)
    }
}
