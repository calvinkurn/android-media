package com.tokopedia.tokopedianow.home.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokopedianow.home.domain.model.GetRecentPurchaseResponse
import com.tokopedia.tokopedianow.home.domain.model.GetRecentPurchaseResponse.*
import com.tokopedia.tokopedianow.home.domain.query.GetRecentPurchase
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetRecentPurchaseUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository
) {

    companion object {
        private const val PARAM_WAREHOUSE_ID = "warehouseID"
    }

    private val graphql by lazy { GraphqlUseCase<GetRecentPurchaseResponse>(graphqlRepository) }

    suspend fun execute(warehouseId: String): RecentPurchaseData {
        graphql.apply {
            setGraphqlQuery(GetRecentPurchase.QUERY)
            setTypeClass(GetRecentPurchaseResponse::class.java)

            setRequestParams(RequestParams.create().apply {
                putString(PARAM_WAREHOUSE_ID, warehouseId)
            }.parameters)

            val response = executeOnBackground().response
            val errorMessages = response?.header?.messages

            if(errorMessages.isNullOrEmpty()) {
                return response?.data ?: throw MessageErrorException()
            } else {
                throw MessageErrorException(errorMessages.firstOrNull().orEmpty())
            }
        }
    }
}