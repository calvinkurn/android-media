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
                lottieUrl = "https://assets.tokopedia.net/asts/ta/insight/empty-state-1-saran-topads.json",
                stateType = "Saran TopAds",
                stateTypeDescription = "Fitur yang menyediakan rekomendasi saat iklanmu masih bisa dimaksimalkan untuk hasil yang lebih baik.",
                buttonText = ""
            ),
            EmptyStatesUiModel(
                heading = "Tetap cek secara rutin atau coba gunakan fitur ini:",
                lottieUrl = "https://assets.tokopedia.net/asts/ta/insight/empty-state-2-laporan-pencarian.json",
                stateType = "Laporan Pencarian",
                stateTypeDescription = "Temukan kata pencarian apa saja yang menampilkan produkmu dengan fitur ini.",
                buttonText = "Pelajari Fitur",
                landingUrl = SEARCH_REPORT_EDU_URL
            ),
            EmptyStatesUiModel(
                heading = "Tetap cek secara rutin atau coba gunakan fitur ini:",
                lottieUrl = "https://assets.tokopedia.net/asts/ta/insight/empty-state-3-split-bid.json",
                stateType = "Biaya Iklan di Pencarian dan Rekomendasi",
                stateTypeDescription = "Biaya Iklan di Pencarian dan Rekomendasi",
                buttonText = "Coba Fitur",
                landingUrl = "$PARAM_PRODUK_IKLAN"
            ),
            EmptyStatesUiModel(
                heading = "Tetap cek secara rutin atau coba gunakan fitur ini:",
                lottieUrl = "https://assets.tokopedia.net/asts/ta/insight/empty-state-4-auto-bid.json",
                stateType = "Biaya Iklan Otomatis",
                stateTypeDescription = "TopAds akan membantu mengatur biaya iklan dan kata kunci untuk memaksimalkan penjualanmu.",
                buttonText = "Coba Fitur",
                landingUrl = "$PARAM_PRODUK_IKLAN"
            ),
            EmptyStatesUiModel(
                heading = "Tetap cek secara rutin atau coba gunakan fitur ini:",
                lottieUrl = "https://assets.tokopedia.net/asts/ta/insight/empty-state-5-auto-topup.json",
                stateType = "Tambah Kredit Otomatis",
                stateTypeDescription = "Agar iklan terus tampil, Kredit TopAds akan diisi secara otomatis saat sisa kredit tinggal sedikit.",
                buttonText = "Coba Fitur",
                landingUrl = TOPADS_AUTO_TOPUP
            )
        )
    }
}
