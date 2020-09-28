package com.tokopedia.play_common.domain.usecases

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play_common.domain.model.PlayToggleChannelEntity
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

class PlayToggleChannelReminderUseCase(
        private val graphqlUseCase: GraphqlUseCase<PlayToggleChannelEntity>
) : UseCase<PlayToggleChannelEntity>(){

    private val query = """
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
    private val params = RequestParams.create()

    init {
        graphqlUseCase.setGraphqlQuery(query)
        graphqlUseCase.setTypeClass(PlayToggleChannelEntity::class.java)
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        params.parameters.clear()
    }

    override suspend fun executeOnBackground(): PlayToggleChannelEntity {
        graphqlUseCase.setRequestParams(params.parameters)
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