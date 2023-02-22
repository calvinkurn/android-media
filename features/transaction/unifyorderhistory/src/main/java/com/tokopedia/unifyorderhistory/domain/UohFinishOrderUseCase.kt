package com.tokopedia.unifyorderhistory.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.unifyorderhistory.data.model.UohFinishOrder
import com.tokopedia.unifyorderhistory.data.model.UohFinishOrderParam
import com.tokopedia.unifyorderhistory.util.UohConsts.PARAM_INPUT
import javax.inject.Inject

@GqlQuery("FinishOrderBuyerQuery", UohFinishOrderUseCase.query)
class UohFinishOrderUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) :
    CoroutineUseCase<UohFinishOrderParam, UohFinishOrder>(dispatchers.io) {

    override fun graphqlQuery(): String = query

    override suspend fun execute(params: UohFinishOrderParam): UohFinishOrder {
        return repository.request(FinishOrderBuyerQuery(), generateParam(params))
    }

    private fun generateParam(param: UohFinishOrderParam): Map<String, Any?> {
        return mapOf(PARAM_INPUT to param)
    }

    companion object {
        const val query = """
            mutation FinishOrderBuyer(${'$'}input:FinishOrderBuyerRequest!) {
              finish_order_buyer(input:${'$'}input) {
                success
                message
              }
            }
        """
    }
}
