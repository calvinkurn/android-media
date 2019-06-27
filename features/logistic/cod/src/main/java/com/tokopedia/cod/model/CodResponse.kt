package com.tokopedia.cod.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by fajarnuha on 14/01/19.
 */
data class CodResponse(@SerializedName("checkout_cod")
                       @Expose
                       var checkoutCod: CheckoutCod? = null)