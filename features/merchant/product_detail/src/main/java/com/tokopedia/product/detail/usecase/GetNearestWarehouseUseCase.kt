package com.tokopedia.product.detail.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.warehouse.MultiOriginWarehouse
import com.tokopedia.product.detail.di.RawQueryKeyConstant
import com.tokopedia.product.detail.view.util.CacheStrategyUtil
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by Yehezkiel on 05/03/20
 */
class GetNearestWarehouseUseCase @Inject constructor(private val rawQueries: Map<String, String>,
                                                     private val graphqlRepository: GraphqlRepository) : UseCase<MultiOriginWarehouse.Data>() {

    companion object {
        fun createParams(listOfProductId: List<String>,
                         warehouseId: String?): RequestParams {
            val requestParams = RequestParams()
            requestParams.putObject(ProductDetailCommonConstant.PARAM_PRODUCT_IDS, listOfProductId)
            requestParams.putString(ProductDetailCommonConstant.PARAM_WAREHOUSE_ID, warehouseId)
            return requestParams
        }
    }

    var requestParams: RequestParams = RequestParams.EMPTY
    var forceRefresh: Boolean = false

    override suspend fun executeOnBackground(): MultiOriginWarehouse.Data {
        val warehouseRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_MULTI_ORIGIN],
                MultiOriginWarehouse.Response::class.java, requestParams.parameters)

        val gqlResponse = graphqlRepository.getReseponse(listOf(warehouseRequest), CacheStrategyUtil.getCacheStrategy(forceRefresh))
        val data = gqlResponse.getData<MultiOriginWarehouse.Response>(MultiOriginWarehouse.Response::class.java)
        val error = gqlResponse.getError(MultiOriginWarehouse.Response::class.java) ?: listOf()

        if (error.isNotEmpty() && error.firstOrNull()?.message?.isNotEmpty() == true) {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        } else if (data == null) {
            throw RuntimeException()
        }

        return data.result
    }

}