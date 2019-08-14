package com.tokopedia.purchase_platform.features.express_checkout.router

import android.content.Context
import android.content.Intent
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.purchase_platform.common.data.model.request.checkout.CheckoutRequest
import com.tokopedia.purchase_platform.common.domain.model.CheckoutData
import com.tokopedia.usecase.RequestParams
import rx.Observable

/**
 * Created by Irfan Khoirul on 12/12/18.
 */

interface ExpressCheckoutRouter {

    fun getGeolocationIntent(context: Context, locationPass: LocationPass): Intent

    fun checkoutProduct(checkoutRequest: CheckoutRequest, isOneClickShipment: Boolean, isExpressCheckout: Boolean): Observable<CheckoutData>

    fun updateAddress(requestParams: RequestParams): Observable<String>

}