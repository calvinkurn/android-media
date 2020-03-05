package com.tokopedia.purchase_platform.features.promo.domain.usecase

import AdditionalInfoUiModel
import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.common.domain.schedulers.ExecutorSchedulers
import com.tokopedia.purchase_platform.features.promo.data.request.validate_use.RequestParamsValidateUse
import com.tokopedia.purchase_platform.features.promo.data.request.varidate_use.PromoRequest
import com.tokopedia.purchase_platform.features.promo.data.response.validate_use.ValidateUseResponse
import com.tokopedia.purchase_platform.features.promo.presentation.mapper.ValidateUsePromoCheckoutMapper
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * Created by fwidjaja on 2020-03-05.
 */
class ValidateUsePromoRevampUseCase @Inject constructor (@ApplicationContext private val context: Context,
                                                         private val graphqlUseCase: GraphqlUseCase, private val schedulers: ExecutorSchedulers)
    : UseCase<AdditionalInfoUiModel>() {

    val variables = HashMap<String, Any?>()

    fun setParams(promo: PromoRequest) {
        val checkPromoParam = RequestParamsValidateUse()
        checkPromoParam.params?.promo = promo
        val jsonTreeCheckoutRequest = Gson().toJsonTree(checkPromoParam)
        val jsonObjectCheckoutRequest = jsonTreeCheckoutRequest.asJsonObject
        variables["params"] = jsonObjectCheckoutRequest
    }

    override fun createObservable(requestParams: RequestParams?): Observable<AdditionalInfoUiModel> {
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                R.raw.mutation_validate_use_promo_revamp), ValidateUseResponse::class.java, variables)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .map { it ->
                    var additionalInfo = AdditionalInfoUiModel()
                    val validateUseGqlRespons = it.getData<ValidateUseResponse>(ValidateUseResponse::class.java)
                    validateUseGqlRespons.validateUsePromoRevamp?.promo?.additionalInfo?.let {
                        additionalInfo = ValidateUsePromoCheckoutMapper.mapToAdditionalInfoUiModel(it)
                    }
                    additionalInfo
                }
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main)
    }
}