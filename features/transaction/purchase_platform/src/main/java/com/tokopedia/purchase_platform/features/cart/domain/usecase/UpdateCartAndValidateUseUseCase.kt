package com.tokopedia.purchase_platform.features.cart.domain.usecase

import com.tokopedia.purchase_platform.common.domain.schedulers.ExecutorSchedulers
import com.tokopedia.purchase_platform.features.cart.data.model.request.UpdateCartRequest
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.UpdateAndValidateUseData
import com.tokopedia.purchase_platform.features.promo.data.request.CouponListRequest
import com.tokopedia.purchase_platform.features.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.usecase.RequestParams
import rx.Observable
import javax.inject.Inject

/**
 * Created by fwidjaja on 2020-03-04.
 */
class UpdateCartAndValidateUseUseCase @Inject constructor(private val updateCartUseCase: UpdateCartUseCase,
                                                          private val validateUseUseCase: ValidateUsePromoRevampUseCase,
                                                          private val schedulers: ExecutorSchedulers) : com.tokopedia.usecase.UseCase<UpdateAndValidateUseData>() {
    /*val variables = HashMap<String, Any?>()

    fun setParams(couponListRequest: CouponListRequest) {
        val checkPromoParam = RequestParamsValidateUse()
        checkPromoParam.params?.promo = couponListRequest
        val jsonTreeCheckoutRequest = Gson().toJsonTree(checkPromoParam)
        val jsonObjectCheckoutRequest = jsonTreeCheckoutRequest.asJsonObject
        variables["params"] = jsonObjectCheckoutRequest
    }*/

    override fun createObservable(requestParams: RequestParams?): Observable<UpdateAndValidateUseData> {
        val paramUpdateList = requestParams?.getObject(UpdateCartUseCase.PARAM_UPDATE_CART_REQUEST) as ArrayList<UpdateCartRequest>
        val requestParamUpdateCart = RequestParams.create()
        requestParamUpdateCart.putObject(UpdateCartUseCase.PARAM_UPDATE_CART_REQUEST, paramUpdateList)

        val paramValidateUse = requestParams.getObject(ValidateUsePromoRevampUseCase.PARAM_VALIDATE_USE) as CouponListRequest
        val requestParamValidateUse = RequestParams.create()
        requestParamValidateUse.putObject(ValidateUsePromoRevampUseCase.PARAM_VALIDATE_USE, paramValidateUse)

        return Observable.just(UpdateAndValidateUseData())
                .flatMap { updateAndValidateUseData ->
                    updateCartUseCase.createObservable(requestParamUpdateCart)
                            .map { updateCartData ->
                                updateAndValidateUseData.updateCartData = updateCartData
                                updateAndValidateUseData
                            }
                }
                .flatMap { updateAndValidateUseData ->
                    validateUseUseCase.createObservable(requestParamValidateUse)
                            .map { validateUseRevampUiModel ->
                                updateAndValidateUseData.additionalInfoUiModel = validateUseRevampUiModel.promoUiModel?.additionalInfoUiModel
                                updateAndValidateUseData
                            }
                }
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main)

    }
}