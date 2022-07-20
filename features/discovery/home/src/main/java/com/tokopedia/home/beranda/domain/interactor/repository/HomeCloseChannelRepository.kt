package com.tokopedia.home.beranda.domain.interactor.repository

import android.os.Bundle
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.domain.gql.CloseChannel
import com.tokopedia.home.beranda.domain.gql.CloseChannelMutation
import com.tokopedia.home.beranda.domain.interactor.HomeRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

class HomeCloseChannelRepository(
        private val graphqlUseCase: GraphqlUseCase<CloseChannelMutation>
) : UseCase<CloseChannel>(), HomeRepository<CloseChannel>{
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

    override suspend fun getRemoteData(bundle: Bundle): CloseChannel {
        return executeOnBackground()
    }

    override suspend fun getCachedData(bundle: Bundle): CloseChannel {
        return getRemoteData()
    }

    fun setParams(id: String){
        params.parameters.clear()
        params.putInt(PARAM_ID, id.toInt())
    }

    companion object{
        private const val PARAM_ID = "channelID"
    }
}