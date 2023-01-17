package com.tokopedia.unifyorderhistory.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.unifyorderhistory.data.model.RechargeSetFailData
import com.tokopedia.unifyorderhistory.util.UohConsts
import javax.inject.Inject

@GqlQuery("RechargeSetFailQuery", RechargeSetFailUseCase.query)
class RechargeSetFailUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) :
    CoroutineUseCase<Int, RechargeSetFailData>(dispatchers.io) {

    override fun graphqlQuery(): String = query

    override suspend fun execute(params: Int): RechargeSetFailData {
        return repository.request(RechargeSetFailQuery(), generateParam(params))
    }

    private fun generateParam(orderId: Int): Map<String, Any?> {
        return mapOf(UohConsts.RECHARGE_GQL_PARAM_ORDER_ID to orderId)
    }

    companion object {
        const val query = """
            mutation rechargeSetOrderToFail(${'$'}orderId: Int64!) {
              rechargeSetOrderToFailV2(order_id:${'$'}orderId) {
                attributes {
                  user_id
                  order_status
                  is_success
                  error_message
                }
                error
              }
            }
        """
    }
}
