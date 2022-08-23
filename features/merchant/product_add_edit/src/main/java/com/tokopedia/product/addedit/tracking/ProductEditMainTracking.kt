package com.tokopedia.product.addedit.tracking

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.sendEditProductClick
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.sendAepPgIrisClickEvent
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.sendAepPgIrisViewEvent

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

    fun sendImpressionPriceSuggestionEntryPointEvent(isEditing: Boolean, productId: String) {
        val labelProductId = if (isEditing) productId else String.EMPTY
        sendAepPgIrisViewEvent(
                action = "impression price suggestion entry point",
                category = if (isEditing) ProductAddEditTracking.CAT_EDIT_PRODUCT_PAGE
                else ProductAddEditTracking.CAT_ADD_PRODUCT_PAGE,
                label = "{$labelProductId} - {Parent}",
                trackerId = if (isEditing) "35684" else "35674"
        )
    }

    fun sendClickPriceSuggestionEntryPointEvent(isEditing: Boolean, productId: String) {
        val labelProductId = if (isEditing) productId else String.EMPTY
        sendAepPgIrisClickEvent(
                action = "click price suggestion entry point",
                category = if (isEditing) ProductAddEditTracking.CAT_EDIT_PRODUCT_PAGE
                else ProductAddEditTracking.CAT_ADD_PRODUCT_PAGE,
                label = "{$labelProductId} - {Parent}",
                trackerId = if (isEditing) "35685" else "35675"
        )
    }

    fun sendClickPriceSuggestionPopUpApplyEvent(
            isEditing: Boolean,
            productId: String,
            currentPrice: String,
            suggestedPrice: String,
            priceRange: String
    ) {
        val labelProductId = if (isEditing) productId else String.EMPTY
        sendAepPgIrisClickEvent(
                action = "click price suggestion pop up - apply",
                category = if (isEditing) ProductAddEditTracking.CAT_EDIT_PRODUCT_PAGE
                else ProductAddEditTracking.CAT_ADD_PRODUCT_PAGE,
                label = "{$labelProductId} - {$currentPrice} - {$suggestedPrice} - {$priceRange} - {Parent}",
                trackerId = if (isEditing) "35686" else "35676"
        )
    }

    fun sendClickPriceSuggestionPopUpAboutPriceSuggestionEvent(isEditing: Boolean) {
        sendAepPgIrisClickEvent(
                action = "click price suggestion pop up - about price suggestion",
                category = if (isEditing) ProductAddEditTracking.CAT_EDIT_PRODUCT_PAGE
                else ProductAddEditTracking.CAT_ADD_PRODUCT_PAGE,
                label = "",
                trackerId = if (isEditing) "35687" else "35677"
        )
    }

    fun sendClickPriceSuggestionPopUpGiveFeedbackEvent(isEditing: Boolean, productId: String) {
        val labelProductId = if (isEditing) productId else String.EMPTY
        sendAepPgIrisClickEvent(
                action = "click price suggestion pop up - give feedback",
                category = if (isEditing) ProductAddEditTracking.CAT_EDIT_PRODUCT_PAGE
                else ProductAddEditTracking.CAT_ADD_PRODUCT_PAGE,
                label = "{$labelProductId} - {Parent}",
                trackerId = if (isEditing) "35688" else "35678"
        )
    }

    fun sendClickPriceSuggestionPopUpLearnMoreEvent(isEditing: Boolean, productId: String) {
        val labelProductId = if (isEditing) productId else String.EMPTY
        sendAepPgIrisClickEvent(
                action = "click price suggestion pop up - learn more",
                category = if (isEditing) ProductAddEditTracking.CAT_EDIT_PRODUCT_PAGE
                else ProductAddEditTracking.CAT_ADD_PRODUCT_PAGE,
                label = "{$labelProductId} - {Parent}",
                trackerId = if (isEditing) "35689" else "35679"
        )
    }

    fun sendClickPriceSuggestionPopUpEditPriceEvent(
            isEditing: Boolean,
            productId: String,
            currentPrice: String,
            suggestedPrice: String,
            priceRange: String,
            updatedPrice: String,
    ) {
        val labelProductId = if (isEditing) productId else String.EMPTY
        sendAepPgIrisClickEvent(
                action = "click price suggestion pop up - edit price",
                category = if (isEditing) ProductAddEditTracking.CAT_EDIT_PRODUCT_PAGE
                else ProductAddEditTracking.CAT_ADD_PRODUCT_PAGE,
                label = "{$labelProductId} - {$currentPrice} - {$suggestedPrice} - {$priceRange} - {$updatedPrice} - {Parent}",
                trackerId = if (isEditing) "35690" else "35680"
        )
    }

    fun sendClickPriceSuggestionPopUpSimilarProductEvent(isEditing: Boolean, row: String) {
        sendAepPgIrisClickEvent(
                action = "click price suggestion pop up - similar product",
                category = if (isEditing) ProductAddEditTracking.CAT_EDIT_PRODUCT_PAGE
                else ProductAddEditTracking.CAT_ADD_PRODUCT_PAGE,
                label = "{$row}",
                trackerId = if (isEditing) "35691" else "35681"
        )
    }

    fun sendClickPriceSuggestionPopUpCloseEvent(isEditing: Boolean) {
        sendAepPgIrisClickEvent(
                action = "click price suggestion pop up - close",
                category = if (isEditing) ProductAddEditTracking.CAT_EDIT_PRODUCT_PAGE
                else ProductAddEditTracking.CAT_ADD_PRODUCT_PAGE,
                label = "",
                trackerId = if (isEditing) "35692" else "35682"
        )
    }

    fun sendClickPriceSuggestionPopUpSaveEvent(
            isEditing: Boolean,
            productId: String,
            currentPrice: String,
            suggestedPrice: String,
            priceRange: String,
            updatedPrice: String,
    ) {
        val labelProductId = if (isEditing) productId else String.EMPTY
        sendAepPgIrisClickEvent(
                action = "click price suggestion pop up - save",
                category = if (isEditing) ProductAddEditTracking.CAT_EDIT_PRODUCT_PAGE
                else ProductAddEditTracking.CAT_ADD_PRODUCT_PAGE,
                label = "{$labelProductId} - {$currentPrice} - {$suggestedPrice} - {$priceRange} - {$updatedPrice} - {Parent}",
                trackerId = if (isEditing) "35693" else "35683"
        )
    }
}