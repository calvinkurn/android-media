package com.tokopedia.checkout.data.repository

import com.tokopedia.checkout.data.model.response.saveshipmentstate.SaveShipmentStateResponse
import rx.Observable

/**
 * Created by Irfan Khoirul on 2019-08-15.
 */

interface ICheckoutRepository {

    fun saveShipmentState(params: Map<String, String>): Observable<SaveShipmentStateResponse>

}