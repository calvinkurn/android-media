package com.tokopedia.play.broadcaster.domain.usecase.interactive

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.broadcaster.domain.model.interactive.quiz.GetInteractiveSummaryLivestreamResponse
import com.tokopedia.play_common.domain.usecase.RetryableGraphqlUseCase
import javax.inject.Inject

/**
 * created by andriyan on 05/23/22
 */
@GqlQuery(
    GetInteractiveSummaryLivestreamUseCase.QUERY_NAME,
    GetInteractiveSummaryLivestreamUseCase.QUERY
)
class GetInteractiveSummaryLivestreamUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
) : RetryableGraphqlUseCase<GetInteractiveSummaryLivestreamResponse>(
    gqlRepository,
    HIGH_RETRY_COUNT
) {

    init {
        setGraphqlQuery(GetInteractiveSummaryLivestreamUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(GetInteractiveSummaryLivestreamResponse::class.java)
    }

    companion object {
        private const val PARAM_CHANNEL_ID = "channelId"
        const val QUERY_NAME = "GetInteractiveSummaryLivestreamUseCaseQuery"
        const val QUERY = """
            query GetInteractiveSummaryLivestream(
                ${'$'}$PARAM_CHANNEL_ID: String!
            ) {
              playInteractiveGetSummaryLivestream(req:{
                channelID: ${"$$PARAM_CHANNEL_ID"}
              }){
                participantCount
              }
            }
        """

        fun createParams(channelId: String): Map<String, Any> = mapOf(
            PARAM_CHANNEL_ID to channelId
        )
    }
}