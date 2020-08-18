package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.data.mapper.HomeDataMapper
import com.tokopedia.home.beranda.data.mapper.HomeDynamicChannelDataMapper
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.HomeChannelData
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetDynamicChannelsUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<HomeChannelData>,
        private val homeDynamicChannelDataMapper: HomeDynamicChannelDataMapper
) : UseCase<List<Visitable<*>>>(){
    private val params = RequestParams.create()

    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(HomeChannelData::class.java)
    }

    fun setParams(groupIds: String = ""){
        params.parameters.clear()
        params.putString(GROUP_IDS, groupIds)
    }

    override suspend fun executeOnBackground(): List<Visitable<*>> {
        graphqlUseCase.clearCache()
        graphqlUseCase.setRequestParams(params.parameters)
        val dynamicChannelData = graphqlUseCase.executeOnBackground()
        val homeViewModel = homeDynamicChannelDataMapper.mapToDynamicChannelDataModel(dynamicChannelData, false)
        return homeViewModel.filterIsInstance(HomeComponentVisitable::class.java)
    }

    companion object{
        private const val GROUP_IDS = "groupIDs"
    }

}