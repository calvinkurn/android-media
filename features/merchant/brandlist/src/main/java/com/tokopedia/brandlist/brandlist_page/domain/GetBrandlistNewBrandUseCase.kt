package com.tokopedia.brandlist.brandlist_page.domain

import com.tokopedia.brandlist.brandlist_page.data.model.BrandlistNewBrandResponse
import com.tokopedia.brandlist.brandlist_page.data.model.OfficialStoreBrandsRecommendation
import com.tokopedia.brandlist.common.GQLQueryConstant
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class GetBrandlistNewBrandUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase,
        @Named(GQLQueryConstant.QUERY_BRANDLIST_NEW_BRAND) val query: String
) : UseCase<OfficialStoreBrandsRecommendation>() {

    var params: Map<String, Any> = mapOf()

    override suspend fun executeOnBackground(): OfficialStoreBrandsRecommendation {
        val gqlRequest = GraphqlRequest(query, BrandlistNewBrandResponse::class.java, params)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(gqlRequest)
        val graphqlResponse = graphqlUseCase.executeOnBackground()
        return graphqlResponse.run {
            getData<BrandlistNewBrandResponse>(BrandlistNewBrandResponse::class.java).officialStoreBrandsRecommendation
        }
    }

    companion object {

        const val USER_ID = "user_id"
        const val CATEGORY_IDS = "category_ids"

        fun createParams(userId: Int, categoryId: Int): Map<String, Any> {
            return mutableMapOf<String, Any>().apply {
                put(USER_ID, userId)
                put(CATEGORY_IDS, categoryId)
            }
        }
    }
}