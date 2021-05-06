package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.broadcaster.domain.model.GetRecommendedChannelTagsResponse
import com.tokopedia.play.broadcaster.util.handler.DefaultUseCaseHandler
import com.tokopedia.play_common.util.extension.defaultErrorMessage
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by jegul on 22/04/21
 */
class GetRecommendedChannelTagsUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val dispatcher: CoroutineDispatchers
) : UseCase<GetRecommendedChannelTagsResponse>() {

    private val query = """
        query GetRecommendedChannelTags(${'$'}$PARAM_CHANNEL_ID: String!) {
            broadcasterGetRecommendedTags(channelID: ${'$'}$PARAM_CHANNEL_ID) {
                tags
            }
        }
    """

    private var params: Map<String, Any> = emptyMap()

    override suspend fun executeOnBackground(): GetRecommendedChannelTagsResponse = withContext(dispatcher.io) {
        val gqlResponse = DefaultUseCaseHandler(
                gqlRepository = graphqlRepository,
                query = query,
                typeOfT = GetRecommendedChannelTagsResponse::class.java,
                params = params,
                gqlCacheStrategy = GraphqlCacheStrategy
                        .Builder(CacheType.ALWAYS_CLOUD).build()
        ).executeWithRetry()
        val response = gqlResponse.getData<GetRecommendedChannelTagsResponse>(GetRecommendedChannelTagsResponse::class.java)
        val errors = gqlResponse.getError(GetRecommendedChannelTagsResponse::class.java)
        if (response != null && errors.isNullOrEmpty()) {
            return@withContext response
        } else {
            throw MessageErrorException(errors.defaultErrorMessage)
        }
    }

    fun setChannelId(channelId: String) {
        params = mapOf(
                PARAM_CHANNEL_ID to channelId
        )
    }

    companion object {

        private const val PARAM_CHANNEL_ID = "channelId"
    }
}