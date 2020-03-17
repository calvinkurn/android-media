package com.tokopedia.centralizedpromo.view

import com.tokopedia.centralizedpromo.view.model.PromoCreationListUiModel
import com.tokopedia.centralizedpromo.view.model.PromoCreationUiModel
import com.tokopedia.sellerhome.R


object PromoCreationStaticData {
    fun provideStaticData(): PromoCreationListUiModel = PromoCreationListUiModel(
            items = arrayListOf(
                    PromoCreationUiModel(
                            R.drawable.sh_ic_top_ads_color,
                            "TopAds",
                            "Increase conversion and new buyers through review given",
                            "100 kuota gratis",
                            "tokopedia://topads/dashboard"
                    ),
                    PromoCreationUiModel(
                            R.drawable.ic_broadcast_chat,
                            "Broadcast Chat",
                            "Increase conversion and new buyers through review given",
                            "",
                            "tokopedia://webview?url=https://m.tokopedia.com/broadcast-chat/create"
                    )
            ),
            errorMessage = ""
    )
}