package com.tokopedia.content.common.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.content.common.model.GetContentFormDomain
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on April 21, 2022
 */
@GqlQuery(GetContentFormUseCase.QUERY_NAME, GetContentFormUseCase.QUERY)
class GetContentFormUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<GetContentFormDomain>(graphqlRepository) {

    init {
        setGraphqlQuery(GetContentFormUseCaseQuery())
        setCacheStrategy(
            GraphqlCacheStrategy
            .Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(GetContentFormDomain::class.java)
    }

    companion object {
        private const val PARAM_TYPE = "type"
        private const val PARAM_RELATED_ID = "relatedID"
        private const val PARAM_POST_ID = "id"
        private const val PARAM_TOKEN = "token"
        const val QUERY_NAME = "GetContentFormUseCaseQuery"
        const val QUERY = """
            query ContentForm(${'$'}relatedID: [String], ${'$'}type: String, ${'$'}id: String, ${'$'}token: String) {
              feed_content_form(relatedID: ${'$'}relatedID, type: ${'$'}type, ID: ${'$'}id, token: ${'$'}token) {
                authors {
                  type
                  id
                  name
                  thumbnail
                  badge
                }
                has_username
                has_accept_tnc
              }
            }
        """

        fun createParams(relatedIds: MutableList<String>, type: String, postId: String): Map<String, Any> {
            return mapOf(
                PARAM_RELATED_ID to relatedIds,
                PARAM_TYPE to type,
                PARAM_POST_ID to postId
            )
        }
    }
}