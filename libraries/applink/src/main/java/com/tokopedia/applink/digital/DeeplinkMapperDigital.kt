package com.tokopedia.applink.digital

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.FirebaseRemoteConfigInstance
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.CATEGORY_ID_ELECTRONIC_MONEY
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.RECHARGE_SUBHOMEPAGE_PLATFORM_ID
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.TEMPLATE_GENERAL_OPERATOR_DIGITAL_PDP
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.TEMPLATE_ID_CC
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.TEMPLATE_ID_ELECTRONIC_MONEY
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.TEMPLATE_ID_GENERAL
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.TEMPLATE_ID_VOUCHER
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.TEMPLATE_PAKET_DATA_DIGITAL_PDP
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.TEMPLATE_POSTPAID_TELCO
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.TEMPLATE_PREPAID_TELCO
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.TEMPLATE_PULSA_DIGITAL_PDP
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.TEMPLATE_ROAMING_DIGITAL_PDP
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.TRAVEL_SUBHOMEPAGE_PLATFORM_ID
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.applink.order.DeeplinkMapperUohOrder.getRegisteredNavigationUohOrder
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl


object DeeplinkMapperDigital {

    const val TEMPLATE_PARAM = "template"
    const val MENU_ID_PARAM = "menu_id"
    const val TEMPLATE_CATEGORY_ID = "category_id"
    const val PLATFORM_ID_PARAM = "platform_id"
    const val IS_FROM_WIDGET_PARAM = "is_from_widget"
    const val REMOTE_CONFIG_MAINAPP_ENABLE_ELECTRONICMONEY_PDP = "android_customer_enable_digital_emoney_pdp"
    const val IS_ADD_SBM = "is_add_sbm"

    const val SCALYR_OLD_APPLINK_TAGS = "DG_OLD_APPLINK"

    fun getRegisteredNavigationFromHttpDigital(context: Context, deeplink: String): String {
        val path = Uri.parse(deeplink).pathSegments.joinToString("/")
        val applink = DeeplinkConstDigital.MAP[path] ?: ""
        if (applink.isNotEmpty()) {
            val internalApplink = getRegisteredNavigationDigital(context, applink)
            val stringBuilder = StringBuilder(internalApplink)
            if (!stringBuilder.contains("?")) stringBuilder.append("?")
            stringBuilder.append(Uri.parse(applink).query)
            return stringBuilder.toString()
        }
        return ""
    }

