package com.tokopedia.purchase_platform.common.feature.promo.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.response.clearpromo.ClearCacheAutoApplyStackResponse
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.SuccessDataUiModel
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class ClearCacheAutoApplyStackUseCase @Inject constructor(@ApplicationContext private val graphqlRepository: GraphqlRepository) : UseCase<ClearPromoUiModel>() {

    private var params: Map<String, Any> = emptyMap()

    companion object {
        const val PARAM_VALUE_MARKETPLACE = "marketplace"

        const val PARAM_PLACEHOLDER_SERVICE_ID = "serviceID"
        const val PARAM_PLACEHOLDER_IS_OCC = "isOCC"
        const val PARAM_PLACEHOLDER_ORDER_DATA = "orderData"

        const val QUERY_CLEAR_CACHE_AUTO_APPLY = "ClearCacheAutoApplyQuery"
    }

    fun setParams(request: ClearPromoRequest): ClearCacheAutoApplyStackUseCase {
        params = mapOf(
                PARAM_PLACEHOLDER_SERVICE_ID to request.serviceId,
                PARAM_PLACEHOLDER_IS_OCC to request.isOcc,
                PARAM_PLACEHOLDER_ORDER_DATA to request.orderData,
        )
        return this
    }

    @GqlQuery(QUERY_CLEAR_CACHE_AUTO_APPLY, CLEAR_CACHE_AUTO_APPLY_QUERY)
    override suspend fun executeOnBackground(): ClearPromoUiModel {
        val request = GraphqlRequest(ClearCacheAutoApplyQuery(), ClearCacheAutoApplyStackResponse::class.java, params)
        val response = graphqlRepository.response(listOf(request)).getSuccessData<ClearCacheAutoApplyStackResponse>()
        return ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                        success = response.successData.success,
                        tickerMessage = response.successData.tickerMessage,
                        defaultEmptyPromoMessage = response.successData.defaultEmptyPromoMessage,
                )
        )
    }
}