package com.tokopedia.seller.menu.common.utils

/**
 * Figma: https://www.figma.com/file/5uH3Pi5GbB0jmx7uo3IZJh/UI---Seller-App-Ramadhan?node-id=90%3A4
 * Note: Illustration links for OS are not quite right with their file naming, but essentially the illustrations are correct
 */
enum class FastingPeriod(val regularMerchantIllustrationUrl: String,
                         val powerMerchantIllustrationUrl: String,
                         val officialStoreIllustrationUrl: String) {
    SUHOOR("https://images.tokopedia.net/img/android/seller_home/ic_ramadhan_sahur_rm.png",
            "https://images.tokopedia.net/img/android/seller_home/ic_ramadhan_sahur_pm.png",
            "https://images.tokopedia.net/img/android/seller_home/ic_ramadhan_puasa_os.png"),
    FASTING("https://images.tokopedia.net/img/android/seller_home/ic_ramadhan_puasa_rm.png",
            "https://images.tokopedia.net/img/android/seller_home/ic_ramadhan_puasa_pm.png",
            "https://images.tokopedia.net/img/android/seller_home/ic_ramadhan_buka_os.png"),
    IFTAR("https://images.tokopedia.net/img/android/seller_home/ic_ramadhan_buka_rm.png",
            "https://images.tokopedia.net/img/android/seller_home/ic_ramadhan_buka_pm.png",
            "https://images.tokopedia.net/img/android/seller_home/ic_ramadhan_sahur_os.png")
}