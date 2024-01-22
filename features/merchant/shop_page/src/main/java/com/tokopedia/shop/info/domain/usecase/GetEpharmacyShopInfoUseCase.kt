package com.tokopedia.shop.info.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.info.data.response.GetEpharmacyShopInfoResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetEpharmacyShopInfoUseCase @Inject constructor(
    private val graphqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<GetEpharmacyShopInfoResponse>() {

    companion object {
        private const val SHOP_ID = "shop_id"
        private const val WAREHOUSE_ID = "warehouse_id"
        private const val QUERY = "query getEpharmacyShopInfo(\$shop_id: Int64!, \$warehouse_id: Int64) {\n" +
            "   getEpharmacyShopInfo(shop_id: \$shop_id, warehouse_id: \$warehouse_id) {\n" +
            "       header {" +
            "           process_time" +
            "           error_code" +
            "           error_message" +
            "       }" +
            "       data {" +
            "           sia_number" +
            "           sipa_number" +
            "           apj" +
            "           epharmacy_working_hours_fmt" +
            "       }" +
            "   }" +
            "}"

        fun createParams(shopId: Long, warehouseId: Long): RequestParams = RequestParams.create().apply {
            putLong(SHOP_ID, shopId)
            putLong(WAREHOUSE_ID, warehouseId)
        }
    }

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): GetEpharmacyShopInfoResponse {
        val getEpharmDetail = GraphqlRequest(QUERY, GetEpharmacyShopInfoResponse::class.java, params.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(getEpharmDetail)
        val gqlResponse = graphqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(GetEpharmacyShopInfoResponse::class.java) ?: listOf()
        if (error == null || error.isEmpty()) {
            return gqlResponse.run { getData(GetEpharmacyShopInfoResponse::class.java) }
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }
}
