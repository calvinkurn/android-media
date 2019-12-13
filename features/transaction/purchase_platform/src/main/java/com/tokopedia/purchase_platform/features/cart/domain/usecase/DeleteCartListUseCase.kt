package com.tokopedia.purchase_platform.features.cart.domain.usecase

import com.tokopedia.abstraction.common.utils.TKPDMapParam
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.features.cart.data.repository.ICartRepository
import com.tokopedia.purchase_platform.features.cart.domain.mapper.ICartMapper
import com.tokopedia.purchase_platform.features.cart.domain.model.DeleteAndRefreshCartListData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.*
import javax.inject.Inject

/**
 * @author anggaprasetiyo on 30/04/18.
 */
class DeleteCartListUseCase @Inject constructor(private val cartRepository: ICartRepository,
                                                private val cartMapper: ICartMapper,
                                                private val clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase) : UseCase<DeleteAndRefreshCartListData>() {

    override fun createObservable(requestParams: RequestParams?): Observable<DeleteAndRefreshCartListData> {

        val paramDelete = requestParams?.getObject(PARAM_REQUEST_AUTH_MAP_STRING_DELETE_CART) as TKPDMapParam<String, String>
        val toBeDeletedPromoCode = requestParams.getObject(PARAM_TO_BE_REMOVED_PROMO_CODES) as ArrayList<String>

        return Observable.just(DeleteAndRefreshCartListData())
                .flatMap { deleteAndRefreshCartListData ->
                    cartRepository.deleteCartData(paramDelete).map { deleteCartDataResponse ->
                        deleteAndRefreshCartListData.deleteCartData = cartMapper.convertToDeleteCartData(deleteCartDataResponse)
                        deleteAndRefreshCartListData
                    }
                }
                .flatMap { deleteAndRefreshCartListData ->
                    if (toBeDeletedPromoCode.isEmpty()) {
                        Observable.just(deleteAndRefreshCartListData)
                    } else {
                        clearCacheAutoApplyStackUseCase.setParams(ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE, toBeDeletedPromoCode)
                        clearCacheAutoApplyStackUseCase.createObservable(RequestParams.create())
                                .map { deleteAndRefreshCartListData }
                    }
                }
    }

    companion object {
        const val PARAM_PARAMS = "params"
        const val PARAM_IS_DELETE_ALL_DATA = "PARAM_IS_DELETE_ALL_DATA"
        const val PARAM_REQUEST_AUTH_MAP_STRING_DELETE_CART = "PARAM_REQUEST_AUTH_MAP_STRING_DELETE_CART"
        const val PARAM_TO_BE_REMOVED_PROMO_CODES = "PARAM_TO_BE_REMOVED_PROMO_CODES"
    }
}
