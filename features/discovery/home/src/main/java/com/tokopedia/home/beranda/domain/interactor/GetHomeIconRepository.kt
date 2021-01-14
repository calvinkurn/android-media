package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.home.beranda.di.module.query.QueryHome
import com.tokopedia.home.beranda.domain.model.HomeIconData
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class GetHomeIconRepository @Inject constructor(
        private val graphqlRepository: GraphqlRepository
){
    suspend fun getIconData(): HomeIconData {
        val gqlResponse = graphqlRepository.getReseponse(
                listOf(buildRequest()), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val errors = gqlResponse.getError(HomeIconData::class.java)
        if (errors.isNullOrEmpty()) {
            val result: HomeIconData = gqlResponse.getData(HomeIconData::class.java)
            return result
        } else throw MessageErrorException(errors.joinToString { it.message })
    }

    private fun buildRequest(): GraphqlRequest {
        return GraphqlRequest(QueryHome.homeIconQuery, HomeIconData::class.java)
    }
}