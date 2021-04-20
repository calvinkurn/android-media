package com.tokopedia.product.addedit.tracking

import com.tokopedia.product.addedit.tracking.ProductVariantTracking.sendEditProductVariantClick
import com.tokopedia.product.addedit.tracking.ProductVariantTracking.sendOpenProductVariantPage

object ProductEditVariantTracking {
    private const val SCREEN_NAME = "/editproductpage - variant"
    private const val CURRENT_SITE = "tokopediamarketplace"

    fun trackScreen(isLoggedInStatus: String, userId: String) {
        sendOpenProductVariantPage(SCREEN_NAME, isLoggedInStatus, userId, CURRENT_SITE)
    }

    // 2.1 label = variant type
    fun selectVariantType(label: String, shopId: String) {
        sendEditProductVariantClick("click variant type", label, shopId, SCREEN_NAME)
    }

    // 2.11
    fun confirmProductVariantReset(shopId: String) {
        sendEditProductVariantClick("click hapus semua variant", "", shopId, SCREEN_NAME)
    }

    // 2.12 label = variant type
    fun confirmVariantTypeCancellation(label: String, shopId: String) {
        sendEditProductVariantClick("click unselect variant type", label, shopId, SCREEN_NAME)
    }

    // 2.13
    fun continueToVariantDetailPage(shopId: String) {
        sendEditProductVariantClick("click lanjut", "", shopId, SCREEN_NAME)
    }

    // 2.2 label = variant type
    fun addingVariantDetailValue(label: String, shopId: String) {
        sendEditProductVariantClick("click tambah variant", label, shopId, SCREEN_NAME)
    }

    // 2.3 label = variant type
    fun selectingVariantUnit(label: String, shopId: String) {
        sendEditProductVariantClick("click variant family", label, shopId, SCREEN_NAME)
    }

    // 2.4 label = variant type - variant unit value
    fun selectVariantUnitValue(label: String, shopId: String) {
        sendEditProductVariantClick("click select variant type value", label, shopId, SCREEN_NAME)
    }

    // 2.5 label = variant type - variant unit values counter
    fun saveVariantUnitValues(label: String, shopId: String) {
        sendEditProductVariantClick("click simpan variant type values", label, shopId, SCREEN_NAME)
    }

    // 2.6
    fun pickSizeChartImage(shopId: String) {
        sendEditProductVariantClick("click done size product", "", shopId, SCREEN_NAME)
    }

    // 2.7 label = variant type - variant unit value
    fun saveCustomVariantUnitValue(label: String, shopId: String) {
        sendEditProductVariantClick("click simpan custom variant value", label, shopId, SCREEN_NAME)
    }

    // 2.8
    fun pickProductVariantPhotos(shopId: String) {
        sendEditProductVariantClick("click done image product", "", shopId, SCREEN_NAME)
    }

    // 2.9 label = variant type - variant unit value
    fun removeVariantUnitValue(label: String, shopId: String) {
        sendEditProductVariantClick("click unselect variant type value", label, shopId, SCREEN_NAME)
    }
}