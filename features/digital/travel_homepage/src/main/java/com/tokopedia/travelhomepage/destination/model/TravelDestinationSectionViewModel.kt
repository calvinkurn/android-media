package com.tokopedia.travelhomepage.destination.model

import com.tokopedia.travelhomepage.destination.factory.TravelDestinationAdapterTypeFactory

/**
 * @author by jessica on 2019-08-14
 */

data class TravelDestinationSectionViewModel(
        var title: String = "",
        var seeAllUrl: String = "",
        var list: List<Item> = listOf(),
        var type: Int = 0,
        var categoryType: String = ""
): TravelDestinationItemModel() {

    override fun type(typeFactory: TravelDestinationAdapterTypeFactory): Int = typeFactory.type(this)

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