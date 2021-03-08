package com.tokopedia.play.widget.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.widget.data.PlayWidgetReminder
import javax.inject.Inject


/**
 * Created by mzennis on 21/10/20.
 */
class PlayWidgetReminderUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository
) : GraphqlUseCase<PlayWidgetReminder>(graphqlRepository) {

    init {
        setGraphqlQuery(query)
        setCacheStrategy(GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(PlayWidgetReminder::class.java)
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