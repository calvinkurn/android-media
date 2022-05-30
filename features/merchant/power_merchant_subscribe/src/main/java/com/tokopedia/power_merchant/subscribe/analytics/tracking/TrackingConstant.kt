package com.tokopedia.power_merchant.subscribe.analytics.tracking

import com.tokopedia.config.GlobalConfig

/**
 * Created By @ilhamsuaib on 24/05/21
 */

object TrackingConstant {

    const val OS = "os"
    const val PM = "pm"
    const val RM = "rm"
    const val POWER_MERCHANT = "power merchant"
    const val POWER_MERCHANT_PRO = "power merchant pro"
    const val TOKOPEDIA_SELLER = "tokopediaseller"
    const val PHYSICAL_GOODS = "physicalgoods"
    const val PHYSICAL_GOODS_NEW = "physical goods"
    const val BECOME_RM = "become RM"
    const val BECOME_PM = "become pm"

    const val KEY_BUSINESS_UNIT = "businessUnit"
    const val KEY_CURRENT_SITE = "currentSite"
    const val KEY_SHOP_ID = "shopId"
    const val KEY_USER_ID = "userId"
    const val KEY_PAGE_SOURCE = "pageSource"

    const val SHOP_TYPE = "shop_type"
    const val SHOP_SCORE = "shop_score"
    const val EVENT_CLICK_POWER_MERCHANT = "clickPowerMerchant"
    const val VIEW_POWER_MERCHANT_IRIS = "viewPowerMerchantIris"

    const val CATEGORY_SELLER_APP = "tokopedia seller app"
    const val CATEGORY_MAIN_APP = "tokopedia main app"
    const val CATEGORY_POWER_MERCHANT_OPT_OUT = "power merchant pro opt out questionnaire"

    const val CLICK_LEARN_MORE_PM_PRO = "click pelajari selengkapnya PM Pro"

    const val PM_PRO_ACTIVATION_PAGE = "PM Pro Activation Page"

    const val IMPRESSION_PM_PRO_LEARN_MORE = "impression pelajari selengkapnya PM Pro"
    const val IMPRESSION_FEE_SERVICE= "impression biaya layanan"

    const val ACTION_CLICK_TICKBOX_TNC = "click tickbox term & condition"
    const val ACTION_CLICK_UPGRADE_POWER_MERCHANT_PRO = "click upgrade power merchant pro"
    const val ACTION_CLICK_INTERESTED_TO_REGISTER = "click interested to register button power merchant pro"
    const val ACTION_CLICK_LEARN_MORE_SHOP_PERFORMANCE_POP_UP = "pop up learn more on shop performance"
    const val ACTION_CLICK_POP_UP_IMPROVE_NUMBER_OF_ORDER = "pop up improve your number of order"
    const val ACTION_CLICK_POP_UP_IMPROVE_NIV = "pop up improve your TIV"
    const val ACTION_CLICK_ADD_ONE_PRODUCT_POP_UP = "click add 1 product on pop up"
    const val ACTION_CLICK_ADD_PRODUCT = "click add product"
    const val ACTION_CLICK_LEARN_MORE_SHOP_PERFORMANCE = "click learn more on shop performance"
    const val ACTION_CLICK_DATA_VERIFICATION = "click data verification"
    const val ACTION_CLICK_STOP_POWER_MERCHANT = "click stop power merchant"
    const val ACTION_CLICK_CONFIRM_TO_STOP_POWER_MERCHANT = "confirm to stop power merchant"
    const val ACTION_CLICK_SUBMIT_QUESTIONNAIRE = "click submit questionnaire"
    const val ACTION_CLICK_CANCEL_OPT_OUT_PROCESS = "cancel opt out process"
    const val ACTION_CLICK_ON_POWER_MERCHANT_BENEFIT = "click on power merchant benefit"
    const val ACTION_CLICK_TIPS_TO_IMPROVE_SHOP_SCORE = "click tips to improve shop score"
    const val ACTION_POPUP_SUCCESS_REGISTER = "pop up success register pm"
    const val ACTION_POPUP_ADD_NEW_PRODUCT = "click add product on pop up minimum product"
    const val ACTION_CLICK_TAB_POWER_MERCHANT = "click power merchant tab"
    const val ACTION_POPUP_UNABLE_TO_REGISTER_SHOP_MODERATION = "pop up unable to register shop moderation"
    const val ACTION_CLICK_ACKNOWLEDGE_SHOP_MODERATION = "click acknowledge shop moderation"
    const val ACTION_CLICK_STOP_PM_BECOME_RM = "click stop power merchant - become rm"
    const val ACTION_CLICK_STOP_PM_BECOME_PM = "click stop power merchant - become pm"
    const val ACTION_CLICK_STOP_PM_CONFIRM_TO_STOP = "stop power merchant - confirm to stop"
    const val ACTION_CLICK_DETAIL_TERM_MEMBERSHIP = "click lihat detail - syarat keanggotaan"
    const val ACTION_CLICK_LEARN_MORE_PM_BENEFIT = "click pelajari power merchant selengkapnya - keuntungan"
    const val ACTION_CLICK_LEARN_MORE_PM = "click pelajari selengkapnya PM Pro"
    const val ACTION_CLICK_SEE_CATEGORY = "click lihat kategori"
    const val ACTION_CLICK_PROGRESS_BAR = "click progress bar"
    const val ACTION_CLICK_FEE_SERVICE = "click biaya layanan"

    const val EVENT_CLICK_PG = "clickPG"
    const val VIEW_PG_IRIS= "viewPGIris"

    fun getPowerMerchantCategory(): String = if (GlobalConfig.isSellerApp()) {
        "$CATEGORY_SELLER_APP - $POWER_MERCHANT_PRO"
    } else {
        "$CATEGORY_MAIN_APP - $POWER_MERCHANT_PRO"
    }

}