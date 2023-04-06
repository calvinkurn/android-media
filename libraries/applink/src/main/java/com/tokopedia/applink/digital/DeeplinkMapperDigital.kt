package com.tokopedia.applink.digital

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.CATEGORY_ID_ELECTRONIC_MONEY
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.CATEGORY_ID_PAKET_DATA
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.CATEGORY_ID_PULSA
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.CATEGORY_ID_ROAMING
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.DEFAULT_SUBHOMEPAGE_PLATFORM_ID
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.NEW_MENU_ID_PAKET_DATA
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.NEW_MENU_ID_PULSA
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.NEW_MENU_ID_ROAMING
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.RECHARGE_SUBHOMEPAGE_PLATFORM_ID
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.TEMPLATE_ID_CC
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.TEMPLATE_ID_ELECTRONIC_MONEY
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.TEMPLATE_ID_GENERAL
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.TEMPLATE_ID_VOUCHER
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.TEMPLATE_OLD_PREPAID_TELCO
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.TEMPLATE_PAKET_DATA_DIGITAL_PDP
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.TEMPLATE_POSTPAID_TELCO
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.TEMPLATE_PULSA_DIGITAL_PDP
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.TEMPLATE_ROAMING_DIGITAL_PDP
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.TEMPLATE_TAGIHAN_LISTRIK_DIGITAL_PDP
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.TEMPLATE_TOKEN_LISTRIK_DIGITAL_PDP
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.TRAVEL_SUBHOMEPAGE_PLATFORM_ID
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.applink.purchaseplatform.DeeplinkMapperUoh.getRegisteredNavigationUohOrder
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl


object DeeplinkMapperDigital {

    const val TEMPLATE_PARAM = "template"
    const val MENU_ID_PARAM = "menu_id"
    const val KODE_BAYAR = "kb"
    const val TEMPLATE_CATEGORY_ID = "category_id"
    const val PLATFORM_ID_PARAM = "platform_id"
    const val IS_FROM_WIDGET_PARAM = "is_from_widget"
    const val REMOTE_CONFIG_MAINAPP_ENABLE_ELECTRONICMONEY_PDP = "android_customer_enable_digital_emoney_pdp"
    const val IS_ADD_SBM = "is_add_sbm"
    const val PARAM_PRODUCT_ID = "product_id"
    const val PARAM_CLIENT_NUMBER = "client_number"

