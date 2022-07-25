package com.tokopedia.campaign.utils.extension

import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


fun Fragment.doOnDelayFinished(delay: Long, operation: () -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        delay(delay)

        if (isAdded && context != null) {
            operation()
        }

    }
}
fun Fragment.routeToUrl(url : String) {
    if (!isAdded) return
    val encodedUrl = url.encodeToUtf8()
    val route = String.format("%s?url=%s", ApplinkConst.WEBVIEW, encodedUrl)
    RouteManager.route(activity ?: return, route)
}