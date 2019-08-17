package com.tokopedia.purchase_platform.features.cart.data.repository

import com.tokopedia.purchase_platform.features.cart.data.api.CartApi
import com.tokopedia.purchase_platform.features.cart.data.model.response.CartDataListResponse
import com.tokopedia.purchase_platform.features.cart.data.model.response.DeleteCartDataResponse
import com.tokopedia.purchase_platform.features.cart.data.model.response.UpdateCartDataResponse
import rx.Observable
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 2019-08-15.
 */

class CartRepository @Inject constructor(private val cartApi: CartApi) : ICartRepository {

    override fun getShopGroupList(param: Map<String, String>): Observable<CartDataListResponse> {
        return cartApi.getShopGroupList(param).map { cartResponseResponse ->
            cartResponseResponse.body()?.convertDataObj(CartDataListResponse::class.java)
        }
    }

    override fun deleteCartData(param: Map<String, String>): Observable<DeleteCartDataResponse> {
        return cartApi.postDeleteCart(param).map { cartResponseResponse ->
            cartResponseResponse.body()?.convertDataObj(DeleteCartDataResponse::class.java)
        }
    }

    override fun updateCartData(param: Map<String, String>): Observable<UpdateCartDataResponse> {
        return cartApi.postUpdateCart(param).map { cartResponseResponse ->
            cartResponseResponse.body()?.convertDataObj(UpdateCartDataResponse::class.java)
        }
    }

}