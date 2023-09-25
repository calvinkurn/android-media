package com.tokopedia.sellerorder.detail.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.detail.domain.mapper.GetFeeTransparencyFeeMapper
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeUiModelWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

const val GET_SOM_INCOME_DETAIL_QUERY = """
    
"""

@GqlQuery("GetSomIncomeDetailQuery", GET_SOM_INCOME_DETAIL_QUERY)
class SomGetFeeTransparencyUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<Unit>,
    private val getFeeTransparencyFeeMapper: GetFeeTransparencyFeeMapper
) {
    init {
        useCase.setGraphqlQuery(GetSomIncomeDetailQuery())
    }

    suspend fun execute(
        orderId: String,
    ): TransparencyFeeUiModelWrapper {
        useCase.setRequestParams(createRequestParams(orderId))
        return getFeeTransparencyFeeMapper.mapToTransparencyFeeWrapperUiModel()
    }

    private fun createRequestParams(orderId: String): Map<String, Any> {
        return RequestParams.create().apply {
            putString(ORDER_ID_KEY, orderId)
        }.parameters
    }

    companion object {
        private const val ORDER_ID_KEY = "order_id"
    }
}
