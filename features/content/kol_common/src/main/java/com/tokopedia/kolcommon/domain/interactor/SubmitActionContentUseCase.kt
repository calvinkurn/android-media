package com.tokopedia.kolcommon.domain.interactor

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.kolcommon.data.SubmitActionContentResponse
import javax.inject.Inject

/**
 * Created by meyta.taliti on 03/08/22.
 */
@GqlQuery(SubmitActionContentUseCase.QUERY_NAME, SubmitActionContentUseCase.QUERY)
class SubmitActionContentUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<SubmitActionContentResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(SubmitActionContentUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(SubmitActionContentResponse::class.java)
    }

    companion object {
        private const val PARAM_ID = "ID"
        private const val PARAM_TYPE = "type"
        private const val PARAM_ACTION = "action"
        private const val TYPE_CONTENT = "content"
        private const val ACTION_DELETE = "delete"

        const val QUERY_NAME = "SubmitActionContentUseCaseQuery"
        const val QUERY = """
            mutation SubmitActionContent(${'$'}$PARAM_ID: String!, ${'$'}$PARAM_TYPE: String!, ${'$'}$PARAM_ACTION: String!) {
              feed_content_submit(Input: {
                ID: ${'$'}$PARAM_ID,
                type: ${'$'}$PARAM_TYPE,
                action: ${'$'}$PARAM_ACTION
              }) {
                success
                redirectURI
                error
                meta {
                  followers
                  content {
                    activityID
                    title
                    description
                    url
                    instagram {
                      backgroundURL
                      profileURL
                    }
                  }
                }
              }
            }
        """

        fun paramToDeleteContent(contentId: String): Map<String, Any> {
            return mapOf(
                PARAM_ID to contentId,
                PARAM_TYPE to TYPE_CONTENT,
                PARAM_ACTION to ACTION_DELETE
            )
        }
    }
}