package com.tokopedia.home.beranda.domain.interactor.repository

import android.os.Bundle
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.home.beranda.di.module.query.QueryHome
import com.tokopedia.home.beranda.domain.interactor.HomeRepository
import com.tokopedia.home.beranda.domain.model.HomeChannelData
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class HomeDynamicChannelsRepository @Inject constructor(
        private val graphqlRepository: GraphqlRepository
): HomeRepository<HomeChannelData> {
    suspend fun getDynamicChannelData(params: RequestParams): HomeChannelData {
        try {
            val gqlResponse = graphqlRepository.response(
                    listOf(buildRequest(params)), GraphqlCacheStrategy
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
        return GraphqlRequest(QueryHome.dynamicChannelQuery, HomeChannelData::class.java, params.parameters)
    }

    companion object{
        const val GROUP_IDS = "groupIDs"
        const val TOKEN = "token"
        const val NUM_OF_CHANNEL = "numOfChannel"
        const val PARAMS = "param"
        const val LOCATION = "location"

        fun buildParams(groupIds: String = "", token: String = "", numOfChannel: Int = 0, queryParams: String = "", locationParams: String = "", doQueryHash: Boolean = false)
        :RequestParams{
            val params = RequestParams.create()
            params.parameters.clear()
            params.putString(PARAMS, queryParams)
            params.putString(GROUP_IDS, groupIds)
            params.putString(TOKEN, token)
            params.putInt(NUM_OF_CHANNEL, numOfChannel)
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

        return getDynamicChannelData(
                buildParams(
                        groupIds = groupId,
                        token = token,
                        numOfChannel = numOfChannel,
                        queryParams = params,
                        locationParams = location
                )
        )
    }

    override suspend fun getCachedData(bundle: Bundle): HomeChannelData {
        return HomeChannelData()
    }
}