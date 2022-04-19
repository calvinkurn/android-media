package com.tokopedia.top_ads_on_boarding.data

import com.tokopedia.applink.ApplinkConst.SellerApp.TOPADS_CREATE_MANUAL_ADS
import com.tokopedia.applink.ApplinkConst.SellerApp.TOPADS_HEADLINE_CREATE
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.top_ads_on_boarding.R
import com.tokopedia.top_ads_on_boarding.constant.TopAdsOnBoardingConstant.ADS_TYPE_PRODUCT
import com.tokopedia.top_ads_on_boarding.constant.TopAdsOnBoardingConstant.ADS_TYPE_SHOP
import com.tokopedia.top_ads_on_boarding.model.AdsTypeModel
import javax.inject.Inject

class AdsTypeData @Inject constructor() {

    fun getAdsTypeList(adsType: String): ArrayList<AdsTypeModel> {
        when (adsType) {
            ADS_TYPE_PRODUCT -> {
                return arrayListOf(
                    AdsTypeModel(
                        adsTypeTitle = "Iklan Otomatis",
                        adsTypeSubTitle = "Cukup atur anggaran iklan aja",
                        adsTypeIcon = R.drawable.icon_aotomatic_ads,
                        adsTypeImage = Pair(
                            "topads_automatic_ad_type_image",
                            "https://images.tokopedia.net/img/android/res/singleDpi/topads_automatic_ad_type_image.png"
                        ),
                        adsTypeDescription =
                        "Hanya dengan 1x klik, iklanmu bisa langsung aktif. Pasang iklan jadi cepat tanpa ribet.",
                        adsTypePositiveButton = "Buat Iklan Otomatis",
                        adsTypeNegativeButton = "Lihat Cara Pakainya",
                        positiveButtonLink = ApplinkConstInternalTopAds.TOPADS_AUTOADS_CREATE,
                        negativeButtonLink = "https://seller.tokopedia.com/edu/tambah-iklan-otomatis-topads"
                    ),
                    AdsTypeModel(
                        adsTypeTitle = "Iklan Manual",
                        adsTypeSubTitle = "Atur produk & anggaran sesukamu",
                        adsTypeIcon = R.drawable.icon_manual_ads,
                        adsTypeImage = Pair(
                            "topads_manual_ads_type",
                            "https://images.tokopedia.net/img/android/res/singleDpi/topads_manual_ads_type.png"
                        ),
                        adsTypeDescription =
                        "Bebas atur produk yang diiklankan, biaya iklannya, hingga kata kunci sesuai strategi beriklanmu.",
                        adsTypePositiveButton = "Buat Iklan Manual",
                        adsTypeNegativeButton = "Lihat Cara Pakainya",
                        positiveButtonLink = TOPADS_CREATE_MANUAL_ADS,
                        negativeButtonLink = "https://seller.tokopedia.com/edu/iklan-manual-baru"
                    )
                )
            }
            ADS_TYPE_SHOP -> {
                return arrayListOf(
                    AdsTypeModel(
                        adsTypeTitle = "Iklan Toko",
                        adsTypeSubTitle = "Tampil menonjol dan teratas",
                        adsTypeIcon = R.drawable.icon_shop_ads,
                        adsTypeImage = Pair(
                            "topads_shop_ads_type",
                            "https://images.tokopedia.net/img/android/res/singleDpi/topads_shop_ads_type.png"
                        ),
                        adsTypeDescription = "Produk dan tokomu akan tampil di bagian teratas halaman pencarian & halaman strategis lainnya.",
                        adsTypePositiveButton = "Buat Iklan Toko",
                        adsTypeNegativeButton = "Lihat Cara Pakainya",
                        positiveButtonLink = TOPADS_HEADLINE_CREATE,
                        negativeButtonLink = "https://seller.tokopedia.com/edu/topads-iklan-toko"
                    ),
                    AdsTypeModel(
                        adsTypeTitle = "Display Network (TDN)",
                        adsTypeSubTitle = "Tampil dominan di semua halaman",
                        adsTypeIcon = R.drawable.icon_disabled_tdn,
                        adsTypeImage = Pair(
                            "topads_ads_type_tdn_disabled",
                            "https://images.tokopedia.net/img/android/res/singleDpi/topads_ads_type_tdn_disabled.png"
                        ),
                        adsTypeDescription = "Upgrade tokomu jadi Official Store atau Power Merchant supaya bisa menggunakan iklan TDN.",
                        adsTypePositiveButton = "Buat Iklan TDN",
                        adsTypeNegativeButton = "Pelajari Selengkapnya",
                        negativeButtonLink = "https://seller.tokopedia.com/edu/tokopedia-display-network",
                        isAdEnable = false
                    )
                )
            }
            else -> {
                return arrayListOf()
            }
        }
    }
}
