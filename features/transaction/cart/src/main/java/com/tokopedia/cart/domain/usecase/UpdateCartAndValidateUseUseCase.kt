package com.tokopedia.cart.domain.usecase

import com.tokopedia.purchase_platform.common.schedulers.ExecutorSchedulers
import com.tokopedia.cart.data.model.request.UpdateCartRequest
import com.tokopedia.cart.domain.model.updatecart.UpdateAndValidateUseData
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.usecase.RequestParams
import rx.Observable
import javax.inject.Inject

/**
 * Created by fwidjaja on 2020-03-04.
 */
class UpdateCartAndValidateUseUseCase @Inject constructor(private val updateCartUseCase: UpdateCartUseCase,
                                                          private val validateUseUseCase: ValidateUsePromoRevampUseCase,
                                                          private val schedulers: ExecutorSchedulers) : com.tokopedia.usecase.UseCase<UpdateAndValidateUseData>() {

    override fun createObservable(requestParams: RequestParams?): Observable<UpdateAndValidateUseData> {
        val paramUpdateList = requestParams?.getObject(UpdateCartUseCase.PARAM_UPDATE_CART_REQUEST) as ArrayList<UpdateCartRequest>
        val requestParamUpdateCart = RequestParams.create()
        requestParamUpdateCart.putObject(UpdateCartUseCase.PARAM_UPDATE_CART_REQUEST, paramUpdateList)

        val paramValidateUse = requestParams.getObject(ValidateUsePromoRevampUseCase.PARAM_VALIDATE_USE) as ValidateUsePromoRequest
        val requestParamValidateUse = RequestParams.create()
        requestParamValidateUse.putObject(ValidateUsePromoRevampUseCase.PARAM_VALIDATE_USE, paramValidateUse)

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