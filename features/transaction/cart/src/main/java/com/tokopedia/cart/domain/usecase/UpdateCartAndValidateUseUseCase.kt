package com.tokopedia.cart.domain.usecase

import com.tokopedia.cart.data.model.request.UpdateCartRequest
import com.tokopedia.cart.domain.model.updatecart.UpdateAndValidateUseData
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.ExecutorSchedulers
import com.tokopedia.usecase.RequestParams
import rx.Observable
import javax.inject.Inject

/**
 * Created by fwidjaja on 2020-03-04.
 */
class UpdateCartAndValidateUseUseCase @Inject constructor(private val updateCartUseCase: UpdateCartUseCase,
                                                          private val validateUseUseCase: OldValidateUsePromoRevampUseCase,
                                                          private val schedulers: ExecutorSchedulers) : com.tokopedia.usecase.UseCase<UpdateAndValidateUseData>() {

    companion object {
        val PARAM_UPDATE_CART_REQUEST = "PARAM_UPDATE_CART_REQUEST"
        val PARAM_CARTS = "carts"

        const val PARAM_KEY_SOURCE = "source"
        const val PARAM_VALUE_SOURCE_UPDATE_QTY_NOTES = "update_qty_notes"
    }

    override fun createObservable(requestParams: RequestParams?): Observable<UpdateAndValidateUseData> {
        val paramUpdateList = requestParams?.getObject(PARAM_UPDATE_CART_REQUEST) as ArrayList<UpdateCartRequest>
        val paramUpdateSource = requestParams?.getString(PARAM_KEY_SOURCE, "")
        val requestParamUpdateCart = RequestParams.create()
        requestParamUpdateCart.putObject(PARAM_UPDATE_CART_REQUEST, paramUpdateList)
        requestParamUpdateCart.putString(PARAM_KEY_SOURCE, paramUpdateSource)

        val paramValidateUse = requestParams.getObject(OldValidateUsePromoRevampUseCase.PARAM_VALIDATE_USE) as ValidateUsePromoRequest
        val requestParamValidateUse = RequestParams.create()
        requestParamValidateUse.putObject(OldValidateUsePromoRevampUseCase.PARAM_VALIDATE_USE, paramValidateUse)

        return Observable.just(UpdateAndValidateUseData())
                .flatMap { updateAndValidateUseData ->
                    updateCartUseCase.createObservable(requestParamUpdateCart)
                            .map { updateCartData ->
                                updateAndValidateUseData.updateCartData = updateCartData
                                if (!updateCartData.isSuccess) {
                                    throw CartResponseErrorException(updateCartData.message)
                                }
                                updateAndValidateUseData
                            }
                }
                .flatMap { updateAndValidateUseData ->
                    validateUseUseCase.createObservable(requestParamValidateUse)
                            .map { validateUseRevampUiModel ->
                                updateAndValidateUseData.promoUiModel = validateUseRevampUiModel.promoUiModel
                                updateAndValidateUseData
                            }
                }
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main)
    }
}