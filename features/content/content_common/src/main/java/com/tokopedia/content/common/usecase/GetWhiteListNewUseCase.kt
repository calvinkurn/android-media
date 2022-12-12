package com.tokopedia.content.common.usecase

import com.tokopedia.content.common.model.GetCheckWhitelistResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

/**
 * @author by yfsx on 20/06/18.
 */
@GqlQuery(GetWhiteListNewUseCase.QUERY_NAME, GetWhiteListNewUseCase.GET_WHITE_LIST_QUERY)
class GetWhiteListNewUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<GetCheckWhitelistResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(GetWhitelistNewUseCaseQuery())
        setCacheStrategy(
            GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build()
        )
        setTypeClass(GetCheckWhitelistResponse::class.java)
    }

    private fun createRequestParams(type: String, id: String = "") {
        val request = mapOf(
            PARAM_TYPE to type,
            PARAM_ID to id,
        )
        setRequestParams(request)
    }

    suspend fun execute(type: String, id: String = ""): GetCheckWhitelistResponse {
        this.createRequestParams(type, id)
        return executeOnBackground()
    }

    companion object {
        private const val PARAM_TYPE = "type"
        private const val PARAM_ID = "ID"
        const val WHITELIST_INTEREST = "interest"
        const val WHITELIST_ENTRY_POINT = "entrypoint"
        const val QUERY_NAME = "GetWhitelistNewUseCaseQuery"
        const val GET_WHITE_LIST_QUERY: String = """
            query FeedCheckWhitelist(${'$'}type: String, ${'$'}ID: String) {
              feed_check_whitelist(type: ${'$'}type, ID: ${'$'}ID) {
                __typename
                iswhitelist
                url
                title
                title_identifier
                description
                post_success
                image_url
                authors {
                  id
                  name
                  title
                  thumbnail
                  link
                  badge
                  type
                  post {
                    enable
                    has_username
                  }
                  livestream {
                    enable
                    has_username
                  }
                  shortvideo {
                    enable
                    has_username
                  }
                }
                error
              }
            }
        """
    }

}
