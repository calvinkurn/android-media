package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.play.broadcaster.domain.model.GetRecommendedChannelTagsResponse
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by jegul on 22/04/21
 */
class GetRecommendedChannelTagsUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository,
        private val dispatcher: CoroutineDispatchers
) : GraphqlUseCase<GetRecommendedChannelTagsResponse>(graphqlRepository) {

    private val query = """
        query GetRecommendedChannelTags(${'$'}$PARAM_CHANNEL_ID: String!) {
            broadcasterGetRecommendedTags(channelID: ${'$'}$PARAM_CHANNEL_ID) {
                tags
            }
        }
    """

    init {
        setGraphqlQuery(query)
        setTypeClass(GetRecommendedChannelTagsResponse::class.java)
    }

    override suspend fun executeOnBackground(): GetRecommendedChannelTagsResponse = withContext(dispatcher.io) {
        return@withContext super.executeOnBackground()
    }

    fun setChannelId(channelId: String) {
        setRequestParams(
                mapOf(
                        PARAM_CHANNEL_ID to channelId
                )
        )
    }

    companion object {

        private const val PARAM_CHANNEL_ID = "channelId"
    }
}