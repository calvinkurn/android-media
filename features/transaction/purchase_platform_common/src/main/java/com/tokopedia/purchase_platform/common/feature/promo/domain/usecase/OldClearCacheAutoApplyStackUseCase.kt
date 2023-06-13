package com.tokopedia.purchase_platform.common.feature.promo.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoRequest
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
class OldClearCacheAutoApplyStackUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase) : UseCase<ClearPromoUiModel>() {

    var params: Map<String, Any> = emptyMap()

    companion object {
        const val PARAM_VALUE_MARKETPLACE = "marketplace"

        const val PARAM_PLACEHOLDER_SERVICE_ID = "serviceID"
        const val PARAM_PLACEHOLDER_IS_OCC = "isOCC"
        const val PARAM_PLACEHOLDER_ORDER_DATA = "orderData"
    }

    fun setParams(request: ClearPromoRequest) {
        params = mapOf(
            PARAM_PLACEHOLDER_SERVICE_ID to request.serviceId,
            PARAM_PLACEHOLDER_IS_OCC to request.isOcc,
            PARAM_PLACEHOLDER_ORDER_DATA to request.orderData
        )
    }

    override fun createObservable(params: RequestParams?): Observable<ClearPromoUiModel> {
        val graphqlRequest = GraphqlRequest(ClearCacheAutoApplyQuery(), ClearCacheAutoApplyStackResponse::class.java, this.params)
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
}
