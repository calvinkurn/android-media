package com.tokopedia.search.result.product.violation

import android.content.Context
import com.tokopedia.search.utils.applinkopener.ApplinkOpener
import com.tokopedia.search.utils.applinkopener.ApplinkOpenerDelegate
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider

class ViolationListenerDelegate(
    context: Context?,
    applinkOpener: ApplinkOpener = ApplinkOpenerDelegate
) : ViolationListener,
    ApplinkOpener by applinkOpener,
    ContextProvider by WeakReferenceContextProvider(context) {

    override fun onViolationButtonClick(applink: String) {
        openApplink(context, applink)
    }
}