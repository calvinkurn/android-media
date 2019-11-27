package com.tokopedia.salam.umrah.checkout.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UmrahCheckoutTermConditionsEntity (
        @SerializedName("umrahTermsConditions")
        @Expose
        val umrahTermsConditions : List<UmrahTermCondition> = emptyList()
)

class UmrahTermCondition(
        @SerializedName("header")
        @Expose
        val header: String,
        @SerializedName("content")
        @Expose
        val content :String
)