    const val OMNI_CATEGORY_ID = "54"
    const val OMNI_PRODUCT_ID = "20159"
    const val OMNI_OPERATOR_ID = "7654"

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
                    UriUtil.buildUri(ApplinkConsInternalDigital.DYNAMIC_SUBHOMEPAGE_WITHOUT_PERSONALIZE, RECHARGE_SUBHOMEPAGE_PLATFORM_ID)
                }
            }
            deeplink.startsWith(ApplinkConst.DIGITAL_CART) -> {
                ApplinkConsInternalDigital.CHECKOUT_DIGITAL
            }
            deeplink.startsWith(ApplinkConst.TELKOMSEL_OMNI) -> {
                val paymentCode = uri.getQueryParameter(KODE_BAYAR) ?: ""
                ApplinkConsInternalDigital.CHECKOUT_DIGITAL+"?category_id=$OMNI_CATEGORY_ID&operator_id=$OMNI_OPERATOR_ID&client_number=$paymentCode&product_id=$OMNI_PRODUCT_ID&is_from_widget=true"
            }
            deeplink.startsWith(ApplinkConst.DIGITAL_SMARTCARD) -> {
                getDigitalSmartcardNavigation(deeplink)
            }
            deeplink.startsWith(ApplinkConst.DIGITAL_SMARTBILLS) -> {
                ApplinkConsInternalDigital.SMART_BILLS
            }
            deeplink.startsWith(ApplinkConst.DIGITAL_SUBHOMEPAGE_HOME) -> {
                if (!uri.getQueryParameter(PLATFORM_ID_PARAM).isNullOrEmpty()) ApplinkConsInternalDigital.DYNAMIC_SUBHOMEPAGE
                else UriUtil.buildUri(ApplinkConsInternalDigital.DYNAMIC_SUBHOMEPAGE_WITH_PARAM, DEFAULT_SUBHOMEPAGE_PLATFORM_ID, false.toString())
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
        val categoryId = uri.getQueryParameter(TEMPLATE_CATEGORY_ID) ?: ""
        val productId = uri.getQueryParameter(PARAM_PRODUCT_ID) ?: ""
        val clientNumber = uri.getQueryParameter(PARAM_CLIENT_NUMBER) ?: ""
        return uri.getQueryParameter(TEMPLATE_PARAM)?.let {
            when {
                it == TEMPLATE_ID_VOUCHER -> {
                    ApplinkConsInternalDigital.VOUCHER_GAME
                }
                it == TEMPLATE_ID_GENERAL -> {
                    ApplinkConsInternalDigital.GENERAL_TEMPLATE
                }
                it == TEMPLATE_ID_CC -> {
                    ApplinkConsInternalDigital.CREDIT_CARD_TEMPLATE
                }
                it == TEMPLATE_OLD_PREPAID_TELCO && categoryId == CATEGORY_ID_PULSA -> {
                    ApplinkConsInternalDigital.DIGITAL_PDP_PULSA+"?$TEMPLATE_CATEGORY_ID=$categoryId" +
                            "&$MENU_ID_PARAM=$NEW_MENU_ID_PULSA&$TEMPLATE_PARAM=$TEMPLATE_PULSA_DIGITAL_PDP" +
                            "&$PARAM_PRODUCT_ID=$productId&$PARAM_CLIENT_NUMBER=$clientNumber"
                }
                it == TEMPLATE_OLD_PREPAID_TELCO && categoryId == CATEGORY_ID_PAKET_DATA -> {
                    ApplinkConsInternalDigital.DIGITAL_PDP_PAKET_DATA+"?$TEMPLATE_CATEGORY_ID=$categoryId" +
                            "&$MENU_ID_PARAM=$NEW_MENU_ID_PAKET_DATA&$TEMPLATE_PARAM=$TEMPLATE_PAKET_DATA_DIGITAL_PDP" +
                            "&$PARAM_PRODUCT_ID=$productId&$PARAM_CLIENT_NUMBER=$clientNumber"
                }
                it == TEMPLATE_OLD_PREPAID_TELCO && categoryId == CATEGORY_ID_ROAMING -> {
                    ApplinkConsInternalDigital.DIGITAL_PDP_PAKET_DATA+"?$TEMPLATE_CATEGORY_ID=$categoryId" +
                            "&$MENU_ID_PARAM=$NEW_MENU_ID_ROAMING&$TEMPLATE_PARAM=$TEMPLATE_ROAMING_DIGITAL_PDP" +
                            "&$PARAM_PRODUCT_ID=$productId&$PARAM_CLIENT_NUMBER=$clientNumber"
                }
                it == TEMPLATE_POSTPAID_TELCO -> {
                    ApplinkConsInternalDigital.TELCO_POSTPAID_DIGITAL
                }
                it == TEMPLATE_ID_ELECTRONIC_MONEY -> {
                    handleEmoneyPdpApplink(context, deeplink)
                }
                it == TEMPLATE_PULSA_DIGITAL_PDP -> {
                    ApplinkConsInternalDigital.DIGITAL_PDP_PULSA
                }
                it == TEMPLATE_PAKET_DATA_DIGITAL_PDP -> {
                    ApplinkConsInternalDigital.DIGITAL_PDP_PAKET_DATA
                }
                it == TEMPLATE_ROAMING_DIGITAL_PDP -> {
                    ApplinkConsInternalDigital.DIGITAL_PDP_PAKET_DATA
                }
                it == TEMPLATE_TOKEN_LISTRIK_DIGITAL_PDP -> {
                    ApplinkConsInternalDigital.DIGITAL_TOKEN_LISTRIK
                }
                it == TEMPLATE_TAGIHAN_LISTRIK_DIGITAL_PDP -> {
                    ApplinkConsInternalDigital.DIGITAL_TAGIHAN_LISTRIK
                }
                else -> deeplink
            }
        } ?: deeplink
    }

    private fun getAddBillsTelco(deeplink: String): String {
        val uri = Uri.parse(deeplink)
        return uri.getQueryParameter(TEMPLATE_PARAM)?.let {
            when(it){
                TEMPLATE_OLD_PREPAID_TELCO -> {
                    UriUtil.buildUri(ApplinkConsInternalDigital.ADD_TELCO, TEMPLATE_OLD_PREPAID_TELCO)
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
