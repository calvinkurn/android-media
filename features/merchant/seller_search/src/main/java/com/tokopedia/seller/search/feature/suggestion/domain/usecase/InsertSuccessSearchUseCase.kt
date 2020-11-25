package com.tokopedia.seller.search.feature.suggestion.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.seller.search.feature.suggestion.domain.model.SuccessSearchResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class InsertSuccessSearchUseCase @Inject constructor(
        private val graphQlRepository: GraphqlRepository
): UseCase<SuccessSearchResponse.SuccessSearch>() {

    companion object {
        private const val KEYWORD = "keyword"
        private const val ID = "id"
        private const val TITLE = "title"
        private const val INDEX = "index"

        private val gqlQuery = """
            mutation successSearch(${'$'}keyword: String!, ${'$'}id: String!, ${'$'}title: String!, ${'$'}index: Int!) {
              	successSearch(keyword: ${'$'}keyword, id: ${'$'}id, title: ${'$'}title, index: ${'$'}index) {
                    message
                    status
              }
            }
        """.trimIndent()

        @JvmStatic
        fun createParams(keyword: String, id: String, title: String, index: Int): Map<String, Any> =
                mapOf(KEYWORD to keyword, ID to id, TITLE to title, INDEX to index)
    }

    var params = mapOf<String, Any>()

    override suspend fun executeOnBackground(): SuccessSearchResponse.SuccessSearch {
        val gqlRequest = GraphqlRequest(gqlQuery, SuccessSearchResponse::class.java, params)
        val gqlResponse = graphQlRepository.getReseponse(listOf(gqlRequest))
        val error = gqlResponse.getError(GraphqlError::class.java)
        if (error.isNullOrEmpty()) {
            return gqlResponse.getData<SuccessSearchResponse>(SuccessSearchResponse::class.java).successSearch
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message })
        }
    }
}