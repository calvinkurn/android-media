package com.tokopedia.normalcheckout.router

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.transactiondata.entity.shared.checkout.CheckoutData
import com.tokopedia.transactiondata.entity.shared.expresscheckout.AtcRequestParam
import com.tokopedia.transaction.common.sharedata.AddToCartRequest
import com.tokopedia.transaction.common.sharedata.AddToCartResult
import com.tokopedia.transactiondata.entity.request.CheckoutRequest
import com.tokopedia.usecase.RequestParams
import rx.Observable
import java.security.PublicKey

/**
 * Created by Irfan Khoirul on 12/12/18.
 */

interface NormalCheckoutRouter {

    fun addToCartProduct(addToCartRequest: AddToCartRequest, isOneClickShipment: Boolean): Observable<AddToCartResult>
    fun getCheckoutIntent(context: Context): Intent
    fun getCartIntent(context: Context): Intent

}