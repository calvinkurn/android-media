package com.tokopedia.play.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.data.PlayChatHistoryResponse
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on October 20, 2022
 */
@GqlQuery(GetChatHistoryUseCase.QUERY_NAME, GetChatHistoryUseCase.QUERY)
class GetChatHistoryUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
) : GraphqlUseCase<PlayChatHistoryResponse>(gqlRepository) {

    init {
        setGraphqlQuery(GetChatHistoryUseCaseQuery())
        setCacheStrategy(
            GraphqlCacheStrategy
            .Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(PlayChatHistoryResponse::class.java)
    }

    suspend fun executeOnBackground(
        channelId: String,
        cursor: String = "",
    ): PlayChatHistoryResponse {
        setRequestParams(
            mapOf(
                PARAM_REQ to mapOf(
                    PARAM_SEARCH_TYPE to SEARCH_TYPE_HISTORY,
                    PARAM_CHANNEL_ID to channelId,
                    PARAM_PAGINATION to mapOf(
                        PARAM_CURSOR to cursor
                    )
                )
            )
        )

        return super.executeOnBackground()
    }

    companion object {
        private const val PARAM_REQ = "req"
        private const val PARAM_SEARCH_TYPE = "searchType"
        private const val PARAM_CHANNEL_ID = "channelID"
        private const val PARAM_PAGINATION = "pagination"
        private const val PARAM_CURSOR = "cursor"

        private const val SEARCH_TYPE_HISTORY = "HISTORY"

        const val QUERY_NAME = "GetChatHistoryUseCaseQuery"
        const val QUERY = """
            query PlayInteractiveGetChatHistory(
                ${"$$PARAM_REQ"}: PlayInteractiveGetChatHistoryRequest
            ) {
                playInteractiveGetChatHistory(
                    $PARAM_REQ: ${"$$PARAM_REQ"}
                ) {
                    data {
                        channel_id
                        message
                        user {
                            id
                            name
                            image
                        }
                    }
                    pagination {
                        nextCursor
                    }
                }
            }
        """
    }
}
