package com.tokopedia.centralizedpromo.view

import android.content.Context
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.centralizedpromo.view.model.PromoCreationListUiModel
import com.tokopedia.centralizedpromo.view.model.PromoCreationUiModel
import com.tokopedia.sellerhome.R


object PromoCreationStaticData {

    private const val BROADCAST_CHAT_URL = "https://m.tokopedia.com/broadcast-chat/create"

    fun provideStaticData(context: Context, broadcastChatExtra: String): PromoCreationListUiModel = with(context) {
        PromoCreationListUiModel(
                items = arrayListOf(
                        PromoCreationUiModel(
                                R.drawable.sh_ic_top_ads_color,
                                getString(R.string.centralized_promo_promo_creation_topads_title),
                                getString(R.string.centralized_promo_promo_creation_topads_description),
                                "",
                                ApplinkConst.CustomerApp.TOPADS_DASHBOARD
                        ),
                        PromoCreationUiModel(
                                R.drawable.ic_broadcast_chat,
                                getString(R.string.centralized_promo_promo_creation_broadcast_chat_title),
                                getString(R.string.centralized_promo_promo_creation_broadcast_chat_description),
                                broadcastChatExtra,
                                String.format("%s?url=%s", ApplinkConst.WEBVIEW, BROADCAST_CHAT_URL)
                        )
                ),
                errorMessage = ""
        )
    }
}