package com.tokopedia.centralizedpromo.view

import com.tokopedia.centralizedpromo.view.model.PromoCreationListUiModel
import com.tokopedia.centralizedpromo.view.model.PromoCreationUiModel
import com.tokopedia.sellerhome.R


object PromoCreationStaticData {
    fun provideStaticData(broadcastChatExtra: String): PromoCreationListUiModel = PromoCreationListUiModel(
            items = arrayListOf(
                    PromoCreationUiModel(
                            R.drawable.sh_ic_top_ads_color,
                            "TopAds",
                            "Iklankan produkmu untuk menjangkau lebih banyak pembeli",
                            "",
                            "tokopedia://topads/dashboard"
                    ),
                    PromoCreationUiModel(
                            R.drawable.ic_broadcast_chat,
                            "Broadcast Chat",
                            "Tingkatkan penjualan dengan kirim pesan promosi ke pembeli",
                            broadcastChatExtra,
                            "tokopedia://webview?url=https://m.tokopedia.com/broadcast-chat/create"
                    )
            ),
            errorMessage = ""
    )
}