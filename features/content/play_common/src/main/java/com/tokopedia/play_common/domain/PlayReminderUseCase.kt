package com.tokopedia.play_common.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play_common.domain.model.PlayReminder
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 03, 2021
 */
class PlayWidgetReminderUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<PlayReminder>(graphqlRepository) {

    init {
        setGraphqlQuery(query)
        setCacheStrategy(
            GraphqlCacheStrategy
            .Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(PlayReminder::class.java)
    }

    companion object {

        private const val query = """
        mutation playToggleChannelReminder(
            ${'$'}channel: String,
            ${'$'}active: Boolean
          ){
            playToggleChannelReminder(input:{channelID: ${'$'}channel, setActive: ${'$'}active}){
              header{
                status
              }
            }
          }
        """

        private const val PARAM_CHANNEL = "channel"
        private const val PARAM_ACTIVE = "active"

        const val RESPONSE_STATUS_SUCCESS = 200

        @JvmStatic
        fun createParams(
            channelId: String,
            remind: Boolean
        ): Map<String, Any> = mapOf(
            PARAM_CHANNEL to channelId,
            PARAM_ACTIVE to remind
        )
    }
}