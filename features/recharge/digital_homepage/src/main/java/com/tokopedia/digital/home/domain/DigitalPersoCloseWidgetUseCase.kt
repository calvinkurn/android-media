package com.tokopedia.digital.home.domain

import com.tokopedia.digital.home.model.DigitalPersoCloseRequest
import com.tokopedia.digital.home.model.DigitalPersoCloseResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.digital.home.util.MutationDigitalPersonalizationCloseWidget

class DigitalPersoCloseWidgetUseCase(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<DigitalPersoCloseResponse>(graphqlRepository) {

    suspend fun digitalPersoCloseWidget(
        favId: String, type: String
    ):  DigitalPersoCloseResponse {
        setGraphqlQuery(MutationDigitalPersonalizationCloseWidget())
        setTypeClass(DigitalPersoCloseResponse::class.java)
        setRequestParams(setMapParams(favId, type))
        return executeOnBackground()
    }

    private fun setMapParams(favId: String, type: String): Map<String, Any> {
        val closePersoRequest = DigitalPersoCloseRequest(favId = favId, type = type)
        return mapOf(
            PARAM_INPUT to closePersoRequest
        )
    }

    companion object {
        private const val PARAM_INPUT = "input"
    }
}
