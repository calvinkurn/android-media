package com.tokopedia.play.widget.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.play.widget.data.PlayWidgetReminder
import com.tokopedia.play.widget.data.PlayWidgetReminderResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject


/**
 * Created by mzennis on 21/10/20.
 */
class PlayWidgetReminderUseCase @Inject constructor(private val repository: GraphqlRepository) : UseCase<PlayWidgetReminder>() {

    var params: Map<String, Any> = emptyMap()

    override suspend fun executeOnBackground(): PlayWidgetReminder {
        val gqlRequest = GraphqlRequest(query, PlayWidgetReminderResponse::class.java, params)
        val gqlResponse = repository.getReseponse(
                listOf(gqlRequest),
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        )
        val errors = gqlResponse.getError(PlayWidgetReminderResponse::class.java)
        if (!errors.isNullOrEmpty()) {
            throw Throwable(errors.firstOrNull()?.message)
        }
        val response = gqlResponse.getData<PlayWidgetReminderResponse>(PlayWidgetReminderResponse::class.java)
        return response.data
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