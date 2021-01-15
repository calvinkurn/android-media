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
    suspend fun getIconData(param: String = ""): HomeIconData {
        val gqlResponse = graphqlRepository.getReseponse(
                listOf(buildRequest(param)), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val errors = gqlResponse.getError(HomeIconData::class.java)
        if (errors.isNullOrEmpty()) {
            return gqlResponse.getData(HomeIconData::class.java)
        } else throw MessageErrorException(errors.joinToString { it.message })
    }

    private fun buildRequest(param: String): GraphqlRequest {
        return GraphqlRequest(QueryHome.homeIconQuery, HomeIconData::class.java, mapOf(PARAM to param))
    }

    companion object{
        private const val PARAM = "param"
    }
}