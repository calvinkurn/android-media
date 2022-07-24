package com.tokopedia.centralizedpromo.view

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.sellerhome.SellerHomeApplinkConst
import com.tokopedia.centralizedpromo.common.util.CentralizedPromoResourceProvider
import com.tokopedia.centralizedpromo.view.model.PromoCreationListUiModel
import com.tokopedia.centralizedpromo.view.model.PromoCreationUiModel
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.seller.menu.common.constant.SellerMenuFreeShippingUrl
import com.tokopedia.sellerhome.R


object PromoCreationStaticData {

    private const val SELLER_ADMIN_ARTICLE = "https://seller.tokopedia.com/edu/fitur-admin-toko/"

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
        isSlashPriceEnabled: Boolean,
        isSlashPriceEligible: Boolean
    ): PromoCreationListUiModel {
        val promoItems = mutableListOf(
            PromoCreationUiModel(
                R.drawable.ic_sah_tokomember,
                resourceProvider.getPromoCreationTitleTokoMember(),
                resourceProvider.getPromoCreationDescriptionTokoMember(),
                String.EMPTY,
                ApplinkConst.SellerApp.TOKOMEMBER,
                resourceProvider.getPromoCreationLabelTokoMember()
            ),
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
            )
        )

        if (isSlashPriceEnabled) {
            val slashPriceApplink =
                if (isSlashPriceEligible) {
                    ApplinkConstInternalSellerapp.SHOP_DISCOUNT
                } else {
                    getSlashPriceApplink()
                }
            promoItems.add(
                PromoCreationUiModel(
                    R.drawable.ic_sah_slash_price,
                    resourceProvider.getPromoCreationTitleSlashPrice(),
                    resourceProvider.getPromoCreationDescriptionSlashPrice(),
                    "",
                    slashPriceApplink
                )
            )
        }

        promoItems.addAll(
            listOf(
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

    private fun getSlashPriceApplink(): String {
        return UriUtil.buildUriAppendParam(
            uri = ApplinkConstInternalSellerapp.ADMIN_RESTRICTION,
            queryParameters = mapOf(
                ApplinkConstInternalSellerapp.PARAM_ARTICLE_URL to SELLER_ADMIN_ARTICLE
            )
        )
    }

}