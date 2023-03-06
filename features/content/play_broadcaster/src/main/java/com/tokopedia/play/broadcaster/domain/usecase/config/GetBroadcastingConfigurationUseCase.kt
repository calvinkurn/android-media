package com.tokopedia.play.broadcaster.domain.usecase.config

import com.tokopedia.content.common.types.ContentCommonUserType
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.broadcaster.domain.model.config.GetBroadcastingConfigurationResponse
import com.tokopedia.play.broadcaster.domain.usecase.config.GetBroadcastingConfigurationUseCase.Companion.QUERY_BROADCASTING_CONFIGURATION
import com.tokopedia.play.broadcaster.domain.usecase.config.GetBroadcastingConfigurationUseCase.Companion.QUERY_NAME
import javax.inject.Inject

@GqlQuery(QUERY_NAME, QUERY_BROADCASTING_CONFIGURATION)
class GetBroadcastingConfigurationUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<GetBroadcastingConfigurationResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(GetBroadcastingConfigurationUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(GetBroadcastingConfigurationResponse::class.java)
    }

    private fun createRequestParams(authorId: String, authorType: String) {
        val request = mapOf(
            PARAMS_AUTHOR_ID to authorId,
            PARAMS_AUTHOR_TYPE to when (authorType) {
                ContentCommonUserType.TYPE_USER -> ContentCommonUserType.VALUE_TYPE_ID_USER
                ContentCommonUserType.TYPE_SHOP -> ContentCommonUserType.VALUE_TYPE_ID_SHOP
                else -> 0
            },
        )
        setRequestParams(request)
    }

    suspend fun execute(
        authorId: String,
        authorType: String
    ): GetBroadcastingConfigurationResponse {
        this.createRequestParams(authorId, authorType)
        return executeOnBackground()
    }

    companion object {
        private const val PARAMS_AUTHOR_ID = "authorID"
        private const val PARAMS_AUTHOR_TYPE = "authorType"
        const val QUERY_NAME = "GetBroadcastingConfigurationUseCaseQuery"
        const val QUERY_BROADCASTING_CONFIGURATION = """
            query BroadcasterGetBroadcastingConfig(
              ${"$$PARAMS_AUTHOR_ID"}: String!, 
              ${"$$PARAMS_AUTHOR_TYPE"}: Int!
            ) {
              broadcasterGetBroadcastingConfig(
               $PARAMS_AUTHOR_ID: ${"$$PARAMS_AUTHOR_ID"}, 
               $PARAMS_AUTHOR_TYPE: ${"$$PARAMS_AUTHOR_TYPE"}
              ) {
                authorID
                authorType
                config {
                 videoWidth
                 videoHeight
                 fps
                 videoBitrate
                 audioRate
                 maxRetry
                 reconnectDelay
                 bitrateMode
                }
              }
            }
        """
    }
}
