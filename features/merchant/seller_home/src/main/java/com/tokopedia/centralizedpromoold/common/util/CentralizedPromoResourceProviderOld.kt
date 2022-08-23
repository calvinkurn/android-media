package com.tokopedia.centralizedpromoold.common.util

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.sellerhome.R
import javax.inject.Inject

class CentralizedPromoResourceProviderOld @Inject constructor(
    @ApplicationContext private val context: Context?
) {
    private fun getString(resId: Int, vararg args: Any): String? {
        return try {
            context?.getString(resId, *args)
        } catch (e: Resources.NotFoundException) {
            null
        }
    }

    fun composeBroadcastChatFreeQuotaLabel(freeCount: Int): String {
        return getString(R.string.centralized_promo_broadcast_chat_extra_free_quota, freeCount).orEmpty()
    }

    fun getPromoCreationTitleTopAds(): String {
        return getString(R.string.centralized_promo_promo_creation_topads_title).orEmpty()
    }

    fun getPromoCreationDescriptionTopAds(): String {
        return getString(R.string.centralized_promo_promo_creation_topads_description).orEmpty()
    }

    fun getPromoCreationTitleBroadcastChat(): String {
        return getString(R.string.centralized_promo_promo_creation_broadcast_chat_title).orEmpty()
    }

    fun getPromoCreationDescriptionBroadcastChat(): String {
        return getString(R.string.centralized_promo_promo_creation_broadcast_chat_description).orEmpty()
    }

    fun getPromoCreationTitleMerchantVoucher(): String {
        return getString(R.string.centralized_promo_promo_creation_merchant_voucher_cashback_title).orEmpty()
    }

    fun getPromoCreationDescriptionMerchantVoucher(): String {
        return getString(R.string.centralized_promo_promo_creation_merchant_voucher_description).orEmpty()
    }

    fun getPromoCreationTitleFreeShipping(): String {
        return getString(R.string.centralized_promo_promo_creation_free_shipping_title).orEmpty()
    }

    fun getPromoCreationDescriptionFreeShipping(): String {
        return getString(R.string.centralized_promo_promo_creation_free_shipping_description).orEmpty()
    }

    fun getPromoCreationTitleVoucherProduct(): String {
        return getString(R.string.centralized_promo_promo_creation_voucher_product_title).orEmpty()
    }

    fun getPromoCreationDescriptionVoucherProduct(): String {
        return getString(R.string.centralized_promo_promo_creation_voucher_product_description).orEmpty()
    }

    fun getPromoCreationTitleTokopediaPlay(): String {
        return getString(R.string.centralized_promo_promo_creation_tokopedia_play_title).orEmpty()
    }

    fun getPromoCreationDescriptionTokopediaPlay(): String {
        return getString(R.string.centralized_promo_promo_creation_tokopedia_play_description).orEmpty()
    }

    fun getPromoCreationTitleTokoMember(): String {
        return getString(R.string.centralized_promo_promo_creation_tokomember_title).orEmpty()
    }

    fun getPromoCreationDescriptionTokoMember(): String {
        return getString(R.string.centralized_promo_promo_creation_tokomember_description).orEmpty()
    }

    fun getPromoCreationLabelTokoMember(): String {
        return getString(R.string.centralized_promo_promo_creation_tokomember_tag_label).orEmpty()
    }

    fun getPromoCreationTitleSlashPrice(): String {
        return getString(R.string.centralized_promo_promo_creation_slash_price_title).orEmpty()
    }

    fun getPromoCreationDescriptionSlashPrice(): String {
        return getString(R.string.centralized_promo_promo_creation_slash_price_description).orEmpty()
    }

    fun getPromoCreationLabelSlashPrice(): String {
        return getString(R.string.centralized_promo_promo_creation_slash_price_tag_label).orEmpty()
    }

    fun getPromoCreationNewInfoSlashPrice(): String {
        return getString(R.string.centralized_promo_promo_creation_slash_price_new_info).orEmpty()
    }

    fun getPromoCreationTitleFlashSaleToko(): String {
        return getString(R.string.centralized_promo_promo_creation_flash_sale_toko_title).orEmpty()
    }

    fun getPromoCreationDescFlashSaleToko(): String {
        return getString(R.string.centralized_promo_promo_creation_flash_sale_toko_description).orEmpty()
    }

    fun getPromoCreationLabelFlashSaleToko(): String {
        return getString(R.string.centralized_promo_promo_creation_flash_sale_toko_tag_label).orEmpty()
    }

    fun getPromoCreationNewInfoFlashSaleToko(): String {
        return getString(R.string.centralized_promo_promo_creation_flash_sale_toko_new_info).orEmpty()
    }

}