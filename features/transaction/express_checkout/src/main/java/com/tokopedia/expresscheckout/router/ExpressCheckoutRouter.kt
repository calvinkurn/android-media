package com.tokopedia.expresscheckout.router

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.transactiondata.entity.shared.checkout.CheckoutData
import com.tokopedia.transactiondata.entity.shared.expresscheckout.AtcRequestParam
import com.tokopedia.transaction.common.sharedata.AddToCartRequest
import com.tokopedia.transaction.common.sharedata.AddToCartResult
import com.tokopedia.transactiondata.entity.request.CheckoutRequest
import rx.Observable

/**
 * Created by Irfan Khoirul on 12/12/18.
 */

interface ExpressCheckoutRouter {

    fun getExpressCheckoutIntent(activity: Activity, atcRequestParam: AtcRequestParam): Intent

    fun getGeolocationIntent(context: Context, locationPass: LocationPass): Intent

    fun getCheckoutIntent(context: Context): Intent

    fun addToCartProduct(addToCartRequest: AddToCartRequest, isOneClickShipment: Boolean): Observable<AddToCartResult>

    fun getCheckoutObservable(checkoutRequest: CheckoutRequest, isOneClickShipment: Boolean, isExpressCheckout: Boolean): Observable<CheckoutData>

}