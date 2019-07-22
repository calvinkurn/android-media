package com.tokopedia.normalcheckout.router

import android.content.Context
import android.content.Intent
import com.tokopedia.transaction.common.sharedata.ShipmentFormRequest
import com.tokopedia.transactiondata.insurance.usecase.AddInsuranceProductUsecase
import com.tokopedia.transactiondata.insurance.usecase.GetInsuranceRecommendationUsecase
import rx.Observable

/**
 * Created by Irfan Khoirul on 12/12/18.
 */

interface NormalCheckoutRouter {

    fun addInsuranceProductToCart(/*request: AddInsuranceProductToCartRequest, marketPlaceRequest: AddMarketPlaceToCartRequest*/): AddInsuranceProductUsecase
    fun getInsuranceRecommendationUsecase(): GetInsuranceRecommendationUsecase
    fun getCheckoutIntent(context: Context, shipmentFormRequest: ShipmentFormRequest): Intent
    fun getCheckoutIntent(context: Context, deviceid: String): Intent
    fun getCartIntent(context: Context): Intent

}