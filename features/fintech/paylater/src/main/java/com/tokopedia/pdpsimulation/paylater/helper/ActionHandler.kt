package com.tokopedia.pdpsimulation.paylater.helper

import android.content.Context
import android.os.Bundle
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.pdpsimulation.paylater.domain.model.Detail

/* Handling of cta redirection on paylater card */
object ActionHandler {

    private const val TYPE_APP_LINK = 1
    private const val TYPE_WEB_VIEW = 2
    private const val TYPE_HOW_TO_USE = 3
    private const val TYPE_HOW_TO_USE_II = 4

    fun handleClickNavigation(
        context: Context?,
        detail: Detail,
        productId: String,
        openHowToUse: (Bundle) -> Unit,
        openGoPay: (Bundle) -> Unit
    ) {
        when (detail.cta.cta_type) {
            TYPE_APP_LINK -> routeToAppLink(context, detail.cta.android_url)
            TYPE_WEB_VIEW -> {
                if (shouldShowGoPayBottomSheet(detail))
                    openGoPay(PayLaterBundleGenerator.getGoPayBundle(productId, detail))
                else routeToWebView(context, detail.cta.android_url)
            }
            TYPE_HOW_TO_USE, TYPE_HOW_TO_USE_II -> {
                if (detail.gatewayDetail?.how_toUse != null) {
                    openHowToUse(PayLaterBundleGenerator.getHowToUseBundle(detail))
                }
            }
        }
    }

    private fun routeToAppLink(context: Context?, appLink: String?) {
        if (!appLink.isNullOrEmpty())
            try {
                RouteManager.route(context, appLink)
            } catch (e: Exception) {
            }
    }

    private fun routeToWebView(context: Context?, webLink: String?) {
        if (!webLink.isNullOrEmpty())
            RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, webLink)
    }

    private fun shouldShowGoPayBottomSheet(detail: Detail) = !detail.cta.android_url.isNullOrEmpty()
            && detail.cta.bottomSheet != null && detail.cta.bottomSheet.isShow == true

}