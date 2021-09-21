package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.broadcaster.domain.model.SetChannelTagsResponse
import com.tokopedia.play.broadcaster.util.handler.DefaultUseCaseHandler
import com.tokopedia.play_common.util.extension.defaultErrorMessage
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by jegul on 22/04/21
 */
class SetChannelTagsUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val dispatcher: CoroutineDispatchers
) : UseCase<SetChannelTagsResponse>() {

    private val query = """
        mutation SetChannelTags(${'$'}$PARAM_CHANNEL_ID: String!, ${'$'}$PARAM_TAGS: [String!]!) {
            broadcasterSetChannelTags(channelID: ${'$'}$PARAM_CHANNEL_ID, tags: ${'$'}$PARAM_TAGS) {
                success
            }
        }
    """

    private var params: Map<String, Any> = emptyMap()

    override suspend fun executeOnBackground(): SetChannelTagsResponse = withContext(dispatcher.io) {
        val gqlResponse = DefaultUseCaseHandler(
                gqlRepository = graphqlRepository,
                query = query,
                typeOfT = SetChannelTagsResponse::class.java,
                params = params,
                gqlCacheStrategy = GraphqlCacheStrategy
                        .Builder(CacheType.ALWAYS_CLOUD).build()
        ).executeWithRetry()
        val response = gqlResponse.getData<SetChannelTagsResponse>(SetChannelTagsResponse::class.java)
        val errors = gqlResponse.getError(SetChannelTagsResponse::class.java)
        if (response != null && errors.isNullOrEmpty()) {
            return@withContext response
        } else {
            throw MessageErrorException(errors.defaultErrorMessage)
        }
    }

    fun setParams(channelId: String, tags: Set<String>) {
        params = mapOf(
                PARAM_CHANNEL_ID to channelId,
                PARAM_TAGS to tags
        )
    }

    companion object {

        private const val PARAM_CHANNEL_ID = "channelId"
        private const val PARAM_TAGS = "tags"
    }
}