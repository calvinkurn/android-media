package com.tokopedia.brandlist.brandlist_page.domain

import com.tokopedia.brandlist.brandlist_page.data.model.BrandlistFeaturedBrandResponse
import com.tokopedia.brandlist.brandlist_page.data.model.OfficialStoreFeaturedShop
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class GetBrandlistFeaturedBrandUseCase @Inject constructor(private val graphqlUseCase: MultiRequestGraphqlUseCase) : UseCase<OfficialStoreFeaturedShop>() {

    var params: Map<String, Any> = mapOf()

    override suspend fun executeOnBackground(): OfficialStoreFeaturedShop {
        val gqlRequest = GraphqlRequest(QUERY, BrandlistFeaturedBrandResponse::class.java, params)
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
        private const val QUERY = "query OfficialStoreFeaturedShop(\$categoryAliasID: Int) {\n" +
                "    OfficialStoreFeaturedShop(categoryAliasID: \$categoryAliasID, device: 4, size: 24) {\n" +
                "        shops{\n" +
                "            id\n" +
                "            name\n" +
                "            url\n" +
                "            logoUrl\n" +
                "            imageUrl\n" +
                "            additionalInformation\n" +
                "            featuredBrandId\n" +
                "        }\n" +
                "        totalShops\n" +
                "        header{\n" +
                "            title\n" +
                "            ctaText\n" +
                "            link\n" +
                "        }\n" +
                "    }\n" +
                "}"

        fun createParams(categoryId: Int): Map<String, Any> {
            return mutableMapOf<String, Any>().apply {
                put(CATEGORY_ALIAS_ID, categoryId)
            }
        }
    }
}