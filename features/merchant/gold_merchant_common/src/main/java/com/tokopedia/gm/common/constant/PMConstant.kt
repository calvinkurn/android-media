package com.tokopedia.gm.common.constant

import com.tokopedia.imageassets.TokopediaImageUrl

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform

/**
 * Created By @ilhamsuaib on 21/03/21
 */

object PMConstant {

    const val PM_SETTING_INFO_SOURCE = "power-merchant-subscription-android-ui"
    const val APP_UPDATE_ERROR_CODE = "err.validate.app_outdated"

    object Images {
        const val PM_BADGE =
            "https://images.tokopedia.net/img/goldmerchant/pm_activation/badge/Power%20Merchant@3x.png"
        const val PM_BADGE_INACTIVE = TokopediaImageUrl.PM_BADGE_INACTIVE
        const val PM_SHOP_ICON = TokopediaImageUrl.PM_SHOP_ICON
        const val PM_PRO_BADGE = TokopediaImageUrl.PM_PRO_BADGE
        const val PM_NEW_REQUIREMENT = TokopediaImageUrl.PM_NEW_REQUIREMENT
        const val PM_ADD_PRODUCT_BOTTOM_SHEET = TokopediaImageUrl.PM_ADD_PRODUCT_BOTTOM_SHEET
        const val PM_TOTAL_ORDER_TERM = TokopediaImageUrl.PM_TOTAL_ORDER_TERM
        const val IMG_TOPED_PM_ACTIVE = TokopediaImageUrl.IMG_TOPED_PM_ACTIVE
        const val IMG_TOPED_PM_INACTIVE = TokopediaImageUrl.IMG_TOPED_PM_INACTIVE
        const val IMG_TOPED_PM_PRO_ACTIVE = TokopediaImageUrl.IMG_TOPED_PM_PRO_ACTIVE
        const val IMG_TOPED_NEW_SELLER_PM_ACTIVE = TokopediaImageUrl.IMG_TOPED_NEW_SELLER_PM_ACTIVE
        const val IMG_TOPED_NEW_SELLER_PM_PRO_ACTIVE = TokopediaImageUrl.IMG_TOPED_NEW_SELLER_PM_PRO_ACTIVE
        const val PM_POTENTIAL_BENEFIT_01 = TokopediaImageUrl.PM_POTENTIAL_BENEFIT_01
        const val PM_POTENTIAL_BENEFIT_02 = TokopediaImageUrl.PM_TOTAL_ORDER_TERM
        const val PM_POTENTIAL_BENEFIT_03 = TokopediaImageUrl.PM_POTENTIAL_BENEFIT_03
        const val PM_MODERATED_SHOP = TokopediaImageUrl.PM_MODERATED_SHOP
    }

    object AppLink {
        private const val MERCHANT_KYC_PROJECT_ID = 10
        private const val APPLINK_PARAMS_KYC_PM = "${ApplinkConstInternalUserPlatform.PARAM_PROJECT_ID}=${MERCHANT_KYC_PROJECT_ID}"
        const val KYC_POWER_MERCHANT = "${ApplinkConst.KYC_NO_PARAM}?$APPLINK_PARAMS_KYC_PM"
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

    object ShopLevel {
        const val ONE = 1
        const val TWO = 2
        const val THREE = 3
        const val FOUR = 4
    }

    object ShopGrade {
        const val PM = "power merchant"
        const val PRO_ADVANCE = "advanced"
        const val PRO_EXPERT = "expert"
        const val PRO_ULTIMATE = "ultimate"
    }

    object EligibleShopGrade {
        const val PRO_ADVANCE = "PM PRO Advanced"
        const val PRO_EXPERT = "PM PRO Expert"
        const val PRO_ULTIMATE = "PM PRO Ultimate"
    }
}
