package com.tokopedia.pdpsimulation.paylater.helper

import android.content.Context
import android.os.Bundle
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.encodeToUtf8
import com.tokopedia.pdpsimulation.paylater.domain.model.BasePayLaterWidgetUiModel
import com.tokopedia.pdpsimulation.paylater.domain.model.Detail
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterArgsDescriptor
import com.tokopedia.pdpsimulation.paylater.domain.model.SeeMoreOptionsUiModel
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

/* Handling of cta redirection on paylater card */
object PayLaterHelper {

    private const val TYPE_APP_LINK = 5
    private const val TYPE_WEB_VIEW = 2
    private const val TYPE_HOW_TO_USE = 3
    private const val TYPE_HOW_TO_USE_II = 4

    fun handleClickNavigation(
        context: Context?,
        detail: Detail,
        customUrl: String,
        callAddOccApi: () -> Unit,
        openHowToUse: (Bundle) -> Unit,
        openGoPay: (Bundle) -> Unit
    ) {
        when (detail.cta.cta_type) {
            TYPE_APP_LINK ->
                {
                    callAddOccApi()
                }
            TYPE_WEB_VIEW -> {
                if (shouldShowGoPayBottomSheet(detail)) {
                    openGoPay(PayLaterBundleGenerator.getGoPayBundle(detail.cta))
                } else {
                    routeToWebView(context, customUrl)
                }
            }
            TYPE_HOW_TO_USE, TYPE_HOW_TO_USE_II -> {
                if (detail.gatewayDetail?.howToUse != null) {
                    openHowToUse(PayLaterBundleGenerator.getHowToUseBundle(detail))
                }
            }
        }
    }

    private fun routeToWebView(context: Context?, webLink: String?) {
        if (!webLink.isNullOrEmpty()) {
            RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, webLink)
        }
    }

    private fun setAdditionalParam(
        productId: String,
        tenure: Int,
        gatewayCode: String?,
        gatewayId: String?
    ): String {
        return (
            ApplinkConst.PAYLATER + "?productID=$productId" +
                "&tenure=$tenure" +
                "&gatewayCode=${gatewayCode ?: ""}" +
                "&gatewayID=${gatewayId ?: ""}"
            ).encodeToUtf8()
    }

    fun setCustomProductUrl(
        detail: Detail,
        payLaterArgsDescriptor: PayLaterArgsDescriptor
    ): String {
        return detail.cta.android_url +
            (
                if
                (detail.cta.android_url?.contains("?") == true) {
                    "&productID=${payLaterArgsDescriptor.productId}"
                } else {
                    "?productID=${payLaterArgsDescriptor.productId}"
                }
                ) +
            "&tenure=${detail.tenure}" +
            "&gatewayCode=${detail.gatewayDetail?.gatewayCode}" +
            "&gatewayID=${detail.gatewayDetail?.gateway_id}" +
            "&productURL=${
            detail.tenure?.let { selectedTenure ->
                setAdditionalParam(
                    payLaterArgsDescriptor.productId,
                    selectedTenure,
                    detail.gatewayDetail?.gatewayCode,
                    detail.gatewayDetail?.gateway_id
                )
            }
            }"
    }

    // currently will be closed from backend
    private fun shouldShowGoPayBottomSheet(detail: Detail) = !detail.cta.android_url.isNullOrEmpty() &&
        detail.cta.bottomSheet != null && detail.cta.bottomSheet.isShow == true

    fun convertPriceValueToIdrFormat(price: Number, hasSpace: Boolean): String {
        val kursIndonesia = DecimalFormat.getInstance(Locale.ENGLISH) as DecimalFormat
        kursIndonesia.maximumFractionDigits = 0
        val formatRp = DecimalFormatSymbols()
        formatRp.currencySymbol = "Rp" + if (hasSpace) " " else ""
        formatRp.groupingSeparator = '.'
        formatRp.monetaryDecimalSeparator = '.'
        formatRp.decimalSeparator = '.'
        kursIndonesia.decimalFormatSymbols = formatRp
        val result: String = if (price is Int) {
            kursIndonesia.format(price.toLong())
        } else {
            kursIndonesia.format(price)
        }
        val res = result.replace(",", ".")
        return if (res.startsWith("Rp")) {
            res
        } else {
            "Rp$res"
        }
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

    fun extractDetailFromList(payLaterList: ArrayList<BasePayLaterWidgetUiModel>): Triple<String?, String?, String?>? {
        return try {
            val allLinkingStatus: ArrayList<String> = ArrayList()
            val allUserStatus: ArrayList<String> = ArrayList()
            val allPartnerName: ArrayList<String> = ArrayList()
            for (i in 0 until payLaterList.size) {
                if (payLaterList[i] is Detail) {
                    allLinkingStatus.add((payLaterList[i] as Detail).linkingStatus.orEmpty())
                    allUserStatus.add((payLaterList[i] as Detail).userState.orEmpty())
                    allPartnerName.add((payLaterList[i] as Detail).gatewayDetail?.name ?: "")
                } else if (payLaterList[i] is SeeMoreOptionsUiModel) {
                    for (j in 0 until (payLaterList[i] as SeeMoreOptionsUiModel).remainingItems.size) {
                        allLinkingStatus.add(
                            (payLaterList[i] as SeeMoreOptionsUiModel).remainingItems[j].linkingStatus.orEmpty()
                        )
                        allUserStatus.add(
                            (payLaterList[i] as SeeMoreOptionsUiModel).remainingItems[j].userState.orEmpty()
                        )
                        allPartnerName.add(
                            (payLaterList[i] as SeeMoreOptionsUiModel).remainingItems[j].gatewayDetail?.name
                                ?: ""
                        )
                    }
                    break
                }
            }
            Triple(
                computeLabel(allLinkingStatus),
                computeLabel(allUserStatus),
                computeLabel(allPartnerName)
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun computeLabel(listOfString: List<String>) =
        listOfString.filter { it.isNotEmpty() }.joinToString(",")
}
