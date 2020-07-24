package com.tokopedia.product.addedit.tracking

import com.tokopedia.product.addedit.tracking.ProductVariantTracking.sendEditProductVariantDetailClick
import com.tokopedia.product.addedit.tracking.ProductVariantTracking.sendOpenProductVariantPage

object ProductEditVariantDetailTracking {
    private const val SCREEN_NAME = "/editproductpage - variant detail"
    private const val CURRENT_SITE = "tokopediamarketplace"

    fun trackScreen(isLoggedInStatus: String, userId: String) {
        sendOpenProductVariantPage(SCREEN_NAME, isLoggedInStatus, userId, CURRENT_SITE)
    }

    // 3.1
    fun selectManageAll(shopId: String) {
        sendEditProductVariantDetailClick("click semua", "", shopId, SCREEN_NAME)
    }

    // 3.2
    fun continueManageAll(shopId: String) {
        sendEditProductVariantDetailClick("click lanjut", "", shopId, SCREEN_NAME)
    }

    // 3.3 label = sku
    fun trackManageAllSku(label: String, shopId: String) {
        sendEditProductVariantDetailClick("fill in sku", label, shopId, SCREEN_NAME)
    }

    // 3.4 label = price
    fun trackManageAllPrice(label: String, shopId: String) {
        sendEditProductVariantDetailClick("fill in price", label, shopId, SCREEN_NAME)
    }

    // 3.5 label = stock
    fun trackManageAllStock(label: String, shopId: String) {
        sendEditProductVariantDetailClick("fill in stock", label, shopId, SCREEN_NAME)
    }

    // 3.6 label = on/off
    fun clickSKUToggle(label: String, shopId: String) {
        sendEditProductVariantDetailClick("click toggle sku", label, shopId, SCREEN_NAME)
    }

    // 3.7 label = on/off
    fun clickVariantStatusToggle(label: String, shopId: String) {
        sendEditProductVariantDetailClick("click toggle variant status", label, shopId, SCREEN_NAME)
    }

    // 3.8
    fun saveVariantDetail(shopId: String) {
        sendEditProductVariantDetailClick("click simpan variant detail", "", shopId, SCREEN_NAME)
    }

    // 3.9 label = contains variant utama
    fun saveMainVariant(label: String, shopId: String) {
        sendEditProductVariantDetailClick("click simpan variant utama", label, shopId, SCREEN_NAME)
    }
}