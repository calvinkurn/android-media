package com.tokopedia.home.beranda.domain.interactor.repository

import android.os.Bundle
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.home.beranda.di.module.query.DynamicChannelQuery
import com.tokopedia.home.beranda.di.module.query.DynamicChannelQueryV2
import com.tokopedia.home.beranda.domain.interactor.HomeRepository
import com.tokopedia.home.beranda.domain.model.HomeChannelData
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class HomeDynamicChannelsRepository @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val isUsingV2: Boolean
): HomeRepository<HomeChannelData> {
    suspend fun getDynamicChannelData(params: RequestParams): HomeChannelData {
        try {
            val gqlRequest = if(isUsingV2) buildRequestV2(params) else buildRequest(params)
            val gqlResponse = graphqlRepository.response(
                    listOf(gqlRequest), GraphqlCacheStrategy
                    .Builder(CacheType.ALWAYS_CLOUD).build())
            val errors = gqlResponse.getError(HomeChannelData::class.java)
            if (errors.isNullOrEmpty()) {
                val result: HomeChannelData = gqlResponse.getData(HomeChannelData::class.java)
                return result
            } else throw MessageErrorException(errors.joinToString { it.message })
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    private fun buildRequest(params: RequestParams): GraphqlRequest {
        return GraphqlRequest(DynamicChannelQuery(), HomeChannelData::class.java, params.parameters)
    }

    private fun buildRequestV2(params: RequestParams): GraphqlRequest {
        return GraphqlRequest(DynamicChannelQueryV2(), HomeChannelData::class.java, params.parameters)
    }

    companion object{
        const val GROUP_IDS = "groupIDs"
        const val TOKEN = "token"
        const val NUM_OF_CHANNEL = "numOfChannel"
        const val PARAMS = "param"
        const val LOCATION = "location"
        const val CHANNEL_IDS = "channelIDs"

        fun buildParams(
            groupIds: String = "",
            token: String = "",
            numOfChannel: Int = 0,
            queryParams: String = "",
            locationParams: String = ""
        ) : RequestParams {
            val params = RequestParams.create()
            params.parameters.clear()
            params.putString(PARAMS, queryParams)
            params.putString(GROUP_IDS, groupIds)
            params.putString(TOKEN, token)
            params.putInt(NUM_OF_CHANNEL, numOfChannel)
            params.putString(LOCATION, locationParams)
            return params
        }

        fun buildParamsV2(
            groupIds: String = "",
            channelIds: String = "0",
            queryParams: String = "",
            locationParams: String = ""
        ) : RequestParams {
            val params = RequestParams.create()
            params.parameters.clear()
            params.putString(PARAMS, queryParams)
            params.putString(GROUP_IDS, groupIds)
            params.putString(CHANNEL_IDS, channelIds)
            params.putString(LOCATION, locationParams)
            return params
        }
    }

    override suspend fun getRemoteData(bundle: Bundle): HomeChannelData {
        val groupId = bundle.getString(GROUP_IDS, "")
        val token = bundle.getString(TOKEN, "")
        val numOfChannel = bundle.getInt(NUM_OF_CHANNEL, 0)
        val params = bundle.getString(PARAMS, "")
        val location = bundle.getString(LOCATION, "")
        val channelIds = bundle.getString(CHANNEL_IDS, "")

        val requestParams = if(isUsingV2){
            buildParamsV2(
                groupIds = groupId,
                channelIds = channelIds,
                queryParams = params,
                locationParams = location
            )
        } else {
            buildParams(
                groupIds = groupId,
                token = token,
                numOfChannel = numOfChannel,
                queryParams = params,
                locationParams = location
            )
        }
        return getDynamicChannelData(requestParams)
    }

    override suspend fun getCachedData(bundle: Bundle): HomeChannelData {
        return HomeChannelData()
    }
}