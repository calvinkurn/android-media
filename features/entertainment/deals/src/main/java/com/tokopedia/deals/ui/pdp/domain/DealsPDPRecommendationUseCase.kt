package com.tokopedia.deals.ui.pdp.domain

import com.tokopedia.deals.common.model.request.RequestParam
import com.tokopedia.deals.common.model.response.SearchData
import com.tokopedia.deals.ui.pdp.domain.query.DealsPDPRecommendationsQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class DealsPDPRecommendationUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<SearchData>(graphqlRepository) {

    init {
        setGraphqlQuery(DealsPDPRecommendationsQuery())
        setTypeClass(SearchData::class.java)
    }

    suspend fun execute(childCategoryIds: String?): SearchData {
        setRequestParams(createRequestParam(childCategoryIds))
        return executeOnBackground()
    }

    private fun createRequestParam(
        childCategoryIds: String?
    ): HashMap<String, Any> {
        val hashMap = hashMapOf<String, Any>()
        val pdpRecommendation: ArrayList<RequestParam> = arrayListOf()
        pdpRecommendation.add(RequestParam(com.tokopedia.deals.ui.search.DealsSearchConstants.MAP_CATEGORY, com.tokopedia.deals.ui.search.DealsSearchConstants.DEFAULT_CATEGORY))
        pdpRecommendation.add(RequestParam(com.tokopedia.deals.ui.search.DealsSearchConstants.MAP_TREE, com.tokopedia.deals.ui.search.DealsSearchConstants.BRAND_PRODUCT_TREE))
        if (childCategoryIds != null) {
            pdpRecommendation.add(RequestParam(com.tokopedia.deals.ui.search.DealsSearchConstants.MAP_CHILD_CATEGORY_IDS, childCategoryIds))
        }
        hashMap.put(com.tokopedia.deals.ui.search.DealsSearchConstants.SEARCH_PARAM, pdpRecommendation)
        return hashMap
    }
}
