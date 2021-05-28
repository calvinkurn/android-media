package com.tokopedia.gm.common.constant

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.user_identification_common.KYCConstant

/**
 * Created By @ilhamsuaib on 21/03/21
 */

object PMConstant {

    const val PM_SETTING_INFO_SOURCE = "power-merchant-subscription-android-ui"
    const val TRANSITION_PERIOD_START_DATE = "31 Mei 2021"

    object Images {
        const val PM_BADGE = "https://images.tokopedia.net/img/goldmerchant/pm_activation/badge/Power%20Merchant@3x.png"
        const val PM_PRO_BADGE = "https://images.tokopedia.net/img/goldmerchant/pm_activation/badge/Power%20Merchant%20Pro@3x.png"
        const val PM_NEW_REQUIREMENT = "https://images.tokopedia.net/img/android/gold_merchant_common/pm_new_requirement.png"
        const val PM_NEW_SCHEMA = "https://images.tokopedia.net/img/android/gold_merchant_common/pm_new_chema.png"
        const val PM_NEW_BENEFITS = "https://images.tokopedia.net/img/android/gold_merchant_common/pm_new_benefits.png"
        const val PM_INTEGRATED_WITH_REPUTATION = "https://images.tokopedia.net/img/android/gold_merchant_common/pm_integrated_with_reputation.png"
        const val PM_SHOP_SCORE_NOT_ELIGIBLE_BOTTOM_SHEET = "https://images.tokopedia.net/img/android/gold_merchant_common/pm_inactive.png"
        const val PM_ADD_PRODUCT_BOTTOM_SHEET = "https://images.tokopedia.net/img/android/gold_merchant_common/gm_add_product.png"
        const val PM_INACTIVE = "https://images.tokopedia.net/img/android/gold_merchant_common/gmc_pm_inactive.png"
        const val PM_TOTAL_ORDER_TERM = "https://images.tokopedia.net/img/android/gold_merchant_common/img_pm_improve_shop_order.png"
        const val IMG_TOPED_PM_ACTIVE = "https://images.tokopedia.net/img/goldmerchant/pm_activation/toped/img_toped_pm_active.png"
        const val IMG_TOPED_PM_INACTIVE = "https://images.tokopedia.net/img/goldmerchant/pm_activation/toped/img_toped_pm_inactive.png"
        const val IMG_TOPED_PM_PRO_ACTIVE = "https://images.tokopedia.net/img/goldmerchant/pm_activation/toped/img_toped_pm_pro_active.png"
        const val IMG_TOPED_PM_PRO_INACTIVE = "https://images.tokopedia.net/img/goldmerchant/pm_activation/toped/img_toped_pm_pro_inactive.png"
        const val PM_POTENTIAL_BENEFIT_01 = "https://images.tokopedia.net/img/android/gold_merchant_common/img_pm_visitor_chart.png"
        const val PM_POTENTIAL_BENEFIT_02 = "https://images.tokopedia.net/img/android/gold_merchant_common/img_pm_improve_shop_order.png"
        const val PM_POTENTIAL_BENEFIT_03 = "https://images.tokopedia.net/img/android/gold_merchant_common/img_pm_improve_shop_performance.png"
    }

    object Urls {
        const val SHOP_SCORE_INTERRUPT_PAGE = "https://www.tokopedia.com/shop-interrupt"
    }

    object AppLink {
        private const val APPLINK_PARAMS_KYC_PM = "${KYCConstant.PARAM_PROJECT_ID}=${KYCConstant.MERCHANT_KYC_PROJECT_ID}"
        private const val APPLINK_PARAMS_KYC_PM_PRO = "${KYCConstant.PARAM_PROJECT_ID}=${KYCConstant.PM_PRO_KYC_PROJECT_ID}"
        const val KYC_POWER_MERCHANT = "${ApplinkConst.KYC_NO_PARAM}?$APPLINK_PARAMS_KYC_PM"
        const val KYC_POWER_MERCHANT_PRO = "${ApplinkConst.KYC_NO_PARAM}?$APPLINK_PARAMS_KYC_PM_PRO"
    }

    object PMTierType {
        const val NA = -1
        const val POWER_MERCHANT = 0
        const val POWER_MERCHANT_PRO = 1
    }

    object ShopTierType {
        const val NA = -1
        const val REGULAR_MERCHANT = 0
        const val POWER_MERCHANT = 1
        const val OFFICIAL_STORE = 2
        const val POWER_MERCHANT_PRO = 3
    }
}