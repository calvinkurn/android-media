package com.tokopedia.travelhomepage.homepage.presentation.adapter.factory

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.travelhomepage.destination.model.TravelArticleModel
import com.tokopedia.travelhomepage.destination.model.TravelDestinationSectionModel
import com.tokopedia.travelhomepage.destination.model.TravelDestinationSummaryModel

/**
 * @author by furqan on 06/08/2019
 */
interface TravelDestinationTypeFactory : AdapterTypeFactory {

    fun type(viewModel: TravelDestinationSummaryModel): Int

    fun type(model: TravelDestinationSectionModel): Int

    fun type(viewModel: TravelArticleModel): Int
}