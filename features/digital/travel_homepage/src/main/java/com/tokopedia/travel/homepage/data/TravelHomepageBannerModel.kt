package com.tokopedia.travel.homepage.data

import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel
import com.tokopedia.travel.homepage.presentation.adapter.factory.TravelHomepageAdapterTypeFactory

/**
 * @author by furqan on 06/08/2019
 */
class TravelHomepageBannerModel(val travelCollectiveBannerModel: TravelCollectiveBannerModel = TravelCollectiveBannerModel())
    : TravelHomepageItemModel() {
    override fun type(typeFactory: TravelHomepageAdapterTypeFactory): Int = typeFactory.type(this)
}