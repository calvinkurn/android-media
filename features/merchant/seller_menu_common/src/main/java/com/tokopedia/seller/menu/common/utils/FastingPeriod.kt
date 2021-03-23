package com.tokopedia.seller.menu.common.utils

enum class FastingPeriod(val regularMerchantIllustrationUrl: String,
                         val powerMerchantIllustrationUrl: String,
                         val officialStoreIllustrationUrl: String) {
    SUHOOR("https://images.tokopedia.net/img/android/seller_home/ic_ramadhan_sahur_rm.png",
            "https://images.tokopedia.net/img/android/seller_home/ic_ramadhan_sahur_pm.png",
            "https://images.tokopedia.net/img/android/seller_home/ic_ramadhan_sahur_os.png"),
    FASTING("https://images.tokopedia.net/img/android/seller_home/ic_ramadhan_puasa_rm.png",
            "https://images.tokopedia.net/img/android/seller_home/ic_ramadhan_puasa_pm.png",
            "https://images.tokopedia.net/img/android/seller_home/ic_ramadhan_puasa_os.png"),
    IFTAR("https://images.tokopedia.net/img/android/seller_home/ic_ramadhan_buka_rm.png",
            "https://images.tokopedia.net/img/android/seller_home/ic_ramadhan_buka_pm.png",
            "https://images.tokopedia.net/img/android/seller_home/ic_ramadhan_buka_os.png")
}