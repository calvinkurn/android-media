package com.tokopedia.variant_common.use_case

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.variant_common.constant.VariantConstant
import com.tokopedia.variant_common.model.VariantMultiOriginWarehouse
import com.tokopedia.variant_common.util.VariantUtil
import javax.inject.Inject

/**
 * Created by Yehezkiel on 08/03/20
 */
class GetNearestWarehouseUseCase @Inject constructor(private val rawQueries: Map<String, String>,
                                                     private val graphqlRepository: GraphqlRepository) : UseCase<VariantMultiOriginWarehouse.Data>() {

    companion object {
        fun createParams(listOfProductId: List<String>,
                         warehouseId: String?): RequestParams {
            val requestParams = RequestParams()
            requestParams.putObject(VariantConstant.PARAM_PRODUCT_IDS, listOfProductId)
            requestParams.putString(VariantConstant.PARAM_WAREHOUSE_ID, warehouseId)
            return requestParams
        }
    }

    var requestParams: RequestParams = RequestParams.EMPTY
    var forceRefresh: Boolean = false

    override suspend fun executeOnBackground(): VariantMultiOriginWarehouse.Data {
        val warehouseRequest = GraphqlRequest(rawQueries[VariantConstant.QUERY_MULTI_ORIGIN],
                VariantMultiOriginWarehouse.Response::class.java, requestParams.parameters)

        val gqlResponse = graphqlRepository.getReseponse(listOf(warehouseRequest), VariantUtil.getCacheStrategy(forceRefresh))
        val data = gqlResponse.getData<VariantMultiOriginWarehouse.Response>(VariantMultiOriginWarehouse.Response::class.java)
        val error = gqlResponse.getError(VariantMultiOriginWarehouse.Response::class.java) ?: listOf()

        if (error.isNotEmpty() && error.firstOrNull()?.message?.isNotEmpty() == true) {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        } else if (data == null) {
            throw RuntimeException()
        }

        return data.result
    }

}
