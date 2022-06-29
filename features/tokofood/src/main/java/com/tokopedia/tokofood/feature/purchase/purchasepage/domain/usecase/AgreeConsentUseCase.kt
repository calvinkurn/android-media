package com.tokopedia.tokofood.feature.purchase.purchasepage.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.response.AgreeConsentResponse
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.query.AgreeConsentQuery
import javax.inject.Inject

class AgreeConsentUseCase @Inject constructor(
    repository: GraphqlRepository
): GraphqlUseCase<AgreeConsentResponse>(repository) {

    init {
        setTypeClass(AgreeConsentResponse::class.java)
        setGraphqlQuery(AgreeConsentQuery)
    }

    suspend fun execute(): AgreeConsentResponse {
        return executeOnBackground()
    }

}