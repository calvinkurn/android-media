package com.tokopedia.play_common.domain.usecases

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play_common.domain.model.PlayToggleChannelReminder
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

class PlayToggleChannelReminderUseCase(
        private val graphqlUseCase: GraphqlUseCase<PlayToggleChannelReminder>
) : UseCase<PlayToggleChannelReminder>(){
    private val channel = "\$channel"
    private val active = "\$active"
    private val query = """
        {
          query playToggleChannelReminder(
            channel: String,
            active: Boolean
          ){
            playToggleChannelReminder(channel_id: $channel, set_active: $active){
              header{
                status
              }
            }

          }
        }
    """.trimIndent()
    private val params = RequestParams.create()

    init {
        graphqlUseCase.setGraphqlQuery(query)
        graphqlUseCase.setTypeClass(PlayToggleChannelReminder::class.java)
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        params.parameters.clear()
    }

    override suspend fun executeOnBackground(): PlayToggleChannelReminder {
        return graphqlUseCase.executeOnBackground()
    }

    fun setParams(channel: String, active: Boolean){
        params.parameters.clear()
        params.putString(PARAM_CHANNEL, channel)
        params.putBoolean(PARAM_ACTIVE, active)
    }

    companion object{
        private const val PARAM_CHANNEL = "channel"
        private const val PARAM_ACTIVE = "active"

    }
}