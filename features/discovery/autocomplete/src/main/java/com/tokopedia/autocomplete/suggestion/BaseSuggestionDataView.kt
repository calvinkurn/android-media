package com.tokopedia.autocomplete.suggestion

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.autocomplete.analytics.AutocompleteEventTracking
import com.tokopedia.kotlin.model.ImpressHolder

open class BaseSuggestionDataView(
        var template: String = "",
        var type: String = "",
        var applink: String = "",
        var url: String = "",
        var title: String = "",
        var subtitle: String = "",
        var iconTitle: String = "",
        var iconSubtitle: String = "",
        var shortcutUrl: String = "",
        var shortcutImage: String = "",
        var imageUrl: String = "",
        var label: String = "",
        var labelType: String = "",
        var searchTerm: String = "",
        var position: Int = -1,
        var urlTracker: String = "",
        var trackingCode: String = "",
        var discountPercentage: String = "",
        var originalPrice: String = "",
        var dimension90: String = ""
): ImpressHolder() {
    fun hasSlashedPrice(): Boolean {
        return discountPercentage.isNotEmpty() && originalPrice.isNotEmpty()
    }

    fun getProductLineAsObjectDataLayer(): Any = DataLayer.mapOf(
            "name", title,
            "id", "0",
            "price", subtitle.replace("[^0-9]".toRegex(), ""),
            "brand", "none / other",
            "category", "none / other",
            "variant", "none / other",
            "position", position,
            "list", AutocompleteEventTracking.Other.PRODUCT_LINE_SUGGESTION_ACTION_FIELD,
            "dimension90", dimension90
    )
}