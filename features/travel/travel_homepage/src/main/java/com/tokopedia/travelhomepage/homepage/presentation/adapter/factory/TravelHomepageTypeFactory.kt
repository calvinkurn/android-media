package com.tokopedia.travelhomepage.homepage.presentation.adapter.factory

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageBannerModel
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageCategoryListModel
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageDestinationModel
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageSectionModel

/**
 * @author by furqan on 06/08/2019
 */
interface TravelHomepageTypeFactory : AdapterTypeFactory {

    fun type(viewModel: TravelHomepageBannerModel): Int

    fun type(viewModel: TravelHomepageCategoryListModel): Int

    fun type(viewModel: TravelHomepageDestinationModel): Int

    fun type(model: TravelHomepageSectionModel): Int
}