package com.tokopedia.purchase_platform.features.cart.domain.usecase

import com.tokopedia.abstraction.common.utils.TKPDMapParam
import com.tokopedia.purchase_platform.common.domain.schedulers.ExecutorSchedulers
import com.tokopedia.purchase_platform.features.cart.data.repository.ICartRepository
import com.tokopedia.purchase_platform.features.cart.domain.mapper.ICartMapper
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.UpdateAndRefreshCartListData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author anggaprasetiyo on 30/04/18.
 */
class UpdateAndReloadCartUseCase @Inject constructor(private val cartRepository: ICartRepository,
                                                     private val cartMapper: ICartMapper,
                                                     private val getCartListSimplifiedUseCase: GetCartListSimplifiedUseCase,
                                                     private val schedulers: ExecutorSchedulers) {

    fun createObservable(requestParams: RequestParams): Observable<UpdateAndRefreshCartListData> {
        val paramUpdateCart = requestParams.getObject(PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART) as TKPDMapParam<String, String>

        return Observable.just(UpdateAndRefreshCartListData())
                .flatMap { updateAndRefreshCartListData ->
                    cartRepository.updateCartData(paramUpdateCart)
                            .map { updateCartDataResponse ->
                                val updateCartData = cartMapper.convertToUpdateCartData(updateCartDataResponse)
                                updateAndRefreshCartListData.updateCartData = updateCartData
                                updateAndRefreshCartListData
                            }
                }
                .flatMap { updateAndRefreshCartListData ->
                    getCartListSimplifiedUseCase.createObservable(RequestParams.EMPTY)
                            .map { cartListData ->
                                updateAndRefreshCartListData.cartListData = cartListData
                                updateAndRefreshCartListData
                            }
                }
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main);
    }

    companion object {
        const val PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART = "PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART"
        const val PARAM_CARTS = "carts"
    }
}
