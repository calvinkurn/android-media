package com.tokopedia.content.common.producttag.view.uimodel.tracker

/**
 * Created By : Jonathan Darwin on May 23, 2022
 */
data class SRPTrackerUiModel(
    val keyword: String,
    val prevKeyword: String,
    val treatmentType: String,
    val responseCode: String,
    val navSource: NavSource,
    val totalData: String,
    val srpComponentId: String,
) {
    enum class NavSource(val text: String) {
        PRODUCT("feed_product"),
        SHOP("feed_shop"),
    }
}