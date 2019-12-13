package com.tokopedia.purchase_platform.features.cart.domain.usecase

import com.tokopedia.abstraction.common.utils.TKPDMapParam
import com.tokopedia.purchase_platform.common.domain.schedulers.ExecutorSchedulers
import com.tokopedia.purchase_platform.features.cart.data.repository.ICartRepository
import com.tokopedia.purchase_platform.features.cart.domain.mapper.ICartMapper
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.UpdateCartData
import com.tokopedia.usecase.RequestParams
import rx.Observable
import javax.inject.Inject

/**
 * @author anggaprasetiyo on 30/04/18.
 */
class UpdateCartUseCase @Inject constructor(private val cartRepository: ICartRepository,
                                            private val cartMapper: ICartMapper,
                                            private val schedulers: ExecutorSchedulers) {

    fun createObservable(requestParams: RequestParams): Observable<UpdateCartData> {
        val paramUpdate = requestParams.getObject(PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART) as TKPDMapParam<String, String>
        return cartRepository.updateCartData(paramUpdate)
                .map { updateCartDataResponse -> cartMapper.convertToUpdateCartData(updateCartDataResponse) }
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main)
    }

    companion object {
        val PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART = "PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART"
        val PARAM_CARTS = "carts"
    }
}
