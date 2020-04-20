package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.data.mapper.HomeDataMapper
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelDataModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetDynamicChannelsUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<HomeData>,
        private val homeDataMapper: HomeDataMapper
) : UseCase<List<DynamicChannelDataModel>>(){
    private val params = RequestParams.create()

    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(HomeData::class.java)
    }

    fun setParams(groupIds: String){
        params.parameters.clear()
        params.putString(GROUP_IDS, groupIds)
    }

    override suspend fun executeOnBackground(): List<DynamicChannelDataModel> {
        graphqlUseCase.clearCache()
        graphqlUseCase.setRequestParams(params.parameters)
        val homeData = graphqlUseCase.executeOnBackground()
        val homeViewModel = homeDataMapper.mapToHomeViewModel(homeData, false)
        return homeViewModel.list.filterIsInstance(DynamicChannelDataModel::class.java)
    }

    companion object{
        private const val GROUP_IDS = "groupIDs"
    }

}