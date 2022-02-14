package com.tokopedia.autocompletecomponent.suggestion

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.autocompletecomponent.suggestion.analytics.SuggestionTracking.Companion.Misc.PRODUCT_LINE_SUGGESTION_ACTION_FIELD
import com.tokopedia.autocompletecomponent.suggestion.doubleline.ShopAdsDataView
import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.searchComponentTracking
import com.tokopedia.kotlin.model.ImpressHolder

class BaseSuggestionDataView(
    val template: String = "",
    val type: String = "",
    val applink: String = "",
    val url: String = "",
    val title: String = "",
    val subtitle: String = "",
    val iconTitle: String = "",
    val iconSubtitle: String = "",
    val shortcutImage: String = "",
    val imageUrl: String = "",
    val label: String = "",
    val labelType: String = "",
    val searchTerm: String = "",
    val position: Int = -1,
    val urlTracker: String = "",
    val trackingCode: String = "",
    val discountPercentage: String = "",
    val originalPrice: String = "",
    val dimension90: String = "",
    val trackingOption: Int = 0,
    val componentId: String = "",
    val childItems: List<ChildItem> = listOf(),
    val shopAdsDataView: ShopAdsDataView? = null,
) : ImpressHolder(),
    SearchComponentTracking by searchComponentTracking(
        trackingOption = trackingOption,
        keyword = searchTerm,
        valueId = "0",
        valueName = title + (if (subtitle.isNotEmpty()) "|$subtitle" else ""),
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