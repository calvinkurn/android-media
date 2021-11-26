package com.tokopedia.purchase_platform.common.feature.promo.domain.usecase

import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.response.clearpromo.ClearCacheAutoApplyStackResponse
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.SuccessDataUiModel
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class ClearCacheAutoApplyStackUseCase @Inject constructor(@ApplicationContext private val graphqlRepository: GraphqlRepository) : UseCase<ClearPromoUiModel>() {

    private var queryString: String = ""

    companion object {
        const val PARAM_VALUE_MARKETPLACE = "marketplace"

        const val PARAM_PLACEHOLDER_SERVICE_ID = "#serviceId"
        const val PARAM_PLACEHOLDER_PROMO_CODE = "#promoCode"
        const val PARAM_PLACEHOLDER_IS_OCC = "#isOCC"
    }

    fun setParams(serviceId: String, promoCodeList: ArrayList<String>, isOcc: Boolean = false): ClearCacheAutoApplyStackUseCase {
        queryString = query()
                .replace(PARAM_PLACEHOLDER_SERVICE_ID, serviceId)
                .replace(PARAM_PLACEHOLDER_PROMO_CODE, Gson().toJson(promoCodeList))
                .replace(PARAM_PLACEHOLDER_IS_OCC, isOcc.toString())
        return this
    }

    override suspend fun executeOnBackground(): ClearPromoUiModel {
        val query = queryString
        if (query.isEmpty()) {
            throw RuntimeException("Query has not been initialized!")
        }

        val request = GraphqlRequest(query, ClearCacheAutoApplyStackResponse::class.java)
        val response = graphqlRepository.response(listOf(request)).getSuccessData<ClearCacheAutoApplyStackResponse>()
        return ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                        success = response.successData.success,
                        tickerMessage = response.successData.tickerMessage,
                        defaultEmptyPromoMessage = response.successData.defaultEmptyPromoMessage,
                )
        )
    }

    private fun query() = """
            mutation {
                clearCacheAutoApplyStack(serviceID: "#serviceId", promoCode: #promoCode, isOCC: #isOCC) {
                    Success
                    ticker_message
                }
            }""".trimIndent()
}