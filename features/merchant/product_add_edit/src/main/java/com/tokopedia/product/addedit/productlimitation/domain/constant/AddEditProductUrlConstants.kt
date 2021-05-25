package com.tokopedia.product.addedit.productlimitation.domain.constant

import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp

class AddEditProductUrlConstants {
    companion object {
        const val IMAGE_URL_UPGRADE_TO_PM = "https://images.tokopedia.net/img/android/merchant/add-edit-product/product_limitation_pm.png"
        const val IMAGE_URL_USE_VARIANT = "https://images.tokopedia.net/img/android/merchant/add-edit-product/product_limitation_variant.png"
        const val IMAGE_URL_DELETE_PRODUCTS = "https://images.tokopedia.net/img/android/merchant/add-edit-product/product_limitation_deleting.png"
        const val IMAGE_URL_USE_PROMOTION = "https://images.tokopedia.net/img/android/merchant/add-edit-product/product_limitation_ads.png"

        val ACTION_URL_UPGRADE_TO_PM = ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE
        const val ACTION_URL_USE_VARIANT = "https://seller.tokopedia.com/edu/fitur-varian"
        val ACTION_URL_DELETE_PRODUCTS = ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST
        val ACTION_URL_USE_PROMOTION = ApplinkConstInternalSellerapp.CENTRALIZED_PROMO
        val ACTION_URL_USE_PROMOTION_WEB = "https://seller.tokopedia.com/promo"

        const val URL_PRODUCT_LIMITATION_EDU = "https://seller.tokopedia.com/edu/kuota-produk"

    }
}