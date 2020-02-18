package com.tokopedia.home.beranda.data.datasource.remote

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.home.beranda.common.HomeDispatcherProvider
import com.tokopedia.home.beranda.data.model.KeywordSearchData
import com.tokopedia.home.beranda.domain.gql.searchHint.KeywordSearchHintQuery
import kotlinx.coroutines.withContext

class KeywordSearchRemoteDataSource(
        private val graphqlRepository: GraphqlRepository,
        private val dispatchers: HomeDispatcherProvider) {

    suspend fun getSearchHint() = withContext(dispatchers.io()){
        val cacheStrategy =
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        val gqlRecommendationRequest = GraphqlRequest(
                KeywordSearchHintQuery.query,
                KeywordSearchData::class.java,
                mapOf()
        )
        val response = graphqlRepository.getReseponse(listOf(gqlRecommendationRequest), cacheStrategy)
        val errors = response.getError(KeywordSearchData::class.java)
        if(errors?.isNotEmpty() == true){
            error(errors.first().message)
        }
        response.getSuccessData<KeywordSearchData>()
    }
}