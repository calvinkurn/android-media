package com.tokopedia.gm.common.constant

/**
 * Created By @ilhamsuaib on 21/03/21
 */

object PMConstant {

    const val POWER_MERCHANT_CHARGING = "1,25%"
    const val POWER_MERCHANT_PRO_CHARGING = "1,5%"
    const val PM_SETTING_INFO_SOURCE = "power-merchant-subscription-ui"
    const val TRANSITION_PERIOD_START_DATE = "31 Mei 2021"

    object Images {
        const val PM_REGISTRATION_SUCCESS = "https://images.tokopedia.net/img/android/gold_merchant_common/pm_registration_success.png"
        const val PM_NEW_REQUIREMENT = "https://images.tokopedia.net/img/android/gold_merchant_common/pm_new_requirement.png"
        const val PM_NEW_SCHEMA = "https://images.tokopedia.net/img/android/gold_merchant_common/pm_new_chema.png"
        const val PM_NEW_BENEFITS = "https://images.tokopedia.net/img/android/gold_merchant_common/pm_new_benefits.png"
        const val PM_INTEGRATED_WITH_REPUTATION = "https://images.tokopedia.net/img/android/gold_merchant_common/pm_integrated_with_reputation.png"
        const val PM_SHOP_SCORE_NOT_ELIGIBLE_BOTTOM_SHEET = "https://images.tokopedia.net/img/android/gold_merchant_common/pm_inactive.png"
        const val PM_ADD_PRODUCT_BOTTOM_SHEET = "https://images.tokopedia.net/img/android/gold_merchant_common/gm_add_product.png"
        const val PM_INACTIVE = "https://images.tokopedia.net/img/android/gold_merchant_common/gmc_pm_inactive.png"
        const val PM_REGISTRATION_PM = "https://images.tokopedia.net/img/android/gold_merchant_common/img_pm_registration_header.png"
        const val PM_REGISTRATION_PM_PRO = "https://images.tokopedia.net/img/android/gold_merchant_common/img_pm_registration_header.png"
        const val PM_TOTAL_ORDER_TERM = "https://images.tokopedia.net/img/android/gold_merchant_common/img_pm_improve_shop_order.png"
    }

    object Urls {
        const val POWER_MERCHANT_PAGE = "https://www.tokopedia.com/myshop/power-merchant"
        const val SHOP_SCORE_INTERRUPT_PAGE = "https://www.tokopedia.com/shop-interrupt"
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