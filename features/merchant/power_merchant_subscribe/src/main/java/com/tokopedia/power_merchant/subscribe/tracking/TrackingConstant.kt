package com.tokopedia.power_merchant.subscribe.tracking

import com.tokopedia.config.GlobalConfig

/**
 * Created By @ilhamsuaib on 24/05/21
 */

object TrackingConstant {

    const val OS = "os"
    const val PM = "pm"
    const val RM = "rm"
    const val POWER_MERCHANT = "power merchant"
    const val TOKOPEDIA_SELLER = "tokopediaseller"
    const val PHYSICAL_GOODS = "physicalgoods"

    const val KEY_BUSINESS_UNIT = "businessUnit"
    const val KEY_CURRENT_SITE = "currentSite"
    const val KEY_SHOP_ID = "shopId"
    const val KEY_USER_ID = "userId"

    const val EVENT_CLICK_POWER_MERCHANT = "clickPowerMerchant"

    const val CATEGORY_SELLER_APP = "tokopedia seller app"
    const val CATEGORY_MAIN_APP = "tokopedia main app"

    const val ACTION_CLICK_TICKBOX_TNC = "click tickbox term & condition"
    const val ACTION_CLICK_UPGRADE_POWER_MERCHANT = "click upgrade power merchant"
    const val ACTION_CLICK_INTERESTED_TO_REGISTER = "click interested to register button"
    const val ACTION_CLICK_LEARN_MORE_SHOP_PERFORMANCE_POP_UP = "learn more on shop performance on pop up"
    const val ACTION_CLICK_ADD_ONE_PRODUCT_POP_UP = "click add 1 product on pop up"
    const val ACTION_CLICK_ADD_PRODUCT = "click add product"
    const val ACTION_CLICK_LEARN_MORE_SHOP_PERFORMANCE = "click learn more on shop performance"
    const val ACTION_CLICK_DATA_VERIFICATION = "click data verification"
    const val ACTION_CLICK_STOP_POWER_MERCHANT = "click stop power merchant"
    const val ACTION_CLICK_CONFIRM_TO_STOP_POWER_MERCHANT = "confirm to stop power merchant"
    const val ACTION_CLICK_SUBMIT_QUESTIONNAIRE = "click submit questionnaire"
    const val ACTION_CLICK_CANCEL_OPT_OUT_POWER_MERCHANT = "cancel opt out power merchant"
    const val ACTION_CLICK_ON_POWER_MERCHANT_BENEFIT = "click on power merchant benefit"
    const val ACTION_CLICK_TIPS_TO_IMPROVE_SHOP_SCORE = "click tips to improve shop score"

    fun getPowerMerchantCategory(): String = if (GlobalConfig.isSellerApp()) {
        "$CATEGORY_SELLER_APP - $POWER_MERCHANT"
    } else {
        "$CATEGORY_MAIN_APP - $POWER_MERCHANT"
    }

}