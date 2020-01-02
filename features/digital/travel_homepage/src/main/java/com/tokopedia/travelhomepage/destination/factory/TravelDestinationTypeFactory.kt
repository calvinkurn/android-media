package com.tokopedia.travelhomepage.homepage.presentation.adapter.factory

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.travelhomepage.destination.model.TravelDestinationSectionViewModel
import com.tokopedia.travelhomepage.destination.model.TravelDestinationSummaryModel
import com.tokopedia.travelhomepage.homepage.data.*
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageItemModel

/**
 * @author by furqan on 06/08/2019
 */
interface TravelDestinationTypeFactory : AdapterTypeFactory {

    fun type(viewModel: TravelDestinationSummaryModel): Int

    fun type(viewModel: TravelDestinationSectionViewModel): Int
}