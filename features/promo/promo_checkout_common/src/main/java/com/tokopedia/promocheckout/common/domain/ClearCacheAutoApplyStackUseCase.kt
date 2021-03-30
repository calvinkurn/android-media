package com.tokopedia.promocheckout.common.domain

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.common.R
import com.tokopedia.promocheckout.common.domain.model.clearpromo.ClearCacheAutoApplyStackResponse
import com.tokopedia.promocheckout.common.util.PromoQuery
import com.tokopedia.promocheckout.common.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.promocheckout.common.view.model.clearpromo.SuccessDataUiModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 18/03/19.
 */

class ClearCacheAutoApplyStackUseCase @Inject constructor(@ApplicationContext val context: Context) : UseCase<ClearPromoUiModel>() {

    var queryString: String = ""

    companion object {
        val PARAM_VALUE_MARKETPLACE = "marketplace"

        val PARAM_PLACEHOLDER_SERVICE_ID = "#serviceId"
        val PARAM_PLACEHOLDER_PROMO_CODE = "#promoCode"
        val PARAM_PLACEHOLDER_IS_OCC = "#isOCC"
    }

    fun setParams(serviceId: String, promoCodeList: ArrayList<String>) {
        queryString = PromoQuery.clearCacheAutoApplyStack()
        queryString = queryString.replace(PARAM_PLACEHOLDER_SERVICE_ID, serviceId)

        queryString = queryString.replace(PARAM_PLACEHOLDER_PROMO_CODE, Gson().toJson(promoCodeList))

        queryString = queryString.replace(PARAM_PLACEHOLDER_IS_OCC, "false")
    }

    fun setParams(serviceId: String, promoCodeList: ArrayList<String>, isOcc: Boolean) {
        queryString = PromoQuery.clearCacheAutoApplyStack()
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

}