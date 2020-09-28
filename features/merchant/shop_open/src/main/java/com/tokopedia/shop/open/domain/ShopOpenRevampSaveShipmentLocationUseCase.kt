package com.tokopedia.shop.open.domain


import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.open.common.GQLQueryConstant.QUERY_SHOP_OPEN_REVAMP_SAVE_SHIPMENT_LOCATION
import com.tokopedia.shop.open.data.model.SaveShipmentLocation
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

import javax.inject.Inject
import javax.inject.Named


class ShopOpenRevampSaveShipmentLocationUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase,
        @Named(QUERY_SHOP_OPEN_REVAMP_SAVE_SHIPMENT_LOCATION) val querySaveShipmentLocation: String
): UseCase<SaveShipmentLocation>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): SaveShipmentLocation {
        val saveShipmentLocation = GraphqlRequest(querySaveShipmentLocation, SaveShipmentLocation::class.java, params.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(saveShipmentLocation)
        val gqlResponse = graphqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(SaveShipmentLocation::class.java) ?: listOf()
        if (error == null || error.isEmpty()) {
            return gqlResponse.run {
                getData<SaveShipmentLocation>(SaveShipmentLocation::class.java)
            }
        } else {
            throw MessageErrorException(error.mapNotNull {
                it.message
            }.joinToString(separator = ", "))
        }
    }

    companion object {
        const val INPUT_OPEN_SHOP = "inputOpenShop"

        fun createRequestParams(paramData: Map<String, Any>): RequestParams = RequestParams.create().apply {
            putObject(INPUT_OPEN_SHOP, paramData)
        }

    }

}