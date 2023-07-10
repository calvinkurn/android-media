package com.tokopedia.topads.dashboard.recommendation.data.model.local.data

import com.tokopedia.applink.ApplinkConst.SellerApp.TOPADS_AUTO_TOPUP
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.PARAM_PRODUK_IKLAN
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.SEARCH_REPORT_EDU_URL
import com.tokopedia.topads.dashboard.recommendation.data.model.local.EmptyStatesUiModel

object EmptyStateData {

    fun getData(): List<EmptyStatesUiModel> {
        return listOf(
            EmptyStatesUiModel(
                heading = "Tetap cek secara rutin atau coba gunakan fitur ini:",
                imageUrl = "https://images.tokopedia.net/img/img/android/res/singleDpi/top_ads_saran_top_ads_empty_state.png",
                stateType = "Saran TopAds",
                stateTypeDescription = "Fitur yang menyediakan rekomendasi saat iklanmu masih bisa dimaksimalkan untuk hasil yang lebih baik.",
                buttonText = ""
            ),
            EmptyStatesUiModel(
                heading = "Tetap cek secara rutin atau coba gunakan fitur ini:",
                imageUrl = "https://images.tokopedia.net/img/img/android/topads/insight_center_page/insight_center_empty_state_two.png",
                stateType = "Laporan Pencarian",
                stateTypeDescription = "Temukan kata pencarian apa saja yang menampilkan produkmu dengan fitur ini.",
                buttonText = "Pelajari Fitur",
                landingUrl = SEARCH_REPORT_EDU_URL
            ),
            EmptyStatesUiModel(
                heading = "Tetap cek secara rutin atau coba gunakan fitur ini:",
                imageUrl = "https://images.tokopedia.net/img/img/img/android/topads/insight_center_page/insight_centre_empty_state_three.png",
                stateType = "Biaya Iklan di Pencarian dan Rekomendasi",
                stateTypeDescription = "Maksimalkan performa iklan dengan atur Biaya Iklan di Pencarian & Biaya Iklan di Rekomendasi.",
                buttonText = "Coba Fitur",
                landingUrl = "$PARAM_PRODUK_IKLAN"
            ),
            EmptyStatesUiModel(
                heading = "Tetap cek secara rutin atau coba gunakan fitur ini:",
                imageUrl = "https://images.tokopedia.net/img/android/topads/insight_center_page/insight_centre_empty_state_four.png",
                stateType = "Biaya Iklan Otomatis",
                stateTypeDescription = "TopAds akan membantu mengatur biaya iklan dan kata kunci untuk memaksimalkan penjualanmu.",
                buttonText = "Coba Fitur",
                landingUrl = "$PARAM_PRODUK_IKLAN"
            ),
            EmptyStatesUiModel(
                heading = "Tetap cek secara rutin atau coba gunakan fitur ini:",
                imageUrl = "https://images.tokopedia.net/img/android/topads/insight_center_page/insight_centre_empty_state_five.png",
                stateType = "Tambah Kredit Otomatis",
                stateTypeDescription = "Agar iklan terus tampil, Kredit TopAds akan diisi secara otomatis saat sisa kredit tinggal sedikit.",
                buttonText = "Coba Fitur",
                landingUrl = TOPADS_AUTO_TOPUP
            )
        )
    }
}
