package com.tokopedia.deals.pdp.domain

import com.tokopedia.deals.common.model.response.SearchData
import com.tokopedia.deals.pdp.domain.query.DealsPDPRecommendationQuery
import com.tokopedia.deals.pdp.domain.query.DealsPDPRecommendationsQuery
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
        setRequestParams(DealsPDPRecommendationQuery.createRequestParam(childCategoryIds))
        return executeOnBackground()
    }
}