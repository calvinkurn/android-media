package com.tokopedia.digital_product_detail.domain.usecase

import com.tokopedia.common.topupbills.data.requests.DigiPersoRequestParam
import com.tokopedia.digital_product_detail.data.model.data.DigitalDigiPersoGetPersonalizedItem
import com.tokopedia.digital_product_detail.domain.query.MCCMProductsQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class GetRechargeMCCMUseCase @Inject constructor(graphqlRepository: GraphqlRepository):
    GraphqlUseCase<DigitalDigiPersoGetPersonalizedItem>(graphqlRepository) {

    init {
        setGraphqlQuery(MCCMProductsQuery())
        setTypeClass(DigitalDigiPersoGetPersonalizedItem::class.java)
    }

    suspend fun execute(clientNumbers: List<String>,
                        dgCategoryIds: List<Int>,
                        dgOperatorIds: List<Int>,
                        channelName: String): DigitalDigiPersoGetPersonalizedItem {
        setRequestParams(createRequestParam(clientNumbers, dgCategoryIds, dgOperatorIds, channelName))
        return executeOnBackground()
    }

    private fun createRequestParam(clientNumbers: List<String>,
                                   dgCategoryIds: List<Int>,
                                   dgOperatorIds: List<Int>,
                                   channelName: String) = HashMap<String, Any>().apply {
        val dgPersoRequestParam = DigiPersoRequestParam(
            channelName = channelName,
            clientNumbers = clientNumbers,
            dgCategoryIDs = dgCategoryIds,
            pgCategoryIDs = emptyList(),
            dgOperatorIds = dgOperatorIds
        )

        put(PERSO_INPUT, dgPersoRequestParam)
    }

    companion object {
        private const val PERSO_INPUT = "input"
    }

}
