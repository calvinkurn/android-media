package com.tokopedia.home.account.revamp.domain.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopOwnerDataModel (
    @SerializedName("is_gold_merchant")
    @Expose
    var goldMerchant: Boolean = false
)