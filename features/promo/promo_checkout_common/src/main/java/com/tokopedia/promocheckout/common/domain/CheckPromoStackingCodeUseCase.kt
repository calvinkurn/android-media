package com.tokopedia.promocheckout.common.domain

import android.content.res.Resources
import com.google.gson.Gson
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.common.R
import com.tokopedia.promocheckout.common.data.entity.request.CheckPromoParam
import com.tokopedia.promocheckout.common.data.entity.request.Promo
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.promocheckout.common.domain.model.promostacking.response.ResponseGetPromoStackFirst
import com.tokopedia.promocheckout.common.util.PromoQuery
import com.tokopedia.promocheckout.common.view.uimodel.ResponseGetPromoStackUiModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class CheckPromoStackingCodeUseCase constructor(val resources: Resources,
                                                val mapper: CheckPromoStackingCodeMapper) : UseCase<ResponseGetPromoStackUiModel>() {

    val variables = HashMap<String, Any?>()

    fun setParams(promo: Promo) {
        val checkPromoParam = CheckPromoParam()
        checkPromoParam.promo = promo
        val jsonTreeCheckoutRequest = Gson().toJsonTree(checkPromoParam)
        val jsonObjectCheckoutRequest = jsonTreeCheckoutRequest.asJsonObject
        variables.put("params", jsonObjectCheckoutRequest)
    }

    override fun createObservable(params: RequestParams?): Observable<ResponseGetPromoStackUiModel> {
        val graphqlRequest = GraphqlRequest(PromoQuery.promoCheckPromoCodePromoStackingFirst(), ResponseGetPromoStackFirst::class.java, variables)
        val graphqlUseCase = GraphqlUseCase()
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .map {
                    val responseGetPromoStack = mapper.map(it)
                    responseGetPromoStack
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

}