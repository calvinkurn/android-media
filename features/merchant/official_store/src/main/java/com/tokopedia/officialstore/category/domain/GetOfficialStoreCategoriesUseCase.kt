package com.tokopedia.officialstore.category.domain

import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.officialstore.category.data.model.OfficialStoreCategories
import com.tokopedia.officialstore.official.di.query.OSCategoriesQuery
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetOfficialStoreCategoriesUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase
): UseCase<OfficialStoreCategories>() {

    private var doQueryHashing : Boolean = false

    override suspend fun executeOnBackground(): OfficialStoreCategories {
        val gqlRequest = GraphqlRequest(OSCategoriesQuery(), OfficialStoreCategories.Response::class.java)
        gqlRequest.isDoQueryHash = doQueryHashing
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(gqlRequest)
        val response = graphqlUseCase.executeOnBackground()
            .getData<OfficialStoreCategories.Response>(OfficialStoreCategories.Response::class.java)

        response?.let {
            return it.OfficialStoreCategories
        }
        return OfficialStoreCategories()
    }

    suspend fun executeOnBackground(isCache: Boolean, doQueryHashing : Boolean): OfficialStoreCategories{
        this.doQueryHashing = doQueryHashing
        if(isCache){
            graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_ONLY).setExpiryTime(GraphqlConstant.ExpiryTimes.MONTHS_3.`val`()).build())
        }
        else{
            graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).setExpiryTime(GraphqlConstant.ExpiryTimes.MONTHS_3.`val`()).build())
        }
        return executeOnBackground()
    }

}