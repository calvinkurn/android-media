package com.tokopedia.play.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.data.PlayReminder
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 14, 2021
 */
class PlayChannelReminderUseCase @Inject constructor(
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

        fun checkRequestSuccess(playReminder: PlayReminder) =
            playReminder.playToggleChannelReminder.header.status == RESPONSE_STATUS_SUCCESS

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