    fun getRegisteredNavigationDigital(context: Context, deeplink: String): String {
        val uri = Uri.parse(deeplink)
        return when {
            deeplink.startsWith(ApplinkConst.DIGITAL_PRODUCT, true) -> {
                if(!uri.getQueryParameter(IS_ADD_SBM).isNullOrEmpty() && uri.getQueryParameter(IS_ADD_SBM) == "true" && !uri.getQueryParameter(TEMPLATE_PARAM).isNullOrEmpty()) getAddBillsTelco(deeplink)
                else if (!uri.getQueryParameter(TEMPLATE_PARAM).isNullOrEmpty() &&
                        !uri.getQueryParameter(MENU_ID_PARAM).isNullOrEmpty())
                            getDigitalTemplateNavigation(context, deeplink)
                else if (!uri.getQueryParameter(IS_FROM_WIDGET_PARAM).isNullOrEmpty()) ApplinkConsInternalDigital.CHECKOUT_DIGITAL
                else if (isEmoneyApplink(uri)) handleEmoneyPdpApplink(context, deeplink)
                else {
                    ServerLogger.log(
                            Priority.P2,
                            SCALYR_OLD_APPLINK_TAGS,
                            mapOf(
                                    "type" to "old applink",
                                    "data" to deeplink
                            )
                    )
                    UriUtil.buildUri(ApplinkConsInternalDigital.DYNAMIC_SUBHOMEPAGE_WITHOUT_PERSONALIZE, RECHARGE_SUBHOMEPAGE_PLATFORM_ID)
                }
            }
            deeplink.startsWith(ApplinkConst.DIGITAL_CART) -> {
                ApplinkConsInternalDigital.CHECKOUT_DIGITAL
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
            deeplink.startsWith(ApplinkConst.TRAVEL_SUBHOMEPAGE_HOME) -> {
                if (!uri.getQueryParameter(PLATFORM_ID_PARAM).isNullOrEmpty()) {
                    ApplinkConsInternalDigital.DYNAMIC_SUBHOMEPAGE
                } else UriUtil.buildUri(ApplinkConsInternalDigital.DYNAMIC_SUBHOMEPAGE_WITH_PARAM, TRAVEL_SUBHOMEPAGE_PLATFORM_ID, false.toString())
            }
            deeplink.startsWith(ApplinkConst.DIGITAL_ORDER) -> {
                getRegisteredNavigationUohOrder(context, deeplink)
            }
            else -> deeplink
        }
    }

    private fun getDigitalTemplateNavigation(context: Context, deeplink: String): String {
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
                TEMPLATE_ID_ELECTRONIC_MONEY -> {
                    handleEmoneyPdpApplink(context, deeplink)
                }
                TEMPLATE_PULSA_DIGITAL_PDP -> {
                    ApplinkConsInternalDigital.DIGITAL_PDP_PULSA
                }
                TEMPLATE_PAKET_DATA_DIGITAL_PDP -> {
                    ApplinkConsInternalDigital.DIGITAL_PDP_PAKET_DATA
                }
                TEMPLATE_ROAMING_DIGITAL_PDP -> {
                    ApplinkConsInternalDigital.DIGITAL_PDP_PAKET_DATA
                }
                TEMPLATE_GENERAL_OPERATOR_DIGITAL_PDP -> {
                    ApplinkConsInternalDigital.DIGITAL_TOKEN_LISTRIK
                }
                else -> deeplink
            }
        } ?: deeplink
    }

    private fun getAddBillsTelco(deeplink: String): String {
        val uri = Uri.parse(deeplink)
        return uri.getQueryParameter(TEMPLATE_PARAM)?.let {
            when(it){
                TEMPLATE_PREPAID_TELCO -> {
                    UriUtil.buildUri(ApplinkConsInternalDigital.ADD_TELCO, TEMPLATE_PREPAID_TELCO)
                }
                TEMPLATE_POSTPAID_TELCO -> {
                    UriUtil.buildUri(ApplinkConsInternalDigital.ADD_TELCO, TEMPLATE_POSTPAID_TELCO)
                }
                TEMPLATE_ID_GENERAL -> {
                    ApplinkConsInternalDigital.GENERAL_TEMPLATE
                }

                else -> deeplink
            }
        } ?: deeplink
    }

    private fun handleEmoneyPdpApplink(context: Context, deeplink: String): String {
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        val getNewEmoneyPage = remoteConfig.getBoolean(REMOTE_CONFIG_MAINAPP_ENABLE_ELECTRONICMONEY_PDP, true)
        return if (getNewEmoneyPage) {
            ApplinkConsInternalDigital.ELECTRONIC_MONEY_PDP
        } else {
            deeplink.replaceBefore("://", DeeplinkConstant.SCHEME_INTERNAL)
        }
    }

    private fun getDigitalSmartcardNavigation(deeplink: String): String {
        val uri = Uri.parse(deeplink)
        val paramValue = uri.getQueryParameter(ApplinkConsInternalDigital.PARAM_SMARTCARD) ?: ""
        val statusBrizzi = uri.getQueryParameter(ApplinkConsInternalDigital.PARAM_BRIZZI) ?: "false"

        return if (deeplink.contains("brizzi"))
            UriUtil.buildUri(ApplinkConsInternalDigital.INTERNAL_SMARTCARD_BRIZZI, paramValue)
        else
            UriUtil.buildUri(ApplinkConsInternalDigital.INTERNAL_SMARTCARD_EMONEY, paramValue)
    }

    private fun isEmoneyApplink(uri: Uri): Boolean {
        return (!uri.getQueryParameter(TEMPLATE_CATEGORY_ID).isNullOrEmpty() && uri.getQueryParameter(TEMPLATE_CATEGORY_ID).equals(CATEGORY_ID_ELECTRONIC_MONEY))
    }
}