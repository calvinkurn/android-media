package com.tokopedia.power_merchant.subscribe.common.constant

/**
 * Created By @ilhamsuaib on 26/02/21
 */

object Constant {

    const val DATE_FORMAT_HH_MM = "HH:mm"
    const val DATE_FORMAT_EXPIRED = "yyyy-MM-dd HH:mm:ss"

    const val PM_TOP_ADS_CREDIT = "5%"
    const val PM_BROAD_CAST_CHAT = "200"
    const val PM_PRO_ADV_TOP_ADS_CREDIT = "5,5%"
    const val PM_PRO_ADV_BROAD_CAST_CHAT = "400"
    const val PM_PRO_EXP_TOP_ADS_CREDIT = "6%"
    const val PM_PRO_EXP_BROAD_CAST_CHAT = "600"
    const val PM_PRO_ULT_TOP_ADS_CREDIT = "6,5%"
    const val PM_PRO_ULT_BROAD_CAST_CHAT = "1.000"
    const val PM_SPECIAL_RELEASE = 5
    const val PM_PRO_ADV_SPECIAL_RELEASE = 10
    const val PM_PRO_EXP_SPECIAL_RELEASE = 15
    const val PM_PRO_ULT_SPECIAL_RELEASE = 20
    const val PM_PRODUCT_BUNDLING = 5
    const val PM_PRO_ADV_PRODUCT_BUNDLING = 10
    const val PM_PRO_EXP_PRODUCT_BUNDLING = 15
    const val PM_PRO_ULT_PRODUCT_BUNDLING = 25
    const val PM_FREE_DELIVERY = "3%"

    const val POWER_MERCHANT = "Power Merchant"
    const val PM_PRO_ADVANCED = "Advanced"
    const val PM_PRO_EXPERT = "Expert"
    const val PM_PRO_ULTIMATE = "Ultimate"

    const val PM_PRO_MIN_ORDER = 3
    const val PM_PRO_MIN_INCOME = 350000L

    object Url {
        const val POWER_MERCHANT_EDU = "https://seller.tokopedia.com/edu/power-merchant/"
        const val POWER_MERCHANT_FEATURES = "https://seller.tokopedia.com/edu/fitur-power-merchant"
        const val POWER_MERCHANT_PRO_EDU = "https://seller.tokopedia.com/edu/power-merchant-pro"
        const val PM_PRO_BENEFIT_PACKAGE_EDU =
            "https://seller.tokopedia.com/edu/paket-keuntungan-power-merchant-pro"
        const val POWER_MERCHANT_TERMS_AND_CONDITION =
            "https://www.tokopedia.com/help/article/syarat-dan-ketentuan-power-merchant-dan-power-merchant-pro"
        const val SHOP_PERFORMANCE_TIPS = "https://seller.tokopedia.com/edu/skor-toko"
        const val PM_PRO_IDLE =
            "https://seller.tokopedia.com/edu/status-power-merchant-pro-tidak-aktif"
        const val PM_SERVICE_FEE =
            "https://seller.tokopedia.com/edu/biaya-layanan-tokopedia"
    }

    object Image {
        const val PM_BG_UPSALE_PM_PRO =
            "https://images.tokopedia.net/img/android/gold_merchant_common/pm_bg_upsale_pm_pro_new.png"
        const val PM_BG_UPSALE_PM_PRO_INACTIVE =
            "https://images.tokopedia.net/img/android/gold_merchant_common/pm_bg_upsale_pm_pro_inactive.png"
        const val PM_BG_REGISTRATION_PM =
            "https://images.tokopedia.net/img/android/gold_merchant_common/pm_bg_registration_header_pm.png"
        const val BG_BENEFIT_PACKAGE_PM_PRO_ULTIMATE =
            "https://images.tokopedia.net/img/android/power_merchant/bg_benefit_package_pm_pro_ultimate.png"
        const val BG_BENEFIT_PACKAGE_PM_PRO_ADVANCED =
            "https://images.tokopedia.net/img/android/power_merchant/bg_benefit_package_pm_pro_advanced.png"
        const val BG_BENEFIT_PACKAGE_PM_PRO_EXPERT =
            "https://images.tokopedia.net/img/android/power_merchant/bg_benefit_package_pm_pro_expert.png"
        const val IC_PM_PRO_DOWNGRADE_WARNING =
            "https://images.tokopedia.net/img/android/power_merchant/ic_pm_pro_downgrade_warning@2x.png"
        const val IC_PM_PRO_UPGRADE_LEVEL =
            "https://images.tokopedia.net/img/android/power_merchant/ic_pm_pro_upgrade_level@2x.png"
        const val IC_PM_CASH_BACK =
            "https://images.tokopedia.net/img/android/power_merchant_subscribe/ic_pm_cash_back.png"
        const val IC_PM_FLASH_SALE =
            "https://images.tokopedia.net/img/android/power_merchant_subscribe/ic_pm_flash_sale.png"
        const val IC_PM_PRODUCT_BUNDLING =
            "https://images.tokopedia.net/img/android/power_merchant_subscribe/ic_pm_product_bundling.png"
        const val IC_PM_SPECIAL_RELEASE =
            "https://images.tokopedia.net/img/android/power_merchant_subscribe/ic_pm_special_release.png"
        const val IC_PM_TOP_ADS =
            "https://images.tokopedia.net/img/android/power_merchant_subscribe/ic_pm_topads.png"
        const val IC_PM_BROADCAST_CHAT =
            "https://images.tokopedia.net/img/android/power_merchant_subscribe/ic_pm_broadcast_chat.png"
        const val IMG_PM_REGISTRATION =
            "https://images.tokopedia.net/img/android/power_merchant_subscribe/img_pm_registration_banner.png"
    }

    object MembershipConst {
        const val PM_ORDER_THRESHOLD = 3L
        const val PM_PRO_ADVANCE_ORDER_THRESHOLD = 3L
        const val PM_PRO_EXPERT_ORDER_THRESHOLD = 50L
        const val PM_PRO_ULTIMATE_ORDER_THRESHOLD = 1500L

        const val PM_INCOME_THRESHOLD = 350000L
        const val PM_PRO_ADVANCE_INCOME_THRESHOLD = PM_INCOME_THRESHOLD
        const val PM_PRO_EXPERT_INCOME_THRESHOLD = 10000000L
        const val PM_PRO_ULTIMATE_INCOME_THRESHOLD = 300000000L

        const val PM_INCOME_THRESHOLD_FMT = "Rp350.000"
        const val PM_PRO_ADVANCE_INCOME_THRESHOLD_FMT = PM_INCOME_THRESHOLD_FMT
        const val PM_PRO_EXPERT_INCOME_THRESHOLD_FMT = "Rp10 juta"
        const val PM_PRO_ULTIMATE_INCOME_THRESHOLD_FMT = "Rp300 juta"
    }
}
