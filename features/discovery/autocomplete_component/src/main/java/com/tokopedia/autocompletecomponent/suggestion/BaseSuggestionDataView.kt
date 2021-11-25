package com.tokopedia.autocompletecomponent.suggestion

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.autocompletecomponent.suggestion.analytics.SuggestionTracking.Companion.Misc.PRODUCT_LINE_SUGGESTION_ACTION_FIELD
import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.searchComponentTracking
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
    var dimension90: String = "",
    var trackingOption: Int = 0,
    var componentId: String = "",
    var keyword: String = "",
    var childItems: List<ChildItem> = listOf(),
) : ImpressHolder(),
    SearchComponentTracking by searchComponentTracking(
        trackingOption = trackingOption,
        keyword = searchTerm,
        valueName = title,
        componentId = componentId,
        applink = applink,
        dimension90 = dimension90,
    ) {

    data class ChildItem(
        val template: String = "",
        val type: String = "",
        val applink: String = "",
        val url: String = "",
        val title: String = "",
        val searchTerm: String = "",
        val dimension90: String = "",
        val position: Int = 0
    )

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
        "list", PRODUCT_LINE_SUGGESTION_ACTION_FIELD,
        "dimension90", dimension90
    )
}