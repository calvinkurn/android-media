package com.tokopedia.officialstore.official.domain

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.home_component.model.DynamicChannelLayout
import com.tokopedia.officialstore.official.data.mapper.OfficialProductCardMapper
import com.tokopedia.officialstore.official.data.model.OfficialStoreChannel
import com.tokopedia.officialstore.official.data.model.dynamic_channel.DynamicChannel
import com.tokopedia.officialstore.official.di.query.DynamicHomeChannelQuery
import com.tokopedia.usecase.coroutines.UseCase
import java.lang.reflect.Type
import javax.inject.Inject

class GetOfficialStoreDynamicChannelUseCase @Inject constructor(
    private val officialProductCardMapper: OfficialProductCardMapper,
    private val graphqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<List<OfficialStoreChannel>>() {
    private val paramChannelType = "type"
    private val paramChannelLocation = "location"

    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    var requestParams: MutableMap<String, Any> = mutableMapOf()
        private set

    override suspend fun executeOnBackground(): List<OfficialStoreChannel> {
        val responseType: Type = DynamicChannel.Response::class.java
        val requestInstance = GraphqlRequest(DynamicHomeChannelQuery(), responseType, requestParams)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(requestInstance)

        val data = graphqlUseCase.executeOnBackground().getData<DynamicChannel.Response>(responseType).dynamicHomeChannel

        return data.channels.map {
            val includeMapping = it.layout in DYNAMIC_HEIGHT_CHANNEL
            val list = if(includeMapping) officialProductCardMapper.mappingProductCards(it.grids) else listOf()
            val height = if(includeMapping) officialProductCardMapper.getMaxHeightProductCards(list) else -1
            OfficialStoreChannel(it, list, height)
        }
    }

    fun setupParams(channelType: String, location: String) {
        if (channelType.isNotEmpty()) {
            requestParams[paramChannelType] = channelType
            requestParams[paramChannelLocation] = location
        }
    }

    companion object{
        private val DYNAMIC_HEIGHT_CHANNEL = listOf(DynamicChannelLayout.LAYOUT_MIX_LEFT, DynamicChannelLayout.LAYOUT_MIX_TOP)
    }
}
