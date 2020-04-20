package com.tokopedia.purchase_platform.common.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.purchase_platform.common.R
import com.tokopedia.purchase_platform.common.data.model.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.data.model.response.validateuse.ValidateUseResponse
import com.tokopedia.purchase_platform.common.feature.promo.view.mapper.ValidateUsePromoCheckoutMapper
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by fwidjaja on 2020-03-05.
 */
class ValidateUsePromoRevampUseCase @Inject constructor (@ApplicationContext private val context: Context,
                                                         private val graphqlUseCase: GraphqlUseCase)
    : UseCase<ValidateUsePromoRevampUiModel>() {

    companion object {
        const val PARAM_VALIDATE_USE = "PARAM_VALIDATE_USE"
        const val PARAM_PROMO = "promo"
        const val PARAM_PARAMS = "params"
    }

    override fun createObservable(requestParams: RequestParams?): Observable<ValidateUsePromoRevampUiModel> {
        val paramValidateUse = requestParams?.getObject(PARAM_VALIDATE_USE) as ValidateUsePromoRequest

        val varPromo = mapOf(
                PARAM_PROMO to paramValidateUse
        )
        val varParams = mapOf(
                PARAM_PARAMS to varPromo
        )

        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                R.raw.mutation_validate_use_promo_revamp), ValidateUseResponse::class.java, varParams)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .map { it ->
                    var validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel()
                    val validateUseGqlResponse = it.getData<ValidateUseResponse>(ValidateUseResponse::class.java)
                    validateUseGqlResponse?.validateUsePromoRevamp?.let {
                        validateUsePromoRevampUiModel = ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(it)
                    }
                    validateUsePromoRevampUiModel
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}