package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.broadcaster.domain.model.GetAddedChannelTagsResponse
import com.tokopedia.play.broadcaster.util.handler.DefaultUseCaseHandler
import com.tokopedia.play_common.util.extension.defaultErrorMessage
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by jegul on 22/04/21
 */
class GetAddedChannelTagsUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val dispatcher: CoroutineDispatchers
) : UseCase<GetAddedChannelTagsResponse>() {

    private val query = """
        query GetAddedChannelTags(${'$'}$PARAM_CHANNEL_ID: String!) {
            broadcasterGetChannelTags(channelID: ${'$'}$PARAM_CHANNEL_ID) {
                tags
            }
        }
    """

    private var params: Map<String, Any> = emptyMap()

    override suspend fun executeOnBackground(): GetAddedChannelTagsResponse = withContext(dispatcher.io) {
        val gqlResponse = DefaultUseCaseHandler(
                gqlRepository = graphqlRepository,
                query = query,
                typeOfT = GetAddedChannelTagsResponse::class.java,
                params = params,
                gqlCacheStrategy = GraphqlCacheStrategy
                        .Builder(CacheType.ALWAYS_CLOUD).build()
        ).executeWithRetry()
        val response = gqlResponse.getData<GetAddedChannelTagsResponse>(GetAddedChannelTagsResponse::class.java)
        val errors = gqlResponse.getError(GetAddedChannelTagsResponse::class.java)
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