package com.tokopedia.centralizedpromo.view

import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.centralizedpromo.view.model.PromoCreationListUiModel
import com.tokopedia.centralizedpromo.view.model.PromoCreationUiModel
import com.tokopedia.sellerhome.R


object PromoCreationStaticData {
    fun provideStaticData(): PromoCreationListUiModel = PromoCreationListUiModel(
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
                            "",
                            "tokopedia://webview?url=https://m.tokopedia.com/broadcast-chat/create"
                    ),
                    PromoCreationUiModel(
                            R.drawable.ic_broadcast_chat,
                            "Voucher Toko - Create",
                            "Kelik di sini gan/sis untuk liat voucher creation",
                            "",
                            ApplinkConstInternalSellerapp.CENTRALIZED_PROMO_FIRST_VOUCHER
                    ),
                    PromoCreationUiModel(
                            R.drawable.ic_broadcast_chat,
                            "Voucher Toko - List",
                            "Kelik di sini gan/sis untuk liat voucher list",
                            "",
                            ApplinkConstInternalSellerapp.VOUCHER_LIST
                    )
            ),
            errorMessage = ""
    )
}