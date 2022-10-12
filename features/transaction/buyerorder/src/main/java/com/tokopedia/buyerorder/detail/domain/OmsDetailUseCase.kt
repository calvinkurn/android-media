package com.tokopedia.buyerorder.detail.domain

import com.tokopedia.buyerorder.detail.data.DetailsData
import com.tokopedia.buyerorder.detail.domain.queries.QueryOmsDetails
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

/**
 * created by @bayazidnasir on 19/8/2022
 */

class OmsDetailUseCase @Inject constructor(
    private val repository: GraphqlRepository
): GraphqlUseCase<DetailsData>(repository) {

    private var params: Map<String, Any?> = mapOf()

    override suspend fun executeOnBackground(): DetailsData {
        val gqlResponse = repository.response(listOf(createRequest()))
        val error = gqlResponse.getError(DetailsData::class.java)
        if (error == null || error.isEmpty()){
            return gqlResponse.getData(DetailsData::class.java)
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.toString())
        }
    }

    private fun createRequest() = GraphqlRequest(QueryOmsDetails(), DetailsData::class.java, params)

    fun createParams(orderId: String, orderCategory: String, upstream: String?){
        params = mapOf(
            Key.ORDER_CATEGORY to orderCategory,
            Key.ORDER_ID to orderId,
            Key.UPSTREAM to upstream,
            Key.DETAIL to DETAIL_ACTION_CODE,
            Key.ACTION to DETAIL_ACTION_CODE,
        )
    }

    companion object{
        private object Key{
            const val ORDER_CATEGORY = "orderCategoryStr"
            const val ORDER_ID = "orderId"
            const val DETAIL = "detail"
            const val ACTION = "action"
            const val UPSTREAM = "upstream"
        }

        private const val DETAIL_ACTION_CODE = 1
    }
}