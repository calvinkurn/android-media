package com.tokopedia.cart.domain.usecase

import com.tokopedia.cart.domain.model.updatecart.UpdateAndReloadCartListData
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
        // Directly send RequestParams without any modification, let each UseCase get required RequestParams
        return Observable.just(UpdateAndReloadCartListData())
                .flatMap { updateAndRefreshCartListData ->
                    updateCartUseCase.createObservable(requestParams)
                            .map { updateCartData ->
                                updateAndRefreshCartListData.updateCartData = updateCartData
                                updateAndRefreshCartListData
                            }
                }
                .flatMap { updateAndRefreshCartListData ->
                    getCartListSimplifiedUseCase.createObservable(requestParams)
                            .map { cartListData ->
                                updateAndRefreshCartListData.cartListData = cartListData
                                updateAndRefreshCartListData
                            }
                }
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main);

    }

}