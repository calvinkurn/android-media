package com.tokopedia.home.beranda.domain.interactor.repository

import android.os.Bundle
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.data.model.PlayData
import com.tokopedia.home.beranda.data.model.PlayLiveDynamicChannelEntity
import com.tokopedia.home.beranda.data.query.PlayLiveDynamicChannelQuery
import com.tokopedia.home.beranda.domain.interactor.HomeRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class HomePlayLiveDynamicRepository @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<PlayLiveDynamicChannelEntity>
): UseCase<PlayData>(), HomeRepository<PlayData> {
    companion object{
        private const val PARAM_PAGE = "page"
        private const val PARAM_SOURCE = "source"
        private const val PARAM_LIMIT = "limit"
        private const val PARAM_DEVICE = "device"
        private const val DEFAULT_SOURCE = "homepage"
        private const val DEFAULT_LIMIT = 1
        private const val DEFAULT_PAGE = 1
        private const val DEFAULT_DEVICE = "android"
    }

    private val params = RequestParams.create()
    init {
        graphqlUseCase.setTypeClass(PlayLiveDynamicChannelEntity::class.java)
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        params.parameters.clear()
    }

    override suspend fun executeOnBackground(): PlayData {
        graphqlUseCase.clearCache()
        graphqlUseCase.setGraphqlQuery(PlayLiveDynamicChannelQuery.getQuery())
        graphqlUseCase.setRequestParams(params.parameters)
        return graphqlUseCase.executeOnBackground().playDynamicData.playData
    }

    fun setParams(source: String = DEFAULT_SOURCE, page: Int = DEFAULT_PAGE, limit: Int = DEFAULT_LIMIT){
        params.parameters.clear()
        params.putString(PARAM_SOURCE, source)
        params.putInt(PARAM_PAGE, page)
        params.putInt(PARAM_LIMIT, limit)
        params.putString(PARAM_DEVICE, DEFAULT_DEVICE)
    }

    override suspend fun getRemoteData(bundle: Bundle): PlayData {
        return executeOnBackground()
    }

    override suspend fun getCachedData(bundle: Bundle): PlayData {
        return PlayData()
    }
}