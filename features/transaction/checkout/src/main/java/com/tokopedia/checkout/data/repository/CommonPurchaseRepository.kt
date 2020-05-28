package com.tokopedia.checkout.data.repository

import com.tokopedia.checkout.data.api.CommonPurchaseAkamaiApi
import com.tokopedia.checkout.data.api.CommonPurchaseApi
import com.tokopedia.checkout.data.model.response.checkout.CheckoutResponse
import rx.Observable
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 2019-08-15.
 */

class CommonPurchaseRepository @Inject constructor(val commonPurchaseApi: CommonPurchaseApi, val commonPurchaseAkamaiApi: CommonPurchaseAkamaiApi) : ICommonPurchaseRepository {

    override fun checkout(param: Map<String, String>): Observable<CheckoutResponse> {
        return commonPurchaseAkamaiApi.checkout(param).map { cartResponseResponse ->
            cartResponseResponse.body()?.convertResponseObj(CheckoutResponse::class.java)
        }
    }

}