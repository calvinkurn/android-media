package com.tokopedia.purchase_platform.common.feature.promo.domain.usecase

import com.google.gson.Gson
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.purchase_platform.common.feature.promo.data.response.clearpromo.ClearCacheAutoApplyStackResponse
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.SuccessDataUiModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

@Deprecated("Please use coroutine version", ReplaceWith("ClearCacheAutoApplyStackUseCase"))
class OldClearCacheAutoApplyStackUseCase @Inject constructor() : UseCase<ClearPromoUiModel>() {

    var queryString: String = ""

    companion object {
        const val PARAM_VALUE_MARKETPLACE = "marketplace"

        const val PARAM_PLACEHOLDER_SERVICE_ID = "#serviceId"
        const val PARAM_PLACEHOLDER_PROMO_CODE = "#promoCode"
        const val PARAM_PLACEHOLDER_IS_OCC = "#isOCC"
    }

    fun setParams(serviceId: String, promoCodeList: ArrayList<String>) {
        queryString = query()
        queryString = queryString.replace(PARAM_PLACEHOLDER_SERVICE_ID, serviceId)

        queryString = queryString.replace(PARAM_PLACEHOLDER_PROMO_CODE, Gson().toJson(promoCodeList))

        queryString = queryString.replace(PARAM_PLACEHOLDER_IS_OCC, "false")
    }

    fun setParams(serviceId: String, promoCodeList: ArrayList<String>, isOcc: Boolean) {
        queryString = query()
        queryString = queryString.replace(PARAM_PLACEHOLDER_SERVICE_ID, serviceId)

        queryString = queryString.replace(PARAM_PLACEHOLDER_PROMO_CODE, Gson().toJson(promoCodeList))

        queryString = queryString.replace(PARAM_PLACEHOLDER_IS_OCC, isOcc.toString())
    }

    override fun createObservable(params: RequestParams?): Observable<ClearPromoUiModel> {
        val graphqlRequest = GraphqlRequest(queryString, ClearCacheAutoApplyStackResponse::class.java)
        val graphqlUseCase = GraphqlUseCase()
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .map {
                    val response = it.getData<ClearCacheAutoApplyStackResponse>(ClearCacheAutoApplyStackResponse::class.java)
                    ClearPromoUiModel().apply {
                        successDataModel = SuccessDataUiModel().apply {
                            success = response.successData.success
                            tickerMessage = response.successData.tickerMessage
                            defaultEmptyPromoMessage = response.successData.defaultEmptyPromoMessage
                        }
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun query() = """
            mutation {
                clearCacheAutoApplyStack(serviceID: "#serviceId", promoCode: #promoCode, isOCC: #isOCC) {
                    Success
                    ticker_message
                }
            }""".trimIndent()
}