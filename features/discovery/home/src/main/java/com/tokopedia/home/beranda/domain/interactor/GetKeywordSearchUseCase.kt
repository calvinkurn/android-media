package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.data.model.KeywordSearchData
import com.tokopedia.home.beranda.domain.gql.searchHint.KeywordSearchHintQuery
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetKeywordSearchUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<KeywordSearchData>
): UseCase<KeywordSearchData>(){

    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(KeywordSearchData::class.java)
    }

    override suspend fun executeOnBackground(): KeywordSearchData {
        graphqlUseCase.clearCache()
        graphqlUseCase.setGraphqlQuery(KeywordSearchHintQuery.query)
        graphqlUseCase.setRequestParams(mapOf())
        return graphqlUseCase.executeOnBackground()
    }
}