package com.tokopedia.brandlist.brandlist_page.domain

import com.tokopedia.brandlist.brandlist_page.data.model.BrandlistPopularBrandResponse
import com.tokopedia.brandlist.brandlist_page.data.model.OfficialStoreBrandsRecommendation
import com.tokopedia.brandlist.common.GQLQueryConstant
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class GetBrandlistPopularBrandUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase,
        @Named(GQLQueryConstant.QUERY_BRANDLIST_RECOMMENDATION) val query: String
) : UseCase<OfficialStoreBrandsRecommendation>() {

    var params: Map<String, Any> = mapOf()

    override suspend fun executeOnBackground(): OfficialStoreBrandsRecommendation {
        val gqlRequest = GraphqlRequest(query, BrandlistPopularBrandResponse::class.java, params)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(gqlRequest)
        val graphqlResponse = graphqlUseCase.executeOnBackground()
        return graphqlResponse.run {
            var popularBrands = OfficialStoreBrandsRecommendation()
            val response = getData<BrandlistPopularBrandResponse>(BrandlistPopularBrandResponse::class.java)
            if (response != null) popularBrands = response.officialStoreBrandsRecommendation
            popularBrands
        }
    }

    companion object {

        const val USER_ID = "userID"
        const val CATEGORY_IDS = "categoryIds"
        const val DEVICE = "device"
        const val ANDROID_DEVICE_NUMBER = 4
        const val NEW_WIDGET_NAME = "NEW"
        const val POPULAR_WIDGET_NAME = "POPULAR"
        const val WIDGET_NAME = "widgetName"

        fun createParams(userId: Int, categoryId: String, widgetName: String): Map<String, Any> {
            return mutableMapOf<String, Any>().apply {
                put(USER_ID, userId)
                put(CATEGORY_IDS, categoryId)
                put(WIDGET_NAME, widgetName)
                put(DEVICE, ANDROID_DEVICE_NUMBER)
            }
        }
    }
}