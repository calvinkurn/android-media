package com.tokopedia.centralizedpromo.view

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.sellerhome.SellerHomeApplinkConst
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
        isTopAdsOnBoardingEnable: Boolean,
        isVoucherCashbackFirstTime: Boolean,
        isProductCouponFirstTime: Boolean,
        isTokopediaPlayFirstTime: Boolean,
        isProductCouponEnabled: Boolean,
    ): PromoCreationListUiModel {
        val promoItems = mutableListOf(
            PromoCreationUiModel(
                R.drawable.ic_tokopedia_play,
                resourceProvider.getPromoCreationTitleTokopediaPlay(),
                resourceProvider.getPromoCreationDescriptionTokopediaPlay(),
                "",
                if (isTokopediaPlayFirstTime) {
                    getFirstTimeApplink(SellerHomeApplinkConst.TYPE_TOKOPEDIA_PLAY)
                } else {
                    ApplinkConstInternalContent.INTERNAL_PLAY_BROADCASTER
                }
            ),
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
                    if (isVoucherCashbackFirstTime) {
                        getFirstTimeApplink(SellerHomeApplinkConst.TYPE_VOUCHER_CASHBACK)
                    } else {
                        ApplinkConstInternalSellerapp.CREATE_VOUCHER
                    }
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

        if (isProductCouponEnabled) {
            val productCouponApplink =
                if (isProductCouponFirstTime) {
                    getFirstTimeApplink(SellerHomeApplinkConst.TYPE_VOUCHER_PRODUCT)
                } else {
                    ApplinkConst.SellerApp.CREATE_VOUCHER_PRODUCT
                }
            promoItems.add(
                PromoCreationUiModel(
                    R.drawable.ic_sah_voucher_product,
                    resourceProvider.getPromoCreationTitleVoucherProduct(),
                    resourceProvider.getPromoCreationDescriptionVoucherProduct(),
                    "",
                    productCouponApplink
                )
            )
        }

        return PromoCreationListUiModel(
            items = promoItems,
            errorMessage = ""
        )
    }

    private fun getFirstTimeApplink(promoType: String): String =
        Uri.parse(ApplinkConstInternalSellerapp.CENTRALIZED_PROMO_FIRST_TIME).buildUpon()
            .appendQueryParameter(SellerHomeApplinkConst.PROMO_TYPE, promoType)
            .build().toString()

}