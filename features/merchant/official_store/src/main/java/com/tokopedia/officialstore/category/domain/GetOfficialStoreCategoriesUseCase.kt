package com.tokopedia.officialstore.category.domain

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.officialstore.GQLQueryConstant.QUERY_OFFICIAL_STORE_CATEGORIES
import com.tokopedia.officialstore.category.data.model.OfficialStoreCategories
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class GetOfficialStoreCategoriesUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase,
        @Named(QUERY_OFFICIAL_STORE_CATEGORIES) val query: String
): UseCase<OfficialStoreCategories>() {

    override suspend fun executeOnBackground(): OfficialStoreCategories {
        val gqlRequest = GraphqlRequest(query, OfficialStoreCategories.Response::class.java)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(gqlRequest)
        val graphqlResponse = graphqlUseCase.executeOnBackground()

        return graphqlResponse.run {
            getData<OfficialStoreCategories.Response>(OfficialStoreCategories.Response::class.java)
                    .OfficialStoreCategories
        }
    }

    suspend fun executeOnBackground(isCache: Boolean): OfficialStoreCategories{
        if(isCache){
            graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build())
        }
        else{
            graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        }
        return executeOnBackground()
    }

}