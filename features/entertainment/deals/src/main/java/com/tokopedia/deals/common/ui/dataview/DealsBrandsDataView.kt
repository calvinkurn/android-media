package com.tokopedia.deals.common.ui.dataview

/**
 * @author by jessica on 17/06/20
 */

data class DealsBrandsDataView(
        var title: String = "",
        val seeAllText: String = "",
        val seeAllUrl: String = "",
        var brands: List<Brand> = listOf()
): DealsBaseItemDataView() {
    data class Brand(
            var id: String = "",
            var title: String = "",
            var image: String = "",
            var brandUrl: String = "",
            var position: Int = 0
    )
}