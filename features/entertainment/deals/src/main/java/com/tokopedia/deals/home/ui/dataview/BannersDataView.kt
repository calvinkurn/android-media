package com.tokopedia.deals.home.ui.dataview

import com.tokopedia.deals.common.ui.dataview.DealsBaseItemDataView

/**
 * @author by jessica on 16/06/20
 */

data class BannersDataView(
        val seeAllText: String = "",
        val seeAllUrl: String = "",
        val list: List<BannerDataView> = listOf()
): DealsBaseItemDataView() {
    data class BannerDataView(
        val bannerId: String = "",
        val bannerName: String = "",
        val bannerUrl: String = "",
        val bannerImageUrl: String = ""
    )
}