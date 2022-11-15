package com.tokopedia.play.broadcaster.shorts.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.broadcaster.shorts.domain.model.BroadcasterAddMediasResponse
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 15, 2022
 */
@GqlQuery(BroadcasterAddMediasUseCase.QUERY_NAME, BroadcasterAddMediasUseCase.QUERY)
class BroadcasterAddMediasUseCase @Inject constructor(
    gqlRepository: GraphqlRepository,
) : GraphqlUseCase<BroadcasterAddMediasResponse>(gqlRepository) {

    init {
        setGraphqlQuery(BroadcasterAddMediasUseCaseQuery())
        setCacheStrategy(
            GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(BroadcasterAddMediasResponse::class.java)
    }

    suspend fun executeOnBackground(
        creationId: String,
        coverUrl: String,
        source: String,
        type: Int,
    ): BroadcasterAddMediasResponse {
        setRequestParams(
            mapOf(
                PARAM_REQ to mapOf(
                    PARAM_CHANNEL_ID to creationId,
                    PARAM_COVER_URL to coverUrl,
                    PARAM_SOURCE to source,
                    PARAM_TYPE to type
                )
            )
        )

        return executeOnBackground()
    }

    companion object {
        private const val PARAM_REQ = "req"

        private const val PARAM_CHANNEL_ID = "channelID"
        private const val PARAM_COVER_URL = "coverURL"
        private const val PARAM_SOURCE = "source"
        private const val PARAM_TYPE = "type"

        const val QUERY_NAME = "BroadcasterAddMediasUseCaseQuery"
        const val QUERY = """
            mutation BroadcasterAddMedias(
                ${"$$PARAM_REQ"}: BroadcasterAddMediaRequest
            ) {
                broadcasterAddMedias(
                    $PARAM_REQ: ${"$$PARAM_REQ"} 
                ) {
                    mediaIDs
                }
            }
        """
    }
}
