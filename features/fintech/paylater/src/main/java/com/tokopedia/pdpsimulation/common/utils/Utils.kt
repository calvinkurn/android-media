package com.tokopedia.pdpsimulation.common.utils

import android.content.Context
import android.os.Bundle
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.pdpsimulation.paylater.domain.model.Cta
import com.tokopedia.pdpsimulation.paylater.domain.model.Detail
import com.tokopedia.pdpsimulation.paylater.presentation.detail.bottomsheet.PayLaterActionStepsBottomSheet
import com.tokopedia.pdpsimulation.paylater.presentation.detail.bottomsheet.PayLaterTokopediaGopayBottomsheet

object Utils {

    fun handleClickNavigation(
        context: Context?,
        detail: Detail,
        openHowToUse: (Bundle) -> Unit,
        openGoPay: (Bundle) -> Unit) {

        when(detail.cta.cta_type) {
            1 -> {
                if (!detail.cta.android_url.isNullOrEmpty())
                    RouteManager.route(
                        context,
                        detail.cta.android_url
                    )
            }
            2 -> {
                if (!detail.cta.web_url.isNullOrEmpty() && detail.cta.bottomSheet != null && detail.cta.bottomSheet.isShow == true) {
                    val bundle = getGoPayBundle(detail.cta)
                    openGoPay(bundle)
                }
                else if (!detail.cta.web_url.isNullOrEmpty())
                    RouteManager.route(
                        context,
                        ApplinkConstInternalGlobal.WEBVIEW,
                        detail.cta.web_url
                    )
            }
            3,4 -> {
                if (detail.gatewayDetail?.how_toUse != null) {
                    val bundle = getHowToUseBundle(detail)
                    openHowToUse(bundle)
                }
            }
        }
    }

    private fun getHowToUseBundle(detail: Detail) = Bundle().apply {
        putParcelable(PayLaterActionStepsBottomSheet.STEPS_DATA, detail)
    }

    private fun getGoPayBundle(cta: Cta) =
        Bundle().apply {
            putParcelable(PayLaterTokopediaGopayBottomsheet.GOPAY_BOTTOMSHEET_DETAIL, cta)
        }

}