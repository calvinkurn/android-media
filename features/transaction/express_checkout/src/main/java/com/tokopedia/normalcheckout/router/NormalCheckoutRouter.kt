package com.tokopedia.normalcheckout.router

import android.content.Context
import android.content.Intent
import com.tokopedia.transaction.common.sharedata.ShipmentFormRequest
import com.tokopedia.checkout.domain.usecase.AddInsuranceProductUsecase
import com.tokopedia.checkout.domain.usecase.GetInsuranceRecommendationUsecase

/**
 * Created by Irfan Khoirul on 12/12/18.
 */

interface NormalCheckoutRouter {

    fun addInsuranceProductToCart(): AddInsuranceProductUsecase
    fun getCheckoutIntent(context: Context, shipmentFormRequest: ShipmentFormRequest): Intent
    fun getCheckoutIntent(context: Context, deviceid: String): Intent
    fun getCartIntent(context: Context): Intent

}