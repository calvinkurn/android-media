package com.tokopedia.shop.common.graphql.domain.usecase.shopopen


import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.graphql.data.shopopen.SaveShipmentLocation
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject


class ShopOpenRevampSaveShipmentLocationUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase
): UseCase<SaveShipmentLocation>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): SaveShipmentLocation {
        val saveShipmentLocation = GraphqlRequest(QUERY, SaveShipmentLocation::class.java, params.parameters)
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
        private const val QUERY = "mutation ongkirOpenShopShipmentLocation(\$inputOpenShop: OngkirOpenShopInput!) {\n" +
                "  ongkirOpenShopShipmentLocation(input: \$inputOpenShop) {\n" +
                "    data {\n" +
                "        success\n" +
                "        message\n" +
                "    }\n" +
                "    errors{\n" +
                "        id\n" +
                "        status\n" +
                "        title\n" +
                "    }\n" +
                "  }\n" +
                "}\n"

        fun createRequestParams(paramData: Map<String, Any>): RequestParams = RequestParams.create().apply {
            putObject(INPUT_OPEN_SHOP, paramData)
        }

    }

}