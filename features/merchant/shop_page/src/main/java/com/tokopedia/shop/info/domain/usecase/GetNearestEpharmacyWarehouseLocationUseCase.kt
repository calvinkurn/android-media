package com.tokopedia.shop.info.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.info.data.response.GetNearestEpharmacyWarehouseLocationResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetNearestEpharmacyWarehouseLocationUseCase @Inject constructor(
    private val gqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<GetNearestEpharmacyWarehouseLocationResponse>() {

    companion object {
        private const val SHOP_ID = "shopID"
        private const val DISTRICT_ID = "districtID"
        private const val QUERY = "query getNearestEpharmacyWarehouseLocation(\$districtID: Int!, \$shopID: Int64!) {\n" +
            "   getNearestEpharmacyWarehouseLocation(districtID: \$districtID, shopID: \$shopID) {\n" +
            "       data {\n" +
            "           gMapsURL\n" +
            "           address\n" +
            "           warehouseID\n" +
            "       }\n" +
            "   }\n" +
            "}"

        fun createParams(
            shopId: Long,
            districtId: Long
        ): RequestParams = RequestParams.create().apply {
            putLong(SHOP_ID, shopId)
            putLong(DISTRICT_ID, districtId)
        }
    }

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): GetNearestEpharmacyWarehouseLocationResponse {
        val getNearestWarehouse = GraphqlRequest(QUERY, GetNearestEpharmacyWarehouseLocationResponse::class.java, params.parameters)
        gqlUseCase.clearRequest()
        gqlUseCase.addRequest(getNearestWarehouse)
        val gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(GetNearestEpharmacyWarehouseLocationResponse::class.java) ?: listOf()
        if (error == null || error.isEmpty()) {
            return gqlResponse.run { getData(GetNearestEpharmacyWarehouseLocationResponse::class.java) }
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }
}
