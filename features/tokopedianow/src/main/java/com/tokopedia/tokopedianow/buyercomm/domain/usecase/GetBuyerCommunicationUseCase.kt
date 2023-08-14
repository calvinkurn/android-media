package com.tokopedia.tokopedianow.buyercomm.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.buyercomm.domain.model.GetBuyerCommunication
import com.tokopedia.tokopedianow.buyercomm.domain.model.GetBuyerCommunication.GetBuyerCommunicationResponse
import com.tokopedia.tokopedianow.buyercomm.domain.query.GetBuyerCommunicationQuery
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetBuyerCommunicationUseCase @Inject constructor(graphqlRepository: GraphqlRepository) {

    private val graphql by lazy { GraphqlUseCase<GetBuyerCommunication>(graphqlRepository) }

    suspend fun execute(addressData: TokoNowLocalAddress): GetBuyerCommunicationResponse {
        graphql.apply {
            val requestParams = RequestParams().apply {
                val warehousesData = addressData.getWarehousesData()
                putString(GetBuyerCommunicationQuery.PARAM_PAGE, "")
                putObject(GetBuyerCommunicationQuery.PARAM_WAREHOUSES, warehousesData)
            }.parameters

            setGraphqlQuery(GetBuyerCommunicationQuery)
            setTypeClass(GetBuyerCommunication::class.java)
            setRequestParams(requestParams)
        }
        return graphql.executeOnBackground().response
    }
}
