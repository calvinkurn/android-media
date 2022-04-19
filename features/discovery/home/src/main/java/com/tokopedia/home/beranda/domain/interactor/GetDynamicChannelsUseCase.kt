package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.data.mapper.HomeDynamicChannelDataMapper
import com.tokopedia.home.beranda.domain.model.HomeChannelData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetDynamicChannelsUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<HomeChannelData>,
        private val homeDynamicChannelDataMapper: HomeDynamicChannelDataMapper
) : UseCase<HomeChannelData>(){
    private val params = RequestParams.create()
    private var doQueryHash = false

    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(HomeChannelData::class.java)
    }

    fun setParams(groupIds: String = "", token: String = "", numOfChannel: Int = 0, queryParams: String = "", doQueryHash: Boolean = false){
        params.parameters.clear()
        params.putString(PARAMS, queryParams)
        params.putString(GROUP_IDS, groupIds)
        params.putString(TOKEN, token)
        params.putInt(NUM_OF_CHANNEL, numOfChannel)
        this@GetDynamicChannelsUseCase.doQueryHash = doQueryHash
    }

    override suspend fun executeOnBackground(): HomeChannelData {
        graphqlUseCase.clearCache()
        graphqlUseCase.setQueryHashFlag(doQueryHash)
        graphqlUseCase.setRequestParams(params.parameters)
        return graphqlUseCase.executeOnBackground()
    }

    companion object{
        private const val GROUP_IDS = "groupIDs"
        private const val TOKEN = "token"
        private const val NUM_OF_CHANNEL = "numOfChannel"
        private const val PARAMS = "param"
    }

}