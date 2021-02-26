package com.tokopedia.cart.domain.usecase

import com.tokopedia.cart.data.model.request.UpdateCartRequest
import com.tokopedia.cart.domain.model.updatecart.UpdateAndReloadCartListData
import com.tokopedia.cart.domain.usecase.GetCartListSimplifiedUseCase.Companion.PARAM_GET_CART
import com.tokopedia.purchase_platform.common.schedulers.ExecutorSchedulers
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

        val paramGetCart = requestParams.getObject(PARAM_GET_CART) as Map<String, Any?>
        val requestParamGetCart = RequestParams.create()
        requestParamGetCart.putAll(paramGetCart)

        return Observable.just(UpdateAndReloadCartListData())
                .flatMap { updateAndRefreshCartListData ->
                    updateCartUseCase.createObservable(requestParamUpdateCart)
                            .map { updateCartData ->
                                updateAndRefreshCartListData.updateCartData = updateCartData
                                updateAndRefreshCartListData
                            }
                }
                .flatMap { updateAndRefreshCartListData ->
                    getCartListSimplifiedUseCase.createObservable(requestParamGetCart)
                            .map { cartListData ->
                                updateAndRefreshCartListData.cartListData = cartListData
                                updateAndRefreshCartListData
                            }
                }
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main);

    }

}