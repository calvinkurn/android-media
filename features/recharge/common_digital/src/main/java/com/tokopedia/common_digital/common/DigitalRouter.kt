package com.tokopedia.common_digital.common

import android.content.Intent

/**
 * Created by Rizky on 13/08/18.
 */
interface DigitalRouter {

    val intentDeepLinkHandlerActivity: Intent

    fun isSupportApplink(url: String): Boolean

    fun getGeneratedOverrideRedirectUrlPayment(url: String): String

    fun getGeneratedOverrideRedirectHeaderUrlPayment(urlFinal: String): Map<String, String>

    companion object {
        val PAYMENT_SUCCESS = 5
    }

}