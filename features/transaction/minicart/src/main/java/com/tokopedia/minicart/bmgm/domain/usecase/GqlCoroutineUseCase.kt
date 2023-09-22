package com.tokopedia.minicart.bmgm.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.withContext

/**
 * Created by @ilhamsuaib on 06/09/23.
 */

abstract class GqlCoroutineUseCase<RESPONSE : Any>(
    private val gqlQueryInterface: GqlQueryInterface,
    private val graphqlRepository: GraphqlRepository,
    protected val dispatchers: CoroutineDispatchers
) {

    abstract val classType: Class<RESPONSE>

    protected suspend fun execute(params: RequestParams): RESPONSE {
        return withContext(dispatchers.io) {
            val gqlRequest = GraphqlRequest(gqlQueryInterface, classType, params.parameters)
            val gqlResponse: GraphqlResponse = graphqlRepository.response(listOf(gqlRequest))
            val errors: List<GraphqlError>? = gqlResponse.getError(classType)
            if (errors.isNullOrEmpty()) {
                return@withContext gqlResponse.getData<RESPONSE>(classType)
            } else {
                throw RuntimeException(errors.firstOrNull()?.message.orEmpty())
            }
        }
    }
}