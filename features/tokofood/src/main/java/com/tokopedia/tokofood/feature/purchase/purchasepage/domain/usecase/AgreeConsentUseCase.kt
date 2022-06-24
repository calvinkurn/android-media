package com.tokopedia.tokofood.feature.purchase.purchasepage.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.response.AgreeConsentResponse
import javax.inject.Inject

private const val QUERY = """
        mutation AgreeConsent {
          tokofoodSubmitUserConsent() {
            message
            success
          }
        }
    """

@GqlQuery("AgreeConsent", QUERY)
class AgreeConsentUseCase @Inject constructor(
    repository: GraphqlRepository
): GraphqlUseCase<AgreeConsentResponse>(repository) {

    suspend fun execute(): AgreeConsentResponse {
        return executeOnBackground()
    }

}