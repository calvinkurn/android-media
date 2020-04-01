package com.tokopedia.purchase_platform.features.cart.domain.usecase

import com.tokopedia.purchase_platform.common.domain.schedulers.ExecutorSchedulers
import com.tokopedia.purchase_platform.features.cart.data.model.request.UpdateCartRequest
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.UpdateAndReloadCartListData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 2019-12-26.
 */

class UpdateAndReloadCartUseCase @Inject constructor(private val getCartListSimplifiedUseCase: GetCartListSimplifiedUseCase,
                                                     private val updateCartUseCase: UpdateCartUseCase,
                                                     private val schedulers: ExecutorSchedulers) : UseCase<UpdateAndReloadCartListData>() {

    override fun createObservable(requestParams: RequestParams?): Observable<UpdateAndReloadCartListData> {
        val paramUpdateList = requestParams?.getObject(UpdateCartUseCase.PARAM_UPDATE_CART_REQUEST) as ArrayList<UpdateCartRequest>
        val requestParamUpdateCart = RequestParams.create()
        requestParamUpdateCart.putObject(UpdateCartUseCase.PARAM_UPDATE_CART_REQUEST, paramUpdateList)

        val cartId = requestParams.getString(GetCartListSimplifiedUseCase.PARAM_SELECTED_CART_ID, "") ?: ""
        val requestParamGetCartSimplified = RequestParams.create()
        requestParamGetCartSimplified.putString(GetCartListSimplifiedUseCase.PARAM_SELECTED_CART_ID, cartId)

        return Observable.just(UpdateAndReloadCartListData())
                .flatMap { updateAndRefreshCartListData ->
                    updateCartUseCase.createObservable(requestParamUpdateCart)
                            .map { updateCartData ->
                                updateAndRefreshCartListData.updateCartData = updateCartData
                                updateAndRefreshCartListData
                            }
                }
                .flatMap { updateAndRefreshCartListData ->
                    getCartListSimplifiedUseCase.createObservable(requestParamGetCartSimplified)
                            .map { cartListData ->
                                updateAndRefreshCartListData.cartListData = cartListData
                                updateAndRefreshCartListData
                            }
                }
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main);

    }

}