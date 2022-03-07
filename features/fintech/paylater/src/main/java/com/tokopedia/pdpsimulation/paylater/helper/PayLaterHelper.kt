package com.tokopedia.pdpsimulation.paylater.helper

import android.content.Context
import android.os.Bundle
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.pdpsimulation.paylater.domain.model.BasePayLaterWidgetUiModel
import com.tokopedia.pdpsimulation.paylater.domain.model.Detail
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

/* Handling of cta redirection on paylater card */
object PayLaterHelper {

    private const val TYPE_APP_LINK = 1
    private const val TYPE_WEB_VIEW = 2
    private const val TYPE_HOW_TO_USE = 3
    private const val TYPE_HOW_TO_USE_II = 4

    fun handleClickNavigation(
        context: Context?,
        detail: Detail,
        customUrl: String,
        openHowToUse: (Bundle) -> Unit,
        openGoPay: (Bundle) -> Unit
    ) {
        when (detail.cta.cta_type) {
            TYPE_APP_LINK -> routeToAppLink(context, customUrl)
            TYPE_WEB_VIEW -> {
                if (shouldShowGoPayBottomSheet(detail))
                    openGoPay(PayLaterBundleGenerator.getGoPayBundle(detail.cta))
                else routeToWebView(context, customUrl)
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

    // currently will be closed from backend
    private fun shouldShowGoPayBottomSheet(detail: Detail) = !detail.cta.android_url.isNullOrEmpty()
            && detail.cta.bottomSheet != null && detail.cta.bottomSheet.isShow == true

    fun convertPriceValueToIdrFormat(price: Number, hasSpace: Boolean): String {
        val kursIndonesia = DecimalFormat.getInstance(Locale.ENGLISH) as DecimalFormat
        kursIndonesia.maximumFractionDigits = 0
        val formatRp = DecimalFormatSymbols()
        formatRp.currencySymbol = "Rp" + if (hasSpace) " " else ""
        formatRp.groupingSeparator = '.'
        formatRp.monetaryDecimalSeparator = '.'
        formatRp.decimalSeparator = '.'
        kursIndonesia.decimalFormatSymbols = formatRp
        val result: String = if (price is Int)
            kursIndonesia.format(price.toLong())
        else kursIndonesia.format(price)
        return result.replace(",", ".")
    }

    fun getProductNameList(modelList: ArrayList<BasePayLaterWidgetUiModel>?): String {
        val nameList = arrayListOf<String>()
        modelList?.forEach {
            if (it is Detail) {
                if (it.gatewayDetail?.name?.isEmpty() == false) {
                    nameList.add(it.gatewayDetail.name)
                }
            }
        }
        return nameList.joinToString(",")
    }
}