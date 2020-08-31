package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.domain.gql.CloseChannel
import com.tokopedia.home.beranda.domain.gql.CloseChannelMutation
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

class CloseChannelUseCase(
        private val graphqlUseCase: GraphqlUseCase<CloseChannelMutation>
) : UseCase<CloseChannel>(){
    private val params = RequestParams.create()
    init {
        graphqlUseCase.setTypeClass(CloseChannelMutation::class.java)
        params.parameters.clear()
    }

    override suspend fun executeOnBackground(): CloseChannel {
        graphqlUseCase.clearCache()
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setRequestParams(params.parameters)
        return graphqlUseCase.executeOnBackground().closeChannel
    }

    fun setParams(id: String){
        params.parameters.clear()
        params.putInt(PARAM_ID, id.toInt())
    }

    companion object{
        private const val PARAM_ID = "channelID"
    }
}