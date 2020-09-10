package com.tokopedia.applink.digital

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.TEMPLATE_ID_CC
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.TEMPLATE_ID_GENERAL
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.TEMPLATE_ID_VOUCHER
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.TEMPLATE_POSTPAID_TELCO
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.TEMPLATE_PREPAID_TELCO
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import tokopedia.applink.R
import java.io.BufferedReader
import java.io.InputStreamReader


object DeeplinkMapperDigital {

    /**
     * Cache to variable to speed up performance
     */
    var whiteList: Whitelist? = null

    const val TEMPLATE_PARAM = "template"
    const val PLATFORM_ID_PARAM = "platform_id"

    private fun readWhitelistFromFile(context: Context): List<WhitelistItem> {
        if (whiteList == null) {
            val inputStream = context.getResources().openRawResource(R.raw.whitelist)
            val reader = BufferedReader(InputStreamReader(inputStream))

            val gson = Gson()
            whiteList = gson.fromJson(reader, Whitelist::class.java)
        }
        return whiteList?.data ?: listOf()
    }

    fun getRegisteredNavigationFromHttpDigital(context: Context, deeplink: String): String {
        val path = Uri.parse(deeplink).pathSegments.joinToString("/")
        return readWhitelistFromFile(context).firstOrNull { it.path.equals(path, false) }?.applink
                ?: ""
    }

    fun getRegisteredNavigationDigital(context: Context, deeplink: String): String {
        val uri = Uri.parse(deeplink)
        return when {
            deeplink.startsWith(ApplinkConst.DIGITAL_PRODUCT, true) -> {
                if (!uri.getQueryParameter(TEMPLATE_PARAM).isNullOrEmpty()) getDigitalTemplateNavigation(context, deeplink)
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

    fun getDigitalTemplateNavigation(context: Context, deeplink: String): String {
        val uri = Uri.parse(deeplink)
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        return uri.getQueryParameter(TEMPLATE_PARAM)?.let {
            when (it) {
                TEMPLATE_ID_VOUCHER -> {
                    if (remoteConfig.getBoolean(RemoteConfigKey.MAINAPP_ENABLE_DIGITAL_VOUCHER_GAME_PDP))
                        ApplinkConsInternalDigital.VOUCHER_GAME else deeplink
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
        var paramValue = uri.getQueryParameter(ApplinkConsInternalDigital.PARAM_SMARTCARD) ?: ""
        var statusBrizzi = uri.getQueryParameter(ApplinkConsInternalDigital.PARAM_BRIZZI)?: "false"

        return if (statusBrizzi == "true")
            UriUtil.buildUri(ApplinkConsInternalDigital.INTERNAL_SMARTCARD_BRIZZI, paramValue)
        else
            UriUtil.buildUri(ApplinkConsInternalDigital.INTERNAL_SMARTCARD_EMONEY, paramValue)
    }
}