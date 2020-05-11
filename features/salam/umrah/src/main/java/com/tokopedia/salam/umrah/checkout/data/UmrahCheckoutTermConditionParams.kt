package com.tokopedia.salam.umrah.checkout.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by firman on 27/11/2019
 */

class UmrahCheckoutTermConditionParams (
        @SerializedName("id")
        @Expose
        val id : String = ""
)