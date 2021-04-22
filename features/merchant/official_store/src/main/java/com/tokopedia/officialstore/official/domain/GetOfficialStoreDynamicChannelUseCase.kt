package com.tokopedia.officialstore.official.domain

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.officialstore.DynamicChannelIdentifiers
import com.tokopedia.officialstore.GQLQueryConstant
import com.tokopedia.officialstore.official.data.mapper.OfficialHomeMapper
import com.tokopedia.officialstore.official.data.model.OfficialStoreChannel
import com.tokopedia.officialstore.official.data.model.dynamic_channel.DynamicChannel
import com.tokopedia.usecase.coroutines.UseCase
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Named

class GetOfficialStoreDynamicChannelUseCase @Inject constructor(
        private val officialHomeMapper: OfficialHomeMapper,
        private val graphqlUseCase: MultiRequestGraphqlUseCase,
        @Named(GQLQueryConstant.QUERY_OFFICIAL_STORE_DYNAMIC_CHANNEL) val gqlQuery: String
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
        val requestInstance = GraphqlRequest(gqlQuery, responseType, requestParams)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(requestInstance)

        val data = graphqlUseCase.executeOnBackground().getData<DynamicChannel.Response>(responseType).dynamicHomeChannel

        return data.channels.map {
            val includeMapping = it.layout in DYNAMIC_HEIGHT_CHANNEL
            val list = if(includeMapping) officialHomeMapper.mappingProductCards(it.grids) else listOf()
            val height = if(includeMapping) officialHomeMapper.getMaxHeightProductCards(list) else -1
            OfficialStoreChannel(it, list, height)
        }
    }

    fun setupParams(channelType: String, location: String) {
        if (channelType.isNotEmpty()) {
            requestParams[paramChannelType] = channelType
        }
    }

    companion object{
        private val DYNAMIC_HEIGHT_CHANNEL = listOf(DynamicChannelIdentifiers.LAYOUT_MIX_LEFT, DynamicChannelIdentifiers.LAYOUT_MIX_TOP)
    }
}
