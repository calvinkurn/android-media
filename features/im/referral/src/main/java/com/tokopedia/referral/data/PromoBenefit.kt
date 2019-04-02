package com.tokopedia.referral.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PromoBenefit {

    @SerializedName("current_benefit")
    @Expose
    var currentBenefit: Int = 0

    @SerializedName("max_benefit")
    @Expose
    var maxBenefit: Int = 0
}
