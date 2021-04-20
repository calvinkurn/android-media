package com.tokopedia.buyerorder.unifiedhistory.list.domain

import com.tokopedia.buyerorder.common.util.BuyerConsts
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.TrainResendEmail
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.TrainResendEmailParam
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 08/08/20.
 */
class TrainResendEmailUseCase @Inject constructor(private val gqlRepository: GraphqlRepository) {
    suspend fun executeSuspend(param: TrainResendEmailParam): Result<TrainResendEmail.Data> {
        return try {
            val request = GraphqlRequest(QUERY, TrainResendEmail.Data::class.java, generateParam(param))
            val response = gqlRepository.getReseponse(listOf(request)).getSuccessData<TrainResendEmail.Data>()
            Success(response)
        } catch (e: Exception) {
            Fail(e)
        }
    }

    private fun generateParam(param: TrainResendEmailParam): Map<String, Any?> {
        return mapOf(BuyerConsts.PARAM_INPUT to param)
    }

    companion object {
        val QUERY = """
            mutation ResendBookingEmail(${'$'}input:ResendBookingEmailArgs!) {
              trainResendBookingEmail(input:${'$'}input){
                Success
              }
            }
            """.trimIndent()
    }
}