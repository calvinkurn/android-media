package com.tokopedia.seller.search.feature.initialsearch.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.seller.search.feature.initialsearch.domain.model.DeleteHistoryResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class DeleteSuggestionHistoryUseCase @Inject constructor(
        private val graphQlRepository: GraphqlRepository
) : UseCase<DeleteHistoryResponse.DeleteHistory>() {

    companion object {
        private const val KEYWORD = "keyword"

        private val gqlQuery = """
            mutation deleteSuggestion(${'$'}keyword: [String]!) {
              	deleteHistory(keyword: ${'$'}keyword) {
                    message
                    status
              }
            }
        """.trimIndent()

        @JvmStatic
        fun createParams(keyword: List<String>): Map<String, List<String>> = mapOf(KEYWORD to keyword)
    }

    var params = mapOf<String, List<String>>()

    override suspend fun executeOnBackground(): DeleteHistoryResponse.DeleteHistory {
        val gqlRequest = GraphqlRequest(gqlQuery, DeleteHistoryResponse::class.java, params)
        val gqlResponse = graphQlRepository.getReseponse(listOf(gqlRequest))
        val error = gqlResponse.getError(GraphqlError::class.java)
        if (error.isNullOrEmpty()) {
            return gqlResponse.getData<DeleteHistoryResponse>(DeleteHistoryResponse::class.java).deleteHistory
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message })
        }
    }
}