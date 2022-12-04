package com.tokopedia.mvc.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.mvc.data.response.TokenResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetTokenUseCase @Inject constructor(
    private val repository: GraphqlRepository,
) : GraphqlUseCase<String>(repository) {

    private val query = object : GqlQueryInterface {
        private val OPERATION_NAME = "GetToken"
        val QUERY = "query GetToken{\n" +
            "\tgetInitiateVoucherPage(Action: \"update\"){\n" +
            "\t\theader{\n" +
            "          process_time\n" +
            "          messages\n" +
            "          reason\n" +
            "          error_code\n" +
            "        }\n" +
            "        data{\n" +
            "          token\n" +
            "        }\n" +
            "    }\n" +
            "}"

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
        override fun getQuery(): String = QUERY.trimIndent()
        override fun getTopOperationName(): String = OPERATION_NAME
    }

    override suspend fun executeOnBackground(): String {
        val request = buildRequest()
        val response = repository.response(listOf(request))
        val errors = response.getError(TokenResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = response.getSuccessData<TokenResponse>()
            return data.tokenModel.data.token
        } else {
            throw MessageErrorException(errors.joinToString { it.message })
        }
    }

    private fun buildRequest(): GraphqlRequest {
        return GraphqlRequest(
            query,
            TokenResponse::class.java,
            RequestParams.EMPTY.parameters
        )
    }
}

