package com.tokopedia.applink.digital

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.TEMPLATE_ID_CC
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.TEMPLATE_ID_GENERAL
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.TEMPLATE_ID_VOUCHER
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.TEMPLATE_POSTPAID_TELCO
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.TEMPLATE_PREPAID_TELCO
import com.tokopedia.applink.internal.ApplinkConsInternalDigital


object DeeplinkMapperDigital {

    const val TEMPLATE_PARAM = "template"
    const val PLATFORM_ID_PARAM = "platform_id"

    fun getRegisteredNavigationFromHttpDigital(deeplink: String): String {
        val path = Uri.parse(deeplink).pathSegments.joinToString("/")
        val applink = DeeplinkConstDigital.MAP[path] ?: ""
        val internalApplink = getRegisteredNavigationDigital(applink)
        val stringBuilder = StringBuilder(internalApplink)
        stringBuilder.append("?")
        stringBuilder.append(Uri.parse(applink).query)
        return stringBuilder.toString()
    }

    fun getRegisteredNavigationDigital(deeplink: String): String {
        val uri = Uri.parse(deeplink)
        return when {
            deeplink.startsWith(ApplinkConst.DIGITAL_PRODUCT, true) -> {
                if (!uri.getQueryParameter(TEMPLATE_PARAM).isNullOrEmpty()) getDigitalTemplateNavigation(deeplink)
                else deeplink.replaceBefore("://", DeeplinkConstant.SCHEME_INTERNAL)
            }
            deeplink.startsWith(ApplinkConst.DIGITAL_SMARTCARD) -> {
                getDigitalSmartcardNavigation(deeplink)
            }
            deeplink.startsWith(ApplinkConst.DIGITAL_SMARTBILLS) -> {
                ApplinkConsInternalDigital.SMART_BILLS
            }
            deeplink.startsWith(ApplinkConst.DIGITAL_SUBHOMEPAGE_HOME) -> {
                if (!uri.getQueryParameter(PLATFORM_ID_PARAM).isNullOrEmpty()) ApplinkConsInternalDigital.DYNAMIC_SUBHOMEPAGE
                else ApplinkConsInternalDigital.SUBHOMEPAGE
            }
            else -> deeplink
        }
    }

    fun getDigitalTemplateNavigation(deeplink: String): String {
        val uri = Uri.parse(deeplink)
        return uri.getQueryParameter(TEMPLATE_PARAM)?.let {
            when (it) {
                TEMPLATE_ID_VOUCHER -> {
                    ApplinkConsInternalDigital.VOUCHER_GAME
                }
                TEMPLATE_ID_GENERAL -> {
                    ApplinkConsInternalDigital.GENERAL_TEMPLATE
                }
                TEMPLATE_ID_CC -> {
                    ApplinkConsInternalDigital.CREDIT_CARD_TEMPLATE
                }
                TEMPLATE_PREPAID_TELCO -> {
                    ApplinkConsInternalDigital.TELCO_PREPAID_DIGITAL
                }
                TEMPLATE_POSTPAID_TELCO -> {
                    ApplinkConsInternalDigital.TELCO_POSTPAID_DIGITAL
                }
                else -> deeplink
            }
        } ?: deeplink
    }

    private fun getDigitalSmartcardNavigation(deeplink: String): String {
        val uri = Uri.parse(deeplink)
        val paramValue = uri.getQueryParameter(ApplinkConsInternalDigital.PARAM_SMARTCARD) ?: ""
        val statusBrizzi = uri.getQueryParameter(ApplinkConsInternalDigital.PARAM_BRIZZI)?: "false"

        return if (statusBrizzi == "true")
            UriUtil.buildUri(ApplinkConsInternalDigital.INTERNAL_SMARTCARD_BRIZZI, paramValue)
        else
            UriUtil.buildUri(ApplinkConsInternalDigital.INTERNAL_SMARTCARD_EMONEY, paramValue)
    }
}