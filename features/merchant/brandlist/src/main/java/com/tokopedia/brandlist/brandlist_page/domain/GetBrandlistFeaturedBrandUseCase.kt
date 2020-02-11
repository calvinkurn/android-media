package com.tokopedia.brandlist.brandlist_page.domain

import com.tokopedia.brandlist.brandlist_page.data.model.BrandlistFeaturedBrandResponse
import com.tokopedia.brandlist.brandlist_page.data.model.OfficialStoreFeaturedShop
import com.tokopedia.brandlist.common.GQLQueryConstant
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class GetBrandlistFeaturedBrandUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase,
        @Named(GQLQueryConstant.QUERY_BRANDLIST_FEATURED_BRAND) val query: String
) : UseCase<OfficialStoreFeaturedShop>() {

    var params: Map<String, Any> = mapOf()

    override suspend fun executeOnBackground(): OfficialStoreFeaturedShop {
        val gqlRequest = GraphqlRequest(query, BrandlistFeaturedBrandResponse::class.java, params)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(gqlRequest)
        val graphqlResponse = graphqlUseCase.executeOnBackground()
        return graphqlResponse.run {
            var featuredBrands = OfficialStoreFeaturedShop()
            val response = getData<BrandlistFeaturedBrandResponse>(BrandlistFeaturedBrandResponse::class.java)
            if (response != null) featuredBrands = response.officialStoreFeaturedShop
            featuredBrands
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