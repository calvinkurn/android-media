package com.tokopedia.officialstore.official.domain

import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.officialstore.GQLQueryConstant.QUERY_OFFICIAL_STORE_BANNERS
import com.tokopedia.officialstore.official.data.model.OfficialStoreBanners
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class GetOfficialStoreBannerUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase,
        @Named(QUERY_OFFICIAL_STORE_BANNERS) val query: String
): UseCase<OfficialStoreBanners>() {

    var params: Map<String, Any> = mapOf()

    override suspend fun executeOnBackground(): OfficialStoreBanners {
        val gqlRequest  = GraphqlRequest(query, OfficialStoreBanners.Response::class.java, params, false)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(gqlRequest)
        val graphqlResponse = graphqlUseCase.executeOnBackground()
        return graphqlResponse.run {
            getData<OfficialStoreBanners.Response>(OfficialStoreBanners.Response::class.java).officialStoreBanners
        }
    }

    suspend fun executeOnBackground(isCache: Boolean): OfficialStoreBanners {
        if(isCache){
            graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).setExpiryTime(GraphqlConstant.ExpiryTimes.MONTHS_3.`val`()).build())
        }
        else{
            graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).setExpiryTime(GraphqlConstant.ExpiryTimes.MONTHS_3.`val`()).build())
        }
        return executeOnBackground()
    }

    companion object {

        const val SLUG = "page"

        fun createParams(slug: String): Map<String, Any> {
            return mutableMapOf<String, Any>().apply {
                put(SLUG, slug)
            }
        }
    }
}