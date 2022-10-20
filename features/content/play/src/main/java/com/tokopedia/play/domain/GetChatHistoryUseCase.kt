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
                PARAM_SEARCH_TYPE to "HISTORY",
                PARAM_CHANNEL_ID to channelId,
                PARAM_PAGINATION to mapOf(
                    PARAM_CURSOR to cursor
                )
            )
        )

        return super.executeOnBackground()
    }

    companion object {
        private const val PARAM_SEARCH_TYPE = "searchType"
        private const val PARAM_CHANNEL_ID = "channelID"
        private const val PARAM_PAGINATION = "pagination"
        private const val PARAM_CURSOR = "cursor"

        const val QUERY_NAME = "GetChatHistoryUseCaseQuery"
        const val QUERY = """
            query PlayInteractiveGetChatHistory(
                ${"$$PARAM_SEARCH_TYPE"}: PlayInteractiveSearchType,
                ${"$$PARAM_CHANNEL_ID"}: String,
                ${"$$PARAM_PAGINATION"}: PlayInteractiveGetChatHistoryPaginationRequest
            ) {
                PlayInteractiveGetChatHistory(
                    $PARAM_SEARCH_TYPE: ${"$$PARAM_SEARCH_TYPE"},
                    $PARAM_CHANNEL_ID: ${"$$PARAM_CHANNEL_ID"},
                    $PARAM_PAGINATION: ${"$$PARAM_PAGINATION"}
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
