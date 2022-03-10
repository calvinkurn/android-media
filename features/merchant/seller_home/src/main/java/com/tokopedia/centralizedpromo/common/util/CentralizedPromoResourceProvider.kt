package com.tokopedia.centralizedpromo.common.util

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.sellerhome.R
import javax.inject.Inject

class CentralizedPromoResourceProvider @Inject constructor(
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

}