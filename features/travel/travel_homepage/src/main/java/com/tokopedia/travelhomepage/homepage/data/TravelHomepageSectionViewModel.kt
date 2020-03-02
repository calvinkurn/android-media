package com.tokopedia.travelhomepage.homepage.data

import com.tokopedia.travelhomepage.homepage.presentation.adapter.factory.TravelHomepageAdapterTypeFactory

/**
 * @author by jessica on 2019-08-14
 */

data class TravelHomepageSectionViewModel(
        var title: String = "",
        var seeAllUrl: String = "",
        var list: List<Item> = listOf(),
        var type: Int = 0,
        var categoryType: String = ""
): TravelHomepageItemModel() {

    override fun type(typeFactory: TravelHomepageAdapterTypeFactory): Int = typeFactory.type(this)

    data class Item(
            var title: String = "",
            var subtitle: String = "",
            var prefix: String = "",
            var prefixStyling: String = "normal",
            var value: String = "",
            var appUrl: String = "",
            var imageUrl: String = "",
            var product: String = ""
    )

    companion object {
        val PREFIX_STYLE_STRIKETHROUGH = "strikethrough"
        val PREFIX_STYLE_NORMAL = "normal"
    }
}