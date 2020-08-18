package com.tokopedia.product.addedit.tracking

import com.tokopedia.product.addedit.tracking.ProductVariantTracking.sendAddProductVariantDetailClick
import com.tokopedia.product.addedit.tracking.ProductVariantTracking.sendOpenProductVariantPage

object ProductAddVariantDetailTracking {
    private const val SCREEN_NAME = "/addproductpage - variant detail"
    private const val CURRENT_SITE = "tokopediaseller"

    fun trackScreen(isLoggedInStatus: String, userId: String) {
        sendOpenProductVariantPage(SCREEN_NAME, isLoggedInStatus, userId, CURRENT_SITE)
    }

    // 3.1
    fun selectManageAll(shopId: String) {
        sendAddProductVariantDetailClick("click semua", "", shopId, SCREEN_NAME)
    }

    // 3.2
    fun continueManageAll(shopId: String) {
        sendAddProductVariantDetailClick("click lanjut", "", shopId, SCREEN_NAME)
    }

    // 3.3 label = price
    fun trackManageAllPrice(label: String, shopId: String) {
        sendAddProductVariantDetailClick("fill in price", label, shopId, SCREEN_NAME)
    }

    // 3.4 label = stock
    fun trackManageAllStock(label: String, shopId: String) {
        sendAddProductVariantDetailClick("fill in stock", label, shopId, SCREEN_NAME)
    }

    // 3.5 label = price
    fun trackManageAllSku(label: String, shopId: String) {
        sendAddProductVariantDetailClick("fill in sku", label, shopId, SCREEN_NAME)
    }

    // 3.6 label = on/off
    fun clickSKUToggle(label: String, shopId: String) {
        sendAddProductVariantDetailClick("click toggle sku", label, shopId, SCREEN_NAME)
    }

    // 3.7 label = on/off
    fun clickVariantStatusToggle(label: String, shopId: String) {
        sendAddProductVariantDetailClick("click toggle variant status", label, shopId, SCREEN_NAME)
    }

    // 3.8
    fun saveVariantDetail(shopId: String) {
        sendAddProductVariantDetailClick("click simpan variant detail", "", shopId, SCREEN_NAME)
    }

    // 3.9 label = contains variant utama
    fun saveMainVariant(label: String, shopId: String) {
        sendAddProductVariantDetailClick("click simpan variant utama", label, shopId, SCREEN_NAME)
    }
}