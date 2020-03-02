package com.tokopedia.travelhomepage.homepage.data

import com.tokopedia.travelhomepage.homepage.presentation.adapter.factory.TravelHomepageAdapterTypeFactory

/**
 * @author by jessica on 2020-03-02
 */

class TravelHomepageProductCardModel (
        val title: String = "",
        val subtitle: String = "",
        val clickSeeAllUrl: String = "",
        val productItem: List<TravelHomepageProductCardModel> = listOf()
): TravelHomepageItemModel() {
    override fun type(typeFactory: TravelHomepageAdapterTypeFactory): Int = typeFactory.type(this)
}