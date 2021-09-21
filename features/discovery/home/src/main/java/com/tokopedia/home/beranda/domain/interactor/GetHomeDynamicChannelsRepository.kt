package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.home.beranda.di.module.query.QueryHome
import com.tokopedia.home.beranda.domain.model.HomeChannelData
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetHomeDynamicChannelsRepository @Inject constructor(
        private val graphqlRepository: GraphqlRepository
){
    suspend fun getDynamicChannelData(params: RequestParams): HomeChannelData {
        try {
            val gqlResponse = graphqlRepository.getReseponse(
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
        private const val GROUP_IDS = "groupIDs"
        private const val TOKEN = "token"
        private const val NUM_OF_CHANNEL = "numOfChannel"
        private const val PARAMS = "param"
        private const val LOCATION = "location"

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

}