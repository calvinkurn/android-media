package com.tokopedia.purchase_platform.features.cart.domain.usecase

import android.app.DownloadManager
import com.google.gson.Gson
import com.tokopedia.purchase_platform.common.domain.schedulers.ExecutorSchedulers
import com.tokopedia.purchase_platform.features.cart.data.model.request.UpdateCartRequest
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.UpdateAndValidateUseData
import com.tokopedia.purchase_platform.features.promo.data.request.validate_use.RequestParamsValidateUse
import com.tokopedia.purchase_platform.features.promo.data.request.varidate_use.PromoRequest
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
    val variables = HashMap<String, Any?>()

    fun setParams(promo: PromoRequest) {
        val checkPromoParam = RequestParamsValidateUse()
        checkPromoParam.params?.promo = promo
        val jsonTreeCheckoutRequest = Gson().toJsonTree(checkPromoParam)
        val jsonObjectCheckoutRequest = jsonTreeCheckoutRequest.asJsonObject
        variables["params"] = jsonObjectCheckoutRequest
    }

    override fun createObservable(requestParams: RequestParams?): Observable<UpdateAndValidateUseData> {
        val paramUpdateList = requestParams?.getObject(UpdateCartUseCase.PARAM_UPDATE_CART_REQUEST) as ArrayList<UpdateCartRequest>
        val requestParamUpdateCart = RequestParams.create()
        requestParamUpdateCart.putObject(UpdateCartUseCase.PARAM_UPDATE_CART_REQUEST, paramUpdateList)

        val requestParamValidateUse = RequestParams.create()

        val cartId = requestParams.getString(GetCartListSimplifiedUseCase.PARAM_SELECTED_CART_ID, "") ?: ""
        val requestParamGetCartSimplified = RequestParams.create()
        requestParamGetCartSimplified.putString(GetCartListSimplifiedUseCase.PARAM_SELECTED_CART_ID, cartId)

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
                            .map { additionalInfo ->
                                updateAndValidateUseData.additionalInfoUiModel = additionalInfo
                                updateAndValidateUseData
                            }
                }
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main)

    }
}