package com.tokopedia.purchase_platform.common.data.common.repository

import com.tokopedia.purchase_platform.common.data.common.api.CommonPurchaseApi
import com.tokopedia.purchase_platform.features.checkout.data.model.response.checkout.CheckoutDataResponse
import rx.Observable
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 2019-08-15.
 */

class CommonPurchaseRepository /*@Inject*/ constructor(val commonPurchaseApi: CommonPurchaseApi) : ICommonPurchaseRepository {

    override fun checkout(param: Map<String, String>): Observable<CheckoutDataResponse> {
        return commonPurchaseApi.checkout(param).map { cartResponseResponse ->
            cartResponseResponse.body()!!.convertDataObj(CheckoutDataResponse::class.java)
        }
    }

}