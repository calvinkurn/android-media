package com.tokopedia.product.addedit.productlimitation.domain.constant

import com.tokopedia.imageassets.TokopediaImageUrl

import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp

class AddEditProductUrlConstants {
    companion object {
        const val IMAGE_URL_UPGRADE_TO_PM = TokopediaImageUrl.IMAGE_URL_UPGRADE_TO_PM
        const val IMAGE_URL_UPGRADE_TO_PM_PRO = TokopediaImageUrl.IMAGE_URL_UPGRADE_TO_PM_PRO
        const val IMAGE_URL_USE_VARIANT = TokopediaImageUrl.IMAGE_URL_USE_VARIANT
        const val IMAGE_URL_DELETE_PRODUCTS = TokopediaImageUrl.IMAGE_URL_DELETE_PRODUCTS
        const val IMAGE_URL_USE_PROMOTION = TokopediaImageUrl.IMAGE_URL_USE_PROMOTION

        val ACTION_URL_UPGRADE_TO_PM = ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE
        const val ACTION_URL_USE_VARIANT = "https://seller.tokopedia.com/edu/fitur-varian"
        val ACTION_URL_DELETE_PRODUCTS = ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST
        val ACTION_URL_USE_PROMOTION = ApplinkConstInternalSellerapp.CENTRALIZED_PROMO
        val ACTION_URL_USE_PROMOTION_WEB = "https://seller.tokopedia.com/promo"

        const val URL_PRODUCT_LIMITATION_EDU = "https://seller.tokopedia.com/edu/kuota-produk"

    }
}