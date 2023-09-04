package com.tokopedia.tokopedianow.home.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokopedianow.common.domain.model.WarehouseData
import com.tokopedia.tokopedianow.home.domain.model.GetRepurchaseResponse
import com.tokopedia.tokopedianow.home.domain.model.GetRepurchaseResponse.RepurchaseData
import com.tokopedia.tokopedianow.home.domain.query.GetRepurchaseWidget
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetRepurchaseWidgetUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository
) {

    companion object {
        const val PARAM_WAREHOUSES = "warehouses"
        const val PARAM_QUERY_PARAM = "queryParam"
        const val PARAM_CAT_ID = "catID"
    }

    private val graphql by lazy { GraphqlUseCase<GetRepurchaseResponse>(graphqlRepository) }

    suspend fun execute(warehouses: List<WarehouseData>, queryParam: String = ""): RepurchaseData {
        graphql.apply {
            setGraphqlQuery(GetRepurchaseWidget)
            setTypeClass(GetRepurchaseResponse::class.java)

            setRequestParams(
                RequestParams.create().apply {
                    putObject(PARAM_WAREHOUSES, warehouses)
                    putString(PARAM_QUERY_PARAM, queryParam)
                }.parameters
            )

            val response = executeOnBackground().response
            val errorMessages = response?.header?.messages

            if (errorMessages.isNullOrEmpty()) {
                return response?.data ?: throw MessageErrorException()
            } else {
                throw MessageErrorException(errorMessages.firstOrNull().orEmpty())
            }
        }
    }
}
