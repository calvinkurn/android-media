package com.tokopedia.product.addedit.tracking

import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.sendEditProductClick
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.sendEditProductPgIrisClickEvent
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.sendEditProductPgIrisViewEvent

object ProductEditMainTracking {
    const val SCREEN = "/addproductpage - main"

    fun trackScreen() {
        ProductAddEditTracking.getTracker().sendScreenAuthenticated(SCREEN)
    }

    fun trackBack(shopId: String) {
        sendEditProductClick(shopId, "click back on main page")
    }

    fun trackAddPhoto(shopId: String) {
        sendEditProductClick(shopId, "click add product photo")
    }

    fun trackRemovePhoto(shopId: String) {
        sendEditProductClick(shopId, "click remove product image")
    }

    fun trackDragPhoto(shopId: String) {
        sendEditProductClick(shopId, "click drag product image")
    }

    fun clickOtherCategory(shopId: String) {
        sendEditProductClick(shopId, "click choose other categories")
    }

    fun clickBackOtherCategory(shopId: String) {
        sendEditProductClick(shopId, "click back choose other categories")
    }

    fun clickSaveOtherCategory(shopId: String) {
        sendEditProductClick(shopId, "click save choose other categories")
    }

    fun clickWholesale(shopId: String) {
        sendEditProductClick(shopId, "click wholesale button")
    }

    fun clickRemoveWholesale(shopId: String) {
        sendEditProductClick(shopId, "click remove wholesale price")
    }

    fun clickAddWholesale(shopId: String) {
        sendEditProductClick(shopId, "click add wholesale price")
    }

    fun clickBackOnVariantPage(shopId: String) {
        sendEditProductClick(shopId, "click back on variant page")
    }

    fun clickWholesaleOnVariantPage(shopId: String) {
        sendEditProductClick(shopId, "click add wholesale price on variant page")
    }

    fun clickSaveVariantPage(shopId: String) {
        sendEditProductClick(shopId, "click save on variant page")
    }

    fun clickPreorderButton(shopId: String) {
        sendEditProductClick(shopId, "click preorder button")
    }

    fun clickPreorderDropDownMenu(shopId: String) {
        sendEditProductClick(shopId, "click preorder dropdown menu")
    }

    fun clickCancelPreOrderDuration(shopId: String) {
        sendEditProductClick(shopId, "click cancel preorder duration")
    }

    fun clickPreOrderDuration(shopId: String, isDay: Boolean) {
        sendEditProductClick(shopId, "click preorder duration", if (isDay) {
            "day"
        } else {
            "week"
        })
    }

    fun clickContinue(shopId: String) {
        sendEditProductClick(shopId, "click continue on main page")
    }

    fun sendImpressionPriceSuggestionEntryPointEvent(productId: String) {
        sendEditProductPgIrisViewEvent(
                action = "impression price suggestion entry point",
                label = "{$productId} - {Parent}",
                trackerId = "35684"
        )
    }

    fun sendClickPriceSuggestionEntryPointEvent(productId: String) {
        sendEditProductPgIrisClickEvent(
                action = "click price suggestion entry point",
                label = "{$productId} - {Parent}",
                trackerId = "35685"
        )
    }

    fun sendClickPriceSuggestionPopUpApplyEvent(
            productId: String,
            currentPrice: String,
            suggestedPrice: String,
            priceRange: String
    ) {
        sendEditProductPgIrisClickEvent(
                action = "click price suggestion pop up - apply",
                label = "{$productId} - {$currentPrice} - {$suggestedPrice} - {$priceRange} - {Parent}",
                trackerId = "35686"
        )
    }

    fun sendClickPriceSuggestionPopUpAboutPriceSuggestionEvent() {
        sendEditProductPgIrisClickEvent(
                action = "click price suggestion pop up - about price suggestion",
                label = "",
                trackerId = "35687"
        )
    }

    fun sendClickPriceSuggestionPopUpGiveFeedbackEvent(productId: String) {
        sendEditProductPgIrisClickEvent(
                action = "click price suggestion pop up - give feedback",
                label = "{$productId} - {Parent}",
                trackerId = "35688"
        )
    }

    fun sendClickPriceSuggestionPopUpLearnMoreEvent(productId: String) {
        sendEditProductPgIrisClickEvent(
                action = "click price suggestion pop up - learn more",
                label = "{$productId} - {Parent}",
                trackerId = "35689"
        )
    }

    fun sendClickPriceSuggestionPopUpEditPriceEvent(
            productId: String,
            currentPrice: String,
            suggestedPrice: String,
            priceRange: String,
            updatedPrice: String,
    ) {
        sendEditProductPgIrisClickEvent(
                action = "click price suggestion pop up - edit price",
                label = "{$productId} - {$currentPrice} - {$suggestedPrice} - {$priceRange} - {$updatedPrice} - {Parent}",
                trackerId = "35690"
        )
    }

    fun sendClickPriceSuggestionPopUpSimilarProductEvent(row: String) {
        sendEditProductPgIrisClickEvent(
                action = "click price suggestion pop up - similar product",
                label = "{$row}",
                trackerId = "35691"
        )
    }

    fun sendClickPriceSuggestionPopUpCloseEvent() {
        sendEditProductPgIrisClickEvent(
                action = "click price suggestion pop up - close",
                label = "",
                trackerId = "35692"
        )
    }

    fun sendClickPriceSuggestionPopUpSaveEvent(
            productId: String,
            currentPrice: String,
            suggestedPrice: String,
            priceRange: String,
            updatedPrice: String,
    ) {
        sendEditProductPgIrisClickEvent(
                action = "click price suggestion pop up - save",
                label = "{$productId} - {$currentPrice} - {$suggestedPrice} - {$priceRange} - {$updatedPrice} - {Parent}",
                trackerId = "35693"
        )
    }
}