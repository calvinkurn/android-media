package com.tokopedia.purchase_platform.features.promo.domain.usecase

import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.validate_use.AdditionalInfoUiModel
import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.common.domain.schedulers.ExecutorSchedulers
import com.tokopedia.purchase_platform.features.cart.data.model.request.UpdateCartRequest
import com.tokopedia.purchase_platform.features.cart.domain.usecase.UpdateCartUseCase
import com.tokopedia.purchase_platform.features.promo.data.request.CouponListRequest
import com.tokopedia.purchase_platform.features.promo.data.request.validate_use.Params
import com.tokopedia.purchase_platform.features.promo.data.request.validate_use.RequestParamsValidateUse
import com.tokopedia.purchase_platform.features.promo.data.request.varidate_use.PromoRequest
import com.tokopedia.purchase_platform.features.promo.data.response.validate_use.ValidateUseResponse
import com.tokopedia.purchase_platform.features.promo.presentation.mapper.ValidateUsePromoCheckoutMapper
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.validate_use.ValidateUsePromoRevampUiModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.Scheduler
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
    }

    override fun createObservable(requestParams: RequestParams?): Observable<ValidateUsePromoRevampUiModel> {
        val paramValidateUse = requestParams?.getObject(PARAM_VALIDATE_USE) as CouponListRequest

        val variables = mapOf(
                PARAM_PROMO to paramValidateUse
        )

        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                R.raw.mutation_validate_use_promo_revamp), ValidateUseResponse::class.java, variables)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .map { it ->
                    var validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel()
                    val validateUseGqlRespons = it.getData<ValidateUseResponse>(ValidateUseResponse::class.java)
                    validateUseGqlRespons.validateUsePromoRevamp?.let {
                        validateUsePromoRevampUiModel = ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(it)
                    }
                    validateUsePromoRevampUiModel
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}