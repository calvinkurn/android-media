package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetDynamicChannelsUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<HomeData>
) : UseCase<DynamicHomeChannel>(){
    private val params = RequestParams.create()

    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(HomeData::class.java)
    }

    fun setParams(groupIds: String){
        params.parameters.clear()
        params.putString(GROUP_IDS, groupIds)
    }

    override suspend fun executeOnBackground(): DynamicHomeChannel {
        graphqlUseCase.clearCache()
        graphqlUseCase.setRequestParams(params.parameters)
        return graphqlUseCase.executeOnBackground().dynamicHomeChannel
    }

    companion object{
        private const val GROUP_IDS = "groupIDs"
    }

}