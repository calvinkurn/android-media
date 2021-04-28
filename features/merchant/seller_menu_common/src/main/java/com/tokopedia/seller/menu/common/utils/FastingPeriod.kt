package com.tokopedia.seller.menu.common.utils

/**
 * Figma: https://www.figma.com/file/5uH3Pi5GbB0jmx7uo3IZJh/UI---Seller-App-Ramadhan?node-id=90%3A4
 */
enum class FastingPeriod(val regularMerchantIllustrationUrl: String,
                         val powerMerchantIllustrationUrl: String,
                         val officialStoreIllustrationUrl: String) {
    SUHOOR("https://images.tokopedia.net/img/android/seller_home/ic_ramadhan_sahur_rm_2x.png",
            "https://images.tokopedia.net/img/android/seller_home/ic_ramadhan_sahur_pm_2x.png",
            "https://images.tokopedia.net/img/android/seller_home/ic_ramadhan_sahur_os_2x.png"),
    FASTING("https://images.tokopedia.net/img/android/seller_home/ic_ramadhan_puasa_rm_2x.png",
            "https://images.tokopedia.net/img/android/seller_home/ic_ramadhan_puasa_pm_2x.png",
            "https://images.tokopedia.net/img/android/seller_home/ic_ramadhan_puasa_os_2x.png"),
    IFTAR("https://images.tokopedia.net/img/android/seller_home/ic_ramadhan_buka_rm_2x.png",
            "https://images.tokopedia.net/img/android/seller_home/ic_ramadhan_buka_pm_2x.png",
            "https://images.tokopedia.net/img/android/seller_home/ic_ramadhan_buka_os_2x.png")
}