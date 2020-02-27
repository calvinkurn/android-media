package com.tokopedia.purchase_platform.features.one_click_checkout.common.domain

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.shipping.Response
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.mapper.ShippingDurationModelMapper
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shipping.ShippingDataModel
import com.tokopedia.usecase.coroutines.UseCase

import javax.inject.Inject

class GetShippingDurationUseCase @Inject constructor(@ApplicationContext val context: Context, val graphql: GraphqlUseCase<Response>, val shippingDurationModelMapper: ShippingDurationModelMapper) : UseCase<ShippingDataModel>() {

    override suspend fun executeOnBackground(): ShippingDataModel {
        val graphqlUseCase = GraphqlHelper.loadRawString(context.resources,
                R.raw.query_preference_list)

        graphql.setGraphqlQuery(graphqlUseCase)
        graphql.setTypeClass(Response::class.java)

        val result = graphql.executeOnBackground()

        return shippingDurationModelMapper.convertToDomainModel(result)
    }

}