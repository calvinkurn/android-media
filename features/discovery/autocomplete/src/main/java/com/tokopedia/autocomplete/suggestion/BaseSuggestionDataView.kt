package com.tokopedia.autocomplete.suggestion

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
        var originalPrice: String = ""
) {
    fun hasSlashedPrice(): Boolean {
        return discountPercentage.isNotEmpty() && originalPrice.isNotEmpty()
    }
}