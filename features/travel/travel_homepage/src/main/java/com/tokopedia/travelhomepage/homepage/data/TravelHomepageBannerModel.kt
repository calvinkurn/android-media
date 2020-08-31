package com.tokopedia.travelhomepage.homepage.data

import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel
import com.tokopedia.travelhomepage.homepage.presentation.adapter.factory.TravelHomepageAdapterTypeFactory

/**
 * @author by furqan on 06/08/2019
 */
class TravelHomepageBannerModel(val travelCollectiveBannerModel: TravelCollectiveBannerModel = TravelCollectiveBannerModel())
    : TravelHomepageItemModel() {
    override fun type(typeFactory: TravelHomepageAdapterTypeFactory): Int = typeFactory.type(this)
}