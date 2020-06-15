package com.tokopedia.centralizedpromo.view

import android.content.Context
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.centralizedpromo.constant.CentralizedPromoUrl
import com.tokopedia.centralizedpromo.view.model.PromoCreationListUiModel
import com.tokopedia.centralizedpromo.view.model.PromoCreationUiModel
import com.tokopedia.sellerhome.R


object PromoCreationStaticData {
    fun provideStaticData(
        context: Context,
        broadcastChatExtra: String,
        broadcastChatUrl: String,
        freeShippingEnabled: Boolean
    ): PromoCreationListUiModel = with(context) {
        val promoItems = mutableListOf(
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
                String.format("%s?url=%s", ApplinkConst.WEBVIEW, broadcastChatUrl)
            )
        )

        if(freeShippingEnabled) {
            val applink = String.format("%s?url=%s", ApplinkConst.WEBVIEW,
                CentralizedPromoUrl.URL_FREE_SHIPPING_LEARN_MORE)

            promoItems.add(PromoCreationUiModel(
                R.drawable.ic_sah_free_shipping,
                getString(R.string.centralized_promo_promo_creation_free_shipping_title),
                getString(R.string.centralized_promo_promo_creation_free_shipping_description),
                "",
                applink
            ))
        }

        PromoCreationListUiModel(
                items = promoItems,
                errorMessage = ""
        )
    }
}