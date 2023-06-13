package com.tokopedia.unifyorderhistory.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.unifyorderhistory.data.model.TrainResendEmail
import com.tokopedia.unifyorderhistory.data.model.TrainResendEmailParam
import com.tokopedia.unifyorderhistory.util.UohConsts.PARAM_INPUT
import javax.inject.Inject

@GqlQuery("ResendBookingEmailQuery", TrainResendEmailUseCase.query)
class TrainResendEmailUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) :
    CoroutineUseCase<TrainResendEmailParam, TrainResendEmail>(dispatchers.io) {

    override fun graphqlQuery(): String = query

    override suspend fun execute(params: TrainResendEmailParam): TrainResendEmail {
        return repository.request(ResendBookingEmailQuery(), generateParam(params))
    }

    private fun generateParam(param: TrainResendEmailParam): Map<String, Any?> {
        return mapOf(PARAM_INPUT to param)
    }

    companion object {
        const val query = """
            mutation ResendBookingEmail(${'$'}input:ResendBookingEmailArgs!) {
              trainResendBookingEmail(input:${'$'}input){
                Success
              }
            }
        """
    }
}
