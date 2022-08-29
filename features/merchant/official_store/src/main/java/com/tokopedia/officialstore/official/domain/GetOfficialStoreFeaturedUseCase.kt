package com.tokopedia.officialstore.official.domain

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.officialstore.official.data.model.OfficialStoreFeaturedShop
import com.tokopedia.officialstore.official.di.query.OSFeaturedShopQuery
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetOfficialStoreFeaturedUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase
): UseCase<OfficialStoreFeaturedShop>() {

    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    var params: Map<String, Any> = mapOf()

    override suspend fun executeOnBackground(): OfficialStoreFeaturedShop {
        val gqlRequest  = GraphqlRequest(OSFeaturedShopQuery(), OfficialStoreFeaturedShop.Response::class.java, params, false)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(gqlRequest)
        val graphqlResponse = graphqlUseCase.executeOnBackground()
        return graphqlResponse.run {
            getData<OfficialStoreFeaturedShop.Response>(OfficialStoreFeaturedShop.Response::class.java)
                    .officialStoreFeaturedShop
        }
    }

    companion object {

        const val CATEGORY_ALIAS_ID = "categoryAliasID"

        fun createParams(categoryId: Int): Map<String, Any> {
            return mutableMapOf<String, Any>().apply {
                put(CATEGORY_ALIAS_ID, categoryId)
            }
        }
    }
}