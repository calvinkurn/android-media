package com.tokopedia.applink.digital

object DeeplinkMapperDigitalConst {

    const val TEMPLATE_ID_VOUCHER = "voucher"
    const val TEMPLATE_ID_GENERAL = "general"
    const val TEMPLATE_ID_CC = "tagihancc"
    const val TEMPLATE_PREPAID_TELCO = "telcopre"
    const val TEMPLATE_POSTPAID_TELCO = "telcopost"
    const val TEMPLATE_ID_ELECTRONIC_MONEY = "electronicmoney"

    /**
     * Template Digital PDP Revamp 2022
     */
    const val TEMPLATE_PULSA_DIGITAL_PDP = "pulsav2"
    const val TEMPLATE_PAKET_DATA_DIGITAL_PDP = "paketdatav2"
    const val TEMPLATE_ROAMING_DIGITAL_PDP = "roamingv2"
    const val TEMPLATE_TOKEN_LISTRIK_DIGITAL_PDP = "tokenplnv2"
    const val TEMPLATE_TAGIHAN_LISTRIK_DIGITAL_PDP = "tagihanplnv2"
    /**
     * End Template
     */
    const val OLD_CATEGORY_ID_PULSA = "1"
    const val OLD_CATEGORY_ID_PAKET_DATA = "2"
    const val OLD_CATEGORY_ID_ROAMING = "20"

    const val NEW_MENU_ID_PULSA = "289"
    const val NEW_MENU_ID_PAKET_DATA = "290"
    const val NEW_MENU_ID_ROAMING = "314"

    const val MENU_ID_TELCO_PREPAID = 2
    const val MENU_ID_TELCO_POSTPAID = 3
    const val MENU_ID_ELECTRONIC_MONEY = "267"

    const val CATEGORY_ID_ELECTRONIC_MONEY = "34"
    const val TRAVEL_SUBHOMEPAGE_PLATFORM_ID = "34"
    const val RECHARGE_SUBHOMEPAGE_PLATFORM_ID = "31"

    val MENU_ID_TELCO = listOf(MENU_ID_TELCO_PREPAID, MENU_ID_TELCO_POSTPAID)

}