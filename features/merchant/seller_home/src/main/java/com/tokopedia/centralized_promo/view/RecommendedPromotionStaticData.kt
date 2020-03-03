package com.tokopedia.centralized_promo.view

import com.tokopedia.centralized_promo.view.model.RecommendedPromotionListUiModel
import com.tokopedia.centralized_promo.view.model.RecommendedPromotionUiModel
import com.tokopedia.sellerhome.R


object RecommendedPromotionStaticData {
    fun provideStaticData(): RecommendedPromotionListUiModel = RecommendedPromotionListUiModel(
            listOf(
                    RecommendedPromotionUiModel(
                            R.drawable.sh_ic_top_ads_color,
                            "TopAds",
                            "Lorem ipsum dolor sit amet consectetur\nLorem ipsum dolor sit amet consectetur",
                            "",
                            "tokopedia://topads/dashboard"
                    ),
                    RecommendedPromotionUiModel(
                            R.drawable.ic_broadcastchat,
                            "Broadcast Chat",
                            "Lorem ipsum dolor sit amet consectetur",
                            "Promo Gratis",
                            "tokopedia://webview?url=https://m.tokopedia.com/broadcast-chat/create"
                    )
            )
    )
}