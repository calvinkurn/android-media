package com.tokopedia.product.addedit.tracking

import com.tokopedia.product.addedit.tracking.ProductVariantTracking.sendEditProductVariantDetailClick
import com.tokopedia.product.addedit.tracking.ProductVariantTracking.sendOpenProductVariantPage

object ProductEditVariantDetailTracking {
    private const val SCREEN_NAME = "/editproductpage - variant detail"

    fun trackScreen(isLoggedInStatus: String, userId: String) {
        sendOpenProductVariantPage(SCREEN_NAME, isLoggedInStatus, userId)
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

    // 3.8
    fun saveVariantDetail(shopId: String) {
        sendEditProductVariantDetailClick("click simpan variant detail", "", shopId, SCREEN_NAME)
    }
}