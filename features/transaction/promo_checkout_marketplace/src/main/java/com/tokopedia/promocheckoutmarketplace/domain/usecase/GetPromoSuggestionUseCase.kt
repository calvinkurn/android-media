package com.tokopedia.promocheckoutmarketplace.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.promocheckoutmarketplace.data.response.GetPromoSuggestionResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

@GqlQuery(GetPromoSuggestionUseCase.QUERY_NAME, GetPromoSuggestionUseCase.QUERY)
class GetPromoSuggestionUseCase @Inject constructor(@ApplicationContext private val graphqlRepository: GraphqlRepository) : UseCase<GetPromoSuggestionResponse>() {

    override suspend fun executeOnBackground(): GetPromoSuggestionResponse {
        val request = GraphqlRequest(GetPromoSuggestionQuery.GQL_QUERY, GetPromoSuggestionResponse::class.java)
        return graphqlRepository.response(listOf(request)).getSuccessData()
    }

    companion object {
        const val QUERY_NAME = "GetPromoSuggestionQuery"
        const val QUERY = """
            {
              GetPromoSuggestion(serviceID: "marketplace") {
                PromoHistory {
                  PromoCode
                  PromoContent {
                    ID
                    Name
                    Public
                    Status
                    BaseCode
                    IsBackdoor
                    PromoType
                    PromoTitle
                    Description
                  }
                }
              }
            }
        """
    }
}