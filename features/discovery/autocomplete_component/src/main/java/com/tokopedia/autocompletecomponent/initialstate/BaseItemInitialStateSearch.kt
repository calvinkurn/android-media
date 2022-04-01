package com.tokopedia.autocompletecomponent.initialstate

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.autocompletecomponent.initialstate.analytics.InitialStateTracking.Companion.Misc.PRODUCT_LINE_INITIAL_STATE_ACTION_FIELD
import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.searchComponentTracking
import com.tokopedia.kotlin.model.ImpressHolder

data class BaseItemInitialStateSearch(
    val itemId: String = "",
    val template: String = "",
    val imageUrl: String = "",
    val applink: String = "",
    val url: String = "",
    val title: String = "",
    val subtitle: String = "",
    val iconTitle: String = "",
    val iconSubtitle: String = "",
    val label: String = "",
    val labelType: String = "",
    val shortcutImage: String = "",
    val productId: String = "",
    val type: String = "",
    val featureId: String = "",
    val header: String = "",
    val discountPercentage: String = "",
    val originalPrice: String = "",
    val position: Int = 0,
    val campaignCode: String = "",
    val dimension90: String = "",
    val componentId: String = "",
    val trackingOption: Int = 0,
    val keyword: String = "",
): ImpressHolder(),
    SearchComponentTracking by searchComponentTracking(
        trackingOption = trackingOption,
        keyword = keyword,
        valueId = itemId,
        valueName = title,
        campaignCode = campaignCode,
        componentId = componentId,
        applink = applink,
        dimension90 = dimension90,
    ) {

    fun hasSlashedPrice(): Boolean {
        return discountPercentage.isNotEmpty() && originalPrice.isNotEmpty()
    }

    fun getRecentViewAsObjectDataLayer(): Any = DataLayer.mapOf(
            "name", title,
            "id", productId,
            "price", "",
            "brand", "none / other",
            "category", "none / other",
            "variant", "none / other",
            "position", position,
            "dimension90", dimension90
    )

    fun getProductLineAsObjectDataLayer(): Any = DataLayer.mapOf(
            "name", title,
            "id", productId,
            "price", subtitle.replace("[^0-9]".toRegex(), ""),
            "brand", "none / other",
            "category", "none / other",
            "variant", "none / other",
            "position", position,
            "list", PRODUCT_LINE_INITIAL_STATE_ACTION_FIELD
    )
}
