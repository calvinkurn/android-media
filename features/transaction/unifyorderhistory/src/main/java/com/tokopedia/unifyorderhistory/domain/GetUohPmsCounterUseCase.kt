package com.tokopedia.unifyorderhistory.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.unifyorderhistory.data.model.PmsNotification
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

@GqlQuery(GetUohPmsCounterUseCase.QUERY_NAME, GetUohPmsCounterUseCase.QUERY)
class GetUohPmsCounterUseCase @Inject constructor(@ApplicationContext private val gqlRepository: GraphqlRepository) {

    suspend fun executeSuspend(): Result<PmsNotification> {
        return try {
            val request = GraphqlRequest(GetPmsNotifications(), PmsNotification::class.java)
            val response = gqlRepository.response(listOf(request)).getSuccessData<PmsNotification>()
            Success(response)
        } catch (e: Exception) {
            Fail(e)
        }
    }

    companion object {
        const val QUERY_NAME = "GetPmsNotifications"
        const val QUERY = """
            query GetPmsNotifications {
              notifications {
                buyerOrderStatus {
                  paymentStatus
                }
              }
            }
        """
    }
}