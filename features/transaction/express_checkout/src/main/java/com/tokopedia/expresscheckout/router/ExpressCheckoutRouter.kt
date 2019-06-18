package com.tokopedia.expresscheckout.router

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.transactiondata.entity.shared.checkout.CheckoutData
import com.tokopedia.transactiondata.entity.shared.expresscheckout.AtcRequestParam
import com.tokopedia.transaction.common.sharedata.AddToCartRequest
import com.tokopedia.transaction.common.sharedata.AddToCartResult
import com.tokopedia.transaction.common.sharedata.ShipmentFormRequest
import com.tokopedia.transactiondata.entity.request.CheckoutRequest
import com.tokopedia.usecase.RequestParams
import rx.Observable
import java.security.PublicKey

/**
 * Created by Irfan Khoirul on 12/12/18.
 */

interface ExpressCheckoutRouter {

    fun getExpressCheckoutIntent(activity: Activity, atcRequestParam: AtcRequestParam): Intent

    fun getGeolocationIntent(context: Context, locationPass: LocationPass): Intent

    fun getCheckoutIntent(context: Context, shiomentFormRequest: ShipmentFormRequest): Intent

    fun addToCartProduct(addToCartRequest: AddToCartRequest, isOneClickShipment: Boolean): Observable<AddToCartResult>

    fun checkoutProduct(checkoutRequest: CheckoutRequest, isOneClickShipment: Boolean, isExpressCheckout: Boolean): Observable<CheckoutData>

    fun updateAddress(requestParams: RequestParams): Observable<String>

    fun checkoutModuleRouterGetEnableFingerprintPayment(): Boolean

    fun checkoutModuleRouterGeneratePublicKey(): PublicKey?

    fun checkoutModuleRouterGetPublicKey(publicKey: PublicKey): String

}