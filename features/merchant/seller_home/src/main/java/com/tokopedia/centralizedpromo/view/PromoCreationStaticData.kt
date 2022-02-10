package com.tokopedia.centralizedpromo.view

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.centralizedpromo.common.util.CentralizedPromoResourceProvider
import com.tokopedia.centralizedpromo.view.model.PromoCreationListUiModel
import com.tokopedia.centralizedpromo.view.model.PromoCreationUiModel
import com.tokopedia.seller.menu.common.constant.SellerMenuFreeShippingUrl
import com.tokopedia.sellerhome.R


object PromoCreationStaticData {
    fun provideStaticData(
        resourceProvider: CentralizedPromoResourceProvider,
        broadcastChatExtra: String,
        broadcastChatUrl: String,
        freeShippingEnabled: Boolean,
        isVoucherCashbackEligible: Boolean,
        isTopAdsOnBoardingEnable: Boolean
    ): PromoCreationListUiModel {
        val promoItems = mutableListOf(
            PromoCreationUiModel(
                R.drawable.sh_ic_top_ads_color,
                resourceProvider.getPromoCreationTitleTopAds(),
                resourceProvider.getPromoCreationDescriptionTopAds(),
                "",
                if (isTopAdsOnBoardingEnable){
                    ApplinkConst.SellerApp.TOPADS_ONBOARDING
                }else{
                    ApplinkConst.CustomerApp.TOPADS_DASHBOARD
                }

            ),
            PromoCreationUiModel(
                R.drawable.ic_broadcast_chat,
                resourceProvider.getPromoCreationTitleBroadcastChat(),
                resourceProvider.getPromoCreationDescriptionBroadcastChat(),
                broadcastChatExtra,
                String.format("%s?url=%s", ApplinkConst.WEBVIEW, broadcastChatUrl)
            ),
            PromoCreationUiModel(
                R.drawable.ic_voucher_cashback,
                resourceProvider.getPromoCreationTitleMerchantVoucher(),
                resourceProvider.getPromoCreationDescriptionMerchantVoucher(),
                "",
                if (isVoucherCashbackEligible) {
                    ApplinkConstInternalSellerapp.CENTRALIZED_PROMO_FIRST_VOUCHER
                } else {
                    ApplinkConstInternalSellerapp.ADMIN_RESTRICTION
                }
            )
        )

        if (freeShippingEnabled) {
            val applink = String.format(
                "%s?url=%s", ApplinkConst.WEBVIEW,
                SellerMenuFreeShippingUrl.URL_FREE_SHIPPING_INTERIM_PAGE
            )

            promoItems.add(
                PromoCreationUiModel(
                    R.drawable.ic_sah_free_shipping,
                    resourceProvider.getPromoCreationTitleFreeShipping(),
                    resourceProvider.getPromoCreationDescriptionFreeShipping(),
                    "",
                    applink
                )
            )
        }

        return PromoCreationListUiModel(
            items = promoItems,
            errorMessage = ""
        )
    }
}