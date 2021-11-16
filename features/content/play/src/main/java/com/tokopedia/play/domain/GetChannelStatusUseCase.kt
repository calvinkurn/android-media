package com.tokopedia.play.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.data.ChannelStatusResponse
import javax.inject.Inject


/**
 * Created by mzennis on 01/02/21.
 */
@GqlQuery(GetChannelStatusUseCase.QUERY_NAME, GetChannelStatusUseCase.QUERY)
class GetChannelStatusUseCase @Inject constructor(
        gqlRepository: GraphqlRepository
): GraphqlUseCase<ChannelStatusResponse>(gqlRepository) {

    init {
        setGraphqlQuery(GetChannelStatusUseCaseQuery.GQL_QUERY)
        setCacheStrategy(GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(ChannelStatusResponse::class.java)
    }

    companion object {
        private const val PARAM_CHANNEL_IDS = "channelIds"
        const val QUERY_NAME = "GetChannelStatusUseCaseQuery"
        const val QUERY = """
            query GetChannelStatus(${'$'}channelIds: [String]){
              playGetChannelsStatus(req: {
                ids: ${'$'}channelIds
              }) {
                data {
                  id
                  status
                }
                waiting_duration
              }
            }
        """

        fun createParams(channelIds: Array<String>): Map<String, Any> = mapOf(
                PARAM_CHANNEL_IDS to channelIds
        )
    }
}