package com.tokopedia.search.result.product.violation

import android.content.Context
import com.tokopedia.search.utils.applinkopener.ApplinkOpener
import com.tokopedia.search.utils.applinkopener.ApplinkOpenerDelegate
import java.lang.ref.WeakReference

class ViolationListenerDelegate(
    context: Context?,
    applinkOpener: ApplinkOpener = ApplinkOpenerDelegate
) : ViolationListener,
    ApplinkOpener by applinkOpener {
    private val contextReference: WeakReference<Context> = WeakReference(context)

    override fun onViolationButtonClick(applink: String) {
        openApplink(contextReference.get(), applink)
    }
}