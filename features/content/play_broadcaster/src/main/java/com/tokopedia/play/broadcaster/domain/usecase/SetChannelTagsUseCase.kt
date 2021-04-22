package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.play.broadcaster.domain.model.GetRecommendedChannelTagsResponse
import com.tokopedia.play.broadcaster.domain.model.SetChannelTagsResponse
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by jegul on 22/04/21
 */
class SetChannelTagsUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository,
        private val dispatcher: CoroutineDispatchers
) : GraphqlUseCase<SetChannelTagsResponse>(graphqlRepository) {

    private val query = """
        mutation SetChannelTags(${'$'}$PARAM_CHANNEL_ID: String!, ${'$'}$PARAM_TAGS: [String!]!) {
            broadcasterSetChannelTags(channelID: ${'$'}$PARAM_CHANNEL_ID, tags: ${'$'}$PARAM_TAGS) {
                success
            }
        }
    """

    init {
        setGraphqlQuery(query)
        setTypeClass(SetChannelTagsResponse::class.java)
    }

    override suspend fun executeOnBackground(): SetChannelTagsResponse = withContext(dispatcher.io) {
        return@withContext super.executeOnBackground()
    }

    fun setParams(channelId: String, tags: List<String>) {
        setRequestParams(
                mapOf(
                        PARAM_CHANNEL_ID to channelId,
                        PARAM_TAGS to tags
                )
        )
    }

    companion object {

        private const val PARAM_CHANNEL_ID = "channelId"
        private const val PARAM_TAGS = "tags"
    }
}