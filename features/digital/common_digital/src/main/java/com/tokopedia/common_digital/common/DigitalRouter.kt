package com.tokopedia.common_digital.common

import android.content.Context
import android.content.Intent

import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData

import okhttp3.Interceptor

/**
 * Created by Rizky on 13/08/18.
 */
interface DigitalRouter {

    val chuckInterceptor: Interceptor

    val intentDeepLinkHandlerActivity: Intent

    fun getBooleanRemoteConfig(key: String, defaultValue: Boolean): Boolean

    fun isSupportApplink(url: String): Boolean

    fun getGeneratedOverrideRedirectUrlPayment(url: String): String

    fun getGeneratedOverrideRedirectHeaderUrlPayment(urlFinal: String): Map<String, String>

    fun instanceIntentCartDigitalProduct(passData: DigitalCheckoutPassData): Intent

    fun getHomeIntent(context: Context): Intent

    companion object {
        val REQUEST_CODE_CART_DIGITAL = 216
        val EXTRA_MESSAGE = "EXTRA_MESSAGE"
        val MULTICHECKOUT_CART_REMOTE_CONFIG = "mainapp_digital_enable_multicheckout_cart"
        val PAYMENT_SUCCESS = 5
        val EXTRA_APPLINK_FROM_PUSH: String ="applink_from_notif"
    }

}