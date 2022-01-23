package com.tokopedia.home.beranda.domain.interactor.repository

import android.os.Bundle
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.home.beranda.di.module.query.QueryHome
import com.tokopedia.home.beranda.domain.interactor.HomeRepository
import com.tokopedia.home.beranda.domain.model.HomeIconData
import com.tokopedia.home.beranda.domain.model.HomeTickerData
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class HomePlayWidgetRepository @Inject constructor(
        private val graphqlRepository: GraphqlRepository
): HomeRepository<HomeTickerData> {
    val gqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    suspend fun getTickerData(locationParams: String = ""): HomeTickerData {
        val gqlResponse = gqlRepository.response(
                listOf(buildRequest(locationParams)), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val errors = gqlResponse.getError(HomeIconData::class.java)
        if (errors.isNullOrEmpty()) {
            val result: HomeTickerData = gqlResponse.getData(HomeTickerData::class.java)
            return result
        } else throw MessageErrorException(errors.joinToString { it.message })
    }

    private fun buildRequest(locationParams: String): GraphqlRequest {
        return GraphqlRequest(QueryHome.homeTickerQuery, HomeTickerData::class.java, mapOf(PARAM_LOCATION to locationParams))
    }


    companion object{
        const val PARAM_LOCATION = "location"
    }

    override suspend fun getRemoteData(bundle: Bundle): HomeTickerData {
        val paramLocationValue = bundle.getString(PARAM_LOCATION, "")
        return getTickerData(locationParams =  paramLocationValue)
    }

    override suspend fun getCachedData(bundle: Bundle): HomeTickerData {
        return HomeTickerData()
    }
}