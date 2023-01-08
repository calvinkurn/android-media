package com.tokopedia.people.domains

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.people.model.VideoPostReimderModel
import javax.inject.Inject

@GqlQuery(VideoPostReminderUseCase.QUERY_NAME, VideoPostReminderUseCase.QUERY)
class VideoPostReminderUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
) : GraphqlUseCase<VideoPostReimderModel>(graphqlRepository) {

    init {
        setGraphqlQuery(VideoPostReminderUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(VideoPostReimderModel::class.java)
    }

    suspend fun executeOnBackground(
        channelId: String,
        isActive: Boolean,
    ): VideoPostReimderModel {
        val request = mapOf(
            KEY_CHANNEL_ID to channelId,
            KEY_SET_ACTIVE to isActive,
        )
        setRequestParams(request)

        return executeOnBackground()
    }

    companion object {
        private const val KEY_CHANNEL_ID = "channelID"
        private const val KEY_SET_ACTIVE = "setActive"

        const val QUERY_NAME = "VideoPostReminderUseCaseQuery"
        const val QUERY = """
            mutation playToggleChannelReminder(
                ${"$$KEY_CHANNEL_ID"}: String, 
                ${"$$KEY_SET_ACTIVE"}: Boolean
            ) {
                playToggleChannelReminder(input: {
                    $KEY_CHANNEL_ID: ${"$$KEY_CHANNEL_ID"}, 
                    $KEY_SET_ACTIVE: ${"$$KEY_SET_ACTIVE"}
                }) {
                  header {
                    message
                    status
                  }
                }
            }
        """
    }
}
