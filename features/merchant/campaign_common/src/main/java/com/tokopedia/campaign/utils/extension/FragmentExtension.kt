package com.tokopedia.campaign.utils.extension

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.URLEncoder

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
    val encodedUrl = URLEncoder.encode(url, "UTF-8")
    val route = String.format("%s?url=%s", ApplinkConst.WEBVIEW, encodedUrl)
    RouteManager.route(activity ?: return, route)
}