package com.tokopedia.travelhomepage.homepage.data

import com.tokopedia.travelhomepage.homepage.data.widgetmodel.LegoBannerItemModel
import com.tokopedia.travelhomepage.homepage.presentation.adapter.factory.TravelHomepageAdapterTypeFactory

/**
 * @author by jessica on 2020-03-02
 */

class TravelHomepageLegoBannerModel (
        val title: String = "",
        val subtitle: String = "",
        val bannerItem: List<LegoBannerItemModel> = listOf()
): TravelHomepageItemModel() {
    override fun type(typeFactory: TravelHomepageAdapterTypeFactory): Int = typeFactory.type(this)
}