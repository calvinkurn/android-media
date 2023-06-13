package com.tokopedia.top_ads_on_boarding.data

import com.tokopedia.top_ads_on_boarding.R
import com.tokopedia.top_ads_on_boarding.model.AdsTypeEducation
import com.tokopedia.top_ads_on_boarding.model.AdsTypeEducationModel

object AdsTypeEducationData {

    fun getAdsTypeEducationList(adsType: String): MutableList<AdsTypeEducation> {
        when (adsType) {
            "productAds" -> {
                return mutableListOf(
                    AdsTypeEducationModel.Header(
                        title = "JENIS IKLAN YANG COCOK",
                        subTitle = "Iklan produk",
                        description = "Bantu calon pembeli menemukan produkmu dengan cepat lewat iklan yang muncul di hasil pencarian dan halaman rekomendasi.",
                        headerImage = Pair(
                            "topads_google_ads_type_image",
                            "https://images.tokopedia.net/img/android/res/singleDpi/topads_google_ads_type_image.png"
                        )
                    ),
                    AdsTypeEducationModel.Points(
                        points_Title = "Buat iklan dengan mudah",
                        points_Description = "Iklan ini akan menampilkan produkmu secara spesifik dan bisa pilih pengaturan otomatis supaya bisa beriklan tanpa ribet. ",
                        points_Icon = com.tokopedia.topads.common.R.drawable.icon_education_point
                    ),

                    AdsTypeEducationModel.Points(
                        points_Title = "Atur biaya iklan sesuai kebutuhan",
                        points_Description = "Dengan sistem biaya per klik, kamu hanya perlu bayar saat iklanmu diklik calon pembeli. Bisa atur biayanya sesuai budget!",
                        points_Icon = com.tokopedia.topads.common.R.drawable.icon_education_point
                    ),

                    AdsTypeEducationModel.Points(
                        points_Title = "Pantau performa iklan & penjualanmu",
                        points_Description = "Kamu bisa rutin pantau performa iklanmu untuk buat strategi beriklan yang efektif agar penjualan lebih maksimal.",
                        points_Icon = com.tokopedia.topads.common.R.drawable.icon_education_point
                    )
                )
            }
            "shopAds" -> {
                return mutableListOf(
                    AdsTypeEducationModel.Header(
                        title = "JENIS IKLAN YANG COCOK",
                        subTitle = "Iklan Toko dan TDN",
                        description = "Buat toko dan brand-mu makin dikenali dengan Iklan Toko dan TDN yang muncul di tempat-tempat strategis di pencarian dan rekomendasi.",
                        headerImage = Pair(
                            "topads_shop_ads_type",
                            "https://images.tokopedia.net/img/android/res/singleDpi/topads_shop_ads_type.png"
                        )
                    ),
                    AdsTypeEducationModel.Points(
                        points_Title = "Toko jadi makin ramai dengan iklan toko",
                        points_Description = "Iklanmu akan tampil menonjol dan eksklusif di halaman-halaman strategis untuk jangkau calon pembeli lebih luas.",
                        points_Icon = com.tokopedia.topads.common.R.drawable.icon_education_point
                    ),

                    AdsTypeEducationModel.Points(
                        points_Title = "Brand-mu jadi makin dikenali dengan TDN",
                        points_Description = "Iklanmu akan menggunakan banner promosi sehingga tampil beda & dominan untuk menarik perhatian calon pembeli.",
                        points_Icon = com.tokopedia.topads.common.R.drawable.icon_education_point
                    ),

                    AdsTypeEducationModel.Points(
                        points_Title = "Atur biaya iklan per tampil",
                        points_Description = "Kamu bisa mengontrol berapa banyak calon pembeli yang melihat iklanmu dengan pengaturan biaya per tampil.",
                        points_Icon = com.tokopedia.topads.common.R.drawable.icon_education_point
                    )
                )

            }
            else -> {
                return mutableListOf()
            }
        }
    }
}
