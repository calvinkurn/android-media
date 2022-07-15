package com.tokopedia.campaign.utils.extension

import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


fun Fragment.doOnDelayFinished(delay : Long, block: () -> Unit) {
    CoroutineScope(Dispatchers.Main).launch {
        delay(delay)
        block()
    }
}

fun Fragment.routeToUrl(url : String) {
    if (!isAdded) return
    val encodedUrl = url.encodeToUtf8()
    val route = String.format("%s?url=%s", ApplinkConst.WEBVIEW, encodedUrl)
    RouteManager.route(activity ?: return, route)
}