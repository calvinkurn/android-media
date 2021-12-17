package com.tokopedia.home.beranda.domain.interactor.repository

import android.os.Bundle
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.home.beranda.di.module.query.QueryHome
import com.tokopedia.home.beranda.domain.interactor.HomeRepository
import com.tokopedia.home.beranda.domain.model.HomeIconData
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class HomeIconRepository @Inject constructor(
        private val graphqlRepository: GraphqlRepository
): HomeRepository<HomeIconData> {
    suspend fun getIconData(param: String = "", locationParams: String = ""): HomeIconData {
        val gqlResponse  = graphqlRepository.response(
                listOf(buildRequest(param, locationParams)), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val errors = gqlResponse.getError(HomeIconData::class.java)
        if (errors.isNullOrEmpty()) {
            return gqlResponse.getData(HomeIconData::class.java)
        } else throw MessageErrorException(errors.joinToString { it.message })
    }

    private fun buildRequest(param: String, locationParams: String): GraphqlRequest {
        return GraphqlRequest(QueryHome.homeIconQuery, HomeIconData::class.java, mapOf(PARAM to param, PARAM_LOCATION to locationParams))
    }

    companion object{
        private const val PARAM = "param"
        private const val PARAM_LOCATION = "location"
    }

    override suspend fun getRemoteData(bundle: Bundle): HomeIconData {
        val paramValue = bundle.getString(PARAM, "")
        val paramLocationValue = bundle.getString(PARAM_LOCATION, "")

        return getIconData(paramValue, paramLocationValue)
    }

    override suspend fun getCachedData(bundle: Bundle): HomeIconData {
        return HomeIconData()
    }
}